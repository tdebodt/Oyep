package com.kovilo.android.test;

import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.LinkedList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import android.location.Location;

public class Track {
	String name;
	private TrackSegment trackSegment;
	public double length;

	public Track(String name, TrackSegment trackSegment) {
		this.name = name;
		this.trackSegment = trackSegment;
	}
	
	public String getName() {
		return name;
	}
	
	public static Track fromGPX(InputStream is) {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
    	DocumentBuilder builder;
		try {
			builder = factory.newDocumentBuilder();
	    	Document doc = builder.parse(is);
//	    	NodeList metadatas = doc.getElementsByTagName("metadata");
	    	NodeList tracksNL = doc.getElementsByTagName("trk");
	    	if (tracksNL.getLength() >= 1) {
	    		Element trackEl = (Element)tracksNL.item(0);
	    		TrackSegment trackSegment = new TrackSegment();
	    		Track track = new Track(trackEl.getElementsByTagName("name").item(0).getTextContent(), trackSegment);
	    		NodeList trackSegments = trackEl.getElementsByTagName("trkseg");
	    		if (trackSegments.getLength() >= 1) {
	    			Element trackSegmentEl = (Element)trackSegments.item(0);
	    			
	    			track.trackSegment = trackSegment;
	    			NodeList trackPointsNL = trackSegmentEl.getElementsByTagName("trkpt");
	    			for (int p=0; p<trackPointsNL.getLength(); p++) {
	    				Element trackPointEl = (Element)trackPointsNL.item(p);
	    				double lat = Double.parseDouble(trackPointEl.getAttribute("lat"));
	    				double lon = Double.parseDouble(trackPointEl.getAttribute("lon"));
	    				double ele = Double.parseDouble(trackPointEl.getElementsByTagName("ele").item(0).getTextContent());
	    				TrackPoint tp = new TrackPoint(lat, lon, ele);
	    				trackSegment.addTrackPoint(tp);
	    			}
	    		}
	    		// compute remaining
	    		LinkedList<TrackPoint> trackPoints = trackSegment.getTrackPoints();
	    		Iterator<TrackPoint> iter = trackPoints.descendingIterator();
	    		TrackPoint ptp = null;
	    		while (iter.hasNext()) {
	    			TrackPoint tp = iter.next();
	    			if (ptp != null) {
	    				tp.remaining = tp.distanceToNext + ptp.remaining;
	    			}
	    			ptp = tp;
	    		}
	    		
	    		track.length = trackPoints.getFirst().remaining;
	    		return track;
	    	}
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	public TrackPoint getClosestTrackPoint(Location location) {
		if (this.getTrackSegment() != null) {
			double dst = Double.MAX_VALUE;
			TrackPoint closest = null;
			TrackSegment ts = this.getTrackSegment();
			if (ts.hasTrackPoint()) {
				for (TrackPoint tp : ts.getTrackPoints()) {
					if (closest == null) {
						closest = tp;
						dst = location.distanceTo(tp.getLocation());
					} else {
						double tpDst = location.distanceTo(tp.getLocation());
						if (tpDst < dst) {
							closest = tp;
							dst = tpDst;
						}
					}
				}
				return closest;
			}
		}
		return null;
	}

	public TrackSegment getTrackSegment() {
		return trackSegment;
	}

}
