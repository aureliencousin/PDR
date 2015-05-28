package gpx;

import java.util.ArrayList;
import java.util.List;
import com.google.android.gms.maps.model.LatLngBounds;

public class GPX {

	public List<Track> trk;

	public GPX() {
		trk = new ArrayList<Track>();
	}

	public List<Track> getTracks() {
		return trk;
	}

	public void addTrack(Track track) {
		trk.add(track);
	}

	public void removeTrack(Track track) {
		trk.remove(track);
	}

	public LatLngBounds getLatLngBounds() {
		LatLngBounds.Builder builder = new LatLngBounds.Builder();
		for (Track track : trk) {
			LatLngBounds bounds = track.getLatLngBounds();
			builder.include(bounds.northeast);
			builder.include(bounds.southwest);
		}
		return builder.build();
	}

}
