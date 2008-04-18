package hsm.evo;

import java.util.HashMap;
import com.jhlabs.image.OpacityFilter;
import hsm.evo.OperationMetadata.PropertyData;
import hsm.image.BufferedImageOpAdaptor;

public class DissolveOperation extends ParametrizedOperationAdaptor {

	static
	{
		OperationMetadata om = OperationMetadata.getInstance();
		PropertyData d = om.registerOperation(DissolveOperation.class);
		om.setOperationProbability(DissolveOperation.class, 0.5);
		d.putProperty("opacity", 0.0, 2.0);
	}
	
	public ParametrizedOperation initWithParameters(HashMap<String, Double> p) {
		int opacity = (int) Math.round((p.get("opacity")*255));
		BufferedImageOpAdaptor op = new BufferedImageOpAdaptor(new OpacityFilter(opacity));
		
		setOperation(op);
		
		return super.initWithParameters(p);
	}

	public String toString()
	{
		return "Dissolve, opacity=" + this.getParameter("opacity");
	}

}
