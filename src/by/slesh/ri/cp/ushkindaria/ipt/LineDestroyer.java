/**
 * 
 */
package by.slesh.ri.cp.ushkindaria.ipt;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import by.slesh.ri.cp.ushkindaria.ipt.segment.HistogramSegmentator;

/**
 * @author slesh
 *
 */
public class LineDestroyer {
    public static BufferedImage destroy(BufferedImage source) {
	int h = source.getHeight();
	int w = source.getWidth();
	int[] pixels = source.getRGB(0, 0, w, h, null, 0, w);

	HistogramSegmentator hs = new HistogramSegmentator();
	source = hs.segment(source, false);
	int x1 = hs.getLeftBorder();
	int x2 = hs.getRightBorder();

	final int N = 2;
	int length = x2 - x1;
	for (int y = 0; y < h - N; ++y) {
	    double quantity = 0;
	    for (int k = y; k < y + N; ++k) {
		for (int x = x1; x < x2; ++x) {
		    int index = w * k + x;
		    if (pixels[index] == Tool._1) {
			++quantity;
		    }
		}
	    }
	    if(quantity / (N * length) > 0.8) clear(pixels, w, x1, y, x2, y + N);
	}
	source.setRGB(0, 0, w, h, pixels, 0, w);	
	return source;
    }

    private static void clear(int[] pixels, int offset, int x1, int y1, int x2,
	    int y2) {
	for (int y = y1; y < y2; ++y) {
	    for (int x = x1; x < x2; ++x) {
		pixels[offset * y + x] = Tool._0;
	    }
	}
    }
}
