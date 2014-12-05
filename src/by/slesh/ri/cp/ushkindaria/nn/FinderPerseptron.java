/**
 * 
 */
package by.slesh.ri.cp.ushkindaria.nn;

import by.slesh.ri.cp.ushkindaria.app.G;

/**
 * @author slesh
 *
 */
public class FinderPerseptron extends Perseptron {
    private static FinderPerseptron mInstance;

    private FinderPerseptron(int quantityNeurons, int quantityInputs) {
	super(quantityNeurons, quantityInputs);
	resizeState = ResizeState.SCALE_LOW;
	pathToTrainSet = G.PATH_IMAGES_FOR_FINDER;
	pathToSaveWeights = G.PATH_WEIGHTS_FINDER;
	pathToSet = G.PATH_SET_FIDNER;
	targetWidth = G.WIDTH_FOR_FINDER;
	targetHeight = G.HEIGHT_FOR_FINDER;
	names = G.NAMES_FOR_FINDER;
    }

    public static void initInstance(int quantityNeurons, int quantityInputs) {
	if (mInstance == null) {
	    mInstance = new FinderPerseptron(quantityNeurons, quantityInputs);
	}
    }

    public static FinderPerseptron getInstance() {
	return mInstance;
    }
}
