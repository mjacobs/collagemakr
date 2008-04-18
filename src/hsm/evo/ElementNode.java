package hsm.evo;

import hsm.image.AnnotatedImage;
import hsm.image.LayerImage;

import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;

public class ElementNode extends ExpressionNode {

	private BufferedImage _image;
	private AnnotatedImage _source;
	private Point2D _center;
	
	public ElementNode(Point2D center, BufferedImage img, AnnotatedImage src)
	{
		_image = img;
		_source = src;
		_center = center;
	}
	
	@Override
	public LayerImage evaluate() 
	{
		return new LayerImage(_image, new Point2D.Double(_center.getX()-_image.getWidth()/2.0, 
												  		 _center.getY()-_image.getHeight()/2.0));
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

	@Override
	protected ExpressionNode createMatedWithLeaf(ElementNode lf) {
		// mating two leaves: pick one at random
		if (Math.random() < 0.5)
		{
			return this;
		}
		else
		{
			return lf;
		}
	}

	@Override
	protected ExpressionNode createMatedWithOperation(OperationNode op) {
		// mating leaf with operation: pick one at random
		if (Math.random() < 0.5)
		{
			return this;
		}
		else
		{
			return op;
		}
	}

	@Override
	protected ExpressionNode visit(ExpressionNode expr) {
		return expr.createMatedWithLeaf(this);
	}
	
}
