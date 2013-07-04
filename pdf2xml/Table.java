/*
    Copyright 2005, 2005 Burcu Yildiz
    Contact: burcu.yildiz@gmail.com
    
    This file is part of pdf2table.    pdf2table is free software: you can redistribute it and/or modify    it under the terms of the GNU General Public License as published by    the Free Software Foundation, either version 3 of the License, or    (at your option) any later version.    pdf2table is distributed in the hope that it will be useful,    but WITHOUT ANY WARRANTY; without even the implied warranty of    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the    GNU General Public License for more details.    You should have received a copy of the GNU General Public License    along with pdf2table.  If not, see <http://www.gnu.org/licenses/>.
*/

package pdf2xml;

import java.util.*;

public class Table {


 Vector columns;
 int page;
 int datarow_begin;
 String title = "";
 
 public Table() {
   this.columns = new Vector();
 } 	
 
 public Object clone() {
   Table t = new Table();
   t.columns = (Vector) this.columns.clone();
   t.page = this.page;
   t.datarow_begin = this.datarow_begin;	
   return t;
 }	
}