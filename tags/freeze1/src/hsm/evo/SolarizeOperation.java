package hsm.evo;

import hsm.global.Config;
import hsm.image.BufferedImageOpAdaptor;
import java.util.HashMap;
import com.jhlabs.image.SolarizeFilter;

public class SolarizeOperation extends ParametrizedOperationAdaptor {
	static
	{
		OperationMetadata om = OperationMetadata.getInstance();
		Config.getConfig().registerDouble("prob_solarize", 1.0);
		om.registerOperation(SolarizeOperation.class);
		om.setOperationProbability(SolarizeOperation.class, Config.getConfig().getDouble("prob_solarize"));
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
