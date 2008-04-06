package hsm.image;

import java.awt.image.BufferedImage;

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
}
