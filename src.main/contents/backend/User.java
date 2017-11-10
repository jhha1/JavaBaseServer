package contents.backend;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;
import net.protocol.EResultCode;
import utils.StringHelper;

public class User {
	final private static Logger log = Logger.getLogger( User.class );
	
	private String userId = null;
	private Integer platformType = -1;
	private String nickname = null;
	private long signin_dt = -1;
	private long login_dt = -1;
	private long logout_dt = -1;
	
	//public Long signin_dt = 0L;
	//private boolean bAdmin = false;
	
	public enum PLATFORM_TYPE { GUEST, GOOGLE, FACEBOOK }; 
	private static final int USERID_MAX_LEN = 20;
	
	
	public static boolean isNull( User user ){
		if( user == null )
			return true;
		
		if( user.userId == null 
				&& user.platformType < 0
				&& user.nickname == null)
			return true;
		
		return false;
	}
	
    public Integer getPlatformType() {
        return this.platformType;
    }
	
    public void setPlatformType(Integer platformType) {
		this.platformType = platformType;
	}
       
    public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public long getSignin_dt() {
		return signin_dt;
	}

	public void setSignin_dt(Long signin_dt) {
		this.signin_dt = signin_dt;
	}

	public long getLogout_dt() {
		return logout_dt;
	}

	public void setLogout_dt(long logout_dt) {
		this.logout_dt = logout_dt;
	}

	public long getLogin_dt() {
		return login_dt;
	}

	public void setLogin_dt(long login_dt) {
		this.login_dt = login_dt;
	}


	public static boolean checkPlatformType( Integer platformType ) {
    	if( platformType == null )
    		return false;
    	
    	if( platformType == PLATFORM_TYPE.GUEST.ordinal() || 
    			platformType == PLATFORM_TYPE.GOOGLE.ordinal() || 
    			platformType == PLATFORM_TYPE.FACEBOOK.ordinal() )
    		return true;
    	else
    		return false;
    }
    
    public static boolean checkUserID( String userID ) {
    	if( StringHelper.isNull(userID)){
             return false;
        }
    	return true;
   }

    
    public String toString() {
		return "platform("+platformType+")"
				+ ", userId("+userId+")"
				+ ", nickname("+nickname+")";
	}
}
