package by.slesh.ri.cp.ushkindaria.app.view.service;

import java.awt.Point;
import java.awt.event.ActionListener;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;

public interface ImageBoxesViewInterface {

    void updateSource(BufferedImage source);

    void updateTarget(BufferedImage target);

    void addTargetImageBoxClickListener(MouseListener l);

    void getLocationOnImage(Point currentLocation);
}
