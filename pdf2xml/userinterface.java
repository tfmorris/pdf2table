/*
    Copyright 2005, 2005 Burcu Yildiz
    Contact: burcu.yildiz@gmail.com
    
<<<<<<< HEAD
    This file is part of pdf2table.    pdf2table is free software: you can redistribute it and/or modify    it under the terms of the GNU General Public License as published by    the Free Software Foundation, either version 3 of the License, or    (at your option) any later version.    pdf2table is distributed in the hope that it will be useful,    but WITHOUT ANY WARRANTY; without even the implied warranty of    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the    GNU General Public License for more details.    You should have received a copy of the GNU General Public License    along with pdf2table.  If not, see <http://www.gnu.org/licenses/>.
=======
    This file is part of pdf2table.

    pdf2table is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    pdf2table is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with pdf2table.  If not, see <http://www.gnu.org/licenses/>.
>>>>>>> mac_original
*/

package pdf2xml;

import java.awt.Button;
import java.awt.Checkbox;
import java.awt.Color;
import java.awt.Dialog;
import java.awt.FileDialog;
import java.awt.Frame;
import java.awt.Label;
import java.awt.TextField;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class userinterface extends Frame{
	
private TextField tf1;
private TextField tf2;
private TextField tf3;
private TextField tf4;
private Button browse1;
private Button browse2;
private Button cancel;
private Button extract;
private Checkbox cb1;
private String f_name;

	public userinterface() {
    try {
        this.setSize(350,287);
        this.setLayout(null);
        this.setBackground(Color.LIGHT_GRAY);
 
        this.addWindowListener( 
           new WindowAdapter() { 
             public void windowClosing( WindowEvent e ) { 
               Runtime.getRuntime().exit( 0 ); 
             } 
           } 
         );   
               

        Label l0 = new Label("Table Extractor 1.0");
        l0.setAlignment(Label.CENTER);
        l0.setBounds(0,40,373,20);

        Label l1 = new Label("Source File");
        l1.setBounds(25,65,100,20);

        this.tf1 = new TextField();
        tf1.setBounds(25,88,238,25);
               
        this.browse1 = new Button("Browse");
        browse1.setBounds(270,88,55,25);
     
        browse1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                browse1ActionPerformed(evt);
            }
        });
                     
        Label l2 = new Label("Target Directory");
        l2.setBounds(25,130,100,20);

        this.tf2 = new TextField();
        tf2.setBounds(25,153,238,25);

        this.browse2 = new Button("Browse");
        browse2.setBounds(270,153,55,25);

        browse2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                browse2ActionPerformed(evt);
            }
        });
     
        Label l3 = new Label("Extract from page ");
        l3.setBounds(25,183,135,20);
        
        this.tf3 = new TextField();
        this.tf3.setBounds(165,183,30,20);
        
        Label l4 = new Label("to");
        l4.setBounds(199,183,30,20);
        l4.setAlignment(Label.CENTER);
        
        this.tf4 = new TextField();
        this.tf4.setBounds(233,183,30,20);
        
        this.cb1 = new Checkbox("interactive extraction");
        cb1.setBounds(25,210,230,20);
        cb1.setState(true);
        
        this.cancel = new Button("Exit");
        cancel.setBounds(25,240,50,25);
 
        cancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelActionPerformed(evt);
            }
        });        
     
        this.extract = new Button("Extract Table(s)");
        extract.setBounds(90,240,235,25);

        extract.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                extractActionPerformed(evt);
            }
        });
    
        // add components to the frame
        this.add(l0);
        this.add(l1);
        this.add(l2);
        this.add(l3);
        this.add(l4);
        this.add(this.tf1);
        this.add(this.tf2);
        this.add(this.tf3);
        this.add(this.tf4);
        this.add(browse1);
        this.add(browse2);
        this.add(cb1);
        this.add(cancel);
        this.add(extract);
        
        this.setLocationRelativeTo(null);
    }
    catch (Exception e) {
           System.out.println("Exception in class: user_interface and method: constructor. " + e);	    		 	
    }     
    }	
 
   private void browse1ActionPerformed(java.awt.event.ActionEvent evt) {
     
      Frame f0 = new Frame();
      try {
      FileDialog file_dialog = new FileDialog(f0,"Source File");
      file_dialog.setDirectory("C:");
      file_dialog.setVisible(true);
      String file_name = file_dialog.getFile();
      String file_directory = file_dialog.getDirectory();
      String target_file;
      if (file_name.endsWith(".pdf")) {      	
      	int i = file_name.indexOf(".pdf");
      	target_file = file_name.substring(0,i);
      	this.f_name = target_file;
      	this.tf2.setText(file_directory + target_file);
        String path = file_directory + file_name;
        this.tf1.setText(path);             	
     }
     else {
     	Dialog d = new Dialog(this,"Error",true);
     	d.add("Center",new Label("The source file must be a PDF file (example.pdf)!"));
     	Button b = new Button("Ok");  	
     	b.addActionListener(new java.awt.event.ActionListener() {
     	  public void actionPerformed(java.awt.event.ActionEvent evt) {
            Button b2 = (Button) evt.getSource();
            ((Dialog)b2.getParent()).dispose();
          }
        });
     	d.setSize(200,100);     	
     	d.add("South",b);
     	d.setLocationRelativeTo(null);
     	d.setVisible(true);
     }
      
     }
     catch (NullPointerException npe) {
        System.out.println("Exception in class: user_interface and method: browse1. " + npe);	    		 	     	
     }
     catch (Exception e) {
        System.out.println("Exception in class: user_interface and method: browse1. " + e);	    		 	
     }       
   }
   
   private void browse2ActionPerformed(java.awt.event.ActionEvent evt) {
     try {
      Frame f0 = new Frame();
      FileDialog file_dialog = new FileDialog(f0,"Target Directory");
      file_dialog.setDirectory("C:");
      file_dialog.setVisible(true);
      String file_directory = file_dialog.getDirectory();
      this.tf2.setText(file_directory);
    }
    catch (Exception e) {
        System.out.println("Exception in class: user_interface and method: browse2. " + e);	    		 	
    }
    }

    private void cancelActionPerformed(java.awt.event.ActionEvent evt) {
        System.exit(0);
    }
    
    private void extractActionPerformed(java.awt.event.ActionEvent evt) {
    	try {
        boolean interactive_extraction = this.cb1.getState();
        
        String source = this.tf1.getText();        
        String target = this.tf2.getText();
        String from_page = this.tf3.getText();
        String to_page = this.tf4.getText();
        if (source.equals("") || target.equals("")) {
     	  Dialog d = new Dialog(this,"Error",true);
     	  d.add("Center",new Label("Please select a file and a target directory."));
     	  Button b = new Button("Ok");  	
     	  b.addActionListener(new java.awt.event.ActionListener() {
     	    public void actionPerformed(java.awt.event.ActionEvent evt) {
              Button b2 = (Button) evt.getSource();
              ((Dialog)b2.getParent()).dispose();
            }
          });
          d.setSize(250,100);     	
     	  d.add("South",b);
     	  d.setLocationRelativeTo(null);
     	  d.setVisible(true);        	
        }
        else {
        	
        execute_converter ec = new execute_converter(this.f_name,source,target,from_page,to_page,interactive_extraction);	         
        }
    }
    catch (Exception e) {
        System.out.println("Exception in class: user_interface and method: extract. " + e);	    		 	
    }    	
    } 
   
}