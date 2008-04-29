package hsm.evo;

import hsm.evo.OperationMetadata.PropertyData;
import hsm.global.Config;
import hsm.image.BufferedImageOpAdaptor;
import java.util.HashMap;

import com.jhlabs.image.BoxBlurFilter;

public class BlurOperation extends ParametrizedOperationAdaptor {
	static
	{
		OperationMetadata om = OperationMetadata.getInstance();
		Config.getConfig().registerDouble("prob_blur", 1.0);
		PropertyData p = om.registerOperation(BlurOperation.class);
		om.setOperationProbability(BlurOperation.class, Config.getConfig().getDouble("prob_blur"));
		
		p.putProperty("radius", 1.0, 10.0);
	}
	
	public ParametrizedOperation initWithParameters(HashMap<String, Double> p) {
		// box blur is fast and works well with alpha, unlike gaussian
		BoxBlurFilter blur = new BoxBlurFilter();
		blur.setRadius(p.get("radius").intValue());
		
		BufferedImageOpAdaptor op = new BufferedImageOpAdaptor(blur, p.get("radius").intValue());
		
		setOperation(op);
		
		return super.initWithParameters(p);
	}

	public String toString()
	{
		return "Box Blur, radius = " + getParameter("radius");
	}
}
