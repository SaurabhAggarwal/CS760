package bias;

public class Bias
{
	public double value;
	public double sumSquareError;
	public double numInstances;
	
	public Bias()
	{
		value = Math.random();
		sumSquareError = 0.0;
		numInstances = 0.0;
	}
}
