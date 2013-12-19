package entities;

public enum Gender
{
	UNKNOWN(0),
	MALE(1),
	FEMALE(2);
	
	public int index;
	
	private Gender(int index)
	{
		this.index = index;
	}
	
	public static Gender getGender(String index)
	{
		if(index.equals("1"))
			return Gender.MALE;
		if(index.equals("2"))
			return Gender.FEMALE;
		return Gender.UNKNOWN;
	}
}
