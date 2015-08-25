package hsm.image;

import java.awt.Graphics2D;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;

public class BufferedImageOpAdaptor extends ImageOperation {

	BufferedImageOp _operation;
	int _padding = 0;
	boolean _translationInvariant;

	public BufferedImageOpAdaptor(BufferedImageOp op)
	{
		_operation = op;
		_padding = 0;
		_translationInvariant = false;
	}
	
	public BufferedImageOpAdaptor(BufferedImageOp op, int padding)
	{
		_operation = op;
		_padding = padding;
		_translationInvariant = true;
	}
	
	@Override
	public int getNumberOfInputs() {
		return 1;
	}

	@Override
	protected LayerImage performInternal(LayerImage... images) {
		LayerImage img = images[0];
		
		if (_translationInvariant)
		{
			// this means we can just apply the filter straight up to the source image
			BufferedImage paddedImg = new BufferedImage(img.getImage().getWidth() + 2*_padding,
												   img.getImage().getHeight()+ 2*_padding,
												   BufferedImage.TYPE_INT_ARGB);
			
			BufferedImage dest = new BufferedImage(img.getImage().getWidth() + 2*_padding,
					   img.getImage().getHeight()+ 2*_padding,
					   BufferedImage.TYPE_INT_ARGB);

			
			Graphics2D g2d = paddedImg.createGraphics();
			g2d.drawImage(img.getImage(), _padding, _padding, null);
			g2d.dispose();
			
			_operation.filter(paddedImg, dest);
			
			return new LayerImage(dest, new Point2D.Double(img.getLocation().getX() - _padding,
														   img.getLocation().getY() - _padding));
			
		}
		else
		{
			BufferedImage flatImg = img.getFlattenedImage();
			BufferedImage dest = new BufferedImage(flatImg.getWidth(), 
												   flatImg.getHeight(), 
												   BufferedImage.TYPE_INT_ARGB);
			
			_operation.filter(flatImg, dest);
			
			return new LayerImage(dest);
		}
		
		
	}

}
