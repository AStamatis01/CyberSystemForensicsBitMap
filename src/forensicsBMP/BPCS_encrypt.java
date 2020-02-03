package forensicsBMP;

import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class BPCS_encrypt {
	

	public static void main(String [] args) {
		BitMap bmp = new BitMap("vessel.bmp");
		
		for (int i = 0; i< bmp.getImage().getWidth(); i++) {
			for (int j=0; j<bmp.getImage().getHeight(); j++) {
				bmp.setPixel(i, j, bmp.pixelToGrayscaleAverage(bmp.getPixel(i, j)));
			}
			
		}
		
		try {
		    // retrieve image
		    File outputfile = new File("saved.bmp");
		    ImageIO.write(bmp.getImage(), "bmp", outputfile);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		for (int i = 0; i< bmp.getImage().getWidth(); i++) {
			for (int j=0; j<bmp.getImage().getHeight(); j++) {
				bmp.setPixel(i, j, bmp.pixelToGrayCode(bmp.getPixel(i, j)));
			}
			
		}
		
		try {
		    // retrieve image
		    File outputfile = new File("savedGray.bmp");
		    ImageIO.write(bmp.getImage(), "bmp", outputfile);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
//		for (int i = 0; i< bmp.getImage().getWidth(); i++) {
//			for (int j=0; j<bmp.getImage().getHeight(); j++) {
//				bmp.setPixel(i, j, bmp.pixelToPBC(bmp.getPixel(i, j)));
//			}
//			
//		}
//		
//		try {
//		    // retrieve image
//		    File outputfile = new File("savedPBC.bmp");
//		    ImageIO.write(bmp.getImage(), "bmp", outputfile);
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//		
//		BitMap bmp2 = new BitMap("savedPBC.bmp");
//		
//		for (int i = 0; i< bmp.getImage().getWidth(); i++) {
//			for (int j=0; j<bmp.getImage().getHeight(); j++) {
//				if (bmp.getPixel(i, j) != bmp2.getPixel(i, j)) {
//					System.out.println("Different Bit!");
//				}
//			}
//			
//		}
	}
}
