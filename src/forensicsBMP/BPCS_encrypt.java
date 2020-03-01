package forensicsBMP;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

public class BPCS_encrypt {
	
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
	
	public static byte[][] replaceBlock(byte [][] vesselBitplane,int x, int y, byte[] block) {
		
		for (int i=0; i<8;i++) {
			for(int j=0;j<8;j++) {
				int newBlockBit = ((block[i] >> (7-j)) & 0x1);
				vesselBitplane[x*8+i][y*8+j] = (byte) newBlockBit;
			}
		}
		return vesselBitplane;
	}
	

	private static void reconstructImage(BitMap vessel, List<byte[][]> vesselBitplanes) {
		vessel = reconstructBitplanes(vessel, vesselBitplanes);
		
		try {
		    // retrieve image
		    File outputfile = new File("saved4.bmp");
		    ImageIO.write(vessel.getImage(), "bmp", outputfile);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		vessel.imageToPBC();
		
		try {
		    // retrieve image
		    File outputfile = new File("saved2.bmp");
		    ImageIO.write(vessel.getImage(), "bmp", outputfile);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	
	private static void hideConjugationMap(BitMap mapVessel) {
		File text = new File("map.txt"); // FILE TO BE CONVERTED TO BITMAP
		byte[] textContent = new byte[(int) text.length()];

		FileInputStream fis = null;

		try {
			fis = new FileInputStream(text);
			fis.read(textContent);
			fis.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		LSB_encrypt.hide(mapVessel, textContent);
	}
	
	
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
					e.printStackTrace();
		}
		
		hideConjugationMap(new BitMap("vessel.bmp"));
	}
			
	
	public static void encrypt(BitMap vessel, List<ImageBlock> blocks, int length) {
		int blocksWidth = vessel.getImage().getWidth()/8;
		int blocksHeight = vessel.getImage().getHeight()/8;	
		int blockCount = 0;

		createConjugationMap(blocks, length);
		
		List<byte[][]> vesselBitplanes = separateBitplanes(vessel);
		//System.out.println("Hi");
		for (int bitPlaneNumber=0; bitPlaneNumber<8;bitPlaneNumber++) {
			System.out.println(bitPlaneNumber + " " + blocks.size());
			byte[][] currVesselBitplane = vesselBitplanes.get(bitPlaneNumber);
			for (int i = 0; i< blocksWidth; i++) 
				for (int j=0; j<blocksHeight; j++) {
					
					if (blocks.isEmpty()) {
						System.out.println("end");
						reconstructImage(vessel,vesselBitplanes);
						return;
					}
					
					byte [] TempcurrVesselBlock = new byte[8];
					for (int k=0; k<8; k++)
						for(int q=0;q<8;q++) {
							TempcurrVesselBlock[k] = (byte) (TempcurrVesselBlock[k] | ((currVesselBitplane[i*8+k][j*8+q] << (7-q))));
							//System.out.println(currVesselBitplane[i*8+k][j*8+q]);
						}
					
					ImageBlock currVesselBlock = new ImageBlock(TempcurrVesselBlock);
					//currVesselBlock.printBlock();
					if (currVesselBlock.calculateComplexity() >= 1.0)
						System.out.println("FOund !");
					//System.out.println(currVesselBlock.calculateComplexity());
					if (currVesselBlock.calculateComplexity() >= 0.3) {
						
						
						//blocks.get(0).conjugateBlock();
						//if (blocks.get(0).calculateComplexity() >=1.0){
							//System.out.println("Found complexity " + blocks.get(0).calculateComplexity() +" " + blocks.size());
						//}
						if (blocks.get(0).calculateComplexity() <= 0.3)
							System.out.println("Complexity " + blocks.get(0).calculateComplexity());
						currVesselBitplane = replaceBlock(currVesselBitplane, i,j,blocks.get(0).getBlock());
						blocks.remove(0);
					}
					else{
						//System.out.println(blockCount++);
					}
					
					
				}
			vesselBitplanes.set(bitPlaneNumber, currVesselBitplane);
		}
		//System.out.println("Hi");

	}
	


	public static void main(String [] args) {
		BitMap bmp = new BitMap("vessel11.bmp");
		
		bmp.imageToGrayscale();
		
		try {
		    // retrieve image
		    File outputfile = new File("saved1.bmp");
		    ImageIO.write(bmp.getImage(), "bmp", outputfile);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		bmp.imageToGrayCode();
		
		
		File text = new File("test2.zip"); // FILE TO BE CONVERTED TO BITMAP
	    byte[] textContent = new byte[(int) text.length()];
	    
	    
	    
	    FileInputStream fis = null;
	    try {
	    	fis = new FileInputStream(text);
	    	fis.read(textContent);
	    	fis.close();
	    }catch(IOException e) {
	    	e.printStackTrace();
	    }
	    System.out.println(textContent.length);
	    
	    int length = textContent.length;
	    
//	    PrintWriter writer;
//		try {
//			writer = new PrintWriter("size.txt", "UTF-8");
//			writer.println(textContent.length);
//		    writer.close();
//		} catch (FileNotFoundException | UnsupportedEncodingException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
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
	    blocks.add(new ImageBlock());

	    
	    encrypt(bmp, blocks, length);
	    

//	    bmp = new BitMap("saved1.bmp");
//	    BitMap bmp2 = new BitMap("saved2.bmp");
//		int count=0;
//		for (int i = 0; i< bmp.getImage().getWidth(); i++) {
//			for (j=0; j<bmp.getImage().getHeight(); j++) {
//				if (bmp.getPixel(i, j) != bmp2.getPixel(i, j)) {
//					count++;
//					if (count % 1000 == 0)
//						System.out.println(count);
//				}
//			}
//			
//		}
	}
}
