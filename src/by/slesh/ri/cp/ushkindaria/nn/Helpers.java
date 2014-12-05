package by.slesh.ri.cp.ushkindaria.nn;

import java.awt.image.BufferedImage;

import by.slesh.ri.cp.ushkindaria.ipt.Tool;

public class Helpers {

    public static int[] initX(BufferedImage image) {
	int h = image.getHeight();
	int w = image.getWidth();
	int[] binaryImage = new int[w * h];
	for (int y = 0, k = 0; y < h; ++y) {
	    for (int x = 0; x < w; ++x) {
		binaryImage[k++] = image.getRGB(x, y) == Tool._0 ? -1 : 1;
	    }
	}
	return binaryImage;
    }

    public static int[] initY(int code, int n) {

	int[] y = new int[n];
	y[code] = 1;
	return y;
    }
}
