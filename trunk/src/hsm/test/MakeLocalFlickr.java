package hsm.test;

import java.io.File;

import javax.imageio.ImageIO;

import hsm.image.FlickrSource;

public class MakeLocalFlickr {

	/**
	 * @param args
	 * @throws Throwable 
	 */
	public static void main(String[] args) throws Throwable {
		FlickrSource source = new FlickrSource();
		
		File dir = new File("data/flickr_local/");
		
		if (! dir.exists()) dir.mkdirs();
		
		File currFile;
		
		for (int i=0; i<100; i++)
		{
			System.out.println(i);
			currFile = File.createTempFile("hsm", ".png", dir);
			
			ImageIO.write(source.getRandomImage().getImage(), "png", currFile);
		}
	}

}
