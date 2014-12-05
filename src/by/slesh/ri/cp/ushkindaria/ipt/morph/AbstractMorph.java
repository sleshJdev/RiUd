package by.slesh.ri.cp.ushkindaria.ipt.morph;

import java.awt.image.BufferedImage;

import by.slesh.ri.cp.ushkindaria.ipt.Tool;

public abstract class AbstractMorph extends Tool {
	
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
