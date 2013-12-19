package common;
import java.util.ArrayList;
import java.util.List;

import entities.Description;
import entities.Keyword;
import entities.Query;
import entities.Title;
import entities.User;

public class Instance {
	
	public List<String> attributeValues = null;
	
	public void addAttribute(String attrValue)
	{
		if (attributeValues == null)
		{
			attributeValues = new ArrayList<String>();
		}
		attributeValues.add(attrValue);
	}
	
	public double getClickProbability()
	{
		return 1.0 * Double.parseDouble(attributeValues.get(AttributeType.CLICK.index)) / Double.parseDouble(attributeValues.get(AttributeType.IMPRESSION.index)); 
	}
	
	public long getClickedTimes()
	{
		return Long.parseLong(attributeValues.get(AttributeType.CLICK.index)); 
	}
	
	public long getImpressedTimes()
	{
		return Long.parseLong(attributeValues.get(AttributeType.IMPRESSION.index)); 
	}
	
	public void print()
	{
		//Main.println("Click Probability: " + getClickProbability());
		for(int i = 0; i < attributeValues.size(); i++)
		{
			Main.print(attributeValues.get(i));
			if(i != (attributeValues.size() - 1))
				Main.print("\t");
		}
		Main.println("");
	}
}
