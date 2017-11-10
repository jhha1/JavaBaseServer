package contents.front.user;

import org.apache.log4j.Logger;
import contents.backend.UserManager;
import contents.backend.UserManager.LogInResultBundle;
import contents.front.FrontLogic;
import net.protocol.EProtocol;
import net.protocol.EResultCode;
import net.protocol.Protocol;
import net.protocol.ProtocolParamChecker;


public class LogOut implements FrontLogic {

	final private Logger log = Logger.getLogger( LogOut.class );
	
	@Override
	public void action(Protocol p)
	{
		log.debug( this.getClass().getName() + " FrontLogic Called. "
					+ p.toString());

		String userId = (String) p.request.get(EProtocol.UserID);
		
		log.debug( "LogOut process start. ");
		final UserManager user = UserManager.getInstance();
		EResultCode resultCode = user.logOut( userId );
		if( resultCode.isFail() )
		{
			p.setFail( resultCode );
		}
		else
		{
			p.setSuccess();
		}
	}
}
