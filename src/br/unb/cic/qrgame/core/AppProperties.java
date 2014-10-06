package br.unb.cic.qrgame.core;

import java.io.IOException;
import java.util.Properties;

import android.content.Context;
import android.util.Log;

public enum AppProperties {
	INSTANCE;
	private static final String FILE = "app.properties";
	private Properties properties = new Properties();

	public String readProperty(Context context, String key) {
		try {
			properties.load(context.getAssets().open(FILE));
		} catch (IOException e) {
			Log.e("properties", e.toString());
			throw new RuntimeException("Error loading properties.");
		}

		return properties.getProperty(key);
	}
}