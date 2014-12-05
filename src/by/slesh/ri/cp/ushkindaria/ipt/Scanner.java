/**
 * 
 */
package by.slesh.ri.cp.ushkindaria.ipt;

import java.awt.image.BufferedImage;

/**
 * @author slesh
 *
 */
public class Scanner {

    public static int findWordGroup(BufferedImage source) {
	final int WIDTH = 43;
	final int HEIGHT = 8;
	int h = source.getHeight();
	int w = source.getWidth();
	int[] pixels = source.getRGB(0, 0, w, h, null, 0, w);
	int currentX = 0;
	double currentDensity = 0;
	for (int y = 0; y < h - HEIGHT; ++y) {
	    for (int x = 0; x < w - WIDTH; ++x) {
		double quantity = 0;
		for (int i = 0; i < HEIGHT; ++i) {
		    for (int j = 0; j < WIDTH; ++j) {
			int r = y + i;
			int c = x + j;
			int index = w * r + c;
			if (index >= pixels.length) continue;
			if (pixels[index] == Tool._1) {
			    ++quantity;
			}
		    }
		}
		double density = quantity / (WIDTH * HEIGHT);
		if (density > currentDensity) {
		    currentDensity = density;
		    currentX = x;
		}
	    }
	}
	return currentX + WIDTH;
    }
}
