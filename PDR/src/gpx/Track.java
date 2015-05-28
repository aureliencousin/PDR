package gpx;

import java.util.ArrayList;
import java.util.List;
import com.google.android.gms.maps.model.LatLngBounds;

public class Track {

	public List<TrackSeg> trkSeg;

	public Track() {
		trkSeg = new ArrayList<TrackSeg>();
	}

	public List<TrackSeg> getTrackSegs() {
		return trkSeg;
	}

	public void addTrackSeg(TrackSeg trackSeg) {
		trkSeg.add(trackSeg);
	}

	public void removeTrackSeg(TrackSeg trackSeg) {
		trkSeg.remove(trackSeg);
	}

	public LatLngBounds getLatLngBounds() {
		LatLngBounds.Builder builder = new LatLngBounds.Builder();
		for (TrackSeg seg : trkSeg) {
			LatLngBounds bounds = seg.getLatLngBounds();
			builder.include(bounds.northeast);
			builder.include(bounds.southwest);
		}
		return builder.build();
	}

}
