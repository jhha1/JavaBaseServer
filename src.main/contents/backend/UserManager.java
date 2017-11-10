package contents.backend;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;
import data.db.DBQueries;
import data.db.DBQueries.DBResult;
import net.protocol.EResultCode;
import utils.StringHelper;

public class UserManager 
{
	final private Logger log = Logger.getLogger( UserManager.class );
	
	private static UserManager instance = new UserManager();
	private UserManager() {}
	
	public static UserManager getInstance() {
		return instance;
	}
	
	public class LogInResultBundle {
		public EResultCode resultCode = EResultCode.UNKNOUN_ERR;
		public String userID = null;
	}
	
	public User getUserById( String userId ) 
	{
		if( StringHelper.isNull(userId) ) {
			log.error("Null or Empty UserName(" + userId +")");
			return null;
		}

		final DBQueries db = DBQueries.getInstance(); 
		DBResult dbResult = db.selectUserById( userId ); 
		if( dbResult == null || dbResult.noSelected() ){
			log.error("NoExisted UserID(" + userId +")");
			return null;
		}
		return (User)dbResult.getResultObject(); 
	}
	

	public boolean isExistUser( String userID ) 
	{
		if( false == User.checkUserID(userID) ) {
			log.error("Invalid UserID(" + userID +")");
			return false;
		}
		
		User user = getUserById( userID );
		boolean bNoSelected = User.isNull(user);
		if( bNoSelected ){
			log.error("There's No UserID in DB. userID(" + userID +")");
			return false;
		}

		return true;
	}
	
	private LogInResultBundle signIn( Integer platformType, String userId )
	{
		String userIdentifier = "[platformType("+platformType+"), userId(" + userId +")]  ";
		log.debug(userIdentifier + "SignIn Called.");
		
		boolean bGuest = platformType!= null && (platformType == User.PLATFORM_TYPE.GUEST.ordinal());
		if( bGuest )
			userId = createUserID();
		
		LogInResultBundle bundle = new LogInResultBundle();
		EResultCode resultCode = checkParametersForSignIn( platformType, userId );
		if( resultCode.isFail() ){
			log.error("Failed SignIn for Some Reason : " + resultCode.stringCode());
			bundle.resultCode = resultCode;
			return bundle;
		}
		
		final DBQueries db = DBQueries.getInstance(); 
		if( ! db.insertUserAccount( platformType, userId ) ){
			log.error(userIdentifier + "Failed SignIn. DB Insert Error.");
			bundle.resultCode = EResultCode.DB_ERR;
			return bundle;
		}
		
		bundle.resultCode = EResultCode.SUCCESS;
		bundle.userID = userId;
		return bundle;
	}
	
	
	private static final Pattern patternAlphaNumeric = Pattern.compile("^[a-zA-Z0-9]*$");
	private EResultCode checkParametersForSignIn( Integer platformType, String userId )
	{
		String userIdentifier = "[platformType("+platformType+"), userId(" + userId +")]";
	
		if( ! User.checkPlatformType(platformType) ){
			log.error(userIdentifier + "Failed SignIn. Invalied PlatformType: " + platformType);
			return EResultCode.INVALID_PLATFORM;
		}
		
		if( ! User.checkUserID(userId) ){
			log.error(userIdentifier + "Failed SignIn. Invalied UserID: " + userId);
			return EResultCode.INVALID_USERID;
		}

		boolean bDuplicated = isExistUser( userId );
		if( bDuplicated ) {
			log.error("Failed SignIn. USERNAME_DUPLICATED(" + userId +")");
			return EResultCode.USERNAME_DUPLICATED;
		}
		
		Matcher m = patternAlphaNumeric.matcher(userId);
        if(false == m.matches()) {
        	log.error("Invalid userId. Only alphanumeric allowed. name("+ userId +")");
        	return EResultCode.INVALID_USERID;
        }
		
		return EResultCode.SUCCESS;
	}
	

	/*
	 * contents rule 적인 실패를 return값으로 대체 할 수 있으므로, 
	 * ContentsRuleException 사용안함.  
	 */
	public LogInResultBundle logIn( Integer platformType, String userId ) 
	{
		String userIdentifier =  "[platformType("+platformType+"), userId(" + userId +")]";
		log.debug(userIdentifier + "Login Called. ");
		
		LogInResultBundle bundle = new LogInResultBundle();
		
		// check SignIn
		boolean bNeedSignIn = ! isExistUser(userId);
		if( bNeedSignIn ){
			// SignIn
			bundle = signIn( platformType, userId );
			if( bundle.resultCode.isFail() )
				return bundle;	
			else
				userId = bundle.userID;  // guest일 경우 signIn()에서 새로만든 id
		} 
		
		// check Login
		EResultCode resultCode = checkParametersForLogin(platformType, userId);
		if( resultCode.isSuccess() ) 
		{
			// Login --> update loginDateTime 
			final DBQueries db = DBQueries.getInstance(); 
			if( ! db.updateLoginDateTime( platformType, userId ) ){
				log.error(userIdentifier + "Failed LogIn. DB Insert Error.");
				bundle.resultCode = EResultCode.DB_ERR;
				return bundle;
			}
			
			// login success.
			bundle.resultCode = EResultCode.SUCCESS;
			return bundle;
		}
		else
		{
			log.error(userIdentifier + "Failed Login for Some Reason : " + resultCode);
			bundle.resultCode = resultCode;
			return bundle;
		}
	}
	
	 /*
     *  check 
     *  platformType, user id
     */
    public EResultCode checkParametersForLogin( Integer platformType, String userId ) {
    	if( false == User.checkPlatformType(platformType) ) {
    		return EResultCode.INVALID_PLATFORM;
    	}
    	
    	if( false == User.checkUserID(userId) ) {
    		return EResultCode.INVALID_USERID;
    	}
       
        return EResultCode.SUCCESS;
    }

    
    /*
     * GUEST + YYMMDDHHMMSS + 00000
     * ex> "GUEST17110217255200010"
     */
    private static java.text.SimpleDateFormat dateTimeFormatter = new java.text.SimpleDateFormat("yyMMddHHmmss");
    private String createUserID()
    {
    	String lastID = null;
    	String newID = null;
    	
    	final DBQueries db = DBQueries.getInstance(); 
		DBResult dbResult = db.selectTodayLastGuestUserId(); 
		if( dbResult == null ) 
		{
			log.error("Can't Get a Today's last guest userId. DB Error.");
			newID = null;
		} 
		else if( dbResult.noSelected() )
		{
			newID = "GUEST" + dateTimeFormatter.format(new java.util.Date()) + "00000";
		} 
		else 
		{
			lastID = (String)dbResult.getResultObject(); 
			String countStr = lastID.substring(17, 22);
	    	if( StringHelper.isNull(countStr) ){
	    		log.error("Failed SubString. substring userId is null. Selected Last UserID: " + lastID);
	    		return null;
	    	}
	    	
	    	Integer count = Integer.valueOf(countStr);
	    	if( count == null || count < 0 ){
	    		log.error("Failed Convert UserID (String to Int). Selected Last UserID: " + lastID);
	    		return null;
	    	}
	    	
	    	if( count >= 99999 ){
	    		log.error("Overflow UserID!!!!! Selected Last UserID: " + lastID);
	    		return null;
	    	}
	    	
	    	count += 1;
	    
	    	countStr = String.valueOf(count);
	    	if( countStr.length() == 1 ) countStr = "0000" + countStr;
	    	else if( countStr.length() == 2 ) countStr = "000" + countStr;
	    	else if( countStr.length() == 3 ) countStr = "00" + countStr;
	    	else if( countStr.length() == 4 ) countStr = "0" + countStr;
	    	
	    	newID = lastID.substring(0, 17) + countStr;
		}
		return newID;
    }
    
    
    public EResultCode logOut( String userId ) 
	{
		String userIdentifier =  "[userId(" + userId +")]";
		log.debug(userIdentifier + "Login Called. ");
		
		// check 
		if( ! isExistUser(userId) )
		{
			log.error(userIdentifier + "NoneExistedUser.");
			return EResultCode.INVALID_USERID;
		}
			
		// update logOutDateTime 
		final DBQueries db = DBQueries.getInstance(); 
		if( ! db.updateLogOutDateTime( userId ) ){
			log.error(userIdentifier + "Failed LogOut. DB Update Error.");
			return EResultCode.DB_ERR;
		}
		return EResultCode.SUCCESS;
	}
	
}
