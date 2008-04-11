package hsm.evo;

import java.awt.image.BufferedImageOp;
import java.util.HashMap;
import com.jhlabs.image.OpacityFilter;
import hsm.evo.OperationMetadata.PropertyData;

public class DissolveOperation extends ParametrizedBIOpAdaptor {

	static
	{
		PropertyData d = OperationMetadata.getInstance().registerOperation(DissolveOperation.class);
		d.putProperty("opacity", 0.0, 1.0);
	}
	
	public ParametrizedOperation initWithParameters(HashMap<String, Double> p) {
		int opacity = (int) Math.round((p.get("opacity")*255));
		BufferedImageOp op = new OpacityFilter(opacity);
		
		return super.initWithOpAndParameters(op, p);
	}

	public String toString()
	{
		return "Dissolve, opacity=" + this.getParameter("opacity");
	}

}
