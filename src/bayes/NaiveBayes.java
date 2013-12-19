package bayes;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import common.AttributeType;
import common.BasePredictor;
import common.Instance;

public class NaiveBayes implements BasePredictor
{
	private Map<String, Long[]> user_to_clickcount = null;
	private Map<String, Long[]> displayurl_to_clickcount = null;
	private Map<String, Long[]> adid_to_clickcount = null;
	private Map<String, Long[]> advertiserid_to_clickcount = null;
	private Map<String, Long[]> depth_to_clickcount = null;
	private Map<String, Long[]> position_to_clickcount = null;
	private Map<String, Long[]> queryid_to_clickcount = null;
	private Map<String, Long[]> keywordid_to_clickcount = null;
	private Map<String, Long[]> titleid_to_clickcount = null;
	private Map<String, Long[]> descid_to_clickcount = null;
	
	private long totalPos;
	private long totalNeg;
	
	public NaiveBayes()
	{
		user_to_clickcount = new HashMap<String, Long[]>();
		displayurl_to_clickcount = new HashMap<String, Long[]>();
		adid_to_clickcount = new HashMap<String, Long[]>();
		advertiserid_to_clickcount = new HashMap<String, Long[]>();
		depth_to_clickcount = new HashMap<String, Long[]>();
		position_to_clickcount = new HashMap<String, Long[]>();
		queryid_to_clickcount = new HashMap<String, Long[]>();
		keywordid_to_clickcount = new HashMap<String, Long[]>();
		titleid_to_clickcount = new HashMap<String, Long[]>();
		descid_to_clickcount = new HashMap<String, Long[]>();	
	}
	
	private void addToMap(AttributeType type, String id, Long clicked, Long impressed)
	{
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
	}
	
	private void addInstanceToMap(Instance inst)
	{
		totalNeg += (inst.getImpressedTimes() - inst.getClickedTimes());
		totalPos += inst.getClickedTimes();
		for(int i = 2; i < inst.attributeValues.size(); i++)
			addToMap(AttributeType.values()[i], inst.attributeValues.get(i), inst.getClickedTimes(), inst.getImpressedTimes());
	}
	
	public void train(List<Instance> allInstances)
	{
		for(Instance inst : allInstances)
			addInstanceToMap(inst);
	}

	public void print()
	{
		System.out.println(totalPos);
		for(String id : user_to_clickcount.keySet())
		{	
			double prob = (1.0 * user_to_clickcount.get(id)[0]) / totalPos;
			System.out.println(id + ": " + prob);
		}
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
		case DESCRIPTION_ID:
			return descid_to_clickcount;
		case DISPLAY_URL:
			return displayurl_to_clickcount;
		case KEYWORD_ID:
			return keywordid_to_clickcount;
		case POSITION:
			return position_to_clickcount;
		case QUERY_ID:
			return queryid_to_clickcount;
		case TITLE_ID:
			return titleid_to_clickcount;
		case USER_ID:
			return user_to_clickcount;
		default:
			return null;
		}
	}
	
	private double getPosProbability()
	{
		return 1.0 * totalPos / (totalPos + totalNeg);
	}
	
	private double getNegProbability()
	{
		return 1.0 * totalNeg / (totalPos + totalNeg);
	}
	
	private double getClickedProbability(int attrIndex, String attrValue)
	{
		try
		{
			Long numClicked = getMap(AttributeType.values()[attrIndex]).get(attrValue)[0];
			Long numImpressed = getMap(AttributeType.values()[attrIndex]).get(attrValue)[1];
			
			return 1.0 * numClicked / (numClicked + numImpressed);
		}
		catch (NullPointerException e)
		{
			e.printStackTrace();
		}
		
		return 0.0;
	}
	
	private double getNumClicked(int attrIndex, String attrValue)
	{
		try
		{
			Long numClicked = getMap(AttributeType.values()[attrIndex]).get(attrValue)[0];
			
			return 1.0 * numClicked;
		}
		catch (NullPointerException e)
		{
			//e.printStackTrace();
		}
		
		return 0.0;
	}
	
	private double getNumNotClicked(int attrIndex, String attrValue)
	{
		try
		{
			Long numClicked = getMap(AttributeType.values()[attrIndex]).get(attrValue)[0];
			Long numImpressed = getMap(AttributeType.values()[attrIndex]).get(attrValue)[1];
			
			return 1.0 * (numImpressed - numClicked);
		}
		catch (NullPointerException e)
		{
			//e.printStackTrace();
		}
		
		return 0.0;
	}
	
	private double getImpressedProbability(int attrIndex, String attrValue)
	{
		try
		{
			Long numClicked = getMap(AttributeType.values()[attrIndex]).get(attrValue)[0];
			Long numImpressed = getMap(AttributeType.values()[attrIndex]).get(attrValue)[1];
			
			return 1.0 * numImpressed / (numClicked + numImpressed);
		}
		catch (NullPointerException e)
		{
			e.printStackTrace();
		}
		
		return 0.0;
	}
	
	public double predictCTR(Instance inst, List<Integer> features)
	{
		double numerator = getPosProbability();
		for(int i : features)
			numerator *= (1.0 * (getNumClicked(i, inst.attributeValues.get(i)) + 1) / (totalPos + getMap(AttributeType.values()[i]).size())); 
		double denominator = getNegProbability();
		for(int i : features)
			denominator *= (1.0 * (getNumNotClicked(i, inst.attributeValues.get(i)) + 1) / (totalNeg + getMap(AttributeType.values()[i]).size()));
		denominator += numerator;
		
		return 1.0 * numerator / denominator;
	}
}
