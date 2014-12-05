package br.unb.cic.qrgame.domain;

import java.io.ByteArrayOutputStream;

import com.google.zxing.BinaryBitmap;
import com.google.zxing.FormatException;
import com.google.zxing.NotFoundException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.DetectorResult;
import com.google.zxing.qrcode.detector.Detector;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageFormat;
import android.graphics.Rect;
import android.graphics.YuvImage;

public class ScannerProcessor extends QRCode {
	
	public ScannerProcessor(Context context) {
		super(context);
	}
	
	/**
	 *  Converte a image scaneada para um bitmap que pode ser processado.
	 *  @param frame: adquire o frame atual que está na tela.
	 *  @param largura: adquire a largura do frame
	 *  @param altura: adquire a altura do frame
	 *  @author techoolblogs
	 *  Método e algoritmo obtido e modificado de "techoolblogs".
	 *  @Reference: http://tech.thecoolblogs.com/2013/02/get-bitmap-image-from-yuv-in-android.html#ixzz3Gd5TdULI
	 */
	public Bitmap bmpFromYUV(byte[] frame, int largura, int altura) {
		
        ByteArrayOutputStream byteAOutS = new ByteArrayOutputStream();
        YuvImage yuvimage = new YuvImage(frame, ImageFormat.NV21, largura, altura, null);
        yuvimage.compressToJpeg( new Rect(0, 0, largura, altura), 80, byteAOutS );
        BitmapFactory.Options bmpFOptions = new BitmapFactory.Options();
        bmpFOptions.inPreferredConfig = Bitmap.Config.ARGB_8888;
        return BitmapFactory.decodeByteArray(byteAOutS.toByteArray(), 0, byteAOutS.toByteArray().length, bmpFOptions);
                
	}
	
	public BitMatrix detector(BinaryBitmap bBmp){
		
		BitMatrix matriz = null;
		try {
			
			matriz = bBmp.getBlackMatrix();
			Detector detector = new Detector(matriz);
	        try {
				DetectorResult result = detector.detect();
				matriz = result.getBits();
	        } catch (NotFoundException eNotFound1) {
				// TODO Auto-generated catch block
			} catch (FormatException eFormatEx) {
				// TODO Auto-generated catch block
			}
	        
		} catch (NotFoundException eNotFound2) {
			// TODO: NotFoundException ocorre quando a imagem nao pode ser binarizada.
		}
		
		return matriz;
		
	}
	
}
