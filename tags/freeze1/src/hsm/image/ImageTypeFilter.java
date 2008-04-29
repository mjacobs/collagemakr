package hsm.image;

import java.io.File;
import java.io.FileFilter;

import javax.imageio.ImageIO;

public class ImageTypeFilter implements FileFilter
{

	public boolean accept(File pathname) {
		String[] components = pathname.toString().split("\\.");
		if (components.length > 0)
		{
			return ImageIO.getImageReadersBySuffix(components[components.length-1]).hasNext();
		}
		else
		{
			return false;
		}
	}
	
}