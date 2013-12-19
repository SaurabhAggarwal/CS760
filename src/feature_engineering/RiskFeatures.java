package feature_engineering;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import common.AttributeType;
import common.BasePredictor;
import common.DataSet;
import common.Instance;
import entities.User;

public class RiskFeatures implements BasePredictor
{
	public Long[] getRiskForFeatureValue(AttributeType type, String attrValue)
	{
		Long[] toReturn = new Long[2];
		toReturn[0] = 0L;
		toReturn[1] = 0L;
		
		switch(type)
		{
		case AD_ID:
			if(adid_to_clickcount.get(attrValue) == null)
				break;
			toReturn[0] = adid_to_clickcount.get(attrValue)[0];
			toReturn[1] = adid_to_clickcount.get(attrValue)[1];
			break;
		case ADVERTISER_ID:
			if(advertiserid_to_clickcount.get(attrValue) == null)
				break;
			toReturn[0] = advertiserid_to_clickcount.get(attrValue)[0];
			toReturn[1] = advertiserid_to_clickcount.get(attrValue)[1];
			break;
		case DISPLAY_URL:
			if(displayurl_to_clickcount.get(attrValue) == null)
				break;
			toReturn[0] = displayurl_to_clickcount.get(attrValue)[0];
			toReturn[1] = displayurl_to_clickcount.get(attrValue)[1];
			break;
		case DEPTH:
			if(depth_to_clickcount.get(attrValue) == null)
				break;
			toReturn[0] = depth_to_clickcount.get(attrValue)[0];
			toReturn[1] = depth_to_clickcount.get(attrValue)[1];
			break;
		case POSITION:
			if(position_to_clickcount.get(attrValue) == null)
				break;
			toReturn[0] = position_to_clickcount.get(attrValue)[0];
			toReturn[1] = position_to_clickcount.get(attrValue)[1];
			break;
		
		
		case DESCRIPTION_ID:
		case KEYWORD_ID:
		case QUERY_ID:
		case TITLE_ID:
			List<String> keywords = getKeywords(ds, type, attrValue);
			toReturn[0] = 0L;
			toReturn[1] = 0L;
			for(String keyword : keywords)
			{
				if(keyword_to_clickcount.get(keyword) != null)
				{
					toReturn[0] += keyword_to_clickcount.get(keyword)[0];
					toReturn[1] += keyword_to_clickcount.get(keyword)[1];
				} 
			}
			break;
			
		case USER_ID:
			toReturn[0] = 0L;
			toReturn[1] = 0L;
			
			if(user_to_clickcount.get(attrValue) != null)
			{
				toReturn[0] += user_to_clickcount.get(attrValue)[0];
				toReturn[1] += user_to_clickcount.get(attrValue)[1];
			}
			
			if(ds.usersMap.get(attrValue) != null)
			{
				if(age_to_clickcount.get(ds.usersMap.get(attrValue).age.toString()) != null)
				{
					toReturn[0] += age_to_clickcount.get(ds.usersMap.get(attrValue).age.toString())[0];
					toReturn[1] += age_to_clickcount.get(ds.usersMap.get(attrValue).age.toString())[1];
				}
				
				if(gender_to_clickcount.get(ds.usersMap.get(attrValue).gender.toString()) != null)
				{
					toReturn[0] += gender_to_clickcount.get(ds.usersMap.get(attrValue).gender.toString())[0];
					toReturn[1] += gender_to_clickcount.get(ds.usersMap.get(attrValue).gender.toString())[1];
				}
			}
			
			break;
			
		default:
			break;
		}
		
		return toReturn;
	}
	
	private DataSet ds = null;
	
	private Map<String, Long[]> user_to_clickcount = null;
	private Map<String, Long[]> gender_to_clickcount = null;
	private Map<String, Long[]> age_to_clickcount = null;
	
	private Map<String, Long[]> displayurl_to_clickcount = null;
	private Map<String, Long[]> adid_to_clickcount = null;
	private Map<String, Long[]> advertiserid_to_clickcount = null;
	private Map<String, Long[]> depth_to_clickcount = null;
	private Map<String, Long[]> position_to_clickcount = null;
	
	private Map<String, Long[]> keyword_to_clickcount = null;
	
	public RiskFeatures()
	{
		user_to_clickcount = new HashMap<String, Long[]>();
		gender_to_clickcount = new HashMap<String, Long[]>();
		age_to_clickcount = new HashMap<String, Long[]>();
		
		displayurl_to_clickcount = new HashMap<String, Long[]>();
		adid_to_clickcount = new HashMap<String, Long[]>();
		advertiserid_to_clickcount = new HashMap<String, Long[]>();
		depth_to_clickcount = new HashMap<String, Long[]>();
		position_to_clickcount = new HashMap<String, Long[]>();
		
		keyword_to_clickcount = new HashMap<String, Long[]>();	
	}
	
	private void addToMap(AttributeType type, String id, Long clicked, Long impressed)
	{
		switch(type)
		{
		case DISPLAY_URL:
		case AD_ID:
		case ADVERTISER_ID:
		case DEPTH:
		case POSITION:
			Map<String, Long[]> toChange = getMap(type);
			Long[] curr = toChange.get(id);
			if(curr == null)
			{
				curr = new Long[2];
				curr[0] = 0L;
				curr[1] = 0L;
			}
			curr[0] += clicked;
			curr[1] += impressed;
			toChange.put(id, curr);
			break;
			
		case DESCRIPTION_ID:
		case KEYWORD_ID:
		case QUERY_ID:
		case TITLE_ID:
			List<String> keywords = getKeywords(ds, type, id);
			for(String keyword : keywords)
			{
				curr = keyword_to_clickcount.get(keyword);
				if(curr == null)
				{
					curr = new Long[2];
					curr[0] = 0L;
					curr[1] = 0L;
				}
				curr[0] += clicked;
				curr[1] += impressed;
				keyword_to_clickcount.put(id, curr);
			}
			break;
		case USER_ID:
			User user = ds.usersMap.get(id);
			
			curr = user_to_clickcount.get(id);
			if(curr == null)
			{
				curr = new Long[2];
				curr[0] = 0L;
				curr[1] = 0L;
			}
			curr[0] += clicked;
			curr[1] += impressed;
			user_to_clickcount.put(id, curr);
			
			curr = age_to_clickcount.get(user.age.toString());
			if(curr == null)
			{
				curr = new Long[2];
				curr[0] = 0L;
				curr[1] = 0L;
			}
			curr[0] += clicked;
			curr[1] += impressed;
			age_to_clickcount.put(user.age.toString(), curr);
			
			curr = gender_to_clickcount.get(user.gender.toString());
			if(curr == null)
			{
				curr = new Long[2];
				curr[0] = 0L;
				curr[1] = 0L;
			}
			curr[0] += clicked;
			curr[1] += impressed;
			gender_to_clickcount.put(user.gender.toString(), curr);
			
			break;
		default:
			break;
		}
	}
		
	private List<String> getKeywords(DataSet ds, AttributeType type, String id)
	{
		switch(type)
		{
		case DESCRIPTION_ID:
			if(ds.descKeyWordsMap.get(id) == null)
				return new ArrayList<String>();
			return ds.descKeyWordsMap.get(id).keywords;
		case KEYWORD_ID:
			if(ds.adKeywordKeyWordsMap.get(id) == null)
				return new ArrayList<String>();
			return ds.adKeywordKeyWordsMap.get(id).keywords;
		case QUERY_ID:
			if(ds.queryKeyWordsMap.get(id) == null)
				return new ArrayList<String>();
			return ds.queryKeyWordsMap.get(id).keywords;
		case TITLE_ID:
			if(ds.titleKeyWordsMap.get(id) == null)
				return new ArrayList<String>();
			return ds.titleKeyWordsMap.get(id).keywords;
		default:
			return new ArrayList<String>();
		}
	}
	
	private void addInstanceToMap(Instance inst)
	{
		for(int i = 2; i < inst.attributeValues.size(); i++)
			addToMap(AttributeType.values()[i], inst.attributeValues.get(i), inst.getClickedTimes(), inst.getImpressedTimes());
	}
	
	public void train(DataSet ds)
	{
		this.ds = ds;
		for(Instance inst : ds.allInstances)
			addInstanceToMap(inst);
	}
	
	private Map<String, Long[]> getMap(AttributeType type)
	{
		switch(type)
		{
		case AD_ID:
			 return adid_to_clickcount;
		case ADVERTISER_ID:
			return advertiserid_to_clickcount;
		case DEPTH:
			return depth_to_clickcount;
		case DISPLAY_URL:
			return displayurl_to_clickcount;
		case POSITION:
			return position_to_clickcount;
		default:
			return null;
		}
	}
	
	public double predictCTR(Instance inst, List<Integer> features)
	{
		Long[] toReturn = SimilarityFeatures.findSimilarityQueryAndAd(ds, inst);
		
		for(int i : features)
		{
			Long[] curr = getRiskForFeatureValue(AttributeType.values()[i], inst.attributeValues.get(i));
			toReturn[0] += curr[0];
			toReturn[1] += curr[1];
		}
		
		return 1.0 * toReturn[0] / toReturn[1];
	}
}
