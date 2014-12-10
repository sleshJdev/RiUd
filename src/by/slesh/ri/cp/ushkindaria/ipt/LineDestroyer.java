/**
 * 
 */
package by.slesh.ri.cp.ushkindaria.ipt;

import java.awt.image.BufferedImage;

/**
 * @author slesh
 *
 */
public class LineDestroyer {
    public static BufferedImage destroyVertical(BufferedImage source) {
	return destroyHorizontal(source, 3, 0.8);
    }

    public static BufferedImage destroyVertical(BufferedImage source, int d,
	    double density) {
	int h = source.getHeight();
	int w = source.getWidth();
	int[] pixels = source.getRGB(0, 0, w, h, null, 0, w);
	
	for (int x = 0; x < w - d; ++x) {
	    double quantity = 0;
	    for (int k = x; k < x + d; ++k) {
		for (int y = 0; y < h; ++y) {
		    int index = w * y + k;
		    if (pixels[index] == Tool._1) {
			++quantity;
		    }
		}
	    }
	    if (quantity / (d * h) > density) {
		for (int i = x; i < x + d; ++i) {
		    for (int y = 0; y < h; ++y) {
			int index = w * y + i;
			pixels[index] = Tool._0;
		    }
		}
	    }
	}
	source.setRGB(0, 0, w, h, pixels, 0, w);
	return source;
    }

    public static BufferedImage destroyHorizontal(BufferedImage source) {
	return destroyHorizontal(source, 3, 0.8);
    }

    public static BufferedImage destroyHorizontal(BufferedImage source, int d,
	    double density) {
	int h = source.getHeight();
	int w = source.getWidth();
	int[] pixels = source.getRGB(0, 0, w, h, null, 0, w);

	for (int y = 0; y < h - d; ++y) {
	    double quantity = 0;
	    for (int k = y; k < y + d; ++k) {
		for (int x = 0; x < w; ++x) {
		    int index = w * k + x;
		    if (pixels[index] == Tool._1) {
			++quantity;
		    }
		}
	    }
	    if (quantity / (d * w) > density) {
		for (int i = y; i < y + d; ++i) {
		    for (int x = 0; x < w; ++x) {
			pixels[w * i + x] = Tool._0;
		    }
		}
	    }
	}
	source.setRGB(0, 0, w, h, pixels, 0, w);
	return source;
    }
}
