package feature_engineering;

public enum EngineeredAttributeType
{
	CLICK(0),					//imp
	IMPRESSION(1),				//imp
	
	DISPLAY_URL(2),
	AD_ID(3),					//imp
	ADVERTISER_ID(4),
	DEPTH(5),					//imp
	POSITION(6),				//imp
	
	KEYWORDS(7),
	
	USER_ID(8),					//imp
	GENDER(9),
	AGE(10),
	
	QUERY_AD_SIMILARITY(11);
	
	public int index;
	
	private EngineeredAttributeType(int index)
	{
		this.index = index;
	}
}
