package com.weddingnotification.httpClient;

import java.net.URL;
import java.net.HttpURLConnection;

/**
 * @author Neel Jain
 * This class will serve as a wrapper to
 * Http Client provided by Java Net package
 */

public class HttpClient {
	private HttpURLConnection conn;
	
	public HttpClient(String url) throws java.net.MalformedURLException, java.io.IOException, java.net.ProtocolException
	{
        // 1. Open connection
        this.conn = (HttpURLConnection) new URL(url).openConnection();
        this.conn.setDoOutput(true);
        // use content-type application/json by default
        this.conn.setRequestProperty("content-type", "application/json");
    }

    public void method(String method) throws java.net.ProtocolException
    {
    	this.conn.setRequestMethod(method);
    }    

    public void property(String property, String value)
    {
    	this.conn.setRequestProperty(property, value);    	
    }

    public HttpURLConnection connection()
    {
    	return this.conn;
    }
        // 4. Set the headers
        //conn.setRequestProperty("Authorization", "Bearer "+apikey);
        //conn.setRequestProperty("Accept", "application/json");
        //conn.setRequestProperty("User-Agent",
            //"Mozilla/5.0 (Macintosh; U; Intel Mac OS X 10.4; en-US; rv:1.9.2.2) Gecko/20100316 Firefox/3.6.2");

}
