package hsm.evo;

import java.util.HashMap;

import com.jhlabs.image.OpacityFilter;

import hsm.evo.OperationMetadata.PropertyData;
import hsm.image.BufferedImageOpAdaptor;
import hsm.image.LayerImage;

public class DissolveOperation extends ParametrizedOperation {

	static
	{
		PropertyData d = OperationMetadata.getInstance().registerOperation(DissolveOperation.class);
		d.putProperty("opacity", 0.0, 1.0);
		d.lockDown();
	}
	
	private BufferedImageOpAdaptor _op;
	
	public DissolveOperation()
	{
		_op = null;
	}
	
	public ParametrizedOperation initWithParameters(HashMap<String, Double> p) {
		assert(_op == null);
		
		int opacity = (int) Math.round((p.get("opacity")*255));
		_op = new BufferedImageOpAdaptor(new OpacityFilter(opacity));
		
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
	

	public String toString()
	{
		return "Dissolve, opacity=" + this.getParameter("opacity");
	}

}
