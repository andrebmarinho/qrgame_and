package br.unb.cic.qrgame.ui;

import br.unb.cic.qrgame.domain.XMPPClient;
import br.unb.cic.qrgame.exception.LoginException;
import br.unb.cic.qrgame.exception.ServerException;
import br.unb.cic.qrgame.R;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class LoginActivity extends Activity {
	private TextField userField;
	private TextField passField;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);

		userField = (TextField) this.findViewById(R.id.textFieldUser);
		passField = (TextField) this.findViewById(R.id.textFieldPass);
		userField.setText("user01");
		passField.setText("user01");

		Button send = (Button) this.findViewById(R.id.buttonEnterLogin);
		send.setOnClickListener(new View.OnClickListener() {
			public void onClick(View view) {
				/*if ((userField.validator(LoginActivity.this, "\"Usuário\""))
						&& (passField
								.validator(LoginActivity.this, "\"Senha\""))) {
					XMPPClient client = new XMPPClient(LoginActivity.this);
					try {
						client.loginToServer(userField.getMsg(),
								passField.getMsg());
						Toast.makeText(LoginActivity.this,
								"Conectado com sucesso!", Toast.LENGTH_SHORT)
								.show();
					} catch (ServerException e) {
						Toast.makeText(LoginActivity.this, e.getMessage(),
								Toast.LENGTH_SHORT).show();
					} catch (LoginException e) {
						Toast.makeText(LoginActivity.this, e.getMessage(),
								Toast.LENGTH_SHORT).show();
					}
				}*/
				
				Intent qrActivity = new Intent(LoginActivity.this, QRActivity.class);
	            qrActivity.putExtra("createQRCode", true);
	            qrActivity.putExtra("scanQRCode", true);
	            qrActivity.putExtra("codigo", "br.unb.cic.qrgame.ui.testes.TESTANDO");
				startActivity(qrActivity);
				finish();
	            
			}
		});
	}
}