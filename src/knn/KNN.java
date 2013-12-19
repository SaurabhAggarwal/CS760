package knn;

import java.util.List;

import common.AttributeType;
import common.DataSet;
import common.Instance;
import entities.Description;
import entities.Keyword;
import entities.Query;
import entities.Title;
import entities.User;

public class KNN
{
	private int k;
	private DataSet dataset;
	private FixedSizePriorityQueue pqueue;
	
	public KNN(DataSet dataset, int k)
	{
		this.dataset = dataset;
		this.k = k;
		this.pqueue = new FixedSizePriorityQueue(k, new CustomComparator(), k);
	}
	
	public double predictCTR(Instance query, List<Integer> features)
	{
		for(Instance inst : dataset.allInstances)
		{
			double similarity = calculateSimilarity(dataset, inst, query, features);
			pqueue.customAdd(new InstanceWrapper(inst, similarity));
		}
		
		double toReturn = 0.0;
		for(int i = 0; i < k; i++)
			toReturn += pqueue.remove().instance.getClickProbability();
		toReturn /= (1.0 * k);
		
		return toReturn;
	}

	public void train(List<Instance> train, int k2)
	{
		// TODO Auto-generated method stub
		
	}
	
	public static double calculateSimilarity(DataSet ds, Instance inst, Instance query, List<Integer> features)
	{
		double similarity = 0.0;
		
		if(features.contains(AttributeType.DESCRIPTION_ID.index))
			similarity += Description.similarity(
				ds.descKeyWordsMap.get(inst.attributeValues.get(AttributeType.DESCRIPTION_ID.index)),
				ds.descKeyWordsMap.get(query.attributeValues.get(AttributeType.DESCRIPTION_ID.index))
			);
		
		if(features.contains(AttributeType.KEYWORD_ID.index))
			similarity += Keyword.similarity(
				ds.adKeywordKeyWordsMap.get(inst.attributeValues.get(AttributeType.KEYWORD_ID.index)),
				ds.adKeywordKeyWordsMap.get(query.attributeValues.get(AttributeType.KEYWORD_ID.index))
			);
		
		if(features.contains(AttributeType.QUERY_ID.index))
			similarity += Query.similarity(
				ds.queryKeyWordsMap.get(inst.attributeValues.get(AttributeType.QUERY_ID.index)),
				ds.queryKeyWordsMap.get(query.attributeValues.get(AttributeType.QUERY_ID.index))
			);
		
		if(features.contains(AttributeType.TITLE_ID.index))
			similarity += Title.similarity(
				ds.titleKeyWordsMap.get(inst.attributeValues.get(AttributeType.TITLE_ID.index)),
				ds.titleKeyWordsMap.get(query.attributeValues.get(AttributeType.TITLE_ID.index))
			);
		
		if(features.contains(AttributeType.USER_ID.index))
			similarity += User.similarity(
				ds.usersMap.get(inst.attributeValues.get(AttributeType.USER_ID.index)),
				ds.usersMap.get(query.attributeValues.get(AttributeType.USER_ID.index))
			);
		
		if(features.contains(AttributeType.AD_ID.index))
			if(inst.attributeValues.get(AttributeType.AD_ID.index).equals(query.attributeValues.get(AttributeType.AD_ID.index)))
				similarity += 1.0;
		
		if(features.contains(AttributeType.ADVERTISER_ID.index))
			if(inst.attributeValues.get(AttributeType.ADVERTISER_ID.index).equals(query.attributeValues.get(AttributeType.ADVERTISER_ID.index)))
				similarity += 1.0;
		
		if(features.contains(AttributeType.DEPTH.index))
			if(inst.attributeValues.get(AttributeType.DEPTH.index).equals(query.attributeValues.get(AttributeType.DEPTH.index)))
				similarity += 1.0;
		
		if(features.contains(AttributeType.DISPLAY_URL.index))
			if(inst.attributeValues.get(AttributeType.DISPLAY_URL.index).equals(query.attributeValues.get(AttributeType.DISPLAY_URL.index)))
				similarity += 1.0;
		
		if(features.contains(AttributeType.POSITION.index))
			if(inst.attributeValues.get(AttributeType.POSITION.index).equals(query.attributeValues.get(AttributeType.POSITION.index)))
				similarity += 1.0;
		
		return similarity;
	}
}
