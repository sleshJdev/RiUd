package by.slesh.ri.cp.ushkindaria.app.view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Toolkit;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.image.BufferedImage;
import java.util.Arrays;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class SpectrogramFrameDialog extends JFrame {
	private final class Canvas extends JPanel {
		private static final long serialVersionUID = 3689561413497688099L;
		private final int PADDING_HORIZONTAL = 0;
		private final int PADDING_TOP = 50;
		private final int PADDING_BOOTOM = 40;
		private final int TEXT_HEIGHT = 10;

		public Canvas() {
			setLayout(null);
		}

		@Override
		public void paintComponent(Graphics g) {
			super.paintComponents(g);
			if (mBrightnessRepeats == null) { return; }
			int lineBottomY = mHeight - PADDING_BOOTOM - TEXT_HEIGHT;
			int widthColumn = (int) Math.ceil(mWidth / 256) + 5;
			double factorY = getFactorY();
			double offsetByX = getOffsetByX(widthColumn);
			double x = PADDING_HORIZONTAL;
			g.setColor(Color.WHITE);
			for (int i = 0; i < mBrightnessRepeats.length; ++i) {
				int value = mBrightnessRepeats[i];
				int height = (int) (value * factorY);
				int y = lineBottomY - height;
				g.fillRect((int) x, y, widthColumn, height);
				x += widthColumn + offsetByX;
				if (i % 15 == 0) {
					g.drawString(Integer.toString(i), (int) x, lineBottomY + TEXT_HEIGHT);
				}
			}
			g.drawLine(0, lineBottomY, mWidth, lineBottomY);
			g.drawLine(PADDING_HORIZONTAL, 0, PADDING_HORIZONTAL, mHeight);
			g.drawLine((int) x, 0, (int) x, mHeight);
		}

		private double getFactorY() {
			int[] distances = mBrightnessRepeats.clone();
			Arrays.sort(distances);
			double max = distances[distances.length - 1];
			return (mHeight - PADDING_TOP - PADDING_TOP - TEXT_HEIGHT) / max;
		}

		private double getOffsetByX(int widthColumn) {
			double restSpace = mWidth - 2 * PADDING_HORIZONTAL - widthColumn * 256;
			return restSpace / 255;
		}
	}

	private static final long serialVersionUID = -2080610871629436651L;

	private int mHeight = 300;
	private int mWidth = 700;

	private static SpectrogramFrameDialog mInstance;
	private int[] mBrightnessRepeats;
	private Canvas mCanvas;

	private SpectrogramFrameDialog() {
		super("Спектрограмма");
		setBackground(Color.BLACK);
		initGui();
	}

	public static SpectrogramFrameDialog getInstance() {
		if (mInstance == null) {
			mInstance = new SpectrogramFrameDialog();
		}
		return mInstance;
	}

	public void setData(BufferedImage data) {

		mCanvas.repaint();
	}

	private void initGui() {
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		int x = (int) screenSize.getWidth() - mWidth;
		int y = (int) screenSize.getHeight() - mHeight;
		setBounds(x, y, mWidth, mHeight);
		setDefaultCloseOperation(HIDE_ON_CLOSE);

		addComponentListener(new ComponentAdapter() {

			@Override
			public void componentResized(ComponentEvent e) {
				mWidth = e.getComponent().getWidth();
				mHeight = e.getComponent().getHeight();
				mCanvas.repaint();
				super.componentResized(e);
			}

		});

		mCanvas = new Canvas();
		add(mCanvas);
	}
}
