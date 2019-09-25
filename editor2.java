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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;


class editor2 extends JFrame implements ActionListener{
    
    JTextPane text;
    Document doc;
    JFrame frame;
    JScrollPane scroll_bar;
    JButton b1, b2, b3;
    FileExplorer panel;
    
    String direct = "";
    String project = "";
    String file = "";
    File curFile;
            
    editor2()
    {
        frame = new JFrame("editor");

        Font f = new Font("sans-serif", Font.PLAIN, 25);
        UIManager.put("Menu.font", f);
        UIManager.put("MenuItem.font", f);
        UIManager.put("FileChooser.listFont",new
        javax.swing.plaf.FontUIResource(f));

        text = new JTextPane();
        ((AbstractDocument) text.getDocument()).setDocumentFilter(new customDocumentFilter());
        doc = text.getDocument();
        doc.addDocumentListener(new textareaMonitor());
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
        JMenuItem fm_saveFileAs = new JMenuItem("Save File As...");
        JMenuItem fm_deleteFile = new JMenuItem("Delete File");
        JMenuItem fm_exit = new JMenuItem("Exit");

        fm_newProject.addActionListener(this);
        fm_openProject.addActionListener(this);
        fm_saveProject.addActionListener(this);
        fm_closeProject.addActionListener(this);
        fm_newFile.addActionListener(this);
        fm_openFile.addActionListener(this);
        fm_saveFile.addActionListener(this);
        fm_saveFileAs.addActionListener(this);
        fm_deleteFile.addActionListener(this);
        fm_exit.addActionListener(this);

        file_menu.add(fm_newProject);
        file_menu.add(fm_openProject);
        file_menu.add(fm_saveProject);
        file_menu.add(fm_closeProject);
        file_menu.addSeparator();
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
                JOptionPane.showMessageDialog(frame, "Cannot start a new project. Please close the current project.");
            }
        }
        else if (s.equals("Open File")){

            JFileChooser choose_file = new JFileChooser(direct);
            choose_file.setPreferredSize(new Dimension(900,700));
            int r = choose_file.showOpenDialog(null);
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
                    direct = file.getParent();
                    curFile = file;

                    File curDir = new File(file.getParent());

                    panel.updateList(curDir);

                    curFile = file;
                }   
                catch (Exception evt){
                    JOptionPane.showMessageDialog(frame, evt.getMessage());
                }
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

                    File curDir = new File(file.getParent());

                    panel.updateList(curDir);

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

                    File curDir = new File(file.getParent());

                    panel.updateList(curDir);

                    direct = file.getParent();

                    curFile = file;
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
            if (r == JFileChooser.APPROVE_OPTION){
                File file = new File(choose_file.getSelectedFile().getAbsolutePath());
                try{
                    FileWriter wr = new FileWriter(file, false);
                    BufferedWriter w = new BufferedWriter(wr);

                    w.write(text.getText());
                    w.flush();
                    w.close();

                    File curDir = new File(file.getParent());

                    panel.updateList(curDir);

                    direct = file.getParent();

                    curFile = file;

                    text.setText("");     
                    scroll_bar.setViewportView(text);
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
                String command = String.format("cmd /c start cmd.exe /K \"pushd %s && javac *.java\"", file_dir);
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
    
    final class customDocumentFilter extends DocumentFilter
    {
    	
    	private final StyledDocument styledDocument = text.getStyledDocument();
    	private final StyleContext styleContext = StyleContext.getDefaultStyleContext();
    	
    	private final AttributeSet booleanAttributeSet = styleContext.addAttribute(styleContext.getEmptySet(), StyleConstants.Foreground, Color.BLUE);
    	private final AttributeSet arithmeticAttributeSet = styleContext.addAttribute(styleContext.getEmptySet(), StyleConstants.Foreground, Color.RED);
    	private final AttributeSet quoteAttributeSet = styleContext.addAttribute(styleContext.getEmptySet(), StyleConstants.Foreground, new Color(0,102,0));
    	private final AttributeSet blackAttributeSet = styleContext.addAttribute(styleContext.getEmptySet(), StyleConstants.Foreground, Color.BLACK);
    	
    	Pattern patternBool = buildPatternBool();
    	Pattern patternQuotes = buildPatternQuotes();
    	
    	@Override
        public void insertString(FilterBypass fb, int offset, String text, AttributeSet attributeSet) throws BadLocationException {
            super.insertString(fb, offset, text, attributeSet);

            handleTextChanged();
        }

        @Override
        public void remove(FilterBypass fb, int offset, int length) throws BadLocationException {
            super.remove(fb, offset, length);

            handleTextChanged();
        }

        @Override
        public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attributeSet) throws BadLocationException {
            super.replace(fb, offset, length, text, attributeSet);

            handleTextChanged();
        }
        
        private Pattern buildPatternBool()
        {
        	Pattern regexBool = Pattern.compile("\\b(?:if|else|while|for)\\b");
        	
        	return regexBool;
        }
        
        private Pattern buildPatternQuotes()
        {
        	Pattern regexQuotes = Pattern.compile("\"([^\"]*)\"");
        	
        	return regexQuotes;
        }
        
        private void handleTextChanged()
        {
        	SwingUtilities.invokeLater(new Runnable() {
        		@Override
        		public void run() 
        		{updateTextStyles();}
        	});
        }
        
        private void updateTextStyles() 
        {
        	// Clears existing styles
        	styledDocument.setCharacterAttributes(0, text.getText().length(), blackAttributeSet, true);
        	
        	String temp = text.getText();
        	char [] tempCharArray = temp.toCharArray();
        	
        	// Look for matching substrings and highlight them
        	Matcher matcherBool = patternBool.matcher(temp);
        	Matcher matcherQuotes = patternQuotes.matcher(temp);
        	
        	while (matcherBool.find())
        		styledDocument.setCharacterAttributes(matcherBool.start(), matcherBool.end()- matcherBool.start(), booleanAttributeSet, false);
        	
        	while (matcherQuotes.find())
        		styledDocument.setCharacterAttributes(matcherQuotes.start(), matcherQuotes.end() - matcherQuotes.start(), quoteAttributeSet, false);;
        	
        	
        	for (int i = 0; i < tempCharArray.length; i++)
        	{
        		switch(tempCharArray[i])
        		{
        		case '+':
        		case '-':
        		case '*':
        		case '/':
        		case '%':
        			System.out.println(i);
        			styledDocument.setCharacterAttributes(i ,1, arithmeticAttributeSet, false);
        			
        		}
        		
        		// If conditional to check for || and && characters - we need to check for an extra character - this handles it
        		
        		
        		if (tempCharArray[i] == '|' || tempCharArray[i] == '&')
        		{
        			if (i+1 < tempCharArray.length)
        				
        				switch(tempCharArray[i+1])
        				{
        				case '|':
        				case '&':
        					styledDocument.setCharacterAttributes(i, 2, arithmeticAttributeSet, false);
        				}
        		}
        		
        		// Another branch to check for strings while we're still in the loop
        		
        		
        	}
        	
        }
    }
}


  