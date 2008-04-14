package hsm.evo;

import hsm.evo.OperationMetadata.PropertyData;
import hsm.image.LayerImage;

import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.AffineTransformOp;

public class RotateOperation extends ParametrizedOperation {

	static
	{
		PropertyData d = OperationMetadata.getInstance().registerOperation(RotateOperation.class);
		d.putProperty("x", 0.0, 500.0);
		d.putProperty("y", 0.0, 500.0);
		d.putProperty("angle", 0.0, 2.0*Math.PI);
	}

	@Override
	public String toString() {
		return "Rotate: by " + getParameter("angle") + " radians about " + getParameter("x") + ", " + getParameter("y");
	}

	@Override
	public int getNumberOfInputs() {

		return 1;
	}

	@Override
	protected LayerImage performInternal(LayerImage... images) {
//		double cx = getParameter("x");
//		double cy = getParameter("y");
//		double ang = getParameter("angle");
//		LayerImage img = images[0];
//		AffineTransform rotate = AffineTransform.getRotateInstance(ang, cx, cy);
//		AffineTransform trans;
//		
//		// we have to be tricky about how we do the rotation, because AffineTransformOp will clip
//		// anything to the left of the y axis or above the x axis
//		Rectangle2D origBounds = img.getBounds();
//		Rectangle2D transBounds = new Rectangle2D.Double();
//		transBounds.add(rotate.transform(new Point2D.Double(origBounds.getMinX(), origBounds.getMinY()), null));
//		transBounds.add(rotate.transform(new Point2D.Double(origBounds.getMinX(), origBounds.getMaxY()), null));
//		transBounds.add(rotate.transform(new Point2D.Double(origBounds.getMaxX(), origBounds.getMaxY()), null));
//		transBounds.add(rotate.transform(new Point2D.Double(origBounds.getMaxX(), origBounds.getMinY()), null));
//		
//		AffineTransformOp op = new AffineTransformOp(trans, AffineTransformOp.TYPE_BILINEAR);
//		
		return null;
	}
	
}
