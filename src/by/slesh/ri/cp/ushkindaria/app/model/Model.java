package by.slesh.ri.cp.ushkindaria.app.model;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.util.Arrays;

import by.slesh.ri.cp.ushkindaria.ipt.AbstractMorph;
import by.slesh.ri.cp.ushkindaria.ipt.AbstractTool;
import by.slesh.ri.cp.ushkindaria.ipt.BugSegmentator;
import by.slesh.ri.cp.ushkindaria.ipt.Contour;
import by.slesh.ri.cp.ushkindaria.ipt.DilateMorph;
import by.slesh.ri.cp.ushkindaria.ipt.HistogramSegmentator;
import by.slesh.ri.cp.ushkindaria.ipt.PercentsBinarizator;
import by.slesh.ri.cp.ushkindaria.ipt.Skeletonizator;
import by.slesh.ri.cp.ushkindaria.nn.NeuralNetwork;

public class Model {

	private BufferedImage			sourceImage;
	private BufferedImage			mTargetImage;
	private PercentsBinarizator		binarizator				= new PercentsBinarizator();
	private Skeletonizator			skeletonizator			= new Skeletonizator();
	private HistogramSegmentator	histogramSegmentator	= new HistogramSegmentator();
	private BugSegmentator			bugSegmentator			= new BugSegmentator();

	public BufferedImage getSourceImage() {

		return sourceImage;
	}

	public void setSourceImage(BufferedImage sourceFrame) {

		this.sourceImage = sourceFrame;
	}

	public BufferedImage getTargetImage() {

		return mTargetImage;
	}

	public void setTargetImage(BufferedImage targetImage) {

		mTargetImage = trim(targetImage, 0, 0, 0, 0);
	}
	
	public void binarization() {

		mTargetImage = binarizator.binarization(mTargetImage);
	}

	public void skeletonization() {

		mTargetImage = skeletonizator.skeletonization(mTargetImage);
	}

	public void histogramSegment(boolean isDrawLine) {

		mTargetImage = histogramSegmentator.segment(mTargetImage, isDrawLine);
	}

	public void setPercents(int value) {

		binarizator.setPercents(value);
	}

	public void setSegmentThreshold(int value) {

		histogramSegmentator.setThreshold(value);
	}

	public void morph(AbstractMorph morph) {

		mTargetImage = morph.morph(mTargetImage);
	}
	
	private static final int WH = 12; // window height. neural network scan size
	private static final int WW = 12; // window width. neural network scan size
	
	public void extract(){

		mTargetImage = trim(sourceImage);
		binarization();
		morph(new DilateMorph());
//		skeletonization();
		histogramSegment(true);
		detect();

		int x1 = histogramSegmentator.getLeftBorder();
		int y1 = yCoords[4] + 25;
		
		int x2 = histogramSegmentator.getRightBorder();
		int y2 = y1 + 20;
		
		mTargetImage = cut(trim(sourceImage), x1, y1, x2, y2);
	}
	
	
	public BufferedImage cut(BufferedImage source, int x1, int y1, int x2, int y2){
		
		BufferedImage target = new BufferedImage(x2 - x1, y2 - y1, AbstractTool.IMAGE_TYPE);
		Graphics g = target.getGraphics();
		g.drawImage(source, 0, 0, x2 - x1, y2 - y1, x1, y1, x2, y2, null);
		return target;
	}
	
	public BufferedImage trim(BufferedImage source) {

		return trim(source, 150, 40, 0, 40);
	}

	public BufferedImage trim(BufferedImage source, 
			int indentTop, int indentRight, int indentBottom, int indentLeft) {

		int x1 = indentLeft;
		int y1 = indentTop;
		int x2 = source.getWidth() - indentRight;
		int y2 = source.getHeight() - indentBottom;
		return cut(source, x1, y1, x2, y2);
	}

	private static final String	PATH	= "resources/touch/nw/nw.nw";
	private NeuralNetwork		nn		= new NeuralNetwork(PATH);

	public void detect() {

		int h = mTargetImage.getHeight();
		int w = mTargetImage.getWidth();
		int[] pixels = mTargetImage.getRGB(0, 0, w, h, null, w, w);
		 
		Arrays.fill(yCoords, 0);
		Arrays.fill(currentOut, 0);
		
		int x0 = histogramSegmentator.getLeftBorder();
		Graphics g = mTargetImage.getGraphics();
		g.setColor(Color.BLUE);
		for (int y = 0; y < h; ++y)
			for (int x = x0; x < x0 + WW; ++x)
				if (pixels[w * y + x] == AbstractTool._1)
					if (y + WH <= h) neuralRecognize(pixels, w, h, x0, y);
		
		g.setColor(Color.RED);
		for (int k = 0; k < 5; ++k) {
			int y = yCoords[k];
			g.drawString(Integer.toString(k +  1), x0 - 20, y);
			g.drawRect(x0, y, WW, WW);
		}
	}

	public void checkOrientation() {

		if (sourceImage.getWidth() > sourceImage.getHeight()) {
			sourceImage = rotate(sourceImage, 90);
		}
	}

	/**
	 * @param source
	 *            image to ratate
	 * @param angle
	 *            angel in degrees
	 */
	private BufferedImage rotate(BufferedImage source, int angle){
		
		BufferedImage target = new BufferedImage(source.getWidth(null), source.getHeight(null), AbstractTool.IMAGE_TYPE);
		Graphics2D g = (Graphics2D) target.getGraphics();
	    g.drawImage(source, 0, 0, null);
		AffineTransform at = new AffineTransform();
		at.scale(1.0, 1.0);
		at.rotate(angle * Math.PI / 180.0, target.getWidth() / 2.0, target.getHeight() / 2.0);
	    AffineTransform translationTransform = findTranslation(at, target);
	    at.preConcatenate(translationTransform);
	    BufferedImageOp bio = new AffineTransformOp(at, AffineTransformOp.TYPE_BILINEAR);
	    return bio.filter(target, null);
	}
	
	/*
	 * find proper translations to keep rotated image correctly displayed
	 */
	private AffineTransform findTranslation(AffineTransform at, BufferedImage bi) {

		Point2D p2din, p2dout;

		p2din = new Point2D.Double(0.0, 0.0);
		p2dout = at.transform(p2din, null);
		double ytrans = p2dout.getY();

		p2din = new Point2D.Double(0, bi.getHeight());
		p2dout = at.transform(p2din, null);
		double xtrans = p2dout.getX();

		AffineTransform tat = new AffineTransform();
		tat.translate(-xtrans, -ytrans);
		return tat;
	}


	/*
	 * yCoords[0] - 1
	 * yCoords[1] - 2
	 * yCoords[2] - 3
	 * yCoords[3] - 4
	 * yCoords[4] - O
	 */
	int[]		yCoords		= new int[5];
	double[]	currentOut	= new double[5];

	public void neuralRecognize(int[] pixels, int w, int h, int x, int y) {

		double[] in = new double[nn.getInQuatity()];
		for (int k = 0, i = 0; i < WH; ++i) {
			for (int j = 0; j < WW; ++j) {
				int pos = w * (y + i) + (x + i);
				if (pixels[pos] == AbstractTool._1) in[k++] = -0.5;
				else in[k++] = 0.5;
			}
		}

		double[] out = new double[5];
		nn.netOut(in, out);

		for (int k = 0; k < 5; ++k) {
			if (out[k] > currentOut[k]) {
				if (k == 4 && y > yCoords[k] && yCoords[k] > 0) continue;
				currentOut[k] = out[k];
				yCoords[k] = y;
			}
		}
	}

	public void bugSegment() {

		Contour contour = bugSegmentator.segment(mTargetImage);
		mTargetImage = contour.sub(mTargetImage);
	}

	public void setStartPointForBugSegmentator(Point point) {

		bugSegmentator.setFrom(point);
		Graphics g = mTargetImage.getGraphics();
		g.setColor(Color.BLUE);
		g.fillOval(point.x - 5, point.y - 5, 10, 10);
	}
}
