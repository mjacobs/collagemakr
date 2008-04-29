package hsm.evo;

import hsm.global.Config;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

public class Composition {
	private ExpressionNode _root;
	private BufferedImage _cachedImage;
	private Color _bgColor;
	
	public Composition(ExpressionNode root, Color bgColor)
	{
		_root = root;
		_cachedImage = null;
		_bgColor = bgColor;
	}
	
	public ExpressionNode getRoot()
	{
		return _root;
	}
	
	public Color getBackgroundColor()
	{
		return _bgColor;
	}
	
	public BufferedImage getImage()
	{
		if (_cachedImage == null)
		{
			System.out.print("Rendering tree...");
			BufferedImage img = _root.evaluate().getFlattenedImage();
			System.out.println("done.");
			int canvasWidth = Config.getConfig().getInt(TreeGenerator.CANVAS_WIDTH);
			int canvasHeight = Config.getConfig().getInt(TreeGenerator.CANVAS_HEIGHT);
			_cachedImage = new BufferedImage(canvasWidth, canvasHeight, BufferedImage.TYPE_3BYTE_BGR);
			
			Graphics2D g2d = _cachedImage.createGraphics();
			
			g2d.setBackground(_bgColor);
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
		float hsv[] = new float[3];
		
		for (int i=0; i<3; i++) hsv[i] = (float) Math.random();
		
		return new Composition(TreeGenerator.randomNode(TreeGenerator.getNewTreeDepth()),
				Color.getHSBColor(hsv[0], hsv[1], hsv[2]));
	}
	
}
