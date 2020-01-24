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
	
	public void setPixel(int x, int y, byte pixel) {
		this.image.setRGB(x, y, pixel);
	}
	
	public byte getPixel(int x, int y) {
		return (byte) this.image.getRGB(x, y);
	}
}
