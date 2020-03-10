/** @file LSB_decrypt.java
* @brief Extraction of data from an image using LSB
*
* The hidden is extracted using LSB.
*
* @author Wasaif ALsolami, 2415072A
* @author Ebtihal Althubiti, 2414366A
* @author Antonios Stamatis, 2479716S
* 
*/

package steganography;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.swing.JOptionPane;



public class LSB_decrypt {
	
	
	/**
	 * Get the LSB of a pixel
	 * @param currentByte The current byte of the data being extracted
	 * @param colorPixel The current part (color) of the current pixel
	 * @return The updated byte
	 */
	private static byte getBit(byte currentByte , int colorPixel) {
		
		currentByte = (byte)((currentByte << 1) | (colorPixel & 0x1));
		
		return currentByte;
	}
	
	
	/**
	 * Get the hidden data using LSB
	 * @param image the vessel image containing hidden data
	 * @return The original data
	 */
	public static byte[] decrypt(BitMap image) {
		
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
		
		
		return data;
	}

	
	
	/**
	 * Start of the LSB decrypt process. Showcases in the end wether the extraction was succesful or not
	 * @param image the image file name
	 * @param form the extension of the file hidden
	 */
	public static void read_image_file(String image, String form) {
		BitMap bmp = new BitMap(image); // VESSEL
		
		byte[] data = decrypt(bmp);
		
		
		try (FileOutputStream file = new FileOutputStream("Original_File_LSB"+"."+form)) {
			   file.write(data);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				JOptionPane.showMessageDialog(null, e.toString(),"Error", JOptionPane.ERROR_MESSAGE);
			}	    
		
		JOptionPane.showMessageDialog(null,"The task is successfully done");
	}

}
