package hsm.evo;

import hsm.evo.OperationMetadata.PropertyData;
import hsm.global.Config;
import hsm.image.AnnotatedImage;
import hsm.image.DirectorySource;
import hsm.image.ImageException;
import hsm.image.FlickrSource;
import hsm.image.ImageSource;
import hsm.tools.IExtractor;
import hsm.tools.LuminanceExtractor;
import hsm.tools.SimpleExtractor;

import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.util.HashMap;

public class TreeGenerator {

	public final static String OFFLINE = "offline";
	public final static String OFFLINE_DIR = "offline_dir";
	public final static String SHOULD_EXTRACT = "extract";
	public final static String CANVAS_WIDTH = "canvas_width";
	public final static String CANVAS_HEIGHT = "canvas_height";
	public final static String NEW_TREE_HEIGHT = "new_tree_height";
	
	public static int getNewTreeDepth()
	{
		return Config.getConfig().getInt(NEW_TREE_HEIGHT);
	}
	
	static
	{
		Config.getConfig().registerBoolean(OFFLINE, false);
		Config.getConfig().registerString(OFFLINE_DIR, ".");
		Config.getConfig().registerBoolean(SHOULD_EXTRACT, true);
		Config.getConfig().registerInt(CANVAS_WIDTH, 250);
		Config.getConfig().registerInt(CANVAS_HEIGHT, 250);
		Config.getConfig().registerInt(NEW_TREE_HEIGHT, 5);
		
	}
	
	public static class GenerationContext
	{
		private static GenerationContext _ctx = null;
		private IExtractor[] _extractors = { new LuminanceExtractor(), new SimpleExtractor() };
		private double[] _probs = { Config.getConfig().getDouble("prob_extract_luminance"), 
								    Config.getConfig().getDouble("prob_extract_magicwand") };
		private ImageSource _source;
		
		static
		{
			Config.getConfig().registerDouble("prob_extract_luminance", 1.0);
			Config.getConfig().registerDouble("prob_extract_magicwand", 1.0);
			
		}
		
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
			
			if (Config.getConfig().getBoolean(OFFLINE))
			{
				_source = new DirectorySource(Config.getConfig().getString(OFFLINE_DIR));
			}
			else
			{
				try {
					_source = new FlickrSource();
					
				} catch (ImageException e) {
					e.printStackTrace();
				}
			}		
		}
		
		public IExtractor randomExtractor()
		{
			double maxProb = Double.NEGATIVE_INFINITY;
			
			for (double d : _probs)
			{
				if (d > maxProb) maxProb = d;
			}
			
			int randIdx;
			
			do
			{
				randIdx = (int)(Math.random()*_extractors.length);
			} while(Math.random()*maxProb > _probs[randIdx]);
			
			return _extractors[randIdx];
		}
		
		public ImageSource getSource()
		{
			return _source;
		}
	}
	
	public static Class<?> randomOperationClass()
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
	
	public static HashMap<String, Double> randomParametersForClass(Class<?> c)
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
			if (Config.getConfig().getBoolean(SHOULD_EXTRACT))
			{
				extractedImage = GenerationContext.getContext().randomExtractor().getExtract(randImage.getImage());
			}
			else
			{
				extractedImage = randImage.getImage();
			}
		}
		
		System.out.println("done.");
		
		double width, height;
		width = Config.getConfig().getInt(CANVAS_WIDTH);
		height = Config.getConfig().getInt(CANVAS_HEIGHT);
		
		return new ElementNode(new Point2D.Double(Math.random()*width,Math.random()*height),
							   extractedImage, randImage);
	}
	
	
	public static ExpressionNode randomNode(int maxDepth)
	{
		if (maxDepth > 0)
		{
			return randomOperationNode(maxDepth);
		}
		else
		{
			return randomLeafNode();

		}
	}
}
