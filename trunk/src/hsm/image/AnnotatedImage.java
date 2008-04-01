package hsm.image;

import java.awt.image.BufferedImage;
import java.util.Set;
import java.util.HashSet;
import java.util.Collection;

public class AnnotatedImage {
	
	protected Set<String> _tags;
	protected String _owner;
	protected BufferedImage _image;
	
	public AnnotatedImage(BufferedImage img, String owner, Collection<String> tags)
	{
		_image = img;
		_tags = new HashSet<String>(tags);
		_owner = owner;
	}

	public String getOwner() {
		return _owner;
	}

	public BufferedImage getImage() {
		return _image;
	}

	@SuppressWarnings("unchecked")
	public Collection<String> getTags() {
		return _tags;
	}
}
