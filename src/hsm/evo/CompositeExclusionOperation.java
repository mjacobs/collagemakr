package hsm.evo;

import hsm.global.Config;
import hsm.image.CompositeAdaptor;

import com.jhlabs.composite.ExclusionComposite;

public class CompositeExclusionOperation extends ParametrizedOperationAdaptor {
	
	private static String MY_PROB = "prob_compositeexclusion";
	
	static
	{
		Config.getConfig().registerDouble(MY_PROB, 1.0);
		
		OperationMetadata.getInstance().registerOperation(CompositeExclusionOperation.class);
	
		OperationMetadata.getInstance().setOperationProbability(CompositeExclusionOperation.class, Config.getConfig().getDouble(MY_PROB));
	}
	
	public CompositeExclusionOperation()
	{
		super(new CompositeAdaptor(new ExclusionComposite(1.0f)));
	}
	
	public String toString()
	{
		return "Composite (exclusion)";
	}
}
