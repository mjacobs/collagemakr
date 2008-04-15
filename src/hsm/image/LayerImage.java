package hsm.image;

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;

public class LayerImage {
	private BufferedImage _image;
	private Point2D _location;
	
	public LayerImage(BufferedImage imageIn, Point2D locIn)
	{
		init(imageIn, locIn);
	}
	
	public LayerImage(BufferedImage imageIn)
	{
		init(imageIn, new Point2D.Double());
	}
	
	private void init(BufferedImage img, Point2D loc)
	{
		_image = img;
		_location = loc;
	}
	
	public BufferedImage getImage()
	{
		return _image;
	}
	
	public Point2D getLocation()
	{
		return _location;
	}
	
	public Rectangle2D.Double getBounds()
	{
		return new Rectangle2D.Double(_location.getX(), _location.getY(), _image.getWidth(), _image.getHeight());
	}
	
	public BufferedImage getFlattenedImage()
	{
		Rectangle2D.Double bounds = getBounds();
		BufferedImage result = new BufferedImage((int)Math.ceil(bounds.getMaxX()), 
											     (int)Math.ceil(bounds.getMaxY()), BufferedImage.TYPE_INT_ARGB);
		
		Graphics2D g = result.createGraphics();
		
		g.drawImage(_image, new AffineTransformOp(AffineTransform.getTranslateInstance(_location.getX(), _location.getY()), 
				            AffineTransformOp.TYPE_BILINEAR), 
				    0, 0);
		
		return result;
	}
}
