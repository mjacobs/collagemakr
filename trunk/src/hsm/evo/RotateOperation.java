package hsm.evo;

import hsm.evo.OperationMetadata.PropertyData;
import hsm.image.AffineTransformOperation;
import java.awt.geom.AffineTransform;
import java.util.HashMap;

public class RotateOperation extends ParametrizedOperationAdaptor {

	static
	{
		PropertyData d = OperationMetadata.getInstance().registerOperation(RotateOperation.class);
		d.putProperty("x", 0.0, 500.0);
		d.putProperty("y", 0.0, 500.0);
		d.putProperty("angle", 0.0, 2.0*Math.PI);
	}

	@Override
	public ParametrizedOperation initWithParameters(HashMap<String, Double> p) {
		double cx = p.get("x");
		double cy = p.get("y");
		double ang = p.get("angle");
		
		setOperation(new AffineTransformOperation(AffineTransform.getRotateInstance(ang, cx, cy)));
		
		return super.initWithParameters(p);
	}

	@Override
	public String toString() {
		return "Rotate: by " + getParameter("angle") + " radians about " + getParameter("x") + ", " + getParameter("y");
	}
	
}
