/**
 * 
 */
package hsm.evo;

import java.awt.image.BufferedImageOp;
import java.util.HashMap;

import hsm.image.BufferedImageOpAdaptor;
import hsm.image.LayerImage;

/**
 * @author bmoore
 *
 */
public abstract class ParametrizedBIOpAdaptor extends ParametrizedOperation {

	protected BufferedImageOpAdaptor _op = null;
	
	protected ParametrizedOperation initWithOpAndParameters(BufferedImageOp op, HashMap<String, Double> p)
	{
		assert(_op == null);
		_op = new BufferedImageOpAdaptor(op);
		
		return super.initWithParameters(p);
	}
	
	public int getNumberOfInputs() {
		assert(_op != null);
		
		return _op.getNumberOfInputs();
	}

	protected LayerImage performInternal(LayerImage... images) {
		assert(_op != null);
		
		return _op.perform(images);
	}

}
