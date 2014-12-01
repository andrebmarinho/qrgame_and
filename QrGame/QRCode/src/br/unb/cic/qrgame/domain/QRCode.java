package br.unb.cic.qrgame.domain;

import com.google.zxing.common.BitMatrix;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;

public class QRCode {

	public String qrText;
	protected Context context;
	public static final String ERRO = new String("Erro na decodificação do código. Tente novamente.");
	
	/**
	 * Construtor de quando não é necessário gerar um QRCode, apenas escanear algum (decodificador).
	 * @param context: contexto ativo do aplicativo.
	 */
	public QRCode(Context context){
		
		this.qrText = "";
		this.context = context;
		
	}
	
	/**
	 * Construtor de quando é necessário gerar um QRCode.
	 * @param qrText: texto a ser codificado
	 * @param context: contexto ativo do aplicativo.
	 */
	public QRCode(String qrText, Context context){
		
		this.qrText = qrText;
		this.context = context;
		
	}
	
	/**
	 * A partir de uma matriz recebida de um qrcode, um bitmap de altura "altura" e largura "largura" é gerado.
	 * @param matriz: recebe uma matriz que será salva em um bitmap
	 * @param largura: largura da matriz
	 * @param altura: altura da matriz
	 * @return
	 */
	public Bitmap createQRCBmp(BitMatrix matriz, int largura, int altura){
		
		Bitmap bmp = Bitmap.createBitmap(largura, altura, Bitmap.Config.ARGB_8888);
		
		//Gera um bitmap (ARG_8888 - 4 bytes por pixel) a partir da matriz (i,j) da string codificada
	    for (int j = 0; j < largura; j++)
	        for (int i = 0; i < altura; i++)
	            bmp.setPixel(j, i, matriz.get(j,i) ? Color.BLACK : Color.WHITE);
	    
	    return bmp;
		
	}
	
}
