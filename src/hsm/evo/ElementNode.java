package hsm.evo;

import hsm.image.AnnotatedImage;
import hsm.image.LayerImage;

import java.awt.image.BufferedImage;

public class ElementNode extends ExpressionNode {

	private BufferedImage _image;
	private AnnotatedImage _source;
	
	public ElementNode(BufferedImage img, AnnotatedImage src)
	{
		_image = img;
		_source = src;
	}
	
	@Override
	public LayerImage evaluate() 
	{
		return new LayerImage(_image);
	}

	@Override
	public ExpressionNode createMated(ExpressionNode friend) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ExpressionNode createMutated() {
		// TODO Auto-generated method stub
		return null;
	}
	
	public void print(String indent)
	{
		System.out.println(indent + "leaf image " + _source.getTitle());
	}
	
}
