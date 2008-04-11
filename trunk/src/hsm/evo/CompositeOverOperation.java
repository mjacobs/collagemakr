package hsm.evo;

import hsm.image.CompositeAdaptor;
import hsm.image.LayerImage;

public class CompositeOverOperation extends ParametrizedOperation {

	static
	{
		OperationMetadata.getInstance().registerOperation(CompositeOverOperation.class).lockDown();
	}
	
	private CompositeAdaptor _comp;
	
	public CompositeOverOperation()
	{
		_comp = new CompositeAdaptor();
	}
	
	@Override
	public int getNumberOfInputs() {
		return _comp.getNumberOfInputs();
	}

	@Override
	protected LayerImage performInternal(LayerImage... images) {
		return _comp.perform(images);
	}

}
