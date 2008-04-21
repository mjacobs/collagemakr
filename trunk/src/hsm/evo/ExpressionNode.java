package hsm.evo;

import java.util.HashMap;

import hsm.image.LayerImage;

public abstract class ExpressionNode {
	// evaluation
	public abstract LayerImage evaluate();
	
	// mutation
	public final ExpressionNode createMutated() { return createMutatedInternal(_mutationProb); }
	public abstract ExpressionNode createMutatedInternal(double mutationProbability);
	
	protected enum MutationType
	{
		REPLACE,// return a new random expression
		JITTER, // jitter parameter values (operationNode only)
		OP_SWAP,// replace operation by a new one (operationNode only)
		OP_WRAP,// make node argument of new random operation
		UNWRAP,// replace by child node (operationNode only)
		COPY_SIBLING // replace by random sibling node (child of operation only)
	}
	
	protected static HashMap<MutationType, Double> _typeProbs;
	protected static HashMap<MutationType, Boolean> _typeOnlyOp; 
	protected static final double _mutationProb = 0.2;
	
	static
	{
		_typeProbs = new HashMap<MutationType, Double>();
		_typeOnlyOp = new HashMap<MutationType, Boolean>();
		
		double[] factors = 
		{
			1.0, 
			50.0,
			1.0,
			1.0,
			1.0,
			1.0
		};
		MutationType[] mutations = MutationType.values();
		assert(factors.length == mutations.length);
		
		double sum = 0.0;
		
		for (int i=0; i<mutations.length; i++)
		{
			sum += factors[i];
		}
		
		for (int i=0; i<mutations.length; i++)
		{
			_typeProbs.put(mutations[i], factors[i]/sum);
		}
		
		// initialize restrictions
		_typeOnlyOp.put(MutationType.JITTER, true);
		_typeOnlyOp.put(MutationType.OP_SWAP, true);
		_typeOnlyOp.put(MutationType.UNWRAP, true);
		_typeOnlyOp.put(MutationType.COPY_SIBLING, true);
		_typeOnlyOp.put(MutationType.REPLACE, false);
		_typeOnlyOp.put(MutationType.OP_WRAP, false);
	}
	
	protected static MutationType randomMutationType(boolean isOp)
	{
		if (isOp)
		{
			return randomMutationTypeBlind();
		}
		else
		{
			MutationType type;
			
			do
			{
				type = randomMutationTypeBlind();
			} while (_typeOnlyOp.get(type));
			
			return type; 
		}
	}
	
	protected static MutationType randomMutationTypeBlind()
	{
		MutationType[] mutations = MutationType.values();
		MutationType type = MutationType.UNWRAP;
		
		do
		{
			type = mutations[(int)(Math.random()*mutations.length)];
		} while (_typeProbs.get(type) < Math.random());
		
		return type;
	}
	
	protected static double getMutationProbability(int exprLen)
	{
		if (exprLen == 0)
		{
			return _mutationProb;
		}
		else
		{
			return _mutationProb/exprLen;
		}
	}
	
	// mating
	public final ExpressionNode createMated(ExpressionNode friend)
	{
		return friend.visit(this);
	}
	
	protected abstract ExpressionNode visit(ExpressionNode expr);
	protected abstract ExpressionNode createMatedWithLeaf(ElementNode op);
	protected abstract ExpressionNode createMatedWithOperation(OperationNode op);
	
	public abstract void print(String prefix);
	public void print() { print(""); }
	
	public static void main(String[] argv)
	{
		
		HashMap<MutationType, Integer> freqs = new HashMap<MutationType, Integer>();
		final int numSamples = 10000;
		MutationType type;
		for (int i=0; i<numSamples; i++)
		{
			type = ExpressionNode.randomMutationType(true);
			
			if (freqs.containsKey(type))
			{
				int oldFreq = freqs.get(type);
				freqs.put(type, oldFreq+1);
			}
			else
			{
				freqs.put(type, 1);
			}
		}
		
		System.out.println(freqs);
	}
}
