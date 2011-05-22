package com.kovilo.android.test;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.app.Activity;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

public class GPXLoaderThread extends Thread {

	public static final String LOCAL_ACTIVITY_GPX = "activity.gpx";

	Handler mHandler;
	Activity activity;
	GPX gpx;

	public GPXLoaderThread(Handler h, Activity a, GPX gpx) {
		mHandler = h;
		activity = a;
		this.gpx = gpx;
	}

	public void run() {
		sendProgress(0);
		importGPX();
		sendProgress(33);
		parseGPX();
		sendProgress(66);
		// computeStatistics();
		sendProgress(100);
	}

	public void sendProgress(int total) {
		Message msg = mHandler.obtainMessage();
		msg.arg1 = total;
		mHandler.sendMessage(msg);
	}

	public void importGPX() {
		InputStream is = null;
		OutputStream os = null;
		try {
			is = gpx.getInputStream();
			if (is == null)
				return;
			try {
				os = activity.openFileOutput(LOCAL_ACTIVITY_GPX,
						Activity.MODE_PRIVATE);
				if (os == null)
					return;
				try {
					Utils.copy(is, os);
				} finally {
					os.close();
					os = null;
				}
			} finally {
				is.close();
				is = null;
			}

		} catch (IOException e) {
		}
	}

	public void parseGPX() {
		try {
			Track track = Track.fromGPX(activity
					.openFileInput(LOCAL_ACTIVITY_GPX));
			((Test) activity).track = track;
		} catch (FileNotFoundException e) {
			Toast.makeText(activity, "No track imported!", Toast.LENGTH_SHORT)
					.show();
		}
	}

}
