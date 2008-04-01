package hsm.test;
import org.junit.*;
import static org.junit.Assert.*;
import hsm.image.*;

public class ImageDownloadTest {
	
	ImageSource _source;
	
	public ImageDownloadTest() throws ImageException
	{
		_source = new FlickrSource();
	}
	 
	public void testDoesDownload() throws ImageException
	{
		AnnotatedImage img = _source.getRandomImage();
		
		// assert we got a non-null image
		assertTrue(img != null);
		
		// assert there are pixels
		assertTrue(img.getImage().getHeight() > 0);
		assertTrue(img.getImage().getWidth() > 0);
		
		assertTrue(img.getTags() != null);
		assertTrue(img.getOwner() != null);
		
		System.out.println("Got picture by " + img.getOwner() + " with tags:");
		System.out.println(img.getTags().toString());
		
	}
	
	@Test 
	public void downloadsMany() throws ImageException
	{
		for (int i=0; i<10; i++)
		{
			testDoesDownload();
		}
	}
}
