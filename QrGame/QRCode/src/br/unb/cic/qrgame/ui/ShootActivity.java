package br.unb.cic.qrgame.ui;

import br.unb.cic.qrgame.R;
import br.unb.cic.qrgame.domain.CameraControl;
import android.app.Activity;
import android.hardware.Camera;
import android.hardware.Camera.PreviewCallback;
import android.os.Bundle;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.Toast;

public class ShootActivity extends Activity implements SurfaceHolder.Callback, Camera.PreviewCallback{
			
	Camera camera;
	SurfaceView surfV;
	CameraControl cControl;
	boolean Teste = false; //Indica se o programa de testes está rodando essa activity.
	
	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_shoot);
		
		if(getIntent().getExtras() != null)
			Teste = getIntent().getExtras().getBoolean("Teste");
		
		surfV = (SurfaceView)findViewById(R.id.scannerView);
		surfV.getHolder().addCallback(this);
		cControl = new CameraControl( getBaseContext() );
		cControl.iniciarCamera();
		camera = cControl.getCamera();
		//camera = Camera.open();
		
	}
	
	
	/**
	 * Processa o frame atual para tentar decodificá-lo como um QR code.
	 * @param frame: frame atual exibido na surface.
	 * @param camera: camera utilizada para capturar esses frames.
	 */
	@Override
	public void onPreviewFrame(byte[] frame, Camera camera) {
		cControl.onPreviewFrame(frame, camera);			
	}

	/**
	 * 
	 * @param holder
	 * @param formato: recebe o formato do tipo de imagem da camera
	 * @param largura: largura da surface
	 * @param altura: altura da surface
	 * 
	 */
	@Override
	public void surfaceChanged(SurfaceHolder holder, int formato, int largura, int altura) {
		if(Teste) this.finish();
		cControl.surfaceChanged(holder, formato, largura, altura);
		try{
        	camera.setPreviewCallback((PreviewCallback) ShootActivity.this);
        	camera.startPreview();
        } catch (Exception eCam){
        	Toast.makeText(ShootActivity.this, "Erro ao preparar a câmera!2 " + eCam.getMessage() , Toast.LENGTH_SHORT).show();
	    }
	}
			
	/**
	 * Criação da surface view.
	 * 
	 */
	@Override
	public void surfaceCreated(SurfaceHolder holder) {		
		cControl.criarSurfaceV(surfV, holder);		
	}
	
	/**
	 * 
	 */
	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		cControl.surfaceDestroyed(holder);
	}
	
}
	


