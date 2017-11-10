package test;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

import app.Initailizer;

import contents.front.user.LogIn;
import exception.contents.ContentsRoleException;
import net.protocol.EResultCode;
import utils.FileHelper;
import utils.StringHelper;


public class TestMain {
	
	final private static Logger log = Logger.getLogger( TestMain.class );
	
	public static void main(String[] args) 
    {      
		java.text.SimpleDateFormat formatter=new java.text.SimpleDateFormat("yyMMddHHmmss"); 

		System.out.println (formatter.format(new java.util.Date()));
		
		//System.out.println("@@@@@@@@@@@@@@@@@@@@@@@ "+createUserID());
		/*String scriptTitle = "132) UNIT 69-11 a cake, some cake, some cakes (countable, uncountable 2) (with answers)-111.pdf";
		
		String REGEX_DOUBLE_DOWNLOAD_NUMBER = "-([0-9]{1,3}).pdf";
		
		scriptTitle = replaceLast(scriptTitle, REGEX_DOUBLE_DOWNLOAD_NUMBER, "");
		scriptTitle = replaceLast(scriptTitle, ".pdf", "");
		System.out.println("@@@@@@@@@@@@@@@@@@@@@@@ "+scriptTitle);
		*/
		
    }

	public static String replaceLast(String text, String regex, String replacement) {
        return text.replaceFirst("(?s)(.*)" + regex, "$1" + replacement);
    }
	static String createUserID()
    {
    	String lastID = "GUEST17110217255200010";
    	
    	String countStr = lastID.substring(17, 22);
    	System.out.println("@@@@@@@@@@@@@@@@@@@@@@@ countStr: "+countStr);
    	if( StringHelper.isNull(countStr) )
    		;
    	
    	Integer count = Integer.valueOf(countStr);
    	if( count == null || count < 0 )
    		;
    	
    	if( count == 99999 )
    		;
    	
    	count += 1;
    
    	countStr = String.valueOf(count);
    	System.out.println("@@@@@@@@@@@@@@@@@@@@@@@ countStr: "+countStr);
    	if( countStr.length() == 1 ) countStr = "0000" + countStr;
    	else if( countStr.length() == 2 ) countStr = "000" + countStr;
    	else if( countStr.length() == 3 ) countStr = "00" + countStr;
    	else if( countStr.length() == 4 ) countStr = "0" + countStr;
    	
    	System.out.println("@@@@@@@@@@@@@@@@@@@@@@@ countStr: "+countStr);
    	
    	String newID = lastID.substring(0, 17) + countStr;
    	System.out.println("@@@@@@@@@@@@@@@@@@@@@@@ lastID: "+lastID.substring(0, 16));
    	return newID;
    }

}
