package hsm.evo;

import java.util.HashMap;

import hsm.evo.OperationMetadata.PropertyData;
import hsm.global.Config;
import hsm.image.BufferedImageOpAdaptor;
import com.jhlabs.image.PosterizeFilter;

public class PosterizeOperation extends ParametrizedOperationAdaptor {
	
	private static String MY_PROB = "prob_posterize";
	
	static
	{
		OperationMetadata om = OperationMetadata.getInstance();
		
		Config.getConfig().registerDouble(MY_PROB, 1.0);
		
		PropertyData props = om.registerOperation(PosterizeOperation.class);
		props.putProperty("levels", 2.0, 12.0);
		om.setOperationProbability(PosterizeOperation.class, Config.getConfig().getDouble(MY_PROB));
	}
	
	public ParametrizedOperation initWithParameters(HashMap<String, Double> p) {
		PosterizeFilter posterize = new PosterizeFilter();
		posterize.setNumLevels(p.get("levels").intValue());
		
		BufferedImageOpAdaptor op = new BufferedImageOpAdaptor(posterize, 0);
		
		setOperation(op);
		
		return super.initWithParameters(p);
	}
	
	public String toString()
	{
		return "Posterize";
	}
}
