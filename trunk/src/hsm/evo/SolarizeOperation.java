package hsm.evo;

import hsm.image.BufferedImageOpAdaptor;
import java.util.HashMap;
import com.jhlabs.image.SolarizeFilter;

public class SolarizeOperation extends ParametrizedOperationAdaptor {
	static
	{
		OperationMetadata om = OperationMetadata.getInstance();
		om.registerOperation(SolarizeOperation.class);
		om.setOperationProbability(DissolveOperation.class, 0.5);
	}
	
	public ParametrizedOperation initWithParameters(HashMap<String, Double> p) {
		BufferedImageOpAdaptor op = new BufferedImageOpAdaptor(new SolarizeFilter());
		
		setOperation(op);
		
		return super.initWithParameters(p);
	}

	public String toString()
	{
		return "Solarize";
	}
}
