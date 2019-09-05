import java.awt.*;
import javax.swing.*;
import java.io.*;
import java.awt.event.*;
import javax.swing.plaf.metal.*;
import javax.swing.UIManager;
import javax.swing.JFrame;
import javax.swing.text.*;

class Editor extends JFrame implements ActionListener{
    JTextArea text;
    JFrame frame;
    JScrollPane scroll_bar;
    
    String direct = "";

    Editor(){
        frame = new JFrame("editor");

        text = new JTextArea();
        scroll_bar = new JScrollPane();

        JMenuBar menubar = new JMenuBar();

        JMenu file_menu = new JMenu("File");
        JMenuItem fm_new = new JMenuItem("New");
        JMenuItem fm_open = new JMenuItem("Open");
        JMenuItem fm_save = new JMenuItem("Save");
        JMenuItem fm_exit = new JMenuItem("Exit");

        fm_new.addActionListener(this);
        fm_open.addActionListener(this);
        fm_save.addActionListener(this);
        fm_exit.addActionListener(this);

        file_menu.add(fm_new);
        file_menu.add(fm_open);
        file_menu.add(fm_save);
        file_menu.add(fm_exit);

        JMenu edit_menu = new JMenu("Edit");
        JMenuItem em_undo = new JMenuItem("Undo");
        JMenuItem em_redo = new JMenuItem("Redo");
        JMenuItem em_cut = new JMenuItem("Cut");
        JMenuItem em_copy = new JMenuItem("Copy");
        JMenuItem em_paste = new JMenuItem("Paste");
        JMenuItem em_find = new JMenuItem("Find");

        em_undo.addActionListener(this);
        em_redo.addActionListener(this);
        em_cut.addActionListener(this);
        em_copy.addActionListener(this);
        em_paste.addActionListener(this);
        em_find.addActionListener(this);

        edit_menu.add(em_undo);
        edit_menu.add(em_redo);
        edit_menu.add(em_cut);
        edit_menu.add(em_copy);
        edit_menu.add(em_paste);
        edit_menu.add(em_find);

        JMenu proj_menu = new JMenu("Project");
        JMenuItem pm_debug = new JMenuItem("Debug");
        JMenuItem pm_run = new JMenuItem("Run");

        pm_debug.addActionListener(this);
        pm_run.addActionListener(this);

        proj_menu.add(pm_debug);
        proj_menu.add(pm_run);

        menubar.add(file_menu);
        menubar.add(edit_menu);
        menubar.add(proj_menu);

        frame.setDefaultCloseOperation(EXIT_ON_CLOSE);
        frame.setJMenuBar(menubar);
        frame.add(text);
        frame.add(scroll_bar);
        frame.setSize(1100,800);
        frame.setVisible(true);
    }

    public void actionPerformed(ActionEvent e){
        String s = e.getActionCommand();

        if (s.equals("Cut")){
            text.cut();
        }
        else if (s.equals("Copy")){
            text.copy();
        }
        else if (s.equals("Paste")){
            text.paste();
        }
        else if (s.equals("Exit")){
            frame.dispose();
            System.exit(0);
        }
        else if (s.equals("Open")){
            JFileChooser choose_file = new JFileChooser("f:");
            int r = choose_file.showOpenDialog(null);
            String dir = "";
            if (r == JFileChooser.APPROVE_OPTION){
                File file = new File(choose_file.getSelectedFile().getAbsolutePath());

                try {
                    String line ="", all_line = "";

                    FileReader fr = new FileReader(file);
                    BufferedReader br = new BufferedReader(fr);

                    all_line = br.readLine();

                    while ((line = br.readLine()) != null){
                        all_line = all_line + "\n" + line;
                    }
                    text.setText(all_line);
                    scroll_bar.setViewportView(text);
                    dir = file.getAbsolutePath();
                }
                catch (Exception evt){
                    JOptionPane.showMessageDialog(frame, evt.getMessage());
                }
            }
            else
                JOptionPane.showMessageDialog(frame, "No File Is Opened");

            direct = dir;
        }
        else if (s.equals("Save")){
            JFileChooser choose_file = new JFileChooser("f:");
            int r = choose_file.showSaveDialog(null);
            String dir = "";
            if (r == JFileChooser.APPROVE_OPTION){
                File file = new File(choose_file.getSelectedFile().getAbsolutePath());
                try{
                    FileWriter wr = new FileWriter(file, false);
                    BufferedWriter w = new BufferedWriter(wr);

                    w.write(text.getText());
                    w.flush();
                    w.close();
                    dir = file.getAbsolutePath();
                }
                catch (Exception evt){
                    JOptionPane.showMessageDialog(frame, evt.getMessage()); 
                }
            }
            else
                JOptionPane.showMessageDialog(frame, "No File Is Saved");

            direct = dir;
        }
        else if (s.equals("New")){
            text.setText("");
            scroll_bar.setViewportView(text);
        }
        else if (s.equals("Debug")){

            try{
                int file_i = direct.lastIndexOf("\\") + 1;
                String file_name = direct.substring(file_i);
                String file_dir = direct.substring(0, file_i);
                String command = String.format("cmd /c start cmd.exe /K \"pushd %s && javac %s\"", file_dir, file_name);
                Runtime.getRuntime().exec(command);
            }
            catch (Exception evt){ 
                evt.printStackTrace(); 
            } 
            
        }
        else if (s.equals("Run")){
            try{
                int file_ie = direct.indexOf(".java");
                int file_i = direct.lastIndexOf("\\") + 1;
                String file_name = direct.substring(file_i, file_ie);
                String file_dir = direct.substring(0, file_i);
                String command = String.format("cmd /c start cmd.exe /K \"pushd %s && java %s\"", file_dir, file_name);
                Runtime.getRuntime().exec(command);
            }
            catch (Exception evt){ 
                evt.printStackTrace(); 
            } 
        }
    }

    public static void main(String[] argv){
        Editor e = new Editor();
    }
}

