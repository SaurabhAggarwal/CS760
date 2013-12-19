package knn;

import java.util.Comparator;

public class CustomComparator implements Comparator<InstanceWrapper>
{
	@Override
	public int compare(InstanceWrapper a, InstanceWrapper b)
	{
		if(a.similarityScore < b.similarityScore)
			return -1;
		if(a.similarityScore > b.similarityScore)
			return 1;
		return 0;
	}
}
