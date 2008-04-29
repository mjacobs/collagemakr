package hsm.evo;

import hsm.global.Config;
import hsm.image.CompositeAdaptor;

import java.awt.AlphaComposite;

public class CompositeOverOperation extends ParametrizedOperationAdaptor {

	static
	{
		Config.getConfig().registerDouble("prob_compositeover", 1.0);
		
		OperationMetadata.getInstance().registerOperation(CompositeOverOperation.class);
	
		OperationMetadata.getInstance().setOperationProbability(CompositeOverOperation.class, Config.getConfig().getDouble("prob_compositeover"));
	}
	
	public CompositeOverOperation()
	{
		super(new CompositeAdaptor(AlphaComposite.SrcOver));
	}
	
	public String toString()
	{
		return "Composite (over)";
	}
}
