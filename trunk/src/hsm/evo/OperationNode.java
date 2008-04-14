package hsm.evo;

import java.util.HashMap;
import java.util.Stack;

import hsm.evo.ParametrizedOperation;
import hsm.evo.OperationMetadata.PropertyData;
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
				result[i] = TreeGenerator.randomNode(3);
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

	private static double randomOffset()
	{
		return 10.0*(Math.random()*2.0 - 1.0);
	}
	
	private static HashMap<String, Double> jitteredParameters(HashMap<String, Double> propIn, PropertyData rangeInfo)
	{
		HashMap<String,Double> newProp = new HashMap<String,Double>();
		
		for (String prop : rangeInfo.getPropertyNames())
		{
			newProp.put(prop, wrapValue(propIn.get(prop) + randomOffset(), 
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
			MutationType mutation = ExpressionNode.randomMutationType(false);
			
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
					return _children[(int)Math.random()*_children.length]
					                 .createMutatedInternal(_mutationProb);
					
				case COPY_SIBLING:
					return new OperationNode(_operation, mutateChildren(copyChild(_children, (int)Math.random()*_children.length, 
																			  (int)Math.random()*_children.length)));
					
				case REPLACE:
					return TreeGenerator.randomNode(4);
					
				case OP_WRAP:
					return TreeGenerator.randomOperationNode(4, this);
					
				default:
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

}
