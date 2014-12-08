/**
 * 
 */
package by.slesh.ri.cp.ushkindaria.nn;

import by.slesh.ri.cp.ushkindaria.app.G;

/**
 * @author slesh
 *
 */
public class RecognizerPerseptron extends Perseptron {
    private static RecognizerPerseptron mInstance;

    private RecognizerPerseptron(int quantityNeurons, int quantityInputs) {
	super(quantityNeurons, quantityInputs);
	resizeState = ResizeState.SCALE_UP;
	pathToSaveWeights = G.PATH_WEIGHTS_RECOGNIZER;
	pathToSet = G.PATH_SET_RECOGNIZER;
	targetWidth = G.WIDTH_FOR_RECOGNIZER;
	targetHeight = G.HEIGHT_FOR_RECOGNIZER;
	names = G.NAMES_FOR_RECOGNIZER;
    }

    public static void initInstance(int quantityNeurons, int quantityInputs) {
	if (mInstance == null) {
	    mInstance = new RecognizerPerseptron(quantityNeurons,
		    quantityInputs);
	}
    }

    public static RecognizerPerseptron getInstance() {
	return mInstance;
    }
}
