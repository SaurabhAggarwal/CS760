package entities;
import java.util.List;


public class Title
{
	public String id;
	public List<String> keywords;
	
	public Title(String id, List<String> keywords)
	{
		this.id = id;
		this.keywords = keywords;
	}
	
	public static double similarity(Title t1, Title t2)
	{
		if(t1.id.equals(t2.id))
			return 1.0;
		int numerator = 0;
		int denominator = Math.max(t1.keywords.size(), t2.keywords.size());
		for(String kw1 : t1.keywords)
			for(String kw2 : t2.keywords)
				if(kw1.equals(kw2))
					numerator++;
		
		return 1.0 * numerator / denominator;
	}
}
