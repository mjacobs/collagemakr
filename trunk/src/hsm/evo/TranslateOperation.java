package hsm.evo;

import hsm.evo.OperationMetadata.PropertyData;
import hsm.image.LayerImage;

import java.awt.geom.Point2D;

public class TranslateOperation extends ParametrizedOperation {

	static
	{
		PropertyData d = OperationMetadata.getInstance().registerOperation(TranslateOperation.class);
		d.putProperty("x", -100.0, 100.0);
		d.putProperty("y", -100.0, 100.0);
	}
	
	@Override
	public String toString() {
		return "Translate: " + getParameter("x") + ", " + getParameter("y");
	}

	@Override
	public int getNumberOfInputs() {
		return 1;
	}

	@Override
	protected LayerImage performInternal(LayerImage... images) {
		LayerImage img  = images[0];
		Point2D loc = img.getLocation();
		LayerImage result = new LayerImage(img.getImage(), 
								new Point2D.Double(loc.getX()+getParameter("x"), 
												   loc.getY()+getParameter("y")));
		return result;
	}
	
}
