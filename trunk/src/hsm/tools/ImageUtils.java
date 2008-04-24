package hsm.tools;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class ImageUtils
{
	public static void writeImage(BufferedImage toWrite, File fileout) throws IOException
	{
		ImageIO.write(toWrite, "jpeg", fileout);
	}
	public static void writeImage(BufferedImage toWrite, String fileout)
			throws IOException
	{
		File f = new File(fileout);
		writeImage(toWrite, f);
	}
}
