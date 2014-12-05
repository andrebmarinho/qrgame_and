package br.unb.cic.qrgame.domain;

import java.io.IOException;
import java.util.List;

import android.content.Context;
import android.hardware.Camera;
import android.hardware.Camera.Size;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.Toast;

public class CameraControl {
	
	Camera camera;
	Context context;
	Decodificador decode;
	SurfaceView surfV;
	
	public CameraControl(Context context){
		this.context = context;		
	}
	
	public boolean iniciarCamera() {
	    	  
	    try {
	        liberarCamera();
	        camera = Camera.open();
	        return (camera != null);
	    } catch (Exception e) {
	       Toast.makeText(context, "Erro ao abrir a câmera! " + e.getMessage(), Toast.LENGTH_SHORT).show();
	    }

	    return false;    
	}

	public void liberarCamera() {
	    if (camera != null) {
	        camera.release();
	        camera = null;
	    }
	}
	
	public void surfaceDestroyed(SurfaceHolder holder) {
	    if (camera != null) {
	        camera.stopPreview();
	    }
	}

	public void stopPreviewAndFreeCamera() {

	    if (camera != null) {
	        camera.stopPreview();
	        camera.release();	    
	        camera = null;
	    }
	}

	public void onPreviewFrame(byte[] data, Camera camera) {
		decode = new Decodificador(context);
		decode.processarFrame(camera, data);		
	}

	public void criarSurfaceV(SurfaceView surfV, SurfaceHolder holder){
		this.surfV = surfV;
		surfaceCreated(holder);		
	}
	
	public void surfaceCreated(SurfaceHolder holder) {
		try {
			camera.setPreviewDisplay(surfV.getHolder());
		} catch (IOException e) {
			Toast.makeText(context, "Erro na visualização da câmera! " + e.getMessage(), Toast.LENGTH_SHORT).show();
		}		
	}

	public void surfaceChanged(SurfaceHolder holder, int format, int largura, int altura) {
		
		Camera.Parameters parametros = camera.getParameters();
        List<Size> dimensoes = parametros.getSupportedPreviewSizes();
        largura = dimensoes.get(0).width;
        altura = dimensoes.get(0).height;
        parametros.setPreviewSize(largura, altura);
        parametros.set("orientation", "portrait");
        try{
        	camera.setParameters(parametros);
        	camera.setDisplayOrientation(90);
        } catch (Exception eCam){
        	Toast.makeText(context, "Erro ao preparar a câmera! " + eCam.getMessage() , Toast.LENGTH_LONG).show();
	    }
		
	}

	public Camera getCamera() {		
		return camera;
	}
	
}
