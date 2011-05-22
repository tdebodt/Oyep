package com.kovilo.android.test;

import java.io.FileInputStream;
import java.text.NumberFormat;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class Test extends Activity implements LocationListener {

	private static final int PROGRESS_DIALOG = 0;
	private ProgressDialog progressDialog;
	private GPXLoaderThread gpxLoaderThread;
	public Track track;
	public TrackStatistics trackStatistics;
	private LocationManager lm;
	private EditText editText1 = null;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		editText1 = (EditText)findViewById(R.id.editText1);
		// event listener
		// ((Button) findViewById(R.id.button1))
		// .setOnClickListener(new OnClickListener() {
		//
		// public void onClick(View v) {
		// showDialog(PROGRESS_DIALOG);
		// }
		// });
		try {
			FileInputStream fis = openFileInput(GPXLoaderThread.LOCAL_ACTIVITY_GPX);
			this.track = Track.fromGPX(fis);
			fis.close();
			updateTrackInfo();
		} catch (Exception e) {
			Toast.makeText(this, "error", Toast.LENGTH_LONG);
		}
		// get location
		lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0L, 0.0f,
				this);
	}

	private void updateTrackInfo() {
		((TextView) findViewById(R.id.textView9)).setText(track.getName());
		NumberFormat nf = NumberFormat.getNumberInstance();
		nf.setMaximumFractionDigits(3);
		nf.setMinimumFractionDigits(3);
		((TextView) findViewById(R.id.textView11)).setText(nf
				.format(track.length / 1000d) + " km");
	}

	@Override
	protected Dialog onCreateDialog(int id, Bundle bundle) {
		switch (id) {
		case PROGRESS_DIALOG:
			progressDialog = new ProgressDialog(this);
			progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
			progressDialog.setMessage("Loading...");
			return progressDialog;
		default:
			return null;
		}
	}

	@Override
	protected void onPrepareDialog(int id, Dialog dialog) {

		final Handler handler = new Handler() {
			public void handleMessage(Message msg) {
				int total = msg.arg1;
				progressDialog.setProgress(total);
				if (total >= 100) {
					dismissDialog(PROGRESS_DIALOG);
				}
				if (track != null && track.getName() != null) {
					updateTrackInfo();
				}
			}
		};

		switch (id) {
		case PROGRESS_DIALOG:
			progressDialog.setProgress(0);
			String activityId = editText1.getText().toString();
			gpxLoaderThread = new GPXLoaderThread(handler, this,
					new GarminConnectGPX(activityId));
			gpxLoaderThread.start();
			break;
		default:
			break;
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch(item.getItemId()) {
		case R.id.garmin:
			GarminDialog();
			break;
		case R.id.refresh:
			updateTrackInfo();
			break;
		case R.id.quit:
			this.finish();
			break;
		default:
			return false;
		}

		return true;
	}
	
	private void GarminDialog() {
		AlertDialog.Builder alert = new AlertDialog.Builder(this);
		alert.setTitle("Garmin Connect");
		alert.setMessage("Activity ID");

		// Set an EditText view to get user input
		final EditText input = new EditText(this);
		alert.setView(input);

		alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				editText1.setText(input.getText());
				dialog.dismiss();
			}
		});

		alert.setNegativeButton("Cancel",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
						editText1.setText("");
						dialog.cancel();
					}
				});
		AlertDialog alertDialog = alert.create();
		alertDialog.setOnDismissListener(new OnDismissListener() {

			public void onDismiss(DialogInterface dialog) {
				if (editText1.getText().toString().trim().equals("")) return;
				showDialog(PROGRESS_DIALOG);
			}
		});
		alertDialog.show();
	}

	@Override
	public void onBackPressed() {
		lm.removeUpdates(this);
		this.finish();
	}

	public void onLocationChanged(Location location) {

		NumberFormat nf = NumberFormat.getNumberInstance();
		nf.setMaximumFractionDigits(6);
		nf.setMinimumFractionDigits(6);
		NumberFormat nf2 = NumberFormat.getNumberInstance();
		nf2.setMaximumFractionDigits(3);
		nf2.setMinimumFractionDigits(3);

		((TextView) findViewById(R.id.textView4)).setText(nf.format(location
				.getLatitude()));
		((TextView) findViewById(R.id.textView6)).setText(nf.format(location
				.getLongitude()));

		if (track != null && track.getTrackSegment() != null) {
			TrackPoint closest = track.getClosestTrackPoint(location);
			if (closest != null) {
				double dst = location.distanceTo(closest.getLocation());
				((TextView) findViewById(R.id.textView7)).setText(nf2
						.format(dst) + " m");
				((TextView) findViewById(R.id.textView13)).setText(nf2
						.format((closest.remaining + dst) / 1000d) + " km");
				((TextView) findViewById(R.id.textView15)).setText(nf
						.format(closest.getLatitude()));
				((TextView) findViewById(R.id.textView17)).setText(nf
						.format(closest.getLongitude()));
			}
		}

	}

	public void onProviderDisabled(String provider) {
		Toast.makeText(this, "GPS disabled!", Toast.LENGTH_SHORT).show();
	}

	public void onProviderEnabled(String provider) {
		Toast.makeText(this, "GPS enabled...", Toast.LENGTH_SHORT).show();

	}

	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub

	}

}