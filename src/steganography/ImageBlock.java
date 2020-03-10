/** @file ImageBlock.java
* @brief Handles image blocks (8x8 bit representations)
*
* Handles image blocks, calculates complexity and conjugates them
* 
* @author Wasaif ALsolami, 2415072A
* @author Ebtihal Althubiti, 2414366A
* @author Antonios Stamatis, 2479716S
* 
*/

package steganography;

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

	
	public ImageBlock(byte [] blocks) {
		this.blocks = blocks;
	}
	
	public ImageBlock() {
		for (int i=0; i<8; i++)
			this.blocks[i] = 0;
	}
	
	
	/**
	 * Calculates the complexity of the block 
	 * @return The Block's complexity (value 0.0 to 1.0)
	 */
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
	
	
	/**
	 * Conjugates the block
	 * XOR operation with a checkered style board block
	 */
	public void conjugateBlock() {
		for (int i = 0; i<this.blocks.length; i++)
			if (i % 2 == 0)
				this.blocks[i] = (byte) (this.blocks[i] ^ this.CHECKERED_BOARD_1);
			else
				this.blocks[i] = (byte) (this.blocks[i] ^ this.CHECKERED_BOARD_2);
	}
	
	
	/**
	 * Get Blocks
	 * @return Blocks in a byte array
	 */
	public byte[] getBlock() {
		return this.blocks;
	}
	
	
}
