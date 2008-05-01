package hsm.image;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class ImageLibrary
{
	private File _location;
	private BufferedImage[] _cachedImages = null;
	
	public ImageLibrary(String dir)
	{
		_location = new File(dir);
		
		if (! _location.exists())
		{
			_location.mkdirs();
		}
	}
	
	public void addImage(BufferedImage img)
	{
		try {
			File newFile = File.createTempFile("hsm", ".png", _location);
			
			ImageIO.write(img, "png", newFile);
			
			_cachedImages = null;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public BufferedImage[] getAllImages()
	{
		if (_cachedImages == null)
		{
			File[] imgFiles = _location.listFiles(new ImageTypeFilter());
			BufferedImage[] imgs = new BufferedImage[imgFiles.length];
			
			for (int i=0; i<imgFiles.length; i++)
			{
				try {
					imgs[i] = ImageIO.read(imgFiles[i]);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
			_cachedImages = imgs;
		}
		
		return _cachedImages;
		
	}
	
	public BufferedImage[] getImageThumbs()
	{
		BufferedImage imgs[] = getAllImages();
		
		BufferedImage thumbs[] = new BufferedImage[imgs.length];
		
		for (int i=0; i<imgs.length; i++)
		{
			thumbs[i] = makeThumb(imgs[i], 100, 100);
		}
		
		return thumbs;
	}
	
	private static BufferedImage makeThumb(BufferedImage b, int thumbWidth, int thumbHeight)
	{
	    Image image = b;
	    	    
	    double thumbRatio = (double)thumbWidth / (double)thumbHeight;
	    int imageWidth = image.getWidth(null);
	    int imageHeight = image.getHeight(null);
	    double imageRatio = (double)imageWidth / (double)imageHeight;
	    if (thumbRatio < imageRatio) {
	      thumbHeight = (int)(thumbWidth / imageRatio);
	    } else {
	      thumbWidth = (int)(thumbHeight * imageRatio);
	    }
	    // draw original image to thumbnail image object and
	    // scale it to the new size on-the-fly
	    BufferedImage thumbImage = new BufferedImage(thumbWidth, 
	      thumbHeight, BufferedImage.TYPE_4BYTE_ABGR);
	    Graphics2D graphics2D = thumbImage.createGraphics();
	    graphics2D.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
	      RenderingHints.VALUE_INTERPOLATION_BILINEAR);
	    graphics2D.drawImage(image, 0, 0, thumbWidth, thumbHeight, null);
	    return thumbImage;
	}
}