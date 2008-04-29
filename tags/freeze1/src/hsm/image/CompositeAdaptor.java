package hsm.image;

import java.awt.AlphaComposite;
import java.awt.Composite;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;

public class CompositeAdaptor extends ImageOperation {
	Composite _composite;
	
	public CompositeAdaptor(Composite c)
	{
		init(c);
	}
	
	public CompositeAdaptor()
	{
		init(AlphaComposite.SrcOver);
	}
	
	private void init(Composite c)
	{
		_composite = c;
	}
	
	@Override
	public int getNumberOfInputs() {
		return 2;
	}

	@Override
	protected LayerImage performInternal(LayerImage... images) {
		LayerImage src = images[0];
		LayerImage dst = images[1];
		
		Rectangle2D srcBounds = src.getBounds();
		Rectangle2D dstBounds = dst.getBounds();
		Rectangle2D unionRect = srcBounds.createUnion(dstBounds);
		
		BufferedImage result = new BufferedImage((int)Math.ceil(unionRect.getWidth()),
												 (int)Math.ceil(unionRect.getHeight()),
												 BufferedImage.TYPE_INT_ARGB);
		
		Rectangle2D resultRect = new Rectangle2D.Double(unionRect.getMinX(), unionRect.getMinY(),
													    result.getWidth(), result.getHeight());
		
		Graphics2D g2d = result.createGraphics();
		g2d.setComposite(_composite);
		
		Point2D.Double srcPos = new Point2D.Double(srcBounds.getMinX() - unionRect.getMinX(),
										    srcBounds.getMinY() - unionRect.getMinY());
		Point2D.Double dstPos = new Point2D.Double(dstBounds.getMinX() - unionRect.getMinX(),
				   							dstBounds.getMinY() - unionRect.getMinY());
		g2d.drawImage(dst.getImage(), new AffineTransformOp(AffineTransform.getTranslateInstance(dstPos.x, dstPos.y), 
	            							AffineTransformOp.TYPE_BILINEAR), 0, 0);
		g2d.drawImage(src.getImage(), new AffineTransformOp(AffineTransform.getTranslateInstance(srcPos.x, srcPos.y), 
												AffineTransformOp.TYPE_BILINEAR), 0, 0);
		g2d.dispose();
		return new LayerImage(result, new Point2D.Double(resultRect.getMinX(), resultRect.getMinY()));
	}

}
