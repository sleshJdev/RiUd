package by.slesh.ri.cp.ushkindaria.ipt.morph;

import java.awt.image.BufferedImage;

import by.slesh.ri.cp.ushkindaria.ipt.Tool;

public class ClearMorph extends AbstractMorph {

    @Override
    public BufferedImage morph(BufferedImage source) {

	int h = source.getHeight();
	int w = source.getWidth();
	int[] pixels = source.getRGB(0, 0, w, h, null, w, w);
	int[] copy = pixels.clone();
	for (int y = 1; y < h - 1; ++y) {
	    for (int x = 1; x < w - 1; ++x) {
		if (countNeighbour(pixels, w, x, y) < 5) {
		    copy[w * y + x] = Tool._0;
		}
	    }
	}
	source.setRGB(0, 0, w, h, copy, w, w);
	return source;
    }

    public int countNeighbour(int[] pixels, int offset, int x, int y) {
	int quantity = 0;
	if (pixels[y * offset + x + 1] == Tool._1) ++quantity;
	if (pixels[y * offset + x - 1] == Tool._1) ++quantity;

	if (pixels[(y - 1) * offset + x] == Tool._1) ++quantity;
	if (pixels[(y + 1) * offset + x] == Tool._1) ++quantity;

	if (pixels[(y - 1) * offset + x + 1] == Tool._1) ++quantity;
	if (pixels[(y - 1) * offset + x - 1] == Tool._1) ++quantity;

	if (pixels[(y + 1) * offset + x + 1] == Tool._1) ++quantity;
	if (pixels[(y + 1) * offset + x - 1] == Tool._1) ++quantity;

	return quantity;
    }
}
