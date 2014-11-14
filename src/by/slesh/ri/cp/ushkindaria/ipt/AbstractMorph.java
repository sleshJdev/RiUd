package by.slesh.ri.cp.ushkindaria.ipt;

import java.awt.image.BufferedImage;
import java.util.Comparator;

public abstract class AbstractMorph extends AbstractTool {
	
	public static final int[][] DISK_KERNEL = new int[][]{
		{_0, _1, _1, _1, _0},
		{_1, _1, _1, _1, _1},
		{_1, _1, _1, _1, _1},
		{_1, _1, _1, _1, _1},
		{_0, _1, _1, _1, _0},
    };

	public static final int[][] RING_KERNEL = new int[][]{
		{_0, _1, _1, _1, _0},
		{_1, _0, _0, _0, _1},
		{_1, _0, _0, _0, _1},
		{_1, _0, _0, _0, _1},
		{_0, _1, _1, _1, _0},	
    };

	public static final int[][]		RECT_KERNEL	= new int[][] {
		{_1, _1, _1, _1, _1},
		{_1, _1, _1, _1, _1},
		{_1, _1, _1, _1, _1},
		{_1, _1, _1, _1, _1},
		{_1, _1, _1, _1, _1},
    };
	
	public static final int[][]		CIRCLE_KERNEL	= new int[][] {
		{_0, _1, _0,},
		{_1, _1, _1,},
		{_0, _1, _0,},
    };
//		{_1, _1, _1,},
//		{_0, _0, _0,},
//		{_1, _1, _1,},
//    };
//	{_1, _0, _1,},
//	{_1, _0, _1,},
//	{_1, _0, _1,},
//};
//		{_1, _0, _1,},
//		{_0, _0, _0,},
//		{_1, _0, _1,},
//	};

	protected Comparator<Integer>	checkExecute;
	protected Comparable<Integer>	checkPixel;
	protected int[][]				kernel;
	protected boolean				initExecuteState;
	protected int					newPixelValue;
	
	
	public BufferedImage morph(BufferedImage source) {

		int heightKernel = kernel.length;
		int widthKernel = kernel[0].length;
		int w = source.getWidth();
		int h = source.getHeight();
		int[] sourceRgb = source.getRGB(0, 0, w, h, null, w, w);
		int[] targetRgb = sourceRgb.clone();
		for (int y = 0; y < h; ++y) {
			for (int x = 0; x < w; ++x) {
				int currentPixel = sourceRgb[y * w + x];
				if (checkPixel.compareTo(currentPixel) == 0) // 0 equal true
					continue;
				boolean isScan = true;
				boolean isExecute = initExecuteState;
				for (int i = 0; (i < heightKernel) && isScan; ++i) {
					for (int j = 0; (j < widthKernel) && isScan; ++j) {
						int pixelPosY = (y - heightKernel / 2) + i;
						int pixelPosX = (x - widthKernel / 2) + j;
						if ((pixelPosY < 0 || pixelPosY >= h) || 
							(pixelPosX < 0 || pixelPosX >= w) || 
							(i == y && j == x)) {
							continue;
						}
						int maskPixel = kernel[i][j];
						int underPixel = sourceRgb[w * pixelPosY + pixelPosX];
						if (checkExecute.compare(underPixel, maskPixel) == 0) {
							isExecute = !isExecute;
							isScan = false;
						}
					}
				}
				if (isExecute) {
					targetRgb[y * w + x] = newPixelValue;
				}
			}
		}
		return rgbToImage(targetRgb, w, h);
	}
}
