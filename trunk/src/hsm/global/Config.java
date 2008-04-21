package hsm.global;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.InvalidPropertiesFormatException;
import java.util.Properties;

public class Config extends Properties {
	private static final long serialVersionUID = 2602541869943866197L;
	
	private static Config g_config = null;
	private static File g_configFile = new File("config/config.xml");
	
	private Config()
	{
		super(new Properties());
		
		assert(g_config == null);
		
		try {
			this.loadFromXML(new FileInputStream(g_configFile));
		} catch (InvalidPropertiesFormatException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			// it's okay, just rely on defaults
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static Config getConfig()
	{
		if (g_config == null)
		{
			g_config = new Config();
		}
		
		return g_config;
	}
	
	public void registerBoolean(String key, boolean def)
	{
		defaults.setProperty(key, Boolean.toString(def));
	}
	
	public boolean getBoolean(String key)
	{
		return Boolean.parseBoolean(this.getProperty(key));
	}
	
	public void registerInt(String key, int def)
	{
		defaults.setProperty(key, Integer.toString(def));
	}
	
	public int getInt(String key)
	{
		return Integer.parseInt(this.getProperty(key));
	}
	
	public void registerString(String key, String def)
	{
		defaults.setProperty(key, def);
	}
	
	public String getString(String key)
	{
		return this.getProperty(key);
	}
}
