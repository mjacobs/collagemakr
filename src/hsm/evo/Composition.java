package hsm.evo;

import java.awt.image.BufferedImage;

public class Composition {
	private ExpressionNode _root;
	private BufferedImage _cachedImage;
	
	public Composition(ExpressionNode root)
	{
		_root = root;
		_cachedImage = null;
	}
	
	public ExpressionNode getRoot()
	{
		return _root;
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
	
	public static Composition randomComposition()
	{
		return new Composition(TreeGenerator.randomNode(4));
	}
	
}
