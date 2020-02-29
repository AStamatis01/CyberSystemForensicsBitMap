package forensicsBMP;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class LSB_decrypt {
	
	private static byte getBit(byte currentByte , int colorPixel) {
		
		currentByte = (byte)((currentByte << 1) | (colorPixel & 0x1));
		
		return currentByte;
	}
	
	private static byte[] decrypt(BitMap image) {
		
		int bitCount = 0;
		int byteCount = 0;
		
		byte [] data = new byte[image.getWidth()*image.getHeight()/8*3];


		int[][] redPixels = new int[image.getWidth()][image.getHeight()];
		int[][] greenPixels = new int[image.getWidth()][image.getHeight()];
		int[][] bluePixels = new int[image.getWidth()][image.getHeight()];

		for (int i = 0; i < image.getWidth(); i++) {
			for (int j = 0; j < image.getHeight(); j++) {
				redPixels[i][j] = image.getRedPixel(image.getPixel(i, j));
				greenPixels[i][j] = image.getGreenPixel(image.getPixel(i, j));
				bluePixels[i][j] = image.getBluePixel(image.getPixel(i, j));

			}
		}
		
		for (int i=0; i<image.getWidth(); i++) {
			for (int j=0; j<image.getHeight(); j++) {
				for (int color = 0; color<3; color++) {

					if (color == 0) {
						data[byteCount] = getBit(data[byteCount], redPixels[i][j]);
					} else {
						if (color == 1) {
							data[byteCount] = getBit(data[byteCount], greenPixels[i][j]);
	
						} else {
							data[byteCount] = getBit(data[byteCount], bluePixels[i][j]);
	
						}
					}
					
					bitCount++;
					if (bitCount == 8) {
						byteCount++;
						bitCount = 0;
					}
				}
			}
		}
		
		System.out.println("End");
		return data;
	}

	public static void main(String[] args) {
		BitMap bmp = new BitMap("savedLSB.bmp");
		
		byte[] data = decrypt(bmp);
		
		System.out.println(data.length);
		try (FileOutputStream file = new FileOutputStream("output")) {
			   file.write(data);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}	    
		
		System.out.println("Created Original File!");


	}

}
