package hsm.image;

import java.io.File;
import java.io.FileFilter;
import java.util.HashSet;

import javax.imageio.ImageIO;

public class DirectorySource implements ImageSource {

	private String _path;
	
	public DirectorySource(String path)
	{
		_path = path;
	}
	
	private class ImageTypeFilter implements FileFilter
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
	
	public AnnotatedImage getRandomImage() {
		File dir = new File(_path);
		File[] dirFiles = dir.listFiles(new ImageTypeFilter());
		
		if (dirFiles.length > 0)
		{
			File imgFile = dirFiles[(int)(Math.random()*dirFiles.length)];
		
			try {
				return new AnnotatedImage(ImageIO.read(imgFile), imgFile.getName(), "", new HashSet<String>());
			} catch (Throwable e) {
				e.printStackTrace();
				return null;
			}
		}
		else
		{
			System.out.println("There are no files in " + dir.getAbsolutePath());
			return null;
		}
	}

}
