package by.slesh.ri.cp.ushkindaria.ipt.morph;

import java.awt.image.BufferedImage;

public class WizardMorph {

    private static final AbstractMorph MORPH_CLEAN = new ClearMorph();
    private static final AbstractMorph MORPH_ERODE = new ErodeMorph();
    private static final AbstractMorph MORPH_DILATE = new DilateMorph();

    public static BufferedImage clear(BufferedImage source) {

	return MORPH_CLEAN.morph(source);
    }

    public static BufferedImage erode(BufferedImage source) {

	return MORPH_ERODE.morph(source);
    }

    public static BufferedImage dilate(BufferedImage source) {

	return MORPH_DILATE.morph(source);
    }

    public static BufferedImage open(BufferedImage source) {

	return dilate(erode(source));
    }

    public static BufferedImage close(BufferedImage source) {

	return erode(dilate(source));
    }
}
