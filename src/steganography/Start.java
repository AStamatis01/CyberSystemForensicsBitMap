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
		
		setTitle("Steganography");
		setSize(400,400);
		setLocation(200,200);
		
		panal = new JPanel(new GridLayout(5,3));
		add(panal, "Center");
		
		Hiding_LSB= (JButton) add_button("Hiding the file by using LSB");
		Hiding_BPCS= (JButton) add_button("Hiding the file by using BPCS");
		Extract_LSB = (JButton) add_button("Extract the file by using LSB");
		Extract_BPCS= (JButton) add_button("Extract the file by using BPCS");
		Exit= (JButton) add_button("EXIT");
		
		setVisible(true);	
		
	}

private Object add_button(String choice) {
		JButton	button = new JButton(choice);
		button.addActionListener(this);
		panal.add(button);
		return button;
	}
 public void actionPerformed(ActionEvent event)
	{
	JButton	button = (JButton)event.getSource();
	
	if (button == Hiding_LSB)
		
	{
		String	image_name = JOptionPane.showInputDialog(null, "Enter The Name of the image withe extintion(bmp)");
	    String	file_name  = JOptionPane.showInputDialog(null, "Enter The Name of the file withe extintion");
	    LSB_encrypt.read_image_file(image_name, file_name);
	    
	}
	
	else if (button == Hiding_BPCS)
	{
		String	image_name = JOptionPane.showInputDialog(null, "Enter The Name of the image withe extintion(bmp)");
	    String	file_name  = JOptionPane.showInputDialog(null, "Enter The Name of the secret file withe extintion");
	    BPCS_encrypt.read_image_file(image_name, file_name);
	}
	
	else if (button == Extract_LSB)
	{
		String	image_name = JOptionPane.showInputDialog(null, "Enter The Name of the embedded image withe extintion(bmp)");
	    String	form  = JOptionPane.showInputDialog(null, "Enter the extintion of the file");
	    LSB_decrypt.read_image_file(image_name, form);
	}
	
	else if (button == Extract_BPCS)
	{
		String	image_name = JOptionPane.showInputDialog(null, "Enter The Name of the embedded image withe extintion(bmp)");
	    String	form  = JOptionPane.showInputDialog(null, "Enter the extintion of the secret file");
	    BPCS_decrypt.read_image_file(image_name, form);
	}
	
	else if (button == Exit)
		System.exit(0);
	}
}
