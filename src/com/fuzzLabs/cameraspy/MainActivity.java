package com.fuzzLabs.cameraspy;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Iterator;
import java.util.List;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Camera;
import android.hardware.Camera.CameraInfo;
import android.hardware.Camera.PreviewCallback;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MainActivity extends Activity implements SurfaceHolder.Callback,
		Camera.PictureCallback {

	// Variabile
	Button seeCaptures;
	Button btnStart;
	Button btnSettings;
	TextView txtView_Results;
	SurfaceView cameraView;
	ImageView imageView;
	Camera camera;
	Camera.PictureCallback picCallBack = this;

	boolean isWorking = false;
	byte[] oldData = null;
	long counter = 0;
	int regionDifference;
	int imageCount = 1;

	String imagePath;
	File imageFile;
	Uri imageFileUri;
	
	String videoPath;
	File videoFile;
	Uri VideoFileUri;
	
	
	final static int REQUEST_VIDEO_CAPTURED = 1;

	// Intentul de pornire a aplicatiei implicite de captura
	Intent cameraIntent;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.new_layout);

		// Ascunde status-bar-ul pentru API > Android 4.1
		/*
		 * View decorView = getWindow().getDecorView(); // Hide the status bar.
		 * int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
		 * decorView.setSystemUiVisibility(uiOptions); // Remember that you
		 * should never show the action bar if the // status bar is hidden, so
		 * hide that too if necessary. ActionBar actionBar = getActionBar();
		 * actionBar.hide();
		 */

		// camera feed
		cameraView = (SurfaceView) findViewById(R.id.CameraView);
		SurfaceHolder surfaceHolder = cameraView.getHolder();
		surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
		surfaceHolder.addCallback(this);

		// output
		txtView_Results = (TextView) findViewById(R.id.textView_results);

		// seeCaptures button
		seeCaptures = (Button) findViewById(R.id.button_seeImages);

		// settings button
		btnSettings = (Button) findViewById(R.id.button_settings);
		btnSettings.setOnClickListener(settingsDefaultListener);

		// start button
		btnStart = (Button) findViewById(R.id.button_startus);
		btnStart.setOnClickListener(startDefaultListener);

		seeCaptures = (Button) findViewById(R.id.button_seeImages);
		seeCaptures.setOnClickListener(seeCapturesDefaultListener);

		// last captured image preview
		imageView = (ImageView) findViewById(R.id.ImageView);
		
		counter = 0;
		regionDifference = 0;
		isWorking = false;
	}

	View.OnClickListener seeCapturesDefaultListener = new View.OnClickListener() {

		// deschide Galeria pentru a putea accesa pozele facute
		@Override
		public void onClick(View v) {
			Intent viewImage = new Intent(Intent.ACTION_VIEW,
					Uri.parse("content://media/internal/images/media"));

			/*
			 * Intent viewImage = new Intent();
			 * viewImage.setAction(Intent.ACTION_VIEW);
			 * viewImage.setDataAndType(Uri.fromFile(new
			 * File(Environment.getExternalStorageDirectory().getPath())),
			 * "image/*");
			 */

			startActivity(viewImage);
		}
	};

	View.OnClickListener settingsDefaultListener = new View.OnClickListener() {

		// deschide un view nou unde putem sa selectam cateva optiuni
		@Override
		public void onClick(View v) {
			// call the Settings View and tweak stuff
			Intent settingsIntent = new Intent();
			settingsIntent.setClass(getApplicationContext(),
					SettingsActivity.class);
			startActivity(settingsIntent);
		}
	};

	View.OnClickListener startDefaultListener = new View.OnClickListener() {

		// porneste the whole thing setand daca variabila "isWorking"
		// este adevarata sau falsa.
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			isWorking = !isWorking;
			if (isWorking) {
				btnStart.setText("Stop");
			} else {
				btnStart.setText("Start");
				txtView_Results.setText("Not running ..");
				counter = 0;
				imageView.setImageResource(android.R.color.transparent);
			}
		}
	};

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int w, int h) {
		// locul unde se intampla toata magia prelucrarii
		camera.setPreviewCallback(new PreviewCallback() {

			@Override
			public void onPreviewFrame(byte[] data, Camera camera) {
				/*
				 * Log.d("onPreviewFrame-surfaceChanged", String.format(
				 * "Got %d bytes of camera data", data.length));
				 */

				// oldData este o variabila menita sa retina frame-ul trecut
				// pentru a putea face o comparatie intre frame-ul curent si
				// acesta
				if (isWorking) {
					if (oldData == null) {
						oldData = data;
					} else {
						regionDifference = 0;
						for (int i = 0; i < data.length; i++)
							if (oldData[i] != data[i])
								regionDifference++;

						// regionalDifference este variabila care numara cati
						// biti difera
						// implicit este setata sa recunoasca miscare daca 1/2
						// din bitii frame-ului curent difera in raport cu
						// frame-ul precedent.
						if (regionDifference > (int) data.length / 2) {
							Log.d("onPreviewFrame-surfaceChanged",
									"motion detected");
							// updateaza status-ul
							txtView_Results
									.setText("Oh wait .. MOTION DETECTED !!");
							counter++;

							// daca miscarea se intampla pe durata unui timp >
							// counter
							// alertam user-ul.
							if (counter > 10) {
								String number = "+40725453248";
								String message = "!!! Motion detected!!!";

								txtView_Results
										.setText("Go get the shotGun !!");

								// trimitem mesaj
								android.telephony.SmsManager.getDefault()
										.sendTextMessage(number, null, message,
												null, null);

								// facem si o poza, sa avem amintire
								camera.takePicture(null, null, picCallBack);
								counter = 0;
							}

						} else {
							counter = 0;
							Log.d("onPreviewFrame-surfaceChanged",
									"everything is fine");

							txtView_Results.setText("Everything seems fine ..");
						}

						// variabila oldData primeste mereu valoare frame-ului
						// curent,
						// inainte de schimbarea acestuia
						oldData = data;
						Log.d("counter", String.valueOf(counter));
					}
				}
			}
		});
	}

	public static final int LARGEST_WIDTH = 700;
	public static final int LARGEST_HEIGHT = 400;

	int bestWidth = 0;
	int bestHeight = 0;

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		// detectam cea mai mare rezolutie posibila a camerei
		// pe care o putem afisa in view-ul nostru
		camera = Camera.open(CameraInfo.CAMERA_FACING_BACK);

		try {
			camera.setPreviewDisplay(holder);
			Camera.Parameters parameters = camera.getParameters();
			List<Camera.Size> previewSizes = parameters
					.getSupportedPreviewSizes();

			if (previewSizes.size() > 0) {
				Iterator<Camera.Size> cei = previewSizes.iterator();
				while (cei.hasNext()) {
					Camera.Size aSize = cei.next();
					if (aSize.width > bestWidth && aSize.width <= LARGEST_WIDTH
							&& aSize.height > bestHeight
							&& aSize.height <= LARGEST_HEIGHT) {
						bestWidth = aSize.width;
						bestHeight = aSize.height;
					}
				}
				if (bestHeight != 0 && bestWidth != 0) {
					parameters.setPreviewSize(bestWidth, bestHeight);
					cameraView.setLayoutParams(new LinearLayout.LayoutParams(
							bestWidth, bestHeight));
				}
			}
			camera.setParameters(parameters);
		} catch (IOException e) {
			camera.release();
		}

		camera.startPreview();
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		// TODO Auto-generated method stub
		camera.stopPreview();
		camera.release();
	}

	@Override
	public void onPictureTaken(byte[] data, Camera camera) {

		// setam directorul unde salvam snapshot-urile
		imagePath = Environment.getExternalStorageDirectory().getPath()
				+ "/CameraSPY/temp" + String.valueOf(imageCount) + ".jpg";
		imageFile = new File(imagePath);
		imageFileUri = Uri.fromFile(imageFile);

		// Uri imageFileUri;
		// Bundle extras = callingIntent.getExtras();
		// imageFileUri = (Uri) extras.get("imageFileUri");

		try {
			OutputStream imageFileOS = getContentResolver().openOutputStream(
					imageFileUri);
			imageFileOS.write(data);
			imageFileOS.flush();
			imageFileOS.close();

			Log.d("onPictureTaken", "Go get the shotgun!");
		} catch (FileNotFoundException e) {
		} catch (IOException e) {
		}

		imageCount++;
		camera.startPreview();

		Bitmap displayedImage = BitmapFactory
				.decodeFile(imageFileUri.getPath());

		// afisam pe ecran ultima poza facuta
		imageView.setImageBitmap(Bitmap.createScaledBitmap(displayedImage,
				bestWidth, bestHeight, false));
	}
}
