package hsm.evo;

import java.util.ArrayList;

public class Population
{
	private final boolean g_elitism = true;
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
	
	public Organism getFittest()
	{
		double maxFit = Double.NEGATIVE_INFINITY;
		Organism maxOrg = null;
		
		for (Organism o : _orgs)
		{
			assert(o.getFitness() >= 0.0);
			
			if (o.getFitness() > maxFit)
			{
				maxFit = o.getFitness();
				maxOrg = o;
			}
		}
		
		return maxOrg;
	}

	protected Organism randomFitOrganism(double maxFit)
	{
		Organism currOrg;
		
		do
		{
			currOrg = _orgs[(int)(Math.random()*_orgs.length)];
		} while (Math.random()*maxFit > currOrg.getFitness());
		
		return currOrg;
	}
	
	public Population nextGeneration()
	{
		Organism fittest = getFittest();
		Organism[] newOrgs = new Organism[_orgs.length];
		double maxFit = fittest.getFitness();
		int startIdx = 0;
		
		if (g_elitism)
		{
			newOrgs[0] = fittest.unrankedCopy();
			startIdx++;
		}
		
		for (int i=startIdx; i<newOrgs.length; i++)
		{
			newOrgs[i] = randomFitOrganism(maxFit).matedCopy(randomFitOrganism(maxFit));
		}
		
		return new Population(newOrgs, _generation+1);
	}
}
