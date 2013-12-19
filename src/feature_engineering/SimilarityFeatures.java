package feature_engineering;

import java.util.ArrayList;
import java.util.List;

import common.AttributeType;
import common.DataSet;
import common.Instance;

public class SimilarityFeatures
{
	public static Long[] findSimilarityQueryAndAd(DataSet ds, Instance inst)
	{
		Long[] toReturn = new Long[2];
		toReturn[0] = 0L;
		toReturn[1] = 0L;
		
		List<String> queryKeywords = new ArrayList<String>();
		List<String> adKeywords = new ArrayList<String>();
		
		String query = inst.attributeValues.get(AttributeType.QUERY_ID.index);
		queryKeywords = ds.queryKeyWordsMap.get(query).keywords;
		
		String keyword = inst.attributeValues.get(AttributeType.KEYWORD_ID.index);
		List<String> keywordTokens = ds.adKeywordKeyWordsMap.get(keyword).keywords;
		for(String token : keywordTokens)
			adKeywords.add(token);
		String title = inst.attributeValues.get(AttributeType.TITLE_ID.index);
		List<String> titleTokens = ds.titleKeyWordsMap.get(title).keywords;
		for(String token : titleTokens)
			adKeywords.add(token);
		String desc = inst.attributeValues.get(AttributeType.DESCRIPTION_ID.index);
		List<String> descTokens = ds.descKeyWordsMap.get(desc).keywords;
		for(String token : descTokens)
			adKeywords.add(token);
		
		Integer[] temp = getProportionQueryKeywordsCoveredInAd(queryKeywords, adKeywords);
		toReturn[0] += temp[0];
		toReturn[1] += temp[1];
		toReturn[0] += getEarlyMatchScore(queryKeywords, adKeywords);
		toReturn[1] += 100;
		
		return toReturn;
	}

	private static Integer[] getProportionQueryKeywordsCoveredInAd(
			List<String> queryKeywords, List<String> adKeywords)
	{
		Integer[] toReturn = new Integer[2];
		toReturn[0] = 0;
		toReturn[1] = 0;
		for(String query : queryKeywords)
		{
			if(adKeywords.contains(query))
				toReturn[0] += 1;
		}
		toReturn[1] = queryKeywords.size();
		
		return toReturn;
	}
	
	private static int getEarlyMatchScore(
			List<String> queryKeywords, List<String> adKeywords)
	{
		int toReturn = 100;
		
		for(String query : queryKeywords)
		{
			int index = adKeywords.indexOf(query);
			if(index != -1)
				toReturn = Math.min(toReturn, index);
		}
		
		return toReturn;
	}
}
