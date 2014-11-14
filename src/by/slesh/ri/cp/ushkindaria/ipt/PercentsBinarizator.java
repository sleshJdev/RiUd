package by.slesh.ri.cp.ushkindaria.ipt;

import java.awt.image.BufferedImage;

public class PercentsBinarizator extends AbstractTool {

	private int	percents	= 10;

	public BufferedImage binarization(BufferedImage source) {

		int threshold = findThreshold(source);
		int w = source.getWidth();
		int h = source.getHeight();
		int[] sourceRgb = source.getRGB(0, 0, w, h, null, w, w);
		for (int y = 0; y < h; ++y) {
			for (int x = 0; x < w; ++x) {
				int pos = w * y + x;
				int brightness = rgbToBrightness(sourceRgb[pos]);
				if (brightness > threshold) sourceRgb[pos] = _0;
				else sourceRgb[pos] = _1;
			}
		}
		return rgbToImage(sourceRgb, w, h);
	}

	/*
	 * Find threshold value for method 40 percents
	 */
	private int findThreshold(BufferedImage source) {

		double limit = percents / 100.0;
		double size = source.getWidth() * source.getHeight();
		int[] repeats = countBrightnessRepeats(source);
		double collector = 0;
		for (int brighness = 0; brighness < 256; ++brighness) {
			collector += repeats[brighness];
			double ratio = collector / size;
			if (ratio > limit) return --brighness;
		}
		return 0;
	}

	public void setPercents(int value) {

		this.percents = value;
	}

	public int getPercents() {

		return percents;
	}

}
