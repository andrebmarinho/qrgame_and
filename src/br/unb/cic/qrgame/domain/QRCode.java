package br.unb.cic.qrgame.domain;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Environment;
import android.widget.Toast;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.NotFoundException;
import com.google.zxing.RGBLuminanceSource;
import com.google.zxing.Result;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.QRCodeWriter;

public class QRCode {

	private String user;
	private Context context;
	
	public QRCode(String user, Context context){
		
		this.user = user;
		this.context = context;
		
	}
	
	public void encodeCode(){
		
		QRCodeWriter encoder = new QRCodeWriter();
		int altura, largura;
		BitMatrix matriz;
		Bitmap bmp;
		
		try {
			
			//Codifica o nome do usuário em uma matriz do tipo QR_CODE
		    matriz = encoder.encode(this.user, BarcodeFormat.QR_CODE, 400, 400); 	
		    altura = matriz.getHeight();
			largura = matriz.getWidth();
			bmp = Bitmap.createBitmap(largura, altura, Bitmap.Config.ARGB_8888);
			
			//Gera um bitmap (ARG_8888 - 4bytes por pixel) a partir da matriz (i,j) da palavra codificada
		    for (int j = 0; j < largura; j++)
		        for (int i = 0; i < altura; i++)
		            bmp.setPixel(j, i, matriz.get(j,i) ? Color.BLACK : Color.WHITE);
		    
		    try{
		    	this.saveBmp(bmp);
		    } catch (FileNotFoundException eFile){
		    	Toast.makeText(context, "Erro! " + eFile.getMessage(), Toast.LENGTH_LONG).show();
		    } catch (IOException eIO){
		    	Toast.makeText(context, "Erro! " + eIO.getMessage(), Toast.LENGTH_LONG).show();
		    }
		    
		    
		} catch (WriterException eEncoder) {
						
			Toast.makeText(context, "Erro! " + eEncoder.getMessage(), Toast.LENGTH_LONG).show();
		    //TODO: tratar essa exceção
		    
		} catch (Exception otherError){
			
			Toast.makeText(context, "Erro! " + otherError.getMessage(), Toast.LENGTH_LONG).show();
		    //TODO: tratar essa exceção
			
		}
				
	}
	
	/** Salva a imagem do qrcode na memória interna do dispositivo
	 * @throws FileNotFoundException, IOException
	 */
	
	public void saveBmp(Bitmap bmp) throws FileNotFoundException, IOException {
		
		File sd = Environment.getExternalStorageDirectory();
		FileOutputStream saida = null;
		
		try {
			
			saida = new FileOutputStream(new File(sd, "QRcode.jpg"));
		    boolean salvo = bmp.compress(Bitmap.CompressFormat.JPEG, 100, saida);
		    if (salvo){
		    	Toast.makeText(context, "Arquivo QRcode.jpg salvo com sucesso!", Toast.LENGTH_LONG).show();
		    } else {
		    	Toast.makeText(context, "Não foi possível salvar o arquivo. Contate os desenvolvedores.", Toast.LENGTH_LONG).show();
		    }
		    
		} catch (FileNotFoundException e) {
			
			throw new FileNotFoundException("Erro ao salvar o arquivo! " + e.getMessage());
		    //TODO: tratar essa exceção
		    		    
		} 
		
	    if (saida != null) 
	    	try {
	    		
	    		saida.close();
		    
	    	} catch (IOException e) {
		    	
	    		throw new IOException("Erro ao salvar o arquivo! " + e.getMessage());
	    		//TODO: tratar essa exceção
		        
		    }
		
	}
	
	public String decodeCode(Bitmap bmp){
		
		int altura = bmp.getHeight();
		int largura = bmp.getWidth(); 
        int[] matriz = new int[altura * largura];
        Result qrCodeMsg = null;
        String erro = new String("Erro na decodificação do código. Tente novamente.");
        
        bmp.getPixels(matriz, 0, largura, 0, 0, largura, altura);
        bmp.recycle();
        RGBLuminanceSource ilumRGB = new RGBLuminanceSource(largura, altura, matriz);
        BinaryBitmap bBmp = new BinaryBitmap(new HybridBinarizer(ilumRGB));
				
		try {
			qrCodeMsg = new MultiFormatReader().decode(bBmp);
		} catch (NotFoundException eFile) {
			Toast.makeText(context, "Erro! " + eFile.getMessage(), Toast.LENGTH_LONG).show();
			//TODO: tratar essa exceção.
		}
		
		if( qrCodeMsg != null)
			return qrCodeMsg.getText();
		else
			return(erro);
		
	}
	
}
