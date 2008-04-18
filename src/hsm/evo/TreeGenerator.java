package hsm.evo;

import hsm.evo.OperationMetadata.PropertyData;
import hsm.image.AnnotatedImage;
//import hsm.image.DirectorySource;
import hsm.image.FlickrSource;
import hsm.image.ImageException;
//import hsm.image.FlickrSource;
//import hsm.image.ImageException;
import hsm.image.ImageSource;
import hsm.tools.IExtractor;
import hsm.tools.SimpleExtractor;

import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.util.HashMap;

public class TreeGenerator {
	public final static int DEFAULT_DEPTH = 5;
	
	private static class GenerationContext
	{
		private static GenerationContext _ctx = null;
		private IExtractor _extract;
		private ImageSource _source;
		
		public static GenerationContext getContext()
		{
			if (_ctx == null)
			{
				_ctx = new GenerationContext();
			}
			
			return _ctx;
		}
		
		private GenerationContext()
		{
			assert(_ctx == null);
			
			// TODO: Abstract the creation of the extractor so that we're not hard coding in
			// a new simpleextractor
			_extract = new SimpleExtractor();
			//_source = new DirectorySource("data/samples");
			try {
				_source = new FlickrSource();
				
			} catch (ImageException e) {
				e.printStackTrace();
			}
		}
		
		public IExtractor getExtractor()
		{
			return _extract;
		}
		
		public ImageSource getSource()
		{
			return _source;
		}
	}
	
	private static Class<?> randomOperationClass()
	{
		OperationMetadata om = OperationMetadata.getInstance();
		Class<?>[] classes = om.getOperationClasses();
		
		Class<?> currClass;
		
		do
		{
			currClass = classes[(int)Math.floor(Math.random()*classes.length)];
		} while (Math.random() > om.getOperationProbability(currClass));
		
		return currClass;
	}
	
	private static HashMap<String, Double> randomParametersForClass(Class<?> c)
	{
		PropertyData props = OperationMetadata.getInstance().getPropertyData(c);
		HashMap<String, Double> result = new HashMap<String, Double>();
		double randValue;
		double max;
		double min;
		
		for (String propName : props.getPropertyNames())
		{
			max = props.getMaximum(propName);
			min = props.getMinimum(propName);
			randValue = min + Math.random()*(max-min);
			
			result.put(propName, randValue);
		}
		
		return result;
	}
	
	public static ParametrizedOperation randomOperation()
	{
		ParametrizedOperation op = randomUninitializedOperation();
		
		return op.initWithParameters(randomParametersForClass(op.getClass()));
	}
	
	public static ParametrizedOperation randomUninitializedOperation()
	{
		Class<?> opClass = randomOperationClass();
		ParametrizedOperation op = null;
		try {
				op = (ParametrizedOperation)opClass.newInstance();
				
			} catch (InstantiationException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
			
		return op;
	}
	
	public static OperationNode randomOperationNode(int maxDepth)
	{
		return randomOperationNode(maxDepth, null);
	}
	
	public static OperationNode randomOperationNode(int maxDepth, ExpressionNode includeChild)
	{
		ParametrizedOperation op = randomOperation();
			
		ExpressionNode[] children = new ExpressionNode[op.getNumberOfInputs()];
		
		int includeIdx = -1;
		
		if (includeChild != null)
		{
			includeIdx = (int)(Math.random()*children.length);
		}
		
		for (int i=0; i<children.length; i++)
		{
			if (i == includeIdx)
			{
				children[i] = includeChild;
			}
			else
			{
				children[i] = randomNode(maxDepth-1);
			}	
		}
		
		return new OperationNode(op, children);
	}
	
	public static ElementNode randomLeafNode()
	{
		AnnotatedImage randImage = null;
		BufferedImage extractedImage = null;
		
		System.out.print("Making leaf... ");
		
		while (extractedImage == null)
		{
			randImage = GenerationContext.getContext().getSource().getRandomImage();
			extractedImage = GenerationContext.getContext().getExtractor().getExtract(randImage.getImage());
		}
		
		System.out.println("done.");
		
		return new ElementNode(new Point2D.Double(Math.random()*250.0,Math.random()*250.0),
							   extractedImage, randImage);
	}
	
	
	public static ExpressionNode randomNode(int maxDepth)
	{
		if (maxDepth > 0)
		{
			return randomOperationNode(maxDepth-1);
		}
		else
		{
			return randomLeafNode();

		}
	}
}