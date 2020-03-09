package steganography;

public class GrayCode {

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
