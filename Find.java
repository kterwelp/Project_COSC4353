//package editorP;

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
import java.lang.NullPointerException;

class Find extends JFrame implements ActionListener {

    JFrame frame;
    JButton button;
    JTextField text;
    editor2 editor;

    Find(editor2 e) {
        frame = new JFrame("Find");
        frame.setSize(400, 400);
        frame.getContentPane().setLayout(new FlowLayout());
    
        button = new JButton("Find");
        text = new JTextField(10);

        frame.add(text);
        frame.add(button);

        button.addActionListener(this);

        frame.pack();
        frame.setVisible(true);

        editor = e;
    }

    public void actionPerformed(ActionEvent evt) 
    { 
        String s = evt.getActionCommand(); 
        if (s.equals("Find")) {

            editor = editor.getCurrentEditorObj();

            if (text.getText() == null)
            {
                JOptionPane.showMessageDialog(frame, "Please enter a character");
            }
            else
            {
                String input = text.getText();
                System.out.println(input);
                editor.highlightWord(input);
            }
        }

    }

    public Dimension getPreferredSize(){
        return new Dimension(50, 320);
      }
    
}