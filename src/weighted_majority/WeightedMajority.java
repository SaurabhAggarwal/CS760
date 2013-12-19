package weighted_majority;

import java.util.ArrayList;
import java.util.List;

import common.BasePredictor;
import common.Instance;
import common.Main;

public class WeightedMajority implements BasePredictor
{
	private List<BasePredictor> predictors = null;
	public List<Double> weights = null;
	
	private final double LEARNING_RATE = 0.0001;
	
	public WeightedMajority()
	{
		this.predictors = new ArrayList<BasePredictor>();
		this.weights = new ArrayList<Double>();
	}
	
	public void addPredictor(BasePredictor predictor)
	{
		this.predictors.add(predictor);
	}
	
	public void learnWeights(List<Instance> tuningSet, List<Integer> features)
	{
		for(BasePredictor p : predictors)
			weights.add(1.0 / predictors.size());
		
		for(Instance inst : tuningSet)
		{
			List<Double> probabilities = new ArrayList<Double>();
			double w_sum = 0.0;
			for(Double w : weights)
				w_sum += w;
			for(Double w: weights)
				probabilities.add(w / w_sum);
			int index = 0;
			List<Double> new_weights = new ArrayList<Double>();
			for(BasePredictor p : predictors)
			{
				double predicted = p.predictCTR(inst, features);
				if(Main.isCorrect(inst.getClickProbability(), predicted))
					new_weights.add(weights.get(index));
				else
					new_weights.add(weights.get(index) * (1.0 - LEARNING_RATE));
				index++;
			}
			weights = new_weights;
		}
		
		for(Double w : weights)
		{
			System.out.println("weight = " + w);
		}
		
	}

	@Override
	public double predictCTR(Instance inst, List<Integer> features)
	{
		double toReturn = 0.0;
		int i = 0;
		for(BasePredictor p : predictors)
		{
			toReturn += weights.get(i) * p.predictCTR(inst, features);
			i++;
		}
		
		return toReturn;
	}
}
