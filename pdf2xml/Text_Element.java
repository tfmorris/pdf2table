/*
    Copyright 2005, 2005 Burcu Yildiz
    Contact: burcu.yildiz@gmail.com
    
    This file is part of pdf2table.    pdf2table is free software: you can redistribute it and/or modify    it under the terms of the GNU General Public License as published by    the Free Software Foundation, either version 3 of the License, or    (at your option) any later version.    pdf2table is distributed in the hope that it will be useful,    but WITHOUT ANY WARRANTY; without even the implied warranty of    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the    GNU General Public License for more details.    You should have received a copy of the GNU General Public License    along with pdf2table.  If not, see <http://www.gnu.org/licenses/>.
*/

package pdf2xml;

import java.util.ArrayList;
import java.util.List;

import org.jdom2.Element;

public class Text_Element {
	
String value;
int top;
int left;
int width;
int height;
int right;
int font;
String format = "";
String typ;
int count_lines;
List<Text_Element> elements;
int last_top; 
int first_top; 
int colspan = 1;
boolean artificial;

  public Text_Element(String v,int t,int l,int w,int h,int f,String f2,String t2) {
  	this.value = v;
  	this.top = t;
  	this.left = l;
  	this.width = w;
	this.right = l+w;
  	this.height = h;
  	this.font = f;  
  	this.format = f2;	
  	this.typ = t2;
    this.last_top = t; // no line merged to this text element
    this.first_top = t;
    this.colspan = 1;
    this.count_lines = 1;
    this.elements = new ArrayList<Text_Element>();
    this.right = this.left + this.width;
    this.artificial = false;
  }	
  
  public Text_Element() {  	
  	this.value = "null";
    this.colspan = 1;  	
    this.count_lines = 1;
    this.artificial = true;
  }

  public Text_Element(String s) {
  	this.value = s;
    this.colspan = 1;  	
    this.count_lines = 1;
    this.artificial = false;
  }  
  
    public Object clone() {
        Text_Element t = new Text_Element(this.value, this.top, this.left,
                this.width, this.height, this.font, this.format, this.typ);
        return t;
    }

    /**
     * Maximize bounds with those of given TextElement
     * @param te TextElement
     */
    public void add(Text_Element te) {
        last_top = Math.max(last_top, te.last_top);
        first_top = Math.min(first_top, te.first_top);
        int t_right = te.left + te.width;
        width = t_right - left;
    }
    
    public static Text_Element getTextElement(Element text) {
        String value = text.getValue().trim();

        int top = Integer.parseInt(text.getAttribute("top").getValue());
        int left = Integer.parseInt(text.getAttribute("left").getValue());
        int width = Integer.parseInt(text.getAttribute("width").getValue());
        int height = Integer.parseInt(text.getAttribute("height").getValue());
        int font = Integer.parseInt(text.getAttribute("font").getValue());

        String typ = "number";
        try {
            Integer.parseInt(value);
            Float.parseFloat(value);
        } catch (NumberFormatException nfe) {
            typ = "text";
        }

        List<Element> format_list = text.getChildren("b");
        List<Element> format_list2 = text.getChildren("i");

        String format = "";
        if (format_list.size() > 0) {
            format = "bold";
        } else if (format_list2.size() > 0) {
            format = "italic";
        } else if (format_list.size() > 0 && format_list2.size() > 0) {
            format = "bolditalic";
        }

        return new Text_Element(value, top, left, width, height, font, format,
                typ);

    }
}