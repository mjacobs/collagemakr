package hsm.evo;

import hsm.global.Config;
import hsm.image.CompositeAdaptor;

import com.jhlabs.composite.ScreenComposite;

public class CompositeScreenOperation extends ParametrizedOperationAdaptor {
	
	private static String MY_PROB = "prob_compositescreen";
	
	static
	{
		Config.getConfig().registerDouble(MY_PROB, 1.0);
		
		OperationMetadata.getInstance().registerOperation(CompositeScreenOperation.class);
	
		OperationMetadata.getInstance().setOperationProbability(CompositeScreenOperation.class, Config.getConfig().getDouble(MY_PROB));
	}
	
	public CompositeScreenOperation()
	{
		super(new CompositeAdaptor(new ScreenComposite(1.0f)));
	}
	
	public String toString()
	{
		return "Composite (screen)";
	}
}
