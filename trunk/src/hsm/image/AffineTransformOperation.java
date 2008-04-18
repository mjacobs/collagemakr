package hsm.image;


import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;

public class AffineTransformOperation extends ImageOperation {

	AffineTransform _baseTrans;
	
	public AffineTransformOperation(AffineTransform af)
	{
		setTransform(af);
	}
	
	protected void setTransform(AffineTransform af)
	{
		if (af != null)
		{
			_baseTrans = (AffineTransform)af.clone();
		}
		else
		{
			_baseTrans = null;
		}
	}
	
	public int getNumberOfInputs()
	{
		return 1;
	}
	
	@Override
	protected LayerImage performInternal(LayerImage... images) {
		assert(_baseTrans != null);
		
		LayerImage img = images[0];
		BufferedImage imgData = img.getImage();
		
		// see how this will transform the bounding box of this image
		Rectangle2D origBounds = img.getBounds();
		Rectangle2D transBounds = new Rectangle2D.Double();
		transBounds.add(_baseTrans.transform(new Point2D.Double(origBounds.getMinX(), origBounds.getMinY()), null));
		transBounds.add(_baseTrans.transform(new Point2D.Double(origBounds.getMinX(), origBounds.getMaxY()), null));
		transBounds.add(_baseTrans.transform(new Point2D.Double(origBounds.getMaxX(), origBounds.getMaxY()), null));
		transBounds.add(_baseTrans.transform(new Point2D.Double(origBounds.getMaxX(), origBounds.getMinY()), null));
		
		Point2D transOrigin = new Point2D.Double(transBounds.getMinX(), transBounds.getMinY());
		
		// construct a new affine transform
		// third translate the transformed bounding box to the origin
		AffineTransform trans = AffineTransform.getTranslateInstance(-transOrigin.getX(), -transOrigin.getY());
		
		// second do the transformation
		trans.concatenate(_baseTrans);
		
		// first translate source image to its layer position
		trans.concatenate(img.imageToCanvasTransform());
		
		// apply the transformation to the input image into a destination image
		AffineTransformOp op = new AffineTransformOp(trans, AffineTransformOp.TYPE_BILINEAR);
		BufferedImage dstImg = op.createCompatibleDestImage(imgData, ColorModel.getRGBdefault());
		Graphics2D g2d = dstImg.createGraphics();
		g2d.drawImage(img.getImage(), op, 0, 0);
		
		// and return a layer image starting at the proper origin
		return new LayerImage(dstImg, new Point2D.Double(transBounds.getMinX(), transBounds.getMinY()));
	}
	
}
