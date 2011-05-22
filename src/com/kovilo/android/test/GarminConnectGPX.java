package com.kovilo.android.test;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

public class GarminConnectGPX implements GPX {
	
	private String activityId;
	private URL url;
	
	public GarminConnectGPX(String activityId) {
		this.activityId = activityId;
		this.url = GarminConnectGPX.getGPXUrl(this.activityId);		
	}
	
	public InputStream getInputStream() {
		if (url == null) return null;
		URLConnection con;
		try {
			con = url.openConnection();
			InputStream is = con.getInputStream();
			return is;
		} catch (IOException e) {}
		return null;
	}
	
	public static URL getGPXUrl(String activity) {
		if (activity==null || activity.trim().equals("")) return null;
		try {
			return new URL("http://connect.garmin.com/proxy/activity-service-1.1/gpx/activity/" + activity + "?full=true");
		} catch (MalformedURLException e) {}
		return null;
	}
}
