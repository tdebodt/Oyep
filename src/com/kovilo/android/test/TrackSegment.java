package com.kovilo.android.test;

import java.util.LinkedList;

public class TrackSegment {
	private LinkedList<TrackPoint> trackPoints = new LinkedList<TrackPoint>();
	
	public boolean addTrackPoint(TrackPoint tp) {
		if (!trackPoints.isEmpty()) {
			TrackPoint last = trackPoints.getLast();
			last.distanceToNext = last.getLocation().distanceTo(tp.getLocation());
			tp.distanceToPrevious = last.distanceToNext;
		}
		return trackPoints.add(tp);
	}
	
	public LinkedList<TrackPoint> getTrackPoints() {
		return new LinkedList<TrackPoint>(trackPoints);
	}

	public boolean hasTrackPoint() {
		// TODO Auto-generated method stub
		return !trackPoints.isEmpty();
	}
}
