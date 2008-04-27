package hsm.evo;

import hsm.global.Config;
import hsm.image.CompositeAdaptor;

import com.jhlabs.composite.DodgeComposite;

public class CompositeDodgeOperation extends ParametrizedOperationAdaptor {
	
	private static String MY_PROB = "prob_compositedodge";
	
	static
	{
		Config.getConfig().registerDouble(MY_PROB, 1.0);
		
		OperationMetadata.getInstance().registerOperation(CompositeDodgeOperation.class);
	
		OperationMetadata.getInstance().setOperationProbability(CompositeDodgeOperation.class, Config.getConfig().getDouble(MY_PROB));
	}
	
	public CompositeDodgeOperation()
	{
		super(new CompositeAdaptor(new DodgeComposite(1.0f)));
	}
	
	public String toString()
	{
		return "Composite (dodge)";
	}
}
