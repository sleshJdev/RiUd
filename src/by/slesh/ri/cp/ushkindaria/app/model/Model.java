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
import by.slesh.ri.cp.ushkindaria.ipt.Rotator;
import by.slesh.ri.cp.ushkindaria.ipt.Skeletonizator;
import by.slesh.ri.cp.ushkindaria.ipt.Tool;
import by.slesh.ri.cp.ushkindaria.ipt.binarization.Binarizator;
import by.slesh.ri.cp.ushkindaria.ipt.morph.AbstractMorph;
import by.slesh.ri.cp.ushkindaria.ipt.morph.WizardMorph;
import by.slesh.ri.cp.ushkindaria.ipt.segment.BugSegmentator;
import by.slesh.ri.cp.ushkindaria.ipt.segment.Contour;
import by.slesh.ri.cp.ushkindaria.ipt.segment.HistogramSegmentator;
import by.slesh.ri.cp.ushkindaria.ipt.segment.RelationSegmentator;
import by.slesh.ri.cp.ushkindaria.ipt.segment.SimpleSkeletonizator;

public class Model {

    private BufferedImage mSourceImage;
    private BufferedImage mTargetImage;
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
	mTargetImage = WizardMorph.clear(mTargetImage);
	mTargetImage = Binarizator.binarization(mTargetImage);
    }

    public void skeletonization() {
	Skeletonizator skeletonizator = new Skeletonizator();
	mTargetImage = skeletonizator.skeletonization(mTargetImage);
    }

    public void histogramSegment() {
	HistogramSegmentator histogramSegmentator = new HistogramSegmentator();
	histogramSegmentator.segment(mTargetImage, 20, 100, true);
	xleft = histogramSegmentator.getLeftBorder();
	xright = histogramSegmentator.getRightBorder();
	ytop = histogramSegmentator.getTopBorder();
	ybottom = histogramSegmentator.getBottomBorder();
    }

    int xleft, xright, ytop, ybottom;

    public BufferedImage trim() {
	mTargetImage = Tool.cut(mTargetImage, xleft, ytop, xright, ybottom);
	return mTargetImage;
    }

    public void morph(AbstractMorph morph) {
	mTargetImage = morph.morph(mTargetImage);
    }

    private int yOLeter;

    public void extractAreaInterest() {
	int y1 = yOLeter + WH + 10;
	int y2 = 0;

	int x1 = 0;
	int x2 = mTargetImage.getWidth();

	Contour contour = null;
	for (int y = yOLeter + WH + 5;; ++y) {
	    if (mTargetImage.getRGB(x1 + WH + 1, y) == Tool._1) {
		bugSegmentator.setFrom(new Point(x1 + WW, y));
		contour = bugSegmentator.segment(mTargetImage);
		y2 = y;
		break;
	    } else {
		mTargetImage.setRGB(x1 + WW + 1, y, Color.GREEN.getRGB());
	    }
	}

	contour.drawOnImage(mTargetImage);

	mX = 0;
	for (Point point : contour.getPath()) {
	    if (point.x > mX) {
		mX = point.x;
	    }
	}

	Graphics g = mTargetImage.getGraphics();
	g.setColor(Color.BLUE);
	g.drawRect(mX, y1, x2 - mX, y2 - y1);

	mY = contour.getMaxY();

	mTargetImage = Tool.cut(mTargetImage, mX + 180, y1,
	        mTargetImage.getWidth(), mY);
    }

    private int mY;
    private int mX;

    public void extractGroupNumber() {
	int old = Binarizator.sPercents;
	Binarizator.sPercents = 20;
	mTargetImage = Binarizator.binarization(mTargetImage);
	mTargetImage = WizardMorph.dilate(mTargetImage);
	Binarizator.sPercents = old;
	mTargetImage = Tool.centrain(mTargetImage);
	mTargetImage = LineDestroyer.destroyHorizontal(mTargetImage, 1, 0.5);
	mTargetImage = LineDestroyer.destroyVertical(mTargetImage, 1, 0.7);
	mTargetImage = Tool.centrain(mTargetImage);
    }

    private BufferedImage[] mDigits;

    public BufferedImage[] segmentGroupNumber() {
	mTargetImage = Tool.centrain(mTargetImage);
	RelationSegmentator rs = new RelationSegmentator(mTargetImage);
	mDigits = rs.segment();
	return mDigits;
    }
    
    public BufferedImage[] skeletonizationSegmentDigits() {
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
	    digit = SimpleSkeletonizator.skeleton(digit);
	    digit = Tool.centrain1(digit);
	    digits[t] = digit;
	    save(digit);
	}
	mDigits = digits;
	return mDigits;
    }

    public BufferedImage[] recognizeNumber() {
	return Recognizer.recognize(mDigits);
    }

    int counter = 0;

    private void save(BufferedImage source) {
	try {
	    ImageIO.write(source, "png", new File("d:\\" + counter++ + ".png"));
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

    private int countMomentOfInertia(BufferedImage image) {
	Point center = new Point(image.getWidth() / 2, image.getHeight() / 2);
	double moment = 0;
	for (int y = 0; y < image.getHeight(); ++y) {
	    for (int x = 0; x < image.getWidth(); ++x) {
		if (image.getRGB(x, y) == Tool._1) {
		    Point point = new Point(x, y);
		    moment += Math.pow(point.distance(center), 2);
		}
	    }
	}
	return (int) moment;
    }

    public void findSymbols() {
	int h = mTargetImage.getHeight();
	int w = mTargetImage.getWidth();
	int[] pixels = mTargetImage.getRGB(0, 0, w, h, null, 0, w);

	List<Point> list = new ArrayList<Point>();

	Graphics g = mTargetImage.getGraphics();
	g.setColor(Color.BLUE);
	for (int y = 0; y < h - WH; ++y) {
	    for (int x = 0; x < WW; ++x) {
		if (pixels[w * y + x] == Tool._1) {
		    BufferedImage i = mTargetImage.getSubimage(0, y, WW, WH);
		    double np = countBlackPixels(i);
		    if (np / WW / WH > 0.2) {
			int n = countMomentOfInertia(i);
			list.add(new Point(n, y));
			y += WH;
		    }
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

	yOLeter = array[0].y;
	g.setColor(Color.RED);
	for (int k = 0; k < G.QUANTITY_NEURONS; ++k) {
	    g.drawRect(0, array[k].y, WW, WH);
	}

	boolean isO = true;
	for (int x = WW / 3; x < 2 * WW / 3; ++x) {
	    for (int y = WH / 3; y < 2 * WH / 3; ++y) {
		if (mTargetImage.getRGB(x, array[0].y + y) == Tool._1) {
		    isO = false;
		}
	    }
	}
	if (isO) {
	    yOLeter = array[0].y;
	}

	isO = true;
	for (int x = WW / 3; x < 2 * WW / 3; ++x) {
	    for (int y = WH / 3; y < 2 * WH / 3; ++y) {
		if (mTargetImage.getRGB(x, array[1].y + y) == Tool._1) {
		    isO = false;
		}
	    }
	}
	if (isO) {
	    yOLeter = array[1].y;
	}
    }

    private static final int WH = 40;
    private static final int WW = 40;

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
