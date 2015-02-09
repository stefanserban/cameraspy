package com.fuzzLabs.cameraspy;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

public class SettingsActivity extends Activity {

	Button btnBack;
	Button btnSave;
	static RadioGroup rgSensitivity;
	static EditText number;
	static EditText message;
	static EditText fps;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.settings_layout);

		// Sensitivitate = cat % din imagine trebuie sa difere pentru a detecta
		// miscare
		rgSensitivity = (RadioGroup) findViewById(R.id.radioGroup1);

		// Casuta numar de telefon
		number = (EditText) findViewById(R.id.editText_phoneNumber);

		// Casuta text mesaj
		message = (EditText) findViewById(R.id.editText_message);

		// Casuta numar de frame-uri ce trebuie sa fie diferite pentru a
		// declansa alarma
		fps = (EditText) findViewById(R.id.editText_fpsAlert);

		btnBack = (Button) findViewById(R.id.button_back);
		btnBack.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				Intent extras = new Intent();

				extras.putExtra("fps", String.valueOf(fps.getText()));
				extras.putExtra("numar", number.getText().toString());
				extras.putExtra("mesaj", message.getText().toString());
				
				RadioButton checkedButton = (RadioButton) rgSensitivity.findViewById(rgSensitivity.getCheckedRadioButtonId());
				int checkedIndex = rgSensitivity.indexOfChild(checkedButton);
				extras.putExtra("sensibilitate", String.valueOf(checkedIndex));
				
				setResult(RESULT_OK, extras);
				finish();
			}
		});

	}
}
