package by.slesh.ri.cp.ushkindaria.ipt;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import by.slesh.ri.cp.ushkindaria.app.G;

public class HistogramSegmentator extends AbstractTool {

	private int[]			histogramX;
	private int[]			histogramY;

	private int				threshold	= G.INIT_SEGMENT_THRESHOLD;
	
	private List<Integer>	linesX		= new ArrayList<Integer>();
	private List<Integer>	linesY		= new ArrayList<Integer>();

	private int				w;
	private int				h;

	public int getThreshold() {

		return threshold;
	}

	public void setThreshold(int threshold) {

		this.threshold = threshold;
	}

	public int getXByLineNumber(int lineNumber) {

		if (lineNumber < 0 || lineNumber > linesX.size()) return -1;
		return linesX.get(lineNumber);
	}

	public int getYByLineNumber(int lineNumber) {

		if (lineNumber < 0 || lineNumber > linesY.size()) return -1;
		return linesY.get(lineNumber);
	}
	
	BufferedImage	source;

	public BufferedImage segment(BufferedImage source, boolean isDrawMarkers) {

		linesX.clear();
		linesY.clear();

		this.source = source;

		h = source.getHeight();
		w = source.getWidth();
		countHistogram(source.getRGB(0, 0, w, h, null, w, w), h, w);
		Graphics g = source.getGraphics();
		
		g.setColor(Color.RED);
		if (histogramX == null || histogramY == null) return null;
		for (int x = 1; x < histogramX.length; ++x) {
			int jump = Math.abs(histogramX[x] - histogramX[x - 1]);
			if (jump > 10) {
				if (isDrawMarkers) g.drawLine(x, 0, x, h);
				linesX.add(x);
				isDrawMarkers = false;
			}
		}

		g.setColor(Color.GREEN);
		for (int y = 1; y < histogramY.length; ++y) {
			int jump = Math.abs(histogramY[y] - histogramY[y - 1]);
			if (jump > threshold) {
				if (isDrawMarkers) g.drawLine(0, y, w, y);
				linesY.add(y);
			}
		}

		return source;
	}

	public int getLeftBorder() {

		if (linesX.size() == 0) return 0;
		for (int k = 1; k < linesX.size(); ++k) {
			int x = linesX.get(k);
			if (x > 5) return x;
		}
		return 0;
	}

	public int getRightBorder() {

		if (linesX.size() == 0) return 0;
		for (int k = linesX.size() - 1; k >= 1; --k) {
			int x = linesX.get(k);
			if (x < w - 10) return x;
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
