package hsm.image;

import java.awt.image.BufferedImage;
import java.util.Set;
import java.util.HashSet;
import java.util.Collection;

public class AnnotatedImage {
	
	protected Set<String> _tags;
	protected String _owner;
	protected DeferredImage _image;
	protected String _title;
	
	public AnnotatedImage(BufferedImage img, String title, String owner, Collection<String> tags)
	{
		_image = new DeferredImage(img);
		_tags = new HashSet<String>(tags);
		_owner = owner;
		_title = title;
	}

	public String getTitle()
	{
		return _title;
	}
	
	public String getOwner() {
		return _owner;
	}

	public BufferedImage getImage() {
		return _image.getImage();
	}

	@SuppressWarnings("unchecked")
	public Collection<String> getTags() {
		return _tags;
	}
}
