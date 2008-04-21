package hsm.evo;

public class Organism
{
	private Composition _composition;

	private double _fitness;

	public Organism(Composition comp)
	{
		_composition = comp;
		_fitness = 1.0;
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
		return new Organism(new Composition(_composition.getRoot()
				.createMutated()));
	}

	public Organism matedCopy(Organism ladyFriend)
	{
		return new Organism(new Composition(_composition.getRoot().createMated(
				ladyFriend.getComposition().getRoot())));
	}

	public static Organism randomOrganism()
	{
		return new Organism(Composition.randomComposition());
	}
	
	public void printGenome()
	{
		_composition.printTree();
	}
	
	public Organism unrankedCopy()
	{
		return new Organism(_composition);
	}
}
