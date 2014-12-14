package by.slesh.ri.cp.ushkindaria.ipt.segment;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import by.slesh.ri.cp.ushkindaria.ipt.Tool;

public class HistogramSegmentator extends Tool {

    private int[] histogramX;
    private int[] histogramY;

    private List<Integer> linesX = new ArrayList<Integer>();
    private List<Integer> linesY = new ArrayList<Integer>();

    private int mWidth;
    private int mHeight;

    public int getXByLineNumber(int lineNumber) {

	if (lineNumber < 0 || lineNumber > linesX.size()) return -1;
	return linesX.get(lineNumber);
    }

    public int getYByLineNumber(int lineNumber) {

	if (lineNumber < 0 || lineNumber > linesY.size()) return -1;
	return linesY.get(lineNumber);
    }

    BufferedImage source;

    public BufferedImage segment(BufferedImage source, int xThreshold, int yThreshold, boolean isDrawLine) {
	linesX.clear();
	linesY.clear();

	this.source = source;

	mHeight = source.getHeight();
	mWidth = source.getWidth();
	countHistogram(source.getRGB(0, 0, mWidth, mHeight, null, mWidth, mWidth), mHeight, mWidth);
	Graphics g = source.getGraphics();

	g.setColor(Color.RED);
	if (histogramX == null || histogramY == null) return null;
	for (int x = 2; x < histogramX.length; ++x) {
	    int jump = Math.abs(histogramX[x] - histogramX[x - 1]);
	    if (jump > xThreshold) {
		linesX.add(x);
	    }
	}

	if (isDrawLine) {
	    g.drawLine(getLeftBorder(), 0, getLeftBorder(), mHeight);
	    g.drawLine(getRightBorder(), 0, getRightBorder(), mHeight);
	}

	g.setColor(Color.GREEN);
	for (int y = 1; y < histogramY.length; ++y) {
	    int jump = Math.abs(histogramY[y] - histogramY[y - 1]);
	    if (jump > 20) {
		linesY.add(y);
	    }
	}
	
	if (isDrawLine) {
	    g.drawLine(0, getTopBorder(), mWidth, getTopBorder());
	    g.drawLine(0, getBottomBorder(), mWidth, getBottomBorder());
	}

	return source;
    }

    public int getLeftBorder() {
	if (linesX.size() == 0) return 0;
	for (int k = 1; k < linesX.size(); ++k) {
	    int x = linesX.get(k);
	    if (x > 50) return x;
	}
	return 0;
    }

    public int getRightBorder() {
	if (linesX.size() == 0) return 0;
	for (int k = linesX.size() - 1; k >= 1; --k) {
	    int x = linesX.get(k);
	    if (x < mWidth - 50) return x;
	}
	return 0;
    }

    public int getTopBorder() {
	if (linesY.size() == 0) return 0;
	for (int k = 1; k < linesY.size(); ++k) {
	    int y = linesY.get(k);
	    if (y > 50) return y;
	}
	return 0;
    }

    public int getBottomBorder() {
	if (linesY.size() == 0) return 0;
	for (int k = linesY.size() - 1; k >= 1; --k) {
	    int y = linesY.get(k);
	    if (y < mHeight - 50) return y;
	}
	return 0;
    }
    
    private void countHistogram(int[] rgb, int h, int w) {

	histogramX = new int[w];
	histogramY = new int[h];
	for (int y = 0; y < h; ++y) {
	    for (int x = 0; x < w; ++x) {
		if (rgb[y * w + x] == _1) {
		    ++histogramX[x];
		    ++histogramY[y];
		}
	    }
	}
    }
}
