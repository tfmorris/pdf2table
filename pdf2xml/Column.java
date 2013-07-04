/*
    Copyright 2005, 2005 Burcu Yildiz
    Contact: burcu.yildiz@gmail.com
    
    This file is part of pdf2table.    pdf2table is free software: you can redistribute it and/or modify    it under the terms of the GNU General Public License as published by    the Free Software Foundation, either version 3 of the License, or    (at your option) any later version.    pdf2table is distributed in the hope that it will be useful,    but WITHOUT ANY WARRANTY; without even the implied warranty of    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the    GNU General Public License for more details.    You should have received a copy of the GNU General Public License    along with pdf2table.  If not, see <http://www.gnu.org/licenses/>.
*/

package pdf2xml;

import java.util.*;

public class Column {

  Vector cells;
  int left;
  int right;
  int empty_cells;
  int header;
    
  public Column(int l, int r) {
    this.cells = new Vector();	 
    this.left = l;
    this.right = r;
    this.empty_cells = 0;
    this.header = -1;
  }
  
  public Column() {
  	this.cells = new Vector();
  	this.left = -1;
  	this.right = -1;
  	this.empty_cells = 0;
  	this.header = -1;
  }

  public Object clone() {
    Column c = new Column(this.left, this.right);
	c.cells.addAll(this.cells);
    return c;
  }
}