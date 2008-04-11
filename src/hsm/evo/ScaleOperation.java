package hsm.evo;

import hsm.evo.OperationMetadata.PropertyData;

import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.util.HashMap;

public class ScaleOperation extends ParametrizedBIOpAdaptor {

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
		AffineTransformOp op = new AffineTransformOp(AffineTransform.getScaleInstance(sx, sy), AffineTransformOp.TYPE_BILINEAR);
		
		return super.initWithOpAndParameters(op, p);
	}

	@Override
	public String toString() {
		return "Scale: " + getParameter("x") + ", " + getParameter("y");
	}
	
}
