package by.slesh.ri.cp.ushkindaria.ipt;

import java.awt.Color;
import java.awt.image.BufferedImage;

public class AbstractTool {
	protected static final int IMAGE_TYPE = BufferedImage.TYPE_4BYTE_ABGR;
	public static final int _0 = Color.WHITE.getRGB();
	public static final int _1 = Color.BLACK.getRGB();
	
	public static BufferedImage rgbToImage(int[] rgb, int w, int h) {
		BufferedImage target = new BufferedImage(w, h, IMAGE_TYPE);
		target.setRGB(0, 0, w, h, rgb, w, w);
		return target;
	}
}
