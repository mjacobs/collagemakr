package hsm.tools;

import java.awt.image.BufferedImage;

public class LuminanceExtractor implements IExtractor
{
	
	public LuminanceExtractor()
	{
		
	}

	public BufferedImage getExtract(BufferedImage image)
	{
		BufferedImage ret = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_4BYTE_ABGR);
		int[] pixels = image.getRGB(0, 0, image.getWidth(), image.getHeight(), null, 0, image.getWidth());
		
		for (int i = 0; i < image.getHeight(); i++)
		{
			for (int j = 0; j < image.getWidth(); j++)
			{
				int rgb = pixels[i * image.getWidth() + j];
				int b =  rgb        & 0xff;
				int g = (rgb >> 8)  & 0xff;
				int r = (rgb >> 16) & 0xff;
				int t = rgb;
				int lum = Math.round((0.257f * r) + (0.504f * g) + (0.098f * b) + 16);
                rgb = ((lum & 0xff) << 24) + rgb;
                pixels[i * image.getWidth() + j] = rgb;
			}
		}
		
		ret.setRGB(0, 0, ret.getWidth(), ret.getHeight(), pixels, 0, ret.getWidth());
		return ret;
	}
}
