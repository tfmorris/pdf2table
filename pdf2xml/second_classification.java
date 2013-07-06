/*
    Copyright 2005, 2005 Burcu Yildiz
    Contact: burcu.yildiz@gmail.com
    
    This file is part of pdf2table.    pdf2table is free software: you can redistribute it and/or modify    it under the terms of the GNU General Public License as published by    the Free Software Foundation, either version 3 of the License, or    (at your option) any later version.    pdf2table is distributed in the hope that it will be useful,    but WITHOUT ANY WARRANTY; without even the implied warranty of    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the    GNU General Public License for more details.    You should have received a copy of the GNU General Public License    along with pdf2table.  If not, see <http://www.gnu.org/licenses/>.
*/

package pdf2xml;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

public class second_classification {
	PrintStream dos;
	PrintStream dos2;
	List<Font> fonts;
	List<Line> lines;
	List<Multiline_Block> multiline_blocks;	
	List<Table> tables;
	boolean interactive_extraction;
    String path;
    
	public second_classification(boolean interactivity,String p) {
        this.path = p;
     
	    this.fonts = new ArrayList<Font>();
		this.lines = new ArrayList<Line>();
		this.multiline_blocks = new ArrayList<Multiline_Block>();
		this.tables = new ArrayList<Table>();
		this.interactive_extraction = interactivity;
	}	
	
	public void run() {
	  try {
       decompose_tables();        	       
       
       if (this.interactive_extraction == true) {
          semi_output so = new semi_output(this.tables, this.fonts, this.path);
          so.setVisible(true);
       }
       else {
          xml_output xml_o = new xml_output(this.tables, this.fonts, this.path);      
       }             
      } 
      catch (Exception e) {
        System.out.println("Exception in class: second_classification and method: run. " + e);	    		 	    
      }
    } // end of run
   
   
    public void decompose_tables() {
   	
   	  for (int i=0;i<this.multiline_blocks.size();i++) {
   	  	 Multiline_Block mlb = (Multiline_Block) this.multiline_blocks.get(i);   	  	 
   	  	 int lines_before = 0;   	  	   
   	  	 int line_count = 0;
   	  	 
   	  	 if (mlb.end - mlb.begin >= 2) {
	     // multiline blocks with less than 3 lines will be ignored
   	  	   int b = mlb.begin;
		
				
		   MyNode root = new MyNode("root",-1);
		   
		   while (b<=mlb.end) {
   	  	     Line l = this.lines.get(b);   	  	        	  	     
			 
 	   	   	 for (int j=0;j<l.texts.size();j++) {
 	   	  	 	Text_Element t = (Text_Element) l.texts.get(j);
				if (t.artificial) {
				}
 	   	  	    insert_into_tree(t,root,lines_before);
 	   	  	 }	

 	   	  	
   	  	     b++;	   	  	   
   	  	     lines_before++;
   	  	   } // end of while (b<=mlb.end)
   	  	   
		   
	       print_tree(root);

		   

   	       Table new_table = new Table();
		   
		   convert_to_table(root, null, new_table.columns, lines_before);
		     

		   
		  for (int k=0; k< new_table.columns.size() -1; k++) {
		      Column c1 = new_table.columns.get(k);
			  Column c2 = new_table.columns.get(k+1);
			  
		    Column nc = (Column) c1.clone();
			  if (c1.left <= c2.left && c1.right >= c2.left) {
			    // merge columns because they overlap
				boolean merge = true;
				for (int j=0; j<c1.cells.size();j++) {
			
				  Text_Element t1 = (Text_Element) c1.cells.get(j);
				  Text_Element t2 = (Text_Element) c2.cells.get(j);
				  Text_Element nt = (Text_Element) nc.cells.get(j);
			
					
				  if (t1.value.equals("null") || t2.value.equals("null") ) {	
				    String new_value = "";
					if (!t1.value.equals("null")) {
					  nt.value = t1.value;
					  if (t1.colspan > 1) {
					    nt.colspan--;
					  }
					  else {
					   actualize_column_values(nc, t1);
					  }

					}
					else {
					  nt.value = t2.value;
					  if (t2.colspan > 1) {
					    nt.colspan--;
					  }
					  else {
					    actualize_column_values(nc,t2);
					  }
					}
				  }
				  else {
				     merge = false;
					 break;
				  }
				 }
				
				
				if (merge == true) {
				 new_table.columns.add(k, nc);
				 new_table.columns.remove(k+1);
				 new_table.columns.remove(k+1);
				}
			  }
		   }
		   
		   new_table.datarow_begin = 0; //data_row_begin;	 
		   
		   boolean header = true;
		   int sum = 0;
		   if (new_table.columns.size() > 0) {
		     Column c1 = new_table.columns.get(0);
			 int k=0;
			 
			while (header == true &&  k < c1.cells.size()) {
			   for (int m=0; m < new_table.columns.size(); m++) {
			     Column current_c = new_table.columns.get(m);
			     Text_Element t = (Text_Element) current_c.cells.get(k);

				 if (t.artificial == false ) {
				   sum = sum + t.colspan;
				   if (sum >= mlb.max_elements) {
		
				     header = false;
					 new_table.datarow_begin = k+1;
				   }
				 }
			   }
			   k++;
			 }
		   }

		
   	        	 
  	 
   	       new_table.page = mlb.page;

   	       this.tables.add(new_table);   

   	  	 } // end of "if more than 3 lines in multiline block"
   	          
	 }    	     	
   }

   public boolean insert_into_tree(Text_Element t, MyNode n, int l) {
   
   
	    if (n.content.equals("null")) {
		
		  if (insert_into_tree(t,(MyNode) n.nodes.get(0),l))  {
		    return true;
		  }
		}
		else {
		  if (in_boundaries(t.left, t.right, n.left, n.right) || n.content.equals("root")) {
			int pos = 0;
	
			for (int i=0;i<n.nodes.size();i++) {
			
			  MyNode next = (MyNode) n.nodes.get(i);
			  // it was t.left > next.right. which means completely on the right side
			  if (t.left > next.left) { pos++; }
			  	
			  if ((in_boundaries(t.left, t.right, next.left, next.right) && next.level < l) || next.content.equals("null")) {
			    if (insert_into_tree(t,next,l)) {
				  return true;
			    }
			  }
			} // end of for
			

			for (int j=n.level; j < l-1; j++) {
			  MyNode dummy = new MyNode("null", t, j+1);
			  n.nodes.add(pos, dummy);
			  n = dummy;
			  pos = 0;
			} 
			MyNode current = new MyNode(t,l);
			n.nodes.add(pos,current);
			return true;
			
		  }
		
		}
		
    return false;
   
   }
   
   public void print_tree(MyNode n) {
   

	for (int i=0;i<n.nodes.size();i++) {
	   
	   print_tree((MyNode) n.nodes.get(i));
	
	}
   }
   
   public int convert_to_table(MyNode n, Column c, List<Column> v, int l) {

     if (c == null) {
	 // root node
	    int spanning =0;
	    for (int i=0; i < n.nodes.size(); i++) {
		  Column new_column = new Column();
		  v.add(new_column);
		  spanning += convert_to_table((MyNode) n.nodes.get(i), new_column, v, l);
		}
		return spanning;
	 }
	 else {
	 // not root node
	    int pos = 0;
	    if (!n.content.equals("null")) {
	      c.cells.add(n.text_element);		
		  pos = c.cells.size();
		  if (n.text_element.colspan == 1) {
		      actualize_column_values(c, n.text_element);
		  }
		}
		else {
		  Text_Element t = new Text_Element();
		  c.cells.add(t);
		  pos = c.cells.size();
		}

		if (n.nodes.size() >= 1) {
		  Column store = (Column) c.clone();
		  int spanning = 0;
		  
		  spanning += convert_to_table((MyNode) n.nodes.get(0), c, v, l);

		  for (int i=1; i < n.nodes.size(); i++) {
		    Column new_column = new Column();
			new_column.cells.addAll(store.cells);
			v.add(new_column);
			spanning += convert_to_table((MyNode) n.nodes.get(i), new_column, v, l);
		  }
		  
		  Text_Element t = (Text_Element) c.cells.get(pos-1);
		  t.colspan = spanning;
		  
		  return spanning;
		}
		else {
		// no children means that we are at the leaf of a branch
		    while (c.cells.size() < l) {
			  Text_Element t = new Text_Element();
			  c.cells.add(t);
			}
			return 1;
		}
	 }
    
   }
   
   
   public void actualize_column_values(Column c, Text_Element t) {
   	 if (c.left ==  -1) {
       c.left = t.left;
	  }
	  else {
	   Math.min(c.left,t.left);
     } 
	 
     c.right = Math.max(c.right,t.left + t.width);
   }
   
   public void actualize_column_values_with_another_column(Column c1, Column c2) {
     c1.left = Math.min(c1.left,c2.left);
     c1.right = Math.max(c1.right,c2.right);
   }
   
   public boolean in_boundaries(int l1, int r1, int l2, int r2) {
      if ((l1 >= l2 && r1 <= r2) || 
	      (l1 >= l2 && l1 <= r2 && r1 > r2) ||
		  (l1 < l2 && r1 >= l2 && r1 <= r2) ||
		  (l2 >= l1 && r2 <= r1))  {
		  return true;
		  
	  }
	  return false;
   }
   
 }