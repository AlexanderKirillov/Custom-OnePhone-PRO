package com.nowenui.customonephonepro;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AlertDialog;
import android.widget.ProgressBar;
import android.widget.Toast;

public class SplashActivity extends ActionBarActivity {
	private static final int ALERT_DIALOG2 = 2;
	boolean doubleBackToExitPressedOnce = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.splash_activity);
		getSupportActionBar().hide();

		//
		// Во избежании пиратства, чтобы полноценно запустить приложение- поменяйте нижеследующую строчку на немного другую!
		showDialog(ALERT_DIALOG2);
		//

	}

	@Override
	protected Dialog onCreateDialog(int id) {

		Dialog dialog;
		AlertDialog.Builder builder;
		switch (id) {
			case ALERT_DIALOG2:
				builder = new AlertDialog.Builder(this);
				builder.setTitle("Уведомления")
						.setMessage("Поздравляем! Вы успешно собрали приложение из исходников!")
						.setCancelable(false)
						.setNegativeButton("Ясно!", new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								Intent intent = new Intent(Intent.ACTION_MAIN);
								intent.addCategory(Intent.CATEGORY_HOME);
								intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
								startActivity(intent);
								finish();
								System.exit(0);
							}
						});

				dialog = builder.create();
				break;
			default:
				dialog = null;
		}
		return dialog;
	}

	public void MainActivity() {
		int SPLASH_TIME_OUT = 2500;

		((ProgressBar) findViewById(R.id.progressBar)).getIndeterminateDrawable()
				.setColorFilter(Color.parseColor("#FFFFFF"), android.graphics.PorterDuff.Mode.SRC_ATOP);
		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				Intent i = new Intent(SplashActivity.this, MainActivity.class);
				startActivity(i);
				finish();
			}
		}, SPLASH_TIME_OUT);
	}


	@Override
	public void onBackPressed() {
		if (doubleBackToExitPressedOnce) {
			Intent intent = new Intent(Intent.ACTION_MAIN);
			intent.addCategory(Intent.CATEGORY_HOME);
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);
			finish();
			System.exit(0);
			return;
		}

		this.doubleBackToExitPressedOnce = true;
		Toast.makeText(this, "Нажмите еще раз для выхода из приложения!", Toast.LENGTH_SHORT).show();

		new Handler().postDelayed(new Runnable() {

			@Override
			public void run() {
				doubleBackToExitPressedOnce = false;
			}
		}, 2000);
	}
}