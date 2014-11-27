package by.slesh.ri.cp.ushkindaria.nn;

import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Transparency;
import java.awt.image.BufferedImage;

import by.slesh.ri.cp.ushkindaria.app.G;
import by.slesh.ri.cp.ushkindaria.ipt.AbstractTool;

public class Helpers {

    public static int[] initX(BufferedImage image) {
	int[] binaryImage = new int[G.HEIGHT * G.WIDTH];
	for (int y = 0, k = 0; y < G.HEIGHT; ++y) {
	    for (int x = 0; x < G.WIDTH; ++x) {
		if (image.getRGB(x, y) == AbstractTool._1) {
		    binaryImage[k++] = 1;
		} else {
		    binaryImage[k++] = 0;
		}
	    }
	}
	return binaryImage;
    }

    public static int[] initY(int code) {

	int[] y = new int[G.QUANTITY_NEURONS];
	y[code] = 1;
	return y;
    }

    public static BufferedImage scaleImage(BufferedImage img) {

	int type = (img.getTransparency() == Transparency.OPAQUE) ? BufferedImage.TYPE_INT_RGB
	        : BufferedImage.TYPE_INT_ARGB;
	BufferedImage ret = img;
	BufferedImage scratchImage = null;
	Graphics2D g2 = null;

	int w = img.getWidth();
	int h = img.getHeight();

	int prevW = w;
	int prevH = h;

	int targetWidth = G.WIDTH;
	int targetHeight = G.HEIGHT;

	do {
	    if (w > targetWidth) {
		w /= 2;
		w = (w < targetWidth) ? targetWidth : w;
	    }

	    if (h > targetHeight) {
		h /= 2;
		h = (h < targetHeight) ? targetHeight : h;
	    }

	    if (scratchImage == null) {
		scratchImage = new BufferedImage(w, h, type);
		g2 = scratchImage.createGraphics();
	    }

	    g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
		    RenderingHints.VALUE_INTERPOLATION_BILINEAR);
	    g2.drawImage(ret, 0, 0, w, h, 0, 0, prevW, prevH, null);

	    prevW = w;
	    prevH = h;
	    ret = scratchImage;
	} while (w != targetWidth || h != targetHeight);

	if (g2 != null) {
	    g2.dispose();
	}

	if (targetWidth != ret.getWidth() || targetHeight != ret.getHeight()) {
	    scratchImage = new BufferedImage(targetWidth, targetHeight, type);
	    g2 = scratchImage.createGraphics();
	    g2.drawImage(ret, 0, 0, null);
	    g2.dispose();
	    ret = scratchImage;
	}

	return ret;

    }
}
