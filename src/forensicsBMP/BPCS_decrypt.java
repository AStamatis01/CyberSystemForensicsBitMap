package forensicsBMP;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Scanner;

public class BPCS_decrypt {
	
	private static byte[] createOriginalData(byte[] imageData, int dataCount) {
		byte[] data = new byte[dataCount];
		for (int i=0;i<dataCount;i++) {
			data[i] = imageData[i];
		}
		return data;
	}
	
	public static byte[] decrypt(BitMap vessel, int originalDataCount) {
		byte [] tempData = new byte[(int) vessel.getDimensions()];
		int blocksWidth = vessel.getImage().getWidth()/8;
		int blocksHeight = vessel.getImage().getHeight()/8;	
		
		int dataCount = 0;
		
		List<byte[][]> vesselBitplanes = BPCS_encrypt.separateBitplanes(vessel);
		
		for (int bitPlaneNumber=0; bitPlaneNumber<8;bitPlaneNumber++) {
			System.out.println("bitplane " + bitPlaneNumber);
			byte[][] currVesselBitplane = vesselBitplanes.get(bitPlaneNumber);
			for (int i = 0; i< blocksWidth; i++) 
				for (int j=0; j<blocksHeight; j++) {
					
					byte [] TempcurrVesselBlock = new byte[8];
					for (int k=0; k<8; k++)
						for(int q=0;q<8;q++) {
							TempcurrVesselBlock[k] = (byte) (TempcurrVesselBlock[k] | ((currVesselBitplane[i*8+k][j*8+q] << (7-q))));
						}
					
					ImageBlock currVesselBlock = new ImageBlock(TempcurrVesselBlock);
					//currVesselBlock.printBlock();
					//System.out.println(currVesselBlock.calculateComplexity());
					//System.out.println(dataCount);
//					if (currVesselBlock.calculateComplexity() >= 1.0) {
//						currVesselBlock.printBlock();
//						System.out.println("FOund !");
//						
//						
//					}
					if (currVesselBlock.calculateComplexity() > 0.3) {
						currVesselBlock.conjugateBlock();
						//currVesselBlock.printBlock();
					
							
						for(int bl = 0; bl<8;bl++) {
							if(dataCount == originalDataCount)
								return createOriginalData(tempData, dataCount);
							tempData[dataCount++] = currVesselBlock.getBlock()[bl];
						}
					}
					
					
				}
		}
		return tempData;
	}
	
	public static void main (String [] args) {
		
		int dataCount = 0;
		Path path = Paths.get("size.txt");
		Scanner scanner;
		try {
			scanner = new Scanner(path);
			while(scanner.hasNextLine()){
			    String line = scanner.nextLine();
			    dataCount = Integer.parseInt(line);
			}
			scanner.close();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		System.out.println(dataCount);
		
		
		BitMap bmp = new BitMap("saved2.bmp"); // IMAGE OF HIDDEN DATA
		bmp.imageToGrayCode();
		
//		try {
//		    // retrieve image
//		    File outputfile = new File("saved3.bmp");
//		    ImageIO.write(bmp.getImage(), "bmp", outputfile);
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//		BitMap bmp2 = new BitMap("saved3.bmp"); // IMAGE OF HIDDEN DATA
//		int count=0;
//		for (int i = 0; i< bmp.getImage().getWidth(); i++) {
//			for (int j=0; j<bmp.getImage().getHeight(); j++) {
//				if (bmp.getPixel(i, j) != bmp2.getPixel(i, j)) {
//					count++;
//					//if (count % 1000 == 0)
//						System.out.println(count);
//				}
//			}
//			
//		}
		
		byte[] pixelData = decrypt(bmp, dataCount);
		
		System.out.println(pixelData.length);
		try (FileOutputStream file = new FileOutputStream("output")) {
			   file.write(pixelData);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}	    
		
		System.out.println("Created Original File!");
	}
}
