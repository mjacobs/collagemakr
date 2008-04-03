package hsm.image;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

public class LayerImage {
	private BufferedImage _image;
	private Point2D.Float _location;
	
	public LayerImage(BufferedImage imageIn, Point2D.Float locIn)
	{
		init(imageIn, locIn);
	}
	
	public LayerImage(BufferedImage imageIn)
	{
		init(imageIn, new Point2D.Float());
	}
	
	private void init(BufferedImage img, Point2D.Float loc)
	{
		_image = img;
		_location = loc;
	}
	
	public BufferedImage getImage()
	{
		return _image;
	}
	
	public Point2D.Float getLocation()
	{
		return _location;
	}
	
	public Rectangle2D.Float getBounds()
	{
		return new Rectangle2D.Float(_location.x, _location.y, _image.getWidth(), _image.getHeight());
	}
	
	public BufferedImage getFlattenedImage(int width, int height)
	{
		BufferedImage result = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		
		Graphics2D g = result.createGraphics();
		
		// the rounding the location isn't ideal- should probably filter...
		g.drawImage(_image, Math.round(_location.x), Math.round(_location.y), new Color(0,0,0,0), null);
		
		return result;
	}
}
