package hsm.image;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Iterator;
import java.util.Properties;
import java.util.Collection;

import javax.imageio.ImageIO;

public class AnnotatedImage {
	
	protected DeferredImage _image;
	protected Properties _props;
	
	public static String join(Collection<String> s, String delimiter) {
        StringBuffer buffer = new StringBuffer();
        Iterator<String> iter = s.iterator();
        while (iter.hasNext()) {
            buffer.append(iter.next());
            if (iter.hasNext()) {
                buffer.append(delimiter);
            }
        }
        return buffer.toString();
    }
	
	public AnnotatedImage(BufferedImage img, String title, String owner, Collection<String> tags)
	{
		_image = new DeferredImage(img);
		_props = new Properties(getDefaultProperties());
		_props.put("tags", join(tags, ","));
		_props.put("owner", owner);
		_props.put("title", title);
	}
	
	public AnnotatedImage(File path)
	{
		loadFromDisk(path);
	}

	

	private static File getPropertiesFile(File imageFile)
	{
		String absPath = imageFile.getAbsolutePath();
		int slashIdx = absPath.lastIndexOf(File.separator);
		String lastComponent = absPath.substring(slashIdx);
		int dotIdx = lastComponent.lastIndexOf(".");
		
		if (dotIdx == -1)
		{
			return new File(absPath + ".xml");
		}
		else
		{
			return new File(absPath.substring(0, dotIdx+slashIdx) + ".xml");
		}
	}
	
	private static final Properties getDefaultProperties(){
		Properties defs = new Properties();
		
		defs.setProperty("title", "Untitled");
		defs.setProperty("tags", "");
		defs.setProperty("owner", "");
		
		return null;
	}
	
	private void loadFromDisk(File imageFile)
	{
		File propFile = getPropertiesFile(imageFile);
		_props = new Properties(getDefaultProperties());
		
		if (propFile.exists())
		{
			try {
				_props.loadFromXML(new FileInputStream(propFile));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		else
		{
			_props.setProperty("title", imageFile.getName());
		}
		
		try {
			_image = new DeferredImage(ImageIO.read(imageFile));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


	public void saveToDisk(File imageFile)
	{
		File propFile = getPropertiesFile(imageFile);
		
		try {
			ImageIO.write(_image.getImage(), "png", imageFile);
			_props.storeToXML(new FileOutputStream(propFile), null);
		} catch (Exception e) { }
	}
	
	public String getTitle()
	{
		return _props.getProperty("title");
	}
	
	public String getOwner() {
		return _props.getProperty("owner");
	}

	public BufferedImage getImage() {
		return _image.getImage();
	}

	@SuppressWarnings("unchecked")
	public String[] getTags() {
		if (_props.containsKey("tags"))
		{
			return _props.getProperty("tags").split(",");
		}
		else
		{
			return new String[0];
		}
	}
	
	public boolean hasTag(String tag)
	{
		String[] tags = getTags();
		
		for (int i=0; i<tags.length; i++)
		{
			if (tags[i].equalsIgnoreCase(tag))
			{
				return true;
			}
		}
		
		return false;
	}
}
