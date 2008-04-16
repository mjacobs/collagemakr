package hsm.evo;

import hsm.evo.OperationMetadata.PropertyData;
import hsm.image.AffineTransformOperation;
import java.awt.geom.AffineTransform;
import java.util.HashMap;

public class ScaleOperation extends ParametrizedOperationAdaptor {

	static
	{
		PropertyData d = OperationMetadata.getInstance().registerOperation(ScaleOperation.class);
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
