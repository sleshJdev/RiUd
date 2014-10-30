package by.slesh.ri.cp.ushkindaria.view;

import java.awt.GridLayout;
import java.awt.event.ActionListener;
import java.awt.event.AdjustmentListener;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JTextField;

import by.slesh.ri.cp.ushkindaria.view.service.ControlPanelViewInterface;

public class ControlPanelView extends JPanel implements ControlPanelViewInterface {
	private static final long serialVersionUID = -2454572918534173516L;

	private JTextField mTfPercentValue;
	private JScrollBar mSbPercent;
	private JButton mBtnSkeletonization;
	private JButton mBtnBinarization;
	private JButton mBtnOpenFile;

	public ControlPanelView() {
		setLayout(new GridLayout(2, 3));
		mBtnSkeletonization = new JButton("Алгорит Зонга-Суня");
		mBtnSkeletonization.setActionCommand(ACTION_SKELETONIZATION);
		mBtnSkeletonization.setEnabled(false);
		mBtnBinarization = new JButton("Бинаризовать(метод 40%)");
		mBtnBinarization.setActionCommand(ACTION_BINARIZATION);
		mBtnBinarization.setEnabled(false);
		mBtnOpenFile = new JButton("Загрузить изображение");
		mBtnOpenFile.setActionCommand(ACTION_FILE_OPEN);
		mSbPercent = new JScrollBar(JScrollBar.HORIZONTAL, 40, 1, 0, 100);
		mSbPercent.setEnabled(false);
		mTfPercentValue = new JTextField("Percent value = 40");
		mTfPercentValue.setHorizontalAlignment(JTextField.CENTER);
		mTfPercentValue.setEditable(false);
		add(mBtnOpenFile);
		add(mBtnSkeletonization);
		add(mBtnBinarization);
		add(mSbPercent);
		add(mTfPercentValue);
	}

	public void updatePercentValue(int value) {
		String text = "Percent value = " + Integer.toString(value);
		mTfPercentValue.setText(text);
	}

	@Override
	public void unableControls() {
		mBtnSkeletonization.setEnabled(true);
		mBtnBinarization.setEnabled(true);
		mSbPercent.setEnabled(true);
	}

	@Override
	public void addOnOpenFileListener(ActionListener listener) {
		mBtnOpenFile.addActionListener(listener);
	}

	@Override
	public void addOnBinarizateListener(ActionListener listener) {
		mBtnBinarization.addActionListener(listener);
	}

	@Override
	public void addOnSkeletonizationListener(ActionListener listener) {
		mBtnSkeletonization.addActionListener(listener);
	}

	@Override
	public void addOnAdjustmentListener(AdjustmentListener listener) {
		mSbPercent.addAdjustmentListener(listener);
	}
}
