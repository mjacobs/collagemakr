package hsm.evo;

import hsm.global.Config;

import java.awt.Color;

public final class EvolutionUtil {
	private EvolutionUtil(){}
	
	public static double wrapValue(double x, double min, double max)
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
	
	public static Color mutateColor(Color col)
	{
		if (Math.random() <= Config.getConfig().getDouble("mutation_prob"))
		{
			float[] hsv = new float[3];
			
			Color.RGBtoHSB(col.getRed(), col.getGreen(), col.getBlue(), hsv);
			for (int i=0; i<3; i++)
			{
				hsv[i] += 0.2*(Math.random()*2.0 - 1.0);
				hsv[i] = (float) wrapValue(hsv[i], 0.0, 1.0);
			}
			
			return Color.getHSBColor(hsv[0], hsv[1], hsv[2]);
		}
		else
		{
			return col;
		}		
	}
	
	public static Color mateColors(Color col1, Color col2)
	{
		float[] hsv1 = new float[3];
		float[] hsv2 = new float[3];
		float[] hsvr = new float[3];
		
		Color.RGBtoHSB(col1.getRed(), col1.getGreen(), col1.getBlue(), hsv1);
		Color.RGBtoHSB(col2.getRed(), col2.getGreen(), col2.getBlue(), hsv2);
		
		for (int i=0; i<3; i++)
		{
			hsvr[i] = (Math.random()<0.5) ? hsv1[i] : hsv2[i];
		}
		
		return Color.getHSBColor(hsvr[0], hsvr[1], hsvr[2]);
	}
}
