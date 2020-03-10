/** @file BitMap.java
* @brief Enables reading and accessing different aspects of a bitmap image
*
* With the constructor, it reads a bmp file
* There are several actions that can be achieved through this class like
* getting/setting pixel values, converting to grayscale,PBC or graycode encoding
*
* @author Wasaif ALsolami, 2415072A
* @author Ebtihal Althubiti, 2414366A
* @author Antonios Stamatis, 2479716S
* 
*/


package steganography;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JOptionPane;

public class BitMap {
	
	private int height;
	private int width;
	private BufferedImage image;
	
	
	/**
	 * Constructor, creates an empty bitmap image
	 * @param height
	 * @param width
	 */
	public BitMap(int height, int width) {
		this.height = height;
		this.width = width;
		this.image =  new BufferedImage(height, width, BufferedImage.TYPE_3BYTE_BGR);
	}
	
	
	/**
	 * Constructor, get bmp image based on the filename
	 * @param fileName, name of the bmp image
	 */
	public BitMap(String fileName) {
		File bmpFile = new File(fileName);
		try {
			this.image = ImageIO.read(bmpFile);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			JOptionPane.showMessageDialog(null, e.toString(),"Error", JOptionPane.ERROR_MESSAGE);
		}
		this.height = this.image.getHeight();
		this.width = this.image.getWidth();
		
	}
	
	
	/**
	 * Converts image to Grayscale
	 */
	public void imageToGrayscale() {
		for (int i = 0; i< this.getImage().getWidth(); i++) {
			for (int j=0; j<this.getImage().getHeight(); j++) {
				this.setPixel(i, j, this.pixelToGrayscale(this.getPixel(i, j)));
			}
			
		}
	}
	
	
	/**
	 * Converts image to Canonical Grey Coding
	 */
	public void imageToGrayCode() {
		for (int i = 0; i< this.getImage().getWidth(); i++) {
			for (int j=0; j<this.getImage().getHeight(); j++) {
				this.setPixel(i, j, this.pixelToGrayCode(this.getPixel(i, j)));
			}
			
		}
	}
	
	
	/**
	 * Converts image to Pure Binary Encoding
	 */
	public void imageToPBC() {
		for (int i = 0; i< this.getImage().getWidth(); i++) {
			for (int j=0; j<this.getImage().getHeight(); j++) {
				this.setPixel(i, j, this.pixelToPBC(this.getPixel(i, j)));
			}
			
		}
	}
	
	
	/**
	 * Converts pixel to color red
	 * @param pixelValue
	 * @return Red pixel value (R=255, G=0, B=0)
	 */
	public int toRed() {
		int red = 255;
		int green = 0;
		int blue =  0;
		
		return (red << 16) | (green << 8) | blue;
	}
	
	
	/**
	 * Converts pixel to grayscale
	 * @param pixelValue The current pixel value
	 * @return The new grayscale pixel
	 */
	public int pixelToGrayscale(int pixelValue) {
		double red = (getRedPixel(pixelValue) * 0.2126);
		double green =  (getGreenPixel(pixelValue) * 0.7152);
		double blue =  (getBluePixel(pixelValue) * 0.0722);
		int gray = (int) Math.round(red + green + blue);
		
		return (gray << 16) | (gray << 8) | gray;
	}
	
	
	/**
	 * Convert pixel to grayscale (Based on the average) (NOT USED in our implementation of LBS/BPCS)
	 * @param pixelValue Current pixel value
	 * @return The new grayscale pixel
	 */
	public int pixelToGrayscaleAverage(int pixelValue) {
		int red = (getRedPixel(pixelValue));
		int green =  (getGreenPixel(pixelValue));
		int blue =  (getBluePixel(pixelValue));
		int gray = (red + green + blue)/3;
		
		return (gray << 16) | (gray << 8) | gray;
	}
	
	
	/**
	 * Convert pixel to Canonical Grey Coding
	 * @param pixelValue Current pixel value
	 * @return The new CGC pixel
	 */
	public int pixelToGrayCode(int pixelValue) {
		GrayCode graycode = new GrayCode();
		int red = graycode.convertToGrayCode((getRedPixel(pixelValue)));
		int green =  graycode.convertToGrayCode((getGreenPixel(pixelValue)));
		int blue =  graycode.convertToGrayCode((getBluePixel(pixelValue)));
		return (red << 16) | (green << 8) | blue;
	}
	
	
	/**
	 * Converts pixel to Pure Binary Encoding
	 * @param pixelValue Current pixel value
	 * @return The new PBC pixel
	 */
	public int pixelToPBC(int pixelValue) {
		GrayCode graycode = new GrayCode();
		int red = graycode.convertToPBC((getRedPixel(pixelValue)));
		int green =  graycode.convertToPBC((getGreenPixel(pixelValue)));
		int blue =  graycode.convertToPBC((getBluePixel(pixelValue)));
		return (red << 16) | (green << 8) | blue;
	}
	
	
	/**
	 * Get the Red value of the pixel
	 * @param pixelValue Current pixel value
	 * @return Red value of pixel
	 */
	public int getRedPixel(int pixelValue) {
		return (pixelValue >> 16) & 0xff;
	}
	
	
	/**
	 * Get the Green value of the pixel
	 * @param pixelValue Current pixel value
	 * @return Green value of pixel
	 */
	public int getGreenPixel(int pixelValue) {
		return (pixelValue >> 8) & 0xff;
	}
	
	
	/**
	 * Get the Blue value of the pixel
	 * @param pixelValue Current pixel value
	 * @return Blue value of pixel
	 */
	public int getBluePixel(int pixelValue) {
		return pixelValue & 0xff;
	}
	
	
	/**
	 * Prints current RGB pixel value
	 * @param pixelValue current pixel value
	 */
	public void printRGB(int pixelValue) {
		int red = (pixelValue >> 16) & 0xff;
	    int green = (pixelValue >> 8) & 0xff;
	    int blue = (pixelValue) & 0xff;
	    System.out.println("rgb: " + pixelValue +", " + red + ", " + green + ", " + blue);
	}

	
	public int getHeight() {
		return this.height;
	}
	
	public int getWidth() {
		return this.width;
	}
	
	public int getDimensions() {
		return this.height * this.width;
	}
	
	public BufferedImage getImage() {
		return this.image;
	}
	
	
	/**
	 * Set the grayscale value to the pixel
	 * @param x X position of pixel
	 * @param y Y position of pixel
	 * @param i Pixel value
	 */
	public void setPixelGrayscale(int x, int y, int i) {
		this.image.setRGB(x,y,(i << 16) | (i << 8) | i);
	}
	
	
	/**
	 * Set pixel value
	 * @param x X position of pixel
	 * @param y Y position of pixel
	 * @param i Pixel value
	 */
	public void setPixel(int x, int y, int i) {
		this.image.setRGB(x, y, i);
	}
	
	
	/**
	 * Get pixel value
	 * @param x X position of pixel
	 * @param y Y position of pixel
	 * @return Pixel value at the specified position
	 */
	public int getPixel(int x, int y) {
		
		return this.image.getRGB(x, y);
	}
}
