package com.example.pdr;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

/** Menu Principal */
public class SelectActivity extends Activity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_select);
	}

	/**
	 * Ouvre le gestionnaire de fichier lorsqu'on clique sur le choix
	 * "trace GPX"
	 */
	/*
	public void startGPX(View v) {
		
		Intent intent = new Intent(Intent.ACTION_PICK);
		intent.setType("*//*");

		// intent special pour les Samsung
		Intent sIntent = new Intent("com.sec.android.app.myfiles.PICK_DATA");
		sIntent.putExtra("CONTENT_TYPE", "*//*");
		sIntent.addCategory(Intent.CATEGORY_DEFAULT);

		Intent chooserIntent;
		if (getPackageManager().resolveActivity(sIntent, 0) != null) {
			// on a bien un gestionnaire de fichiers samsung
			chooserIntent = Intent.createChooser(sIntent, "Choisir un fichier");
			chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS,
					new Intent[] { intent });
		} else {
			chooserIntent = Intent.createChooser(intent, "Choisir un fichier");
		}

		try {
			startActivityForResult(chooserIntent, 100);
		} catch (android.content.ActivityNotFoundException ex) {
			Toast.makeText(getApplicationContext(),
					"Aucun explorateur de fichier n'a été trouvé.",
					Toast.LENGTH_SHORT).show();
		}
	} */
	
	/**
	 * Ouvre le gestionnaire de fichier lorsqu'on clique sur le choix
	 * "trace GPX"
	 * ACTION_OPEN_DOCUMENT necessite API 19
	 */
	public void startGPX(View v) {
		
		 Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
		    intent.addCategory(Intent.CATEGORY_OPENABLE);

		    intent.setType("*/*");

		    startActivityForResult(intent, 42);
	}

	/** Demarre le PDR */
	public void startPDR(View v) {
		Intent pdr = new Intent(this, MainActivity.class);
		startActivity(pdr);
	}

	
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		
		
		switch (requestCode) {
		// fichier choisi
		case 42:
			if (resultCode == RESULT_OK) {
		        Uri uri = null;
		        if (data != null) {
		            uri = data.getData();
		          //demarrage du tracer GPX avec le fichier choisi
					Intent i = new Intent(getApplicationContext(),
							GPXActivity.class);
					i.putExtra("uri", uri.toString());
					startActivity(i);
		        }
				 else {
					Log.d("error","Impossible de récupérer le chemin du fichier");
				}
			}
			break;
		}
	}

}
