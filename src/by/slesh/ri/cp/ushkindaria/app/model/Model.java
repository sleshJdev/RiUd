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
    private int mTargetWidth = 2552;
    private int mTargetHeight = 3510;

    private BufferedImage mSourceImage;
    private BufferedImage mTargetImage;
    private BugSegmentator bugSegmentator = new BugSegmentator();

    public BufferedImage getSourceImage() {
	return mSourceImage;
    }

    public void setSourceImage(BufferedImage source) {
	mSourceImage = source;
	int h = mSourceImage.getHeight();
	int w = mSourceImage.getWidth();
	if (w < mTargetWidth)
	    mSourceImage = Resizer.scaleUp(mSourceImage, mTargetWidth, h, true);
	if (w > mTargetWidth)
	    mSourceImage = Resizer
	            .scaleLow(mSourceImage, mTargetWidth, h, true);
	w = mTargetWidth;
	if (h < mTargetHeight)
	    mSourceImage = Resizer
	            .scaleUp(mSourceImage, w, mTargetHeight, true);
	if (w > mTargetHeight)
	    mSourceImage = Resizer.scaleLow(mSourceImage, w, mTargetHeight,
	            true);
    }

    public BufferedImage getTargetImage() {
	return mTargetImage;
    }

    public void setTargetImage(BufferedImage targetImage) {
	mTargetImage = Tool.trim(targetImage, 0, 0, 0, 0);
    }

    public void binarization() {
	mTargetImage = Binarizator.binarization(mTargetImage);
	mTargetImage = WizardMorph.clear(mTargetImage);
	mTargetImage = Rotator.rotate(mTargetImage);
	mTargetImage = Binarizator.binarization(mTargetImage);
	mTargetImage = Tool.trim(mTargetImage, 50, 50, 50, 50);
    }

    public void skeletonization() {
	Skeletonizator skeletonizator = new Skeletonizator();
	mTargetImage = skeletonizator.skeletonization(mTargetImage);
    }

    public void histogramSegment() {
	HistogramSegmentator histogramSegmentator = new HistogramSegmentator();
	histogramSegmentator.segment(mTargetImage, 15, 100, true);
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

    private boolean isGroupleft() {
	int x = 850;
	int y = yOLeter + WH + 75;
	int w = 600;
	int h = 50;
	int q = countBlackPixels(mTargetImage.getSubimage(x, y, w, h));
	return q < 500;
    }

    private int yOLeter;

    public void extractAreaInterest() {
	int w = mTargetImage.getWidth();

	int y1 = yOLeter + WH + 10;
	int y2 = 0;

	int x1 = 10;
	int x2 = w;

	Contour contour = null;

	for (int y = y1;; ++y) {
	    if (mTargetImage.getRGB(x1, y) == Tool._1) {
		y2 = y;
		break;
	    } else {
		mTargetImage.setRGB(x1 + WW + 1, y, Color.GREEN.getRGB());
	    }
	}

	Graphics g = mTargetImage.getGraphics();

	if (isGroupleft()) {
	    int xLeft = x1;
	    int xRight = x1;
	    int yTop = y1;
	    int yLow = Integer.MIN_VALUE;
	    int y0 = y2 + 10;

	    for (int k = 0; k < 10; ++k) {
		xLeft = xRight;
		for (int x = xLeft; x < w; ++x) {
		    if (mTargetImage.getRGB(x, y0) == Tool._1) {
			xLeft = x - 1;
			break;
		    }
		}
		bugSegmentator.setFrom(new Point(xLeft, y0));
		contour = bugSegmentator.segment(mTargetImage);
		contour.drawOnImage(mTargetImage);
		xRight = contour.getMaxX();
		if (contour.getMaxY() > yLow) yLow = contour.getMaxY();
		int counter = 0;
		for (int x = xRight; x < w; ++x) {
		    if (mTargetImage.getRGB(x, y0) == Tool._0) ++counter;
		    else break;
		}
		if (counter > 20) break;
	    }

	    for (int x = xRight; x < w; ++x) {
		boolean isLowBotderFound = false;
		for (int y = y0; y <= yLow; ++y)
		    if (mTargetImage.getRGB(x, y) == Tool._1) {
			isLowBotderFound = true;
			yLow = y;
			break;
		    }

		if (isLowBotderFound) {
		    xLeft = x + 1;
		    break;
		}
	    }

	    yLow -= 5;
	    yTop = yLow - 70;

	    g.setColor(Color.BLUE);
	    g.drawRect(xLeft, yTop, w, yLow - yTop);

	    mTargetImage = Tool.cut(mTargetImage, xLeft, yTop, w, yLow);
	} else {
	    bugSegmentator.setFrom(new Point(x1, y2));
	    contour = bugSegmentator.segment(mTargetImage);
	    contour.drawOnImage(mTargetImage);

	    x1 = contour.getMaxX();
	    y2 = contour.getMaxY();

	    y2 -= 5;

	    g.setColor(Color.BLUE);
	    g.drawRect(x1, y1, x2 - x1, y2 - y1);
	    mTargetImage = Tool.cut(mTargetImage, x1 + 180, y1, x2, y2);
	}
    }

    public void extractGroupNumber() {
	int old = Binarizator.sPercents;
	Binarizator.sPercents = 20;
	mTargetImage = Binarizator.binarization(mTargetImage);
	// mTargetImage = WizardMorph.erode(mTargetImage);
	// mTargetImage = WizardMorph.dilate(mTargetImage);
	Binarizator.sPercents = old;
	mTargetImage = Tool.centrain(mTargetImage);
	mTargetImage = LineDestroyer.destroyHorizontal(mTargetImage, 1, 0.4);
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
	for (int y = WH; y < h - 2 * WH; ++y) {
	    for (int x = 0; x < WW; ++x) {
		if (pixels[w * y + x] == Tool._1) {
		    BufferedImage i = mTargetImage.getSubimage(0, y, WW, WH);
		    double np = countBlackPixels(i);
		    if (np / WW / WH > 0.2) {
			int n = countMomentOfInertia(i);
			list.add(new Point(n, y));
			System.out.println(new Point(n, y));
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
	int counter = 0;
	for (int x = WW / 3; x < 2 * WW / 3; ++x) {
	    for (int y = WH / 3; y < 2 * WH / 3; ++y) {
		if (mTargetImage.getRGB(x, array[1].y + y) == Tool._1) {
		    ++counter;
		}
		if (counter > 10) isO = false;
	    }
	}
	if (isO) {
	    yOLeter = array[1].y;
	}

	g.setColor(Color.RED);
	for (int k = 0; k < G.QUANTITY_NEURONS; ++k) {
	    if (yOLeter == array[k].y) {
		g.setColor(Color.BLUE);
		g.fillRect(0, array[k].y - 20, WW, 15);
	    } else {
		g.setColor(Color.RED);
		g.drawRect(0, array[k].y, WW, WH);
	    }
	}

	save(mTargetImage);
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
