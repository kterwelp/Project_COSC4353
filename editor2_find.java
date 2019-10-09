//package editorP;

import java.awt.*;

import javax.lang.model.util.ElementScanner6;
import javax.swing.*;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.awt.event.*;
import javax.swing.plaf.metal.*;
import javax.swing.UIManager.*;
import javax.swing.JFrame;
import javax.swing.text.*;
import javax.swing.tree.*;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.JSplitPane;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;

import java.util.concurrent.TimeUnit;

class editor2 extends JFrame implements ActionListener{
	
	// Helper functions
	/*
	 * @param - File folder of the project and file contents Holder ArrayList
	 * 
	 * Functionality - returns an ArrayList of all the contents of the project
	 * 
	 */
	
	private ArrayList<String> storeFilesForFolder (final File folder, ArrayList<String> projectText)
	{
		for (final File fileEntry : folder.listFiles())
		{
			if (fileEntry.isDirectory())
				projectText = storeFilesForFolder(fileEntry, projectText);
			// Read the text from each file
			else
			{
				String absolutePath = fileEntry.getAbsolutePath();
				File file = new File(absolutePath);
				
				try 
				{
					BufferedReader br = new BufferedReader(new FileReader(file));
					String line;
					String fileContents = "";
					
					while((line = br.readLine()) != null)
					{
						fileContents += line;
					}
					
					projectText.add(fileContents);
				} 
				catch (FileNotFoundException e) 
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		return projectText;
	}
	
	private void keyWordsProjectHelper(ArrayList<String> projectText)
	{
		for (String fileContent : projectText)
		{
			String [] fileContentArray = fileContent.split("\\s+");
			
			// Iterate through the tempArray contents and update the keywords
			for (String toCheck : fileContentArray)
			{
				int index = Arrays.binarySearch(keywords, toCheck);
				if (index >= 0)
					keywordsCountProject[index] += 1;
			}
		}
	}
    
	statsFrame sf;
	statsFrame sf_Project;
	ArrayList <String> projectText;
    JTextPane text;
    Document doc;
    JFrame frame;
    JScrollPane scroll_bar;
    JButton b1, b2, b3;
    FileExplorer panel;
    Find newFind;
    //JPanel findPanel;
    boolean Opened_File = false;
    JSplitPane jSplitPane1;
    
    static final String keywords[] = { "abstract", "assert", "boolean",
            "break", "byte", "case", "catch", "char", "class", "const",
            "continue", "default", "do", "double", "else", "extends", "false",
            "final", "finally", "float", "for", "goto", "if", "implements",
            "import", "instanceof", "int", "interface", "long", "native",
            "new", "null", "package", "private", "protected", "public",
            "return", "short", "static", "strictfp", "super", "switch",
            "synchronized", "this", "throw", "throws", "transient", "true",
            "try", "void", "volatile", "while" };
    static int keywordsCount[] = new int [keywords.length];
    static int keywordsCountProject[] = new int [keywords.length];
    static boolean change = false;
    
    String direct = "";
    String project = "";
    String file = "";
    File curFile;
    String allText = "";
    Document d;
    int length = 0;
    
    editor2()
    {
        frame = new JFrame("editor");

        Font f = new Font("sans-serif", Font.PLAIN, 25);
        UIManager.put("Menu.font", f);
        UIManager.put("MenuItem.font", f);
        UIManager.put("FileChooser.listFont",new
        javax.swing.plaf.FontUIResource(f));

        text = new JTextPane();
        text.setEditable(false);
        ((AbstractDocument) text.getDocument()).setDocumentFilter(new customDocumentFilter());
        doc = text.getDocument();
        
        
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
        JMenuItem pm_debug_pro = new JMenuItem("Debug Project");
        JMenuItem pm_debug_fi = new JMenuItem("Debug File");
        JMenuItem pm_run = new JMenuItem("Run");

        pm_debug_pro.addActionListener(this);
        pm_debug_fi.addActionListener(this);
        pm_run.addActionListener(this);

        proj_menu.add(pm_debug_pro);
        proj_menu.add(pm_debug_fi);
        proj_menu.add(pm_run);
        
        JMenu stat_menu = new JMenu("Statistics");
        JMenuItem sm_keywords = new JMenuItem("Keywords stats");
        JMenuItem sm_keywords_project = new JMenuItem("Keywords stats - Project");
        
        sm_keywords.addActionListener(this);
        sm_keywords_project.addActionListener(this);
        
        stat_menu.add(sm_keywords);
        stat_menu.add(sm_keywords_project);

        menubar.add(file_menu);
        menubar.add(edit_menu);
        menubar.add(proj_menu);
        menubar.add(stat_menu);

        frame.setDefaultCloseOperation(EXIT_ON_CLOSE);
        frame.setJMenuBar(menubar);
        panel = new FileExplorer();
        panel.getPreferredSize();
        scroll_bar = new JScrollPane(text);
       
        jSplitPane1 = new JSplitPane(SwingConstants.VERTICAL, panel, scroll_bar);
        
        jSplitPane1.setOneTouchExpandable(true);
        jSplitPane1.setResizeWeight(0.02);
       
        frame.setLayout(new BorderLayout());
      
        frame.getContentPane().add(jSplitPane1,BorderLayout.CENTER);
        frame.setSize(1000,800);
        frame.setVisible(true);

        text.setFont(text.getFont().deriveFont(28f));

        //frame.addWindowListener(new WindowCloser());
    }

    editor2(String s, String allText, int length)
    {
        //highlightWord(s, allText, length);
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

            if (project != "") {

                project = "";
                Opened_File = false;
                text.setText("");
                text.setEditable(false);

                JOptionPane.showMessageDialog(frame, "Project Closed");

                panel.updateList(new File(""));
            } 
            else {
                JOptionPane.showMessageDialog(frame, "No project is open.");
            }

        }
        else if (s.equals("Open Project")) {

            if (project == "") {

                JFileChooser choose_file = new JFileChooser("f:");
                choose_file.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                choose_file.setPreferredSize(new Dimension(900,700));
                int r = choose_file.showOpenDialog(null);
                String dir = "";
                if (r == JFileChooser.APPROVE_OPTION){
                    File file = new File(choose_file.getSelectedFile().getAbsolutePath());

                    dir = file.getAbsolutePath();
                    
                    projectText = new ArrayList<String> ();
                    
                    projectText = storeFilesForFolder(file, projectText);
                    
                    keyWordsProjectHelper(projectText);
                    
                    // We have to interpret each string s in the
                    
                    panel.updateList(file);

                    JOptionPane.showMessageDialog(frame, "Currently in Project Directory: " + dir);

                    project = dir;
                    Opened_File = false;
                    text.setEditable(true);
                }
                else
                    JOptionPane.showMessageDialog(frame, "No File Is Opened");
            }
            else {
                JOptionPane.showMessageDialog(frame, "Cannot open project.  Please close current project");
            }
        }
        else if (s.equals("Save Project")) {
            ArrayList<ExtendWin> exWin = new ArrayList<ExtendWin>();
            exWin = panel.getHash();
            if (exWin.size() > 0){
                for (int i = 0; i < exWin.size(); i++){
                    exWin.get(i).SaveFile();
                }
                JOptionPane.showMessageDialog(frame, "All Project Files Saved");
            }
        }
        else if (s.equals("New Project")) {

            if (project == "") {

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
                        Opened_File = false;
                        panel.updateList(file);
                    }
                    catch (Exception evt){
                        JOptionPane.showMessageDialog(frame, evt.getMessage()); 
                    }   
                    text.setEditable(true);
                    project = dir;
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
                    text.setEditable(true);
                    scroll_bar.setViewportView(text);
                    direct = file.toString();
                    curFile = file;
                    Opened_File = true;
                    File curDir = new File(file.getParent());

                    panel.updateList(curDir);

                    curFile = file;
                    
                }   
                catch (Exception evt){
                    //JOptionPane.showMessageDialog(frame, evt.getMessage());
                }

                updateDocument();
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

                    direct = file.toString();

                    curFile = file;
                }
                catch (Exception evt){
                    //JOptionPane.showMessageDialog(frame, evt.getMessage()); 
                }
                
                updateDocument();
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

                    direct = file.toString();

                    curFile = file;
                }
                catch (Exception evt){
                    //JOptionPane.showMessageDialog(frame, evt.getMessage()); 
                }

                updateDocument();
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
                    File curDir = new File(file.getParent());

                    panel.updateList(curDir);

                    direct = file.toString();

                    curFile = file;
                    Opened_File = true;
                    text.setText("");     
                    text.setEditable(true);
                    scroll_bar.setViewportView(text);
                }
                catch (Exception evt){
                    //JOptionPane.showMessageDialog(frame, evt.getMessage()); 
                }

                updateDocument();
            }
            else
                JOptionPane.showMessageDialog(frame, "Unable to Create New File");
            
        }
        else if (s.equals("Debug Project")){
           
            try{
                if(project != "") {
                    String file_dir = project;
                    String command = String.format("cmd /c start cmd.exe /K \"pushd %s && javac *.java\"", file_dir);
                    Runtime.getRuntime().exec(command);
                    JOptionPane.showMessageDialog(frame, "All Files in Project Folder Debugged");
                }
                
            }
            catch (Exception evt){ 
                evt.printStackTrace(); 
            } 
            
        }
        else if (s.equals("Debug File")){
            try{
                if (Opened_File == true){
                    System.out.println(direct);
                    int file_i = direct.lastIndexOf("\\") + 1;
                    String file_name = direct.substring(file_i);
                    String file_dir = direct.substring(0, file_i);
                    String command = String.format("cmd /c start cmd.exe /K \"pushd %s && javac %s\"", file_dir, file_name);
                    Runtime.getRuntime().exec(command);
                    String outInfo = "File " + file_name + " Debugged";
                    JOptionPane.showMessageDialog(frame, outInfo);
                }
                
            }
            catch (Exception evt){ 
                evt.printStackTrace(); 
            } 
        }
        else if (s.equals("Run")){
            try{
                if (Opened_File == true){
                    int file_ie = direct.indexOf(".java");
                    int file_i = direct.lastIndexOf("\\") + 1;
                    String file_name = direct.substring(file_i, file_ie);
                    String file_dir = direct.substring(0, file_i);
                    String command = String.format("cmd /c start cmd.exe /K \"pushd %s && java %s\"", file_dir, file_name);
                    Runtime.getRuntime().exec(command);
                }
            }
            catch (Exception evt){ 
                evt.printStackTrace(); 
            } 
        }
        else if (s.equals("Keywords stats"))
        {
        	if (sf == null)
        		{sf = new statsFrame(keywordsCount, keywords);}
        	else if (sf != null)
        		{sf.updateFrame();}
        }
        else if (s.equals("Keywords stats - Project"))
        {
        	if (sf_Project == null)
        	{
        		System.out.println("Hello");
        		sf_Project = new statsFrame(keywordsCountProject, keywords);
        	}
        	else if (sf != null) 
        	{sf_Project.updateFrame();}
        }
        else if (s.equals("Find")){
            System.out.println("You are in find");
            updateDocument();

            newFind = new Find(this);
            //newFind.getPreferredSize();
            //findPanel = new JPanel();
            //findPanel.add(newFind);
            //findPanel.getContentPane().add(newFind);
            //frame.add(findPanel,BorderLayout.NORTH);
            //frame.setVisible(true);
        }
    }

    public editor2 getCurrentEditorObj()
    {
        return this;
    }

    //This function updates the document the "Find" feature is searching
    public void updateDocument()
    {
        try
        {
            d = text.getDocument();
            System.out.println("Document");
            length = d.getLength();
            System.out.println(length);
            allText = d.getText(0, length);
            System.out.println(allText);
        }
        catch (BadLocationException ble)
        {
            JOptionPane.showMessageDialog(frame, "Unable to obtain file content");
        }
    }

    //This function is for the "Find" feature.  It highlights the word searched for
    //by the user
    public void highlightWord(String s)
    {
        System.out.println("You are in highlightWord");
        System.out.println(s);
        System.out.println(allText);
        System.out.println(length);
        //String word = "";
        //word = text.getDocument().getLength();
        //System.out.println(word);

        int index1 = 0;
        int index2 = 0;
        
        index1 = allText.indexOf(s);
        System.out.println(index1);
        index2 = s.length();
        System.out.println(index2);

        Highlighter.HighlightPainter painter = new DefaultHighlighter.DefaultHighlightPainter(Color.yellow);
        System.out.println("Highlighter");

        if (index1 == -1)
        {
            JOptionPane.showMessageDialog(frame, "Unable to find character or string. Please save file before using \"Find\".");
        }
        else 
        {
            text.getHighlighter().removeAllHighlights();
            
            while(index1 != -1)
            {
                try
                {
                    text.getHighlighter().addHighlight(index1, index1 + index2, painter);
                    index1 = allText.indexOf(s, index1+1);
                    // try {
                    //     TimeUnit.SECONDS.sleep(2);
                    // } catch (InterruptedException e) {
                    //     e.printStackTrace();
                    // }
                }
                catch (BadLocationException ble)
                {
                    JOptionPane.showMessageDialog(frame, "Unable to highlight word");
                }
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
        	
        	temp = temp.replace("\n", " ").replace("\r", "");
        	
        	
        	String [] tempStringArray = temp.split("\\s+");
        	char [] tempCharArray = temp.toCharArray();
        	
        	// Pass the string array into the reserved word finder
        	lookforKeywords(tempStringArray);
        	
        	// Look for matching substrings and highlight them
        	Matcher matcherBool = patternBool.matcher(temp);
        	Matcher matcherQuotes = patternQuotes.matcher(temp);
        	
        	while (matcherBool.find())
        		{
        		styledDocument.setCharacterAttributes(matcherBool.start(), matcherBool.end() - matcherBool.start(), booleanAttributeSet, false);
        		
        		System.out.println("String is : " + temp.substring(matcherBool.start(), matcherBool.end()));
        		
        		System.out.println("Matcher Boolean start location : " + matcherBool.start());
        		System.out.println("Matcher Boolean end location : " + matcherBool.end());
        		}
        	
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
        		
        		
        		
        	}
        	
        }
        
        private void lookforKeywords(String [] text)
        {
        	// Helper array because we read in the text each time a character is added/deleted from the text
        	int [] helpArray = new int [keywords.length];
        	Arrays.fill(helpArray, 0);
        	
        	// Iterate through the string array and check with static keywords array through binary Search
        	for (String word : text)
        	{
        		int index = Arrays.binarySearch(keywords, word);
        		if (index >= 0) 
        		{
        			helpArray[index] += 1;
        		}
        		
        	}
        	
        	// Use library to compare Arrays - if we detect a change then we set static bool to true, else it is false
        	if (Arrays.equals(keywordsCount, helpArray) == true){change = false;}
        	else
        	{
        		System.out.println("There was a change");
        		change = true;
        	}
        	
        	keywordsCount = helpArray;
        	
        	// If we detect a change then we should update the existing keywords count JFrame
        	if (change == true && sf != null)
        	{
        		sf.update(keywordsCount, keywords);
        	}
        	
        }
    }
}


  