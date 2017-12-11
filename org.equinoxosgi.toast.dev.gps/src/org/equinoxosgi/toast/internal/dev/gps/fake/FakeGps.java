package org.equinoxosgi.toast.internal.dev.gps.fake;

import org.equinoxosgi.toast.dev.gps.IGps;

public class FakeGps implements IGps {
	@Override
	public int getHeading() {
		return 90; // 90 degrees (east)
	}
	
	@Override
	public int getLatitude() {
		return 3776999; // 37.76999 N
	}
	
	@Override
	public int getLongitude() {
		return -12244694; // 122.44694 W
	}
	
	@Override
	public int getSpeed() {
		return 50; // 50 kph
	}
}
