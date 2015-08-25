package hsm.evo;

import java.util.HashMap;
import com.jhlabs.image.OpacityFilter;
import hsm.evo.OperationMetadata.PropertyData;
import hsm.global.Config;
import hsm.image.BufferedImageOpAdaptor;

public class DissolveOperation extends ParametrizedOperationAdaptor {

	static
	{
		Config.getConfig().registerDouble("prob_dissolve", 1.0);
		
		OperationMetadata om = OperationMetadata.getInstance();
		PropertyData d = om.registerOperation(DissolveOperation.class);
		om.setOperationProbability(DissolveOperation.class, Config.getConfig().getDouble("prob_dissolve"));
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
