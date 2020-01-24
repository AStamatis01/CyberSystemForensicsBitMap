package forensicsBMP;


public class ExtractDataFromBitmap {
	
	public static void main (String [] args) {
		BitMap bmp = new BitMap("saved.bmp");
		byte [] pixelData = new byte[(int) bmp.getDimensions()];
		int k=0;
		for (int i=0; i<bmp.getHeight(); i++) {
	    	for (int j=0; j<bmp.getWidth(); j++) {
		    	pixelData[k++] = (byte) bmp.getPixel(i, j);
	    	}
		}
		
	    for (int i : pixelData) {
	    	if (i == 0) break;
	    	System.out.print((char) i);
	    }
		
	}
	
}
