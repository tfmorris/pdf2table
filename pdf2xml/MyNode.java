/*
    Copyright 2005, 2005 Burcu Yildiz
    Contact: burcu.yildiz@gmail.com
    
    This file is part of pdf2table.    pdf2table is free software: you can redistribute it and/or modify    it under the terms of the GNU General Public License as published by    the Free Software Foundation, either version 3 of the License, or    (at your option) any later version.    pdf2table is distributed in the hope that it will be useful,    but WITHOUT ANY WARRANTY; without even the implied warranty of    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the    GNU General Public License for more details.    You should have received a copy of the GNU General Public License    along with pdf2table.  If not, see <http://www.gnu.org/licenses/>.
*/

package pdf2xml;

import java.util.Vector;

public class MyNode {

int left;
int right;
int level;
String content;
Vector nodes;
Text_Element text_element;

  public MyNode(String c, int l) {
    // used only for the construction of the root element in second_classification.java
    this.content = c;
	this.level = l;
	this.nodes = new Vector();
  }
  
  public MyNode(Text_Element t , int l) {
    this.text_element = t;
    this.content = t.value;
	this.left = t.left;
	this.right = t.right;
	this.level = l;
	this.nodes = new Vector();
  }
  
  public MyNode(String c, Text_Element t , int l) {
    this.content = c;
	this.left = t.left;
	this.right = t.right;
	this.level = l;
	this.nodes = new Vector();
  }
  
}
