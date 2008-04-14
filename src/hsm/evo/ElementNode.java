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
	public ExpressionNode createMutatedInternal(double mutationProb) {
		if (Math.random() <= mutationProb)
		{
			MutationType mutation = ExpressionNode.randomMutationType(false);
			
			switch (mutation)
			{
				case REPLACE:
					return TreeGenerator.randomNode(0);
					
				case OP_WRAP:
					return TreeGenerator.randomOperationNode(1, this);
					
				default:
					return null;
			}
		}
		else
		{
			return this;
		}
	}
	
	public void print(String indent)
	{
		System.out.println(indent + "leaf image " + _source.getTitle());
	}
	
}
