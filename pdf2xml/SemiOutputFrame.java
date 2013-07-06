/*
    Copyright 2005, 2005 Burcu Yildiz
    Contact: burcu.yildiz@gmail.com
    
    This file is part of pdf2table.    pdf2table is free software: you can redistribute it and/or modify    it under the terms of the GNU General Public License as published by    the Free Software Foundation, either version 3 of the License, or    (at your option) any later version.    pdf2table is distributed in the hope that it will be useful,    but WITHOUT ANY WARRANTY; without even the implied warranty of    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the    GNU General Public License for more details.    You should have received a copy of the GNU General Public License    along with pdf2table.  If not, see <http://www.gnu.org/licenses/>.
*/

package pdf2xml;

import java.awt.Button;
import java.awt.Checkbox;
import java.awt.Choice;
import java.awt.Color;
import java.awt.Frame;
import java.awt.Label;
import java.awt.Panel;
import java.awt.ScrollPane;
import java.awt.TextArea;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.List;

public class SemiOutputFrame extends Frame {

    public List<TextArea> text_areas;
    public List<Checkbox> checkboxes = new ArrayList<Checkbox>();
    TextArea title_field;
    public List<Table> tables;

    java.awt.Font f1;
    java.awt.Font f2;
    java.awt.Font f3;

    Panel button_panel;
    Panel main_panel;
    Panel table_panel;
    ScrollPane sc;

    public int counter;
    Choice ch;
    public List<Font> fonts;

    boolean add_new_column;
    boolean delete_column;

    int column_position;
    String path;
    Table undo_table;

    public SemiOutputFrame(List<Table> tables2, List<Font> fonts2, String p) {
        super("Table Verification");
        try {

            this.setSize(1000, 700);
            this.setLayout(null);
            this.setBackground(Color.LIGHT_GRAY);
            this.setLocationRelativeTo(null);
            this.setResizable(true);
            this.path = p;

            this.add_new_column = false;
            this.delete_column = false;

            this.fonts = fonts2;
            this.counter = 0;

            this.f1 = new java.awt.Font("SansSerif", java.awt.Font.PLAIN, 11);
            this.f2 = new java.awt.Font("SansSerif", java.awt.Font.PLAIN, 13);
            this.f3 = new java.awt.Font("SansSerif", java.awt.Font.PLAIN, 15);

            this.addWindowListener(new WindowAdapter() {
                public void windowClosing(WindowEvent e) {

                    Runtime.getRuntime().exit(0);
                }
            });

            Color panel_background_color = new Color(217, 217, 217);

            this.main_panel = new Panel();
            this.main_panel.setBounds(0, 0, 1000, 700);
            this.main_panel.setBackground(panel_background_color);
            this.main_panel.setLayout(null);

            Panel button_panel = new Panel();
            button_panel.setBounds(800, 0, 200, 700);
            button_panel.setBackground(panel_background_color);
            button_panel.setLayout(null);

            Color color3 = new Color(203, 203, 203);

            Button next = new Button("Next Table");
            next.setBounds(15, 40, 170, 20);
            next.setBackground(color3);
            next.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    next();
                }
            });
            button_panel.add(next);
            Button back = new Button("Previous Table");
            back.setBounds(15, 70, 170, 20);
            back.setBackground(color3);
            back.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    if (counter >= 2) {
                        counter = counter - 2;
                        next();
                    }
                }
            });
            button_panel.add(back);

            Button delete = new Button("Delete Table");
            delete.setBounds(15, 120, 170, 20);
            delete.setBackground(color3);
            delete.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    delete();
                }
            });
            button_panel.add(delete);

            Button merge = new Button("Merge with Previuos Table");
            merge.setBounds(15, 150, 170, 20);
            merge.setBackground(color3);
            merge.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    merge();
                }
            });
            button_panel.add(merge);

            Button edit_table = new Button("Edit Table");
            edit_table.setBounds(15, 180, 170, 20);
            edit_table.setBackground(color3);
            edit_table.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    edit_table_clicked();
                }
            });
            button_panel.add(edit_table);

            Button cancel_exit = new Button("Close");
            cancel_exit.setBounds(15, 260, 170, 20);
            cancel_exit.setBackground(color3);
            cancel_exit.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    cancel_exit_clicked();
                }
            });
            button_panel.add(cancel_exit);

            Label note = new Label("Note: To edit the content of");
            Label note2 = new Label("       a cell, simply click on it.");
            Label note3 = new Label("To delete/insert columns or");
            Label note4 = new Label("rows, click on \"Edit Table\".");

            note.setBounds(15, 600, 170, 20);
            note2.setBounds(15, 620, 170, 20);
            note3.setBounds(15, 650, 170, 20);
            note4.setBounds(15, 670, 170, 20);

            button_panel.add(note);
            button_panel.add(note2);
            button_panel.add(note3);
            button_panel.add(note4);

            this.sc = new ScrollPane(ScrollPane.SCROLLBARS_ALWAYS);
            this.sc.getVAdjustable().setUnitIncrement(10);
            this.sc.getHAdjustable().setUnitIncrement(10);
            this.sc.setBounds(5, 25, 795, 670);

            this.sc.setBackground(panel_background_color);
            this.sc.setWheelScrollingEnabled(true);
            this.sc.validate();

            this.table_panel = new Panel();
            this.table_panel.setLayout(null);
            this.sc.add(this.table_panel);

            main_panel.add(button_panel);
            main_panel.add(this.sc);
            this.add(main_panel);

            this.tables = tables2;
            next();

        } catch (Exception e) {
            System.out
                    .println("Exception in class: SemiOutputFrame and method: constructor. "
                            + e);
        }
    }

    public void cancel_exit_clicked() {
        try {
            if (this.counter >= 1 && this.counter <= this.tables.size()) {
                Table prev = this.tables.get(this.counter - 1);
                prev.title = title_field.getText();

                int i = 1;
                while (i < this.checkboxes.size()) {
                    Checkbox c = this.checkboxes.get(i);
                    if (c.getState() == false) {
                        break;
                    }
                    i++;
                } // end of while
                if (i > prev.datarow_begin) {
                    prev.datarow_begin = i;
                }
                this.checkboxes.clear();
            }
            XmlOutput.create(this.tables, this.fonts, this.path);
            this.dispose();
        } catch (Exception e) {
            System.out
                    .println("Exception in class: SemiOutputFrame and method: cancel_exit_clicked. "
                            + e);
        }
    }

    public void next() {

        try {

            if (this.counter >= 1 && this.counter <= this.tables.size()) {
                Table prev = this.tables.get(this.counter - 1);
                prev.title = title_field.getText();

                int i = 1;
                while (i < this.checkboxes.size()) {
                    Checkbox c = this.checkboxes.get(i);
                    if (c.getState() == false) {
                        break;
                    }
                    i++;
                }
                if (i > prev.datarow_begin) {
                    prev.datarow_begin = i;
                }
                this.checkboxes.clear();
            }
            if (this.counter < this.tables.size()) {
                Table current_table = this.tables.get(this.counter);
                draw_table(current_table);
                this.counter++;

            } else if (this.counter == this.tables.size()) {
                XmlOutput.create(this.tables, this.fonts, this.path);
            }
        } catch (Exception e) {
            System.out
                    .println("Exception in class: gui and method: next. " + e);
        }
    }

    
    public void delete() {
        try {
            if (this.counter > 0 && this.counter <= this.tables.size()) {
                this.tables.remove(this.counter - 1);
                this.counter--;
                next();
            }
        } catch (Exception e) {
            System.out
                    .println("Exception in class: SemiOutputFrame and method: delete. "
                            + e);
        }
    }

    
    public void merge() {
        try {
            if (this.counter >= 2) {
                Table now = this.tables.get(this.counter - 1);
                Table previous = this.tables.get(this.counter - 2);

                int count_of_columns = Math.min(now.columns.size(),
                        previous.columns.size());
                System.out.println(count_of_columns);
                for (int i = 0; i < count_of_columns; i++) {
                    Column previous_column = previous.columns.get(i);
                    Column now_column = now.columns.get(i);
                    previous_column.cells.addAll(now_column.cells);
                }
                this.tables.remove(this.counter - 1);
                this.counter = this.counter - 2;
                next();
            }
        } catch (Exception e) {
            System.out
                    .println("Exception in class: SemiOutputFrame and method: merge. "
                            + e);
        }

    }

    
    public void draw_table(Table t) {
        this.table_panel.removeAll();

        try {
            this.text_areas = new ArrayList<TextArea>();
            this.checkboxes = new ArrayList<Checkbox>();

            Label page_information = new Label("Table on page " + t.page);
            page_information.setFont(this.f3);
            page_information.setBounds(20, 20, 150, 20);
            table_panel.add(page_information);

            Color color1 = new Color(164, 164, 164);

            int labels_start = 70;
            int column_width = 0;

            for (int l = 1; l <= t.columns.size(); l++) {
                Column current_column = t.columns.get(l - 1);
                column_width = current_column.right - current_column.left;
                if (column_width < 20) {
                    column_width = 20;
                }
                Label l1 = new Label("" + l, Label.CENTER);
                l1.setBounds(labels_start, 110, column_width, 20);
                this.table_panel.add(l1);
                labels_start = labels_start + column_width + 10;
            }

            Column first_column = t.columns.get(0);
            int label_top = 140;
            int label_left = 70;
            int max_height = 0;

            for (int k = 1; k <= first_column.cells.size(); k++) {
                label_left = 70;
                Label l1 = new Label("" + k, Label.CENTER);
                this.table_panel.add(l1);
                l1.setBounds(40, label_top, 20, 20);

                Checkbox checkbox = new Checkbox();
                checkbox.setBounds(20, label_top, 20, 20);

                max_height = 0;
                for (int m = 0; m < t.columns.size(); m++) {

                    Column current_column = t.columns.get(m);
                    column_width = current_column.right - current_column.left;
                    if (column_width < 20) {
                        column_width = 20;
                    }
                    List<Text_Element> current_texts = new ArrayList<Text_Element>(
                            current_column.cells);
                    Text_Element current_t = current_texts.get(k - 1);
                    if (m == 0) {
                        if (k - 1 < t.datarow_begin) {
                            checkbox.setState(true);
                        }
                    }

                    if (current_t.value.equals("null")) {
                        current_t.value = "";
                    }
                    TextArea ta = new TextArea(current_t.value, 1, 1,
                            TextArea.SCROLLBARS_NONE);
                    ta.setBackground(Color.WHITE);
                    ta.setEditable(false);
                    this.table_panel.add(ta);

                    if (current_t.colspan > 1) {
                        for (int n = 1; n < current_t.colspan; n++) {
                            if (m + n < t.columns.size()) {
                                Column next_column = t.columns.get(m + n);
                                column_width = 10
                                        + column_width
                                        + (next_column.right - next_column.left);
                                text_areas.add(null);
                            }
                        }
                        m = m + current_t.colspan - 1;
                    }

                    ta.setBounds(label_left, label_top, column_width,
                            current_t.count_lines * 20);
                    max_height = Math.max(max_height,
                            current_t.count_lines * 20);

                    ta.addMouseListener(new MouseAdapter() {
                        public void mouseClicked(MouseEvent e) {
                            TextArea current_ta = (TextArea) e.getSource();
                            int i = text_areas.indexOf(current_ta);
                            edit(i);
                        }
                    });

                    label_left = label_left + column_width + 10;
                    this.text_areas.add(ta);
                }
                this.table_panel.add(checkbox);
                this.checkboxes.add(checkbox);

                label_top = label_top + max_height + 10;
            }

            Label title_label = new Label("Title");
            title_label.setBounds(40, 50, 60, 20);
            table_panel.add(title_label);

            title_field = new TextArea("", 1, 1, TextArea.SCROLLBARS_NONE);
            title_field.setFont(this.f2);
            title_field.setLocation(40, 70);
            title_field.setBounds(40, 70, labels_start - 10, 25);
            title_field.setBackground(color1);
            title_field.setText(t.title);
            table_panel.add(title_field);

            this.table_panel.setSize(labels_start, label_top);
            this.sc.validate();
        } catch (Exception e) {
            System.out.println("Exception: " + e);
        }
    }

    
    public void edit(int index) {
        try {
            Table current_table = this.tables.get(this.counter - 1);
            this.undo_table = (Table) current_table.clone();

            final EditTableCellFrame etc = new EditTableCellFrame(
                    current_table, index);
            etc.addWindowListener(new WindowAdapter() {
                public void windowClosed(WindowEvent e) {
                    merge_cells(etc.changed_index, etc.same, etc.merge_l,
                            etc.merge_r, etc.merge_u, etc.merge_d);
                }
            });
            etc.setVisible(true);

        } catch (Exception e) {
            System.out
                    .println("Exception in class: SemiOutputFrame and method: edit. "
                            + e);
        }
  
     }

    
    public void edit_table_clicked() {
        try {
            Table current_table = this.tables.get(this.counter - 1);
            this.undo_table = (Table) current_table.clone();

            final EditTableContentFrame etc = new EditTableContentFrame(
                    current_table);
            etc.setVisible(true);
            etc.addWindowListener(new WindowAdapter() {
                public void windowClosed(WindowEvent e) {
                    change_table(etc.changed_table);
                    counter--;
                    next();
                }
            });

        } catch (Exception e) {
            System.out
                    .println("Exception in class: SemiOutputFrame and method: edit_table_clicked. "
                            + e);
        }
    }

    
    public void change_table(Table t) {
        Table current_table = this.tables.get(this.counter);
        current_table = (Table) t.clone();
    }

    
    public void merge_cells(int index, boolean s, boolean l, boolean r,
            boolean u, boolean d) {
        counter--;
        try {
            Table current_table = this.tables.get(this.counter);
            this.undo_table = (Table) current_table.clone();

            int position = index;
            int line_pos = position / current_table.columns.size();
            int cell_pos = position % current_table.columns.size();

            if (s == true) {
                // do nothing
            } else if (l == true) {
                Column c = current_table.columns.get(cell_pos);
                Text_Element current_t = c.cells.get(line_pos);
                if (cell_pos > 0) {
                    // element left exists
                    Column c2 = current_table.columns.get(cell_pos - 1);
                    Text_Element left = c2.cells.get(line_pos);
                    left.value = left.value + " " + current_t.value;
                    left.width += current_t.width;
                    left.colspan++;
                }
            } else if (r == true) {
                Column c = current_table.columns.get(cell_pos);
                Text_Element current_t = c.cells.get(line_pos);
                if (cell_pos < current_table.columns.size() - 1) {
                    // element right exists
                    Column c2 = current_table.columns.get(cell_pos + 1);
                    Text_Element right = c2.cells.get(line_pos);
                    current_t.value = current_t.value + " " + right.value;
                    current_t.width += right.width;
                    current_t.colspan++;
                }
            } else if (u == true) {
                Column c = current_table.columns.get(cell_pos);
                Text_Element current_t = c.cells.get(line_pos);
                if (line_pos > 0) {
                    // element above exits
                    Text_Element above = c.cells.get(line_pos - 1);
                    above.value = above.value + "\n" + current_t.value;
                    above.count_lines = above.count_lines
                            + current_t.count_lines;
                    current_t.count_lines = 1;
                    current_t.value = "";
                }
            } else if (d == true) {
                Column c = current_table.columns.get(cell_pos);
                Text_Element current_t = c.cells.get(line_pos);
                if (line_pos < c.cells.size() - 1) {
                    // element below exists
                    Text_Element under = c.cells.get(line_pos + 1);
                    current_t.value = current_t.value + "\n" + under.value;
                    current_t.count_lines += under.count_lines;
                    under.count_lines = 1;
                    under.value = "";
                }
            }
            next();
        } catch (Exception e) {
            System.out
                    .println("Exception in class: SemiOutputFrame and method: merge_cells. "
                            + e);
        }
    }

}
