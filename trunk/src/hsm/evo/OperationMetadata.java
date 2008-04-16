package hsm.evo;

import java.util.HashMap;

public final class OperationMetadata {
	public final class PropertyData
	{
		private class Range
		{
			public Range(double mi, double ma)
			{
				min = mi;
				max = ma;
			}
			
			public double min = 0;
			public double max = 1;
		}
		
		private HashMap<String, Range> _data;
		
		public PropertyData()
		{
			_data = new HashMap<String, Range>();
			
		}
		
		public void putProperty(String name, double min, double max)
		{
			_data.put(name, new Range(min, max));
		}
		
		public String[] getPropertyNames()
		{
			String[] result = new String[_data.size()];
			int idx = 0;
			
			for (String str : _data.keySet())
			{
				result[idx] = str;
				idx++;
			}
			
			return result;
		}
		
		public double getMinimum(String property)
		{
			return _data.get(property).min;
		}
		
		public double getMaximum(String property)
		{
			return _data.get(property).max;
		}
	}
	
	private static OperationMetadata g_instance = null;
	
	public static OperationMetadata getInstance()
	{
		if (g_instance == null)
		{
			g_instance = new OperationMetadata();

			try {
				Class.forName ("hsm.evo.DissolveOperation");
				Class.forName ("hsm.evo.CompositeOverOperation");
				Class.forName ("hsm.evo.CompositeXorOperation");
				Class.forName ("hsm.evo.TranslateOperation");
				Class.forName ("hsm.evo.ScaleOperation");
				Class.forName ("hsm.evo.RotateOperation");
			} catch (ClassNotFoundException e) {
			}
		}
		
		return g_instance;
	}
	
	private HashMap<Class<?>, PropertyData> _propertyData;
	private HashMap<Class<?>, Double> _selectionProbs;
	private double _maxProb = 1.0;
	
	private OperationMetadata()
	{
		_propertyData = new HashMap<Class<?>, PropertyData>();
		_selectionProbs = new HashMap<Class<?>, Double>();
	}
	
	public PropertyData registerOperation(Class<?> key)
	{
		PropertyData pdata = new PropertyData();
		_propertyData.put(key, pdata);
		_selectionProbs.put(key, 1.0);
		
		return pdata;
	}
	
	public Class<?>[] getOperationClasses()
	{
		Class<?>[] result = new Class<?>[_propertyData.size()];
		int idx = 0;
		
		for (Class<?> cl : _propertyData.keySet())
		{
			result[idx] = cl;
			idx++;
		}
		
		return result;
	}
	
	public PropertyData getPropertyData(Class<?> theclass)
	{
		return _propertyData.get(theclass);
	}
	
	public void setOperationProbability(Class<?> opClass, double newProb)
	{
		_selectionProbs.remove(opClass);
		_selectionProbs.put(opClass, newProb);
		
		if (newProb > _maxProb)
		{
			_maxProb = newProb;
		}
	}
	
	public double getOperationProbability(Class<?> opClass)
	{
		// normalize to between 0 and 1
		// ACTUALLY a probability density
		return _selectionProbs.get(opClass)/_maxProb;
	}
}
