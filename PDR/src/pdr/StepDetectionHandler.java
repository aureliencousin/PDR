package pdr;

import android.app.Activity;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

public class StepDetectionHandler extends Activity implements
		SensorEventListener {

	SensorManager sm;
	Sensor sensor;

	private StepDetectionListener mStepDetectionListener;

	int step = 0;

	public StepDetectionHandler(SensorManager sm) {
		super();
		this.sm = sm;
		sensor = sm.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);
	}

	public void start() {
		sm.registerListener(this, sensor, SensorManager.SENSOR_DELAY_NORMAL);

	}

	public void stop() {
		sm.unregisterListener(this);
	}

	@Override
	public void onAccuracyChanged(Sensor arg0, int arg1) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onSensorChanged(SensorEvent e) {
		float y;

		if (e.sensor.getType() == Sensor.TYPE_LINEAR_ACCELERATION) {
			y = e.values[1];
			// seuil à partir duquel on considère qu'il s'agit bien d'un pas
			if (y > 1 && mStepDetectionListener != null) {
				onNewStepDetected();

			}
		}
	}

	public void onNewStepDetected() {
		float distanceStep = 0.8f;
		step++;
		mStepDetectionListener.newStep(distanceStep);
	}

	public void setStepListener(StepDetectionListener listener) {
		mStepDetectionListener = listener;
	}

	public interface StepDetectionListener {
		public void newStep(float stepSize);

	}

}
