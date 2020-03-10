/** @file BPCS_encrypt.java
* @brief Hiding of data in an image using BPCS
*
* BPCS uses a secondary image, which holds the conjugation map.
* The map is hidden in the secondary image using LSB.
* The threshold of the complexity is 0.3
*
* @author Wasaif ALsolami, 2415072A
* @author Ebtihal Althubiti, 2414366A
* @author Antonios Stamatis, 2479716S
* 
*/

package steganography;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.JOptionPane;



public class BPCS_encrypt {
	
	
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
			bitplanes.add(tempBitplane);
		}
		return bitplanes;
	}
	
	
	/**
	 * Reconstructs bit planes from the List of Integers
	 * @param vessel vessel image
	 * @param bitplanes List of planes (Byte 2-d arrays)
	 * @return The newly bmp image that has data hidden in it
	 */
	public static BitMap reconstructBitplanes(BitMap vessel, List<byte[][]>bitplanes) {
		
		for (int i=0; i<vessel.getWidth(); i++) {
			for(int j=0; j<vessel.getHeight(); j++) {
				int tempPixel=0;
				for(int plane=7; plane>=0; plane--) {
					tempPixel = tempPixel | (bitplanes.get(plane)[i][j] << plane);
				}
				vessel.setPixelGrayscale(i, j, tempPixel);
			}
		}
		return vessel;
	}

	
	/**
	 * Replace the specified block (starting at x and y) with a new block
	 * @param vesselBitplane Current vessel bit plane
	 * @param x The start of the block (Row X)
	 * @param y The start of the block (Column Y)
	 * @param block The new block
	 * @return The updated bitplane
	 */
	public static byte[][] replaceBlock(byte [][] vesselBitplane,int x, int y, byte[] block) {
		
		for (int i=0; i<8;i++) {
			for(int j=0;j<8;j++) {
				int newBlockBit = ((block[i] >> (7-j)) & 0x1);
				vesselBitplane[x*8+i][y*8+j] = (byte) newBlockBit;
			}
		}
		return vesselBitplane;
	}
	

	/**
	 * Reconstruct image, convert it the PBC and then output it
	 * @param vessel Vessel image
	 * @param vesselBitplanes vessel bit planes
	 */
	private static void reconstructImage(BitMap vessel, List<byte[][]> vesselBitplanes) {
		vessel = reconstructBitplanes(vessel, vesselBitplanes);
		
		vessel.imageToPBC();
		
		try {
		    // retrieve image
		    File outputfile = new File("savedBPCS.bmp");
		    ImageIO.write(vessel.getImage(), "bmp", outputfile);
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null, e.toString(),"Error", JOptionPane.ERROR_MESSAGE);
		}
		
	}
	
	
	/**
	 * Hide conjugation map using LSB in a secondary image
	 * @param mapVessel the vessel for the conjugation map
	 */
	private static void hideConjugationMap(BitMap mapVessel) {
		File text = new File("map.txt"); // FILE TO BE CONVERTED TO BITMAP
		byte[] textContent = new byte[(int) text.length()];
		int flag=1;
		FileInputStream fis = null;

		try {
			fis = new FileInputStream(text);
			fis.read(textContent);
			fis.close();
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null, e.toString(),"Error", JOptionPane.ERROR_MESSAGE);
		}

		LSB_encrypt.hide(mapVessel, textContent,flag);
	}
	
	
	/**
	 * Create the conjugation map by calculating each block's complexity
	 * If a block is under 0.3 complexity, then it will conjugate it.
	 * Saves it in a secondary image using LSB.
	 * @param blocks The blocks of the original data
	 * @param length The length of the data in bytes
	 */
	private static void createConjugationMap(List<ImageBlock> blocks, int length) {
		PrintWriter writer;
		try {
			writer = new PrintWriter("map.txt", "UTF-8");
			writer.println(length);
			for(int i=0; i<blocks.size(); i++) {
				if(blocks.get(i).calculateComplexity()< 0.3) {
					blocks.get(i).conjugateBlock();
					writer.println(i);
				}
			}
			writer.close();
		} catch (FileNotFoundException | UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
			JOptionPane.showMessageDialog(null, e.toString(),"Error", JOptionPane.ERROR_MESSAGE);
		}
		hideConjugationMap(new BitMap("vessel.bmp"));
	}
			
	
	/**
	 * Hide the data in a vessel image using BPCS
	 * @param vessel vessel image
	 * @param blocks original data in blocks (8x8 bits)
	 * @param length Length of original data in bytes
	 */
	public static void encrypt(BitMap vessel, List<ImageBlock> blocks, int length) {
		int blocksWidth = vessel.getImage().getWidth()/8;
		int blocksHeight = vessel.getImage().getHeight()/8;	

		createConjugationMap(blocks, length);
		
		List<byte[][]> vesselBitplanes = separateBitplanes(vessel);
		for (int bitPlaneNumber=0; bitPlaneNumber<8;bitPlaneNumber++) {
			System.out.println(bitPlaneNumber + " " + blocks.size());
			byte[][] currVesselBitplane = vesselBitplanes.get(bitPlaneNumber);
			for (int i = 0; i< blocksWidth; i++) 
				for (int j=0; j<blocksHeight; j++) {
					if (blocks.isEmpty()) {
						reconstructImage(vessel,vesselBitplanes);
						JOptionPane.showMessageDialog(null,"The task is successfully done");
						return;
					}
					byte [] TempcurrVesselBlock = new byte[8];
					for (int k=0; k<8; k++)
						for(int q=0;q<8;q++)
							TempcurrVesselBlock[k] = (byte) (TempcurrVesselBlock[k] | ((currVesselBitplane[i*8+k][j*8+q] << (7-q))));
					ImageBlock currVesselBlock = new ImageBlock(TempcurrVesselBlock);

					if (currVesselBlock.calculateComplexity() >= 0.3) {
						currVesselBitplane = replaceBlock(currVesselBitplane, i,j,blocks.get(0).getBlock());
						blocks.remove(0);
					}
				}
			vesselBitplanes.set(bitPlaneNumber, currVesselBitplane);
		}
	}
	

	/**
	 * Start of the BPCS encrypt/hiding process. Converts the image to grayscale and then to CGC. Reads the file data in a byte array
	 * @param image_name the image file name
	 * @param file_name the file to be hidden
	 */
	public static void read_image_file(String image_name, String file_name) {
		BitMap bmp = new BitMap(image_name); //VESSEL IMAGE
		
		bmp.imageToGrayscale();
		
		bmp.imageToGrayCode();
		
		
		File text = new File(file_name); // FILE TO BE CONVERTED TO BITMAP
	    byte[] textContent = new byte[(int) text.length()];
	    
	    FileInputStream fis = null;
	    try {
	    	fis = new FileInputStream(text);
	    	fis.read(textContent);
	    	fis.close();
	    }catch(IOException e) {
	    	JOptionPane.showMessageDialog(null, e.toString(),"Error", JOptionPane.ERROR_MESSAGE);
	    }
	    System.out.println(textContent.length);
	    
	    int length = textContent.length;

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
	    blocks.add(new ImageBlock());

	    
	    encrypt(bmp, blocks, length);
	    
	}
}
