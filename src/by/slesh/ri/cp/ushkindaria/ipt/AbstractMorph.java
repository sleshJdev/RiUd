package by.slesh.ri.cp.ushkindaria.ipt;

import java.awt.image.BufferedImage;

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
		{_1, _1,},
		{_1, _1,},
    };
//		{_0, _1, _0,},
//		{_1, _1, _1,},
//		{_0, _1, _0,},
//    };
//		{_0, _1, _1, _0},
//		{_1, _1, _1, _1},
//		{_1, _1, _1, _1},
//		{_0, _1, _1, _0},
//    };
//	{_1, _0, _1,},
//	{_1, _0, _1,},
//	{_1, _0, _1,},
//};
//		{_1, _1, _1,},
//		{_0, _0, _0,},
//		{_1, _1, _1,},
//	};

	protected int[][]				kernel;
	
	
	public abstract BufferedImage morph(BufferedImage source);
}
