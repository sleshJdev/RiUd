package by.slesh.ri.cp.ushkindaria.ipt;

import java.awt.image.BufferedImage;

public abstract class AbstractBinarizator extends AbstractTool {

	public abstract BufferedImage binarization(BufferedImage source);
	
	public BufferedImage binarization(BufferedImage source, int threshold) {
		int w = source.getWidth();
		int h = source.getHeight();
		int[] sourceRgb = source.getRGB(0, 0, w, h, null, w, w);
		for (int y = 0; y < h; ++y) {
			for (int x = 0; x < w; ++x) {
				int pos = w * y + x;
				int brightness = IptUtil.rgbToBrightness(sourceRgb[pos]);
				if (brightness > threshold) sourceRgb[pos] = _0;
				else sourceRgb[pos] = _1;
			}
		}
		return rgbToImage(sourceRgb, w, h);
	}
}
