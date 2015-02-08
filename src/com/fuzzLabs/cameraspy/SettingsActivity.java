package com.fuzzLabs.cameraspy;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;

public class SettingsActivity extends Activity {

	Button btnBack;
	RadioGroup rgSensitivity;
	EditText number;
	EditText message;
	EditText fps;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.settings_layout);

		btnBack = (Button) findViewById(R.id.button_back);
		btnBack.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});

		RadioGroup rgSensitivity = (RadioGroup) findViewById(R.id.radioGroup1);
		rgSensitivity
				.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

					@Override
					public void onCheckedChanged(RadioGroup group, int checkedId) {
						// TODO Auto-generated method stub
						Intent goBack = new Intent(SettingsActivity.this,
								MainActivity.class);
						String result = "";

						if (checkedId == 0) {
							result = "low";
						} else if (checkedId == 1) {
							result = "medium";
						} else if (checkedId == 2) {
							result = "high";
						}

						goBack.putExtra("sensitivity", result);
					}
				});

		number = (EditText) findViewById(R.id.editText_phoneNumber);
		number.addTextChangedListener(new TextWatcher() {

			@Override
			public void afterTextChanged(Editable arg0) {
				// TODO Auto-generated method stub
				Intent goBack = new Intent(SettingsActivity.this,
						MainActivity.class);
				String result = "";

				result = number.getText().toString();
				goBack.putExtra("number", result);
			}

			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1,
					int arg2, int arg3) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onTextChanged(CharSequence arg0, int arg1, int arg2,
					int arg3) {
				// TODO Auto-generated method stub

			}
		});

		message = (EditText) findViewById(R.id.editText_message);
		message.addTextChangedListener(new TextWatcher() {

			@Override
			public void afterTextChanged(Editable arg0) {
				// TODO Auto-generated method stub
				Intent goBack = new Intent(SettingsActivity.this,
						MainActivity.class);
				String result = "";

				result = message.getText().toString();
				goBack.putExtra("message", result);
			}

			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1,
					int arg2, int arg3) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onTextChanged(CharSequence arg0, int arg1, int arg2,
					int arg3) {
				// TODO Auto-generated method stub

			}
		});

		fps = (EditText) findViewById(R.id.editText_fpsAlert);
		fps.addTextChangedListener(new TextWatcher() {

			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				Intent goBack = new Intent(SettingsActivity.this,
						MainActivity.class);
				String result = "";

				result = fps.getText().toString();
				goBack.putExtra("fps", result);
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// TODO Auto-generated method stub

			}

		});
	}
}
