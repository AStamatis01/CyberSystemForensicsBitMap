package forensicsBMP;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class BitMap {
	
	private int height;
	private int width;
	private BufferedImage image;
	
	public BitMap(int height, int width) {
		this.height = height;
		this.width = width;
		this.image =  new BufferedImage(height, width, BufferedImage.TYPE_3BYTE_BGR);
	}
	
	public BitMap(String fileName) {
		File bmpFile = new File(fileName);
		try {
			this.image = ImageIO.read(bmpFile);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		this.height = this.image.getHeight();
		this.width = this.image.getWidth();
		System.out.println(this.height + " " + this.width);
		
	}
	
	public int toRed(int pixelValue) {
		int red = 255;
		int green = 0;
		int blue =  0;
		
		return (red << 16) | (green << 8) | blue;
	}
	
	public int pixelToGrayscale(int pixelValue) {
		double red = (getRedPixel(pixelValue) * 0.2126);
		double green =  (getGreenPixel(pixelValue) * 0.7152);
		double blue =  (getBluePixel(pixelValue) * 0.0722);
		int gray = (int) Math.round(red + green + blue);
		
		return (gray << 16) | (gray << 8) | gray;
	}
	
	public int pixelToGrayscaleAverage(int pixelValue) {
		int red = (getRedPixel(pixelValue));
		int green =  (getGreenPixel(pixelValue));
		int blue =  (getBluePixel(pixelValue));
		int gray = (red + green + blue)/3;
		
		return (gray << 16) | (gray << 8) | gray;
	}
	
	public int pixelToGrayCode(int pixelValue) {
		GrayCode graycode = new GrayCode();
		int red = graycode.convertToGrayCode((getRedPixel(pixelValue)));
		int green =  graycode.convertToGrayCode((getGreenPixel(pixelValue)));
		int blue =  graycode.convertToGrayCode((getBluePixel(pixelValue)));
		return (red << 16) | (green << 8) | blue;
	}
	
	public int pixelToPBC(int pixelValue) {
		GrayCode graycode = new GrayCode();
		int red = graycode.convertToPBC((getRedPixel(pixelValue)));
		int green =  graycode.convertToPBC((getGreenPixel(pixelValue)));
		int blue =  graycode.convertToPBC((getBluePixel(pixelValue)));
		return (red << 16) | (green << 8) | blue;
	}
	
	public int getRedPixel(int pixelValue) {
		return (pixelValue >> 16) & 0xff;
	}
	
	public int getGreenPixel(int pixelValue) {
		return (pixelValue >> 8) & 0xff;
	}
	
	public int getBluePixel(int pixelValue) {
		return pixelValue & 0xff;
	}
	
	public void printRGB(int pixelValue) {
		//int alpha = (pixelValue >> 24) & 0xff;

		int red = (pixelValue >> 16) & 0xff;
	    int green = (pixelValue >> 8) & 0xff;
	    int blue = (pixelValue) & 0xff;
	    System.out.println("rgb: " + pixelValue +", " + red + ", " + green + ", " + blue);
	}
	
	public void printRGBtoBinary (int pixelValue) {
		int red = (pixelValue >> 16) & 0xff;
		for (int i = 7; i<=0; i--) {
			System.out.print((red >> i) & 0x1);
		}
	    int green = (pixelValue >> 8) & 0xff;
	    for (int i = 7; i<=0; i--) {
			System.out.print((green >> i) & 0x1);
		}
	    int blue = (pixelValue) & 0xff;
	    for (int i = 7; i<=0; i--) {
			System.out.print((blue >> i) & 0x1);
		}
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
	
	public void setPixel(int x, int y, int i) {
		this.image.setRGB(x, y, i);
	}
	
	public int getPixel(int x, int y) {
		return this.image.getRGB(x, y);
	}
}
