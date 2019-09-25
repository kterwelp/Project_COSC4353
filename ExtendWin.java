// package editorP;

import java.awt.*;
import javax.swing.*;
import java.io.*;
import java.awt.event.*;
import javax.swing.plaf.metal.*;
import javax.swing.UIManager.*;
import javax.swing.JFrame;
import javax.swing.text.*;
import javax.swing.tree.*;
import java.util.*;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;

class ExtendWin extends JFrame implements ActionListener{
    
    JTextPane text;
    Document doc;
    JFrame frame;
    JScrollPane scroll_bar;
    JButton b1, b2, b3;
    File O_File;

    String direct = "";
    String project = "";
    String file = "";
    File curFile;

    ExtendWin(File file){
        this.O_File = file;
        this.curFile = file;
        String fileName = O_File.toString();
        int idex = fileName.lastIndexOf("\\");
        String delfileName = fileName.substring(0, idex + 1);
        fileName = fileName.replace(delfileName, "");
        System.out.println(fileName);

        frame = new JFrame(fileName);

        Font f = new Font("sans-serif", Font.PLAIN, 25);
        UIManager.put("Menu.font", f);
        UIManager.put("MenuItem.font", f);
        UIManager.put("FileChooser.listFont",new
        javax.swing.plaf.FontUIResource(f));

        text = new JTextPane();
        doc = text.getDocument();
        doc.addDocumentListener(new textareaMonitor());
        scroll_bar = new JScrollPane();

        JMenuBar menubar = new JMenuBar();

        JMenu file_menu = new JMenu("File");
        JMenuItem fm_newFile = new JMenuItem("New File");
        JMenuItem fm_openFile = new JMenuItem("Open File");
        JMenuItem fm_saveFile = new JMenuItem("Save File");
        JMenuItem fm_saveFileAs = new JMenuItem("Save File As...");
        JMenuItem fm_deleteFile = new JMenuItem("Delete File");
        JMenuItem fm_exit = new JMenuItem("Exit");

        fm_newFile.addActionListener(this);
        fm_openFile.addActionListener(this);
        fm_saveFile.addActionListener(this);
        fm_saveFileAs.addActionListener(this);
        fm_deleteFile.addActionListener(this);
        fm_exit.addActionListener(this);

        file_menu.add(fm_newFile);
        file_menu.add(fm_openFile);
        file_menu.add(fm_saveFile);
        file_menu.add(fm_saveFileAs);
        file_menu.add(fm_deleteFile);
        file_menu.addSeparator();
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

        //frame.setDefaultCloseOperation(EXIT_ON_CLOSE);
        frame.setJMenuBar(menubar);
        frame.setLayout(new BorderLayout());
        frame.add(text);
        frame.add(scroll_bar);
        frame.setSize(1500,1200);
        frame.setVisible(true);

        text.setFont(text.getFont().deriveFont(28f));
        try {
            String line ="", all_line = "";

            FileReader fr = new FileReader(O_File);
            BufferedReader br = new BufferedReader(fr);

            all_line = br.readLine();

            while ((line = br.readLine()) != null){
                all_line = all_line + "\n" + line;
            }
            br.close();
            text.setText(all_line);
            scroll_bar.setViewportView(text);
            
            String dir = O_File.getAbsolutePath();
            direct = dir;
        }   
        catch (Exception evt){
            JOptionPane.showMessageDialog(frame, evt.getMessage());
        }

        //frame.addWindowListener(new WindowCloser());
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
        else if (s.equals("Open File")){

            JFileChooser choose_file = new JFileChooser(direct);
            choose_file.setPreferredSize(new Dimension(900,700));
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
                    br.close();
                    text.setText(all_line);
                    scroll_bar.setViewportView(text);
                    dir = file.getAbsolutePath();
                    curFile = file;

                    String fileName = file.toString();
                    int idex = fileName.lastIndexOf("\\");
                    String delfileName = fileName.substring(0, idex + 1);
                    fileName = fileName.replace(delfileName, "");

                    frame.setTitle(fileName);
                }   
                catch (Exception evt){
                    JOptionPane.showMessageDialog(frame, evt.getMessage());
                }

                direct = dir;
            }
            else
                JOptionPane.showMessageDialog(frame, "No File Is Opened");

        }
        else if (s.equals("Save File")) {

            if (curFile != null)
            {
                File file = new File(curFile.getAbsolutePath());
                try{
                    FileWriter wr = new FileWriter(file, false);
                    BufferedWriter w = new BufferedWriter(wr);

                    w.write(text.getText());
                    w.flush();
                    w.close();

                    direct = file.getParent();

                    curFile = file;
                }
                catch (Exception evt){
                    JOptionPane.showMessageDialog(frame, evt.getMessage()); 
                } 
            }
            else
                JOptionPane.showMessageDialog(frame, "Unable to Save File");                  
            
        }
        else if (s.equals("Save File As...")) {

            JFileChooser choose_file = new JFileChooser(direct);
            choose_file.setPreferredSize(new Dimension(900,700));
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

                    direct = file.getParent();

                    curFile = file;

                    String fileName = file.toString();
                    int idex = fileName.lastIndexOf("\\");
                    String delfileName = fileName.substring(0, idex + 1);
                    fileName = fileName.replace(delfileName, "");

                    frame.setTitle(fileName);
                }
                catch (Exception evt){
                    JOptionPane.showMessageDialog(frame, evt.getMessage()); 
                }
            }
            else
                JOptionPane.showMessageDialog(frame, "No File Is Saved");

        }
        else if (s.equals("Delete File")) {
            
            if (curFile.delete())
            {
                text.setText(""); 
                JOptionPane.showMessageDialog(frame, "File Deleted");
                curFile = null;
                frame.setTitle("");
            }
            else
            {
                JOptionPane.showMessageDialog(frame, "Unable to Delete File");
            }
        }
        else if (s.equals("New File")){
            
            JFileChooser choose_file = new JFileChooser(direct);
            choose_file.setPreferredSize(new Dimension(900,700));
            int r = choose_file.showSaveDialog(null);
            String dir = "";
            if (r == JFileChooser.APPROVE_OPTION){
                File file = new File(choose_file.getSelectedFile().getAbsolutePath());
                try{
                    FileWriter wr = new FileWriter(file, false);
                    BufferedWriter w = new BufferedWriter(wr);

                    w.write("");
                    w.flush();
                    w.close();
                    dir = file.getAbsolutePath();

                    direct = file.getParent();

                    curFile = file;

                    text.setText("");     
                    scroll_bar.setViewportView(text);

                    String fileName = file.toString();
                    int idex = fileName.lastIndexOf("\\");
                    String delfileName = fileName.substring(0, idex + 1);
                    fileName = fileName.replace(delfileName, "");

                    frame.setTitle(fileName);
                }
                catch (Exception evt){
                    JOptionPane.showMessageDialog(frame, evt.getMessage()); 
                }
            }
            else
                JOptionPane.showMessageDialog(frame, "Unable to Create New File");
            
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
}