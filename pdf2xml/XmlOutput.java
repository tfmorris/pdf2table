/*
    Copyright 2005, 2005 Burcu Yildiz
    Contact: burcu.yildiz@gmail.com
    
    This file is part of pdf2table.    pdf2table is free software: you can redistribute it and/or modify    it under the terms of the GNU General Public License as published by    the Free Software Foundation, either version 3 of the License, or    (at your option) any later version.    pdf2table is distributed in the hope that it will be useful,    but WITHOUT ANY WARRANTY; without even the implied warranty of    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the    GNU General Public License for more details.    You should have received a copy of the GNU General Public License    along with pdf2table.  If not, see <http://www.gnu.org/licenses/>.
*/

package pdf2xml;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.List;

public class XmlOutput  {


    public static void create(List<Table> table_list, List<Font> font_list,String path) {
        try {

            create_stylesheet(path);
            create_tables_dtd(path, table_list, font_list);   	  
            create_output(path, font_list, table_list);
        }
        catch (Exception e) {
            System.out.println("Exception in class: XmlOutput and method: constructor. " + e);	    		 	
        }   	
    }
   	

   	public static void create_stylesheet(String path) {
	  try {
	  	File my_file = new File(path, "table_view.xsl");
	  	PrintStream ps = new PrintStream(new FileOutputStream(my_file));		
	    
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
      ps.print(xsl_value);
      ps.close();
      }
      catch (IOException ie) {
        System.out.println("Exception in class: XmlOutput and method: create_stylesheet. " + ie);	    		 	      	
      }   		
      catch (Exception e) {
        System.out.println("Exception in class: XmlOutput and method: create_stylesheet. " + e);	    		 	      	
      }
   	}
   	   	
   	public static void create_tables_dtd(String path, List<Table> table_list, List<Font> font_list) {
	  try {
	  	File my_file = new File(path, "tables.dtd");
	  	PrintStream ps = new PrintStream(new FileOutputStream(my_file));		

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

          ps.print(dtd_value);
          ps.close();
      }
      catch (IOException ie) {
        System.out.println("Exception in class: XmlOutput and method: create_tables_dtd. " + ie);	    		 	      	
      }   		
      catch (Exception e) {
        System.out.println("Exception in class: XmlOutput and method: create_tables_dtd. " + e);	    		 	      	
      }
   	
   	}
   	
   	public static void create_output(String path, List<Font> fonts, List<Table> tables) {
   		
	  try {
	  	File my_file = new File(path, "output.xml");
		System.out.println(my_file.toString());
		PrintStream ps = new PrintStream(new FileOutputStream(my_file),true,"UTF-16");		
	    
	  
       ps.println("<?xml version=\"1.0\" encoding=\"UTF-16\" ?>");
       ps.println("<?xml-stylesheet href=\"table_view.xsl\" type=\"text/xsl\" ?>");
       ps.println("<tables>");
       
       for (int km=0;km<fonts.size();km++) {
       	  Font f = (Font) fonts.get(km);
       	  ps.println("<fontspec id=\"" + f.id + 
       	     "\" size=\"" + f.size + "\" family=\"" + 
			 f.family + "\" color=\"" + f.color + "\"/>");       	
       }
	   
           
       for (int i=0;i < tables.size();i++) {
       	 		
       	 Table c_table = (Table) tables.get(i);
		 int cells_on_column = 0;
		 	
         ps.println("<table>");
         
         ps.println("<page>" + "TABLE ON PAGE " + c_table.page + "</page>");
         ps.println("<title>" + c_table.title + "</title>");
         // Mac version below
//         dos.println("<title>" + "TABLE ON PAGE " + c_table.page + "</title>");
 
		 ps.println("<header>");		     
		 
		 for (int j=0; j < c_table.datarow_begin; j++) {
		    int p = 0;
			ps.println("<header_line>");
			while (p < c_table.columns.size()) {
			   Column cc1 = (Column) c_table.columns.get(p);
			   cells_on_column = cc1.cells.size();
			   cc1.header = p+j;
			   Text_Element t1 = (Text_Element) cc1.cells.get(j);
			   ps.println("<header_element id=\"" + (p+j) + "\" sh=\"" + cc1.header); 
			   ps.println("\" font=\"" + t1.font + "\" colspan=\"" + t1.colspan + "\">");
       	 	   ps.println("<![CDATA[");
			     if (!t1.value.equals("null")) {
       	 	       ps.println(t1.value);
				 }
       	 	   ps.println("]]>");
       	 	   ps.println("</header_element>"); 
			   p = p + t1.colspan;
			}
			ps.println("</header_line>");
		 }	 
		 ps.println("</header>");

         ps.println("<tbody>");    
         for (int j=c_table.datarow_begin; j < cells_on_column; j++) {
		  ps.println("<data_row>"); 	           
          int k = 0;
          while (k < c_table.columns.size()) {
               Column cc = (Column) c_table.columns.get(k);      
			   Text_Element t = (Text_Element) cc.cells.get(j);
        	   ps.print("<cell sh=\"" + cc.header + "\" font=\"" + t.font);                                     	      	 	       
       	 	   ps.println("\" colspan=\"" + t.colspan + "\" format=\"" + t.format + "\">");  
       	 	   ps.println("<![CDATA[");
			     if (!t.value.equals("null")) {
       	 	       ps.println(t.value);
				 }
       	 	   ps.println("]]>");
       	 	   ps.println("</cell>"); 
       	 	   
       	 	   k = k + t.colspan;         
       	 	   
            }
            ps.println("</data_row>"); 	
	                                 	
         }
		 
       	ps.println("</tbody>");
       	ps.println("</table>");
       	
       }
	   
       ps.println("</tables>");
      
	
       System.out.println("TableExtractor extracted " + tables.size() + " table(s)!");
       ps.close();
      }
    
      catch (IOException ie) {
        System.out.println("Exception in class: XmlOutput and method: create_output. " + ie);	    		 	      	
      }   		
      catch (Exception e) {
        System.out.println("Exception in class: XmlOutput and method: create_output. " + e);	    		 	      	
      }
   	
   } 
  	
}