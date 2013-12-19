package knn;

import java.util.PriorityQueue;

class FixedSizePriorityQueue extends PriorityQueue<InstanceWrapper> 
{
	private static final long serialVersionUID = 1L;
	private int MAX_SIZE = 5;
	
	public FixedSizePriorityQueue(int i, CustomComparator customComparator, int K)
	{
		super(i, customComparator);
		this.MAX_SIZE = K;
	}
	
	public void customAdd(InstanceWrapper i)
	{
		add(i);
		if(size() > MAX_SIZE)
			remove();
	}
}