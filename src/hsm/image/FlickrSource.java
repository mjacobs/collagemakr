package hsm.image;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Collection;
import java.util.LinkedList;

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
import com.aetrion.flickr.photos.licenses.License;
import com.aetrion.flickr.tags.Tag;
import com.aetrion.flickr.tags.TagsInterface;


import java.util.Random;

public class FlickrSource implements ImageSource {

	private Flickr _flickr;
	private Random _rand;
	private SearchParameters _query;
	private int _numImages;
	
	private final boolean recent = false;
	
	@SuppressWarnings("unchecked")
	public FlickrSource() throws ImageException
	{
		_rand = new Random();
		_flickr = new Flickr("d989e76ce73619396ca877158e11aac2");
		
		try {
			Collection<License> lics = (Collection<License>)_flickr.getLicensesInterface().getInfo();
			for (License li : lics)
			{
				System.out.println(li.getId() + " " + li.getName());
			}
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (SAXException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (FlickrException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		_query = new SearchParameters();
		_query.setSafeSearch(Flickr.SAFETYLEVEL_SAFE);
		_query.setText("e");
		_query.setLicense("4,5,2,1");
		_query.setExtrasLicense(true);
		_query.setExtrasOwnerName(true);
		_query.setExtrasTags(true);
		
		PhotoList scoutResult;
		PhotosInterface photosInterface = _flickr.getPhotosInterface();
		
		try {
			if (recent)
			{
				scoutResult = photosInterface.getRecent(0,0);
			}
			else
			{
				scoutResult = photosInterface.search(_query, 0, 0);
			}
			
			_numImages = scoutResult.getTotal();
			System.out.println(_numImages + " photos");
			
			if (_numImages <= 0)
				throw new ImageException();

		} catch (IOException e) {
			throw new ImageException();
		} catch (SAXException e) {
			throw new ImageException();
		} catch (FlickrException e) {
			e.printStackTrace();
			throw new ImageException();
		}
		

	}
	
	public AnnotatedImage getRandomImage() throws ImageException {
		PhotoList realResult;
		PhotosInterface photosInterface = _flickr.getPhotosInterface();
		
		int whichOne = _rand.nextInt(_numImages);
		try {
			System.out.println("picking " + whichOne);
			int perPage = 100;
			int whichPage = whichOne / perPage;
			int idxOnPage = whichOne % perPage;
			
			if (recent)
			{
				realResult = photosInterface.getRecent(perPage, whichPage);
			}
			else
			{
				realResult = photosInterface.search(_query, perPage, whichPage);//getRecent(1, whichOne);	
			}
			
			if (realResult.getPerPage() <= 0)
				throw new ImageException();
			
			Photo randPhoto = (Photo)realResult.get(idxOnPage);
			
			return getFlickrImage(randPhoto, _flickr);
			
		} catch (IOException e) {
			throw new ImageException();
		} catch (SAXException e) {
			throw new ImageException();
		} catch (FlickrException e) {
			e.printStackTrace();
			throw new ImageException();
		}
	}
	
	private AnnotatedImage getFlickrImage(Photo p, Flickr flickr) throws ImageException {

		return new AnnotatedImage(loadImage(p, flickr.getPhotosInterface()), 
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

	private static BufferedImage loadImage(Photo photo, PhotosInterface pinterface) throws ImageException {
		try {
			return pinterface.getImage(photo, Size.MEDIUM);
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
