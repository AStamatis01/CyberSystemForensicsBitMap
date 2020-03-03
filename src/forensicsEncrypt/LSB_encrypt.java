package forensicsEncrypt;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

import forensicsBMP.BitMap;

public class LSB_encrypt {

	private static void createImage(BitMap image, int[][] pixels) {

		for (int i = 0; i < image.getWidth(); i++) {
			for (int j = 0; j < image.getHeight(); j++) {
				image.setPixel(i, j, pixels[i][j]);
			}

		}

		try {
			// retrieve image
			File outputfile = new File("savedLSB.bmp");
			ImageIO.write(image.getImage(), "bmp", outputfile);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static int changeColorPixel(int[][] pixels, byte[] file, int i, int j, int bitCount, int fileCount) {
		pixels[i][j] = pixels[i][j] >> 1;
		pixels[i][j] = pixels[i][j] << 1;

		int bit = (file[fileCount] >> (7-bitCount)) & 0x1;

		return pixels[i][j] | bit;
	}

	public static void hide(BitMap image, byte[] file) {

		int size = file.length;
		System.out.println("Size: " + size);
		int bitCount = 0;
		int fileCount = 0;
		int width = 0;
		int height = 0;
		int[][] subarray = new int[image.getWidth()][image.getHeight()];

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

		for (int i = 0; i < image.getWidth(); i++) {
			for (int j = 0; j < image.getHeight(); j++) {
				for (int color = 0; color<3; color++) {
					if (color == 0) {
						redPixels[i][j] = changeColorPixel(redPixels, file, i, j, bitCount, fileCount);
	
					} else {
						if (color == 1) {
							greenPixels[i][j] = changeColorPixel(greenPixels, file, i, j, bitCount, fileCount);
	
						} else {
							bluePixels[i][j] = changeColorPixel(bluePixels, file, i, j, bitCount, fileCount);
	
						}
					}
					
					bitCount++;
					if (bitCount == 8) {
						fileCount++;
						bitCount = 0;
						if (fileCount == size) {
							for (int q = 0; q < image.getWidth(); q++) {
								for (int p = 0; p < image.getHeight(); p++) {
									subarray[q][p] = (redPixels[q][p] << 16) | (greenPixels[q][p] << 8)
											| (bluePixels[q][p]);
								}
							}
							createImage(image, subarray);
							System.out.println("End");
							return;
						}
					}
				}

				
			}

		}
		System.out.println("Data cannot be encrypted! Image size small!");
	}

	public static void main(String[] args) {

		BitMap bmp = new BitMap("vessel6.bmp");

		File text = new File("Welcome.pdf"); // FILE TO BE CONVERTED TO BITMAP
		byte[] textContent = new byte[(int) text.length()];

		FileInputStream fis = null;

		try {
			fis = new FileInputStream(text);
			fis.read(textContent);
			fis.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		hide(bmp, textContent);

	}

}
