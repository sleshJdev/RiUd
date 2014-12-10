package by.slesh.ri.cp.ushkindaria.app.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Point;
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
    private JLabel mAreaInterestImageBox;
    private JLabel mGroupNumberImageBox;
    private JPanel mGroupNumberSegmentedPanel;
    private JPanel mUnrecognizedPanel;
    private JPanel mRecognizedPanel;

    public ImageBoxesView() {

	setLayout(new GridLayout(1, 2, 5, 5));
	mSourceImageBox = new JLabel();
	mTargetImageBox = new JLabel();
	mAreaInterestImageBox = new JLabel();
	mGroupNumberImageBox = new JLabel();

	mSourceImageBox.setHorizontalAlignment(JLabel.CENTER);
	mTargetImageBox.setHorizontalAlignment(JLabel.CENTER);
	mAreaInterestImageBox.setHorizontalAlignment(JLabel.CENTER);
	mGroupNumberImageBox.setHorizontalAlignment(JLabel.CENTER);

	mGroupNumberImageBox.setBorder(BorderFactory.createLineBorder(
	        Color.BLUE, 1));

	mGroupNumberSegmentedPanel = new JPanel(new GridLayout(1, 1));
	mGroupNumberSegmentedPanel.setBorder(BorderFactory.createLineBorder(
	        Color.RED, 1));

	JScrollPane sourceScrollPane = new JScrollPane(mSourceImageBox);
	JScrollPane targetScrollPane = new JScrollPane(mTargetImageBox);

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

	JPanel panel11 = new JPanel(new GridLayout(1, 3, 5, 5));
	panel11.add(mGroupNumberImageBox);
	panel11.add(mGroupNumberSegmentedPanel);

	JPanel panel12 = new JPanel(new GridLayout(2, 1));
	panel12.add(new JScrollPane(mAreaInterestImageBox));
	panel12.add(panel11);
	panel12.setPreferredSize(new Dimension(1, 200));
	panel12.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

	JPanel panel13 = new JPanel(new BorderLayout());
	panel13.add(sourceScrollPane);
	panel13.add(panel12, BorderLayout.PAGE_END);

	mUnrecognizedPanel = new JPanel(new GridLayout(1, 1));
	mUnrecognizedPanel.setPreferredSize(new Dimension(1, 100));
	mUnrecognizedPanel.setBorder(BorderFactory.createLineBorder(
	        Color.BLACK, 1));

	mRecognizedPanel = new JPanel(new GridLayout(1, 1));
	mRecognizedPanel.setPreferredSize(new Dimension(1, 100));
	mRecognizedPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK,
	        1));

	JPanel panel22 = new JPanel(new GridLayout(2, 1));
	panel22.add(mUnrecognizedPanel);
	panel22.add(mRecognizedPanel);

	JPanel panel21 = new JPanel(new BorderLayout());
	panel21.add(targetScrollPane);
	panel21.add(panel22, BorderLayout.PAGE_END);

	add(panel13);
	add(panel21);
    }

    @Override
    public void getLocationOnImage(Point currentLocation) {

	int h = mTargetImageBox.getHeight();
	int w = mTargetImageBox.getWidth();
	int ih = mTargetImageBox.getIcon().getIconHeight();
	int iw = mTargetImageBox.getIcon().getIconWidth();

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

    @Override
    public void updateAreaInterest(BufferedImage area) {
	mAreaInterestImageBox.setIcon(new ImageIcon(area));

    }

    @Override
    public void updateGroupNumber(BufferedImage groupNumber) {
	mGroupNumberImageBox.setIcon(new ImageIcon(groupNumber));
    }

    @Override
    public void updateSegmentGroupNumber(BufferedImage[] digits) {
	mGroupNumberSegmentedPanel.setLayout(new GridLayout(1, digits.length));
	mGroupNumberSegmentedPanel.removeAll();
	for (BufferedImage digit : digits) {
	    JLabel l = new JLabel(new ImageIcon(digit));
	    l.setBorder(BorderFactory.createLineBorder(Color.RED, 2));
	    mGroupNumberSegmentedPanel.add(l);
	    mGroupNumberSegmentedPanel.revalidate();
	}
    }

    @Override
    public void updateUnrecognizeNumber(BufferedImage[] digits) {
	mUnrecognizedPanel.setLayout(new GridLayout(1, digits.length));
	mUnrecognizedPanel.removeAll();
	for (BufferedImage digit : digits) {
	    JLabel l = new JLabel(new ImageIcon(digit));
	    l.setBorder(BorderFactory.createLineBorder(Color.RED, 2));
	    mUnrecognizedPanel.add(l);
	    mUnrecognizedPanel.revalidate();
	}
    }

    @Override
    public void updateRecognizeNumber(BufferedImage[] digits) {
	mRecognizedPanel.setLayout(new GridLayout(1, digits.length));
	mRecognizedPanel.removeAll();
	for (BufferedImage digit : digits) {
	    JLabel l = new JLabel(new ImageIcon(digit));
	    l.setBorder(BorderFactory.createLineBorder(Color.RED, 2));
	    mRecognizedPanel.add(l);
	    mRecognizedPanel.revalidate();
	}
    }
}
