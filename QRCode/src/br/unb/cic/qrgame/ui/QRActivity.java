package br.unb.cic.qrgame.ui;

import br.unb.cic.qrgame.R;
import br.unb.cic.qrgame.domain.QRCode;
import android.app.Activity;
//import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.widget.Toast;

public class QRActivity extends Activity {

	private boolean createQRCode = false; //Flag que determina se um qrcode deve ser criado ao chamar essa activity.
	private boolean scanQRCode = false; //Flag que determina se o jogador est� atirando em alguem ou n�o ao chamar essa activity.
	String qrText = null;
	//Sem par�metros: n�o cria o QRcode, nem scaneia nenhum QRcode. 	
		
	QRCode code; 	
	
	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_qr);
		
		Bundle parametros = getIntent().getExtras();
		
		if (parametros != null){
			
			this.createQRCode = parametros.getBoolean("createQRCode");
			this.qrText = parametros.getString("codigo");
			this.scanQRCode = parametros.getBoolean("scanQRCode");
			
		}
		
		if(createQRCode){
			
			code = new QRCode(qrText, getBaseContext());
			code.encodeCode();
		
		}		
		
		if(scanQRCode){
			
			/*Intent actScan = new Intent("com.google.zxing.client.android.SCAN"); //Inicia a atividade SCAN, fornecida pela API zxing
			actScan.putExtra("SCAN_MODE", "QR_CODE_MODE");
			startActivityForResult(actScan, 0); //requestCode: 0, Intent data: actScan
			*/
			code = new QRCode(getBaseContext());
			//code.getImageFromCamera();
			String file = Environment.getExternalStorageDirectory().getPath() + "/QRcode.jpg";
			Toast.makeText(this, "Decodificando qrcode gerado: " + code.decodeCode(code.imageToBmp(file)), Toast.LENGTH_SHORT).show();
			
		}
		
	}
	
	/*@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent actScan) {	    
	    if (requestCode == 0) {
	        if (resultCode == RESULT_OK) {
	        	
	        	String target = actScan.getStringExtra("SCAN_RESULT");
	        	Toast.makeText(QRActivity.this, target, Toast.LENGTH_LONG).show();

	        } else {
	        	
	        	
	        	
	        }
	    }
	}*/
	
}
