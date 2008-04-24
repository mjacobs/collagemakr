package hsm.evo;

import hsm.global.Config;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

public class Composition {
	private ExpressionNode _root;
	private BufferedImage _cachedImage;
	
	public Composition(ExpressionNode root)
	{
		_root = root;
		_cachedImage = null;
	}
	
	public ExpressionNode getRoot()
	{
		return _root;
	}
	
	public BufferedImage getImage()
	{
		if (_cachedImage == null)
		{
			System.out.print("Evaluating tree...");
			BufferedImage img = _root.evaluate().getFlattenedImage();
			System.out.println("done.");
			int canvasWidth = Config.getConfig().getInt(TreeGenerator.CANVAS_WIDTH);
			int canvasHeight = Config.getConfig().getInt(TreeGenerator.CANVAS_HEIGHT);
			_cachedImage = new BufferedImage(canvasWidth, canvasHeight, BufferedImage.TYPE_3BYTE_BGR);
			
			Graphics2D g2d = _cachedImage.createGraphics();
			
			g2d.setBackground(new Color(159,180,204));
			g2d.clearRect(0, 0, canvasWidth, canvasHeight);
			g2d.drawImage(img, 0, 0, null);
			
			g2d.dispose();
		}
		
		return _cachedImage;
	}
	
	public void printTree()
	{
		_root.print();
	}
	
	public static Composition randomComposition()
	{
		return new Composition(TreeGenerator.randomNode(TreeGenerator.getNewTreeDepth()));
	}
	
}
