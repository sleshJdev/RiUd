/**
 * 
 */
package by.slesh.ri.cp.ushkindaria.ipt;

import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Transparency;
import java.awt.image.BufferedImage;

/**
 * @author slesh
 *
 */
public class Resizer {
    public static BufferedImage scaleUp(BufferedImage img, int targetWidth,
	    int targetHeight, boolean higherQuality) {
	int type = (img.getTransparency() == Transparency.OPAQUE) ? BufferedImage.TYPE_INT_RGB
	        : BufferedImage.TYPE_INT_ARGB;
	BufferedImage ret = (BufferedImage) img;
	int w, h;
	if (higherQuality) {
	    // Use multi-step technique: start with original size, then
	    // scale down in multiple passes with drawImage()
	    // until the target size is reached
	    w = img.getWidth();
	    h = img.getHeight();
	} else {
	    // Use one-step technique: scale directly from original
	    // size to target size with a single drawImage() call
	    w = targetWidth;
	    h = targetHeight;
	}

	do {
	    if (higherQuality && w < targetWidth) {
		w *= 2;
		if (w > targetWidth) {
		    w = targetWidth;
		}
	    }

	    if (higherQuality && h < targetHeight) {
		h *= 2;
		if (h > targetHeight) {
		    h = targetHeight;
		}
	    }

	    BufferedImage tmp = new BufferedImage(w, h, type);
	    Graphics2D g2 = tmp.createGraphics();
	    g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
		    RenderingHints.VALUE_INTERPOLATION_BILINEAR);
	    g2.setRenderingHint(RenderingHints.KEY_RENDERING,
		    RenderingHints.VALUE_RENDER_QUALITY);
	    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
		    RenderingHints.VALUE_ANTIALIAS_ON);
	    g2.drawImage(ret, 0, 0, w, h, null);
	    g2.dispose();

	    ret = tmp;
	} while (w != targetWidth || h != targetHeight);

	return ret;
    }

    public static BufferedImage scaleLow(BufferedImage img, int targetWidth,
	    int targetHeight, boolean higherQuality) {
	int type = (img.getTransparency() == Transparency.OPAQUE) ? BufferedImage.TYPE_INT_RGB
	        : BufferedImage.TYPE_INT_ARGB;
	BufferedImage ret = (BufferedImage) img;
	int w, h;
	if (higherQuality) {
	    // Use multi-step technique: start with original size, then
	    // scale down in multiple passes with drawImage()
	    // until the target size is reached
	    w = img.getWidth();
	    h = img.getHeight();
	} else {
	    // Use one-step technique: scale directly from original
	    // size to target size with a single drawImage() call
	    w = targetWidth;
	    h = targetHeight;
	}

	do {
	    if (higherQuality && w > targetWidth) {
		w /= 2;
		if (w < targetWidth) {
		    w = targetWidth;
		}
	    }

	    if (higherQuality && h > targetHeight) {
		h /= 2;
		if (h < targetHeight) {
		    h = targetHeight;
		}
	    }

	    BufferedImage tmp = new BufferedImage(w, h, type);
	    Graphics2D g2 = tmp.createGraphics();
	    g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
		    RenderingHints.VALUE_INTERPOLATION_BILINEAR);
	    g2.setRenderingHint(RenderingHints.KEY_RENDERING,
		    RenderingHints.VALUE_RENDER_QUALITY);
	    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
		    RenderingHints.VALUE_ANTIALIAS_ON);
	    g2.drawImage(ret, 0, 0, w, h, null);
	    g2.dispose();

	    ret = tmp;
	} while (w != targetWidth || h != targetHeight);

	return ret;
    }
}
