/*
    Copyright 2005, 2005 Burcu Yildiz
    Contact: burcu.yildiz@gmail.com
    
    This file is part of pdf2table.    pdf2table is free software: you can redistribute it and/or modify    it under the terms of the GNU General Public License as published by    the Free Software Foundation, either version 3 of the License, or    (at your option) any later version.    pdf2table is distributed in the hope that it will be useful,    but WITHOUT ANY WARRANTY; without even the implied warranty of    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the    GNU General Public License for more details.    You should have received a copy of the GNU General Public License    along with pdf2table.  If not, see <http://www.gnu.org/licenses/>.
*/

package pdf2xml;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.jdom2.Element;



class TopComparator implements Comparator<Text_Element> {

    public int compare(Text_Element t1, Text_Element t2) {
        return (t1.top - t2.top);
    }
}

class LeftComparator implements Comparator<Text_Element> {

    public int compare(Text_Element t1, Text_Element t2) {
        return (t1.left - t2.left);
    }
}

public class Text_Element {

    public enum Type  {TEXT, NUMBER};
    public enum Style {NORMAL, BOLD, ITALIC, BOLD_ITALIC};
    
    String value;
    int top;
    int left;
    int width;
    int height;
    int right;
    int font;
    Style style = Style.NORMAL;
    Type typ;
    int count_lines;
    List<Text_Element> elements;
    int last_top;
    int first_top;
    int colspan = 1;
    boolean artificial;

    public Text_Element(String v, int t, int l, int w, int h, int f, Style f2,
            Type t2) {
        this.value = v;
        this.top = t;
        this.left = l;
        this.width = w;
        this.right = l + w;
        this.height = h;
        this.font = f;
        this.style = f2;
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
                this.width, this.height, this.font, this.style, this.typ);
        return t;
    }

    /**
     * Maximize bounds with those of given TextElement
     * 
     * @param te
     *            TextElement
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

        Type typ = Type.NUMBER;
        try {
            Integer.parseInt(value);
            Float.parseFloat(value);
        } catch (NumberFormatException nfe) {
            typ = Type.TEXT;
        }

        List<Element> bold_elements = text.getChildren("b");
        List<Element> italic_elements = text.getChildren("i");

        Style style;
        if (bold_elements.size() > 0) {
            if (italic_elements.size() > 0) {
                style = Style.BOLD_ITALIC;
            } else {
                style = Style.BOLD;
            }
        } else if (italic_elements.size() > 0) {
            style = Style.ITALIC;
        } else {
            style = Style.NORMAL;
        }

        return new Text_Element(value, top, left, width, height, font, style,
                typ);

    }
    

    /**
     * Test whether another TextElement is contained by, contains, or overlaps
     * this one.
     * 
     * @param te
     *            other Text_Element
     * @return true if they intersect in any way.
     */
    public boolean intersects(Text_Element te) {
        return Text_Element.intersect(this, te);
    }

    /**
     * Test whether two Text_Element are contained by or overlap each other this
     * one.
     * 
     * @param te1
     *            firstText_Element
     * @param te2
     *            other Text_Element
     * @return true if they intersect in any way.
     */
    public static boolean intersect(Text_Element te1, Text_Element te2) {
        int l1 = te1.left;
        int r1 = te1.left + te1.width;

        int l2 = te2.left;
        int r2 = te2.left + te2.width;

        return ((l1 >= l2 && r1 <= r2) ||
                (l1 >= l2 && l1 <= r2 && r1 > r2) ||
                (l1 < l2 && r1 >= l2 && r1 <= r2) ||
                (l2 >= l1 && r2 <= r1)); 
    }
    
    public static int belong_together(Text_Element t, Text_Element n) {

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
            } else if (n.left > t.left && n_right < t_right) {
                return 1;
            } else if (n_right > t.left && n_right < t_right) {
                return 1;
            } else if (n.left > t.left && n.left < t_right) {
                return 1;
            } else if (distance <= n_letter_width && distance <= t_letter_width) {
                return 0;
            }
        } else if (n.value.length() == 0) {
            return 0;
        }
        return -1;
    }


    /**
     * Sort sub elements and concatenate into a new value
     */
    void coalesceSubElements() {
        if (elements.size() > 0) {
            Collections.sort(elements, new TopComparator());
            StringBuilder sb = new StringBuilder();
            for (Text_Element element : elements) {
                sb.append(element.value).append(" ");
            }
            value = sb.toString();
            elements.clear();
        }
    }

    /**
     * Process all the text elements for a single line of text and reduce/merge
     * into a simpler list of elements.
     * 
     * @param texts
     */
    static void processLineTexts(List<Text_Element> texts) {
        leftSort(texts);
    
        int p = 0;
        while (p<texts.size()-1) {
            Text_Element t = texts.get(p);
            Text_Element n = texts.get(p+1);
    
            int result = belong_together(t,n);
    
            if (result != -1) {
                texts.remove(p + 1);
                if (result == 1) {
                    if (t.elements.size() == 0) {
                        t.elements.add(t);
                        t.add(n);
                    }
                    if (n.value.length() > 0) {
                        t.elements.add(n);
                        t.add(n);
                    }
                } else if (result == 0) {
                    t.value = t.value + " " + n.value;
                    t.add(n);
                }
                p--;
            }
            p++;
        }
    
        for (Text_Element t : texts) {
            t.coalesceSubElements();
        }
    }

    static void leftSort(List<Text_Element> elements) {
        Collections.sort(elements, new LeftComparator());
    }

}