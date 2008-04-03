package hsm.image;

import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;

public class BufferedImageOpAdaptor extends ImageOperation {

	BufferedImageOp _operation;
	int _canvasWidth, _canvasHeight;
	
	public BufferedImageOpAdaptor(BufferedImageOp op, int canvasWidth, int canvasHeight)
	{
		_canvasWidth = canvasWidth;
		_canvasHeight = canvasHeight;
		_operation = op;
	}
	
	@Override
	int getNumberOfInputs() {
		return 1;
	}

	@Override
	protected LayerImage performInternal(LayerImage... images) {
		LayerImage img = images[0];
		BufferedImage flatImg = img.getFlattenedImage(_canvasWidth, _canvasHeight);
		BufferedImage dest = _operation.createCompatibleDestImage(flatImg, flatImg.getColorModel());
		
		_operation.filter(flatImg, dest);
		
		return new LayerImage(dest);
	}

}
