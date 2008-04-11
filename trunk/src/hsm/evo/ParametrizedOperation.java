package hsm.evo;

import java.util.HashMap;

import hsm.evo.OperationMetadata.PropertyData;
import hsm.image.ImageOperation;

public abstract class ParametrizedOperation extends ImageOperation {
	protected HashMap<String, Double> _params;
	
	// notes for subclasses: 
	// - should provide empty constructor
	// - should register properties with OperationMetadata in static block
	public ParametrizedOperation initWithParameters(HashMap<String, Double> p)
	{
		_params = new HashMap<String, Double>(p);
		
		return this;
	}
	
	public PropertyData getPropertyData()
	{
		return OperationMetadata.getInstance().getPropertyData(this.getClass());
	}
	
	public double getParameter(String name)
	{
		return _params.get(name);
	}
}
