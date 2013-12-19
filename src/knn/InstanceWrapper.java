package knn;

import common.Instance;

public class InstanceWrapper
{
	public Instance instance;
	public double similarityScore;
	
	public InstanceWrapper(Instance instance, double similarityScore)
	{
		this.instance = instance;
		this.similarityScore = similarityScore;
	}
}
