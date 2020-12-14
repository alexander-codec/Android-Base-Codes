package com.kinemarts.jogodamemoria;

import android.app.Application;

import com.kinemarts.jogodamemoria.utils.FontLoader;

public class GameApplication extends Application {

	@Override
	public void onCreate() {
		super.onCreate();
		FontLoader.loadFonts(this);

	}
}
