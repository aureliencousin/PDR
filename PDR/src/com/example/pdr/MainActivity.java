package com.example.pdr;

import pdr.DeviceAttitudeHandler;
import pdr.StepDetectionHandler;
import pdr.StepDetectionHandler.StepDetectionListener;
import pdr.StepPositioningHandler;
import viewer.GoogleMapTracer;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.*;
import com.google.android.gms.maps.GoogleMap.OnMapClickListener;
import com.google.android.gms.maps.model.*;

import android.content.Context;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

public class MainActivity extends FragmentActivity {

	GoogleMap map;
	SensorManager sm;
	StepDetectionHandler sdh;
	StepPositioningHandler sph;
	DeviceAttitudeHandler dah;
	GoogleMapTracer gm;
	boolean isWalking = false;
	Location lKloc;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		if (servicesConnected()) {
			map = ((SupportMapFragment) getSupportFragmentManager()
					.findFragmentById(R.id.mapView)).getMap();
			LatLng lastKnown;
			lKloc = getLocation();
			lastKnown = new LatLng(lKloc.getLatitude(),
					lKloc.getLongitude());

			map.setMyLocationEnabled(true);
			// centrer la vue sur la derniere position connue
			map.moveCamera(CameraUpdateFactory.newLatLngZoom(lastKnown, 13));
			gm = new GoogleMapTracer(map);

			map.setOnMapClickListener(new OnMapClickListener() {
				@Override
				public void onMapClick(LatLng arg0) {
					lKloc.setLatitude(arg0.latitude);
					lKloc.setLongitude(arg0.longitude);
					sph.setmCurrentLocation(lKloc);
					//on se met a enregistrer les mouvements sur un nouveau segment
					if (!isWalking) {
						isWalking = true;
						gm.newSegment();
						gm.newPoint(arg0, 0);
					//on arrête d'enregistrer les mouvements et on pose un marqueur de fin sur le segment
					} else {
						isWalking = false;
						gm.endSegment();
					}
				}

			});

			SensorManager sm = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
			sdh = new StepDetectionHandler(sm);
			sdh.setStepListener(mStepDetectionListener);
			dah = new DeviceAttitudeHandler(sm);
			sph = new StepPositioningHandler();
			sph.setmCurrentLocation(lKloc);
		}

	}

	/** Récupère la dernière position connue de l'utilisateur */
	public Location getLocation() {
		LocationManager locationManager = (LocationManager) this
				.getSystemService(Context.LOCATION_SERVICE);
		if (locationManager != null) {
			Log.d("loc",locationManager.toString());
			Location lastKnownLocationGPS = locationManager
					.getLastKnownLocation(LocationManager.GPS_PROVIDER);
			if (lastKnownLocationGPS != null) {
				return lastKnownLocationGPS;
			} else {
				Location loc = locationManager
						.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER);
				return loc;
			}
		} else {
			return null;
		}
	}

	/** verifier la disponibilité des services google */
	private boolean servicesConnected() {
		int resultCode = GooglePlayServicesUtil
				.isGooglePlayServicesAvailable(this);
		if (ConnectionResult.SUCCESS == resultCode) {
			return true;
		}
		return false;
	}

	protected void onResume() {
		super.onResume();
		sdh.start();
		dah.start();
	}

	protected void onPause() {
		super.onPause();
		sdh.stop();
		dah.stop();
	}

	private StepDetectionListener mStepDetectionListener = new StepDetectionListener() {
		public void newStep(float stepSize) {
			Location newloc = sph.computeNextStep(stepSize, dah.orientationVals[0]);
			Log.d("LATLNG", newloc.getLatitude() + " " + newloc.getLongitude()+ " " + dah.orientationVals[0]);
			if (isWalking) {
				//ajout du nouveau point à la carte
				gm.newPoint(new LatLng(newloc.getLatitude(), newloc.getLongitude()),dah.orientationVals[0]);
			}
		}
	};
}
