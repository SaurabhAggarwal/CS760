package bias;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import common.AttributeType;
import common.Instance;

public class BiasModelEngineered
{
	private Map<String, Bias> user_to_clickcount = null;
	private Map<String, Bias> displayurl_to_clickcount = null;
	private Map<String, Bias> adid_to_clickcount = null;
	private Map<String, Bias> advertiserid_to_clickcount = null;
	private Map<String, Bias> depth_to_clickcount = null;
	private Map<String, Bias> position_to_clickcount = null;
	private Map<String, Bias> queryid_to_clickcount = null;
	private Map<String, Bias> keywordid_to_clickcount = null;
	private Map<String, Bias> titleid_to_clickcount = null;
	private Map<String, Bias> descid_to_clickcount = null;
	
	public final double LEARNING_RATE = 0.01;
	
	public BiasModelEngineered()
	{
		user_to_clickcount = new HashMap<String, Bias>();
		displayurl_to_clickcount = new HashMap<String, Bias>();
		adid_to_clickcount = new HashMap<String, Bias>();
		advertiserid_to_clickcount = new HashMap<String, Bias>();
		depth_to_clickcount = new HashMap<String, Bias>();
		position_to_clickcount = new HashMap<String, Bias>();
		queryid_to_clickcount = new HashMap<String, Bias>();
		keywordid_to_clickcount = new HashMap<String, Bias>();
		titleid_to_clickcount = new HashMap<String, Bias>();
		descid_to_clickcount = new HashMap<String, Bias>();	
	}
	
	private Map<String, Bias> getMap(AttributeType type)
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
	
	private void addToMap(AttributeType type, String id, Long clicked, Long impressed)
	{
		Map<String, Bias> toChange = getMap(type);
		
		Bias curr = toChange.get(id);
		if(curr == null)
			curr = new Bias();
		toChange.put(id, curr);
	}
	
	private void addInstanceToMap(Instance inst)
	{
		for(int i = 2; i < inst.attributeValues.size(); i++)
			addToMap(AttributeType.values()[i], inst.attributeValues.get(i), inst.getClickedTimes(), inst.getImpressedTimes());
	}
	
	public void train(List<Instance> allInstances)
	{
		for(Instance inst : allInstances)
			addInstanceToMap(inst);
	}
	
	public double predictCTR(Instance inst, List<Integer> features)
	{
		double prediction = 0.0;
		for(int i : features)
			prediction += getMap(AttributeType.values()[i]).get(inst.attributeValues.get(i)).value;
		
		return prediction;
	}
	
	public void learnBiases(List<Instance> allInstances, List<Integer> features)
	{
		for(Instance inst : allInstances)
		{
			double prediction = predictCTR(inst, features);
			double answer = inst.getClickProbability();
			double errorSq = Math.pow(prediction - answer, 2);
			
			for(int i = 2; i < inst.attributeValues.size(); i++)
			{
				Bias bias = getMap(AttributeType.values()[i]).get(inst.attributeValues.get(i));
				bias.sumSquareError += errorSq;
				bias.numInstances += 1.0;
				getMap(AttributeType.values()[i]).put(inst.attributeValues.get(i), bias);
			}
		}
		
		for(int i = 2; i < AttributeType.values().length; i++)
		{
			Map<String, Bias> map = getMap(AttributeType.values()[i]);
			for(String id : map.keySet())
			{
				Bias bias = map.get(id);
				if(bias.numInstances == 0.0)
					continue;
				bias.value -= (LEARNING_RATE * (bias.sumSquareError / bias.numInstances));
				bias.numInstances = 0.0;
				bias.sumSquareError = 0.0;
			}
		}
	}
}
