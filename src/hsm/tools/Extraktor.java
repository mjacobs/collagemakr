package hsm.tools;

import java.awt.Color;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;

import javax.imageio.ImageIO;

public class Extraktor
{
	
	private static final double THRESHOLD = 5000;
	
	private static final double SIZE_THRESHOLD = .3;
	
	private BufferedImage _im, _imFF;
	private BoundaryMap _bmap;
	private Random _rand;
	
	public static void main(String[] args)
	{
		Extraktor e = new Extraktor();
		String d = System.getProperty("file.separator");
		try
		{
			for (int i = 0; i < 20; i++)
				writeImage(e.getExtract("data" + d + "samples" + d
						+ "cowboy.jpg"), "data" + d + "output" + d + "extract"
						+ i + ".jpg");
		}
		catch (IOException ioe)
		{
			ioe.printStackTrace();
		}
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
		
		_imFF = copyImage(b);
		
		return getRandomExtract();
	}
	
	private BufferedImage copyImage(BufferedImage b)
	{
		BufferedImage c = new BufferedImage(b.getWidth(), b.getHeight(),
				BufferedImage.TYPE_INT_RGB);
		c.setData(b.getData());
		return c;
	}
	
	public BufferedImage getExtract(BufferedImage b, Point p)
	{
		_im = b;
		
		_imFF = new BufferedImage(_im.getWidth(), _im.getHeight(),
				BufferedImage.TYPE_INT_RGB);
		_imFF.setData(_im.getData());
		
		return getPointExtract(p);
	}
	
	public BufferedImage getRandomExtract(BufferedImage b, BufferedImage back,
			Point p)
	{
		_im = b;
		
		_imFF = copyImage(b);
		
		Point initPt = new Point(_rand.nextInt(_im.getWidth()), _rand
				.nextInt(_im.getHeight()));
		_bmap = new BoundaryMap(initPt);
		floodFill(_imFF, Color.red, initPt, _bmap);
		
		BufferedImage imNew = copyPixelsInto(_im, back, p, _bmap);
		
		return imNew;
	}
	
	private BufferedImage getRandomExtract()
	{
		Point initPt = new Point(_rand.nextInt(_im.getWidth()), _rand
				.nextInt(_im.getHeight()));
		_bmap = new BoundaryMap(initPt);
		
		floodFill(copyImage(_imFF), Color.red, initPt, _bmap);
		
		while ((double) _bmap.size()
				/ (double) (_im.getWidth() * _im.getHeight()) < SIZE_THRESHOLD)
		{
			System.out.println(((double) _bmap.size() / (double) (_im
					.getWidth() * _im.getHeight()))
					+ " w/ "
					+ _bmap.size()
					+ " div by "
					+ (_im.getWidth() * _im.getHeight()));
			initPt = new Point(_rand.nextInt(_im.getWidth()), _rand.nextInt(_im
					.getHeight()));
			_bmap = new BoundaryMap(initPt);
			floodFill(copyImage(_imFF), Color.red, initPt, _bmap);
		}
		System.out.println("SUCCESS!!!!!!!!!!!!!!!!!!");
		BufferedImage imNew = copyPixelsToNew(_im, _bmap);
		
		return imNew;
	}
	
	private BufferedImage getPointExtract(Point p)
	{
		floodFill(_imFF, Color.red, p, _bmap);
		
		BufferedImage imNew = copyPixelsToNew(_im, _bmap);
		
		return imNew;
	}
	
	private BufferedImage copyPixelsInto(BufferedImage im,
			BufferedImage copyInto, Point pt, BoundaryMap bm)
	{
		Point[] bounds = bm.getRectBounds();
		
		int w = bounds[1].x - bounds[0].x + 1;
		int h = bounds[1].y - bounds[0].y + 1;
		
		Collection<Point> ptsToCopy = bm.getPoints().values();
		
		// Gets the pixel which is the center of this extract in the original
		// image
		Point c = bm.getCenter();
		int cX = -bounds[0].x + pt.x;
		int cY = -bounds[0].y + pt.y;
		
		for (Iterator<Point> i = ptsToCopy.iterator(); i.hasNext();)
		{
			Point ptIt = i.next();
			copyInto.setRGB(Math.min(cX + ptIt.x, copyInto.getWidth()), Math
					.min(cY + ptIt.y, copyInto.getHeight()), im.getRGB(c.x
					+ ptIt.x, c.y + ptIt.y));
		}
		
		return copyInto;
	}
	
	private BufferedImage copyPixelsToNew(BufferedImage im, BoundaryMap bm)
	{
		Point[] bounds = bm.getRectBounds();
		
		int w = bounds[1].x - bounds[0].x + 1;
		int h = bounds[1].y - bounds[0].y + 1;
		BufferedImage imNew = new BufferedImage(w, h, im.getType());
		
		Collection<Point> ptsToCopy = bm.getPoints().values();
		
		// Gets the pixel which is the center of this extract in the original
		// image
		Point c = bm.getCenter();
		int cX = -bounds[0].x;
		int cY = -bounds[0].y;
		
		for (Iterator<Point> i = ptsToCopy.iterator(); i.hasNext();)
		{
			Point pt = i.next();
			imNew.setRGB(cX + pt.x, cY + pt.y, im
					.getRGB(c.x + pt.x, c.y + pt.y));
		}
		
		return imNew;
	}
	
	private static void writeImage(BufferedImage toWrite, String fileout)
			throws IOException
	{
		
		// The output file
		File f = new File(fileout);
		
		// Gets the output format based on the file extention
		String ext = fileout.substring(fileout.length() - 3, fileout.length());
		String filetype = "jpeg";
		if (ext.equalsIgnoreCase("jpg"))
		{
			filetype = "jpeg";
		}
		else if (ext.equalsIgnoreCase("gif"))
		{
			filetype = "gif";
		}
		else if (ext.equalsIgnoreCase("png"))
		{
			filetype = "png";
		}
		
		// Writes the file
		ImageIO.write(toWrite, filetype, f);
		
	}
	
	/**
	 * Fills the selected pixel and all surrounding pixels of the same color
	 * with the fill color.
	 * 
	 * @param img
	 *            image on which operation is applied
	 * @param fillColor
	 *            color to be filled in
	 * @param loc
	 *            location at which to start fill
	 * @throws IllegalArgumentException
	 *             if loc is out of bounds of the image
	 */
	private void floodFill(BufferedImage img, Color fillColor, Point loc,
			BoundaryMap bm)
	{
		if (img == null)
			return;
		
		if (loc.x < 0 || loc.x >= img.getWidth() || loc.y < 0
				|| loc.y >= img.getHeight())
			throw new IllegalArgumentException();
		
		WritableRaster raster = img.getRaster();
		
		int[] fill = new int[]
		{ fillColor.getRed(), fillColor.getGreen(), fillColor.getBlue(),
				fillColor.getAlpha() };
		
		int[] old = raster.getPixel(loc.x, loc.y, new int[4]);
		
		floodLoop(raster, loc.x, loc.y, fill, old, bm);
	}
	
	// Recursively fills surrounding pixels of the old color
	private void floodLoop(WritableRaster raster, int x, int y, int[] fill,
			int[] old, BoundaryMap bm)
	{
		LinkedList<Point> q = new LinkedList<Point>();
		q.add(new Point(x, y));
		
		Rectangle bounds = raster.getBounds();
		
		int[] aux =
		{ 255, 255, 255, 255 };
		
		Point w, e;
		while (!q.isEmpty())
		{
			Point p = q.removeFirst();
			
			int l = p.x;
			int r = p.x;
			
			while ((l - 1 > 0)
					&& isFuzzyEqRGB(raster.getPixel(l - 1, p.y, aux), old)
					&& !bm.containsRelativePoint(l - 1, p.y))
			{
				l--;
				bm.putRelative(new Point(l, p.y));
			}
			
			while ((r + 1 < raster.getWidth())
					&& isFuzzyEqRGB(raster.getPixel(r + 1, p.y, aux), old)
					&& !bm.containsRelativePoint(r + 1, p.y))
			{
				r++;
				bm.putRelative(new Point(r, p.y));
			}
			
			for (int i = l; i < r; i++)
			{
				if ((p.y - 1 > 0)
						&& (isFuzzyEqRGB(raster.getPixel(i, p.y - 1, aux), old))
						&& !bm.containsRelativePoint(i, p.y - 1))
				{
					bm.putRelative(new Point(i, p.y - 1));
					q.add(new Point(i, p.y - 1));
				}
				if ((p.y + 1 < raster.getHeight())
						&& (isFuzzyEqRGB(raster.getPixel(i, p.y + 1, aux), old))
						&& !bm.containsRelativePoint(i, p.y + 1))
				{
					bm.putRelative(new Point(i, p.y + 1));
					q.add(new Point(i, p.y + 1));
				}
			}
		}
		
	}
	
	// Returns true if RGBA arrays are equivalent, false otherwise
	// Could use Arrays.equals(int[], int[]), but this is probably a little
	// faster...
	private static boolean isFuzzyEqRGB(int[] pix1, int[] pix2)
	{
		double diff = Math.pow(pix1[0] - pix2[0], 2)
				+ Math.pow(pix1[1] - pix2[1], 2)
				+ Math.pow(pix1[2] - pix2[2], 2);
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
		
		public Collection<Point> getAbsolutePoints()
		{
			Collection<Point> pts = _bPoints.values();
			
			Point max = new Point(0, 0);
			Point min = new Point(Integer.MAX_VALUE, Integer.MAX_VALUE);
			for (Iterator<Point> it = pts.iterator(); it.hasNext();)
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
			
			Collection<Point> ret = new LinkedList<Point>();
			
			for (Iterator<Point> it = pts.iterator(); it.hasNext();)
			{
				Point p = it.next();
				ret.add(new Point(p.x - min.x, p.y - min.y));
			}
			
			return ret;
		}
		
		public boolean containsRelativePoint(int x, int y)
		{
			x = (-_center.x + x);
			y = (-_center.y + y);
			return _bPoints.containsKey(x + "," + y);
		}
		
		public int size()
		{
			return _bPoints.size();
		}
		
		public HashMap<String, Point> getPoints()
		{
			return _bPoints;
		}
		
		public Point getCenter()
		{
			return _center;
		}
		
		public void print()
		{
			Collection<Point> pts = _bPoints.values();
			for (Iterator<Point> i = pts.iterator(); i.hasNext();)
			{
				Point pt = i.next();
				System.out.println(pt.toString());
			}
		}
		
		public Point get(int x, int y)
		{
			return _bPoints.get(x + "," + y);
		}
		
		public HashMap<Integer,Point> getBounds()
		{
			HashMap<Integer,Point> bounds = new HashMap<Integer, Point>();
			
			for (Iterator<Point> it = _bPoints.values().iterator(); it.hasNext(); )
			{
				Point pt = it.next();
				if (bounds.containsKey(new Integer(pt.y)))
				{
					
				}
			}
			
			return bounds;
		}
		
		public Point[] getRectBounds()
		{
			
			Point max = new Point(0, 0);
			Point min = new Point(Integer.MAX_VALUE, Integer.MAX_VALUE);
			for (Iterator<Point> it = _bPoints.values().iterator(); it
					.hasNext();)
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
			return new Point[]
			{ min, max };
		}
		
		public void put(Point p)
		{
			_bPoints.put(p.x + "," + p.y, p);
		}
		
		public void putRelative(Point p)
		{
			p.x = (-_center.x + p.x);
			p.y = (-_center.y + p.y);
			_bPoints.put(p.x + "," + p.y, p);
		}
	}
	
}