package by.slesh.ri.cp.ushkindaria.app.model;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import javax.imageio.ImageIO;

import by.slesh.ri.cp.ushkindaria.app.G;
import by.slesh.ri.cp.ushkindaria.ipt.LineDestroyer;
import by.slesh.ri.cp.ushkindaria.ipt.Recognizer;
import by.slesh.ri.cp.ushkindaria.ipt.Resizer;
import by.slesh.ri.cp.ushkindaria.ipt.Rotator;
import by.slesh.ri.cp.ushkindaria.ipt.Scanner;
import by.slesh.ri.cp.ushkindaria.ipt.Skeletonizator;
import by.slesh.ri.cp.ushkindaria.ipt.Tool;
import by.slesh.ri.cp.ushkindaria.ipt.binarization.Binarizator;
import by.slesh.ri.cp.ushkindaria.ipt.morph.AbstractMorph;
import by.slesh.ri.cp.ushkindaria.ipt.morph.TrainCleaner;
import by.slesh.ri.cp.ushkindaria.ipt.morph.WizardMorph;
import by.slesh.ri.cp.ushkindaria.ipt.segment.BugSegmentator;
import by.slesh.ri.cp.ushkindaria.ipt.segment.Contour;
import by.slesh.ri.cp.ushkindaria.ipt.segment.HistogramSegmentator;
import by.slesh.ri.cp.ushkindaria.ipt.segment.RelationSegmentator;
import by.slesh.ri.cp.ushkindaria.ipt.segment.SimpleSkeletonizator;

public class Model {

    private BufferedImage mSourceImage;
    private BufferedImage mTargetImage;
    private HistogramSegmentator histogramSegmentator = new HistogramSegmentator();
    private BugSegmentator bugSegmentator = new BugSegmentator();

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
	mTargetImage = Tool.trim(targetImage, 0, 0, 0, 0);
    }

    public void binarization() {
	mTargetImage = Binarizator.binarization(mTargetImage);
	mTargetImage = Rotator.rotate(mTargetImage);
	mTargetImage = Binarizator.binarization(mTargetImage);
	// mTargetImage = LineDestroyer.destroyHorizontal(mTargetImage, 2, 0.4);
    }

    public void skeletonization() {
	Skeletonizator skeletonizator = new Skeletonizator();
	mTargetImage = skeletonizator.skeletonization(mTargetImage);
    }

    public void lineDestroy() {
	mTargetImage = LineDestroyer.destroyHorizontal(mTargetImage);
    }

    public void histogramSegment() {
	mTargetImage = histogramSegmentator.segment(mTargetImage, true);
    }

    public void setSegmentThreshold(int value) {
	histogramSegmentator.setThreshold(value);
    }

    public void morph(AbstractMorph morph) {
	mTargetImage = morph.morph(mTargetImage);
    }

    private int yOLeter;

    public void extractAreaInterest() {
	int x1 = histogramSegmentator.getLeftBorder();
	int y1 = yOLeter + 25;

	int x2 = histogramSegmentator.getRightBorder();
	int y2 = y1 + 18;

	Contour contour = null;
	for (int y = yOLeter + WH + 5;; ++y) {
	    if (mTargetImage.getRGB(x1 + WH, y) == Tool._1) {
		bugSegmentator.setFrom(new Point(x1 + WW, y));
		contour = bugSegmentator.segment(mTargetImage);
		y2 = y;
		break;
	    } else {
		mTargetImage.setRGB(x1 + WW + 1, y, Color.GREEN.getRGB());
		mTargetImage.setRGB(x1 + WW, y, Color.GREEN.getRGB());
		mTargetImage.setRGB(x1 + WW - 1, y, Color.GREEN.getRGB());
	    }
	}

	contour.drawOnImage(mTargetImage);

	Point prev = null;
	for (Point curr : contour.getPath()) {
	    if (prev == null) prev = curr;
	    if (Math.abs(curr.y - prev.y) > 5) {
		mStartGroupNumberX = curr.x + 50;
		break;
	    }
	}

	y1 = y2- 20;

	Graphics g = mTargetImage.getGraphics();
	g.setColor(Color.BLUE);
	g.drawRect(mStartGroupNumberX, y1, x2 - mStartGroupNumberX, y2 - y1);

	mStartGroupNumberX -= x1;
	
	BufferedImage i = Rotator.rotate(mSourceImage, Rotator.sAngle);
	mTargetImage = Tool.cut(Tool.trim(i), x1, y1, x2, y2);
	mTargetImage = Resizer.scaleUp(mTargetImage, mTargetImage.getWidth(),
	        2 * mTargetImage.getHeight(), true);
    }

    private int mStartGroupNumberX;

    public void extractGroupNumber() {
	// mStartGroupNumberX =
	// Scanner.findWordGroup(Binarizator.binarization(mTargetImage));
	mTargetImage = Tool.cut(mTargetImage, mStartGroupNumberX, 0,
	        mTargetImage.getWidth(), mTargetImage.getHeight());
	save(mTargetImage);
	int old = Binarizator.sPercents;
	Binarizator.sPercents = 20;
	mTargetImage = Binarizator.binarization(mTargetImage);
	Binarizator.sPercents = old;
	save(mTargetImage);
	mTargetImage = Tool.centrain(mTargetImage);
	mTargetImage = LineDestroyer.destroyHorizontal(mTargetImage, 1, 0.4);
	mTargetImage = LineDestroyer.destroyVertical(mTargetImage, 1, 0.7);
	mTargetImage = Tool.centrain(mTargetImage);
//	 mTargetImage = WizardMorph.clear(mTargetImage);
    }

    private BufferedImage[] mDigits;

    public BufferedImage[] segmentGroupNumber() {
	mTargetImage = Tool.centrain(mTargetImage);
	RelationSegmentator rs = new RelationSegmentator(mTargetImage);
	mDigits = rs.segment();
	return mDigits;
    }

    public BufferedImage[] skeletonizationSegmentDigits() {
	// Skeletonizator skeletonizator = new Skeletonizator();
	BufferedImage[] digits = new BufferedImage[mDigits.length];
	for (int t = 0; t < mDigits.length; ++t) {
	    BufferedImage digit = Tool.trim(mDigits[t], 0, 0, 0, 0);
	    int h = digit.getHeight();
	    int w = digit.getWidth();
	    int[] pixels = digit.getRGB(0, 0, w, h, null, 0, w);
	    for (int k = 0; k < pixels.length; ++k) {
		if (pixels[k] != Tool._0) pixels[k] = Tool._1;
	    }
	    digit.setRGB(0, 0, w, h, pixels, 0, w);
	    digit = Resizer.scaleUp(digit, 40, 80, true);
	    digit = Binarizator.binByThreshold(digit, 100);
	    // digit = skeletonizator.skeletonization(digit);
	    digit = SimpleSkeletonizator.skeleton(digit);
	    digit = Tool.centrain1(digit);
	    digits[t] = TrainCleaner.remove(digit);
	    save(digits[t]);
	}
	mDigits = digits;
	return mDigits;
    }

    public BufferedImage[] recognizeNumber() {
	return Recognizer.recognize(mDigits);
    }

    int counter = 0;

    // public static void main(String[] args) {
    // try {
    // BufferedImage i = ImageIO.read(new File("d:\\t1.bmp"));
    // i = Binarizator.binarization(i);
    // ImageIO.write(i, "bmp", new File("d:\\1.bmp"));
    // Skeletonizator s = new Skeletonizator();
    // i = s.skeletonization(i);
    // ImageIO.write(i, "bmp", new File("d:\\2.bmp"));
    // i = Tool.centrain1(i);
    // ImageIO.write(i, "bmp", new File("d:\\3.bmp"));
    // BufferedImage[] is = Recognizer
    // .recognize(new BufferedImage[] { i });
    // ImageIO.write(is[0], "bmp", new File("d:\\t2.bmp"));
    // } catch (IOException e) {
    //
    // }
    // }

    private void save(BufferedImage source) {
	try {
	    ImageIO.write(source, "png", new File("d:\\image" + counter++
		    + ".png"));
	} catch (IOException e) {
	    e.printStackTrace();
	}
    }

    private int countBlackPixels(BufferedImage image) {
	int quantity = 0;
	for (int y = 0; y < image.getHeight(); ++y) {
	    for (int x = 0; x < image.getWidth(); ++x) {
		if (image.getRGB(x, y) == Tool._1) ++quantity;
	    }
	}
	return quantity;
    }

    public void findSymbols() {
	int h = mTargetImage.getHeight();
	int w = mTargetImage.getWidth();
	int[] pixels = mTargetImage.getRGB(0, 0, w, h, null, 0, w);

	List<Point> list = new ArrayList<Point>();
	int x0 = histogramSegmentator.getLeftBorder();

	Graphics g = mTargetImage.getGraphics();
	g.setColor(Color.BLUE);
	for (int y = 0; y < h - WH; ++y) {
	    for (int x = x0 + WW / 2; x < x0 + WW; ++x) {
		if (pixels[w * y + x] == Tool._1) {
		    int n = countBlackPixels(mTargetImage.getSubimage(x0, y,
			    WW, WH));
		    list.add(new Point(n, y));
		}
	    }
	}

	list.sort(new Comparator<Point>() {
	    @Override
	    public int compare(Point o1, Point o2) {
		return o2.x - o1.x;
	    }

	});

	Point[] array = new Point[list.size()];
	list.toArray(array);

	Point[] maximums = new Point[G.QUANTITY_NEURONS];
	maximums[0] = array[0];

	for (int k = 0; k < maximums.length - 1; ++k) {
	    int i = 0;
	    while (array[i++].y - maximums[k].y < WH) {
		if (i >= array.length) break;
	    }
	    maximums[k + 1] = array[i];
	}

	yOLeter = maximums[1].y;

	g.setColor(Color.RED);
	for (int k = 0; k < maximums.length; ++k) {
	    g.drawRect(x0, maximums[k].y, WW, WH);
	}
    }

    private static final int WH = 12;
    private static final int WW = 11;

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
