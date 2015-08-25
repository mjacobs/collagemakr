package hsm.test;

import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;

import hsm.evo.TreeGenerator;
import hsm.image.AnnotatedImage;
import hsm.image.FlickrSource;
import hsm.tools.TagChecker;

public class MakeLocalFlickr {

	public static final boolean extract = true;
	public static final int numPerTag = 10;
	
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

		String[] allTags = TagChecker.getInstance().getTags();
		int numImages = allTags.length * numPerTag;
		
		for (int i=35; i<allTags.length; i++)
		{
			for (int j=0; j<numPerTag; j++)
			{
				System.out.println((i*numPerTag + j) + " out of " + numImages);
				
				AnnotatedImage sourceImg = source.getRandomImage(allTags[i]);
				BufferedImage img = sourceImg.getImage();
				
				if (extract)
					img = TreeGenerator.GenerationContext.getContext().randomExtractor().getExtract(img);
				
				if (img != null)
				{
					currFile = File.createTempFile("hsm", ".png", dir);
					sourceImg.saveToDisk(currFile);
					ImageIO.write(img, "png", currFile);
				}
			}
		}
	}

}
