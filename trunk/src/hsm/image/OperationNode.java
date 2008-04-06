package hsm.image;

public class OperationNode implements ExpressionNode {

	private ExpressionNode[] _children;
	private ImageOperation _operation;
	
	public OperationNode(ImageOperation op, ExpressionNode... expressionNodes)
	{
		_operation = op;
		_children = expressionNodes;
	}
	
	@Override
	public LayerImage evaluate() {
		LayerImage[] args = new LayerImage[_children.length];
		
		for (int i=0; i<_children.length; i++)
		{
			args[i] = _children[i].evaluate();
		}
		
		return _operation.perform(args);
		
	}

}
