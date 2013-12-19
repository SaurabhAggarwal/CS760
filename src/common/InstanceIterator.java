package common;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Iterator;


public class InstanceIterator implements Iterator<Instance>, Iterable<Instance>
{
	private BufferedReader in;
	
	public InstanceIterator(String fileName)
	{
		try
		{
			in = new BufferedReader(new FileReader(fileName));
		}
		catch (Exception e) {
			e.printStackTrace();
			System.exit(-1);
		}
	}
	
	@Override
	public boolean hasNext()
	{
		try
		{
			return in.ready();
		}
		catch (IOException e)
		{
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public Instance next()
	{
		Instance toReturn = null;
		
		try
		{
			String line;
			line = in.readLine();
			line = line.trim();
			if((line == null) || line.equals(""))
				return null;
			toReturn = new Instance();
			String[] values = line.split("\t");
			for(String value : values)
				toReturn.addAttribute(value);
		}
		catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return toReturn;
	}

	@Override
	public void remove()
	{
	}
	

	@Override
	public Iterator<Instance> iterator()
	{
		return this;
	}
}