package hsm.evo;

import hsm.image.AnnotatedImage;
import hsm.image.DeferredImage;
import hsm.image.LayerImage;
import hsm.tools.TagChecker;

import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.MutableTreeNode;

public class ElementNode extends ExpressionNode {

	private DeferredImage _image;
	private AnnotatedImage _source;
	private Point2D _center;
	
	public ElementNode(Point2D center, BufferedImage img, AnnotatedImage src)
	{
		_image = new DeferredImage(img);
		_source = src;
		_center = center;
	}
	
	@Override
	public LayerImage evaluate() 
	{
		BufferedImage img = _image.getImage();
		return new LayerImage(img, new Point2D.Double(_center.getX()-img.getWidth()/2.0, 
												  		 _center.getY()-img.getHeight()/2.0));
	}

	@Override
	public ExpressionNode createMutatedInternal(double mutationProb) {
		if (Math.random() <= mutationProb)
		{
			MutationType mutation = ExpressionNode.randomMutationType(false);
			
			switch (mutation)
			{
				case REPLACE:
					// replace with related node
					String tags[] = TagChecker.getInstance().filterTags(_source.getTags());
					
					if (tags.length == 0)
					{
						return TreeGenerator.randomNode(0);
					}
					else
					{
						String randTag = tags[(int)(Math.random()*tags.length)];
						System.out.println("Creating new image related to tag " + randTag);
						
						TreeGenerator.GenerationContext gc = TreeGenerator.GenerationContext.getContext();
						AnnotatedImage img = null;
						BufferedImage extractedImage = null;
						
						while (extractedImage == null)
						{
							img = gc.getSource().getRandomImage(randTag);
							extractedImage = gc.randomExtractor().getExtract(img.getImage());
						}
						
						return new ElementNode(_center, extractedImage, img);
					}
					
				case OP_WRAP:
					return TreeGenerator.randomOperationNode(1, this);
					
				default:
					assert(false);
					return null;
			}
		}
		else
		{
			return this;
		}
	}
	
	public int countLeaves()
	{
		return 1;
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
	
	public int countDescendantsAndSelf()
	{
		return 0;
	}
	
	protected ExpressionNode selectRandomDescendantOrSelf()
	{
		return this;
	}
	
	protected ExpressionNode replaceDescendantOrSelf(ExpressionNode replaceThis, ExpressionNode withThis)
	{
		if (replaceThis != this)
		{
			return this;
		}
		else
		{
			return withThis;
		}
	}
	
	public int treeDepth()
	{
		return 0;
	}

	@Override
	public MutableTreeNode generateJTreeNode() {
		return new DefaultMutableTreeNode("\"" + _source.getTitle() + "\"" + " by " + _source.getOwner());
	}

}
