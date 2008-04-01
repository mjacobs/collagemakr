package hsm.tools;

import hsm.gui.FlickrComposrApp;

import java.awt.Color;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.awt.image.ImagingOpException;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Random;
import java.util.TreeMap;

import javax.imageio.ImageIO;
import javax.swing.SwingUtilities;


public class Extraktor {

	private BufferedImage _im, _imOutput;
	private Random _rand;
	private static double _threshold;
	private BoundaryMap _bmap;
	private boolean isInit;
	private boolean _isDebug;
	FlickrComposrApp gui;

	public static void main(String[] args) {
		Extraktor e = new Extraktor("blobs.jpg");
		//BoundaryMap bm = e.getRandomExtraction();
		//bm.print();		

	}

	public Extraktor() 
	{
		_im = null;
		_imOutput = null;
	}

	public Extraktor(String filename) 
	{
		try
		{
			_im = ImageIO.read(new File(filename));
			_imOutput = _im;
		}
		catch (IOException e)
		{
			System.out.println("Can't read filename " + filename);
			e.printStackTrace();
		}
		
		if (_isDebug)
		{
			SwingUtilities.invokeLater(new Runnable() {
				public void run() {
					gui = new FlickrComposrApp();
					gui.getJFrame().setVisible(true);
				}
			});
		}

		initClass();
	}

	public void setImage(BufferedImage b)
	{
		_im = b;
		initClass();
	}

	private void initClass()
	{
		isInit = true;
		_isDebug = true;
		_rand = new Random();
		_bmap = new BoundaryMap(_im.getWidth(),_im.getHeight());
		_threshold = 100;
		floodFill(_im, Color.red, new Point(25,25));
		if (_isDebug)
		{
			//gui.setIm1(_im);
			try{
				writeImage(_im,"ex.jpg");
			}catch(IOException e)
			{
				System.err.println("err");
				e.printStackTrace();
			}
		}
	}

	public static void writeImage (BufferedImage toWrite, String fileout) throws IOException {

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
		ImageIO.write( toWrite, filetype, f);

	}	

	/**
	 * Fills the selected pixel and all surrounding pixels of the same color with the fill color.
	 * @param img image on which operation is applied
	 * @param fillColor color to be filled in
	 * @param loc location at which to start fill
	 * @throws IllegalArgumentException if loc is out of bounds of the image
	 */
	public static void floodFill(BufferedImage img, Color fillColor, Point loc) {
		if (loc.x < 0 || loc.x >= img.getWidth() || loc.y < 0 || loc.y >= img.getHeight()) throw new IllegalArgumentException();

		WritableRaster raster = img.getRaster();
		int[] fill =
			new int[] {fillColor.getRed(), fillColor.getGreen(), fillColor.getBlue(),
				fillColor.getAlpha()};
		int[] old = raster.getPixel(loc.x, loc.y, new int[4]);

		// Checks trivial case where loc is of the fill color
		if (isFuzzyEqRGB(fill, old)) return;

		floodLoop(raster, loc.x, loc.y, fill, old);
	}

	// Recursively fills surrounding pixels of the old color
	private static void floodLoop(WritableRaster raster, int x, int y, int[] fill, int[] old) {
		Rectangle bounds = raster.getBounds();
		int[] aux = {255, 255, 255, 255};

		// finds the left side, filling along the way
		int fillL = x;
		do {
			raster.setPixel(fillL, y, fill);
			fillL--;
		} while (fillL >= 0 && isFuzzyEqRGB(raster.getPixel(fillL, y, aux), old));
		fillL++;

		// find the right right side, filling along the way
		int fillR = x;
		do {
			raster.setPixel(fillR, y, fill);
			fillR++;
		} while (fillR < bounds.width - 1 && isFuzzyEqRGB(raster.getPixel(fillR, y, aux), old));
		fillR--;

		// checks if applicable up or down
		for (int i = fillL; i <= fillR; i++) {
			if (y > 0 && isFuzzyEqRGB(raster.getPixel(i, y - 1, aux), old)) floodLoop(raster, i, y - 1,
					fill, old);
			if (y < bounds.height - 1 && isFuzzyEqRGB(raster.getPixel(i, y + 1, aux), old)) floodLoop(
					raster, i, y + 1, fill, old);
		}
	}

	// Returns true if RGBA arrays are equivalent, false otherwise
	// Could use Arrays.equals(int[], int[]), but this is probably a little faster...
	private static boolean isFuzzyEqRGB(int[] pix1, int[] pix2) {
		double diff = Math.pow(pix1[0] - pix2[0],2) + Math.pow(pix1[1] - pix2[1],2) + Math.pow(pix1[2] - pix2[2],2);
		return (diff < _threshold);
	}

	public enum PointType { UNKNOWN, INSIDE, OUTSIDE, BORDER };

	public class BoundaryMap
	{
		private HashMap<String, ExtractPoint> _bPoints;
		public BoundaryMap(int width, int height)
		{
			_bPoints = new HashMap<String, ExtractPoint>();
		}

		public void print() {
			Collection<ExtractPoint> pts = _bPoints.values();
			for (Iterator<ExtractPoint> i = pts.iterator(); i.hasNext(); )
			{
				ExtractPoint pt = i.next();
				System.out.println(pt.toString());
			}
		}

		public ExtractPoint get(int x, int y)
		{
			String k = x + "," + y;
			if (_bPoints.containsKey(k))
				return _bPoints.get(k);
			else
			{
				ExtractPoint e = new ExtractPoint(x,y);
				_bPoints.put(k, e);
				return e;
			}
		}
	}

	public class ExtractPoint extends Point
	{
		private static final long serialVersionUID = -1354987372950303369L;
		private PointType type;

		public PointType getType() {
			return type;
		}

		public void setType(PointType type) {
			this.type = type;
		}

		public ExtractPoint()
		{
			super();
			type = PointType.UNKNOWN;
		}

		public ExtractPoint(int x, int y)
		{
			super(x,y);
			type = PointType.UNKNOWN;
		}

		public String toString()
		{
			return "(" + x + "," + y + ") : " + type;
		}
	}
}