package by.slesh.ri.cp.ushkindaria.ipt.morph;

import java.awt.image.BufferedImage;

import by.slesh.ri.cp.ushkindaria.ipt.Tool;

public class ErodeMorph extends AbstractMorph {

    public ErodeMorph() {

	kernel = CIRCLE_KERNEL;
    }

    @Override
    public BufferedImage morph(BufferedImage source) {

	int heightKernel = kernel.length;
	int widthKernel = kernel[0].length;
	int w = source.getWidth();
	int h = source.getHeight();
	int[] sourceRgb = source.getRGB(0, 0, w, h, null, w, w);
	int[] targetRgb = sourceRgb.clone();
	for (int y = 0; y < h; ++y) {
	    for (int x = 0; x < w; ++x) {

		int index = y * w + x;
		if (sourceRgb[index] == Tool._0) {
		    continue;
		}

		for (int i = 0; i < heightKernel; ++i) {

		    for (int j = 0; j < widthKernel; ++j) {

			int pixelPosY = (y - heightKernel / 2) + i;
			int pixelPosX = (x - widthKernel / 2) + j;
			if ((pixelPosY < 0 || pixelPosY >= h)
			        || (pixelPosX < 0 || pixelPosX >= w)
			        || (i == y && j == x)) {
			    continue;
			}
			if (kernel[i][j] == Tool._1
			        && sourceRgb[w * pixelPosY + pixelPosX] == Tool._0) {
			    targetRgb[index] = Tool._0;
			    i = heightKernel;
			    j = widthKernel;
			}
		    }
		}
	    }
	}
	return rgbToImage(targetRgb, w, h);
    }
}
