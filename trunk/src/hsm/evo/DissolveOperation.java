package hsm.evo;

import java.util.HashMap;
import com.jhlabs.image.OpacityFilter;
import hsm.evo.OperationMetadata.PropertyData;
import hsm.image.BufferedImageOpAdaptor;

public class DissolveOperation extends ParametrizedOperationAdaptor {

	static
	{
		PropertyData d = OperationMetadata.getInstance().registerOperation(DissolveOperation.class);
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
