package hsm.evo;

import hsm.global.Config;
import hsm.image.CompositeAdaptor;

import com.jhlabs.composite.LightenComposite;

public class CompositeLightenOperation extends ParametrizedOperationAdaptor {
	
	private static String MY_PROB = "prob_compositelighten";
	
	static
	{
		Config.getConfig().registerDouble(MY_PROB, 1.0);
		
		OperationMetadata.getInstance().registerOperation(CompositeLightenOperation.class);
	
		OperationMetadata.getInstance().setOperationProbability(CompositeLightenOperation.class, Config.getConfig().getDouble(MY_PROB));
	}
	
	public CompositeLightenOperation()
	{
		super(new CompositeAdaptor(new LightenComposite(1.0f)));
	}
	
	public String toString()
	{
		return "Composite (lighten)";
	}
}
