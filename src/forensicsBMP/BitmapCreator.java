package forensicsBMP;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import javax.imageio.ImageIO;

public class BitmapCreator {	
	
	public static void main (String [] args) {
		BitMap bmp = new BitMap(200,200);
		
		File text = new File("manyURLs.txt");
	    byte[] textContent = new byte[(int) text.length()];
	    FileInputStream fis = null;
	    try {
	    	fis = new FileInputStream(text);
	    	fis.read(textContent);
	    	fis.close();
	    }catch(IOException e) {
	    	e.printStackTrace();
	    }
	    System.out.println(textContent.length);
	    
	    byte [] pixelData = new byte[(int) bmp.getDimensions()];
	    
	    int byteCount = 0;
	    int k = 0;
	    for (int i=0; i<bmp.getHeight(); i++) {
	    	for (int j=0; j<bmp.getWidth(); j++) {
	    		if (byteCount>=textContent.length)
		    		break;
		    	bmp.setPixel(i, j, textContent[byteCount++]);
		    	pixelData[k++] = bmp.getPixel(i, j);
	    	}
	    }
	    
	    
	    
	    for (byte i : pixelData) {
	    	System.out.print((char) i);
	    }
	    
	    	    
	    File outputfile = new File("saved.bmp");
	    try {
			ImageIO.write(bmp.getImage(), "bmp", outputfile);
		} catch (IOException e) {
			e.printStackTrace();
		}
	    
		
	}
}
