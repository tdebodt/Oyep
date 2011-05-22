package com.kovilo.android.test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import junit.framework.TestCase;

public class TrackTest extends TestCase {

	public void testFromGPX() {
		try {
			Track.fromGPX(new FileInputStream(new File("/Users/tdb/Downloads/activity_86858422.gpx")));
		} catch (FileNotFoundException e) {
			TestCase.fail(e.getMessage());
		}
	}

}
