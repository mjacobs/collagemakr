package hsm.evo;

import hsm.image.BufferedImageOpAdaptor;
import java.util.HashMap;
import com.jhlabs.image.InvertFilter;

public class InvertOperation extends ParametrizedOperationAdaptor {
	static
	{
		OperationMetadata om = OperationMetadata.getInstance();
		om.registerOperation(InvertOperation.class);
		om.setOperationProbability(DissolveOperation.class, 2.5);
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
