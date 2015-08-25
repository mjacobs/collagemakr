package hsm.evo;

import hsm.evo.OperationMetadata.PropertyData;
import hsm.global.Config;
import hsm.image.BufferedImageOpAdaptor;
import java.util.HashMap;
import com.jhlabs.image.ShadowFilter;

public class ShadowOperation extends ParametrizedOperationAdaptor {
	static
	{
		OperationMetadata om = OperationMetadata.getInstance();
		Config.getConfig().registerDouble("prob_shadow", 1.0);
		
		PropertyData p = om.registerOperation(ShadowOperation.class);
		om.setOperationProbability(ShadowOperation.class, Config.getConfig().getDouble("prob_shadow"));
		
		p.putProperty("angle", 0.0, 2.0*Math.PI);
		p.putProperty("distance", 0.1, 10.0);
		p.putProperty("radius", 0.0, 10.0);
	}
	
	public ParametrizedOperation initWithParameters(HashMap<String, Double> p) {
		ShadowFilter shadow = new ShadowFilter();
		shadow.setAngle(p.get("angle").floatValue());
		shadow.setDistance(p.get("distance").floatValue());
		shadow.setRadius(p.get("radius").floatValue());
		
		BufferedImageOpAdaptor op = new BufferedImageOpAdaptor(shadow);
		
		setOperation(op);
		
		return super.initWithParameters(p);
	}

	public String toString()
	{
		return "Shadow";
	}
}
