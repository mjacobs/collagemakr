package hsm.tools;

import java.awt.Color;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Random;

import javax.imageio.ImageIO;

public class Extraktor {

	private static final double THRESHOLD = 1000;
	
	private BufferedImage _im, _imFF;
	private BoundaryMap _bmap;
	private Random _rand;
	
	public static void main(String[] args)
	{
		Extraktor e = new Extraktor();
		try 
		{ 
			for (int i = 0; i < 20; i++)
				writeImage(e.getExtract("yarnell.jpg"), "extract"+i+".jpg"); 
		}
		catch (IOException ioe) { ioe.printStackTrace(); }
	}
	
	public Extraktor() 
	{
		_rand = new Random();
	}
	
	public BufferedImage getExtract(String fname)
	{
		try
		{
			_im = ImageIO.read(new File(fname));
			_imFF = ImageIO.read(new File(fname));
		}
		catch (IOException e)
		{
			System.out.println("Can't read filename " + fname);
			e.printStackTrace();
		}
		
		return getRandomExtract();
	}

	public BufferedImage getExtract(BufferedImage b)
	{
		_im = b;
		
		_imFF = new BufferedImage(_im.getWidth(), _im.getHeight(), BufferedImage.TYPE_INT_RGB);
		_imFF.setData(_imFF.getData());
		
		return getRandomExtract();
	}
	
	public BufferedImage getExtract(BufferedImage b, Point p)
	{
		_im = b;
		
		_imFF = new BufferedImage(_im.getWidth(), _im.getHeight(), BufferedImage.TYPE_INT_RGB);
		_imFF.setData(_imFF.getData());
		
		return getPointExtract(p);
	}
	
	private BufferedImage getRandomExtract()
	{			
		Point initPt = new Point(_rand.nextInt(_im.getWidth()),_rand.nextInt(_im.getHeight()));
		_bmap = new BoundaryMap(initPt);
		floodFill(_imFF, Color.red, initPt, _bmap);

		BufferedImage imNew = copyPixels(_im, _bmap);
		
		return imNew;
	}
	
	private BufferedImage getPointExtract(Point p)
	{
		floodFill(_imFF, Color.red, p, _bmap);

		BufferedImage imNew = copyPixels(_im, _bmap);
		
		return imNew;
	}

	private BufferedImage copyPixels(BufferedImage im, BoundaryMap bm) {
		Point[] bounds = bm.getBounds();
		
		int w = bounds[1].x - bounds[0].x + 1;
		int h = bounds[1].y - bounds[0].y + 1;
		BufferedImage imNew = new BufferedImage(w,h,_im.getType());
		
		Collection<Point> ptsToCopy = bm.getPoints().values();
		
		// Gets the pixel which is the center of this extract in the original image
		Point c = bm.getCenter();
		int cX = -bounds[0].x;
		int cY = -bounds[0].y;
		
		for (Iterator<Point> i = ptsToCopy.iterator(); i.hasNext();)
		{
			Point pt = i.next();
			imNew.setRGB(cX + pt.x,cY + pt.y, _im.getRGB(c.x + pt.x,c.y + pt.y));
		}
		
		return imNew;
	}

	private static void writeImage (BufferedImage toWrite, String fileout) throws IOException {

		// The output file
		File f = new File(fileout);

		// Gets the output format based on the file extention
		String ext = fileout.substring(fileout.length()-3,fileout.length());
		String filetype = "jpeg";
		if (ext.equalsIgnoreCase("jpg")) {
			filetype = "jpeg";
		} else if (ext.equalsIgnoreCase("gif")) {
			filetype = "gif";
		} else if (ext.equalsIgnoreCase("png")) {
			filetype = "png";
		}

		// Writes the file
		ImageIO.write(toWrite, filetype, f);

	}	

	/**
	 * Fills the selected pixel and all surrounding pixels of the same color with the fill color.
	 * @param img image on which operation is applied
	 * @param fillColor color to be filled in
	 * @param loc location at which to start fill
	 * @throws IllegalArgumentException if loc is out of bounds of the image
	 */
	private void floodFill(BufferedImage img, Color fillColor, Point loc, BoundaryMap bm) 
	{
		if ((_im == null)||(_imFF == null)) 	
			return;

		if (loc.x < 0 || loc.x >= img.getWidth() || loc.y < 0 || loc.y >= img.getHeight()) throw new IllegalArgumentException();

		WritableRaster raster = img.getRaster();
		
		int[] fill =
			new int[] {fillColor.getRed(), fillColor.getGreen(), fillColor.getBlue(),
				fillColor.getAlpha()};
		
		int[] old = raster.getPixel(loc.x, loc.y, new int[4]);

		floodLoop(raster, loc.x, loc.y, fill, old, bm);
	}

	// Recursively fills surrounding pixels of the old color
	private void floodLoop(WritableRaster raster, int x, int y, int[] fill, int[] old, BoundaryMap bm) 
	{
		Rectangle bounds = raster.getBounds();
		int[] aux = {255, 255, 255, 255};

		// finds the left side, filling along the way
		int fillL = x;
		
		do 
		{
			bm.put(new Point(fillL-bm.getCenter().x,y-bm.getCenter().y));
			//raster.setPixel(fillL, y, fill);
			fillL--;
		}
		while (fillL >= 0 && bm.get(fillL-bm.getCenter().x,y-bm.getCenter().y)==null && isFuzzyEqRGB(raster.getPixel(fillL, y, aux), old));
		
		fillL++;

		// find the right right side, filling along the way
		int fillR = x;
		
		do 
		{
			bm.put(new Point(fillR-bm.getCenter().x,y-bm.getCenter().y));
			//raster.setPixel(fillR, y, fill);
			fillR++;
		} 
		while (fillR < bounds.width - 1 && bm.get(fillR-bm.getCenter().x, y-bm.getCenter().y)==null && isFuzzyEqRGB(raster.getPixel(fillR, y, aux), old) );
		
		fillR--;

		// checks if applicable up or down
		for (int i = fillL; i <= fillR; i++) 
		{
			if (y > 0  && bm.get(i-bm.getCenter().x, y-1-bm.getCenter().y)==null && isFuzzyEqRGB(raster.getPixel(i, y - 1, aux), old))
				floodLoop(raster, i, y - 1, fill, old, bm);
			if (y < bounds.height - 1 && bm.get(i-bm.getCenter().x, y + 1-bm.getCenter().y)==null && isFuzzyEqRGB(raster.getPixel(i, y + 1, aux), old))
				floodLoop(raster, i, y + 1, fill, old, bm);
		}
	}

	// Returns true if RGBA arrays are equivalent, false otherwise
	// Could use Arrays.equals(int[], int[]), but this is probably a little faster...
	private static boolean isFuzzyEqRGB(int[] pix1, int[] pix2) {
		double diff = Math.pow(pix1[0] - pix2[0],2) + Math.pow(pix1[1] - pix2[1],2) + Math.pow(pix1[2] - pix2[2],2);
		return (diff < THRESHOLD);
	}

	private class BoundaryMap
	{
		private HashMap<String, Point> _bPoints;
		private Point _center;
		public BoundaryMap(Point center)
		{
			_center = center;
			_bPoints = new HashMap<String, Point>();
		}

		public HashMap<String, Point> getPoints() {
			return _bPoints;
		}

		public Point getCenter() {
			return _center;
		}

		public void print() {
			Collection<Point> pts = _bPoints.values();
			for (Iterator<Point> i = pts.iterator(); i.hasNext(); )
			{
				Point pt = i.next();
				System.out.println(pt.toString());
			}
		}

		public Point get(int x, int y)
		{
			return _bPoints.get(x + "," + y);
		}
		
		public Point[] getBounds()
		{
			
			Point max = new Point(0,0);
			Point min = new Point(Integer.MAX_VALUE,Integer.MAX_VALUE);
			for (Iterator<Point> it = _bPoints.values().iterator(); it.hasNext(); )
			{
				Point p = it.next();
				if (p.x < min.x)
					min.x = p.x;
				
				if (p.x > max.x)
					max.x = p.x;
				
				if (p.y < min.y)
					min.y = p.y;
				
				if (p.y > max.y)
					max.y = p.y;
				
			}
			return new Point[]{min, max};
		}
		
		public void put(Point p)
		{
			_bPoints.put(p.x + "," + p.y, p);
		}
	}

}