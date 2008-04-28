package hsm.evo;

import java.util.HashMap;

import hsm.global.Config;
import hsm.image.LayerImage;

public abstract class ExpressionNode {
	// evaluation
	public abstract LayerImage evaluate();
	
	// mutation
	public final ExpressionNode createMutated() { return createMutatedInternal(getBaseMutationProbability()); }
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
	public final static String MUTATION_PROB = "mutation_prob";
	public final static String CROSSOVER = "crossover";
	
	public final static String PROB_REPLACE = "prob_mutation_replace";
	public final static String PROB_JITTER = "prob_mutation_jitter";
	public final static String PROB_OP_SWAP = "prob_mutation_op_swap";
	public final static String PROB_OP_WRAP = "prob_mutation_op_wrap";
	public final static String PROB_UNWRAP = "prob_mutation_unwrap";
	public final static String PROB_COPY_SIBLING = "prob_mutation_copy_sibling";
	
	public static double getBaseMutationProbability()
	{
		return Config.getConfig().getDouble(MUTATION_PROB);
	}
	
	static
	{
		Config.getConfig().registerDouble(MUTATION_PROB, 0.2);
		Config.getConfig().registerBoolean(CROSSOVER, false);
		Config.getConfig().registerDouble(PROB_REPLACE, 1.0);
		Config.getConfig().registerDouble(PROB_JITTER, 1.0);
		Config.getConfig().registerDouble(PROB_OP_SWAP, 1.0);
		Config.getConfig().registerDouble(PROB_OP_WRAP, 1.0);
		Config.getConfig().registerDouble(PROB_UNWRAP, 1.0);
		Config.getConfig().registerDouble(PROB_COPY_SIBLING, 1.0);
		
		
		_typeProbs = new HashMap<MutationType, Double>();
		_typeOnlyOp = new HashMap<MutationType, Boolean>();
		
		double[] factors = 
		{
			Config.getConfig().getDouble(PROB_REPLACE),
			Config.getConfig().getDouble(PROB_JITTER),
			Config.getConfig().getDouble(PROB_OP_SWAP),
			Config.getConfig().getDouble(PROB_OP_WRAP),
			Config.getConfig().getDouble(PROB_UNWRAP),
			Config.getConfig().getDouble(PROB_COPY_SIBLING)
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
			return getBaseMutationProbability();
		}
		else
		{
			return getBaseMutationProbability()/exprLen;
		}
	}
	
	// mating
	public final ExpressionNode createMated(ExpressionNode friend)
	{
		if (Config.getConfig().getBoolean(CROSSOVER))
		{
			return this.replaceDescendantOrSelf(selectRandomDescendantOrSelf(), 
												friend.selectRandomDescendantOrSelf());
		}
		else
		{
			return friend.visit(this);
		}

	}
	
	// Traversal Mating
	protected abstract ExpressionNode visit(ExpressionNode expr);
	protected abstract ExpressionNode createMatedWithLeaf(ElementNode op);
	protected abstract ExpressionNode createMatedWithOperation(OperationNode op);
	
	// Crossover Mating
	protected abstract ExpressionNode selectRandomDescendantOrSelf();
	protected abstract ExpressionNode replaceDescendantOrSelf(ExpressionNode replaceThis, ExpressionNode withThis);
	
	public abstract int countDescendantsAndSelf();
	public abstract int countLeaves();
	
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
