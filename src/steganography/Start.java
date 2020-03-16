/** @file Start.java
* @brief Use this to start the steganography application
*
* @author Wasaif ALsolami, 2415072A
* @author Ebtihal Althubiti, 2414366A
* @author Antonios Stamatis, 2479716S
* 
*/

package steganography;
import java.awt.GridLayout;
import java.awt.event.*;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;




public class Start  extends JFrame implements ActionListener {
	private	JPanel	panal;
	private	JButton Hiding_LSB,Hiding_BPCS,Extract_LSB,Extract_BPCS,Exit;
	public static void main(String[] args) {	
		// TODO Auto-generated method stub
		new Start();

	}

	public Start()
	{
		
		setTitle("****Steganography****");
		setSize(300,300);
		setLocation(600,300);
		
		panal = new JPanel(new GridLayout(5,3));
		add(panal, "Center");
		
		Hiding_LSB= new JButton("Hiding the file by using LSB");
		Hiding_LSB.addActionListener(this);
		panal.add(Hiding_LSB);
		
		Hiding_BPCS= new JButton("Hiding the file by using BPCS");
		Hiding_BPCS.addActionListener(this);
		panal.add(Hiding_BPCS);
		
		Extract_LSB = new JButton("Extract the file by using LSB");
		Extract_LSB.addActionListener(this);
		panal.add(Extract_LSB);
		
		Extract_BPCS= new JButton("Extract the file by using BPCS");
		Extract_BPCS.addActionListener(this);
		panal.add(Extract_BPCS);
		
		Exit= new JButton("EXIT");
		Exit.addActionListener(this);
		panal.add(Exit);
		
		setVisible(true);	
		
	}

 public void actionPerformed(ActionEvent event)
	{
	JButton	button = (JButton)event.getSource();
	
	if (button == Hiding_LSB)
		
	{
		String	image_name = JOptionPane.showInputDialog(null, "Enter The Name of the image with extension (eg. imageName.bmp)");
	    String	file_name  = JOptionPane.showInputDialog(null, "Enter The Name of the file you want to hide. with extension (eg. file.txt)");
	    LSB_encrypt.read_image_file(image_name, file_name);
	    
	}
	
	else if (button == Hiding_BPCS)
	{
		String	image_name = JOptionPane.showInputDialog(null, "Enter The Name of the image with extension (eg. imageName.bmp)");
	    String	file_name  = JOptionPane.showInputDialog(null, "Enter The Name of the file you want to hide. with extension (eg. file.txt)");
	    BPCS_encrypt.read_image_file(image_name, file_name);
	}
	
	else if (button == Extract_LSB)
	{
		String	image_name = JOptionPane.showInputDialog(null, "Enter The Name of the vessel image with extension (eg. savedLSB.bmp)");
	    String	form  = JOptionPane.showInputDialog(null, "Enter the extension of the file hidden file (eg. txt)");
	    LSB_decrypt.read_image_file(image_name, form);
	}
	
	else if (button == Extract_BPCS)
	{
		String	image_name = JOptionPane.showInputDialog(null, "Enter The Name of the vessel image with extension (eg. savedBPCS.bmp)");
	    String	form  = JOptionPane.showInputDialog(null, "Enter the extension of the file hidden file (eg. txt)");
	    BPCS_decrypt.read_image_file(image_name, form);
	}
	
	else if (button == Exit)
		System.exit(0);
	}
}
