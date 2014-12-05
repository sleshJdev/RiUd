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
import java.io.File;
import java.io.IOException;
import java.util.Arrays;

import javax.imageio.ImageIO;

import by.slesh.ri.cp.ushkindaria.app.G;
import by.slesh.ri.cp.ushkindaria.ipt.Resizer;
import by.slesh.ri.cp.ushkindaria.ipt.Scanner;
import by.slesh.ri.cp.ushkindaria.ipt.Skeletonizator;
import by.slesh.ri.cp.ushkindaria.ipt.Tool;
import by.slesh.ri.cp.ushkindaria.ipt.binarization.Binarizator;
import by.slesh.ri.cp.ushkindaria.ipt.morph.AbstractMorph;
import by.slesh.ri.cp.ushkindaria.ipt.segment.BugSegmentator;
import by.slesh.ri.cp.ushkindaria.ipt.segment.Contour;
import by.slesh.ri.cp.ushkindaria.ipt.segment.HistogramSegmentator;
import by.slesh.ri.cp.ushkindaria.ipt.segment.RelationSegmentator;
import by.slesh.ri.cp.ushkindaria.nn.FinderPerseptron;
import by.slesh.ri.cp.ushkindaria.nn.Helpers;
import by.slesh.ri.cp.ushkindaria.nn.RecognizerPerseptron;

public class Model {

    private BufferedImage mSourceImage;
    private BufferedImage mTargetImage;
    private Skeletonizator skeletonizator = new Skeletonizator();
    private HistogramSegmentator histogramSegmentator = new HistogramSegmentator();
    private BugSegmentator bugSegmentator = new BugSegmentator();

    public Model() {
	FinderPerseptron.initInstance(G.QUANTITY_NEURONS_FOR_FINDER,
	        G.QUANTITY_INPUTS_FOR_FINDER);
	FinderPerseptron.getInstance().teach(6);

	RecognizerPerseptron.initInstance(G.QUANTITY_NEURONS_FOR_RECOGNIZER,
	        G.QUANTITY_INPUTS_FOR_RECOGNIZER);
	RecognizerPerseptron.getInstance().teach(1);
    }

    public BufferedImage getSourceImage() {
	return mSourceImage;
    }

    public void setSourceImage(BufferedImage sourceFrame) {
	mSourceImage = sourceFrame;
    }

    public BufferedImage getTargetImage() {
	return mTargetImage;
    }

    public void setTargetImage(BufferedImage targetImage) {
	mTargetImage = trim(targetImage, 0, 0, 0, 0);
    }

    public void binarization() {
	mTargetImage = Binarizator.binarization(mTargetImage);
    }

    public void skeletonization() {
	mTargetImage = skeletonizator.skeletonization(mTargetImage);
    }

    public void histogramSegment() {
	mTargetImage = histogramSegmentator.segment(trim(mTargetImage, 0, 0, 0,
	        0));
    }

    public void setSegmentThreshold(int value) {
	histogramSegmentator.setThreshold(value);
    }

    public void morph(AbstractMorph morph) {
	mTargetImage = morph.morph(mTargetImage);
    }

    private static final int WH = G.HEIGHT_FOR_FINDER; // window height. neural
	                                               // network
    // scan size
    private static final int WW = G.WIDTH_FOR_FINDER; // window width. neural
	                                              // network scan

    private int findY() {
	int h = mTargetImage.getHeight();
	int w = mTargetImage.getWidth();
	int[] pixels = mTargetImage.getRGB(0, 0, w, h, null, w, w);

	int x0 = histogramSegmentator.getLeftBorder();
	for (int y = 0; y < h - WH; ++y) {
	    double quantity = 0;
	    for (int i = 0; i < WH; ++i) {
		for (int j = 0; j < WW; ++j) {
		    int r = y + i;
		    int c = x0 + j;
		    int index = w * r + c;
		    if (index >= pixels.length) continue;
		    if (pixels[index] == Tool._1) {
			++quantity;
		    }
		}
	    }
	    double density = quantity / (WW * WH);
	    if (density > 0.4) return y;
	}
	return 0;
    }

    public void extractAreaInterest() {
	int x1 = histogramSegmentator.getLeftBorder();
	// int y1 = mYCoords[0] + 25;
	int y1 = findY() + 25;

	int x2 = histogramSegmentator.getRightBorder();
	int y2 = y1 + 20;

	mTargetImage = cut(trim(mSourceImage), x1, y1, x2, y2);
	mTargetImage = Resizer.scaleUp(mTargetImage, mTargetImage.getWidth(),
	        2 * mTargetImage.getHeight(), true);
    }

    public void extractGroupNumber() {
	int x0 = Scanner.findWordGroup(Binarizator.binarization(mTargetImage));
	mTargetImage = cut(mTargetImage, x0, 0, mTargetImage.getWidth(),
	        mTargetImage.getHeight());

	// mTargetImage = Resizer
	// .scaleUp(mTargetImage, 3 * mTargetImage.getWidth(),
	// 3 * mTargetImage.getHeight(), true);

	int old = Binarizator.sPercents = 10;
	mTargetImage = Binarizator.binarization(mTargetImage);
	Binarizator.sPercents = old;
    }

    public BufferedImage[] segmentGroupNumber() {
	RelationSegmentator rs = new RelationSegmentator(mTargetImage);
	BufferedImage[] digits = rs.segment();
	for (int k = 0; k < digits.length; ++k) {
	    digits[k] = Resizer.scaleUp(digits[k], 100, 100, true);
	}
	return digits;
    }

    public BufferedImage[] recognizeNumber() {
	return null;
    }

    private void save(BufferedImage source) {
	try {
	    ImageIO.write(source, "bmp", new File("image.bmp"));
	} catch (IOException e) {
	    e.printStackTrace();
	}
    }

    public void detect() {
	int h = mTargetImage.getHeight();
	int w = mTargetImage.getWidth();
	int[] pixels = mTargetImage.getRGB(0, 0, w, h, null, w, w);

	Arrays.fill(mYCoords, 0);
	Arrays.fill(mCurrentOut, 0);

	int x0 = histogramSegmentator.getLeftBorder();
	Graphics g = mTargetImage.getGraphics();
	g.setColor(Color.BLUE);
	for (int y = 0; y < h; ++y) {
	    for (int x = x0; x < x0 + G.WIDTH_FOR_FINDER; ++x) {
		if (pixels[w * y + x] == Tool._1) {
		    g.drawRect(x0, y - 2, WW, WH);
		    if (y + WH <= h) neuralRecognize(x0, y - 2);
		    y += WH;
		}
	    }
	}

	g.setColor(Color.RED);
	for (int k = 0; k < G.QUANTITY_NEURONS_FOR_FINDER; ++k) {
	    int y = mYCoords[k];
	    g.drawString(Integer.toString(k + 1), x0 - 20, y + 20);
	    g.drawRect(x0, y, WW, WH);
	}
    }

    public void checkOrientation() {
	if (mSourceImage.getWidth() > mSourceImage.getHeight()) {
	    mSourceImage = rotate(mSourceImage, 90);
	}
    }

    /**
     * @param source
     *            image to ratate
     * @param angle
     *            angel in degrees
     */
    private BufferedImage rotate(BufferedImage source, int angle) {
	BufferedImage target = new BufferedImage(source.getWidth(null),
	        source.getHeight(null), Tool.IMAGE_TYPE);
	Graphics2D g = (Graphics2D) target.getGraphics();
	g.drawImage(source, 0, 0, null);
	AffineTransform at = new AffineTransform();
	at.scale(1.0, 1.0);
	at.rotate(angle * Math.PI / 180.0, target.getWidth() / 2.0,
	        target.getHeight() / 2.0);
	AffineTransform translationTransform = findTranslation(at, target);
	at.preConcatenate(translationTransform);
	BufferedImageOp bio = new AffineTransformOp(at,
	        AffineTransformOp.TYPE_BILINEAR);
	return bio.filter(target, null);
    }

    /*
     * Find proper translations to keep rotated image correctly displayed
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
     * yCoords[0] - 1 yCoords[1] - 2 yCoords[2] - 3 yCoords[3] - 4 yCoords[4] -
     * O
     */
    private int[] mYCoords = new int[G.QUANTITY_NEURONS_FOR_FINDER];
    private double[] mCurrentOut = new double[G.QUANTITY_NEURONS_FOR_FINDER];

    public void neuralRecognize(int x, int y) {
	BufferedImage bi = mTargetImage.getSubimage(x, y, WW, WH);
	int[] input = Helpers.initX(bi);
	int[] output = FinderPerseptron.getInstance().recognize(input);
	for (int k = 0; k < output.length; ++k) {
	    if (output[k] > mCurrentOut[k]) {
		mCurrentOut[k] = output[k];
		mYCoords[k] = y;
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

    public BufferedImage trim(BufferedImage source) {
	return trim(source, 150, 40, 0, 40);
    }

    private BufferedImage cut(BufferedImage source, int x1, int y1, int x2,
	    int y2) {

	BufferedImage target = new BufferedImage(x2 - x1, y2 - y1,
	        Tool.IMAGE_TYPE);
	Graphics g = target.getGraphics();
	g.drawImage(source, 0, 0, x2 - x1, y2 - y1, x1, y1, x2, y2, null);
	return target;
    }

    private BufferedImage trim(BufferedImage source, int indentTop,
	    int indentRight, int indentBottom, int indentLeft) {
	int x1 = indentLeft;
	int y1 = indentTop;
	int x2 = source.getWidth() - indentRight;
	int y2 = source.getHeight() - indentBottom;
	return cut(source, x1, y1, x2, y2);
    }
}
