package hsm.evo;

import hsm.evo.ParametrizedOperation;
import hsm.image.LayerImage;

public class OperationNode extends ExpressionNode {


	private ExpressionNode[] _children;
	private ParametrizedOperation _operation;
	
	public OperationNode(ParametrizedOperation op, ExpressionNode... expressionNodes)
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
	

	@Override
	public ExpressionNode createMated(ExpressionNode friend) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ExpressionNode createMutated() {
		// TODO Auto-generated method stub
		return null;
	}
	
	public void print(String indent)
	{
		System.out.println(indent + _operation);
		for (ExpressionNode child : _children)
		{
			child.print(indent + "---");
		}
	}

}
