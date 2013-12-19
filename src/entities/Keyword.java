package entities;
import java.util.List;


public class Keyword
{
	public String id;
	public List<String> keywords;
	
	public Keyword(String id, List<String> keywords)
	{
		this.id = id;
		this.keywords = keywords;
	}
	
	public static double similarity(Keyword k1, Keyword k2)
	{
		if(k1.id.equals(k2.id))
			return 1.0;
		int numerator = 0;
		int denominator = Math.max(k1.keywords.size(), k2.keywords.size());
		for(String kw1 : k1.keywords)
			for(String kw2 : k2.keywords)
				if(kw1.equals(kw2))
					numerator++;
		
		return 1.0 * numerator / denominator;
	}
}
