package common;

public enum AttributeType
{
	CLICK(0),			//imp
	IMPRESSION(1),		//imp
	DISPLAY_URL(2),
	AD_ID(3),			//imp
	ADVERTISER_ID(4),
	DEPTH(5),			//imp
	POSITION(6),		//imp
	QUERY_ID(7),		//imp
	KEYWORD_ID(8),
	TITLE_ID(9),
	DESCRIPTION_ID(10),
	USER_ID(11);		//imp
	
	public int index;
	
	private AttributeType(int index)
	{
		this.index = index;
	}
}
