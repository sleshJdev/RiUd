package by.slesh.ri.cp.ushkindaria.ipt;

import java.util.Comparator;

public class DilateMorph extends AbstractMorph {

	public DilateMorph() {

		kernel = CIRCLE_KERNEL;
		checkExecute = new Comparator<Integer>() {

			@Override
			public int compare(Integer imagePixel, Integer maskPixel) {

				return (imagePixel - _1) + (maskPixel - _1); // if 0 - then black
			}
		};
		checkPixel = new Comparable<Integer>() {

			@Override
			public int compareTo(Integer imagePixel) {

				return imagePixel - _1; // if black
			}
		};
		newPixelValue = _1;
		initExecuteState = false;
	}
}
