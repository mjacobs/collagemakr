package hsm.evo;

import hsm.evo.OperationMetadata.PropertyData;
import hsm.image.AnnotatedImage;
import hsm.image.FlickrSource;
import hsm.image.ImageException;
import hsm.image.ImageSource;
import hsm.tools.Extraktor;

import java.awt.image.BufferedImage;
import java.util.HashMap;

public class Composition {
	private ExpressionNode _root;
	private BufferedImage _cachedImage;
	
	public Composition(ExpressionNode root)
	{
		_root = root;
		_cachedImage = null;
	}
	
	public BufferedImage getImage()
	{
		if (_cachedImage == null)
		{
			_cachedImage = _root.evaluate().getFlattenedImage();
		}
		
		return _cachedImage;
	}
	
	public void printTree()
	{
		_root.print();
	}
	
	private static class GenerationContext
	{
		private Extraktor _extract;
		private ImageSource _source;
		
		public GenerationContext()
		{
			_extract = new Extraktor();
			try {
				_source = new FlickrSource();
			} catch (ImageException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		public Extraktor getExtractor()
		{
			return _extract;
		}
		
		public ImageSource getSource()
		{
			return _source;
		}
	}
	
	private static enum NodeType {
		OPERATION,
		FEATURE_LEAF
	}

	private static class GenerationParameters
	{
		HashMap<NodeType, Double> _typeProbs;
		
		public GenerationParameters(NodeType[] types, double[] probs)
		{
			assert(types.length == probs.length);
			
			// sum probs so we can normalize later
			double sumProbs = 0.0;
			for (double x : probs) sumProbs += x;
			
			assert(sumProbs > 0.0);
			
			_typeProbs = new HashMap<NodeType, Double>();
			
			for (int i=0; i<types.length; i++)
			{
				_typeProbs.put(types[i], probs[i]/sumProbs);
			}
		}
		
		public double getTypeProbability(NodeType type)
		{
			if (_typeProbs.containsKey(type))
			{
				return _typeProbs.get(type);
			}
			else
			{
				return 0.0;
			}
		}
	}
	
	public static Composition makeRandomComposition()
	{
		NodeType[] typs = {
				NodeType.OPERATION,
				NodeType.FEATURE_LEAF};
		
		double[] probs = {
				1.0,
				0.0
		};
		
		return new Composition(randomNode(3, new GenerationContext(), 
									new GenerationParameters(typs, probs)));
	}
	
	private static NodeType randomType(GenerationParameters params)
	{
		NodeType type;
		NodeType[] vals = NodeType.values();
		
		// rejection sample to get distribution
		do
		{
			type = vals[(int)Math.floor(Math.random()*vals.length)];
		} while (Math.random() > params.getTypeProbability(type));
		
		return type;
	}
	
	
	private static Class<?> randomOperationClass()
	{
		Class<?>[] classes = OperationMetadata.getInstance().getOperationClasses();
		
		return classes[(int)Math.floor(Math.random()*classes.length)];
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
	
	private static ExpressionNode randomNode(int maxDepth, GenerationContext ctx, GenerationParameters params)
	{
		NodeType type;
		
		if (maxDepth > 0)
		{
			type = randomType(params);
		}
		else
		{
			type = NodeType.FEATURE_LEAF;
		}
		
		switch(type)
		{
		case OPERATION:
			Class<?> opClass = randomOperationClass();
			ParametrizedOperation op = null;
			try {
					op = (ParametrizedOperation)opClass.newInstance();
					op.initWithParameters(randomParametersForClass(opClass));
					
				} catch (InstantiationException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			ExpressionNode[] children = new ExpressionNode[op.getNumberOfInputs()];
			
			for (int i=0; i<children.length; i++)
			{
				children[i] = randomNode(maxDepth-1, ctx, params);
			}
			
			return new OperationNode(op, children);
				
		case FEATURE_LEAF:
			AnnotatedImage randImage = null;
			BufferedImage extractedImage = null;
			while (extractedImage == null)
			{
				randImage = ctx.getSource().getRandomImage();
				extractedImage = ctx.getExtractor().getExtract(randImage.getImage());
			}
			
			return new ElementNode(extractedImage, randImage);
		
		default:
			assert(false);
			return null;
		}
	}
}
