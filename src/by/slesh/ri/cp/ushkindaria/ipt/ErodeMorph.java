package by.slesh.ri.cp.ushkindaria.ipt;

import java.util.Comparator;

public class ErodeMorph extends AbstractMorph {

	public ErodeMorph() {

		kernel = CIRCLE_KERNEL;
		checkExecute = new Comparator<Integer>() {

			@Override
			public int compare(Integer imagePixel, Integer maskPixel) {

				return (imagePixel - _0) + (maskPixel - _1);
			}
		};
		checkPixel = new Comparable<Integer>() {

			@Override
			public int compareTo(Integer imagePixel) {

				return imagePixel - _0;
			}
		};
		newPixelValue = _0;
		initExecuteState = false;
	}
}
