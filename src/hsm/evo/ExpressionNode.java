package hsm.evo;

import hsm.image.LayerImage;

public abstract class ExpressionNode {
	public abstract LayerImage evaluate();
	public abstract ExpressionNode createMutated();
	public abstract ExpressionNode createMated(ExpressionNode friend);
	
	public abstract void print(String prefix);
	public void print() { print(""); }
}
