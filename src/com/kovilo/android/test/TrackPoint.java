package com.kovilo.android.test;

import android.location.Location;

public final class TrackPoint {
	double latitude;
	double longitude;
	double elevation;
	Location location;
	double remaining = 0.0d;
	double distanceToNext = 0.0d;
	double distanceToPrevious = 0.0d;

	public TrackPoint(double latitude, double longitude, double elevation) {
		this.latitude = latitude;
		this.longitude = longitude;
		this.elevation = elevation;
		this.location = new Location("nimportequoi");
		this.location.setLatitude(latitude);
		this.location.setLongitude(longitude);
		this.location.setAltitude(elevation);
	}
	
	public double getLatitude() {
		return latitude;
	}

	public double getLongitude() {
		return longitude;
	}

	public Location getLocation() {
		return location;
	}

	public double getElevation() {
		return elevation;
	}

}
