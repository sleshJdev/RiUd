package by.slesh.ri.cp.ushkindaria.view;

import java.awt.GridLayout;
import java.awt.image.BufferedImage;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import by.slesh.ri.cp.ushkindaria.view.service.ImageBoxesViewInterface;

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
		JScrollPane spSource = new JScrollPane(mSourceImageBox);
		JScrollPane spTarget = new JScrollPane(mTargetImageBox);
		spTarget.getViewport().addChangeListener(new ChangeListener() {

			@Override
			public void stateChanged(ChangeEvent arg0) {
				int x = spTarget.getHorizontalScrollBar().getValue();
				int y = spTarget.getVerticalScrollBar().getValue();
				spSource.getHorizontalScrollBar().setValue(x);
				spSource.getVerticalScrollBar().setValue(y);
			}
		});

		spSource.getViewport().addChangeListener(new ChangeListener() {

			@Override
			public void stateChanged(ChangeEvent arg0) {
				int x = spSource.getHorizontalScrollBar().getValue();
				int y = spSource.getVerticalScrollBar().getValue();
				spTarget.getHorizontalScrollBar().setValue(x);
				spTarget.getVerticalScrollBar().setValue(y);
			}
		});

		add(spSource);
		add(spTarget);
	}

	@Override
	public void showSourceImage(BufferedImage source) {
		mSourceImageBox.setIcon(new ImageIcon(source));
	}

	@Override
	public void showTargetImage(BufferedImage target) {
		mTargetImageBox.setIcon(new ImageIcon(target));
	}
}
