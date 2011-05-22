package com.kovilo.android.test;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class Utils {
	
    static boolean existsURL(URL url){
  	  try {
  		   HttpURLConnection.setFollowRedirects(false);
	    	   HttpURLConnection con = (HttpURLConnection) url.openConnection();
	    	   con.setRequestMethod("HEAD");
	    	   return (con.getResponseCode() == HttpURLConnection.HTTP_OK);
  	  }
  	  catch (Exception e) {
  	       e.printStackTrace();
  	       return false;
         }
  }
  
  private static final int IO_BUFFER_SIZE = 4 * 1024;  
  static void copy(InputStream in, OutputStream out) throws IOException {  
	    byte[] b = new byte[IO_BUFFER_SIZE];  
	    int read;  
	    while ((read = in.read(b)) != -1) {  
	    	out.write(b, 0, read);  
	    }  
  }  
}
