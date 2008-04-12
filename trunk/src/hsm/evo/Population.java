package hsm.evo;

import java.util.ArrayList;

public class Population
{

	protected int _generation;

	protected Organism[] _orgs;

	public Population(Organism[] orgs, int generation)
	{
		_orgs = orgs;
		_generation = generation;
	}

	public int getGeneration()
	{
		return _generation;
	}

	public Organism[] getOrganisms()
	{
		return _orgs;
	}

	public static Population randomPopulation(int popSize)
	{
		Organism[] orgs = new Organism[] {};
		ArrayList<Organism> arrayList = new ArrayList<Organism>();

		for (int i = 0; i < popSize; i++)
		{
			arrayList.add(Organism.randomOrganism());
		}

		return new Population(arrayList.toArray(orgs), 1);
	}

	public Population nextGeneration()
	{
		// TODO implement me! (assigned to bjmoore)
		return null;
	}
}
