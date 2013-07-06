/*
    Copyright 2005, 2005 Burcu Yildiz
    Contact: burcu.yildiz@gmail.com
    
    This file is part of pdf2table.    pdf2table is free software: you can redistribute it and/or modify    it under the terms of the GNU General Public License as published by    the Free Software Foundation, either version 3 of the License, or    (at your option) any later version.    pdf2table is distributed in the hope that it will be useful,    but WITHOUT ANY WARRANTY; without even the implied warranty of    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the    GNU General Public License for more details.    You should have received a copy of the GNU General Public License    along with pdf2table.  If not, see <http://www.gnu.org/licenses/>.
*/

package pdf2xml;

import java.util.ArrayList;
import java.util.List;

public class Node {
    int left;
    int right;
    int level;
    String content;
    List<Node> nodes;
    Text_Element text_element;

    public Node(String c, int l) {
        // used only for the construction of the root element in SecondClassification.java
        this.content = c;
        this.level = l;
        this.nodes = new ArrayList<Node>();
    }

    public Node(Text_Element t , int l) {
        this.text_element = t;
        this.content = t.value;
        this.left = t.left;
        this.right = t.right;
        this.level = l;
        this.nodes = new ArrayList<Node>();
    }

    public Node(String c, Text_Element t , int l) {
        this.content = c;
        this.left = t.left;
        this.right = t.right;
        this.level = l;
        this.nodes = new ArrayList<Node>();
    }


    public boolean insert(Text_Element t, int l) {

        if (content.equals("null")) {
            if (nodes.get(0).insert(t, l)) {
                return true;
            }
        } else {
            if (in_boundaries(t.left, t.right, left, right) 
                    || content.equals("root")) {
                int pos = 0;

                for (int i=0;i<nodes.size();i++) {

                    Node next = nodes.get(i);
                    // it was t.left > next.right. which means completely on the right side
                    if (t.left > next.left) { pos++; }

                    if ((in_boundaries(t.left, t.right, next.left, next.right) && next.level < l) 
                            || next.content.equals("null")) {
                        if (next.insert(t,l)) {
                            return true;
                        }
                    }
                } // end of for

                Node n = this;
                for (int j=level; j < l-1; j++) {
                    Node dummy = new Node("null", t, j+1);
                    n.nodes.add(pos, dummy);
                    n = dummy;
                    pos = 0;
                } 
                Node current = new Node(t,l);
                n.nodes.add(pos,current);
                return true;
            }
        }
        return false;
    }


    private  static boolean in_boundaries(int l1, int r1, int l2, int r2) {
        if ((l1 >= l2 && r1 <= r2) || 
                (l1 >= l2 && l1 <= r2 && r1 > r2) ||
                (l1 < l2 && r1 >= l2 && r1 <= r2) ||
                (l2 >= l1 && r2 <= r1))  {
            return true;
        }
        return false;
    }
    

    public void print_tree() {

        for (int i = 0; i < nodes.size(); i++) {
            nodes.get(i).print_tree();
        }
    }

}
