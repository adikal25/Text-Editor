package texteditor;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.ComponentOrientation;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.awt.GridLayout;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetEvent;
import java.awt.dnd.DropTargetListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowEvent;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.AbstractAction;
import javax.swing.Action;
import static javax.swing.Action.MNEMONIC_KEY;
import static javax.swing.Action.SHORT_DESCRIPTION;
import javax.swing.GroupLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import static javax.swing.JFrame.EXIT_ON_CLOSE;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextPane;
import javax.swing.JToolBar;
import javax.swing.KeyStroke;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultEditorKit;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import javax.swing.text.StyledEditorKit;
import shape.Sketchpad;

public class GUI_Frame extends JFrame implements ActionListener {

    
    public JTextPane tp;
    private final JMenuBar menuBar;
    private final JComboBox<String> fontType;
    private final JComboBox<Integer> fontSize;
    private final JMenu fileMenu, editMenu, menuFind, reviewMenu, helpMenu;
    private final JMenuItem newFile, openFile, saveFile, close, cut, copy, paste, clearFile, selectAll, quickFind;
    private final JToolBar mainToolbar;
    JButton newButton, openButton, saveButton, clearButton,drawSketchpad, quickButton, closeButton, countButton, boldButton, italicButton, underlineButton, strikeButton, lButton, rButton, cButton, jButton;

    JLabel resultData = new JLabel();
    JLabel totalCharacter = new JLabel();

    private final Action selectAllAction;
    private final String[] myFileExtension = {".txt"};

  
  
    private boolean edit = false;
    public StyledDocument doc;
    public Font[] allf = GraphicsEnvironment.getLocalGraphicsEnvironment().getAllFonts();
    public Font initf = new Font("Courier New", 0, 17);
    private Style style;
    int xx = 0;

    public GUI_Frame() {

        setSize(800, 500);
        setTitle("New | " + TextEditor.NAME);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
       
        
        tp = new JTextPane();
        tp.setBackground(new Color(225,225,225));
        tp.setForeground(Color.black);
        tp.setCaretColor(Color.black);
        doc = tp.getStyledDocument();
        tp.setFont(initf);
        tp.setDragEnabled(true);
       // tp.setTransferHandler(new StyleTransferHandler());
        setLayout(new BorderLayout());

        JScrollPane scrollPane = new JScrollPane(tp);
        

        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        getContentPane().setLayout(new BorderLayout()); // the BorderLayout bit makes it fill it automatically

        getContentPane().add(scrollPane);

        fileMenu = new JMenu("File");
        editMenu = new JMenu("Edit");
        menuFind = new JMenu("Search");
        reviewMenu = new JMenu("Review");
        helpMenu = new JMenu("Help");

        newFile = new JMenuItem("New");
        openFile = new JMenuItem("Open");
        saveFile = new JMenuItem("Save");
        close = new JMenuItem("Quit");
        clearFile = new JMenuItem("Clear");
        quickFind = new JMenuItem("Quick");
       

        menuBar = new JMenuBar();
        menuBar.add(fileMenu);
        menuBar.add(editMenu);
        menuBar.add(menuFind);
        menuBar.add(reviewMenu);
        menuBar.add(helpMenu);
        this.setJMenuBar(menuBar);
        selectAllAction = new SelectAllAction("Select All", "Select all text", new Integer(KeyEvent.VK_A),
                tp);

        this.setJMenuBar(menuBar);

        newFile.addActionListener(this);
        newFile.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, InputEvent.CTRL_MASK));
        fileMenu.add(newFile);

        openFile.addActionListener(this);
        openFile.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, InputEvent.CTRL_MASK));
        fileMenu.add(openFile);

        saveFile.addActionListener(this);
        saveFile.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_MASK));
        fileMenu.add(saveFile);

        close.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Q, InputEvent.CTRL_MASK));
        close.addActionListener(this);
        fileMenu.add(close);

        selectAll = new JMenuItem(selectAllAction);
        selectAll.setText("Select All");

        selectAll.setToolTipText("Select All");
        selectAll.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_A, InputEvent.CTRL_MASK));
        editMenu.add(selectAll);

        clearFile.addActionListener(this);
        clearFile.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_K, InputEvent.CTRL_MASK));
        editMenu.add(clearFile);

        cut = new JMenuItem(new DefaultEditorKit.CutAction());
        cut.setText("Cut");

        cut.setToolTipText("Cut");
        cut.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_X, InputEvent.CTRL_MASK));
        editMenu.add(cut);

        copy = new JMenuItem(new DefaultEditorKit.CopyAction());
        copy.setText("Copy");
        copy.setToolTipText("Copy");
        copy.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C, InputEvent.CTRL_MASK));
        editMenu.add(copy);

        paste = new JMenuItem(new DefaultEditorKit.PasteAction());
        paste.setText("Paste");
        paste.setToolTipText("Paste");
        paste.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_V, InputEvent.CTRL_MASK));
        editMenu.add(paste);

        quickFind.addActionListener(this);
        quickFind.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F, InputEvent.CTRL_MASK));
        
        
         menuFind.add(quickFind);
      

        mainToolbar = new JToolBar();
        this.add(mainToolbar, BorderLayout.NORTH);
        
        

        boldButton = new JButton(new StyledEditorKit.BoldAction());
        boldButton.setText("Bold");
        boldButton.setToolTipText("Bold");

        mainToolbar.add(boldButton);
        mainToolbar.addSeparator();

        italicButton = new JButton(new StyledEditorKit.ItalicAction());
        italicButton.setToolTipText("Italic");
         italicButton.setText("Italic");

        mainToolbar.add(italicButton);
        mainToolbar.addSeparator();

        underlineButton = new JButton(new StyledEditorKit.UnderlineAction());
        underlineButton.setToolTipText("Underline");
        underlineButton.setText("Underline");

        mainToolbar.add(underlineButton);
        mainToolbar.addSeparator();
        

        strikeButton = new JButton("strike");
        strikeButton.setToolTipText("strike");
        strikeButton.addActionListener(this);
         drawSketchpad = new JButton("Draw SkitchPad");
        drawSketchpad.setToolTipText("SkitchPad");
        drawSketchpad.addActionListener(this);
        
        countButton = new JButton("Word&Char Count");
        countButton.setToolTipText("Word&Char Count");
        countButton.addActionListener(this);
        mainToolbar.add(strikeButton);
        mainToolbar.addSeparator();
        mainToolbar.add(countButton);
        mainToolbar.addSeparator();

        lButton = new JButton(new StyledEditorKit.AlignmentAction("Left", xx));
        lButton.setToolTipText("Left");

        mainToolbar.add(lButton);
        mainToolbar.addSeparator();

        rButton = new JButton(new StyledEditorKit.AlignmentAction("Right", 2));
        rButton.setToolTipText("Right");

        mainToolbar.add(rButton);
        mainToolbar.addSeparator();

        cButton = new JButton(new StyledEditorKit.AlignmentAction("Center", 1));
        cButton.setToolTipText("Center");

        mainToolbar.add(cButton);
        mainToolbar.addSeparator();

        jButton = new JButton(new StyledEditorKit.AlignmentAction("Justify", StyleConstants.ALIGN_JUSTIFIED));
        jButton.setToolTipText("Justify");

        mainToolbar.add(jButton);
        mainToolbar.addSeparator();

        fontType = new JComboBox<String>();

        String[] fonts = GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames();

        for (String font : fonts) {
            //Adding font family names to font[] array
            fontType.addItem(font);
        }
        //Setting maximize size of the fontType ComboBox
        fontType.setMaximumSize(new Dimension(170, 30));
        fontType.setToolTipText("Font Type");
        mainToolbar.add(fontType);
        
        mainToolbar.addSeparator();
        

        //Adding Action Listener on fontType JComboBox
        fontType.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ev) {
                //Getting the selected fontType value from ComboBox
                String p = fontType.getSelectedItem().toString();
                int start = tp.getSelectionStart();
                int end = tp.getSelectionEnd();
                String newfont = p;
                Style style = doc.addStyle("font", null);
                StyleConstants.setFontFamily(style, newfont);
                doc.setCharacterAttributes(start, end - start, style, false);

            }
        });

        fontSize = new JComboBox<Integer>();

        for (int i = 5; i <= 100; i++) {
            fontSize.addItem(i);
        }
        fontSize.setMaximumSize(new Dimension(70, 30));
        fontSize.setToolTipText("Font Size");
        mainToolbar.add(fontSize);
          mainToolbar.addSeparator();
     mainToolbar.add(drawSketchpad);
      
        fontSize.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ev) {
                String sizeValue = fontSize.getSelectedItem().toString();
                int sizeOfFont = Integer.parseInt(sizeValue);
                int start = tp.getSelectionStart();
                int end = tp.getSelectionEnd();
                int fontsize = sizeOfFont;
                Style style = doc.addStyle("size", null);
                StyleConstants.setFontSize(style, fontsize);
                doc.setCharacterAttributes(start, end - start, style, false);

            }
        });

    }

    @Override
    protected void processWindowEvent(WindowEvent e) {
        if (e.getID() == WindowEvent.WINDOW_CLOSING) {
            if (edit) {
                Object[] options = {"Save and exit", "No Save and exit", "Return"};
                int n = JOptionPane.showOptionDialog(this, "Do you want to save the file ?", "Question",
                        JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[1]);
                if (n == 0) {// save and exit
                    saveFile();
                    this.dispose();
                } else if (n == 1) {
                    this.dispose();
                }
            } else {
                System.exit(99);
            }
        }
    }

   

   
    public void actionPerformed(ActionEvent e) {
        // If the source of the event was our "close" option
        if (e.getSource() == close || e.getSource() == closeButton) {
            if (edit) {
                Object[] options = {"Save and exit", "No Save and exit", "Return"};
                int n = JOptionPane.showOptionDialog(this, "Do you want to save the file ?", "Question",
                        JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[2]);
                if (n == 0) {// save and exit
                    saveFile();
                    this.dispose();// dispose all resources and close the application
                } else if (n == 1) {// no save and exit
                    this.dispose();// dispose all resources and close the application
                }
            } else {
                this.dispose();// dispose all resources and close the application
            }
        } // If the source was the "new" file option
        else if (e.getSource() == newFile || e.getSource() == newButton) {
            if (edit) {
                Object[] options = {"Save", "No Save", "Return"};
                int n = JOptionPane.showOptionDialog(this, "Do you want to save the file at first ?", "Question",
                        JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[2]);
                if (n == 0) {// save
                    saveFile();
                    edit = false;
                } else if (n == 1) {
                    edit = false;
                    tp.setText("");
                }
            } else {
                tp.setText("");
            }

        } // If the source was the "open" option
        else if (e.getSource() == openFile || e.getSource() == openButton) {
            JFileChooser open = new JFileChooser(); // open up a file chooser (a dialog for the user to  browse files to open)
            
            // if true does normal operation
            JFileChooser chooser = new JFileChooser();
            FileNameExtensionFilter filter = new FileNameExtensionFilter("TEXT/JAVA FILES", "txt", "text", "java");
            chooser.setFileFilter(filter);
            if (chooser.showOpenDialog(GUI_Frame.this)
                    != JFileChooser.APPROVE_OPTION) {
                return;
            }
            File file = chooser.getSelectedFile();
            if (file == null) {
                return;
            }

            FileReader reader = null;
            try {
                setTitle(file.getName());
                reader = new FileReader(file);
                tp.read(reader, null);
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(GUI_Frame.this,
                        "File Not Found", "ERROR", JOptionPane.ERROR_MESSAGE);
            } finally {
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (IOException x) {
                    }
                }
            }

        } // If the source of the event was the "save" option
        else if (e.getSource() == saveFile || e.getSource() == saveButton) {
            saveFile();
        }// If the source of the event was the "Bold" button
         else if (e.getSource() == drawSketchpad) {
             Sketchpad gui = new Sketchpad(); 
             gui.frame.setVisible(true);
             gui.frame.setSize(600,600);
         }
        
         else if (e.getSource() == countButton) {
             String data=tp.getSelectedText();
             if(data.length()>0){
             int wordCount=data.split(" ").length;
             int charCount=data.length();
             String wordCountMessage="Total Word Count : "+wordCount+"\n Character Count : "+charCount;
             System.out.println(wordCountMessage);
              JOptionPane.showMessageDialog(GUI_Frame.this,
                        wordCountMessage, "Word and Char Count", JOptionPane.DEFAULT_OPTION);
             }
             else{
                  JOptionPane.showMessageDialog(GUI_Frame.this,
                        "please select word", "Word and Char Count", JOptionPane.ERROR_MESSAGE);
             }
         }
        
        else if (e.getSource() == strikeButton) {

            int start = tp.getSelectionStart();
            int end = tp.getSelectionEnd();
            style = tp.addStyle("Format", null);
            StyleConstants.setStrikeThrough(style, true);

            doc.setCharacterAttributes(start, end - start, style, false);

        } else if (e.getSource() == lButton) {
            doc = tp.getStyledDocument();
            style = tp.addStyle(tp.getText(), null);
            StyleConstants.setAlignment(style, 1);
            try {
                doc.insertString(WIDTH, tp.getText(), style);
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        }

        // Clear File (Code)
        if (e.getSource() == clearFile || e.getSource() == clearButton) {

            Object[] options = {"Yes", "No"};
            int n = JOptionPane.showOptionDialog(this, "Are you sure to clear the text Area ?", "Question",
                    JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[1]);
            if (n == 0) {// clear
                tp.setText("");
            }
        }
        // Find
        if (e.getSource() == quickFind || e.getSource() == quickButton) {
            new Find(tp);
        } // About Me

    }

    class SelectAllAction extends AbstractAction {

        /**
         * Used for Select All function
         */
        public SelectAllAction(String text,  String desc, Integer mnemonic, JTextPane tp) {
            super(text);
            putValue(SHORT_DESCRIPTION, desc);
            putValue(MNEMONIC_KEY, mnemonic);
        }

        public void actionPerformed(ActionEvent e) {
            tp.selectAll();
        }
    }

    private void saveFile() {
        // Open a file chooser
        JFileChooser chooser = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter("TEXT", "txt", "text", ".txt");
        chooser.setFileFilter(filter);
        FileNameExtensionFilter filter2 = new FileNameExtensionFilter("JAVA", "java", ".java");
        chooser.setFileFilter(filter2);
        if (chooser.showSaveDialog(GUI_Frame.this)
                != JFileChooser.APPROVE_OPTION) {
            return;
        }
        File file = chooser.getSelectedFile();
        if (file == null) {
            return;
        }
        FileWriter writer = null;
        try {
            if (chooser.getFileFilter() == filter) {
                writer = new FileWriter(file + ".txt");
                tp.write(writer);
                setTitle(file.getName() + ".txt");
            } else if (chooser.getFileFilter() == filter2) {
                writer = new FileWriter(file + ".java");
                tp.write(writer);
                setTitle(file.getName() + ".java");
            }
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(GUI_Frame.this,
                    "File Not Saved", "ERROR", JOptionPane.ERROR_MESSAGE);
        } finally {
            if (writer != null) {
                try {
                    writer.close();
                } catch (IOException x) {
                }
            }
        }

    }
    DropTargetListener dropTargetListener = new DropTargetListener() {

        @Override
        public void dragEnter(DropTargetDragEvent e) {
        }

        @Override
        public void dragExit(DropTargetEvent e) {
        }

        @Override
        public void dragOver(DropTargetDragEvent e) {
        }

        @Override
        public void drop(DropTargetDropEvent e) {
            if (edit) {
                Object[] options = {"Save", "No Save", "Return"};
                int n = JOptionPane.showOptionDialog(GUI_Frame.this, "Do you want to save the file at first ?", "Question",
                        JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[2]);
                if (n == 0) {// save
                    GUI_Frame.this.saveFile();
                    edit = false;
                } else if (n == 1) {
                    edit = false;
                    tp.setText("");
                } else if (n == 2) {
                    e.rejectDrop();
                    return;
                }
            }
            try {
                Transferable tr = e.getTransferable();
                DataFlavor[] flavors = tr.getTransferDataFlavors();
                for (DataFlavor flavor : flavors) {
                    if (flavor.isFlavorJavaFileListType()) {
                        e.acceptDrop(e.getDropAction());

                        try {
                            String fileName = tr.getTransferData(flavor).toString().replace("[", "").replace("]", "");

                            // Allowed file filter extentions for drag and drop
                            boolean extensionAllowed = false;
                            for (String s : myFileExtension) {
                                if (fileName.endsWith(s)) {
                                    extensionAllowed = true;
                                    break;
                                }
                            }
                            if (!extensionAllowed) {
                                JOptionPane.showMessageDialog(GUI_Frame.this, "This file is not allowed for drag & drop", "Error", JOptionPane.ERROR_MESSAGE);

                            } else {
                                FileInputStream fis = new FileInputStream(new File(fileName));
                                byte[] ba = new byte[fis.available()];
                                fis.read(ba);
                                
                                fis.close();
                            }
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                        e.dropComplete(true);
                        return;
                    }
                }
            } catch (Throwable t) {
                t.printStackTrace();
            }
            e.rejectDrop();
        }

        @Override
        public void dropActionChanged(DropTargetDragEvent e) {
        }
    };

}
