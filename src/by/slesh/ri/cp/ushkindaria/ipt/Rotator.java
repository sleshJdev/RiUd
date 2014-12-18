/**
 * 
 */
package by.slesh.ri.cp.ushkindaria.ipt;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import by.slesh.ri.cp.ushkindaria.ipt.segment.HistogramSegmentator;

/**
 * @author slesh
 *
 */
public class Rotator {
	public static double sAngle;

	public static BufferedImage rotate(BufferedImage source) {

		int h = source.getHeight();
		int w = source.getWidth();
		int[] pixels = source.getRGB(0, 0, w, h, null, 0, w);

		HistogramSegmentator segmentator = new HistogramSegmentator();
		segmentator.segment(source, 20, 100, true);

		int x1 = 500;
		int y1 = h - 20;
		for (; y1 > 0; --y1) {
			boolean is = true;
			for (int x = x1; x < x1 + 10; ++x) {
				if (pixels[w * y1 + x1] == Tool._0) {
					is = false;
				}
			}
			pixels[w * y1 + x1] = Color.RED.getRGB();
			if (is)
				break;
		}

		int x2 = 800;
		int y2 = h - 20;
		for (; y2 > 0; --y2) {
			boolean is = true;
			for (int x = x2; x < x2 + 10; ++x) {
				if (pixels[w * y2 + x2] == Tool._0) {
					is = false;
				}
			}
			pixels[w * y2 + x2] = Color.RED.getRGB();
			if (is)
				break;
		}

		if (y2 - y1 == 0)
			return source;

		sAngle = Math.atan2(y2 - y1, x2 - x1);
		System.out.println("Rotate angle " + -sAngle);
		source.setRGB(0, 0, w, h, pixels, 0, w);
		
		try{
			ImageIO.write(source, "png", new File("d:\\sssssssssssss.png"));
		}catch(IOException e){
			e.printStackTrace();
		}
		
		return Tool.rotate(source, -sAngle);
	}
}
