package br.unb.cic.qrgame.domain;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Environment;
import android.widget.Toast;

public class Codificador extends QRCode{

	public Codificador(String qrText, Context context) {
		super(qrText, context);
		// TODO Auto-generated constructor stub
	}

	/**
	 *  A partir de uma string, gera um QR code correspondente. 
	 *	A string passada para o objeto é codificada em uma matriz
	 *	do tipo QR code. A partir da matriz, é construído uma imagem bitmap do tipo ARG_8888.
	 *	Essa imagem é comprimida em jpeg e salva pelo método saveBmp().
	 *
	 */
	
	public void encodeCode(){
		
		QRCodeWriter encoder = new QRCodeWriter();
		BitMatrix matriz;
		Bitmap bmp;
		
		try {
			
			//Codifica o nome do usuário em uma matriz do tipo QR_CODE
		    matriz = encoder.encode(this.qrText, BarcodeFormat.QR_CODE, 400, 400);
			bmp = createQRCBmp(matriz, matriz.getWidth(), matriz.getHeight());
					    
		    try{		    	
		    	this.saveBmp(bmp);		    	
		    } catch (FileNotFoundException eFile){		    			    
		    	Toast.makeText(context, "Erro! " + eFile.getMessage(), Toast.LENGTH_SHORT).show();		  
		    } catch (IOException eIO){		    
		    	Toast.makeText(context, "Erro! " + eIO.getMessage(), Toast.LENGTH_SHORT).show();		   
		    }
		    		    
		} catch (WriterException eEncoder) {						
			Toast.makeText(context, "Erro! " + eEncoder.getMessage(), Toast.LENGTH_SHORT).show();
		} catch (Exception otherError){			
			Toast.makeText(context, "Erro! " + otherError.getMessage(), Toast.LENGTH_SHORT).show();
		}
				
	}
			
	/** 
	 *  Salva a imagem do qrcode na memória externa do dispositivo.
	 * 	A partir de um bitmap passado ao método, esse comprime a imagem para uma do tipo Jpeg
	 * 	ao passo que a salva na memória externa do dispositivo utilizado.   
	 * @param bmp: recebe um bitmap a ser salvo no dispositivo.
	 * @throws FileNotFoundException, IOException
	 * 		
	 */	
	public void saveBmp(Bitmap bmp) throws FileNotFoundException, IOException {
		
		File sd = Environment.getExternalStorageDirectory();
		FileOutputStream saida = null;
		
		try {
			
			saida = new FileOutputStream(new File(sd, "QRcode.jpg"));
		    
		    if ( bmp.compress(Bitmap.CompressFormat.JPEG, 100, saida )){
		    	Toast.makeText(context, "Arquivo QRcode.jpg salvo na memória externa com sucesso!", Toast.LENGTH_SHORT).show();
		    	saida.close();
		    } else {
		    	Toast.makeText(context, "Não foi possível salvar o arquivo. Contate os desenvolvedores.", Toast.LENGTH_SHORT).show();
		    }
		    
		} catch (FileNotFoundException e) {			
			throw new FileNotFoundException("Erro ao salvar o arquivo! " + e.getMessage());		    		    
		} 
				
	}
	
}
