package hsm.image;

import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;

public class BufferedImageOpAdaptor extends ImageOperation {

	BufferedImageOp _operation;
	
	public BufferedImageOpAdaptor(BufferedImageOp op)
	{
		_operation = op;
	}
	
	@Override
	public int getNumberOfInputs() {
		return 1;
	}

	@Override
	protected LayerImage performInternal(LayerImage... images) {
		LayerImage img = images[0];
		BufferedImage flatImg = img.getFlattenedImage();
		BufferedImage dest = _operation.createCompatibleDestImage(flatImg, flatImg.getColorModel());
		
		_operation.filter(flatImg, dest);
		
		return new LayerImage(dest);
	}

}
