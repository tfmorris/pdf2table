/*
    Copyright 2005, 2005 Burcu Yildiz
    Contact: burcu.yildiz@gmail.com
    
    This file is part of pdf2table.    pdf2table is free software: you can redistribute it and/or modify    it under the terms of the GNU General Public License as published by    the Free Software Foundation, either version 3 of the License, or    (at your option) any later version.    pdf2table is distributed in the hope that it will be useful,    but WITHOUT ANY WARRANTY; without even the implied warranty of    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the    GNU General Public License for more details.    You should have received a copy of the GNU General Public License    along with pdf2table.  If not, see <http://www.gnu.org/licenses/>.
*/

package pdf2xml;

import java.util.ArrayList;
import java.util.List;

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
  	Text_Element t = new Text_Element(this.value,this.top, this.left, this.width, this.height, this.font, this.format, this.typ);
  	return t;
  }
}