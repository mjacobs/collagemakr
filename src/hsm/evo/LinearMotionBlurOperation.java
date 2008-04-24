package hsm.evo;

import hsm.evo.OperationMetadata.PropertyData;
import hsm.image.BufferedImageOpAdaptor;
import java.util.HashMap;

import com.jhlabs.image.MotionBlurFilter;

public class LinearMotionBlurOperation extends ParametrizedOperationAdaptor {
	static
	{
		OperationMetadata om = OperationMetadata.getInstance();
		PropertyData p = om.registerOperation(LinearMotionBlurOperation.class);
		om.setOperationProbability(DissolveOperation.class, 1.5);
		
		p.putProperty("angle", 0.0, 2.0*Math.PI);
		p.putProperty("distance", 1.0, 50.0);
	}
	
	public ParametrizedOperation initWithParameters(HashMap<String, Double> p) {
		MotionBlurFilter blur = new MotionBlurFilter();
		
		blur.setAngle(p.get("angle").floatValue());
		blur.setDistance(p.get("distance").floatValue());
		
		BufferedImageOpAdaptor op = new BufferedImageOpAdaptor(blur, p.get("distance").intValue());
		
		setOperation(op);
		
		return super.initWithParameters(p);
	}

	public String toString()
	{
		return "Motion Blur";
	}
}
