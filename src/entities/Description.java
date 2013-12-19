package entities;
import java.util.List;


public class Description
{
	public String id;
	public List<String> keywords;
	
	public Description(String id, List<String> keywords)
	{
		this.id = id;
		this.keywords = keywords;
	}
	
	public static double similarity(Description d1, Description d2)
	{
		if(d1.id.equals(d2.id))
			return 1.0;
		int numerator = 0;
		int denominator = Math.max(d1.keywords.size(), d2.keywords.size());
		for(String kw1 : d1.keywords)
			for(String kw2 : d2.keywords)
				if(kw1.equals(kw2))
					numerator++;
		
		return 1.0 * numerator / denominator;
	}
}
