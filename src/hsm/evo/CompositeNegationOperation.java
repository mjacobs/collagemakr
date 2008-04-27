package hsm.evo;

import hsm.global.Config;
import hsm.image.CompositeAdaptor;

import com.jhlabs.composite.NegationComposite;

public class CompositeNegationOperation extends ParametrizedOperationAdaptor {
	
	private static String MY_PROB = "prob_compositenegation";
	
	static
	{
		Config.getConfig().registerDouble(MY_PROB, 1.0);
		
		OperationMetadata.getInstance().registerOperation(CompositeNegationOperation.class);
	
		OperationMetadata.getInstance().setOperationProbability(CompositeNegationOperation.class, Config.getConfig().getDouble(MY_PROB));
	}
	
	public CompositeNegationOperation()
	{
		super(new CompositeAdaptor(new NegationComposite(1.0f)));
	}
	
	public String toString()
	{
		return "Composite (negation)";
	}
}
