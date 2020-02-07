package forensicsBMP;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ImageBlock {

	private byte [] blocks = new byte[8];
	public final double MAX_COMPLEXITY = 2*8*(8-1);
	
	public final byte CHECKERED_BOARD_1 = (byte) 170;
	public final byte CHECKERED_BOARD_2 = (byte) 85;

	
	public double calculateComplexity() {
		int complexity = 0;
		int currBit = 0;
		for (int i = 0; i<8;i++) {
			for (int j=7; j>=0;j--) {
				int temp = blocks[i] >> j & 0x1;
				if (j==7)
					currBit = temp;
				else {
					if (currBit != temp) {
						complexity++;
						currBit = temp;
					}
				}
			}
		}
		
		for (int i = 7; i>=0;i--) {
			for (int j=0; j<8;j++) {
				int temp = blocks[j] >> i & 0x1;
				if (j==0)
					currBit = temp;
				else {
					if (currBit != temp) {
						complexity++;
						currBit = temp;
					}
				}
			}
		}
		
		return complexity/MAX_COMPLEXITY;
	}
	
	
	public void conjugateBlock() {
		for (int i = 0; i<this.blocks.length; i++)
			if (i % 2 == 0)
				this.blocks[i] = (byte) (this.blocks[i] ^ this.CHECKERED_BOARD_1);
			else
				this.blocks[i] = (byte) (this.blocks[i] ^ this.CHECKERED_BOARD_2);
	}
	
	public byte[] getBlock() {
		return this.blocks;
	}
	
	
	public ImageBlock(byte [] blocks) {
		this.blocks = blocks;
	}
	
	public void printBlock() {
		for (int i=0; i<8; i++) {
			for(int j=7; j>=0;j--)
			System.out.print(this.blocks[i] >> j & 0x1);
			System.out.println();

		}
		System.out.println();
		System.out.println();

	}
	
	
	public static void main(String [] args) {
		File text = new File("testPaper.pdf"); // FILE TO BE CONVERTED TO BITMAP
	    byte[] textContent = new byte[(int) text.length()];
	    FileInputStream fis = null;
	    try {
	    	fis = new FileInputStream(text);
	    	fis.read(textContent);
	    	fis.close();
	    }catch(IOException e) {
	    	e.printStackTrace();
	    }
//		BitMap bmp = new BitMap("vessel.bmp");
//		byte[] textContent = new byte[(int) bmp.getWidth() * bmp.getHeight()];
//		for (int i=0; i<bmp.getWidth(); i++)
//			for(int j=0; j<bmp.getHeight(); j++)
//				textContent[i*bmp.getHeight() + j] =  (byte) bmp.getBluePixel(bmp.getPixel(i, j));
//		
	    List <ImageBlock> blocks = new ArrayList<ImageBlock>();
	    int j=0;
	    byte[] tempByte = new byte[8];
	    for (int i = 0; i<textContent.length; i++) {
	    	if (j==0)
	    		tempByte = new byte[8];
	    
	    	tempByte[j] = textContent[i];
	    	j++;
	    	if (j==8) {
	    		blocks.add(new ImageBlock(tempByte));
	    		j=0;
	    	}
	    }
	    blocks.add(new ImageBlock(tempByte));
	    
	    for (ImageBlock block : blocks) 
	    	if (block.calculateComplexity() < 0.3) 
	    		block.conjugateBlock();
	    	
	    System.out.println(blocks.size());
	}
}
