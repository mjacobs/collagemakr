package hsm.evo;

import hsm.global.Config;
import hsm.image.CompositeAdaptor;

import com.jhlabs.composite.AddComposite;

public class CompositeAddOperation extends ParametrizedOperationAdaptor {

	static
	{
		Config.getConfig().registerDouble("prob_compositeadd", 1.0);
		
		OperationMetadata.getInstance().registerOperation(CompositeAddOperation.class);
	
		OperationMetadata.getInstance().setOperationProbability(CompositeAddOperation.class, Config.getConfig().getDouble("prob_compositeadd"));
	}
	
	public CompositeAddOperation()
	{
		super(new CompositeAdaptor(new AddComposite(1.0f)));
	}
	
	public String toString()
	{
		return "Composite (add)";
	}
}
