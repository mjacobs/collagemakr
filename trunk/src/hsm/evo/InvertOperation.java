package hsm.evo;

import hsm.global.Config;
import hsm.image.BufferedImageOpAdaptor;
import java.util.HashMap;
import com.jhlabs.image.InvertFilter;

public class InvertOperation extends ParametrizedOperationAdaptor {
	static
	{
		Config.getConfig().registerDouble("prob_invert", 1.0);
		
		OperationMetadata om = OperationMetadata.getInstance();
		om.registerOperation(InvertOperation.class);
		om.setOperationProbability(InvertOperation.class, Config.getConfig().getDouble("prob_invert"));
	}
	
	public ParametrizedOperation initWithParameters(HashMap<String, Double> p) {
		BufferedImageOpAdaptor op = new BufferedImageOpAdaptor(new InvertFilter());
		
		setOperation(op);
		
		return super.initWithParameters(p);
	}

	public String toString()
	{
		return "Invert";
	}
}
