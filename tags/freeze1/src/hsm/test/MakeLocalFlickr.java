package hsm.test;

import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;

import hsm.evo.TreeGenerator;
import hsm.image.FlickrSource;

public class MakeLocalFlickr {

	public static final boolean extract = false;
	
	/**
	 * @param args
	 * @throws Throwable 
	 */
	public static void main(String[] args) throws Throwable {
		FlickrSource source = new FlickrSource();
		
		File dir;
		
		if (extract)
			dir = new File("data/features/");
		else
			dir = new File("data/flickr_local");
		
		if (! dir.exists()) dir.mkdirs();
		
		File currFile;
		
		for (int i=0; i<100; i++)
		{
			System.out.println(i);
			
			BufferedImage img = source.getRandomImage().getImage();
			
			if (extract)
				img = TreeGenerator.GenerationContext.getContext().randomExtractor().getExtract(img);
			
			if (img != null)
			{
				currFile = File.createTempFile("hsm", ".png", dir);
				
				ImageIO.write(img, "png", currFile);
			}
		}
	}

}