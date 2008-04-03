package hsm.image;

public abstract class ImageOperation {
	abstract int getNumberOfInputs();
	
	public LayerImage perform(LayerImage... images)
	{
		if (images.length != getNumberOfInputs())
		{
			throw new IllegalArgumentException();
		}
		else
		{
			return performInternal(images);
		}
	}
	
	// override this method in concrete subclasses
	abstract protected LayerImage performInternal(LayerImage... images);
}
