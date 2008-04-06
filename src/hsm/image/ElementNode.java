package hsm.image;

import java.awt.image.BufferedImage;

public class ElementNode implements ExpressionNode {

	private BufferedImage _image;
	
	public ElementNode(BufferedImage img)
	{
		_image = img;
	}
	
	@Override
	public LayerImage evaluate() 
	{
		return new LayerImage(_image);
	}
	
}
