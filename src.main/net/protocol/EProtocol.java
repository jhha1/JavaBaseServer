package net.protocol;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

public enum EProtocol {
	
	// Required 
	JSON ("JSON"),
	PID ("pid"),	// string
	iPID ("iPID"),  // integer 
	sPID ("sPID"),  // pid name
	DeviceID ("DeviceId"),
	
	// Response Only 
	CODE("code"),
	MSG("msg"),
	
	
	// user
	UserID ("UserID"),
	PlatformType ("PlatformType"),
	Nickname("Nickname"),
	
	
	// for test
	TEST("test"),
	TEST_TIME("time"),
	TEST_FLOAT("-float"),
	TEST_INTMAX("intmax"),
	TEST_LIST("list"),
	TEST_COMPLEXMAP("complexMap"),
	
	NULL("null");
	
	private static Map<String, EProtocol> lookup = null; 
	static 
	{
		lookup = new HashMap<String, EProtocol>();
		for( EProtocol e : EnumSet.allOf(EProtocol.class) ) 
		{
			String upperKey = e.string().trim().toUpperCase();
		    lookup.put( upperKey, e );
		}
	}
	
	private String value;
	
	private EProtocol( String value ) 
	{
		this.value = value;
	}
	
	public String string() 
	{
		return value;
	}
	
	public static EProtocol toEnum( String key )
	{
		if( key == null || key.isEmpty() )
			return EProtocol.NULL;
		
		String upperKey = key.trim().toUpperCase();
		return lookup.getOrDefault(upperKey, NULL);
	}
}
