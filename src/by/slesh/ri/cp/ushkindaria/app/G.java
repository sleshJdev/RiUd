/**
 * 
 */
package by.slesh.ri.cp.ushkindaria.app;

import by.slesh.ri.cp.ushkindaria.app.view.MainView;

/**
 * @author slesh
 *
 */
public class G {
    public static int INIT_BIN_PERCENT = 8;
    public static int INIT_SEGMENT_THRESHOLD = 250;

    public static final String[] NAMES_FOR_FINDER = { "O", "1", "2", "3", "4" };
    public static final String[] NAMES_FOR_RECOGNIZER = { "0", "1", "2", "3",
	    "4", "5", "6", "7", "8", "9" };

    public static final int QUANTITY_NEURONS_FOR_FINDER = NAMES_FOR_FINDER.length;
    public static final int QUANTITY_NEURONS_FOR_RECOGNIZER = NAMES_FOR_RECOGNIZER.length;

    public static final int WIDTH_FOR_FINDER = 12;
    public static final int HEIGHT_FOR_FINDER = 12;

    public static final int WIDTH_FOR_RECOGNIZER = 100;
    public static final int HEIGHT_FOR_RECOGNIZER = 100;

    public static final int QUANTITY_INPUTS_FOR_FINDER = HEIGHT_FOR_FINDER
	    * WIDTH_FOR_FINDER;
    public static final int QUANTITY_INPUTS_FOR_RECOGNIZER = HEIGHT_FOR_RECOGNIZER
	    * WIDTH_FOR_RECOGNIZER;

    public static final String PATH_WEIGHTS_FINDER = "resources\\teach\\weights\\finder";
    public static final String PATH_WEIGHTS_RECOGNIZER = "resources\\teach\\weights\\recognizer";

    public static final String PATH_SET_FIDNER = "resources\\teach\\set\\finder";
    public static final String PATH_SET_RECOGNIZER = "resources\\teach\\set\\recognizer";

    public static final String PATH_IMAGES_FOR_FINDER = "resources\\teach\\images\\finder";
    public static final String PATH_IMAGES_FOR_RECOGNIZER = "resources\\teach\\images\\recognizer";
}
