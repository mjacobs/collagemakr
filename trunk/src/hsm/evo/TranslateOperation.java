package hsm.evo;

import hsm.evo.OperationMetadata.PropertyData;

import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.util.HashMap;

public class TranslateOperation extends ParametrizedBIOpAdaptor {

	static
	{
		PropertyData d = OperationMetadata.getInstance().registerOperation(TranslateOperation.class);
		d.putProperty("x", 0.0, 100.0);
		d.putProperty("y", 0.0, 100.0);
	}
	
	@Override
	public ParametrizedOperation initWithParameters(HashMap<String, Double> p) {
		double tx = p.get("x");
		double ty = p.get("y");
		AffineTransformOp op = new AffineTransformOp(AffineTransform.getTranslateInstance(tx, ty), AffineTransformOp.TYPE_BILINEAR);
		
		return super.initWithOpAndParameters(op, p);
	}

	@Override
	public String toString() {
		return "Translate: " + getParameter("x") + ", " + getParameter("y");
	}
	
}
