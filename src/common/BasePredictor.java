package common;

import java.util.List;

public interface BasePredictor
{
	public double predictCTR(Instance inst, List<Integer> features);
}
