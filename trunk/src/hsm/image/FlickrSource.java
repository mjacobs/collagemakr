package hsm.image;

import hsm.global.Config;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Collection;
import java.util.LinkedList;
import java.util.Vector;

import org.xml.sax.SAXException;

import com.aetrion.flickr.Flickr;
import com.aetrion.flickr.FlickrException;
import com.aetrion.flickr.people.PeopleInterface;
import com.aetrion.flickr.people.User;
import com.aetrion.flickr.photos.Photo;
import com.aetrion.flickr.photos.PhotoList;
import com.aetrion.flickr.photos.PhotosInterface;
import com.aetrion.flickr.photos.SearchParameters;
import com.aetrion.flickr.photos.Size;
import com.aetrion.flickr.tags.Tag;
import com.aetrion.flickr.tags.TagsInterface;


import java.util.Random;

/**
 * @author bmoore
 *
 */
public class FlickrSource implements ImageSource {

	private Flickr _flickr;
	private Random _rand;
	private SearchParameters _query;
	private Vector<Photo> _photoPool;
	
	private final boolean recent = false;
	
	public static final String FLICKR_SIZE = "flickr_size";
	
	static
	{
		Config.getConfig().registerString(FLICKR_SIZE, "medium");
	}
	
	@SuppressWarnings("unchecked")
	public FlickrSource() throws ImageException
	{
		//Flickr.debugStream = true;
		
		_rand = new Random();
		_flickr = new Flickr("d989e76ce73619396ca877158e11aac2");

//		try {
//			Collection<License> lics = (Collection<License>)_flickr.getLicensesInterface().getInfo();
//			for (License li : lics)
//			{
//				System.out.println(li.getId() + " " + li.getName());
//			}
//		} catch (IOException e1) {
//			// TODO Auto-generated catch block
//			e1.printStackTrace();
//		} catch (SAXException e1) {
//			// TODO Auto-generated catch block
//			e1.printStackTrace();
//		} catch (FlickrException e1) {
//			// TODO Auto-generated catch block
//			e1.printStackTrace();
//		}
		
		// create the query
		_query = new SearchParameters();
		_query.setSafeSearch(Flickr.SAFETYLEVEL_SAFE);
		//_query.setText("llama");
		String tgs[] = {"car"};
		_query.setTags(tgs);
		_query.setLicense("4,5,2,1");
		_query.setExtrasLicense(true);
		_query.setExtrasOwnerName(true);
		_query.setExtrasTags(true);
		
		PhotoList cacheResult;
		PhotosInterface photosInterface = _flickr.getPhotosInterface();
		int numToCache = 500;
		int numImages;
		
		try {
			// get the images
			if (recent)
			{
				cacheResult = photosInterface.getRecent(numToCache,1);
			}
			else
			{
				cacheResult = photosInterface.search(_query, numToCache, 1);
			}
			
			numImages = cacheResult.size();
			
			if (numImages <= 0)
				throw new ImageException();
			
			// cache first page of results into _photoPool
			// this seems to be the best option, since the API is broken
			//  for selecting random pages
			_photoPool = new Vector<Photo>();
			
			for (int i=0; i<numImages; i++)
			{
				_photoPool.add(((Photo)cacheResult.get(i)));
			}

		} catch (IOException e) {
			throw new ImageException();
		} catch (SAXException e) {
			throw new ImageException();
		} catch (FlickrException e) {
			e.printStackTrace();
			throw new ImageException();
		}
		

	}

	
	public AnnotatedImage getRandomImage() {
		AnnotatedImage img = null;
		
		while (img == null)
		{
			img = getRandomImageHelper();
		}
		
		return img;
	}
	
	public AnnotatedImage getRandomImageHelper() {
		// select random photo from the cached results
		int whichOne = _rand.nextInt(_photoPool.size());
		
		try {
			return getFlickrImage(_photoPool.get(whichOne), _flickr);
		} catch (Throwable e) {
			return null;
		}
	}
	
	
	/**
	 * @param p - flickrj Photo object describing the photo.  <i>Must include owner and tag information</i>
	 * @param flickr - the flickr session
	 * @return an AnnotatedImage object describing the image from flickr
	 * @throws ImageException if something unrecoverable happens
	 */
	private AnnotatedImage getFlickrImage(Photo p, Flickr flickr) throws ImageException {

		return new AnnotatedImage(loadImage(p, flickr.getPhotosInterface()), 
									p.getTitle(),
									loadOwner(p,  flickr.getPeopleInterface()), 
									loadTags(p, flickr.getTagsInterface()));
	}
	
	private static String loadOwner(Photo photo, PeopleInterface pi) throws ImageException {
		User userdata = photo.getOwner();
		if (userdata.getRealName() != null)
			return userdata.getRealName();
		else if (userdata.getUsername() != null)
			return userdata.getUsername();
		else 
			return "Unknown";
	}
	
	private static int parseSize(String size)
	{
		size = size.toLowerCase();
		
		if (size.startsWith("s"))
		{
			return Size.SMALL;
		}
		else if (size.startsWith("m"))
		{
			return Size.MEDIUM;
		}
		else if (size.startsWith("l"))
		{
			return Size.LARGE;
		}
		else if (size.startsWith("o"))
		{
			return Size.ORIGINAL;
		}
		else if (size.startsWith("t"))
		{
			return Size.THUMB;
		}
		else
		{
			throw new IllegalArgumentException(size + " is not a valid size");
		}
	}

	private static BufferedImage loadImage(Photo photo, PhotosInterface pinterface) throws ImageException {
		try {
			return pinterface.getImage(photo, parseSize(Config.getConfig().getString(FLICKR_SIZE)));
		} catch (IOException e) {
			throw new ImageException();
		} catch (FlickrException e) {
			throw new ImageException();
		}
	}

	@SuppressWarnings("unchecked")
	private static Collection<String> loadTags(Photo photo, TagsInterface ti) throws ImageException {
		Collection<Tag> flickrTags;
		flickrTags = photo.getTags();
		
		Collection<String> output = new LinkedList<String>();
		
		for (Tag tag : flickrTags) {
			output.add(tag.getValue());
		}
		
		return output;
	}

}
