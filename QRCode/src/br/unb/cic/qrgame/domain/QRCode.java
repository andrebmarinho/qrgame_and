package br.unb.cic.qrgame.domain;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Environment;
import android.widget.Toast;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.RGBLuminanceSource;
import com.google.zxing.Result;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.WriterException;
import com.google.zxing.qrcode.QRCodeWriter;

public class QRCode {

	private String qrText;
	private Context context;
	
	/**
	 * @Brief: Construtor de quando n�o � necess�rio gerar um QRCode, apenas escanear algum.
	 * @param context
	 */
	public QRCode(Context context){
		
		this.qrText = "";
		this.context = context;
		
	}
	
	/**
	 * @Brief: Construtor de quando � necess�rio gerar um QRCode.
	 * @param qrText
	 * @param context
	 */
	public QRCode(String qrText, Context context){
		
		this.qrText = qrText;
		this.context = context;
		
	}
	
	/**
	 *	@Brief: A partir de uma string, gera um QR code correspondente. 
	 *		A string passada para o objeto � codificada em uma matriz
	 *	do tipo QR code. A partir da matriz, � constru�do uma imagem bitmap do tipo ARG_8888.
	 *	Essa imagem � comprimida em jpeg e salva pelo m�todo saveBmp().
	 *
	 */
	
	public void encodeCode(){
		
		QRCodeWriter encoder = new QRCodeWriter();
		int altura, largura;
		BitMatrix matriz;
		Bitmap bmp;
		
		try {
			
			//Codifica o nome do usu�rio em uma matriz do tipo QR_CODE
		    matriz = encoder.encode(this.qrText, BarcodeFormat.QR_CODE, 400, 400); 	
		    altura = matriz.getHeight();
			largura = matriz.getWidth();
			bmp = createQRCBmp(matriz, largura, altura);
					    
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
	 * @Brief: a partir da matriz recebida, um bitmap de altura "altura" e largura "largura" � gerado.
	 * @param matriz
	 * @param largura
	 * @param altura
	 * @return
	 */
	protected Bitmap createQRCBmp(BitMatrix matriz, int largura, int altura){
		
		Bitmap bmp = Bitmap.createBitmap(largura, altura, Bitmap.Config.ARGB_8888);
		
		//Gera um bitmap (ARG_8888 - 4 bytes por pixel) a partir da matriz (i,j) da palavra codificada
	    for (int j = 0; j < largura; j++)
	        for (int i = 0; i < altura; i++)
	            bmp.setPixel(j, i, matriz.get(j,i) ? Color.BLACK : Color.WHITE);
	    
	    return bmp;
		
	}
	
	/** 
	 * @Brief: Salva a imagem do qrcode na mem�ria externa do dispositivo.
	 * 		A partir de um bitmap passado ao m�todo, esse comprime a imagem para uma do tipo Jpeg
	 * 	ao passo que a salva na mem�ria externa do dispositivo utilizado.   
	 * 
	 * @throws FileNotFoundException, IOException
	 * 		
	 */
	
	public void saveBmp(Bitmap bmp) throws FileNotFoundException, IOException {
		
		File sd = Environment.getExternalStorageDirectory();
		FileOutputStream saida = null;
		
		try {
			
			saida = new FileOutputStream(new File(sd, "QRcode.jpg"));
		    boolean salvo = bmp.compress(Bitmap.CompressFormat.JPEG, 100, saida);
		    if (salvo){
		    	Toast.makeText(context, "Arquivo QRcode.jpg salvo com sucesso!", Toast.LENGTH_SHORT).show();
		    } else {
		    	Toast.makeText(context, "N�o foi poss�vel salvar o arquivo. Contate os desenvolvedores.", Toast.LENGTH_SHORT).show();
		    }
		    
		} catch (FileNotFoundException e) {
			
			throw new FileNotFoundException("Erro ao salvar o arquivo! " + e.getMessage());
		    		    
		} 
		
	    if (saida != null) 
	    	try {
	    		
	    		saida.close();
		    
	    	} catch (IOException e) {
		    	
	    		throw new IOException("Erro ao salvar o arquivo! " + e.getMessage());
		        
		    }
		
	}

	/**
	 * @Brief: converte uma imagem em um bitmap
	 * 	A partir do endere�o da imagem, o m�todo decodifica uma imagem, se v�lida, em um bitmap.
	 * @param filePath
	 * @return bmp
	 */	
	public Bitmap imageToBmp(String filePath){
		
		Bitmap bmp = null;
		
		File arquivo = new File(filePath);
			if(!arquivo.exists()) 
				Toast.makeText(context, "Arquivo n�o encontrado.", Toast.LENGTH_SHORT).show();
			
		try{
			
			bmp = BitmapFactory.decodeFile(filePath);
			
		} catch (Exception eFile) {
			
			//TODO: tratar essa exce��o
		    		    
		} 
		
		return bmp;
		
	}
	
	/**
	 *	@Brief: decodifica um bitmap de um QR code em uma string.
	 *		A partir do bitmap de um poss�vel QR code, o m�todo obt�m uma matriz bin�ria dele e 
	 *	decodifica o QR code em uma string.
	 *
	 */
	
	/**
	 * 	@Brief: decodifica um bitmap de um QR code em uma string.
	 *	A partir do bitmap de um poss�vel QR code, o m�todo obt�m uma matriz bin�ria dele e decodifica o QR code em uma string.
	 * @param bmp
	 * @return
	 */
	public String decodeCode(Bitmap bmp){
		
		
        Result qrCodeMsg = null;
        String erro = new String("Erro na decodifica��o do c�digo. Tente novamente.");
        BinaryBitmap bBmp = binaryBmpFromBmp(bmp); 
        
				
		try {
			qrCodeMsg = new MultiFormatReader().decode(bBmp);
		} catch (Exception eFile) {
			Toast.makeText(context, "Erro! " + eFile.getMessage(), Toast.LENGTH_SHORT).show();
			//TODO: tratar essa exce��o.
		}
		
		if( qrCodeMsg != null)
			return qrCodeMsg.getText();
		else
			return(erro);
		
	}
	
	/**
	 * 
	 * @Brief: Gerar um bitmap bin�rio, para a decodifica��o do QR code.
	 * @param bmp
	 * @return bBmp
	 */
	protected BinaryBitmap binaryBmpFromBmp(Bitmap bmp){
		
		int altura = bmp.getHeight();
		int largura = bmp.getWidth(); 
        int[] matriz = new int[altura * largura];
		bmp.getPixels(matriz, 0, largura, 0, 0, largura, altura);
        bmp.recycle();
        RGBLuminanceSource ilumRGB = new RGBLuminanceSource(largura, altura, matriz);
        BinaryBitmap bBmp = new BinaryBitmap(new HybridBinarizer(ilumRGB));
        
        return bBmp;
		
	}
	
}
