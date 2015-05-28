package pdr;

import android.location.Location;

public class StepPositioningHandler {

	private Location mCurrentLocation;
	private static final int eRadius = 6371000; //rayon de la terre en m

	public Location getmCurrentLocation() {
		return mCurrentLocation;
	}

	public void setmCurrentLocation(Location mCurrentLocation) {
		this.mCurrentLocation = mCurrentLocation;
	}
	
	/** Calcule la nouvelle position de l'utilisateur à partir de la courante
	 * @param stepSize la taille du pas qu'a fait l'utilisateur
	 * @param bearing l'angle de direction
	 * @return la nouvelle localisation
	 */
	public Location computeNextStep(float stepSize,float bearing) {
		Location newLoc = new Location(mCurrentLocation);
		float angDistance = stepSize / eRadius;
		double oldLat = mCurrentLocation.getLatitude();
		double oldLng = mCurrentLocation.getLongitude();
		double newLat = Math.asin( Math.sin(Math.toRadians(oldLat))*Math.cos(angDistance) +
					Math.cos(Math.toRadians(oldLat))*Math.sin(angDistance)*Math.cos(bearing) );
		double newLon = Math.toRadians(oldLng) + 
					Math.atan2(Math.sin(bearing)*Math.sin(angDistance)*Math.cos(Math.toRadians(oldLat)),
                    Math.cos(angDistance) - Math.sin(Math.toRadians(oldLat))*Math.sin(newLat));
		//reconversion en degres
		newLoc.setLatitude(Math.toDegrees(newLat));
		newLoc.setLongitude(Math.toDegrees(newLon));
		
		newLoc.setBearing((mCurrentLocation.getBearing()+180)% 360);
		mCurrentLocation = newLoc;
		
		return newLoc;
	}
	
}
