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
	public DBResult selectUserById( Integer userId ) 
	{	
		log.debug( "2-2-1. selectUserById Called . ");
		if( ! User.checkUserID(userId)) {
			log.error("invalid argment. userId("+userId+")");
			return null;
		}
		
		String query = "SELECT id, name, "
							+ "unix_timestamp(signin_dt) as signin_long, "
							+ "is_admin"
						+ " FROM user "
		 				+ " WHERE id = ?";
 
		DBConnectionHelper db = null;
		try {
			db = new DBConnectionHelper( query );
		    db.setInt(1, userId);
		    
		    ResultSet rs = db.executeQuery();
		    
		    User user = new User();
		    while(rs.next()){     
		    	user.userID = rs.getInt("id");
		    	user.userName = rs.getString("name");
                user.signin_dt = rs.getLong("signin_long");
                user.setAdmin(rs.getInt("is_admin"));
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
	
	public DBResult selectUserByName( String userName ) 
	{
		if( ! User.checkUserName(userName)) {
			log.error("invalid argment. userName("+userName+")");
			return null;
		}
		
		String query = "SELECT id, name, "
				+ "unix_timestamp(signin_dt) as signin_long, "
				+ "is_admin"
				+ " FROM user"
 				+ " WHERE name = ?";

		DBConnectionHelper db = null;
		try {
			db = new DBConnectionHelper( query );
		    db.setString(1, userName);
		    ResultSet rs = db.executeQuery(); 
		    
		    User user = new User();
		    while(rs.next()){     
		    	user.userID = rs.getInt("id");
		    	user.userName = rs.getString("name");
		        user.signin_dt = rs.getLong("signin_long");
		        user.setAdmin(rs.getInt("is_admin"));
		        System.out.println( user.toString() + "\t");
		    }
		    DBResult dbResult = new DBResult();
		    dbResult.noSelected = User.isNull(user);
		    dbResult.resultObject = user;
		    return dbResult;
		    
		} catch (Exception e) {
			log.error("query["+query+"], args[userName:"+userName+"]");
			e.printStackTrace();
		} finally {
			db.close();
		} 
		return null; 
	}
	
	/*
	 * 
	 CREATE TABLE user ( 
		  id int unsigned NOT NULL AUTO_INCREMENT,
		  name varchar(255) NOT NULL,
		  signin_dt datetime,
		  login_dt datetime,
		  PRIMARY KEY (id)
		);
	 */
	public boolean insertUserAccount( String userName )
	{
		if( StringHelper.isNull(userName) ) {
			log.error("Invalid Agument. userName:"+userName);
			return false;
		}
		
		String query = "insert into user (name, signin_dt, is_admin) "
		 				+ "values(?, now(), ?)";
		 
		DBConnectionHelper db = null;
		try {
			db = new DBConnectionHelper( query );
			db.setString(1, userName);
			db.setInt(2, User.USERTYPE_NORMAL);
			db.executeUpdate();
			return true;
			
        } catch (Exception e) {
        	log.error("querys["+query+"], "
        			+ "userName["+userName+"]");
        	e.printStackTrace();
        } finally {
        	db.close();
        } 
		return false;  
	}

	
	
}
