/*
    Copyright 2005, 2005 Burcu Yildiz
    Contact: burcu.yildiz@gmail.com
    
    This file is part of pdf2table.    pdf2table is free software: you can redistribute it and/or modify    it under the terms of the GNU General Public License as published by    the Free Software Foundation, either version 3 of the License, or    (at your option) any later version.    pdf2table is distributed in the hope that it will be useful,    but WITHOUT ANY WARRANTY; without even the implied warranty of    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the    GNU General Public License for more details.    You should have received a copy of the GNU General Public License    along with pdf2table.  If not, see <http://www.gnu.org/licenses/>.
*/

package pdf2xml;

import java.awt.Button;
import java.awt.Color;
import java.awt.Frame;
import java.awt.Label;
import java.awt.TextField;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class EditTableContentFrame extends Frame {
	
TextField new_row;
TextField new_column;
TextField delete_row;
TextField delete_column;
Table table;
Table changed_table;

boolean add_new_column;
boolean delete_column_value;
int column_position;


  public EditTableContentFrame(Table t) {
		super("Edit Table");
		try {
        this.setSize(450,170);
        this.setLayout(null);
        this.setBackground(Color.LIGHT_GRAY);
        this.setLocationRelativeTo(null);
        
        this.add_new_column = false;
        this.delete_column_value = false;
        this.column_position = -1;
              
        this.table = t;
        this.addWindowListener( 
           new WindowAdapter() { 
             public void windowClosing( WindowEvent e ) {              
               Frame o = (Frame) e.getSource();
               o.dispose();
             } 
           } 
         );
                 
        Color color3 = new Color(203,203,203);        
                 
        Label l1 = new Label("Insert a new row at position");
        l1.setBounds(20,40,200,20);
        this.add(l1);
        
        this.new_row = new TextField();
  	    this.new_row.setBounds(225,40,20,20);
  	    this.add(this.new_row);
  	    
  	    Label l2 = new Label("Insert a new column at position");
  	    l2.setBounds(20,70,200,20);
  	    this.add(l2);
  	    
  	    this.new_column = new TextField();
  	    this.new_column.setBounds(225,70,20,20);
  	    this.add(this.new_column);
  	    
  	    Label l3 = new Label("Delete row at position");
  	    l3.setBounds(20,100,200,20);
  	    this.add(l3);  	   
  	    
  	    this.delete_row = new TextField();
  	    this.delete_row.setBounds(225,100,20,20);
  	    this.add(this.delete_row);
  	    
  	    Label l5 = new Label("Delete column at position");
  	    l5.setBounds(20,130,200,20);
  	    this.add(l5);
  	    
  	    this.delete_column = new TextField();
  	    this.delete_column.setBounds(225,130,20,20);
  	    this.add(this.delete_column);
  	    
	    Button ok_button = new Button("Ok");
	    ok_button.setBounds(310,40,120,20);
	    ok_button.setBackground(color3);
        ok_button.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ok();
            }
        });  	    
	    this.add(ok_button);
	    
	    Button close_button = new Button("Close");
	    close_button.setBounds(310,70,120,20);
	    close_button.setBackground(color3);
        close_button.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                close();
            }
        });  	    
	    this.add(close_button);	      	    
	 }
	 catch (Exception e) {
           System.out.println("Exception in class: EditTableContentFrame and method: constructor. " + e);	    		 	
	 }
  }	
	
	
  public void ok() {
	  if (!this.new_row.getText().equals("")) {
  	  Column c = this.table.columns.get(0);	  
  	  try {
  	     int row_position = Integer.parseInt(this.new_row.getText());
  	     if (row_position > 0 && row_position <= c.cells.size()) {
  	       
  	       	  for (int i=0;i<this.table.columns.size();i++) {
  	       	  	 Column current_c = this.table.columns.get(i);
  	       	  	 Text_Element dummy = new Text_Element();
  	       	  	 current_c.cells.add(row_position-1, dummy);  	       	  	 
  	       	  }
  	       
  	     }
  	  }
  	  catch (Exception e) {  
  	     System.out.println(e);	  	  	  	  	
  	  }	
  	}
  	if (!this.new_column.getText().equals("")) {
  	  try {
  	     int column_pos = Integer.parseInt(this.new_column.getText());
  	     if (column_pos <= this.table.columns.size() && column_pos > 0) {
  	       Column c = this.table.columns.get(this.table.columns.size()-1);
  	       Column new_column = new Column();
  	       for (int i=0;i<c.cells.size();i++) {
             Text_Element dummy = new Text_Element();  	       	             
             new_column.cells.add(dummy);             
  	       }  	     
           this.table.columns.add(column_pos-1,new_column);
  	     }  	     	
  	  }
  	  catch (Exception e) {  	 
  	   System.out.println(e);	  	  	   	
  	  }	
  	}
  	if (!this.delete_row.getText().equals("")) {
  	  try {      
        int row_position = Integer.parseInt(this.delete_row.getText());
        Column c = this.table.columns.get(0);
  	      if (row_position > 0 && row_position <= c.cells.size()) {  	       
  	       	  for (int i=0;i<this.table.columns.size();i++) {
  	       	  	 Column current_c = this.table.columns.get(i);
  	       	  	 current_c.cells.remove(row_position-1);
  	       	  }  	       
  	      }
  	     	
  	  }
  	  catch (Exception e) {  
  	   System.out.println(e);	  	
  	  }	
  	}  	
  	if (!this.delete_column.getText().equals("")) {
  	  try {
  	     int column_pos = Integer.parseInt(this.delete_column.getText());  	
  	     if (column_pos <= this.table.columns.size() && column_pos > 0) {
  	       this.table.columns.remove(column_pos-1);
  	     }    	
  	  }
  	  catch (Exception e) { 
  	   System.out.println(e);	  	  	   	  	
  	  }	
  	}  	
  

    this.changed_table = this.table;  
  	close();	
  }
  
  public void close() {
  	this.changed_table = this.table;
  	this.dispose();
  }
}