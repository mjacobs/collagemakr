package hsm.tools;

import java.awt.Color;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Random;

public class SimpleExtractor implements IExtractor
{

	private final static double COLOR_SPREAD_THRESHOLD = 3500;

	private final static double SIZE_THRESHOLD = .1;

	private final static int NUM_ATTEMPTS = 5;

	private Random _rand;

	public SimpleExtractor()
	{
		_rand = new Random();
	}

	public BufferedImage getExtract(BufferedImage bImage)
	{
		Point initPt = new Point(getGaussianRand(bImage.getWidth()),
				getGaussianRand(bImage.getHeight()));
		BoundaryMap boundaryMap = new BoundaryMap(initPt);

		floodFill(copyImage(bImage), Color.red, initPt, boundaryMap);

		double gatherRatio = (double) boundaryMap.size()
				/ (double) (bImage.getWidth() * bImage.getHeight());

		int numAttempts = 0;

		while ((gatherRatio < SIZE_THRESHOLD) && (numAttempts++ < NUM_ATTEMPTS))
		{
			initPt = new Point(getGaussianRand(bImage.getWidth()),
					getGaussianRand(bImage.getHeight()));
			BoundaryMap resMap = new BoundaryMap(initPt);
			floodFill(copyImage(bImage), Color.red, initPt, resMap);

			if (resMap.size() > boundaryMap.size())
			{
				boundaryMap = resMap;
			}

			gatherRatio = (double) resMap.size()
					/ (double) (bImage.getWidth() * bImage.getHeight());
		}

		if (numAttempts >= NUM_ATTEMPTS)
		{
			return null;
		}

		return copyPixelsToNew(bImage, boundaryMap);

	}

	private BufferedImage copyImage(BufferedImage bImage)
	{
		BufferedImage newImage = new BufferedImage(bImage.getWidth(), bImage
				.getHeight(), BufferedImage.TYPE_4BYTE_ABGR);
		newImage.createGraphics().drawImage(bImage, 0, 0, null);
		return newImage;
	}

	private int getGaussianRand(int maxValue)
	{
		double gaussianDouble = Math.abs(_rand.nextGaussian() / 3.0);
		gaussianDouble = gaussianDouble >= 1 ? .99 : gaussianDouble;
		return (int) Math.floor(gaussianDouble * maxValue);
	}

	private BufferedImage copyPixelsToNew(BufferedImage im, BoundaryMap bm)
	{
		Point[] bounds = bm.getRectBounds();

		int w = bounds[1].x - bounds[0].x + 1;
		int h = bounds[1].y - bounds[0].y + 1;
		BufferedImage imNew = new BufferedImage(w, h,
				BufferedImage.TYPE_INT_ARGB);

		Collection<Point> ptsToCopy = bm.getPoints().values();

		// Gets the pixel which is the center of this extract in the original
		// image
		Point c = bm.getCenter();
		int cX = -bounds[0].x;
		int cY = -bounds[0].y;

		for (Iterator<Point> i = ptsToCopy.iterator(); i.hasNext();)
		{
			Point pt = i.next();
			int rgb = im.getRGB(c.x + pt.x, c.y + pt.y);
			rgb = clearAlpha(rgb);
			rgb |= ((255 & 0xff) << 24);

			imNew.setRGB(cX + pt.x, cY + pt.y, rgb);
		}

		return imNew;
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

		int[] fill = new int[] { fillColor.getRed(), fillColor.getGreen(),
				fillColor.getBlue(), fillColor.getAlpha() };

		int[] old = raster.getPixel(loc.x, loc.y, new int[4]);

		floodLoop(raster, loc.x, loc.y, fill, old, bm);
	}

	// Recursively fills surrounding pixels of the old color
	private void floodLoop(WritableRaster raster, int x, int y, int[] fill,
			int[] old, BoundaryMap bm)
	{
		LinkedList<Point> q = new LinkedList<Point>();
		q.add(new Point(x, y));

		int[] aux = { 255, 255, 255, 255 };

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
	private boolean isFuzzyEqRGB(int[] pix1, int[] pix2)
	{
		double diff = Math.pow(pix1[0] - pix2[0], 2)
				+ Math.pow(pix1[1] - pix2[1], 2)
				+ Math.pow(pix1[2] - pix2[2], 2);
		return (diff < COLOR_SPREAD_THRESHOLD);
	}

	private static int clearAlpha(int n)
	{
		n <<= 8;
		n >>= 8;
		return n;
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

		public HashMap<Integer, Point> getBounds()
		{
			HashMap<Integer, Point> bounds = new HashMap<Integer, Point>();

			for (Iterator<Point> it = _bPoints.values().iterator(); it
					.hasNext();)
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
			return new Point[] { min, max };
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