/*
    Copyright 2005, 2005 Burcu Yildiz
    Contact: burcu.yildiz@gmail.com
    
    This file is part of pdf2table.    pdf2table is free software: you can redistribute it and/or modify    it under the terms of the GNU General Public License as published by    the Free Software Foundation, either version 3 of the License, or    (at your option) any later version.    pdf2table is distributed in the hope that it will be useful,    but WITHOUT ANY WARRANTY; without even the implied warranty of    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the    GNU General Public License for more details.    You should have received a copy of the GNU General Public License    along with pdf2table.  If not, see <http://www.gnu.org/licenses/>.
*/

package pdf2xml;

import java.util.*;
import java.io.*;
import java.awt.*;

public class xml_output extends Frame{
PrintStream dos;
Vector tables;
Vector fonts;
String path;
PrintStream dos2;
PrintStream dos3;
   	
   public xml_output(Vector table_vector, Vector font_vector,String p) {
   	try {
   	  this.path = p;
   	  this.tables = (Vector) table_vector.clone();
   	  this.fonts = (Vector) font_vector.clone();
  
	  
   	  create_stylesheet();
   	  create_tables_dtd();   	  
   	  create_output();
   	}
   	catch (Exception e) {
        System.out.println("Exception in class: xml_output and method: constructor. " + e);	    		 	
    }   	
   	}
   	

   	public void create_stylesheet() {
	  try {
	  	File my_file = new File(this.path, "table_view.xsl");
	    this.dos2 = new PrintStream(new FileOutputStream(my_file));		
	    
	    String xsl_value = "<?xml version=\"1.0\" encoding=\"iso-8859-1\" ?>\n" +	        
          "<xsl:stylesheet xmlns:xsl=\"http://www.w3.org/1999/XSL/Transform\" version=\"1.0\">\n" +
          "<xsl:output method=\"html\" />\n" +
          "<xsl:template match=\"/\">" +
          "<html>\n" +
          "<body>\n" +
          "<xsl:for-each select=\"tables/table\">\n" +
          "<table border=\"1\">\n" + 
          "<caption>\n" +
          // TODO: Next 3 lines windows version only - need to resolve discrepancy
          "<xsl:value-of select=\"page\"/>\n" +
          "</caption>\n" +             
          "<caption>\n" +
          // End Windows version only code
          "<xsl:value-of select=\"title\"/>\n" +
          "</caption>\n" +     
          "<xsl:for-each select=\"header/header_line\">\n" +
          "<tr>\n" +
          "<xsl:for-each select=\"header_element\">\n" +
              "<th bgcolor=\"#ccdddd\" colspan=\"{@colspan}\">\n" +
              "<xsl:value-of select=\".\" /> \n" +
              "</th>\n" +
          "</xsl:for-each>\n" +
          "</tr>\n" +
     "</xsl:for-each>\n" +      
     "<xsl:for-each select=\"tbody/data_row\">\n" +
         "<tr>\n" +
         "<xsl:for-each select=\"cell\">\n" + 
             "<td colspan=\"{@colspan}\">\n" + 
             "<xsl:if test=\"@format='bold'\">\n" + 
                "<b>\n" + 
                "<xsl:value-of select=\".\" />\n" + 
                "</b>\n" + 
             "</xsl:if>\n" + 
             "<xsl:if test=\"@format='italic'\">\n" + 
                "<i>\n" + 
                "<xsl:value-of select=\".\" />\n" + 
                "</i>\n" + 
             "</xsl:if>\n" + 
             "<xsl:if test=\"@format='bolditalic'\">\n" + 
                "<b><i>\n" + 
                "<xsl:value-of select=\".\" />\n" + 
                "</i></b>\n" + 
             "</xsl:if>\n" +                           
             "<xsl:if test=\"@format=''\">\n" +
                "<xsl:value-of select=\".\" />\n" + 
             "</xsl:if>\n" + 
             "</td>\n" + 
         "</xsl:for-each>\n" + 
         "</tr>\n" + 
     "</xsl:for-each>\n" + 
     "<BR>   </BR>\n" + 
     "<BR>   </BR>\n" +
     "<BR>   </BR>\n" + 
     "</table>\n" + 
  "</xsl:for-each>\n" + 
  "</body>\n" + 
  "</html>\n" +
  "</xsl:template>\n" + 
  "</xsl:stylesheet>\n";
      this.dos2.print(xsl_value);
      
      }
      catch (IOException ie) {
        System.out.println("Exception in class: xml_output and method: create_stylesheet. " + ie);	    		 	      	
      }   		
      catch (Exception e) {
        System.out.println("Exception in class: xml_output and method: create_stylesheet. " + e);	    		 	      	
      }
   	}
   	   	
   	public void create_tables_dtd() {
	  try {
	  	File my_file = new File(this.path, "tables.dtd");
	    this.dos3 = new PrintStream(new FileOutputStream(my_file));		

        String dtd_value = "<?xml version=\"1.0\" encoding=\"iso-8859-1\"?>\n" +
          "<!ELEMENT tables(table+,fontspec*)>\n" + 
          "<!ELEMENT fontspec EMPTY>\n" +
          "<!ATTLIST fontspec\n" + 
	          "id CDATA #REQUIRED\n" +
	          "size CDATA #REQUIRED\n" +
	          "family CDATA #REQUIRED\n" + 
	          "color CDATA #REQUIRED\n" +
          ">\n" +
          "<!ELEMENT table (header,tbody)>\n" +
          "<!ELEMENT header (header_element)*>\n" +
          "<!ELEMENT header_element (#PCDATA)>\n" + 
          "<!ATTLIST header_element\n" + 
	          "id CDATA #REQUIRED\n" +
	          "sh CDATA #REQUIRED\n" + 
	          "font CDATA #REQUIRED\n" + 
	          "colspan CDATA #REQUIRED\n" +
          ">\n" +
          "<!ELEMENT tbody (data_row)*>\n" +
          "<!ELEMENT data_row (cell)*>\n" + 
          "<!ELEMENT cell (#PCDATA)>\n" + 
          "<!ATTLIST cell\n" +
	          "sh CDATA #REQUIRED\n" +
	          "font CDATA #REQUIRED\n" +
	          "colspan CDATA #REQUIRED\n" +
              "format CDATA #REQUIRED\n" +
          ">\n";

          this.dos3.print(dtd_value);
      }
      catch (IOException ie) {
        System.out.println("Exception in class: xml_output and method: create_tables_dtd. " + ie);	    		 	      	
      }   		
      catch (Exception e) {
        System.out.println("Exception in class: xml_output and method: create_tables_dtd. " + e);	    		 	      	
      }
   	
   	}
   	
   	public void create_output() {
   		
	  try {
	  	File my_file = new File(this.path, "output.xml");
		System.out.println(my_file.toString());
	    this.dos = new PrintStream(new FileOutputStream(my_file),true,"UTF-16");		
	    
	  
       dos.println("<?xml version=\"1.0\" encoding=\"UTF-16\" ?>");
       dos.println("<?xml-stylesheet href=\"table_view.xsl\" type=\"text/xsl\" ?>");
       dos.println("<tables>");
       
       for (int km=0;km<this.fonts.size();km++) {
       	  Font f = (Font) this.fonts.elementAt(km);
       	  dos.println("<fontspec id=\"" + f.id + 
       	     "\" size=\"" + f.size + "\" family=\"" + 
			 f.family + "\" color=\"" + f.color + "\"/>");       	
       }
	   
           
       for (int i=0;i < this.tables.size();i++) {
       	 		
       	 Table c_table = (Table) this.tables.elementAt(i);
		 int cells_on_column = 0;
		 	
         dos.println("<table>");
         
         dos.println("<page>" + "TABLE ON PAGE " + c_table.page + "</page>");
         dos.println("<title>" + c_table.title + "</title>");
         // Mac version below
//         dos.println("<title>" + "TABLE ON PAGE " + c_table.page + "</title>");
 
		 dos.println("<header>");		     
		 
		 for (int j=0; j < c_table.datarow_begin; j++) {
		    int p = 0;
			dos.println("<header_line>");
			while (p < c_table.columns.size()) {
			   Column cc1 = (Column) c_table.columns.elementAt(p);
			   cells_on_column = cc1.cells.size();
			   cc1.header = p+j;
			   Text_Element t1 = (Text_Element) cc1.cells.elementAt(j);
			   dos.println("<header_element id=\"" + (p+j) + "\" sh=\"" + cc1.header); 
			   dos.println("\" font=\"" + t1.font + "\" colspan=\"" + t1.colspan + "\">");
       	 	   dos.println("<![CDATA[");
			     if (!t1.value.equals("null")) {
       	 	       dos.println(t1.value);
				 }
       	 	   dos.println("]]>");
       	 	   dos.println("</header_element>"); 
			   p = p + t1.colspan;
			}
			dos.println("</header_line>");
		 }	 
		 dos.println("</header>");

         dos.println("<tbody>");    
         for (int j=c_table.datarow_begin; j < cells_on_column; j++) {
		  dos.println("<data_row>"); 	           
          int k = 0;
          while (k < c_table.columns.size()) {
               Column cc = (Column) c_table.columns.elementAt(k);      
			   Text_Element t = (Text_Element) cc.cells.elementAt(j);
        	   dos.print("<cell sh=\"" + cc.header + "\" font=\"" + t.font);                                     	      	 	       
       	 	   dos.println("\" colspan=\"" + t.colspan + "\" format=\"" + t.format + "\">");  
       	 	   dos.println("<![CDATA[");
			     if (!t.value.equals("null")) {
       	 	       dos.println(t.value);
				 }
       	 	   dos.println("]]>");
       	 	   dos.println("</cell>"); 
       	 	   
       	 	   k = k + t.colspan;         
       	 	   
            }
            dos.println("</data_row>"); 	
	                                 	
         }
		 
       	dos.println("</tbody>");
       	dos.println("</table>");
       	
       }
	   
       dos.println("</tables>");
      
	
		System.out.println("TableExtractor extracted " + this.tables.size() + " table(s)!");
   
	

		       
      }
    
      catch (IOException ie) {
        System.out.println("Exception in class: xml_output and method: create_output. " + ie);	    		 	      	
      }   		
      catch (Exception e) {
        System.out.println("Exception in class: xml_output and method: create_output. " + e);	    		 	      	
      }
   	
   } 
  	
}