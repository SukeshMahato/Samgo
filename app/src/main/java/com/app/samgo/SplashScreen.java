package com.app.samgo;

import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.widget.MediaController;
import android.widget.VideoView;

import com.app.database.SamgoSQLOpenHelper;
import com.app.services.CountryService;

public class SplashScreen extends Activity {

	protected Handler mHandler;
	private long delay = 6000;
	protected int i = 0;
	SamgoSQLOpenHelper db;
	
	private MediaController media_control;
	public static final String PREFS_NAME = "AOP_PREFS";
	public static final String LAUNCH_ID="LAUNCHED";
	private String getLaunchId="";
	public void getValue(Context context) {
		SharedPreferences settings;
		settings = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE); //1
		getLaunchId = settings.getString(LAUNCH_ID, null); //2
		}

	public void save(Context context,String id) {
		SharedPreferences settings;
		SharedPreferences.Editor editor;
		settings = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE); //1
		editor = settings.edit(); //2
		editor.putString(LAUNCH_ID, id); //3
		editor.commit();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_splash_screen);
		getValue(getApplicationContext());
		db = new SamgoSQLOpenHelper(getApplicationContext());
	try {
	if (getLaunchId == null) {
		//save(getApplicationContext(), "1");
		db.truncateTableForCountry();
		startService(new Intent(this, CountryService.class));
	}
	}catch (NullPointerException e){
		db.truncateTableForCountry();
		startService(new Intent(this, CountryService.class));
	}
		
		mute();
		
		VideoView video_view = (VideoView) findViewById(R.id.VideoView);
		 
	    Uri uri = Uri.parse("android.resource://" + getPackageName() + "/" +R.raw.video_1);
	 
	    media_control = new MediaController(SplashScreen.this);
	    video_view.setMediaController(media_control);
	 
	    video_view.setVideoURI(uri);
	    
	    video_view.start();
	    video_view.setMediaController(null);

		Timer timer = new Timer();
		timer.schedule(task, delay);
		
	}

	TimerTask task = new TimerTask() {
		@Override
		public void run() {

			Intent in = new Intent().setClass(SplashScreen.this, LoginActivity.class)
					.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(in);
			finish();
			//unmute();
		}
	};
	
	public void mute() {
		  AudioManager am = (AudioManager)getSystemService(Context.AUDIO_SERVICE);
		  am.setStreamMute(AudioManager.STREAM_MUSIC, true);
		}
	
	public void unmute() {
		  AudioManager am = (AudioManager)getSystemService(Context.AUDIO_SERVICE);
		  am.setStreamMute(AudioManager.STREAM_MUSIC, false);
		}	
	

}