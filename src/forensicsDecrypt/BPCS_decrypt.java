package forensicsDecrypt;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import forensicsBMP.BitMap;
import forensicsBMP.ImageBlock;

public class BPCS_decrypt {
	
	public static List<byte[][]> separateBitplanes(BitMap vessel) {
		List<byte[][]> bitplanes = new ArrayList<byte[][]>();
		
		for (int plane=0;plane<8;plane++) {
			byte[][] tempBitplane = new byte[vessel.getWidth()][vessel.getHeight()];
			for (int i=0; i<vessel.getWidth();i++)
				for(int j=0; j<vessel.getHeight();j++) {
					tempBitplane[i][j] = (byte) ((vessel.getImage().getRGB(i, j) >> plane) & 0x1);
			}
			//System.out.println(bitplanes.size());
			bitplanes.add(tempBitplane);
		}
		
		return bitplanes;
	}
	
	
	private static byte[] createOriginalData(byte[] imageData, int dataCount) {
		byte[] data = new byte[dataCount];
		for (int i=0;i<dataCount;i++) {
			data[i] = imageData[i];
		}
		return data;
	}
	
	
	private static List<Integer> getConjugationMap(){
		
		
		byte[] data = LSB_decrypt.decrypt(new BitMap("savedLSB.bmp"));
		

		System.out.println(data.length);
		try (FileOutputStream file = new FileOutputStream("output")) {
			file.write(data);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
		List<Integer> map = new ArrayList<Integer>();
		
		Path path = Paths.get("output");
		Scanner scanner;
		try {
			scanner = new Scanner(path);
			while(scanner.hasNextLine()){
			    String line = scanner.nextLine();
			    //System.out.println(line);
			    map.add(Integer.parseInt(line));
			}
			scanner.close();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (NumberFormatException e2) {
			return map;
		}
		//System.out.println(map);
		return map;
	}
	
	public static byte[] decrypt(BitMap vessel) {
		byte [] tempData = new byte[(int) vessel.getDimensions()];
		int blocksWidth = vessel.getImage().getWidth()/8;
		int blocksHeight = vessel.getImage().getHeight()/8;	
		
		int dataCount = 0;
		
		List<Integer> conjugationMap = getConjugationMap();
		int originalDataCount = conjugationMap.get(0);
		conjugationMap.remove(0);
		
		int blockCount = 0;
		
		List<byte[][]> vesselBitplanes = separateBitplanes(vessel);
		
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
					if (currVesselBlock.calculateComplexity() >= 0.3) {
						if (!(conjugationMap.isEmpty()) & conjugationMap.get(0) == blockCount) {
							currVesselBlock.conjugateBlock();
							conjugationMap.remove(0);
						}
						blockCount++;
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
		
		BitMap bmp = new BitMap("savedBPCS.bmp"); // IMAGE OF HIDDEN DATA
		bmp.imageToGrayCode();
		
		byte[] pixelData = decrypt(bmp);
		
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
