package br.unb.cic.qrgame.ui;

import java.io.IOException;
import java.util.List;

import com.google.zxing.common.BitMatrix;

import br.unb.cic.qrgame.R;
import br.unb.cic.qrgame.domain.Decodificador;
import br.unb.cic.qrgame.domain.QRCode;
import android.app.Activity;
import android.graphics.Bitmap;
import android.hardware.Camera;
import android.hardware.Camera.PreviewCallback;
import android.hardware.Camera.Size;
import android.os.Bundle;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.Toast;

public class ShootActivity extends Activity implements SurfaceHolder.Callback, Camera.PreviewCallback{
			
	QRCode cCode;
	Decodificador decode;
	Camera camera;
	SurfaceView surfV;
	Bitmap bmp = null;
	boolean Teste = false; //Indica se o programa de testes está rodando essa activity.
	
	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_shoot);
		
		if(getIntent().getExtras() != null)
			Teste = getIntent().getExtras().getBoolean("Teste");
		
		decode = new Decodificador( getBaseContext() );
		cCode = new QRCode( getBaseContext() );
		camera = Camera.open();
		this.scanQRCode();
		
	}
	
	//TODO: ADICIONAR camera.release() no método, para que outros aplicativos possam ter acesso a camera.
		
	/**
	 * Por meio do scaneamento, obtém um bitmap (de possível QR code) que é posteriormente decodificado em uma string.
	 * 
	 */
	protected void scanQRCode(){
		
		surfV = (SurfaceView)findViewById(R.id.scannerView);
		surfV.getHolder().addCallback(this);
		
	}
	
	/**
	 * Processa o frame atual para tentar decodificá-lo como um QR code.
	 * @param frame: frame atual exibido na surface.
	 * @param camera: camera utilizada para capturar esses frames.
	 */
	@Override
	public void onPreviewFrame(byte[] frame, Camera camera) {
		
		int largura = camera.getParameters().getPreviewSize().width;
		int altura = camera.getParameters().getPreviewSize().height;
		Bitmap liveImg = decode.bmpFromYUV(frame, largura, altura);		
		BitMatrix matriz = decode.detector( decode.binaryBmpFromBmp(liveImg) );
		if(matriz != null){
			Bitmap bmpmat = cCode.createQRCBmp( matriz, matriz.getWidth(), matriz.getHeight()  );
			if( decode.tryDecode( bmpmat ) ){
				
				Toast.makeText(ShootActivity.this, "Alvo atingido: " + decode.qrText , Toast.LENGTH_SHORT).show();
	        	camera.stopPreview();
	        	
			}
		}
		
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
		Camera.Parameters parametros = camera.getParameters();
        List<Size> dimensoes = parametros.getSupportedPreviewSizes();
        largura = dimensoes.get(0).width;
        altura = dimensoes.get(0).height;
        parametros.setPreviewSize(largura, altura);
        parametros.set("orientation", "portrait");
        try{
        	camera.setParameters(parametros);
        	camera.setPreviewDisplay(holder);
        	camera.setPreviewCallback((PreviewCallback) ShootActivity.this);
        	camera.setDisplayOrientation(90);
        	camera.startPreview();
        } catch (Exception eCam){
        	//TODO:        	
        }
	}
			
	/**
	 * Criação da surface view.
	 * 
	 */
	@Override
	public void surfaceCreated(SurfaceHolder holder) {
				
		try {
			camera.setPreviewDisplay(surfV.getHolder());
		} catch (IOException e) {
			Toast.makeText(this, "Erro na visualização da câmera! " + e.getMessage(), Toast.LENGTH_SHORT).show();
		}		
		
	}
	
	/**
	 * 
	 */
	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		camera.release();
		camera = null;
	}
	
}
	


