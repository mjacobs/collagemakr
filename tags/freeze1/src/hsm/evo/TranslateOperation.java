package hsm.evo;

import hsm.evo.OperationMetadata.PropertyData;
import hsm.global.Config;
import hsm.image.LayerImage;

import java.awt.geom.Point2D;

public class TranslateOperation extends ParametrizedOperation {

	static
	{
		OperationMetadata om = OperationMetadata.getInstance();
		
		Config.getConfig().registerDouble("prob_translate", 1.0);
		
		PropertyData d = om.registerOperation(TranslateOperation.class);
		om.setOperationProbability(TranslateOperation.class, Config.getConfig().getDouble("prob_translate"));
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
