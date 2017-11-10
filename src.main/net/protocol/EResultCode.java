package net.protocol;

public enum EResultCode {
	
	SUCCESS ( 0000 ),
	
	// common
	INVALID_ARGUMENT ( 1001 ),
	NOEXSIT (1002),
	NULL_VALUE(1003),
	INVALID_DATATYPE(1004),
	NOT_AUTHORITY(1005),
	
	// pid
	INVALID_PID ( 1100 ),

	// user 
	INVALID_USERID ( 1300 ),
	USERID_DUPLICATED( 1101 ),
	NONEXIST_USER( 1102 ),
	
	INVALID_USERNAME( 1110 ),
	USERNAME_DUPLICATED( 1111 ),

	INVALID_PLATFORM( 1120 ),
	
	// system
	MAINTAIN_SERVER ( 9993 ),
	ENCODING_ERR(9994),
	SYSTEM_ERR ( 9995 ),
	NETWORK_ERR ( 9996 ),
	DB_ERR ( 9997 ),
	UNKNOUN_ERR ( 9998 ),
	MAX( 9999 );
	
	
	private Integer code;
	
	private EResultCode( int value ) 
	{
		this.code = value;
	}
	
	public Integer intCode() 
	{
		return code;
	}
	
	public String stringCode()
	{
		return code.toString();
	}
	
	public boolean isSuccess(){
		return code == SUCCESS.intCode();
	}
	
	public boolean isFail(){
		return !isSuccess();
	}
}
