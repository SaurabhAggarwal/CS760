package entities;
import java.util.List;


public class Query
{
	public String id;
	public List<String> keywords;
	
	public Query(String id, List<String> keywords)
	{
		this.id = id;
		this.keywords = keywords;
	}
	
	public static double similarity(Query q1, Query q2)
	{
		if(q1.id.equals(q2.id))
			return 1.0;
		int numerator = 0;
		int denominator = Math.max(q1.keywords.size(), q2.keywords.size());
		for(String kw1 : q1.keywords)
			for(String kw2 : q2.keywords)
				if(kw1.equals(kw2))
					numerator++;
		
		return 1.0 * numerator / denominator;
	}
}
