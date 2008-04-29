package hsm.image;

import hsm.global.Config;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class DeferredImage {
	
	protected File _file = null;
	protected static final File g_tempDir;
	protected BufferedImage _image = null;
	
	static
	{
		Config.getConfig().registerBoolean("defer_images", true);
		
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
		
		if (Config.getConfig().getBoolean("defer_images"))
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
		else
		{
			_image = img;
		}
		
	}
	
	@Override
	protected void finalize() throws Throwable {
		if (_file != null)
		{
			_file.delete();
		}
		super.finalize();
	}



	public BufferedImage getImage()
	{
		if (_image != null)
		{
			return _image;
		}
		else
		{
			assert(_file != null);
			try {
				return ImageIO.read(_file);
			} catch (IOException e) {
				e.printStackTrace();
				return null;
			}
		}
	}
}
