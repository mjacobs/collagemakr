package hsm.image;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class DeferredImage {
	
	protected File _file;
	protected static final File g_tempDir;
	
	static
	{
		g_tempDir = new File("temp/img/");
		
		if (! g_tempDir.exists())
		{
			g_tempDir.mkdirs();
		}
		
		File[] dirFiles = g_tempDir.listFiles();
		
		for (File f : dirFiles)
		{
			f.delete();
		}
	}
	
	public DeferredImage(BufferedImage img)
	{
		
		
		try {
			_file = File.createTempFile("hsm", ".png", g_tempDir);
			_file.deleteOnExit(); // just in case...
			
			ImageIO.write(img, "png", _file);
		} catch (IOException e) {
			e.printStackTrace();
			_file = null;
		}
		
		
	}
	
	@Override
	protected void finalize() throws Throwable {
		_file.delete();
		
		super.finalize();
	}



	public BufferedImage getImage()
	{
		try {
			return ImageIO.read(_file);
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}
}
