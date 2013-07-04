/*
    Copyright 2005, 2005 Burcu Yildiz
    Contact: burcu.yildiz@gmail.com
    
    This file is part of pdf2table.    pdf2table is free software: you can redistribute it and/or modify    it under the terms of the GNU General Public License as published by    the Free Software Foundation, either version 3 of the License, or    (at your option) any later version.    pdf2table is distributed in the hope that it will be useful,    but WITHOUT ANY WARRANTY; without even the implied warranty of    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the    GNU General Public License for more details.    You should have received a copy of the GNU General Public License    along with pdf2table.  If not, see <http://www.gnu.org/licenses/>.
*/

package pdf2xml;

import java.util.*;

public class Multiline_Block {
	
int begin;
int end;

int leftmost;
int rightmost;
int max_elements;
int avg_distance;
int page;
int used_space = 0;

  public Multiline_Block() {
  
  }
  
  public Multiline_Block(int b, int e, int l,int r,int me,int ad,int p) {
  	this.begin = b;
  	this.end = e;
  	this.leftmost = l;
  	this.rightmost = r;
  	this.max_elements = me;
  	this.avg_distance = ad;
  	this.page = p;
  }	
}