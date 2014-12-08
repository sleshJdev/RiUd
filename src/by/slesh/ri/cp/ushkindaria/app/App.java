package by.slesh.ri.cp.ushkindaria.app;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;

import javax.imageio.ImageIO;

import by.slesh.ri.cp.ushkindaria.app.view.MainView;
import by.slesh.ri.cp.ushkindaria.ipt.Resizer;
import by.slesh.ri.cp.ushkindaria.ipt.Tool;
import by.slesh.ri.cp.ushkindaria.ipt.binarization.Binarizator;
import by.slesh.ri.cp.ushkindaria.ipt.morph.WizardMorph;

public class App {

    static BufferedImage cut(BufferedImage source) {
	int h = source.getHeight();
	int w = source.getWidth();
	int[] pixels = source.getRGB(0, 0, w, h, null, 0, w);
	int x1 = Integer.MAX_VALUE;
	int y1 = Integer.MAX_VALUE;
	int x2 = Integer.MIN_VALUE;
	int y2 = Integer.MIN_VALUE;
	for (int y = 0; y < h; ++y) {
	    for (int x = 0; x < w; ++x) {
		if (pixels[w * y + x] == Tool._1) {
		    if (x < x1) x1 = x;
		    if (y < y1) y1 = y;
		    if (x > x2) x2 = x;
		    if (y > y2) y2 = y;
		}
	    }
	}
	return source.getSubimage(x1, y1, x2 - x1, y2 - y1);
    }

    static void helper() {
	try {
	    class BmpFilter implements FilenameFilter {
		public boolean accept(File dir, String name) {
		    return (name.endsWith(".bmp"));
		}
	    }
	    String[] list = new File(G.PATH_IMAGES_FOR_FINDER + "\\")
		    .list(new BmpFilter());
	    for (String fileName : list) {
		File f = new File(G.PATH_IMAGES_FOR_FINDER + "\\" + fileName);
		BufferedImage image = ImageIO.read(f);
		int h = image.getHeight();
		int w = image.getWidth();
		image = Binarizator.binByThreshold(image);
		image = cut(image);
//		image = WizardMorph.erode(image);
		image = Resizer.scaleUp(image, w, h, true);
		image = Resizer.scaleLow(image, G.WIDTH_FOR_FINDER,
		        G.HEIGHT_FOR_FINDER, true);
		image = Binarizator.binByThreshold(image);
		ImageIO.write(image, "bmp", new File(G.PATH_SET_FIDNER + "\\"
		        + fileName));
	    }
	} catch (IOException e) {
	    e.printStackTrace();
	}
    }

    public static void main(String[] args) {

	helper();
	MainView mainView = new MainView();
	mainView.setVisible(true);
    }
}
