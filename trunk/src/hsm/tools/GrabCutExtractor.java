package hsm.tools;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import com.mathworks.jmi.*;

public class GrabCutExtractor implements IExtractor
{
	private static String FS = System.getProperty("file.separator");

	private static String GRABCUT_SCRIPT = "matlab" + FS
			+ "run_GraphCutMain.sh " + FS + "cfarm" + FS + "matlab";

	public GrabCutExtractor()
	{
//		BufferedImage b = null;
//		try
//		{
//			b = getExtract("data" + FS + "samples" + FS + "lillies.jpg");
//		} catch (IOException e)
//		{
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		b.getData();
	}

	public BufferedImage getExtract(BufferedImage bImage)
	{
		
		try
		{
			File tmp = File.createTempFile("gImgTmp", "jpg");
			ImageUtils.writeImage(bImage, tmp);
			return getExtract(tmp.getAbsolutePath());
		} catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public BufferedImage getExtract(String imIn) throws IOException
	{
		String imOut = "gCutExtractTmp.jpg";
		String cmd = GRABCUT_SCRIPT + " " + imIn + " " + imOut;
		Process p = Runtime.getRuntime().exec(cmd);
		File tmpFile = new File(imOut);

		int cnt = 0;

		while (!tmpFile.exists() && cnt < 50)
		{
			try
			{
				Thread.sleep(3000);
			} catch (InterruptedException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			System.out.println("Waiting for GrabCut... " + cnt++);
		}

		if (cnt >= 50)
			return null;

		BufferedImage res = ImageIO.read(tmpFile);
		if (!tmpFile.delete())
			throw new IOException(
					"Couldn't delete temporary file after GrabCut");
		return res;
	}

	public static void main(String[] args) throws IOException
	{
		new GrabCutExtractor();
	}

}
