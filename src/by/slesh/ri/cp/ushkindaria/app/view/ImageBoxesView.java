package by.slesh.ri.cp.ushkindaria.app.view;

import java.awt.Color;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.event.ActionListener;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import by.slesh.ri.cp.ushkindaria.app.view.service.ImageBoxesViewInterface;

public class ImageBoxesView extends JPanel implements ImageBoxesViewInterface {

    private static final long serialVersionUID = -3893100366850568189L;
    private JLabel mSourceImageBox;
    private JLabel mTargetImageBox;

    public ImageBoxesView() {
        setLayout(new GridLayout(1, 2, 5, 5));
        mSourceImageBox = new JLabel();
        mTargetImageBox = new JLabel();

        mSourceImageBox.setHorizontalAlignment(JLabel.CENTER);
        mTargetImageBox.setHorizontalAlignment(JLabel.CENTER);

//        mSourceImageBox.setBorder(BorderFactory.createLineBorder(Color.RED));
//        mTargetImageBox.setBorder(BorderFactory.createLineBorder(Color.BLUE));

        JScrollPane sourceScrollPane = new JScrollPane(mSourceImageBox);
        JScrollPane targetScrollPane = new JScrollPane(mTargetImageBox);

        // JPanel sourcePanel = new JPanel();
        // sourcePanel.add(mSourceImageBox);
        // sourcePanel.setBorder(BorderFactory.createLineBorder(Color.RED));
        //
        // JPanel targetPanel = new JPanel();
        // targetPanel.add(mTargetImageBox);
        // targetPanel.setBorder(BorderFactory.createLineBorder(Color.BLUE));

        targetScrollPane.getViewport().addChangeListener(new ChangeListener() {

            @Override
            public void stateChanged(ChangeEvent arg0) {
                int x = targetScrollPane.getHorizontalScrollBar().getValue();
                int y = targetScrollPane.getVerticalScrollBar().getValue();
                sourceScrollPane.getHorizontalScrollBar().setValue(x);
                sourceScrollPane.getVerticalScrollBar().setValue(y);
            }
        });

        sourceScrollPane.getViewport().addChangeListener(new ChangeListener() {

            @Override
            public void stateChanged(ChangeEvent arg0) {
                int x = sourceScrollPane.getHorizontalScrollBar().getValue();
                int y = sourceScrollPane.getVerticalScrollBar().getValue();
                targetScrollPane.getHorizontalScrollBar().setValue(x);
                targetScrollPane.getVerticalScrollBar().setValue(y);
            }
        });

        add(sourceScrollPane);
        add(targetScrollPane);
    }

    @Override
    public void getLocationOnImage(Point currentLocation) {
        int h = mTargetImageBox.getHeight();
        int w = mTargetImageBox.getWidth();
        int ih = mTargetImageBox.getIcon().getIconHeight();
        int iw = mTargetImageBox.getIcon().getIconWidth();

        System.out.println("w=" + w + "h=" + h);
        System.out.println("iw=" + iw + "ih=" + ih);

        int dx = (iw - w) / 2;
        int dy = (ih - h) / 2;

        currentLocation.translate(dx, dy);
    }

    @Override
    public void addTargetImageBoxClickListener(MouseListener l) {
        mTargetImageBox.addMouseListener(l);
    }

    @Override
    public void updateSource(BufferedImage source) {
        mSourceImageBox.setIcon(new ImageIcon(source));
    }

    @Override
    public void updateTarget(BufferedImage target) {
        mTargetImageBox.setIcon(new ImageIcon(target));
    }
}
