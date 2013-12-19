package common;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import constants.FileLocations;

import entities.Description;
import entities.Keyword;
import entities.Query;
import entities.Title;
import entities.User;


public class DataSet
{
	public List<Instance> allInstances;
	public List<Instance> testInstances;
	public Map<String, Description> descKeyWordsMap;
	public Map<String, Keyword> adKeywordKeyWordsMap;
	public Map<String, Query> queryKeyWordsMap;
	public Map<String, Title> titleKeyWordsMap;
	public Map<String, User> usersMap;
	
	public DataSet()
	{
		allInstances = new ArrayList<Instance>();
		testInstances = new ArrayList<Instance>();
		descKeyWordsMap = new HashMap<String, Description>();
		adKeywordKeyWordsMap = new HashMap<String, Keyword>();
		queryKeyWordsMap = new HashMap<String, Query>();
		titleKeyWordsMap = new HashMap<String, Title>();
		usersMap = new HashMap<String, User>();
	}
	
	public void loadAllDefaultFiles()
	{
		List<String> trainingFiles = new ArrayList<String>();
		trainingFiles.add(FileLocations.TRAIN_FILE1);
		trainingFiles.add(FileLocations.TRAIN_FILE2);
		trainingFiles.add(FileLocations.TRAIN_FILE3);
		trainingFiles.add(FileLocations.TRAIN_FILE4);
		trainingFiles.add(FileLocations.TRAIN_FILE5);
		trainingFiles.add(FileLocations.TRAIN_FILE6);
		trainingFiles.add(FileLocations.TRAIN_FILE7);
		trainingFiles.add(FileLocations.TRAIN_FILE8);
		trainingFiles.add(FileLocations.TRAIN_FILE9);
		loadTrainInstances(trainingFiles);
		
		List<String> testingFiles = new ArrayList<String>();
		testingFiles.add(FileLocations.TRAIN_FILE10);
		testingFiles.add(FileLocations.TRAIN_FILE7);
		testingFiles.add(FileLocations.TRAIN_FILE8);
		testingFiles.add(FileLocations.TRAIN_FILE9);
		loadTestInstances(testingFiles);
		
		loadDescriptionMap(FileLocations.DESC_FILE);
		loadAdKeywordMap(FileLocations.KEYWORD_FILE);
		loadQueryMap(FileLocations.QUERY_FILE);
		loadTitleMap(FileLocations.TITLE_FILE);
		loadUserMap(FileLocations.USER_FILE);
	}
	
	public void loadTrainInstances(List<String> fileNames)
	{
		allInstances.clear();
		try {
			for(String fileName : fileNames)
			{
				BufferedReader in = new BufferedReader(new FileReader(fileName));
				while(in.ready())
				{
					String line = in.readLine();
					line = line.trim();
					if(line.equals(""))
						continue;
					Instance inst = new Instance();
					String[] values = line.split("\t");
					for(String value : values)
						inst.addAttribute(value);
					allInstances.add(inst);
				}
				in.close();
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void loadTestInstances(List<String> fileNames)
	{
		testInstances.clear();
		try {
			for(String fileName : fileNames)
			{
				BufferedReader in = new BufferedReader(new FileReader(fileName));
				while(in.ready())
				{
					String line = in.readLine();
					line = line.trim();
					if(line.equals(""))
						continue;
					Instance inst = new Instance();
					String[] values = line.split("\t");
					for(String value : values)
						inst.addAttribute(value);
					testInstances.add(inst);
				}
				in.close();
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void loadDescriptionMap(String fileName)
	{
		descKeyWordsMap.clear();
		try {
			BufferedReader in = new BufferedReader(new FileReader(fileName));
			while(in.ready())
			{
				String line = in.readLine();
				line = line.trim();
				String[] desc = line.split("\t");
				String key = desc[0];
				List<String> value = new ArrayList<String>();
				String[] keywords = desc[1].split("|");
				for(String keyword : keywords)
					value.add(keyword);
				descKeyWordsMap.put(key, new Description(key, value));
			}
			in.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void loadAdKeywordMap(String fileName)
	{
		adKeywordKeyWordsMap.clear();
		try {
			BufferedReader in = new BufferedReader(new FileReader(fileName));
			while(in.ready())
			{
				String line = in.readLine();
				line = line.trim();
				String[] desc = line.split("\t");
				String key = desc[0];
				List<String> value = new ArrayList<String>();
				String[] keywords = desc[1].split("|");
				for(String keyword : keywords)
					value.add(keyword);
				adKeywordKeyWordsMap.put(key, new Keyword(key, value));
			}
			in.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void loadQueryMap(String fileName)
	{
		queryKeyWordsMap.clear();
		try {
			BufferedReader in = new BufferedReader(new FileReader(fileName));
			while(in.ready())
			{
				String line = in.readLine();
				line = line.trim();
				String[] desc = line.split("\t");
				String key = desc[0];
				List<String> value = new ArrayList<String>();
				String[] keywords = desc[1].split("|");
				for(String keyword : keywords)
					value.add(keyword);
				queryKeyWordsMap.put(key, new Query(key, value));
			}
			in.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void loadTitleMap(String fileName)
	{
		titleKeyWordsMap.clear();
		try {
			BufferedReader in = new BufferedReader(new FileReader(fileName));
			while(in.ready())
			{
				String line = in.readLine();
				line = line.trim();
				String[] desc = line.split("\t");
				String key = desc[0];
				List<String> value = new ArrayList<String>();
				String[] keywords = desc[1].split("|");
				for(String keyword : keywords)
					value.add(keyword);
				titleKeyWordsMap.put(key, new Title(key, value));
			}
			in.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void loadUserMap(String fileName)
	{
		usersMap.clear();
		try {
			BufferedReader in = new BufferedReader(new FileReader(fileName));
			while(in.ready())
			{
				String line = in.readLine();
				line = line.trim();
				String[] desc = line.split("\t");
				usersMap.put(desc[0], new User(desc[0], desc[1], desc[2]));
			}
			in.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public boolean contains(AttributeType attr, String value)
	{
		for(Instance inst : allInstances)
			if(inst.attributeValues.get(attr.index).equals(value))
				return true;
		
		return false;
	}
}
