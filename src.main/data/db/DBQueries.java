package data.db;


import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.mysql.jdbc.Statement;


import contents.backend.User;
import exception.system.DBException;
import net.protocol.EProtocol;
import net.protocol.EResultCode;
import net.protocol.ObjectBundle;
import utils.StringHelper;

public class DBQueries {
	
	final private Logger log = Logger.getLogger( DBQueries.class );
	private static DBQueries instance = new DBQueries();
	private DBQueries(){}
	
	public static DBQueries getInstance() {
		return instance;
	}

	
	public class DBResult {
		boolean noSelected = false;
		Object resultObject = null;
		
		public boolean noSelected(){
			return noSelected;
		}
		public Object getResultObject(){
			return resultObject;
		}
	}
	
	/*
	 *  User
	 */
	public DBResult selectUserById( String userId ) 
	{	
		log.debug( "selectUserById Called . ");
		if( ! User.checkUserID(userId)) {
			log.error("invalid argment. userId("+userId+")");
			return null;
		}
		
		String query = "SELECT platformType, userId, nickname, "
							+ "unix_timestamp(signin_dt) as signin_long, "
							+ "unix_timestamp(login_dt) as login_long, "
							+ "unix_timestamp(logout_dt) as logout_long "
						+ " FROM user "
		 				+ " WHERE userId = ?";
 
		DBConnectionHelper db = null;
		try {
			db = new DBConnectionHelper( query );
		    db.setString(1, userId);
		    
		    ResultSet rs = db.executeQuery();
		    
		    User user = new User();
		    while(rs.next()){     
		    	user.setUserId(rs.getString("userId"));
		    	user.setPlatformType(rs.getInt("platformType"));
		    	user.setNickname(rs.getString("nickname"));
		    	user.setSignin_dt(rs.getLong("signin_long"));
		    	user.setLogin_dt(rs.getLong("login_long"));
		    	user.setLogout_dt(rs.getLong("logout_long"));
		    	
		    	System.out.println( user.toString() + "\t");
		    }
		    DBResult dbResult = new DBResult();
		    dbResult.noSelected = User.isNull(user);
		    dbResult.resultObject = user;
		    return dbResult;
		    
		} catch (Exception e) {
			log.error("query["+query+"], args[userId:"+userId+"]");
			e.printStackTrace();
		} finally {
			db.close();
		} 
		return null; 
	}
	
	
	/*
	 * 
	 CREATE TABLE user (
	  	  platformType tinyint(1) NOT NULL,
		  userId varchar(255) NOT NULL,
		  nickname varchar(8) default "", 
		  signin_dt datetime,
		  login_dt datetime,
		  logout_dt datatime,
		  PRIMARY KEY (userId)
		);
	 */
	public boolean insertUserAccount( Integer platformType, String userId )
	{
		String userIdentifier = "[platformType("+platformType+"), userId(" + userId +")]";
		
		if( ! User.checkPlatformType(platformType) ){
			log.error(userIdentifier + "Invalied PlatformType: " + platformType);
			return false;
		}
		if( ! User.checkUserID(userId) ){
			log.error(userIdentifier + "Invalied UserID: " + userId);
			return false;
		}
		
		String query = "insert into user (platformType, userId, signin_dt) "
		 				+ "values(?, ?, now())";
		 
		DBConnectionHelper db = null;
		try {
			db = new DBConnectionHelper( query );
			db.setInt(1, platformType);
			db.setString(2, userId);
			db.executeUpdate();
			return true;
			
        } catch (Exception e) {
        	log.error(userIdentifier + "Failed DB. query["+query+"] ");
        	e.printStackTrace();
        } finally {
        	db.close();
        } 
		return false;  
	}

	
	public boolean updateLoginDateTime( Integer platformType, String userId )
	{
		String userIdentifier = "[platformType("+platformType+"), userId(" + userId +")]";	
		String query = "update user set login_dt = now() "
						+ "where userId = ? ";
		 				
		DBConnectionHelper db = null;
		try {
			db = new DBConnectionHelper( query );
			db.setString(1, userId);
			db.executeUpdate();
			return true;
			
        } catch (Exception e) {
        	log.error(userIdentifier + "Failed DB. query["+query+"] ");
        	e.printStackTrace();
        } finally {
        	db.close();
        } 
		return false;  
	}
	
	public boolean updateLogOutDateTime( String userId )
	{
		String userIdentifier = "[userId(" + userId +")]";	
		String query = "update user set logout_dt = now() "
						+ "where userId = ? ";
		 				
		DBConnectionHelper db = null;
		try {
			db = new DBConnectionHelper( query );
			db.setString(1, userId);
			db.executeUpdate();
			return true;
			
        } catch (Exception e) {
        	log.error(userIdentifier + "Failed DB. query["+query+"] ");
        	e.printStackTrace();
        } finally {
        	db.close();
        } 
		return false;  
	}
	public DBResult selectTodayLastGuestUserId()
	{
		log.debug( "selectTodayLastGuestUserId Called . ");
		
		// signin_dt > CURRENT_DATE() : 오늘 0시 이후의 데이터를 뽑아온다.
		String query = "SELECT userId "
						+ " FROM user "
		 				+ " WHERE signin_dt > CURRENT_DATE()";
 
		DBConnectionHelper db = null;
		try {
			db = new DBConnectionHelper( query );  
		   
			ResultSet rs = db.executeQuery();
		    
		    String lastUserID = null;
		    while(rs.next()){     
		    	lastUserID = rs.getString("userId");
		    	
		    	System.out.println( lastUserID + "\t");
		    }
		    DBResult dbResult = new DBResult();
		    dbResult.noSelected = (lastUserID == null || lastUserID.isEmpty());
		    dbResult.resultObject = lastUserID;
		    return dbResult;
		    
		} catch (Exception e) {
			log.error("query["+query+"]");
			e.printStackTrace();
		} finally {
			db.close();
		} 
		return null; 

	}
}
