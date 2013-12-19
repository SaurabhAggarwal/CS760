package entities;

public class User
{
	public String id;
	public Gender gender;
	public Integer age;
	
	public User(String id, String gender, String age)
	{
		this.id = id;
		this.gender = Gender.getGender(gender);
		this.age = Integer.parseInt(age);
	}
	
	public static double similarity(User u1, User u2)
	{
		if(u1.id.equals(u2.id))
			return 1.0;
		
		int numerator = 0;
		if(u1.gender == u2.gender)
			numerator++;
		if(u1.age == u2.age)
			numerator++;
		
		int denominator = 2;
		
		return 1.0 * numerator / denominator;
	}
}
