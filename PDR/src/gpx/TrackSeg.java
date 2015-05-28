package gpx;

import java.util.ArrayList;
import java.util.List;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;

public class TrackSeg {

	public List<TrackPt> trkpt;

	public TrackSeg() {
		trkpt = new ArrayList<TrackPt>();
	}

	public List<TrackPt> getTrackPt() {
		return trkpt;
	}

	public void addTrkPt(TrackPt trkPt) {
		trkpt.add(trkPt);
	}

	public void removeTrackPt(TrackPt trkPt) {

		trkpt.remove(trkPt);
	}

	public LatLngBounds getLatLngBounds() {
		LatLngBounds.Builder builder = new LatLngBounds.Builder();
		for (TrackPt point : trkpt) {
			builder.include(new LatLng(point.getLatitude(), point
					.getLongitude()));
		}
		return builder.build();
	}
}
