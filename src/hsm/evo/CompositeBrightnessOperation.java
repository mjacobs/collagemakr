package hsm.evo;

import hsm.global.Config;
import hsm.image.CompositeAdaptor;

import com.jhlabs.composite.ValueComposite;

public class CompositeBrightnessOperation extends ParametrizedOperationAdaptor {
	
	private static String MY_PROB = "prob_compositebrightness";
	
	static
	{
		Config.getConfig().registerDouble(MY_PROB, 1.0);
		
		OperationMetadata.getInstance().registerOperation(CompositeBrightnessOperation.class);
	
		OperationMetadata.getInstance().setOperationProbability(CompositeBrightnessOperation.class, Config.getConfig().getDouble(MY_PROB));
	}
	
	public CompositeBrightnessOperation()
	{
		super(new CompositeAdaptor(new ValueComposite(1.0f)));
	}
	
	public String toString()
	{
		return "Composite (brightness)";
	}
}
