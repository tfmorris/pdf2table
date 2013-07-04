/*
    Copyright 2005, 2005 Burcu Yildiz
    Contact: burcu.yildiz@gmail.com
    
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
*/

package pdf2xml;

import java.awt.*;
import java.awt.event.*;
import java.awt.event.WindowEvent;
import java.awt.event.WindowAdapter; 
import java.util.*;
import java.lang.*;


public class edit_table_cell extends Frame{
	
Text_Element my_text;	
Choice my_choice;
TextArea ta;
CheckboxGroup cbg;
TextField colspan_tf;
int max;
int changed_index;
boolean control;
	
boolean same;
boolean merge_l;
boolean merge_r;
boolean merge_u;
boolean merge_d;
Checkbox cb1;
Checkbox cb2;
Checkbox cb3;
Checkbox cb4;
Checkbox cb5;
Table t;

	public edit_table_cell(Table t, int index) {
	  super("Edit Table Cell");
      try {		
        this.setSize(460,420);
        this.setLayout(null);
        this.setBackground(Color.LIGHT_GRAY);
        this.setLocationRelativeTo(null);
        
		this.t = t;
        int column_pos = index % t.columns.size();
        int row_pos = index/t.columns.size();
        
        Column current_column = (Column) t.columns.elementAt(column_pos);
        this.my_text = (Text_Element) current_column.cells.elementAt(row_pos);
 	  	// true stands for header element
     
        this.addWindowListener( 
           new WindowAdapter() { 
             public void windowClosing( WindowEvent e ) {              
               Frame o = (Frame) e.getSource();
               o.dispose();
             } 
           } 
         );
		Color panel_background_color = new Color(217,217,217);
        this.changed_index = index;
         
		Panel main_panel = new Panel();
		main_panel.setBounds(0,0,460,420);
		main_panel.setBackground(panel_background_color);
		main_panel.setLayout(null);
		
		Panel cell_panel = new Panel();
		cell_panel.setSize(300,420);
		cell_panel.setLayout(null);
		cell_panel.setBackground(panel_background_color);
		
		Label content = new Label("Content");
		content.setBounds(20,30,80,20);
		cell_panel.add(content);
		this.ta = new TextArea(this.my_text.value,1,1,TextArea.SCROLLBARS_NONE);
		ta.setBounds(20,55,260,200);
		cell_panel.add(ta);
	
		
        this.cbg = new CheckboxGroup();
        this.cb1 = new Checkbox("just the same", cbg, true);        
        this.cb2 = new Checkbox("merge with left", cbg, false);
        this.cb3 = new Checkbox("merge with right", cbg, false);
        this.cb4 = new Checkbox("merge with up", cbg, false);
        this.cb5 = new Checkbox("merge with down", cbg, false);        
        cb1.setBounds(190,270,110,20);
        cb2.setBounds(190,290,110,20);
        cb3.setBounds(190,310,110,20);
        cb4.setBounds(190,330,110,20);
        cb5.setBounds(190,350,110,20);
        
		cell_panel.add(cb1);	
		cell_panel.add(cb2);	
		cell_panel.add(cb3);	
		cell_panel.add(cb4);	
		cell_panel.add(cb5);	
	    
	
		Label colspan2 = new Label("Columns spanning");
	    colspan2.setBounds(20,270,120,20);
	    cell_panel.add(colspan2);     
             
        this.colspan_tf = new TextField();
        this.colspan_tf.setBounds(20,295,40,20);
		this.colspan_tf.setText("" + this.my_text.colspan);
        cell_panel.add(this.colspan_tf);
        
		Panel button_panel = new Panel();
		button_panel.setBounds(300,0,160,420);
		button_panel.setBackground(panel_background_color);
	    button_panel.setLayout(null);
	    
        Color color3 = new Color(203,203,203);        
	
	    Button ok_button = new Button("Ok");
	    ok_button.setBounds(20,40,120,20);
	    ok_button.setBackground(color3);
        ok_button.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ok();
            }
        });  	    
	    button_panel.add(ok_button);
	    
	    Button cancel_button = new Button("Cancel");
	    cancel_button.setBounds(20,80,120,20);
	    cancel_button.setBackground(color3);
	    button_panel.add(cancel_button);	    
        cancel_button.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
               cancel();
            }
        });  
       	    
		this.same = false;
		this.merge_l = false;
		this.merge_r = false;
		this.merge_u = false;
		this.merge_d = false;      
		 	    
	    main_panel.add(cell_panel);
		main_panel.add(button_panel);
        this.add(main_panel);
    }
    catch (Exception e) {
           System.out.println("Exception in class: edit_table_cell and method: constructor. " + e);	    		
    }
		
	}
	
	public void cancel() {
        this.dispose();		
	}
	
	public void ok() {
	
		try {
		this.my_text.value = this.ta.getText();
        
	    if (!this.colspan_tf.getText().equals("")) { 	
		  try {
		    int cs = Integer.parseInt(this.colspan_tf.getText());
            if (cs >0 && cs <= this.t.columns.size()) {
            	this.my_text.colspan = cs;
            }   		    
		  }
		  catch (Exception e) {
		  }	
	    }

	    if (this.cb1.getState() == false) {
	    	this.same = false;
	    }
	    else {
	    	this.same = true;
	    }
	    if (this.cb2.getState() == false) {
	    	this.merge_l = false;
	    }
	    else {
	    	this.merge_l = true;
	    }
	    if (this.cb3.getState() == false) {
	    	this.merge_r = false;
	    }
	    else {
	    	this.merge_r = true;
	    }
	    if (this.cb4.getState() == false) {
	    	this.merge_u = false;
	    }
	    else {
	    	this.merge_u = true;
	    }
	    if (this.cb5.getState() == false) {
	    	this.merge_d = false;
	    }
	    else {
	    	this.merge_d = true;
	    }
	    
	    }
	    catch (Exception e) {
           System.out.println("Exception in class: edit_table_cell and method: ok. " + e);	    	
	    }	    	    	    	    
	    cancel();
	}
}
