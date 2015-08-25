package hsm.tools;

import hsm.global.Config;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

public class TagChecker
{
	private static final String DEFAULT_TAGS = "animals,architecture,art,baby,band,beach,birthday,black,blue,boston,camping,canon,car,cat,christmas,city,clouds,color,concert,cute,day,de,dog,europe,fall,family,festival,film,florida,flower,flowers,food,france,friends,fun,garden,girl,girls,graffiti,green,halloween,hawaii,hiking,holiday,home,honeymoon,house,kids,lake,landscape,light,macro,mountain,mountains,museum,music,nature,new,night,ocean,park,party,people,photo,photos,portrait,red,river,rock,sky,snow,street,sun,sunset,travel,tree,trees,trip,urban,vacation,water,wedding,white,winter,yellow,zoo";
	private static TagChecker CHECKER_INSTANCE;

	private List<String> tags;
	
	private TagChecker()
	{
//		String file_name = Config.getConfig().getProperty("accepted_tags_file");
//		BufferedReader bReader = null;
//		String tagsString = "";
//	    try 
//	    {
//	    	bReader = new BufferedReader(new FileReader(new File(file_name)));
//
//	      // dis.available() returns 0 if the file does not have more lines.
//	      if (bReader.readLine() != -1) {
//
//	      // this statement reads the line from the file and print it to
//	        // the console.
//	        tagsString += dInputStream.();
//	      }
//	    }
//	    catch (Exception e)
//	    {
//	    	e.printStackTrace();
//	    }
		String[] tagsArray = DEFAULT_TAGS.split(",");
		tags = new ArrayList<String>(tagsArray.length);
		for (int i = 0; i < tagsArray.length; i++)
			tags.add(tagsArray[i]);
		
	}

	private static TagChecker makeInstance()
	{
		CHECKER_INSTANCE = new TagChecker();
		return CHECKER_INSTANCE;
	}

	public static TagChecker getInstance()
	{
		return CHECKER_INSTANCE == null ? makeInstance() : CHECKER_INSTANCE;
	}

	public boolean isValidTag(String tag)
	{
		return tags.contains((String)tag) ? true : false;
	}
	
	public String[] filterTags(String[] tags)
	{
		List<String> filteredTags = new ArrayList<String>();
		for (int i = 0; i < tags.length; i++)
		{
			if (isValidTag(tags[i]))
			{
				filteredTags.add(tags[i]);
			}
		}
		return filteredTags.toArray(new String[] {});
	}

	public String[] getTags()
	{
		return tags.toArray(new String[] {});
	}

	public String[] getTags(int numTags)
	{
		return null;
	}
}
