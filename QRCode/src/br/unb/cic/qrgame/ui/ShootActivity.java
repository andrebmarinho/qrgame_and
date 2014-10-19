package br.unb.cic.qrgame.ui;

import java.io.IOException;
import java.util.List;

import br.unb.cic.qrgame.R;
import br.unb.cic.qrgame.domain.QRCode;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.PixelFormat;
import android.hardware.Camera;
import android.hardware.Camera.Size;
import android.os.Bundle;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.Toast;

public class ShootActivity extends Activity implements SurfaceHolder.Callback{
			
	QRCode code;
	Camera camera;
	SurfaceView surfV;
	SurfaceView surfVTransp;
	Canvas canvas;
	
	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_shoot);
		
		this.scanQRCode();
		
	}
	
	//TODO: implementar o tiro.
	
	/**
	 * @Brief: por meio do scaneamento, obt�m um bitmap (de poss�vel QR code) que � posteriormente decodificado em uma string.
	 * 
	 */
	protected void scanQRCode(){
		
		code = new QRCode(getBaseContext());
		//Bitmap bmp = null;
		//bmp = this.startScan();
		//String file = Environment.getExternalStorageDirectory().getPath() + "/QRcode.jpg";
		//Toast.makeText(this, "Decodificando qrcode gerado: " + code.decodeCode(code.imageToBmp(file)), Toast.LENGTH_SHORT).show();
		//Toast.makeText(this, "Decodificando qrcode gerado: " + code.decodeCode(bmp), Toast.LENGTH_SHORT).show();
		this.startScan();	
		
	}
	
	/**
	 * 
	 * @Brief: inicia o scaneamento, pela c�mera, de um poss�vel QRCode.
	 * @return bmp
	 * 
	 */
	public Bitmap startScan(){
		
		Bitmap bmp = null;
		
		//SV de "baixo", onde aparece a imagem da c�mera.
		surfV = (SurfaceView)findViewById(R.id.scannerView);
		surfV.getHolder().addCallback(this);
		//surfV.getHolder().setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS); 
		
		//SV onde ser� desenhado o quadrado na tela (sview de desenho).
		surfVTransp = (SurfaceView)findViewById(R.id.drawningSurfView);
		surfVTransp.getHolder().setFormat(PixelFormat.TRANSPARENT);
		surfVTransp.getHolder().addCallback(this);
		surfVTransp.setZOrderOnTop(true);
		
//		canvas = surfVTransp.getHolder().lockCanvas();
//		if (canvas == null)
//	    	Toast.makeText(ShootActivity.this, "Erro! CANVAS NULL" , Toast.LENGTH_SHORT).show();
//	    drawScanRect();
		
        camera = Camera.open();
        //TODO: Testar se o frame atual do preview � um qrcode (v�lido) e autenticar o encontro em tempo real.
		
		return bmp;
		
	}

	/**
	 * @Brief:
	 * 
	 */
	@Override
	public void surfaceChanged(SurfaceHolder holder, int formato, int largura, int altura) {
		
		Camera.Parameters parametros = camera.getParameters();
        List<Size> dimensoes = parametros.getSupportedPreviewSizes();
        largura = dimensoes.get(0).width;
        altura = dimensoes.get(0).height;
        parametros.setPreviewSize(largura, altura);
        camera.setParameters(parametros);
        camera.startPreview();
		
	}
	
	/**
	 * 
	 */
	/*private void drawScanRect() {
		
		Paint paint = new Paint();
	    paint.setColor(Color.GREEN);
	    paint.setStyle(Paint.Style.FILL_AND_STROKE);
	    paint.setStrokeWidth(10);
	    
	    
	    //Quadrado de 100x100 pixels formado a partir do centro da view de desenho.
	    float x0 = surfVTransp.getX();
	    float y0 = surfVTransp.getY();
	    float largura = surfVTransp.getWidth();
	    float altura = surfVTransp.getHeight();
	    float centroX = x0 + largura / 2;
	    float centroY = y0 + altura / 2;
	    float esquerda = centroX - 100;
	    float cima = centroY - 100;
	    float direita = centroX + 100;
	    float baixo = centroY + 100; 
	    
	    try{
	    	
	    	canvas.drawRect(esquerda, cima, direita, baixo, paint);
	    	surfVTransp.getHolder().unlockCanvasAndPost(canvas);
	    	
	    } catch (Exception e){
		    
	    	Toast.makeText(ShootActivity.this, "Erro! " + e.getMessage(), Toast.LENGTH_SHORT).show();
	   
	    }
	}*/
	
	/**
	 * @Brief: Cria��o da surface view.
	 * 
	 */
	@Override
	public void surfaceCreated(SurfaceHolder holder) {
				
		try {
			camera.setPreviewDisplay(surfV.getHolder());
		} catch (IOException e) {
			Toast.makeText(this, "Erro na visualiza��o da c�mera! " + e.getMessage(), Toast.LENGTH_SHORT).show();
		}		
		
	}
	
	/**
	 * 
	 */
	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
	}
	
}
	
	

