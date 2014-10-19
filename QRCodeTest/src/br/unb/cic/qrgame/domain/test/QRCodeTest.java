package br.unb.cic.qrgame.domain.test;

import java.io.File;

import android.graphics.Bitmap;
import android.os.Environment;
import android.test.InstrumentationTestCase;
import br.unb.cic.qrgame.domain.QRCode;

public class QRCodeTest extends InstrumentationTestCase {

	public void testQRCodeEncodeDecode() {
		
		QRCode code = new QRCode("Usuario Teste", getInstrumentation().getContext());
		
		//Testa se o arquivo do QR code est� sendo criado corretamente:
		code.encodeCode();
		String arquivo = Environment.getExternalStorageDirectory().getPath() + "/QRcode.jpg";
		assertNotNull(arquivo);
		
		//Testa se � poss�vel obter a matriz bitmap do arquivo "QRcode.jpg" gerado. 
		Bitmap bmp = code.imageToBmp(arquivo);
		assertNotNull(bmp);
		
		//Teste 1 - Testa se a string obtida da imagem � a esperada.
		String strDecodificada;
		try {
			strDecodificada = code.decodeCode(bmp);
			assertEquals("Usuario Teste", strDecodificada);
		} catch (Exception e) {
			e.printStackTrace();
		}		
				
		//Teste 2 - decodificar a partir de arquivo
        String file = Environment.getExternalStorageDirectory().getPath() + "/QRcode.jpg";
        try {
			strDecodificada = code.decodeCode( code.imageToBmp(file) );
			assertEquals("Usuario Teste", strDecodificada);
		} catch (Exception e) {
			e.printStackTrace();
		}	
        
		//Remove o QRCode gerado
		String sd = Environment.getExternalStorageDirectory().getPath();
        File qrC = new File(sd, "QRcode.jpg");
        boolean remove = qrC.delete();
        assertEquals(remove, true);
        
        
	}
	
}
