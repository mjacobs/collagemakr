package hsm.evo;

import java.util.ArrayList;
import java.util.Vector;

public class Population
{
	private int _eliteCount;
	protected int _generation;

	protected Organism[] _orgs;

	public Population(Organism[] orgs, int generation)
	{
		_orgs = orgs;
		_generation = generation;
		_eliteCount = orgs.length/2;
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
	
	public Vector<Organism> getAllFittest()
	{
		double maxFit = Double.NEGATIVE_INFINITY;
		Vector<Organism> maxOrgs = new Vector<Organism>();
		
		for (Organism o : _orgs)
		{
			assert(o.getFitness() >= 0.0);
			
			System.out.println("curr = " + o.getFitness() + ", max="+maxFit + " numMax: " + maxOrgs.size());
			
			if (o.getFitness() > maxFit)
			{
				System.out.println("clearing");
				maxFit = o.getFitness();
				maxOrgs.clear();
				maxOrgs.add(o);
			}
			else if (o.getFitness() == maxFit)
			{
				System.out.println("adding");
				maxOrgs.add(o);
			}
			
		}
		
		return maxOrgs;
	}
	
	public Organism getFittest()
	{
		Vector<Organism> maxOrgs = getAllFittest();
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
		Organism[] newOrgs = new Organism[_orgs.length];
		
		Vector<Organism> elites = getAllFittest();
		System.out.println("numelites: " + elites.size());
		
		double maxFit = elites.get(0).getFitness();
		
		if (maxFit == 0.0) return randomPopulation(_orgs.length);
		else
		{
			int keepElites = Math.min(_eliteCount, elites.size());
			
			for (int i=0; i<keepElites; i++)
			{
				int randIdx = (int)(Math.random()*elites.size());
				
				newOrgs[i] = elites.get(randIdx).unrankedCopy();
				
				elites.remove(randIdx);
			}
			
			int tot = 0;
			int num = 0;
			
			for (int i=keepElites; i<newOrgs.length; i++)
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

		
		for (int i=0; i<100; i++)
		{
			for (Organism o : p.getOrganisms())
				o.setFitness(1.0);
			
			p = p.nextGeneration();
		}
		System.out.println("Done.");
		
	}
}
