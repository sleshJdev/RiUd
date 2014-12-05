package by.slesh.ri.cp.ushkindaria.ipt.segment;

import java.awt.Graphics;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import by.slesh.ri.cp.ushkindaria.ipt.Tool;

public class Contour {

    private static Point UNSET = new Point(-1, -1);

    private List<Point> path = new ArrayList<Point>();
    private Point startPoint = UNSET;
    private Point endPoint = UNSET;

    private int xMin = Integer.MAX_VALUE;
    private int xMax;

    private int yMin = Integer.MAX_VALUE;
    private int yMax;

    public BufferedImage sub(BufferedImage source) {
        int w = xMax - xMin;
        int h = yMax - yMin;
        BufferedImage result = source.getSubimage(xMin, yMin, w, h);
        int[] rgb = result.getRGB(0, 0, w, h, null, w, w);
        return Tool.rgbToImage(rgb, w, h);
    }

    public void drawOnImage(BufferedImage bitmap) {
        int size = path.size();
        if (size < 50) return;
        Graphics g = bitmap.getGraphics();
        int[] xCoords = new int[size];
        int[] yCoords = new int[size];
        int index = 0;
        for (Point p : path) {
            xCoords[index] = p.x;
            yCoords[index] = p.y;
            ++index;
        }
        g.drawPolygon(xCoords, yCoords, size);
    }

    public void addPoint(Point point) {
        if (point.x < xMin) xMin = point.x;
        if (point.x > xMax) xMax = point.x;
        if (point.y < yMin) yMin = point.y;
        if (point.y > yMax) yMax = point.y;
        if (startPoint == UNSET) {
            startPoint = point;
            path.add(point);
            return;
        }
        endPoint = point;
        path.add(point);
    }

    public boolean isClosed() {
        return startPoint.equals(endPoint) && !endPoint.equals(UNSET);
    }
}