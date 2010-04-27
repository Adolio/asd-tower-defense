package testClient.client;

import java.util.ArrayList;

public class Parser {
    private Parser(){}
    
    /** ********************************* */
    
    /**
     * Give a string "message" to the fonction, and she's gonna split according to expr
     */
    public static ArrayList<String> parse(String message,String expr){
	ArrayList<String> r = new ArrayList<String>();
	//String[] tmp = message.split()
	 
	
	return r;
    }
    
    public static String getNext(String message, String expr){
	return message.split(expr)[1];
    }

}
