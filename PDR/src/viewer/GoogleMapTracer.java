package viewer;

import java.util.List;
import android.graphics.Color;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

public class GoogleMapTracer {

	GoogleMap gm;
	Polyline current;
	Marker currentMarker;

	public GoogleMapTracer(GoogleMap gm) {
		this.gm = gm;
		//marqueur de suivi de l'utilisateur
		currentMarker = gm.addMarker(new MarkerOptions()
				.position(new LatLng(0, 0)).title("Current")
				.icon(BitmapDescriptorFactory.fromAsset("arrow-32.png"))
				.visible(false));
	}

	public void newSegment() {
		//dessine un nouveau tracé
		current = gm.addPolyline(new PolylineOptions().color(Color.RED)
				.width(5));
	}

	/** Ajoute un point au tracé courant
	 * @param point les coordonnées du point
	 * @param angle la direction en radians
	 */
	public void newPoint(LatLng point, double angle) {
		if (current == null) {
			//s'il n'ya pas de tracé en cours, en démarrer un nouveau
			newSegment();
		}
		// premier point: ajout d'un marker de debut
		if (current.getPoints().size() == 0) {
			gm.addMarker(new MarkerOptions()
					.position(point)
					.title("Start")
					.icon(BitmapDescriptorFactory
							.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
		}

		// curseur pour la position courante
		currentMarker.setVisible(true);
		currentMarker.setPosition(point);
		currentMarker.setRotation((float) Math.toDegrees(angle));

		// ajout du nouveau point
		List<LatLng> points = current.getPoints();
		points.add(point);
		current.setPoints(points);

		// centrer la camera sur la position courante
		CameraUpdate center = CameraUpdateFactory.newLatLng(point);
		gm.moveCamera(center);

	}

	public void endSegment() {
		/* ajout d'un marker de fin sur le segment actuel */
		if (current != null && current.getPoints().size() > 0) {
			List<LatLng> points = current.getPoints();
			gm.addMarker(new MarkerOptions()
					.position(points.get(points.size() - 1))
					.title("End")
					.icon(BitmapDescriptorFactory
							.defaultMarker(BitmapDescriptorFactory.HUE_RED)));
		}
		currentMarker.setVisible(false);
	}

	public void clear() {
		gm.clear();
	}

}
