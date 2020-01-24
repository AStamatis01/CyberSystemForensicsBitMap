package forensicsBMP;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class ExtractDataFromBitmap {
	
	public static void main (String [] args) {
		BitMap bmp = new BitMap("saved.bmp"); // IMAGE OF HIDDEN DATA
		byte [] pixelData = new byte[(int) bmp.getDimensions()];
		int k=0;
		for (int i=0; i<bmp.getWidth(); i++) {
	    	for (int j=0; j<bmp.getHeight(); j++) {
		    	pixelData[k++] = (byte) bmp.getPixel(i, j);
	    	}
		}
		
		try (FileOutputStream file = new FileOutputStream("output.pdf")) {
			   file.write(pixelData);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}	    
		
		System.out.println("Created Original File!");
//		for (int i : pixelData) {
//	    	if (i == 0) break;
//	    	System.out.print((char) i);
//	    }
		
	}
	
}
