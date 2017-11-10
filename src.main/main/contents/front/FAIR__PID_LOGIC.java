package contents.front;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

public enum FAIR__PID_LOGIC 
{	
	Dummy ( new Pair(1000, contents.front.Dummy.class) ),
	User_CheckExist ( new Pair(1001, contents.front.user.CheckExistUser.class) ),
	User_SignIn ( new Pair(1002, contents.front.user.SignIn.class) ),
	User_LogIn ( new Pair(1003, contents.front.user.LogIn.class) ),
	
	
	NULL( new Pair(-1, Class.class) );

	
	
	private static Map<Integer, FAIR__PID_LOGIC> lookup = null; 
	static 
	{
		lookup = new HashMap<Integer, FAIR__PID_LOGIC>();
		for( FAIR__PID_LOGIC e : EnumSet.allOf(FAIR__PID_LOGIC.class) )
		    lookup.put( e.getPID(), e );
	}
	
	private Pair value;
	
	private FAIR__PID_LOGIC( Pair value ) 
	{
		this.value = value;
	}
	
	public Integer getPID() 
	{
		return value.getPID();
	}
	
	public String getFrontLogicName() 
	{
		return value.getLogicName();
	}
	
	public static FAIR__PID_LOGIC toEnum( Integer key )
	{
		return lookup.getOrDefault(key, NULL);
	}
}


class Pair 
{
	private Integer pid;
	private String logicname;
	
	public Pair( Integer pid, Class<?> logic )
	{
		this.pid = pid;
		this.logicname = logic.getName();
	}
	
	public Integer getPID() { return pid; }
	public String getLogicName() { return logicname; }
};
