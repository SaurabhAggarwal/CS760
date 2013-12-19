package common;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import weighted_majority.WeightedMajority;

import feature_engineering.RiskFeatures;

import knn.KNN;
import bayes.NaiveBayes;
import bias.BiasModel;


public class Main
{
	private static boolean DEBUG = true;
	
	public static void print(String line)
	{
		if(DEBUG)
			System.out.print(line);
	}
	
	public static void println(String line)
	{
		print(line + "\n");
	}
	
	public static void main(String[] args) throws IOException
	{
		long start = System.currentTimeMillis();
		DataSet ds = new DataSet();
		ds.loadAllDefaultFiles();
		List<Integer> features = new ArrayList<Integer>();
		for(int i = 2; i < AttributeType.values().length; i++)
			features.add(i);
		System.out.println(doWeightedMajority(ds, features));
		
		//System.out.println(doKNN(ds, ds.testInstances, features, 2));
		//System.out.println(doNaiveBayes(ds.allInstances, ds.testInstances, features));
		//System.out.println(doRiskFeaturePrediction(ds));
		//evaluateModel(ds, ds.allInstances);
		
		//createTestFile(ds, "C:\\data\\train\\train.dat", FileLocations.TEST_FILE);
		//createOtherFiles(FileLocations.TRAIN_FILE, AttributeTrain.DESCRIPTION_ID, "C:\\data\\train\\descriptionid_tokensid.txt", FileLocations.DESC_FILE);
		//sampleFile(inputFile, 1.0);
		//sampleFileAttrValue(inputFile, Attribute.CLICK, 1);
		
		long end = System.currentTimeMillis();
		System.out.println(((end - start) / 1000) + " seconds taken.");
	}
	
	private static double doWeightedMajority(DataSet ds, List<Integer> features)
	{
		WeightedMajority wm = new WeightedMajority();
		
		RiskFeatures rf = new RiskFeatures();
		rf.train(ds);
		
		BiasModel bm = new BiasModel();
		bm.train(ds.allInstances);
		
		NaiveBayes nb = new NaiveBayes();
		nb.train(ds.allInstances);

		wm.addPredictor(rf);
		wm.addPredictor(bm);
		wm.addPredictor(nb);
		
		wm.learnWeights(ds.testInstances, features);
		
		int countRight = 0;
		int countWrong = 0;
		
		for(Instance inst : ds.testInstances)
		{
			double a = inst.getClickProbability();
			double b = wm.predictCTR(inst, features);
			if(isCorrect(a, b))
				countRight++;
			else
				countWrong++;
		}
		
		return 1.0 * countRight / (countRight + countWrong);
	}
	
	private static void evaluateModel(DataSet ds, List<Instance> test)
	{
		List<Integer> features = new ArrayList<Integer>();
		double best_accuracy = 0.0;
		for(int i = 10; i < AttributeType.values().length; i++)
		{
			features.add(i);
			double accuracy = doNaiveBayes(ds.allInstances, test, features);
			System.out.println(features.toString() + ": " + accuracy);
			if(accuracy > best_accuracy)
				best_accuracy = accuracy;
			else
				features.remove(features.size() - 1);
		}
	}
	
	private static double doRiskFeaturePrediction(DataSet ds, List<Integer> features)
	{
		RiskFeatures rf = new RiskFeatures();
		rf.train(ds);
		int countRight = 0;
		int countWrong = 0;
		
		for(Instance inst : ds.testInstances)
		{
			double a = inst.getClickProbability();
			double b = rf.predictCTR(inst, features);
			if(isCorrect(a, b))
				countRight++;
			else
				countWrong++;
		}
		
		return 1.0 * countRight / (countRight + countWrong); 
	}
	
	private static double doBiasModel(List<Instance> train, List<Instance> test, List<Integer> features, int epoch)
	{
		BiasModel bm = new BiasModel();
		bm.train(train);
		int countRight = 0;
		int countWrong = 0;
		for(int i = 0; i < epoch; i++)
		{
			countRight = 0;
			countWrong = 0;
			bm.learnBiases(train, features);
			for(Instance inst : test)
			{
				double a = inst.getClickProbability();
				double b = bm.predictCTR(inst, features);
				if(isCorrect(a, b))
					countRight++;
				else
					countWrong++;
			}
		}
		
		return 1.0 * countRight / (countRight + countWrong); 
	}
	
	private static double doNaiveBayes(List<Instance> train, List<Instance> test, List<Integer> features)
	{
		NaiveBayes nb = new NaiveBayes();
		nb.train(train);
		int countRight = 0;
		int countWrong = 0;
		
		for(Instance inst : test)
		{
			double a = inst.getClickProbability();
			double b = nb.predictCTR(inst, features);
			if(isCorrect(a, b))
				countRight++;
			else
				countWrong++;
		}
		
		return 1.0 * countRight / (countRight + countWrong);
	}
	
	private static double doKNN(DataSet ds, List<Instance> test, List<Integer> features, int K)
	{
		KNN knn = new KNN(ds, K);
		int countRight = 0;
		int countWrong = 0;
		for(Instance inst : test)
		{
			double a = inst.getClickProbability();
			double b = knn.predictCTR(inst, features);
			if(isCorrect(a, b))
				countRight++;
			else
				countWrong++;
		}
		return 1.0 * countRight / (countRight + countWrong);
	}
	
	public static boolean isCorrect(double predicted, double actual)
	{
		if(predicted >= 0.5)
			if(actual >= 0.5)
				return true;
		if(predicted < 0.5)
			if(actual < 0.5)
				return true;
		return false;
		
	}
	
	private static void createTestFile(DataSet ds, String inputTestFile, String outFile)
	{
		InstanceIterator a = new InstanceIterator(inputTestFile);
		BufferedWriter bw = null;
		try
		{
			bw = new BufferedWriter(new FileWriter(outFile));
			for(Instance inst : a)
			{
				if(ds.usersMap.keySet().contains(inst.attributeValues.get(AttributeType.USER_ID.index)))
				{
					if(ds.adKeywordKeyWordsMap.keySet().contains(inst.attributeValues.get(AttributeType.KEYWORD_ID.index)))
					{
						if(ds.descKeyWordsMap.keySet().contains(inst.attributeValues.get(AttributeType.DESCRIPTION_ID.index)))
						{
							if(ds.queryKeyWordsMap.keySet().contains(inst.attributeValues.get(AttributeType.QUERY_ID.index)))
							{
								if(ds.titleKeyWordsMap.keySet().contains(inst.attributeValues.get(AttributeType.TITLE_ID.index)))
								{
									for(int i = 0; i < inst.attributeValues.size(); i++)
									{
										bw.write(inst.attributeValues.get(i));
										if(i != (inst.attributeValues.size() - 1))
											bw.write("\t");
									}
									bw.write("\n");
								}
							}
						}
					}
					
				}
			}
			bw.close();
		} catch (IOException e)
		{
			e.printStackTrace();
		}
	}
	
	private static void createOtherFiles(String inputTrainFile, AttributeType filterAttr, String inputHashFile, String outFile)
	{
		InstanceIterator a = new InstanceIterator(inputTrainFile);
		Set<String> allIds = new HashSet<String>();
		for(Instance inst : a)
			allIds.add(inst.attributeValues.get(filterAttr.index));
		
		try {
			BufferedReader in = new BufferedReader(new FileReader(inputHashFile));
			BufferedWriter bw = new BufferedWriter(new FileWriter(outFile));
			while(in.ready())
			{
				String line = in.readLine();
				String[] split = line.split("\t");
				if(allIds.contains(split[0]))
					bw.write(line + "\n");
			}
			in.close();
			bw.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void sampleFileAttrValue(String inputFile, AttributeType attr, int minValue)
	{
		InstanceIterator a = new InstanceIterator(inputFile);
		
		for(Instance inst : a)
		{
			if(Integer.parseInt(inst.attributeValues.get(attr.index)) >= minValue)
				inst.print();
		}
	}
	
	public static void sampleFile(String inputFile, double sampleRate)
	{
		try {
			BufferedReader in = new BufferedReader(new FileReader(inputFile));
			while(in.ready())
			{
				double rand = Math.random();
				if(rand <= sampleRate)
					System.out.println(in.readLine());
				else
					in.readLine();
			}
			in.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
