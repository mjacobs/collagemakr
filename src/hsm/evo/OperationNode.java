package hsm.evo;

import java.util.HashMap;
import java.util.Stack;

import hsm.evo.ParametrizedOperation;
import hsm.evo.OperationMetadata.PropertyData;
import hsm.image.LayerImage;

public class OperationNode extends ExpressionNode {

	private int _numDesc = -1;
	private ExpressionNode[] _children;
	private ParametrizedOperation _operation;
	
	public OperationNode(ParametrizedOperation op, ExpressionNode... expressionNodes)
	{
		_operation = op;
		_children = expressionNodes;
		
		for (ExpressionNode e : _children)
		{
			assert(e != null);
		}
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
	
	public ParametrizedOperation getOperation()
	{
		return _operation;
	}
	
	public ExpressionNode[] getChildren()
	{
		return _children;
	}
	
	private static double wrapValue(double x, double min, double max)
	{
		assert(max > min);
		
		double interval = max - min;
		
		while (x > max || x < min)
		{
			if (x > max)
			{
				x -= interval;
			}
			else if (x < min)
			{
				x += interval;
			}			
		}
		
		return x;
	}
	
	private static ExpressionNode[] adaptChildren(ExpressionNode[] origChildren, ParametrizedOperation dstOp)
	{
		ExpressionNode[] result = new ExpressionNode[dstOp.getNumberOfInputs()];
		
		if (origChildren.length > dstOp.getNumberOfInputs())
		{
			for (int i=0; i<dstOp.getNumberOfInputs(); i++)
			{
				result[i] = origChildren[i];
			}
		}
		else
		{
			for (int i=0; i<origChildren.length; i++)
			{
				result[i] = origChildren[i];
			}
			
			for (int i=origChildren.length; i<dstOp.getNumberOfInputs(); i++)
			{
				result[i] = TreeGenerator.randomNode(TreeGenerator.getNewTreeDepth());
			}
		}
		
		return result;
	}
	
	@SuppressWarnings("unchecked")
	private static HashMap<String, Double> adaptProperties(HashMap<String, Double> pOrig, PropertyData dstInfo)
	{
		Stack<String> unmatchedProps = new Stack<String>();
		HashMap<String, Double> pOrigCpy = (HashMap<String, Double>) pOrig.clone();
		HashMap<String, Double> pOut = new HashMap<String, Double>();
		
		// first match up all properties with the same names
		for (String currProp : dstInfo.getPropertyNames())
		{
			if (pOrig.containsKey(currProp))
			{
				pOut.put(currProp, wrapValue(pOrig.get(currProp), 
							dstInfo.getMinimum(currProp), dstInfo.getMaximum(currProp)));
				
				pOrigCpy.remove(currProp);
			}
			else
			{
				unmatchedProps.push(currProp);
			}
		}
		
		// then record all values which weren't matched up above
		Stack<Double> unmatchedValues = new Stack<Double>();
		
		for (double v : pOrigCpy.values())
		{
			unmatchedValues.push(v);
		}
		
		double currVal;
		
		// for each unmatched property, take an unmatched value until there are none left, then randomly
		for (String prop : unmatchedProps)
		{
			if (unmatchedValues.empty())
			{
				double min, max;
				min = dstInfo.getMinimum(prop);
				max = dstInfo.getMaximum(prop);
				currVal = Math.random()*(max-min) + min;
			}
			else
			{
				currVal = wrapValue(unmatchedValues.pop(), dstInfo.getMinimum(prop), dstInfo.getMaximum(prop));
			}
				
			
			pOut.put(prop, currVal);
		}
		
		return pOut;
	}

	private static double randomOffset(double min, double max)
	{
		final double factor = 0.2;
		return factor*(max-min)*(Math.random()*2.0 - 1.0);
	}
	
	private static HashMap<String, Double> jitteredParameters(HashMap<String, Double> propIn, PropertyData rangeInfo)
	{
		HashMap<String,Double> newProp = new HashMap<String,Double>();
		
		for (String prop : rangeInfo.getPropertyNames())
		{
			newProp.put(prop, wrapValue(propIn.get(prop) + randomOffset(rangeInfo.getMinimum(prop), rangeInfo.getMaximum(prop)), 
										rangeInfo.getMinimum(prop), rangeInfo.getMaximum(prop)));
		}
		
		return newProp;
	}

	private static ExpressionNode[] mutateChildren(ExpressionNode[] childrenIn)
	{
		ExpressionNode[] newChildren = new ExpressionNode[childrenIn.length];
		double childMutateProb = ExpressionNode.getMutationProbability(childrenIn.length);
		
		for (int i=0; i<newChildren.length; i++)
		{
			newChildren[i] = childrenIn[i].createMutatedInternal(childMutateProb);
		}
		
		return newChildren;
	} 
	
	private static ExpressionNode[] copyChild(ExpressionNode[] childrenIn, int idxSrc, int idxDst)
	{
		ExpressionNode[] newChildren = new ExpressionNode[childrenIn.length];
		
		for (int i=0; i<newChildren.length; i++)
		{
			newChildren[i] = childrenIn[i];
		}
		
		newChildren[idxDst] = newChildren[idxSrc];
		
		return newChildren;
	}
	
	@Override
	public ExpressionNode createMutatedInternal(double mutationProb) {
		if (Math.random() <= mutationProb)
		{
			MutationType mutation = ExpressionNode.randomMutationType(true);
			
			
			switch (mutation)
			{
				case JITTER:
				{
					// take parameter values and jitter randomly, then wrap
					Class<?> opClass = _operation.getClass();
					ParametrizedOperation newOp = null;
					try {
						newOp = (ParametrizedOperation)opClass.newInstance();
					} catch (InstantiationException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IllegalAccessException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					newOp.initWithParameters(jitteredParameters(_operation.getParameters(), newOp.getPropertyData()));
					return new OperationNode(newOp, mutateChildren(_children));
				}
					
				case OP_SWAP:
				{
					// replace operation with another, wrapping parameters as needed
					ParametrizedOperation newOp = TreeGenerator.randomUninitializedOperation();
					newOp.initWithParameters(adaptProperties(_operation.getParameters(), newOp.getPropertyData()));;
					
					return new OperationNode(newOp, mutateChildren(adaptChildren(_children, newOp)));	
				}
				case UNWRAP:
					// replace self with child
					return _children[(int)(Math.random()*_children.length)]
					                 .createMutatedInternal(getBaseMutationProbability());
					
				case COPY_SIBLING:
					return new OperationNode(_operation, mutateChildren(copyChild(_children, (int)Math.random()*_children.length, 
																			  (int)Math.random()*_children.length)));
					
				case REPLACE:
					return TreeGenerator.randomNode(TreeGenerator.getNewTreeDepth());
					
				case OP_WRAP:
					return TreeGenerator.randomOperationNode(TreeGenerator.getNewTreeDepth(), this);
					
				default:
					assert(false);
					return null;
			}
		}
		else
		{
			return new OperationNode(_operation, mutateChildren(_children));
		}
	}
	
	public void print(String indent)
	{
		System.out.println(indent + _operation);
		for (ExpressionNode child : _children)
		{
			child.print(indent + "---");
		}
	}

	@Override
	protected ExpressionNode createMatedWithLeaf(ElementNode lf) {
		// mating operation with leaf: pick one at random
		if (Math.random() > 0.5)
		{
			return this;
		}
		else
		{
			return lf;
		}
	}

	@Override
	protected ExpressionNode createMatedWithOperation(OperationNode opNode) {
		// crossing operation with operation: the good stuff!
		// if same class: cross parameter values
		if (opNode.getOperation().getClass().equals(this.getOperation().getClass()))
		{
			HashMap<String, Double> newParams = crossOperationParams(this.getOperation(), opNode.getOperation());
			ParametrizedOperation newOp = null ;
			try {
				newOp = (ParametrizedOperation)_operation.getClass().newInstance();
				newOp.initWithParameters(newParams);
			} catch (InstantiationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			ExpressionNode[] newChildren = new ExpressionNode[newOp.getNumberOfInputs()];
			
			// for each child, mate
			for (int i=0; i<newOp.getNumberOfInputs(); i++)
			{
				newChildren[i] = _children[i].createMated(opNode.getChildren()[i]);
			}
			
			return new OperationNode(newOp, newChildren);
		}
		// if diff operation but same number of args, pick random operation from the two
		else if (opNode.getOperation().getNumberOfInputs() == this.getOperation().getNumberOfInputs())
		{
			ParametrizedOperation newOp = Math.random()>0.5 ? opNode.getOperation() : this.getOperation();
			
			ExpressionNode[] newChildren = new ExpressionNode[newOp.getNumberOfInputs()];
			ExpressionNode[] otherChildren = opNode.getChildren();
			// for each child, mate
			for (int i=0; i<newOp.getNumberOfInputs(); i++)
			{
				newChildren[i] = _children[i].createMated(otherChildren[i]);
				
				assert(newChildren[i] != null);
				assert(_children[i] != null);
				assert(otherChildren[i] != null);
			}
			
			return new OperationNode(newOp, newChildren);
		}
		else
		{
			return Math.random()>0.5 ? this : opNode;
		}
	}

	private static HashMap<String, Double> crossOperationParams(ParametrizedOperation operation, ParametrizedOperation operation2) {
		// for each parameter, choose randomly between the two
		PropertyData pData = operation.getPropertyData();
		HashMap<String, Double> resultProps = new HashMap<String, Double>();
		
		for (String param : pData.getPropertyNames())
		{
			resultProps.put(param, Math.random()<0.5 ? 
										operation.getParameter(param) :
										operation2.getParameter(param));
		}
		return resultProps;
	}

	@Override
	protected ExpressionNode visit(ExpressionNode expr) {
		return expr.createMatedWithOperation(this);
	}
	
	public int countLeaves()
	{
		int ct = 0;
		
		for (ExpressionNode n : _children)
		{
			ct += n.countLeaves();
		}
		
		return ct;
	}
	
	public int countDescendantsAndSelf()
	{
		if (_numDesc == -1)
		{
			int ct = _children.length;
			
			for (ExpressionNode n : _children)
			{
				ct += n.countDescendantsAndSelf();
			}
			
			_numDesc = ct;
		}
		
		return _numDesc;
	}
	
	protected ExpressionNode selectRandomDescendantOrSelf()
	{
		int subtreeSize = countDescendantsAndSelf();
		int[] counts = new int[_children.length];
		
		for (int i=0; i<counts.length; i++)
		{
			counts[i] = _children[i].countDescendantsAndSelf();
		}
		
		int randSelect = (int)(Math.random()*subtreeSize);
		
		if (randSelect == subtreeSize-1)
		{
			return this;
		}
		else
		{
			int cdfi = 0;
			for (int i=0; i<counts.length; i++)
			{
				cdfi += counts[i];
				
				if (randSelect <= cdfi)
				{
					return _children[i];
				}
			}
			
			// something went terribly wrong
			assert(false);
			
			return null;
		}
	}
	 
	protected ExpressionNode replaceDescendantOrSelf(ExpressionNode replaceThis, ExpressionNode withThis)
	{
		if (replaceThis != this)
		{
			ExpressionNode[] newChildren = new ExpressionNode[_children.length];
			
			for (int i=0; i<newChildren.length; i++)
			{
				newChildren[i] = _children[i].replaceDescendantOrSelf(replaceThis, withThis);
			}
			
			return new OperationNode(_operation, newChildren);
		}
		else
		{
			return withThis;
		}
	}
	
}
