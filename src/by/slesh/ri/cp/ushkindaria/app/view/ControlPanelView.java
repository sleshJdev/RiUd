package by.slesh.ri.cp.ushkindaria.app.view;

import java.awt.Component;
import java.awt.Container;
import java.awt.GridLayout;
import java.awt.event.ActionListener;
import java.awt.event.AdjustmentListener;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JTextField;

import by.slesh.ri.cp.ushkindaria.app.G;
import by.slesh.ri.cp.ushkindaria.app.view.service.ControlViewInterface;

public class ControlPanelView extends JPanel implements ControlViewInterface {

	private static final long	serialVersionUID	= -2454572918534173516L;

	private JTextField			percentValueTextField;
	private JScrollBar			percentScrollBar;
	private JButton				binarizationButton;
	private JTextField			segmentThresholdValueTextField;
	private JScrollBar			segmentThresholScrollBar;
	private JButton				histogramSegmentButton;
	private JButton				skeletonizationButton;
	private JButton				openFileButton;
	private JButton				bugSegmentButton;
	private JButton				resetButton;
	private JButton				trimButton;
	private JButton				extractButton;
	private JButton				dilateButton;
	private JButton				erodeButton;
	private JButton				neuralNetworkUseButton;

	public ControlPanelView() {

		openFileButton = new JButton("��������� �����������");
		openFileButton.setActionCommand(ACTION_FILE_OPEN);

		skeletonizationButton = new JButton("������� �����-����");
		skeletonizationButton.setActionCommand(ACTION_SKELETONIZATION);
		skeletonizationButton.setEnabled(false);

		bugSegmentButton = new JButton("�������� ����");
		bugSegmentButton.setActionCommand(ACTION_SEGMENT_BUG);
		bugSegmentButton.setEnabled(false);

		/* Binarization controls */
		binarizationButton = createButton("������������", ACTION_BINARIZATION, false);
		percentScrollBar = new JScrollBar(JScrollBar.HORIZONTAL, G.INIT_BIN_PERCENT, 1, 0, 100);
		percentScrollBar.setEnabled(false);
		percentValueTextField = new JTextField("������� ��� ����������� = " + G.INIT_BIN_PERCENT);
		percentValueTextField.setHorizontalAlignment(JTextField.CENTER);
		percentValueTextField.setEditable(false);
		/* =========== */

		/* Segment controls */
		histogramSegmentButton = createButton("��������������", ACTION_SEGMENT_HISTOGRAM, false);
		segmentThresholScrollBar = new JScrollBar(JScrollBar.HORIZONTAL, G.INIT_SEGMENT_THRESHOLD, 1, 0, 500);
		segmentThresholScrollBar.setEnabled(false);
		segmentThresholdValueTextField = new JTextField("����� ����������� = " + G.INIT_SEGMENT_THRESHOLD);
		segmentThresholdValueTextField.setHorizontalAlignment(JTextField.CENTER);
		segmentThresholdValueTextField.setEditable(false);
		/* =========== */

		resetButton = createButton("��������", ACTION_RESET, false);
		trimButton = createButton("�������� ����(40 ��������)", ACTION_TRIM, false);
		extractButton = createButton("�������� ������������ �������", ACTION_EXTRACT, false);
		dilateButton = createButton("����������", ACTION_DILATE, false);
		erodeButton = createButton("������", ACTION_ERODE, false);
		neuralNetworkUseButton = createButton("������������ ���������", ACTION_NEURALNETWORK, false);
		
		setLayout(new GridLayout(6, 3));

		add(openFileButton);
		add(skeletonizationButton);
		add(bugSegmentButton);

		add(binarizationButton);
		add(percentScrollBar);
		add(percentValueTextField);

		add(trimButton);
		add(erodeButton);
		add(dilateButton);
		
		add(histogramSegmentButton);
		add(segmentThresholScrollBar);
		add(segmentThresholdValueTextField);

		add(resetButton);
		add(neuralNetworkUseButton);
		add(extractButton);
	}

	private JButton createButton(String caption, String actionCommand, boolean isEnable) {

		JButton button = new JButton(caption);
		button.setActionCommand(actionCommand);
		button.setEnabled(isEnable);
		return button;
	}

	public void enableComponents(Container container, boolean enable) {

		Component[] components = container.getComponents();
		for (Component component : components) {
			component.setEnabled(enable);
			if (component instanceof Container) {
				enableComponents((Container) component, enable);
			}
		}
	}

	@Override
	public void updatePercentValue(int value) {

		String text = "������� ��� ����������� = " + value;
		percentValueTextField.setText(text);
	}

	@Override
	public void updateSegmentThresholdValue(int value) {

		String text = "����� ����������� = " + value;
		segmentThresholdValueTextField.setText(text);
	}

	@Override
	public void enableControls() {

		enableComponents(this, true);
	}

	@Override
	public void addOpenFileClickListener(ActionListener l) {

		openFileButton.addActionListener(l);
	}

	@Override
	public void addBinarizateClickListener(ActionListener l) {

		binarizationButton.addActionListener(l);
	}

	@Override
	public void addSkeletonizationClickListener(ActionListener l) {

		skeletonizationButton.addActionListener(l);
	}

	@Override
	public void addBinPercentChangeValueListener(AdjustmentListener l) {

		percentScrollBar.addAdjustmentListener(l);
	}

	@Override
	public void addSegmentThresholdChangeValueListener(AdjustmentListener l) {

		segmentThresholScrollBar.addAdjustmentListener(l);
	}

	@Override
	public void addHistogramSegmentClickListener(ActionListener l) {

		histogramSegmentButton.addActionListener(l);
	}

	@Override
	public void addBugSegmentClickListener(ActionListener l) {

		bugSegmentButton.addActionListener(l);
	}

	@Override
	public void addResetClickListener(ActionListener l) {

		resetButton.addActionListener(l);
	}

	@Override
	public void addErodeClickListener(ActionListener l) {

		erodeButton.addActionListener(l);
	}

	@Override
	public void addDilateClickListener(ActionListener l) {

		dilateButton.addActionListener(l);
	}

	@Override
	public void addExtractClickListener(ActionListener l) {

		extractButton.addActionListener(l);
	}

	@Override
	public void addTrimClickListener(ActionListener l) {

		trimButton.addActionListener(l);
	}

	@Override
	public void addNeuralNetworkClickListener(ActionListener l) {

		neuralNetworkUseButton.addActionListener(l);
	}

}
