package hsm.evo;

import hsm.image.ImageOperation;
import hsm.image.LayerImage;

public abstract class ParametrizedOperationAdaptor extends
		ParametrizedOperation {
	
	protected ImageOperation _op;

	protected ParametrizedOperationAdaptor()
	{
		setOperation(null);
	}
	
	protected ParametrizedOperationAdaptor(ImageOperation op)
	{
		setOperation(op);
	}
	
	protected void setOperation(ImageOperation op)
	{
		_op = op;
	}
	
	@Override
	public int getNumberOfInputs() {
		return _op.getNumberOfInputs();
	}
	
	@Override
	protected LayerImage performInternal(LayerImage... images) {
		return _op.perform(images);
	}
}
