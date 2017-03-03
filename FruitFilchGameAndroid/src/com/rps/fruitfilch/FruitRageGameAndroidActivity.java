package com.rps.fruitfilch;

import android.os.Bundle;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;

public class FruitRageGameAndroidActivity extends AndroidApplication {
    /** Called when the activity is first created. */	
	GameDataSource dataSource;	
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);        
        /*PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
        PowerManager.WakeLock wl = pm.newWakeLock(PowerManager.SCREEN_BRIGHT_WAKE_LOCK, null);*/
        AndroidApplicationConfiguration cfg = new AndroidApplicationConfiguration();
        dataSource = new GameDataSource(this.getApplicationContext());
        //Log.i("All is well!", "Oh Yes all is well!!");
        initialize(new FruitRageGame(dataSource), cfg);
    }/*
    
    @Override
    public void onDestroy() {
    	
    }*/
}