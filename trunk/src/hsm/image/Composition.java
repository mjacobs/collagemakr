package hsm.image;

import hsm.tools.Extraktor;

import java.awt.AlphaComposite;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.util.HashMap;

import com.jhlabs.image.OpacityFilter;

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
		COMPOSITE_OVER,
		COMPOSITE_XOR,
		DISSOLVE,
		FEATURE_LEAF,
		ROTATE,
		TRANSLATE,
		SCALE
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
				NodeType.COMPOSITE_OVER,
				NodeType.COMPOSITE_XOR,
				NodeType.DISSOLVE,
				NodeType.ROTATE,
				NodeType.TRANSLATE,
				NodeType.SCALE,
				NodeType.FEATURE_LEAF};
		
		double[] probs = {
				0.2,
				0.2,
				0.1,
				0.0,
				0.4,
				0.2,
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
		case COMPOSITE_OVER:
			return new OperationNode(new CompositeAdaptor(AlphaComposite.SrcOver), randomNode(maxDepth-1, ctx, params), randomNode(maxDepth-1, ctx, params));
			
		case COMPOSITE_XOR:
			return new OperationNode(new CompositeAdaptor(AlphaComposite.Xor), randomNode(maxDepth-1, ctx, params), randomNode(maxDepth-1, ctx, params));
			
		case DISSOLVE:
			return new OperationNode(new BufferedImageOpAdaptor(new OpacityFilter((int)Math.round(Math.random()*0.8*256))), randomNode(maxDepth-1, ctx, params));
			
		case ROTATE:
		case TRANSLATE:
		case SCALE:
			AffineTransform af = null;
			
			switch (type)
			{
			case ROTATE:
				af = AffineTransform.getRotateInstance(Math.random()*2.0*Math.PI);
				break;
				
			case TRANSLATE:
				af = AffineTransform.getTranslateInstance(Math.random()*500, Math.random()*500);
				break;
				
			case SCALE:
				af = AffineTransform.getScaleInstance(Math.random()*1.5 + 0.5, Math.random()*1.5 + 0.5);
				break;
				
			}
			
			return new OperationNode(new BufferedImageOpAdaptor(new AffineTransformOp(af, AffineTransformOp.TYPE_BILINEAR)), randomNode(maxDepth-1, ctx, params));
			
		case FEATURE_LEAF:
			return new ElementNode(ctx.getExtractor().getExtract(ctx.getSource().getRandomImage().getImage()));
		
		default:
			assert(false);
			return null;
		}
	}
}
