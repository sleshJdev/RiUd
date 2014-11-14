package by.slesh.ri.cp.ushkindaria.ipt;

import java.awt.image.BufferedImage;

public class WizardMorph {

	private static final AbstractMorph	MORPH_CLEAN		= new CleanMorph();
	private static final AbstractMorph	MORPH_ERODE		= new ErodeMorph();
	private static final AbstractMorph	MORPH_DILATE	= new DilateMorph();

	public BufferedImage clean(BufferedImage source) {

		return MORPH_CLEAN.morph(source);
	}
	
	public BufferedImage erode(BufferedImage source) {

		return MORPH_ERODE.morph(source);
	}

	public BufferedImage dilate(BufferedImage source) {

		return MORPH_DILATE.morph(source);
	}

	public BufferedImage open(BufferedImage source) {

		return dilate(erode(source));
	}

	public BufferedImage close(BufferedImage source) {

		return erode(dilate(source));
	}
}
