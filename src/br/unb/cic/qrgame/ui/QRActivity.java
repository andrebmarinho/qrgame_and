package br.unb.cic.qrgame.ui;

import br.unb.cic.qrgame.R;
import br.unb.cic.qrgame.domain.QRCode;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

public class QRActivity extends Activity {

	private boolean createQRCode = false; //Flag que determina se um qrcode deve ser criado ao chamar essa activity.
	private boolean scanQRCode = false; //Flag que determina se o jogador está atirando em alguem ou não ao chamar essa activity.
	String qrText = null;
	//Sem parâmetros: não cria o QRCode, nem scaneia nenhum qrcode. 	
		
	QRCode code; 	
	
	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_qr);
		
		Bundle parametros = getIntent().getExtras();
		
		if (parametros != null){
			
			this.createQRCode = parametros.getBoolean("createQRCode");
			this.qrText = new String(parametros.getString("codigo"));
			this.scanQRCode = parametros.getBoolean("scanQRCode");
			
		}
		
		if(createQRCode){
			
			code = new QRCode(qrText, getBaseContext());
			code.encodeCode();
		
		}		
		
		if(scanQRCode){
			
			Intent actScan = new Intent("com.google.zxing.client.android.SCAN"); //Inicia a atividade SCAN, fornecida pela API zxing
			actScan.putExtra("SCAN_MODE", "QR_CODE_MODE");
			try{
				startActivityForResult(actScan, 0); //requestCode: 0, Intent data: actScan
				//startActivity(actScan);
			} catch (Exception e){
				
				Toast.makeText(QRActivity.this, "Erro: " +e.getMessage(), Toast.LENGTH_LONG).show();
				
			}					
		}
		
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent actScan) {	    
	    if (requestCode == 0) {
	        if (resultCode == RESULT_OK) {
	        	String target = actScan.getStringExtra("SCAN_RESULT");
	        	Toast.makeText(QRActivity.this, target, Toast.LENGTH_LONG).show();

	        }
	    }
	}
	
}
