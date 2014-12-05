package br.unb.cic.qrgame.domain;

import java.io.File;

import com.google.zxing.BinaryBitmap;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.RGBLuminanceSource;
import com.google.zxing.Result;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.HybridBinarizer;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Camera;
import android.widget.Toast;

public class Decodificador extends ScannerProcessor{
		
	public Decodificador(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	/**
	 * Converte uma imagem de um arquivo em um bitmap.
	 * A partir do endere�o da imagem, o m�todo decodifica uma imagem, se v�lida, em um bitmap.
	 * @param filePath: caminho da imagem no dispositivo
	 * @return bmp
	 */	
	public Bitmap imageToBmp(String filePath){
		
		Bitmap bmp = null;		
		File arquivo = new File(filePath);
		if(!arquivo.exists()) 
			Toast.makeText(context, "Arquivo n�o encontrado.", Toast.LENGTH_SHORT).show();
			
		bmp = BitmapFactory.decodeFile(filePath);	
		return bmp;
		
	}
		
	/**
	 * Decodifica um bitmap de um QR code em uma string.
	 * A partir do bitmap de um poss�vel QR code, o m�todo obt�m uma matriz bin�ria dele e decodifica o QR code em uma string.
	 * @param bmp
	 * @return
	 * @throws Exception 
	 */
	public String decodeCode(Bitmap bmp) throws Exception{
		
        Result qrCodeMsg = null;        
        BinaryBitmap bBmp = binaryBmpFromBmp(bmp); 
        				
		try {
			qrCodeMsg = new MultiFormatReader().decode(bBmp);
		} catch (Exception eFile) {
			throw new Exception("Erro ao decodificar! " + eFile.getMessage());
		}
		
		if( qrCodeMsg != null)
			return qrCodeMsg.getText();
		else
			return(ERRO);
		
	}
	
	/**	 
	 * Gerar um bitmap bin�rio, para a decodifica��o do QR code.
	 * @param bmp: recebe um bitmap
	 * @return bBmp: retorna um bitmap bin�rio
	 */
	public BinaryBitmap binaryBmpFromBmp(Bitmap bmp){
		
		int altura = bmp.getHeight();
		int largura = bmp.getWidth(); 
        int[] matriz = new int[altura * largura];
		bmp.getPixels(matriz, 0, largura, 0, 0, largura, altura);
        bmp.recycle();
        RGBLuminanceSource ilumRGB = new RGBLuminanceSource(largura, altura, matriz);
        BinaryBitmap bBmp = new BinaryBitmap(new HybridBinarizer(ilumRGB));    
        
        return bBmp;
		
	}
		
	/**
	 * Tenta decodificar o frame.
	 * @param bmp: tenta decodificar um arquivo bitmap.
	 */
	public boolean tryDecode(Bitmap bmp) {
		if( bmp != null ){
			
			String str = QRCode.ERRO;
			try {
				str = decodeCode(bmp);
			} catch (Exception e) {
				//TODO: Escrever exce��o em log.
			}  
			
			if(!str.equals(QRCode.ERRO)){
				
				//TODO: Tratar acerto de tiro.
				setQrText(str);
				return true;
				
			}
        					
		}
		return false;
	}

	public void processarFrame(Camera camera, byte[] frame) {
		int largura = camera.getParameters().getPreviewSize().width;
		int altura = camera.getParameters().getPreviewSize().height;
		
		Bitmap liveImg = bmpFromYUV(frame, largura, altura);		
		BitMatrix matriz = detector( binaryBmpFromBmp(liveImg) );
		if(matriz != null){
			Bitmap bmpmat = createQRCBmp( matriz, matriz.getWidth(), matriz.getHeight()  );
			if( tryDecode( bmpmat ) ){
				
				Toast.makeText(context, "Alvo atingido: " + getQrText() , Toast.LENGTH_SHORT).show();
	        		        	
			}
		}
		
	}
	
	
	
}
