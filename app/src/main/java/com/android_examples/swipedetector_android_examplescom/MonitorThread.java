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
import java.util.Random;

/**
 * Created by andrastoth on 2018. 03. 05..
 */

public class MonitorThread implements Runnable {

	int actualRandomTiming = 0;
	int actualRandomExcercise = 0;
	private static final int LOWER_RANDOM_LIMIT = 10;
	private static final int HIGHER_RANDOM_LIMIT = 20;
	private static final int WAIT_FOR_ACTION_IN_SEC = 5;
	private static final Random RANDOM = new Random();
	private static final Random ACTIONRANDOM = new Random();
	Handler randomTiming = new Handler();
	Handler waitForAction = new Handler();
	MediaPlayer mpRes = null;
	Context mContext = null;
	private int measurementCounter = 0;
	private int actionCounter = 0;
	boolean measuring = true;
	private GestureActions actualGesture = GestureActions.BALROLJOBBRA;
	private GestureActions waitedGesture = GestureActions.BALROLJOBBRA;

	long commandRequestTime = 0;
	long lastAnswerTime = 0;
	String finalResultOfTest = "";

	public enum GestureActions {
		BALROLJOBBRA, FENTROLLE, JOBBROLBALRA, LENTROLFEL, KERESZTBEBALRAFEL, KERESZTBEBALRALE, KERESZTBEJOBBRAFEL, KERESZTBEJOBBRALE
	}

	public void setContext(Context c) {
		this.mContext = c;
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
		makeATest();
	}

	private int getRandomSeconds() {
		int randomNum =
			RANDOM.nextInt(HIGHER_RANDOM_LIMIT - LOWER_RANDOM_LIMIT) + LOWER_RANDOM_LIMIT;
		return randomNum;
	}

	public static GestureActions getRandomAction() {
		return GestureActions.values()[ACTIONRANDOM.nextInt(GestureActions.values().length)];
	}

	public void stopMeasuring() {
		this.measuring = false;
	}

	public void fireActionFromActivity(GestureActions action) {
		actualGesture = action;
		actionCounter++;
		lastAnswerTime = System.currentTimeMillis();
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

				// Say command
				switch (actualAction) {
					case BALROLJOBBRA:
						mpRes = MediaPlayer.create(mContext, R.raw.bj);
						mpRes.start();
						break;
					case FENTROLLE:
						mpRes = MediaPlayer.create(mContext, R.raw.fl);
						mpRes.start();
						break;
					case JOBBROLBALRA:
						mpRes = MediaPlayer.create(mContext, R.raw.jb);
						mpRes.start();
						break;
					case LENTROLFEL:
						mpRes = MediaPlayer.create(mContext, R.raw.lf);
						mpRes.start();
						break;
					case KERESZTBEBALRAFEL:
						mpRes = MediaPlayer.create(mContext, R.raw.kbf);
						mpRes.start();
						break;
					case KERESZTBEBALRALE:
						mpRes = MediaPlayer.create(mContext, R.raw.kbl);
						mpRes.start();
						break;
					case KERESZTBEJOBBRAFEL:
						mpRes = MediaPlayer.create(mContext, R.raw.kjf);
						mpRes.start();
						break;
					case KERESZTBEJOBBRALE:
						mpRes = MediaPlayer.create(mContext, R.raw.kjl);
						mpRes.start();
						break;


				}
				// Wait fix time

				waitForAction.postDelayed(new Runnable() {
					@Override
					public void run() {

						finalResultOfTest = "";

						// Todo evaluate the results and save
						if (actualGesture == waitedGesture) {
							mpRes = MediaPlayer.create(mContext, R.raw.ok);
							mpRes.start();
							finalResultOfTest = "Success";
						} else {
							mpRes = MediaPlayer.create(mContext, R.raw.hiba);
							mpRes.start();
							finalResultOfTest = "NOK";
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
