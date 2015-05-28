package com.example.pdr;

import gpx.GPX;
import gpx.GPXParser;
import gpx.Track;
import gpx.TrackPt;
import gpx.TrackSeg;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.xmlpull.v1.XmlPullParserException;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

public class GPXActivity extends FragmentActivity {

	GoogleMap map;
	Polyline current;
	GPX gpx;

	@SuppressWarnings("resource")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_gpx);
		if (servicesConnected()) {
			map = ((SupportMapFragment) getSupportFragmentManager()
					.findFragmentById(R.id.gpxmapView)).getMap();
			map.setMyLocationEnabled(true);
			// centrer sur grenoble
			map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(45.188529, 5.724524), 15));

			try {
				InputStream is;
				Bundle extras = getIntent().getExtras();
				// recupération du fichier choisi dans l'activité précédente
				if (extras != null) {
					String value = extras.getString("uri");
					is = getContentResolver().openInputStream(Uri.parse(value));
				}
				// ouverture d'un fichier par defaut
				else {
					is = getAssets().open("bikeandrun.gpx");
				}

				gpx = GPXParser.parse(is);
				is.close();
				map.setOnMapLoadedCallback(new GoogleMap.OnMapLoadedCallback() {
					@Override
					public void onMapLoaded() {
						//recentrage de la carte selon les limites du fichier gpx
						map.moveCamera(CameraUpdateFactory.newLatLngBounds(
								gpx.getLatLngBounds(), 20));
					}
				});

				displayGPX(gpx);
			} catch (IOException e) {
				Log.d("ioe", "Impossible d'ouvrir le fichier gpx");
				e.printStackTrace();
			} catch (XmlPullParserException e) {
				Log.d("xml", "Erreur lors du parsing du fichier gpx");
				e.printStackTrace();
			}
		}
	}

	public void displayGPX(GPX in) {
		for (Track trk : in.getTracks()) {
			for (TrackSeg trkseg : trk.getTrackSegs()) {
				//afficher chaque segment dans une couleur différente
				int color = Color.rgb((int) (Math.random() * 256),(int) (Math.random() * 256),(int) (Math.random() * 256));
				current = map.addPolyline(new PolylineOptions().color(color).width(5));
				LatLng lastPt = null;
				boolean first = true;
				for (TrackPt trkPt : trkseg.getTrackPt()) {
					lastPt = new LatLng(trkPt.getLatitude(),trkPt.getLongitude());
					if (first) {
						//marqueur de depart
						map.addMarker(new MarkerOptions()
								.position(lastPt)
								.icon(BitmapDescriptorFactory
										.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
						first = false;
					}
					List<LatLng> points = current.getPoints();
					points.add(lastPt);
					current.setPoints(points);
				}
				//marqueur d'arrivée
				if (lastPt != null)
					map.addMarker(new MarkerOptions()
							.position(lastPt)
							.icon(BitmapDescriptorFactory
									.defaultMarker(BitmapDescriptorFactory.HUE_RED)));
			}
		}
	}

	/** verifie la disponibilité des services google */
	private boolean servicesConnected() {
		int resultCode = GooglePlayServicesUtil
				.isGooglePlayServicesAvailable(this);
		if (ConnectionResult.SUCCESS == resultCode) {
			return true;
		}
		return false;
	}
}
