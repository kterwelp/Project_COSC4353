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
import FileExplorer;

class editor2 extends JFrame implements ActionListener{
    
    JTextArea text;
    JFrame frame;
    JScrollPane scroll_bar;
    JButton b1, b2, b3;
    FileExplorer panel;
    
    String direct = "";
    String project = "";
    String file = "";

    editor2(){
        frame = new JFrame("editor");

        Font f = new Font("sans-serif", Font.PLAIN, 25);
        UIManager.put("Menu.font", f);
        UIManager.put("MenuItem.font", f);
        UIManager.put("FileChooser.listFont",new
        javax.swing.plaf.FontUIResource(f));

        text = new JTextArea();
        scroll_bar = new JScrollPane();

        JMenuBar menubar = new JMenuBar();

        JMenu file_menu = new JMenu("File");
        JMenuItem fm_newProject = new JMenuItem("New Project");
        JMenuItem fm_openProject = new JMenuItem("Open Project");
        JMenuItem fm_saveProject = new JMenuItem("Save Project");
        JMenuItem fm_closeProject = new JMenuItem("Close Project");
        JMenuItem fm_newFile = new JMenuItem("New File");
        JMenuItem fm_openFile = new JMenuItem("Open File");
        JMenuItem fm_saveFile = new JMenuItem("Save File");
        JMenuItem fm_exit = new JMenuItem("Exit");

        fm_newProject.addActionListener(this);
        fm_openProject.addActionListener(this);
        fm_saveProject.addActionListener(this);
        fm_closeProject.addActionListener(this);
        fm_newFile.addActionListener(this);
        fm_openFile.addActionListener(this);
        fm_saveFile.addActionListener(this);
        fm_exit.addActionListener(this);

        file_menu.add(fm_newProject);
        file_menu.add(fm_openProject);
        file_menu.add(fm_saveProject);
        file_menu.add(fm_closeProject);
        file_menu.addSeparator();
        file_menu.add(fm_newFile);
        file_menu.add(fm_openFile);
        file_menu.add(fm_saveFile);
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

        frame.setDefaultCloseOperation(EXIT_ON_CLOSE);
        frame.setJMenuBar(menubar);
        panel = new FileExplorer();
        panel.getPreferredSize();
        frame.setLayout(new BorderLayout());
        frame.add(panel,BorderLayout.SOUTH);
        frame.add(text);
        frame.add(scroll_bar);
        frame.setSize(1500,1200);
        frame.setVisible(true);

        text.setFont(text.getFont().deriveFont(28f));

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
        else if (s.equals("Close Project")) {

            if (direct != "") {

                direct = "";

                JOptionPane.showMessageDialog(frame, "Project Closed");

                panel.updateList(new File(""));
            } 
            else {
                JOptionPane.showMessageDialog(frame, "No project is open.");
            }

        }
        else if (s.equals("Open Project")) {

            if (direct == "") {

                JFileChooser choose_file = new JFileChooser("f:");
                choose_file.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                choose_file.setPreferredSize(new Dimension(900,700));
                int r = choose_file.showOpenDialog(null);
                String dir = "";
                if (r == JFileChooser.APPROVE_OPTION){
                    File file = new File(choose_file.getSelectedFile().getAbsolutePath());

                    dir = file.getAbsolutePath();

                    panel.updateList(file);

                    JOptionPane.showMessageDialog(frame, "Currently in Project Directory: " + dir);

                    direct = dir;
                }
                else
                    JOptionPane.showMessageDialog(frame, "No File Is Opened");
            }
            else {
                JOptionPane.showMessageDialog(frame, "Cannot open project.  Please close current project");
            }
        }
        else if (s.equals("Save Project")) {
            
            if (direct != "") {
                File folder = new File(direct);
                for (File file : folder.listFiles()) {
                    if (file.getName().endsWith(".txt") || file.getName().endsWith(".java") || 
                        file.getName().endsWith(".class")) {
                            try{
                                FileWriter wr = new FileWriter(file, false);
                                BufferedWriter w = new BufferedWriter(wr);
            
                                w.write(text.getText());
                                w.flush();
                                w.close();
                            }
                            catch (Exception evt){
                                JOptionPane.showMessageDialog(frame, evt.getMessage()); 
                            }
                    }
                }

                JOptionPane.showMessageDialog(frame, "All Project Files Saved");
            } 
            else {
                JOptionPane.showMessageDialog(frame, "Cannot save project.");
            } 
        }
        else if (s.equals("New Project")) {

            if (direct == "") {

                JFileChooser choose_file = new JFileChooser("f:");
                choose_file.setPreferredSize(new Dimension(900,700));
                int r = choose_file.showSaveDialog(null);
                String dir = "";
                if (r == JFileChooser.APPROVE_OPTION){
                    File file = new File(choose_file.getSelectedFile().getAbsolutePath());
                    try{

                        file.mkdir();
            
                        dir = file.getAbsolutePath();

                        JOptionPane.showMessageDialog(frame, "New Project Created");

                        panel.updateList(file);
                    }
                    catch (Exception evt){
                        JOptionPane.showMessageDialog(frame, evt.getMessage()); 
                    }   

                    direct = dir;
                }
                else
                    JOptionPane.showMessageDialog(frame, "No Project Is Saved");
            }
            else {
                JOptionPane.showMessageDialog(frame, "Cannot start a new project. Please close any open project or file.");
            }
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
                    text.setText(all_line);
                    scroll_bar.setViewportView(text);
                    dir = file.getAbsolutePath();
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

                    File curDir = new File(file.getParent());

                    panel.updateList(curDir);

                    direct = file.getParent();
                }
                catch (Exception evt){
                    JOptionPane.showMessageDialog(frame, evt.getMessage()); 
                }
            }
            else
                JOptionPane.showMessageDialog(frame, "No File Is Saved");
            
        }
        else if (s.equals("New File")){
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

        try {
            for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception e) {
            // If Nimbus is not available, you can set the GUI to another look and feel.
        }

        editor2 e = new editor2();
    }
}
  
//   class MyJFrame extends JFrame {
//     JButton b1, b2, b3;
//     SimpleTree panel;
//     MyJFrame(String s) {
//       super(s);
//       panel = new SimpleTree();
//       getContentPane().add(panel,"Center");
//       setSize(600,600);
//       setVisible(true);
//       setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
//       addWindowListener(new WindowCloser());
//       }
  
//   }