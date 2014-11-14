package by.slesh.ri.cp.ushkindaria.ipt;

import java.awt.image.BufferedImage;

public class CleanMorph extends AbstractMorph {

	@Override
	public BufferedImage morph(BufferedImage source) {

		int h = source.getHeight();
		int w = source.getWidth();
		int[] pixels = source.getRGB(0, 0, w, h, null, w, w);

		for (int index = 0, y = 1; y < h - 1; ++y) {
			for (int x = 1; x < w - 1; ++x, ++index) {
				if (pixels[index] == _0) continue;
				boolean isClean = true;
				for (int i = 0; isClean && i < 3; ++i) {
					for (int j = 0; isClean && j < 3; ++j) {
						int r = (y - 1 + i);
						int c = (x - 1 + j);
						if (r == y && c == x) continue;
						int pos = w * r + c;
						if (pixels[pos] == _1) {
							isClean = false;
							break;
						}
					}
					if (isClean) pixels[index] = _0;
				}
			}
		}

		source.setRGB(0, 0, w, h, pixels, w, w);
		return source;
	}
}
