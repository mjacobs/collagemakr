package hsm.image;

import java.io.File;

public class DirectorySource implements ImageSource {

	private String _path;
	
	public DirectorySource(String path)
	{
		_path = path;
	}
	
	
	public AnnotatedImage getRandomImage() {
		File dir = new File(_path);
		File[] dirFiles = dir.listFiles(new ImageTypeFilter());
		
		if (dirFiles.length > 0)
		{
			File imgFile = dirFiles[(int)(Math.random()*dirFiles.length)];
			
			try {
				return new AnnotatedImage(imgFile);
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
