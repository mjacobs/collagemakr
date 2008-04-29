package hsm.evo;

import java.util.ArrayList;
import java.util.Vector;

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
	
	public int getSize()
	{
		return _orgs.length;
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
			System.out.println("Creating organism " + i);
			arrayList.add(Organism.randomOrganism());
		}

		return new Population(arrayList.toArray(orgs), 1);
	}
	
	public Organism getFittest()
	{
		double maxFit = Double.NEGATIVE_INFINITY;
		Vector<Organism> maxOrgs = new Vector<Organism>();
		
		for (Organism o : _orgs)
		{
			assert(o.getFitness() >= 0.0);
			
			if (o.getFitness() >= maxFit)
			{
				maxFit = o.getFitness();
				
				if (o.getFitness() == maxFit)
				{
					maxOrgs.add(o);
				}
				else
				{
					maxOrgs.clear();
				}
			}
			
		}
		
		return maxOrgs.get((int)(Math.random()*maxOrgs.size()));
	}

	protected Organism randomFitOrganism(double maxFit)
	{
		Organism currOrg;
		
		do
		{
			currOrg = _orgs[(int)(Math.random()*_orgs.length)];
		} while (Math.random()*maxFit >= currOrg.getFitness());
		
		return currOrg;
	}
	
	public Population nextGeneration()
	{
		Organism fittest = getFittest();
		Organism[] newOrgs = new Organism[_orgs.length];
		double maxFit = fittest.getFitness();
		int startIdx = 0;
		
		if (maxFit == 0.0) return randomPopulation(_orgs.length);
		else
		{
			if (g_elitism)
			{
				newOrgs[0] = fittest.unrankedCopy();
				startIdx++;
			}
						
			int tot = 0;
			int num = 0;
			
			for (int i=startIdx; i<newOrgs.length; i++)
			{
				System.out.println("Making organism " + i);
				newOrgs[i] = randomFitOrganism(maxFit).mutatedCopy().matedCopy(randomFitOrganism(maxFit).mutatedCopy());
			
				tot += newOrgs[i].getComposition().getRoot().treeDepth();
				num++;
			}
			
			System.out.println("Average depth: " + tot/num);
			
			return new Population(newOrgs, _generation+1);
		}
	}
	
	public static void main(String[] vargs)
	{
		Population p = randomPopulation(4);
		System.out.println("Creation done, trying to advance generation...");
		for (Organism o : p.getOrganisms())
			o.setFitness(1.0);
		
		for (int i=0; i<10; i++)
		{
			p = p.nextGeneration();
		}
		System.out.println("Done.");
		
	}
}
