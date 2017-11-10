package contents.front.user;

import org.apache.log4j.Logger;
import contents.backend.UserManager;
import contents.backend.UserManager.LogInResultBundle;
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

		Integer platformType = (Integer) p.request.get(EProtocol.PlatformType);
		String userId = (String) p.request.get(EProtocol.UserID);

		
		log.debug( "Login process start. ");
		final UserManager user = UserManager.getInstance();
		LogInResultBundle bundle = user.logIn( platformType, userId );
		if( bundle.resultCode.isFail() )
		{
			p.setFail( bundle.resultCode );
		}
		else
		{
			p.response.set(EProtocol.UserID, bundle.userID);
			p.setSuccess();
		}
	}
}
