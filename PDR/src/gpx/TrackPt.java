package gpx;

public class TrackPt {

	private double latitude;
	private double longitude;

	public TrackPt(double latitude, double longitude) {
		this.latitude = latitude;
		this.longitude = longitude;
	}

	public double getLatitude() {
		return latitude;
	}

	public double getLongitude() {
		return longitude;
	}

	public String toString() {
		return "[Lat:" + latitude + ",Lon:" + longitude + "]";
	}

}
