/*
    Copyright 2005, 2005 Burcu Yildiz
    Contact: burcu.yildiz@gmail.com
    
    This file is part of pdf2table.    pdf2table is free software: you can redistribute it and/or modify    it under the terms of the GNU General Public License as published by    the Free Software Foundation, either version 3 of the License, or    (at your option) any later version.    pdf2table is distributed in the hope that it will be useful,    but WITHOUT ANY WARRANTY; without even the implied warranty of    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the    GNU General Public License for more details.    You should have received a copy of the GNU General Public License    along with pdf2table.  If not, see <http://www.gnu.org/licenses/>.
*/

package pdf2xml;

import java.awt.Button;
import java.awt.Dialog;
import java.awt.Frame;
import java.awt.GraphicsEnvironment;
import java.awt.Label;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.ListIterator;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;


class top_comparator implements Comparator<Element>  {
	  
	  public int compare(Element e1, Element e2) {
	    int top1 = Integer.parseInt(e1.getAttribute("top").getValue());            	          	     
	    int top2 = Integer.parseInt(e2.getAttribute("top").getValue());            	          	     
	    
	    return (top1 - top2);
	  }
	  
}

class top_comparator_for_texts implements Comparator<Text_Element>  {
	  
	  public int compare(Text_Element t1, Text_Element t2) {
	    return (t1.top - t2.top);
	  }
	  
}

class left_comparator implements Comparator<Text_Element>  {
	  
	  public int compare(Text_Element t1, Text_Element t2) {
	    return (t1.left - t2.left);
	  }
}

public class first_classification {
	
List<Font> fonts;
List<Line> lines;
//List font_counter;
List<Multiline_Block> mlbs;
boolean interactive_extraction;
String path;
String pdftohtml_file_name = "";
PrintStream dos;
int page_text_columns_count;
List<Text_Column> text_columns;
int removed_elements_before;
int removed_elements_after;

boolean modus;

int distance_sum = 0;

List<Line> current_text_elements;

	public first_classification(boolean interactivity, String p) {//, int c) {
        this.fonts = new ArrayList<Font>();
        this.lines = new ArrayList<Line>();
//        this.font_counter = new List();
        this.mlbs = new ArrayList<Multiline_Block>();
        this.interactive_extraction = interactivity;
        this.path = p;
        this.modus = false;
        this.page_text_columns_count = 1;
        this.text_columns = new ArrayList<Text_Column>();
        this.removed_elements_before = 0;
        this.removed_elements_after = 0;
    }
    
	public void run(String file_name) {

    this.pdftohtml_file_name = file_name;
    SAXBuilder builder = new SAXBuilder();
    
    try {
      Document doc = builder.build(file_name);
      Element root = doc.getRootElement();
      List<Element> pages = root.getChildren("page");      

      ListIterator<Element> li_pages = pages.listIterator();

      int lines_before = 0;      
    
      while (li_pages.hasNext()) {
      	this.text_columns.clear();
      	
      	Element page = (Element) li_pages.next();
      	int page_number = Integer.parseInt(page.getAttribute("number").getValue());
      	int page_height = Integer.parseInt(page.getAttribute("height").getValue());
      	int page_width = Integer.parseInt(page.getAttribute("width").getValue());

        int text_columns_width = page_width/this.page_text_columns_count;
        
        for (int i=0;i<this.page_text_columns_count;i++) {
           Text_Column tc = new Text_Column(text_columns_width);
           this.text_columns.add(tc);	
        }
        
      	List<Element> current_font_elements = page.getChildren("fontspec");
      	ListIterator<Element> page_fonts = current_font_elements.listIterator();

      	while(page_fonts.hasNext()) {
      	   Element font = (Element) page_fonts.next();
      	   int id = Integer.parseInt(font.getAttribute("id").getValue());      	        	   
      	   int size = Integer.parseInt(font.getAttribute("size").getValue());
      	   String family = font.getAttribute("family").getValue();
      	   String color = font.getAttribute("color").getValue();
           Font f = new Font(page_number,id,size,family,color);
           this.fonts.add(f);
        } // end of while
      
      	this.current_text_elements  = page.getChildren("text"); 
      	Element[] e_array = new Element[this.current_text_elements.size()];
      	this.current_text_elements.toArray(e_array);
        Arrays.sort(e_array, new top_comparator());

        int distance = 0;      
        Text_Column current_tc;
    
        for (int j=0;j<e_array.length;j++) {
        	
           Element e = e_array[j];
           Text_Element current_t = getTextElement(e);
           
           int right_column = Math.abs(current_t.left/text_columns_width);
           
           if (right_column < this.text_columns.size()) {
           
           current_tc = this.text_columns.get(right_column);
           
           if (current_tc.lines.size() > 0) {
                Line l = (Line) current_tc.lines.get(current_tc.lines.size()-1);
                
              	if (in_the_line(current_t, l)) {
              	  // exactly in the boundaries of the line
              	  l.texts.add(current_t);
                  actualize_line_values(current_t,l);
              	}
              	else {    
              	  Line new_line = new Line();              	  
              	  new_line.texts.add(current_t);
                  set_new_line_values(current_t,new_line);                	  
              	  current_tc.lines.add(new_line);
              	  distance += new_line.first_top - l.last_top;          		                              	  	
              	}
           }
           else {
              	  Line new_line = new Line();
              	  new_line.texts.add(current_t);
                  set_new_line_values(current_t,new_line);              	  	             	              	 
              	  current_tc.lines.add(new_line);
           } // if current_tc.lines               
           } // if right_column ...
           
        } // for e_array.length

        for (int p=0;p<this.text_columns.size();p++) {
        	Text_Column tc = this.text_columns.get(p);
            this.lines.addAll(tc.lines);                	
        }
        
        
        boolean multi_modus = false;        
        int d = 0;
        int sum_of_distances = 0;

        
        for (int o=lines_before;o<this.lines.size();o++) {
        	
        	Line l = this.lines.get(o);
        	Text_Element[] t_array = new Text_Element[l.texts.size()];
        	l.texts.toArray(t_array);        	 
        	Arrays.sort(t_array, new left_comparator());
        	l.texts.clear();
        	l.texts.addAll(Arrays.asList(t_array));       
        	   	  
        	int p = 0; 
        	  	  
        	while (p<l.texts.size()-1) {
        	     Text_Element t = l.texts.get(p);
        	     Text_Element n = l.texts.get(p+1);
        	     
        	     int result = belonging_together(t,n);
     
        	     if (result != -1) {
        	       l.texts.remove(p+1);
        	       if (result == 1) {
        	        if (t.elements.size() == 0) {
        	       	  t.elements.add(t);
        	          actualize_text_values(t,n);     
        	        }        	               	       
        	        if (n.value.length() > 0) {        	       	    	              	       
        	          t.elements.add(n);
        	          actualize_text_values(t,n);        	          
        	        }  
        	       }
        	       else if (result == 0) {
        	       	 t.value = t.value + " " + n.value;
        	         actualize_text_values(t,n);        	       	 
        	       }
        	       p--;        	      
        	     }
        	     p++;
          	  } 
          	  
              for (int h=0;h<l.texts.size();h++) {
              	Text_Element t = l.texts.get(h);
              	
              	if (t.elements.size() > 0) {
              	  Text_Element[] t_array2 = new Text_Element[t.elements.size()];
             	  t.elements.toArray(t_array2);                	       	               	               	  

              	  Arrays.sort(t_array2, new top_comparator_for_texts());              	                	  
              	  String value = "";
             	  for (int u=0;u<t_array2.length;u++) {
             	  	value += t_array2[u].value + " ";
             	  }
              	  t.value = value;    
              	  t.elements.clear();           	
              	}              	
              } 
              	              	
              if (l.texts.size() > 1) {
              	// multi-line
                if (multi_modus == true) {                	
                  Multiline_Block current_mlb = this.mlbs.get(this.mlbs.size()-1);
                  sum_of_distances += d;
                  actualize_mlb_values(current_mlb, l);                  
                }
                else {
                  Multiline_Block mlb = new Multiline_Block();
                  sum_of_distances = 0;
                  set_mlb_values(mlb,l,o,page_number);                  
                  this.mlbs.add(mlb);
                  multi_modus = true;
                }
              }
              else if (l.texts.size() == 1) {
              	// single-line
                if (multi_modus == true) {
                	
               	  Line pl = this.lines.get(o-1);
                  sum_of_distances += d;
        	   	  Text_Element t = l.texts.get(0);
        	   	  int top_distance = l.first_top - pl.bottom;
                  
                  boolean control = false;
				
				  int belongs = 0;
				  
        	      for (int k=0;k<pl.texts.size();k++) {
        	   	
        	   	     Text_Element n = pl.texts.get(k);
        	   	     int left_distance = Math.abs(n.left - t.left);
        	   	     int right_distance = Math.abs((n.left + n.width) - (t.left+t.width));
                  
        	   	    if (top_distance < t.height/2 && n.typ.equals(t.typ) && n.typ.equals("text") && ((left_distance < 3) || (right_distance < 3))) {
				
					 String s = n.value + "\n" + t.value;
        	   	  	 n.value = s;

        	   	  	 n.count_lines++;
        	   	  	 this.lines.remove(o);
        	   	  	 o--;
        	   	  	 actualize_text_values(n,t);              	   	  	 
                     actualize_line_values(t,pl);        	   	  	 
        	   	  	 control = true;
        	   	    }  
					if (in_boundaries(t,n) == 1) {
					    belongs++; 
					 }
        	   	 } // end of for
                 if (control == false) {
				 
					
				/*	if (belongs == 1)  {
                        Multiline_Block current_mlb = (Multiline_Block) this.mlbs.lastElement();
                        actualize_mlb_values(current_mlb, l);
					 }			 
					 else {*/
					 //if (belongs == 0 || count_single_lines > 5) {
                       Multiline_Block current_mlb = this.mlbs.get(this.mlbs.size());                
                       int mlb_element_count = current_mlb.end - current_mlb.begin;
                       if (mlb_element_count > 0) {
                         current_mlb.avg_distance = sum_of_distances/mlb_element_count;
                       }
                       else {
                         current_mlb.avg_distance = d;                  	
                       }
                       multi_modus = false;      
					// }    	   	 
                  }
        	   }	
        	   else {
        	   	// do nothing
         	   }
              }
              
          
          }
               	        	                
          multi_modus = false;        		
          lines_before = this.lines.size();
      } // end of while pages
      

     multiline_block_merge();

     second_classification sc = new second_classification(this.interactive_extraction, this.path);
      
     sc.fonts = new ArrayList<Font>(this.fonts);
     sc.lines = new ArrayList<Line>(this.lines);
//     sc.font_counter = (List) this.font_counter.clone();
     sc.multiline_blocks = new ArrayList<Multiline_Block>(this.mlbs);
     sc.run();
    
    }
    catch (JDOMException e) { 
      System.out.println(e.getMessage());

      GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
      Frame f = new Frame(ge.getDefaultScreenDevice().getDefaultConfiguration());
      
      Dialog d = new Dialog(f, "Failure", true);
   	  	 Label l = new Label("pdftohtml was unable to return right data.");
   	  	 Label l2 = new Label("Would you like to restart with pre-debugging?");
   	  	 d.setLayout(null);
   	  	 l.setBounds(60,50,300,20);
   	  	 l2.setBounds(60,70,300,20);
   	  	 d.add(l);
   	  	 d.add(l2);
   	  	 d.setSize(420,150);
   	  	 
   	  	 Button b = new Button("Yes");
       	 b.addActionListener(new java.awt.event.ActionListener() {
     	   public void actionPerformed(java.awt.event.ActionEvent evt) {
             Button b2 = (Button) evt.getSource();
             debug_pdftohtml_output();
             ((Dialog)b2.getParent()).dispose();            
          }
         }); 
         b.setBounds(180,100,60,20); 
         
   	  	 Button b2 = new Button("No");
       	 b2.addActionListener(new java.awt.event.ActionListener() {
     	   public void actionPerformed(java.awt.event.ActionEvent evt) {
             Button b3 = (Button) evt.getSource();
             ((Dialog)b3.getParent()).dispose();            
          }
         }); 
         b.setBounds(180,100,60,20); 
         b2.setBounds(250,100,60,20);
         
     	 d.add(b);     	 	
     	 d.add(b2);
         d.setLocationRelativeTo(null);       	  
   	  	 d.setVisible(true);
      
    }  
    catch (IOException e) { 
      System.out.println(e);
    }  
    catch (Exception e) {
      System.out.println("Exception in class: first_classification. " + e);
    }
    
    }

   public int in_boundaries(Text_Element t, Text_Element n) {
      int l1 = t.left;
	  int r1 = t.left + t.width;
	  
	  int l2 = n.left;
	  int r2 = n.left + n.width;
	  
      if ((l1 >= l2 && r1 <= r2) || 
	      (l1 >= l2 && l1 <= r2 && r1 > r2) ||
		  (l1 < l2 && r1 >= l2 && r1 <= r2) ||
		  (l2 >= l1 && r2 <= r1))  {
		  return 1;
		  
	  }
	  return 0;
   }
   
    public boolean in_the_line(Text_Element t, Line l) {
      Font f = this.fonts.get(t.font);
      
     // int text_bottom = t.top + t.height;
      int text_bottom = t.top + f.size;
     
      if (t.top >= l.first_top && t.top <= l.bottom) {
      	 return true;
      }
      else if (text_bottom >= l.first_top && text_bottom <= l.bottom) {
      	 return true;
      }
      else if(t.top <= l.first_top && text_bottom >= l.bottom) {
      	 return true;
      }
      else {
      	 return false;
      }
      	
    }
    
    public int belonging_together(Text_Element t, Text_Element n) {
    	boolean merge = false;
    	
    	int n_letter_width = 0;
    	int t_letter_width = 0;
    	
        if (n.value.length() != 0) {   
           n_letter_width = n.width/n.value.length();    	
        
           if (t.value.length() != 0) {
             t_letter_width = t.width/t.value.length();
           }	
        
         int distance = n.left - (t.left + t.width);                 
         int t_right = t.left + t.width;
         int n_right = n.left + n.width;
         
         
         if (t.left > n.left && t_right < n_right) {
           	  return 1;
         }
         else if (n.left > t.left && n_right < t_right) {
           	  return 1;           	
         }         	
         else if (n_right > t.left && n_right < t_right) {
           	  return 1;           	
         }
         else if (n.left > t.left && n.left < t_right) {
           	  return 1;           	
         }
         else if (distance <= n_letter_width && distance <= t_letter_width) {
           	 return 0;         	          
         }                  
        
        }            
        else if (n.value.length() == 0) {       
           return 0;
        }       
      
       return -1;
    }
    
	public void actualize_line_values(Text_Element t,Line l) {			        
			l.top = Math.min(t.top, l.top);
			Font f = this.fonts.get(t.font);
			int b = t.top + t.height;
			l.bottom = Math.max(b, l.bottom);						
			l.height = l.bottom - l.top;
			l.leftmost = Math.min(t.left,l.leftmost);
			l.rightmost = Math.max(l.rightmost,t.left+t.width);
			l.font = t.font;	  
			l.last_top = Math.max(t.top,l.last_top);  			
			l.first_top = Math.min(t.top,l.first_top); 
            l.used_space += t.width*t.height;			  		
	}
	
    public void actualize_text_values(Text_Element pt, Text_Element t) {
    	pt.last_top = Math.max(pt.last_top, t.last_top);
    	pt.first_top = Math.min(pt.first_top,t.first_top);
    	int t_right = t.left + t.width;
    	pt.width = t_right - pt.left;
    }
        
	public void set_new_line_values(Text_Element t, Line l) {
	   l.top = t.top;
	   Font f = this.fonts.get(t.font);
	   l.bottom = t.top + t.height;
	   l.height = l.bottom - l.top;
	   l.leftmost = t.left;
	   l.rightmost = t.left + t.width;
	   l.font = t.font;
	   l.last_top = t.top;
	   l.first_top = t.top;
	   l.used_space = t.width * t.height;	
	}
	
	public void actualize_mlb_values(Multiline_Block mlb, Line l) {
	   // actualize mlb values after adding a new line to the mlb
	   mlb.end++;
	   mlb.leftmost = Math.min(mlb.leftmost,l.leftmost);
	   mlb.rightmost = Math.max(mlb.rightmost,l.rightmost);
	   mlb.max_elements = Math.max(mlb.max_elements, l.texts.size());		   
	   mlb.used_space += l.used_space;
	}

    public void actualize_mlb_values2(Multiline_Block m1, Multiline_Block m2) {
      // actualize mlb values after merging two mlbs
      m1.leftmost = Math.min(m1.leftmost,m2.leftmost);	
	  m1.rightmost = Math.max(m1.rightmost,m2.rightmost);
	  m1.max_elements = Math.max(m1.max_elements, m2.max_elements);		   
	  m1.used_space += m2.used_space;
	  m1.avg_distance = (m1.avg_distance + m2.avg_distance)/2;
      	
    }
    
    public void set_mlb_values(Multiline_Block mlb, Line l, int b, int p) {    
      	mlb.begin = b;
      	mlb.end = b;
      	mlb.leftmost = l.leftmost;
      	mlb.rightmost = l.rightmost;
      	mlb.max_elements = l.texts.size();
      	mlb.avg_distance = 0;
      	mlb.page = p;
      	mlb.used_space = l.used_space;
    }	
    
	public Text_Element getTextElement(Element text) {	
	          String value = text.getValue().trim();            
	          
	          int top = Integer.parseInt(text.getAttribute("top").getValue());            	          	     
	          int left = Integer.parseInt(text.getAttribute("left").getValue());
	          int width = Integer.parseInt(text.getAttribute("width").getValue());	          
	          int height = Integer.parseInt(text.getAttribute("height").getValue());
	          int font = Integer.parseInt(text.getAttribute("font").getValue());  	          
	          Font text_f = this.fonts.get(font);
	          int text_size = text_f.size;
	          	          
	          String typ = "number";	          	               
	          try {
	          	int int_or_not = Integer.parseInt(value);
	          	float float_or_not = Float.parseFloat(value);        
	          }
	          catch (NumberFormatException nfe) {
	            typ = "text";
              }
            
              List<Element> format_list = text.getChildren("b");
              List<Element> format_list2 = text.getChildren("i");
              
              String format = "";
              if (format_list.size() > 0) {
              	format = "bold";
              }
              else if (format_list2.size() > 0) {
              	format = "italic";
              }
              else if (format_list.size() > 0 && format_list2.size() > 0) {
                format = "bolditalic";   
              }
     
      return new Text_Element(value,top,left,width,height,font,format,typ);
	
	}	

	public void multiline_block_merge() {
      int b = 0;		
	  boolean begin = true;	
	  int steps_backward = 0;
	  int steps_forward = 0;
	  int before = 0;
	  int after = 0;
	  
	  this.removed_elements_before = 0;
	  this.removed_elements_after = 0;
	  
	  for (int i=0;i<this.mlbs.size();i++) {
	
	  	Multiline_Block mlb2 = this.mlbs.get(i);
	  	
	   	mlb2.begin = mlb2.begin - this.removed_elements_before - this.removed_elements_after;
	   	mlb2.end = mlb2.end - this.removed_elements_before - this.removed_elements_after;
	   	
	   	before = this.removed_elements_before;
	  	after = this.removed_elements_after;
	  	
        if (i==0) {
          // first multiline block
	  	  if (mlb2.begin-10 > 0) {
	  	    steps_backward = 10;	
	  	  }	      	  	
	  	  else {
	  	  	steps_backward = mlb2.begin - 1;
	  	  }
	  	  steps_forward = 0;
	  	  line_merge(i, steps_backward, steps_forward);  	
	  	  mlb2.begin = mlb2.begin - (this.removed_elements_before - before);
	  	  mlb2.end = mlb2.end - (this.removed_elements_before - before);
        }
        else if (i==this.mlbs.size()-1) {
          // last multiline block
          if (mlb2.end+10 < this.lines.size()) {
	  	  	 steps_forward = 10;
	  	  }
	  	  else {
	  	  	 steps_forward = this.lines.size() - mlb2.end -1;	  	  
	  	  }        	
	  	  steps_backward = 0;	
	  	  line_merge(i, steps_backward, steps_forward);  
        }
        else {
          // every other multiline block between the first and the last
          Multiline_Block mlb1 = this.mlbs.get(i-1);
          Multiline_Block mlb3 = this.mlbs.get(i+1);
          
	  	  steps_forward = mlb3.begin - mlb2.end-1;
	  	  steps_backward = mlb2.begin - mlb1.end-1;

	  	  if (mlb2.page == mlb3.page && mlb2.page != mlb1.page) {
	  	  	steps_backward = 0;
   	  	    line_merge(i, steps_backward, steps_forward);  	 
	  	  }
	  	  else if (mlb2.page == mlb1.page && mlb2.page != mlb3.page) {
	  	  	steps_forward = 0;
   	  	    line_merge(i, steps_backward, steps_forward);  	
	  	  }
	  	  else if (mlb2.page == mlb1.page && mlb2.page == mlb3.page) {
  	  	    line_merge(i, steps_backward, steps_forward);  	          	  	  	
	  	  } // if mlbs on the same page
	  	  	  	    	  	  
  	  	  boolean merge_with_before = false;

	  	  if (mlb2.begin - mlb1.end <= 3 && mlb2.page == mlb1.page && (Math.abs(mlb2.max_elements - mlb1.max_elements) <=1))  {
	  	  	 mlb1.end = mlb2.end - (this.removed_elements_before - before);
	  	  	 this.mlbs.remove(i);	  	  	 
	  	  	 merge_with_before = true;
	  	  	 actualize_mlb_values2(mlb1,mlb2);	  	  	 
	  	  	 i--;
	  	  }  	  	  
	  	  if (mlb3.begin - mlb2.end <= 3 && mlb3.page == mlb2.page && (Math.abs(mlb2.max_elements - mlb3.max_elements) <=1)) { 
	  	  	 if (merge_with_before == false) {
	  	  	   mlb2.begin = mlb2.begin - (this.removed_elements_before - before);	
	  	  	   mlb2.end = mlb3.end - (this.removed_elements_before - before) - (this.removed_elements_after - after);
	  	  	   actualize_mlb_values2(mlb2,mlb3);
	  	  	   this.mlbs.remove(i+1);	  	  	   	  	  	 
	  	  	 }
	  	  	 else {
	  	  	   mlb1.end = mlb3.end - (this.removed_elements_before - before) - (this.removed_elements_after - after);	  	  	 	  	  	   
	  	  	   actualize_mlb_values2(mlb1,mlb3);
	  	  	   this.mlbs.remove(i+1);	  	  	  
	  	  	 } 
	  	  }
          			
        }	  		  
        

	  }

	}
	
    public void line_merge(int pos, int steps_back, int steps_for) {
    	Multiline_Block mlb = this.mlbs.get(pos);
        Line first_line = this.lines.get(mlb.begin);   
        Line last_line = this.lines.get(mlb.end);    	
    	int count = 0;
    	boolean merge_control = true;
    	
        for (int i=1; i<=steps_back && merge_control == true;i++) {
        	Line pl = this.lines.get(mlb.begin - i);
        	List<Text_Element> storage = new ArrayList<Text_Element>(first_line.texts);
        	
        	int top_distance = first_line.first_top - pl.bottom;
        	
        	for (int j=0;j<first_line.texts.size();j++) {
        	   Text_Element t = storage.get(j);
        	   
        	   for (int k=0;k<pl.texts.size();k++) {
        	   	
        	   	  Text_Element n = pl.texts.get(k);
        	   	  int left_distance = Math.abs(n.left - t.left);
        	   	  int right_distance = Math.abs((n.left + n.width) - (t.left+t.width));
        	   	  if (top_distance < t.height/2 && t.typ.equals(n.typ) && t.typ.equals("text") && ((left_distance < 3) || (right_distance < 3))) {
        	   	  	 String s = n.value + " " + t.value;
        	   	  	 t.value = s;
        	   	  	 t.count_lines++;
        	   	  	 actualize_text_values(t,n);
                     count++;        	   	  	 
        	   	  }
        	   }	
        	}
        	   if (count == pl.texts.size()) {
        	   	  List<Text_Element> clone = new ArrayList<Text_Element>(storage);
				first_line.texts = clone;
        	   	  for (int p=0;p<first_line.texts.size();p++) {
                    Text_Element t = first_line.texts.get(p);
                    actualize_line_values(t,first_line);
                  }
        	   	  
        	   	  this.lines.remove(mlb.begin - i);
        	   	  this.removed_elements_before++;
        	   }
        	   else {
        	   	  merge_control = false;
        	   }
        	   count = 0;  
        	   
        }    	
        merge_control = true;
        
        for (int i=1;i<=steps_for && merge_control == true;i++) {
        	Line nl = this.lines.get(mlb.end + i);
        	List<Text_Element> storage = new ArrayList<Text_Element>(last_line.texts);
        	
        	int top_distance = nl.first_top - last_line.bottom;
        	
        	for (int j=0;j<last_line.texts.size();j++) {
        	   Text_Element t = last_line.texts.get(j);
        	   for (int k=0;k<nl.texts.size();k++) {
        	   	  Text_Element n = nl.texts.get(k);
        	   	  int left_distance = Math.abs(n.left - t.left);
        	   	  int right_distance = Math.abs((n.left + n.width) - (t.left+t.width));
        	   	  
        	   	  if (top_distance < t.height/2 && t.typ.equals(n.typ) && t.typ.equals("text") && ((left_distance < 3) || (right_distance < 3))) {
        	   	  	
        	   	  	 String s = t.value + " " + n.value;
        	   	  	 t.value = s;
        	   	  	 t.count_lines++;
        	   	  	 actualize_text_values(t,n);
                     count++;    
        	   	  }
        	   }	
        	}       
        	   if (count == nl.texts.size()) {
        	   	  last_line.texts = new ArrayList<Text_Element>(storage); 
        	   	  for (int p=0;p<last_line.texts.size();p++) {
                    Text_Element t = last_line.texts.get(p);
                    actualize_line_values(t,last_line);
                  }
        	   	  this.lines.remove(mlb.end+i);
        	   	  this.removed_elements_after++;     	   	          	   	  
        	   }
        	   else {
        	   	  merge_control = false;
        	   }
        	   count = 0;        	 	
        }
    }
	
	
	public void debug_pdftohtml_output() {
	  try {
	  BufferedReader br = new BufferedReader(new FileReader(this.pdftohtml_file_name));

      PrintStream dos = new PrintStream(new FileOutputStream(new File(this.path + File.pathSeparator + "debugged_output.xml")));
	  
	  String current_line = br.readLine();
	  
	  while (current_line != null) {	  	
	  	 current_line = current_line.replaceAll("A href","a href");
	  	 current_line = current_line.replaceAll("<B>","<b>");
	  	 current_line = current_line.replaceAll("<I>","<i>");	
	  	 current_line = current_line.replaceAll("</I>","</i>");	
	  	 current_line = current_line.replaceAll("</B>","</b>");	
	  	   	 
	  	 dos.println(current_line);	  	 
	  	 current_line = br.readLine();
	  }
      
      run(this.path + File.pathSeparator + "debugged_output.xml");
      
	 }
	 catch(Exception e) {
	   System.out.println(e);	
	 }
	 
	}


}