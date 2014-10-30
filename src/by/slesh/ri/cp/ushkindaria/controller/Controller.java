package by.slesh.ri.cp.ushkindaria.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.AbstractButton;
import javax.swing.JFileChooser;

import by.slesh.ri.cp.ushkindaria.model.Model;
import by.slesh.ri.cp.ushkindaria.view.service.ControlPanelViewInterface;
import by.slesh.ri.cp.ushkindaria.view.service.ImageBoxesViewInterface;

public class Controller implements ActionListener, AdjustmentListener {
	private ControlPanelViewInterface mControlPanelView;
	private ImageBoxesViewInterface mImageBoxesView;
	private Model mModel;

	public Controller(ControlPanelViewInterface controlPanelView,
			ImageBoxesViewInterface imageBoxesView, Model model) {
		mImageBoxesView = imageBoxesView;
		mControlPanelView = controlPanelView;
		mModel = model;

		controlPanelView.addOnBinarizateListener(this);
		controlPanelView.addOnOpenFileListener(this);
		controlPanelView.addOnAdjustmentListener(this);
		controlPanelView.addOnSkeletonizationListener(this);
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		switch (((AbstractButton) arg0.getSource()).getActionCommand()) {
		case ControlPanelViewInterface.ACTION_BINARIZATION:
			mModel.binarization();
			mImageBoxesView.showTargetImage(mModel.getmTargetImage());
			break;
		case ControlPanelViewInterface.ACTION_FILE_OPEN:
			BufferedImage source = openFile();
			if(source == null) return;
			mModel.setmSourceImage(source);
			mModel.setmTargetImage(source);
			mImageBoxesView.showSourceImage(mModel.getmSourceImage());
			mImageBoxesView.showTargetImage(mModel.getmTargetImage());
			mControlPanelView.unableControls();
			break;
		case ControlPanelViewInterface.ACTION_SKELETONIZATION:
			mModel.skeletonization();
			mImageBoxesView.showTargetImage(mModel.getmTargetImage());
			break;
		}
	}

	@Override
	public void adjustmentValueChanged(AdjustmentEvent arg0) {
		mModel.setmPercents(arg0.getValue());
		mModel.binarization();
		mImageBoxesView.showTargetImage(mModel.getmTargetImage());
		mControlPanelView.updatePercentValue(arg0.getValue());
	}

	private static final JFileChooser FC = new JFileChooser();

	private BufferedImage openFile() {
		BufferedImage loadedImage = null;
		int returnValue = FC.showDialog(null, ControlPanelViewInterface.ACTION_FILE_OPEN);
		if (returnValue == JFileChooser.APPROVE_OPTION) {
			File file = FC.getSelectedFile();
			try {
				loadedImage = ImageIO.read(file);
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
		return loadedImage;
	}
}
