package hsm.test;

import java.util.HashMap;

import hsm.evo.OperationMetadata;
import hsm.evo.ParametrizedOperation;
import hsm.evo.TreeGenerator;
import hsm.global.Config;

public class StatsTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		double expectedBranching = getExpectedBranching();
		int depth = Config.getConfig().getInt(TreeGenerator.NEW_TREE_HEIGHT);
		
		System.out.println(expectedBranching);
		System.out.println(depth);
		System.out.println(Math.pow(expectedBranching, depth));
		
		HashMap<Class<?>, Integer> counts = new HashMap<Class<?>, Integer>();
		
		int numTrial = 1000000;
		Class<?> currClass;
		int currCount;
		for (int i=0; i<numTrial; i++)
		{
			currClass = TreeGenerator.randomOperationClass();
			
			if (counts.containsKey(currClass))
			{
				currCount = counts.get(currClass);
				
				counts.remove(currClass);
				counts.put(currClass, currCount+1);
			}
			else
			{
				counts.put(currClass, 1);
			}
			
		}
		double tot = getOperationProbTotal();
		OperationMetadata om = OperationMetadata.getInstance();
		
		for (Class<?> c : counts.keySet())
		{
			System.out.println(c.getName() + ":\t" + (double)counts.get(c)/numTrial + " (vs. " + om.getOperationProbability(c)/tot + ")");
		}
//		}
//		int accum = 0;
//		int currCt = 0;
//		int numTrial = 10;
//		
//		for (int i=0; i<numTrial; i++)
//		{
//			currCt = TreeGenerator.randomNode(depth).countLeaves();
//			
//			System.out.println("Leaves: " + currCt);
//			accum += currCt;
//		}
//		
//		System.out.println("Average: " + accum/numTrial);
	}
	
	public static double getOperationProbTotal()
	{
		double sum = 0.0;
		OperationMetadata om = OperationMetadata.getInstance();
		Class<?>[] cls = om.getOperationClasses();
		
		for (Class<?> c : cls)
		{
			sum += om.getOperationProbability(c);
		}
		
		return sum;
	}
	
	public static double getExpectedBranching()
	{
		double sum = getOperationProbTotal();
		
		OperationMetadata om = OperationMetadata.getInstance();
		Class<?>[] cls = om.getOperationClasses();
		
		double branchSum = 0.0;
		ParametrizedOperation op = null;;
		
		for (Class<?> c : cls)
		{
			try {
				op = (ParametrizedOperation)c.newInstance();
			} catch (InstantiationException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (IllegalAccessException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			op.initWithParameters(TreeGenerator.randomParametersForClass(c));
			
			branchSum += op.getNumberOfInputs()*(om.getOperationProbability(c)/sum);
		}
		
		return branchSum;
	}

}
