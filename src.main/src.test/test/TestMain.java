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


public class TestMain {
	
	final private static Logger log = Logger.getLogger( TestMain.class );
	
	public static void main(String[] args) 
    {      
		
		String scriptTitle = "132) UNIT 69-11 a cake, some cake, some cakes (countable, uncountable 2) (with answers)-111.pdf";
		
		String REGEX_DOUBLE_DOWNLOAD_NUMBER = "-([0-9]{1,3}).pdf";
		
		scriptTitle = replaceLast(scriptTitle, REGEX_DOUBLE_DOWNLOAD_NUMBER, "");
		scriptTitle = replaceLast(scriptTitle, ".pdf", "");
		System.out.println("@@@@@@@@@@@@@@@@@@@@@@@ "+scriptTitle);
		
		
    }

	public static String replaceLast(String text, String regex, String replacement) {
        return text.replaceFirst("(?s)(.*)" + regex, "$1" + replacement);
    }
	

}
