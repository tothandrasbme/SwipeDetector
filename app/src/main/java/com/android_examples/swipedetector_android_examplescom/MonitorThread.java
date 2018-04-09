package com.android_examples.swipedetector_android_examplescom;

import android.content.Context;
import android.media.MediaPlayer;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Random;

/**
 * Created by andrastoth on 2018. 03. 05..
 */

public class MonitorThread implements Runnable {

	private static final int LOWER_RANDOM_LIMIT = 10;
	private static final int HIGHER_RANDOM_LIMIT = 20;
	private static final int WAIT_FOR_ACTION_IN_SEC = 5;
	private static final Random RANDOM = new Random();
	private static final Random ACTIONRANDOM = new Random();
	private HashMap<GestureActions,MediaPlayer> players = new HashMap<>();
	Handler randomTiming = new Handler();
	Handler waitForAction = new Handler();
	Context mContext = null;
	private int measurementCounter = 0;
	private int actionCounter = 0;
	boolean measuring = true;
	boolean waitForActions = false;
	private GestureActions actualGesture = GestureActions.GAP;
	private GestureActions waitedGesture = GestureActions.GAP;
	private String sleepnessLevel = "";

	long commandRequestTime = 0;
	long lastAnswerTime = 0;
	String finalResultOfTest = "";

	public enum GestureActions {
		GAP, BALROLJOBBRA, FENTROLLE, JOBBROLBALRA, LENTROLFEL, KERESZTBEBALRAFEL, KERESZTBEBALRALE, KERESZTBEJOBBRAFEL, KERESZTBEJOBBRALE, OK, HIBA
	}

	public void setContext(Context c) {
		this.mContext = c;
	}

	public MonitorThread() {
	}

	public void initMonitorThread() {
		this.players.put(GestureActions.BALROLJOBBRA, MediaPlayer.create(mContext, R.raw.bj));
		this.players.put(GestureActions.FENTROLLE, MediaPlayer.create(mContext, R.raw.fl));
		this.players.put(GestureActions.JOBBROLBALRA, MediaPlayer.create(mContext, R.raw.jb));
		this.players.put(GestureActions.LENTROLFEL, MediaPlayer.create(mContext, R.raw.lf));
		this.players.put(GestureActions.KERESZTBEBALRAFEL, MediaPlayer.create(mContext, R.raw.kbf));
		this.players.put(GestureActions.KERESZTBEBALRALE, MediaPlayer.create(mContext, R.raw.kbl));
		this.players.put(GestureActions.KERESZTBEJOBBRAFEL, MediaPlayer.create(mContext, R.raw.kjf));
		this.players.put(GestureActions.KERESZTBEJOBBRALE, MediaPlayer.create(mContext, R.raw.kjl));
		this.players.put(GestureActions.OK, MediaPlayer.create(mContext, R.raw.ok));
		this.players.put(GestureActions.HIBA, MediaPlayer.create(mContext, R.raw.hiba));
	}


	public class MyErrorListener implements MediaPlayer.OnErrorListener {
		@Override
		public boolean onError(MediaPlayer mp, int what, int extra) {
			// The MediaPlayer has moved to the Error state, must be reset!
			Log.d("MonitorThread","MediaPlayer error : " + what );
			mp.reset();
			return true;
		}
	}

	public void setSleepnessLevel(String sleepnLevel) {
		this.sleepnessLevel = sleepnLevel;
	}

	/*
	 * Defines the code to run for this task.
     */
	@Override
	public void run() {
		// Write to the log file
		appendLog("Chosen Action | " + "Random timing upper limit in secs | "
			+ "Random timing lower limit in secs | " + "Waiting timing in secs | "
			+ "Measurement Counter | " + "Random timing value | "
			+ "Timestamp for request action | " + "Timestamp for last action | "
			+ "Action counter | " + "Result");

		appendLog("Sleepness level : " + this.sleepnessLevel);

		makeATest();
	}

	private int getRandomSeconds() {
		int randomNum =
			RANDOM.nextInt(HIGHER_RANDOM_LIMIT - LOWER_RANDOM_LIMIT) + LOWER_RANDOM_LIMIT;
		return randomNum;
	}

	public static GestureActions getRandomAction() {
		return GestureActions.values()[ACTIONRANDOM.nextInt(8)+1];
	}

	public void stopMeasuring() {
		this.measuring = false;
	}

	public void fireActionFromActivity(GestureActions action) {
		if(waitForActions) {
			actualGesture = action;
			actionCounter++;
			lastAnswerTime = System.currentTimeMillis();
		}
	}

	public void clearActionMonitoring() {
		actualGesture = GestureActions.GAP;
		actionCounter = 0;
		lastAnswerTime = 0;
	}

	private void makeATest() {
		measurementCounter++;
		actionCounter = 0;

		final int actualWaintingValue = getRandomSeconds();
		randomTiming.postDelayed(new Runnable() {
			@Override
			public void run() {
				// Random an action
				final GestureActions actualAction = getRandomAction();

				commandRequestTime = System.currentTimeMillis();

				waitedGesture = actualAction;

				Log.d("MonitorThread","Gesture : Actual action" + actualAction.toString() );

				players.get(actualAction).setOnErrorListener(new MyErrorListener());
				players.get(actualAction).start();

				// Action has been asked, so we are waiting for actions now

				waitForActions = true;
				clearActionMonitoring();

				// Wait fix time

				waitForAction.postDelayed(new Runnable() {
					@Override
					public void run() {

						finalResultOfTest = "";

						// The test period is over, so we are not waiting now actions
						waitForActions = false;

						// Todo evaluate the results and save
						if (actualGesture == waitedGesture) {
							players.get(GestureActions.OK).setOnErrorListener(new MyErrorListener());
							players.get(GestureActions.OK).start();
							finalResultOfTest = "Success";
							Log.d("MonitorThread","Result : OK");
						} else {
							players.get(GestureActions.HIBA).setOnErrorListener(new MyErrorListener());
							players.get(GestureActions.HIBA).start();
							finalResultOfTest = "NOK";
							Log.d("MonitorThread","Result : NOK");
						}

						if(lastAnswerTime==0){
							lastAnswerTime = System.currentTimeMillis();
						}

						// TODO: save out data

						Log.d("Gesture Detection Step",
							"*******************************************");
						Log.d("Gesture Detection Step",
							"Chosen Action : " + actualAction.toString());
						Log.d("Gesture Detection Step",
							"Random timing upper limit in secs : " + HIGHER_RANDOM_LIMIT);
						Log.d("Gesture Detection Step",
							"Random timing lower limit in secs : " + LOWER_RANDOM_LIMIT);
						Log.d("Gesture Detection Step",
							"Waiting timing in secs : " + WAIT_FOR_ACTION_IN_SEC);
						Log.d("Gesture Detection Step",
							"Measurement Counter : " + measurementCounter);
						Log.d("Gesture Detection Step",
							"Random timing value : " + actualWaintingValue + " secs");
						Log.d("Gesture Detection Step",
							"Timestamp for request action : " + commandRequestTime);
						Log.d("Gesture Detection Step",
							"Timestamp for last action : " + lastAnswerTime);
						Log.d("Gesture Detection Step", "Action counter : " + actionCounter);
						Log.d("Gesture Detection Step", "Result : " + finalResultOfTest);

						// Write to the log file
						appendLog(actualAction.toString() + "|" + HIGHER_RANDOM_LIMIT + "|"
							+ LOWER_RANDOM_LIMIT + "|" + WAIT_FOR_ACTION_IN_SEC + "|"
							+ measurementCounter + "|" + actualWaintingValue + "|"
							+ commandRequestTime + "|" + lastAnswerTime + "|" + actionCounter + "|"
							+ finalResultOfTest + "|");

						actionCounter = 0;
						commandRequestTime = 0;
						lastAnswerTime = 0;

						if (measuring) {
							makeATest();
						}

					}

				}, WAIT_FOR_ACTION_IN_SEC * 1000);


			}

		}, (actualWaintingValue * 1000));
	}

	public void appendLog(String text) {

		File logFile = new File(
			Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + File.separator + "actions.log");

		if (!logFile.exists()) {
			try {
				logFile.createNewFile();
				Log.d("Gesture Detection Step", "Create new file!");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		try {
			//BufferedWriter for performance, true to set append to file flag
			BufferedWriter buf = new BufferedWriter(new FileWriter(logFile, true));
			buf.append(text);
			buf.newLine();
			buf.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {

		}
	}

}
