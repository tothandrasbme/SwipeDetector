package com.android_examples.swipedetector_android_examplescom;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;
import com.android_examples.swipedetector_android_examplescom.MonitorThread.GestureActions;

public class MainActivity extends AppCompatActivity implements OnGestureListener, NumberPicker.OnValueChangeListener {

	GestureDetector gestureDetector;
	Toast messageToScreen = null;
	int trashHoldVertical = 100;
	int trashHoldHorizontal = 100;
	MonitorThread monitoring = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);

		verifyStoragePermissions(this);

		setContentView(R.layout.activity_main);

		gestureDetector = new GestureDetector(MainActivity.this, MainActivity.this);

	}

	@Override
	public boolean onFling(MotionEvent motionEvent1, MotionEvent motionEvent2, float X, float Y) {

		// Swipe up over 50 pixels but not more then 50 in X direction
		if ((motionEvent1.getY() - motionEvent2.getY() > 50) && (
			Math.abs(motionEvent1.getX() - motionEvent2.getX()) < trashHoldHorizontal)) {
			if (messageToScreen == null) {
				messageToScreen = new Toast(getApplicationContext());

			} else {
				messageToScreen.cancel();
			}

			if (monitoring == null) {
				messageToScreen = Toast
					.makeText(MainActivity.this, " Start Monitoring ", Toast.LENGTH_LONG);
				messageToScreen.show();

				showNumberDialog();

			} else {
				messageToScreen = Toast
					.makeText(MainActivity.this, " Swipe Up ", Toast.LENGTH_LONG);
				messageToScreen.show();
				if(monitoring != null) {
					monitoring.fireActionFromActivity(GestureActions.LENTROLFEL);
				}
			}

		}

		// Swipe down over 50 pixels but not more then 50 in X direction
		if ((motionEvent2.getY() - motionEvent1.getY() > 50) && (
			Math.abs(motionEvent1.getX() - motionEvent2.getX()) < trashHoldHorizontal)) {

			if (messageToScreen == null) {
				messageToScreen = new Toast(getApplicationContext());

			} else {
				messageToScreen.cancel();
			}

			messageToScreen = Toast.makeText(MainActivity.this, " Swipe Down ", Toast.LENGTH_LONG);
			messageToScreen.show();
			if(monitoring != null) {
				monitoring.fireActionFromActivity(GestureActions.FENTROLLE);
			}
		}

		// Swipe left over 50 pixels but not more then 50 in Y direction
		if ((motionEvent1.getX() - motionEvent2.getX() > 50) && (
			Math.abs(motionEvent1.getY() - motionEvent2.getY()) < trashHoldVertical)) {

			if (messageToScreen == null) {
				messageToScreen = new Toast(getApplicationContext());

			} else {
				messageToScreen.cancel();
			}

			messageToScreen = Toast.makeText(MainActivity.this, " Swipe Left ", Toast.LENGTH_LONG);
			messageToScreen.show();

			if(monitoring != null) {
				monitoring.fireActionFromActivity(GestureActions.JOBBROLBALRA);
			}
		}

		// Swipe right over 50 pixels but not more then 50 in Y direction
		if ((motionEvent2.getX() - motionEvent1.getX() > 50) && (
			Math.abs(motionEvent1.getY() - motionEvent2.getY()) < trashHoldVertical)) {

			if (messageToScreen == null) {
				messageToScreen = new Toast(getApplicationContext());

			} else {
				messageToScreen.cancel();
			}

			messageToScreen = Toast.makeText(MainActivity.this, " Swipe Right ", Toast.LENGTH_LONG);
			messageToScreen.show();

			if(monitoring != null) {
				monitoring.fireActionFromActivity(GestureActions.BALROLJOBBRA);
			}
		}

		// Swipe diagonal to top right over 50 pixels
		if ((motionEvent2.getX() - motionEvent1.getX() > 50) && (
			motionEvent1.getY() - motionEvent2.getY() > 50)) {

			if (messageToScreen == null) {
				messageToScreen = new Toast(getApplicationContext());

			} else {
				messageToScreen.cancel();
			}

			messageToScreen = Toast
				.makeText(MainActivity.this, " Swipe from bottom left to top right ",
					Toast.LENGTH_LONG);
			messageToScreen.show();

			if(monitoring != null) {
				monitoring.fireActionFromActivity(GestureActions.KERESZTBEJOBBRAFEL);
			}
		}

		// Swipe diagonal to bottom right over 50 pixels
		if ((motionEvent2.getX() - motionEvent1.getX() > 50) && (
			motionEvent2.getY() - motionEvent1.getY() > 50)) {

			if (messageToScreen == null) {
				messageToScreen = new Toast(getApplicationContext());

			} else {
				messageToScreen.cancel();
			}

			messageToScreen = Toast
				.makeText(MainActivity.this, " Swipe from top left to bottom right ",
					Toast.LENGTH_LONG);
			messageToScreen.show();

			if(monitoring != null) {
				monitoring.fireActionFromActivity(GestureActions.KERESZTBEJOBBRALE);
			}
		}

		// Swipe diagonal to bottom left over 50 pixels
		if ((motionEvent1.getX() - motionEvent2.getX() > 50) && (
			motionEvent2.getY() - motionEvent1.getY() > 50)) {

			if (messageToScreen == null) {
				messageToScreen = new Toast(getApplicationContext());

			} else {
				messageToScreen.cancel();
			}

			messageToScreen = Toast
				.makeText(MainActivity.this, " Swipe from top right to bottom left ",
					Toast.LENGTH_LONG);
			messageToScreen.show();

			if(monitoring != null) {
				monitoring.fireActionFromActivity(GestureActions.KERESZTBEBALRALE);
			}
		}

		// Swipe diagonal to top left over 50 pixels
		if ((motionEvent1.getX() - motionEvent2.getX() > 50) && (
			motionEvent1.getY() - motionEvent2.getY() > 50)) {

			if (messageToScreen == null) {
				messageToScreen = new Toast(getApplicationContext());

			} else {
				messageToScreen.cancel();
			}

			messageToScreen = Toast
				.makeText(MainActivity.this, " Swipe from bottom right to top left ",
					Toast.LENGTH_LONG);
			messageToScreen.show();

			if(monitoring != null) {
				monitoring.fireActionFromActivity(GestureActions.KERESZTBEBALRAFEL);
			}
		} else {

			return true;
		}

		return true;
	}

	@Override
	public void onLongPress(MotionEvent arg0) {

		if (monitoring != null) {
			monitoring.stopMeasuring();
			messageToScreen = Toast
				.makeText(MainActivity.this, " Stop measuring, Close file ", Toast.LENGTH_LONG);
			messageToScreen.show();
			monitoring = null;
		}

	}

	@Override
	public boolean onScroll(MotionEvent arg0, MotionEvent arg1, float arg2, float arg3) {

		// TODO Auto-generated method stub

		return false;
	}

	@Override
	public void onShowPress(MotionEvent arg0) {

		// TODO Auto-generated method stub

	}

	@Override
	public boolean onSingleTapUp(MotionEvent arg0) {

		// TODO Auto-generated method stub

		return false;
	}

	@Override
	public boolean onTouchEvent(MotionEvent motionEvent) {

		// TODO Auto-generated method stub

		return gestureDetector.onTouchEvent(motionEvent);
	}

	@Override
	public boolean onDown(MotionEvent arg0) {

		// TODO Auto-generated method stub

		return false;
	}


	private static final int REQUEST_EXTERNAL_STORAGE = 1;
	private static String[] PERMISSIONS_STORAGE = {
		Manifest.permission.READ_EXTERNAL_STORAGE,
		Manifest.permission.WRITE_EXTERNAL_STORAGE
	};


	public static void verifyStoragePermissions(Activity activity) {
		// Check if we have write permission
		int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);

		if (permission != PackageManager.PERMISSION_GRANTED) {
			// We don't have permission so prompt the user
			ActivityCompat.requestPermissions(
				activity,
				PERMISSIONS_STORAGE,
				REQUEST_EXTERNAL_STORAGE
			);
		}
	}

	@Override
	public void onValueChange(NumberPicker picker, int oldVal, int newVal) {

		Log.i("value is",""+newVal);

	}

	public void showNumberDialog()
	{

		final Dialog d = new Dialog(MainActivity.this);
		d.setTitle("Mennyire fáradt? ");
		d.setContentView(R.layout.number_dialog);
		Button b1 = (Button) d.findViewById(R.id.button1);
		Button b2 = (Button) d.findViewById(R.id.button2);
		final NumberPicker np = (NumberPicker) d.findViewById(R.id.numberPicker1);
		final TextView tv = (TextView) d.findViewById(R.id.header);
		tv.setText("Kérem írja le mennyire fáradt 0-10 ! (0 nagyon éber , 10 nagyon fáradt)");
		np.setMaxValue(10); // max value 100
		np.setMinValue(0);   // min value 0
		np.setWrapSelectorWheel(false);
		np.setOnValueChangedListener(this);
		b1.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v) {
				messageToScreen = Toast.makeText(MainActivity.this, " Chosen Number : " + String.valueOf(np.getValue()), Toast.LENGTH_LONG);
				messageToScreen.show();

				monitoring = new MonitorThread();
				monitoring.setContext(getApplicationContext());
				monitoring.initMonitorThread();
				monitoring.setSleepnessLevel(String.valueOf(np.getValue()));
				new Thread(monitoring).start();

				d.dismiss();
			}
		});
		b2.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v) {
				d.dismiss(); // dismiss the dialog
			}
		});
		d.show();


	}

}
