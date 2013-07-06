/*
    Copyright 2005, 2005 Burcu Yildiz
    Contact: burcu.yildiz@gmail.com
    
    This file is part of pdf2table.    pdf2table is free software: you can redistribute it and/or modify    it under the terms of the GNU General Public License as published by    the Free Software Foundation, either version 3 of the License, or    (at your option) any later version.    pdf2table is distributed in the hope that it will be useful,    but WITHOUT ANY WARRANTY; without even the implied warranty of    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the    GNU General Public License for more details.    You should have received a copy of the GNU General Public License    along with pdf2table.  If not, see <http://www.gnu.org/licenses/>.
*/

package pdf2xml;

import java.util.ArrayList;
import java.util.List;

public class Line {

    List<Text_Element> texts;

    int top;
    int bottom;
    int height;
    int leftmost;
    int rightmost;
    int font;
    int last_top;
    int first_top;
    int used_space;
    String typ;

    public Line() {
        this.texts = new ArrayList<Text_Element>();
    }

    public void init(Text_Element t) {
        top = t.top;
        bottom = t.top + t.height;
        height = bottom - top;
        leftmost = t.left;
        rightmost = t.left + t.width;
        font = t.font;
        last_top = t.top;
        first_top = t.top;
        used_space = t.width * t.height;
    }

    void add(Text_Element t) {
        top = Math.min(t.top, top);
        int b = t.top + t.height;
        bottom = Math.max(b, bottom);
        height = bottom - top;
        leftmost = Math.min(t.left, leftmost);
        rightmost = Math.max(rightmost, t.left + t.width);
        font = t.font;
        last_top = Math.max(t.top, last_top);
        first_top = Math.min(t.top, first_top);
        used_space += t.width * t.height;
    }
}