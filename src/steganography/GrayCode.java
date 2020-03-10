/** @file GrayCode.java
* @brief GrayCode conversion
*
* Convert an 8-bit binary:
* - From Pure Binary Coding to Canonical Grey Coding
* - From Canonical Grey Coding to Pure Binary Coding
* 
* @author Wasaif ALsolami, 2415072A
* @author Ebtihal Althubiti, 2414366A
* @author Antonios Stamatis, 2479716S
* 
*/

package steganography;

public class GrayCode {

	/**
	 * Converts binary from PBC to CGC
	 * @param binary 8-bit binary
	 * @return binary in CGC
	 */
	public int convertToGrayCode(int binary) {
		int gray=0;
		for (int i = 0; i<7; i++) {
			int curBit = (binary >> i) & 0x1;
			int nextBit = (binary >> (i+1)) & 0x1;
			if (nextBit == 1) {
				curBit = 1 - curBit;
			}
			gray |= (curBit << i);
			if (i == 6)
				gray |= (nextBit << (i+1)); 
		}
		return gray;
	}
	
	
	/**
	 * Converts binary from CGC to PBC
	 * @param binary 8-bit binary
	 * @return binary in PBC
	 */
	public int convertToPBC(int binary) {
		int pbc=0;
		int bitLen = 7;
		for (int i=0; i<7; i++) {
			int curBit = (binary >> i) & 0x1;
			int sum=0;
			for (int j=bitLen; j>i;j--) {
				sum+= ((binary>>j) & 0x1);
			}
			sum = sum % 2;
			if (sum == 1) {
				curBit = 1-curBit;
			}
			pbc |= (curBit << i);
			if (i == 6) {
				int nextBit = (binary >> (i+1)) & 0x1;
				pbc |= (nextBit << (i+1));
			}
		}
		return pbc;
	}
}
