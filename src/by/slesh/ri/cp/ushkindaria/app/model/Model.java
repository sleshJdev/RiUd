package by.slesh.ri.cp.ushkindaria.app.model;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;

import javax.imageio.ImageIO;

import by.slesh.ri.cp.ushkindaria.app.G;
import by.slesh.ri.cp.ushkindaria.ipt.LineDestroyer;
import by.slesh.ri.cp.ushkindaria.ipt.Resizer;
import by.slesh.ri.cp.ushkindaria.ipt.Rotator;
import by.slesh.ri.cp.ushkindaria.ipt.Scanner;
import by.slesh.ri.cp.ushkindaria.ipt.Skeletonizator;
import by.slesh.ri.cp.ushkindaria.ipt.Tool;
import by.slesh.ri.cp.ushkindaria.ipt.binarization.Binarizator;
import by.slesh.ri.cp.ushkindaria.ipt.morph.AbstractMorph;
import by.slesh.ri.cp.ushkindaria.ipt.morph.ErodeMorph;
import by.slesh.ri.cp.ushkindaria.ipt.morph.WizardMorph;
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
	FinderPerseptron.getInstance().teach(2);

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
	mTargetImage = Tool.trim(targetImage, 0, 0, 0, 0);
    }

    public void binarization() {
	mTargetImage = Binarizator.binarization(mTargetImage);
	mTargetImage = Rotator.rotate(mTargetImage);
	mTargetImage = Binarizator.binarization(mTargetImage);
    }

    public void skeletonization() {
	mTargetImage = skeletonizator.skeletonization(mTargetImage);
    }

    public void lineDestroy() {
	mTargetImage = LineDestroyer.destroy(mTargetImage);
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

    public void extractAreaInterest() {
	int x1 = histogramSegmentator.getLeftBorder();
	int y1 = mYCoords[2] + 25;

	int x2 = histogramSegmentator.getRightBorder();
	int y2 = y1 + 17;

	mTargetImage = Tool.cut(Tool.trim(mSourceImage), x1, y1, x2, y2);
	mTargetImage = Resizer.scaleUp(mTargetImage, mTargetImage.getWidth(),
	        2 * mTargetImage.getHeight(), true);
    }

    public void extractGroupNumber() {
	int x0 = Scanner.findWordGroup(Binarizator.binarization(mTargetImage));
	mTargetImage = Tool.cut(mTargetImage, x0, 0, mTargetImage.getWidth(),
	        mTargetImage.getHeight());

	int old = Binarizator.sPercents;
	Binarizator.sPercents = 15;
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

    int counter = 0;

    private void save(BufferedImage source) {
	try {
	    ImageIO.write(source, "png", new File("d:\\image" + counter++
		    + ".png"));
	} catch (IOException e) {
	    e.printStackTrace();
	}
    }

    private int[] mYCoords = new int[G.QUANTITY_NEURONS_FOR_FINDER];
    private double[] mCurrentOut = new double[G.QUANTITY_NEURONS_FOR_FINDER];

    private static final int WH = 12;
    private static final int WW = 11;

    boolean f = true;

    public void neuralRecognize(int x, int y) {
	BufferedImage bi = Tool.cut(mTargetImage, x, y, x + WW, y + WH);
//	if (f) {
	    bi = Resizer.scaleUp(bi, G.WIDTH_FOR_FINDER, G.HEIGHT_FOR_FINDER,
		    true);
	    bi = Binarizator.binByThreshold(bi);
	    bi = WizardMorph.erode(bi);
	    save(bi);
	    f = false;
//	}
	Tool.centrain(bi);
	int[] input = Helpers.initX(bi);
	int[] output = FinderPerseptron.getInstance().recognize(input);
	for (int k = 0; k < output.length; ++k) {
	    if (output[k] > mCurrentOut[k]) {
		mCurrentOut[k] = output[k];
		mYCoords[k] = y;
	    }
	}
    }

    public void detect() {
	int h = mTargetImage.getHeight();
	int w = mTargetImage.getWidth();
	int[] pixels = mTargetImage.getRGB(0, 0, w, h, null, 0, w);

	Arrays.fill(mYCoords, 0);
	Arrays.fill(mCurrentOut, 0);

	int x0 = histogramSegmentator.getLeftBorder();
	Graphics g = mTargetImage.getGraphics();
	g.setColor(Color.BLUE);
	for (int y = 0; y < h - WH; ++y) {
	    for (int x = x0 + WW / 2; x < x0 + WW; ++x) {
		if (pixels[w * y + x] == Tool._1) {
		    // g.drawRect(x0, y, WW, WH);
		    neuralRecognize(x0, y);
//		    y += WH;
		}
	    }
	}

	g.setColor(Color.RED);
	for (int k = 0; k < G.QUANTITY_NEURONS_FOR_FINDER; ++k) {
	    int y = mYCoords[k];
	    g.drawString(FinderPerseptron.getInstance().getNameById(k),
		    x0 - 20, y + 20);
	    g.drawRect(x0, y, WW, WH);
	}
    }

    public void checkOrientation() {
	if (mSourceImage.getWidth() > mSourceImage.getHeight()) {
	    mSourceImage = Tool.rotate(mSourceImage, 90);
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
