package hsm.evo;

import hsm.global.Config;
import hsm.image.CompositeAdaptor;

import com.jhlabs.composite.DifferenceComposite;

public class CompositeDifferenceOperation extends ParametrizedOperationAdaptor {

	static
	{
		Config.getConfig().registerDouble("prob_compositedifference", 1.0);
		
		OperationMetadata.getInstance().registerOperation(CompositeDifferenceOperation.class);
	
		OperationMetadata.getInstance().setOperationProbability(CompositeDifferenceOperation.class, Config.getConfig().getDouble("prob_compositedifference"));
	}
	
	public CompositeDifferenceOperation()
	{
		super(new CompositeAdaptor(new DifferenceComposite(1.0f)));
	}
	
	public String toString()
	{
		return "Composite (difference)";
	}
}
