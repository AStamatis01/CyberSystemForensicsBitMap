/** @file BPCS_decrypt.java
* @brief Extraction of data from an image using BPCS
*
* BPCS uses a secondary image, which holds the conjugation map.
* The map is extracted using LSB.
* Using that conjugate map, the BPCS process is completed thus outputing the original file that was hidden
*
* @author Wasaif ALsolami, 2415072A
* @author Ebtihal Althubiti, 2414366A
* @author Antonios Stamatis, 2479716S
* 
*/

package steganography;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import javax.swing.JOptionPane;



public class BPCS_decrypt {
	
	
	/**
	 * Separate the bit planes of the image
	 * @param vessel the image
	 * @return a list of byte 2-d arrays representing each bit-plane (8 elements in list)
	 */
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
	
	
	/**
	 * Recreates the original data
	 * @param imageData The original data in bytes
	 * @param dataCount The length of the imageData array
	 * @return a 
	 */
	private static byte[] createOriginalData(byte[] imageData, int dataCount) {
		byte[] data = new byte[dataCount];
		for (int i=0;i<dataCount;i++) {
			data[i] = imageData[i];
		}
		return data;
	}
	
	
	/**
	 * Get the size of the original file and the conjugation map from a secondary image
	 * @return The size and conjugation map in a List of Integers
	 */
	private static List<Integer> getConjugationMap(){
		byte[] data = LSB_decrypt.decrypt(new BitMap("savedLSB.bmp"));
		System.out.println(data.length);
		try (FileOutputStream file = new FileOutputStream("Original_File_BPCS.txt")) {
			file.write(data);
		} catch (FileNotFoundException e) {
			JOptionPane.showMessageDialog(null, e.toString(),"Error", JOptionPane.ERROR_MESSAGE);
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null, e.toString(),"Error", JOptionPane.ERROR_MESSAGE);
		}
		
		List<Integer> map = new ArrayList<Integer>();
		Path path = Paths.get("Original_File_BPCS.txt");
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
			JOptionPane.showMessageDialog(null, e1.toString(),"Error", JOptionPane.ERROR_MESSAGE);
		} catch (NumberFormatException e2) {
			return map;
		}
		return map;
	}
	
	
	/**
	 * Extracts the data from an image using BPCS
	 * @param vessel the vessel image , containing hidden data
	 * @return the data that was hidden
	 */
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
	
	
	/**
	 * Start of the BPCS decrypt process. Showcases in the end wether the extraction was succesful or not
	 * @param image the image file name
	 * @param form the extension of the file hidden
	 */
	public static void read_image_file(String image, String form) {		
		
		BitMap bmp = new BitMap(image); // IMAGE OF HIDDEN DATA
		bmp.imageToGrayCode();
		
		byte[] pixelData = decrypt(bmp);
		
		System.out.println("Hi");
		try (FileOutputStream file = new FileOutputStream("Original_File_BPCS"+"."+form)) {
			   file.write(pixelData);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				JOptionPane.showMessageDialog(null, e.toString(),"Error", JOptionPane.ERROR_MESSAGE);
			}	    
		
		JOptionPane.showMessageDialog(null,"The task is successfully done");
	}
}
