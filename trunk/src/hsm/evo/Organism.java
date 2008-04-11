package hsm.evo;

public class Organism {
	private Composition _composition;
	private double _fitness;
	
	public Organism(Composition comp)
	{
		_composition = comp;
		_fitness = 0.0;
	}
	
	public Composition getComposition()
	{
		return _composition;
	}
	
	public double getFitness()
	{
		return _fitness;
	}
	
	public void setFitness(double fit)
	{
		_fitness = fit;
	}
	
	public Organism mutatedCopy()
	{
		return new Organism(new Composition(_composition.getRoot().createMutated()));
	}
	
	public Organism matedCopy(Organism friend)
	{
		return new Organism(new Composition(_composition.getRoot().createMated(friend.getComposition().getRoot())));
	}
	
	public static Organism randomOrganism()
	{
		return new Organism(Composition.makeRandomComposition());
	}
}
