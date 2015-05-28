package pdr;

import android.app.Activity;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

public class DeviceAttitudeHandler extends Activity implements
		SensorEventListener {

	SensorManager sm;
	Sensor sensor;
	private float[] mRotationMatrixFromVector = new float[9];
	private float[] mRotationMatrix = new float[9];
	public float[] orientationVals = new float[3];
	private final int sensorType = Sensor.TYPE_ROTATION_VECTOR;

	public DeviceAttitudeHandler(SensorManager sm) {
		super();
		this.sm = sm;
		sensor = sm.getDefaultSensor(sensorType);
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

	public void onSensorChanged(SensorEvent event) {
		// Convert the rotation-vector to a 4x4 matrix.
		SensorManager.getRotationMatrixFromVector(mRotationMatrixFromVector,
				event.values);
		SensorManager.remapCoordinateSystem(mRotationMatrixFromVector,
				SensorManager.AXIS_X, SensorManager.AXIS_Z, mRotationMatrix);
		SensorManager.getOrientation(mRotationMatrix, orientationVals);

		orientationVals[0] = (float) orientationVals[0];
		orientationVals[1] = (float) orientationVals[1]; // axe de rotation
		orientationVals[2] = (float) orientationVals[2];

	}

}
