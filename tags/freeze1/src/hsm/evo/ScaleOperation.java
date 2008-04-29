package hsm.evo;

import hsm.evo.OperationMetadata.PropertyData;
import hsm.global.Config;
import hsm.image.AffineTransformOperation;
import java.awt.geom.AffineTransform;
import java.util.HashMap;

public class ScaleOperation extends ParametrizedOperationAdaptor {

	static
	{
		OperationMetadata om = OperationMetadata.getInstance();
		Config.getConfig().registerDouble("prob_scale", 1.0);
		
		PropertyData d = om.registerOperation(ScaleOperation.class);
		om.setOperationProbability(ScaleOperation.class, Config.getConfig().getDouble("prob_scale"));
		d.putProperty("x", 0.1, 2.0);
		d.putProperty("y", 0.1, 2.0);
	}
	
	@Override
	public ParametrizedOperation initWithParameters(HashMap<String, Double> p) {
		double sx = p.get("x");
		double sy = p.get("y");
		AffineTransformOperation op = new AffineTransformOperation(AffineTransform.getScaleInstance(sx, sy));
		
		setOperation(op);
		
		return super.initWithParameters(p);
	}

	@Override
	public String toString() {
		return "Scale: " + getParameter("x") + ", " + getParameter("y");
	}
	
}
