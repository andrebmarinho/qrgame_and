package br.unb.cic.qrgame.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

public class TextField extends EditText {
	public TextField(Context context, EditText text) {
		super(context);
	}

	public TextField(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public TextField(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public String getMsg() {
		return this.getText().toString();
	}

	public boolean validator(Context context, String textType) {
		if (this.getMsg().equals("")) {
			String msg = "Preencha o campo " + textType;
			Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
			Log.i("emptyField", textType + " field is empty");

			return false;
		}

		return true;
	}
}
