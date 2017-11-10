package contents.front.user;

import org.apache.log4j.Logger;
import contents.backend.UserManager;
import contents.front.FrontLogic;
import net.protocol.EProtocol;
import net.protocol.EResultCode;
import net.protocol.Protocol;
import net.protocol.ProtocolParamChecker;


public class LogIn implements FrontLogic {

	final private Logger log = Logger.getLogger( LogIn.class );
	
	@Override
	public void action(Protocol p)
	{
		log.debug( this.getClass().getName() + " FrontLogic Called. "
					+ p.toString());

		Integer userId = ProtocolParamChecker.checkUserID(p.request.get(EProtocol.UserID));
		
		log.debug( "1. Id Checked. ");
		
		/*
		 * login
		 */
		log.debug( "2. Login process start. ");
		final UserManager user = UserManager.getInstance();
		Integer loginResult = user.logIn( userId );
		if( loginResult == 0 ) {
			p.setFail(EResultCode.INVALID_USERID, "userID:"+userId);
			return;
		} else if ( loginResult == -1 ) {
			p.setFail(EResultCode.SYSTEM_ERR);
			return;
		}
		
		log.debug( "3. isAdminUser Start ");
		boolean bAdminUser = user.isAdminUser(userId);
		
		
		log.debug( "4. sync Start ");
		
				
		
		p.response.set(EProtocol.UserID, userId);
		p.response.set(EProtocol.IsAdmin, bAdminUser);

		p.setSuccess();
	}
}
