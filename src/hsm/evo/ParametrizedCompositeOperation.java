package hsm.evo;

import java.awt.Composite;
import hsm.image.CompositeAdaptor;
import hsm.image.LayerImage;

public abstract class ParametrizedCompositeOperation extends
		ParametrizedOperation {
	
	protected CompositeAdaptor _comp;
	
	protected ParametrizedCompositeOperation(Composite comp)
	{
		_comp = new CompositeAdaptor(comp);
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
