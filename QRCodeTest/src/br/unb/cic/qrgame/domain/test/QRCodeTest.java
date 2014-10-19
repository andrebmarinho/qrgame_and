package br.unb.cic.qrgame.domain.test;

import java.io.File;

import android.graphics.Bitmap;
import android.os.Environment;
import android.test.InstrumentationTestCase;
import br.unb.cic.qrgame.domain.QRCode;

public class QRCodeTest extends InstrumentationTestCase {

	public void testQRCodeEncodeDecode() {
		
		QRCode code = new QRCode("Usuario Teste", getInstrumentation().getContext());
		
		//Testa se o arquivo do QR code está sendo criado corretamente:
		code.encodeCode();
		String arquivo = Environment.getExternalStorageDirectory().getPath() + "/QRcode.jpg";
		assertNotNull(arquivo);
		
		//Testa se é possível obter a matriz bitmap do arquivo "QRcode.jpg" gerado. 
		Bitmap bmp = code.imageToBmp(arquivo);
		assertNotNull(bmp);
		
		//Testa se a string obtida da imagem é a esperada.
		String strDecodificada = code.decodeCode(bmp);		
		assertEquals("Usuario Teste", strDecodificada);
		
		//Remove o QRCode gerado
		String sd = Environment.getExternalStorageDirectory().getPath();
        File qrC = new File(sd, "QRcode.jpg");
        boolean remove = qrC.delete();
        assertEquals(remove, true);
		
	}
	
}
