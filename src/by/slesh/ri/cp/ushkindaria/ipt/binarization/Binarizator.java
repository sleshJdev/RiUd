package by.slesh.ri.cp.ushkindaria.ipt.binarization;

import java.awt.image.BufferedImage;

import by.slesh.ri.cp.ushkindaria.ipt.Tool;

public class Binarizator extends Tool {

    public static int sPercents = 10;

    public static BufferedImage binarization(BufferedImage source) {
	int threshold = findThreshold(source);
	return bin(source, threshold);
    }

    private static BufferedImage bin(BufferedImage source, int threshold) {
	int w = source.getWidth();
	int h = source.getHeight();
	int[] sourceRgb = source.getRGB(0, 0, w, h, null, 0, w);
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
    private static int findThreshold(BufferedImage source) {

	double limit = sPercents / 100.0;
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
}
