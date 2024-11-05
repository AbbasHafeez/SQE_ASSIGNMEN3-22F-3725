//package PL;
//
//import java.awt.BorderLayout;
//import java.awt.Color;
//import java.awt.Dimension;
//import java.awt.FlowLayout;
//import java.awt.Font;
//import java.awt.event.ActionEvent;
//import java.awt.event.ActionListener;
//import java.awt.event.KeyAdapter;
//import java.awt.event.KeyEvent;
//import java.awt.event.KeyListener;
//import java.awt.event.MouseAdapter;
//import java.awt.event.MouseEvent;
//import java.io.File;
//import java.io.IOException;
//import java.security.NoSuchAlgorithmException;
//import java.sql.SQLException;
//import java.util.List;
//
//import javax.swing.BorderFactory;
//import javax.swing.JButton;
//import javax.swing.JComboBox;
//import javax.swing.JDialog;
//import javax.swing.JFileChooser;
//import javax.swing.JFrame;
//import javax.swing.JLabel;
//import javax.swing.JOptionPane;
//import javax.swing.JPanel;
//import javax.swing.JScrollPane;
//import javax.swing.JTable;
//import javax.swing.JTextArea;
//import javax.swing.JTextField;
//import javax.swing.event.DocumentEvent;
//import javax.swing.event.DocumentListener;
//import javax.swing.table.DefaultTableModel;
//
//import BL.B_LOGIC;
//import BL.Facade;
//import DAL.AbstractDAOFactory;
//import DAL.DALFacade;
//import DAL.IDALFacade;
//import DAL.IDAO;
//import DAL.MySQLDAO;
//
//public class Presentation extends JFrame implements ActionListener {
//    private B_LOGIC BLO;
//    
//    int start = 0;
//    int end = 0;
//
//    int currentPage = 0;
//    
//    String[] lines;
//    private static final long serialVersionUID = 1L;
//    private JButton importButton, save, create, update, delete, view, searchButton;
//    private JTextArea text;  
//    private JTextField searchBar;
//    private JComboBox<String> font, suggestionsBox;
//    Integer[] fontsize = {12, 16, 20};
//    private JComboBox<Integer> size;
//    String[] fontStyles = {"Arial", "Times New Roman", "Courier New"};
//    private boolean isContentSaved;
//    private String fileContent;
//    private String fileName;
//    private JFileChooser fileChooser;
//    private JTextArea inputText;
//
//	private IDALFacade dalFacade;
//    
//    public Presentation() {
//    	IDAO dao = AbstractDAOFactory.getInstance().createDAO();
//    	dalFacade = new DALFacade(dao);
//        BLO = new B_LOGIC(dalFacade);
//
//
//        setTitle("ArabicTextEditor");
//        setSize(800, 500);
//        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//
//        text = new JTextArea();  
//        importButton = new JButton("Import");
//        save = new JButton("Save");
//        create = new JButton("Create");
//        update = new JButton("Update");
//        delete = new JButton("Delete");
//        view = new JButton("View");
//        searchButton = new JButton("Search");
//
//      
//        font = new JComboBox<>(fontStyles);
//        size = new JComboBox<>(fontsize);
//        searchBar = new JTextField(15); 
//        inputText = new JTextArea();
//        suggestionsBox = new JComboBox<>();
//        JScrollPane pane = new JScrollPane(inputText);
//        
//        JScrollPane scrollPane = new JScrollPane(text);  
//        JPanel topPanel = new JPanel();
//        topPanel.setLayout(new FlowLayout());
//        topPanel.add(new JLabel("Font:"));
//        topPanel.add(font);
//        topPanel.add(new JLabel("Font Size:"));
//        topPanel.add(size);
//
//        
//        topPanel.add(new JLabel("Search:"));
//        topPanel.add(searchBar);
//        topPanel.add(suggestionsBox);
//        topPanel.add(searchButton);
//        topPanel.add(save);
//        topPanel.add(delete);
//        topPanel.add(importButton);
//        topPanel.add(update);
//        topPanel.add(create);
//        topPanel.add(view);
//
//        add(topPanel, BorderLayout.NORTH);
//        add(scrollPane, BorderLayout.CENTER);  
//        add(pane, BorderLayout.CENTER); 
//		
//        save.addActionListener(this);
//        delete.addActionListener(this);
//        create.addActionListener(this);
//        update.addActionListener(this);
//        view.addActionListener(this);
//        searchButton.addActionListener(this);
//
//        setVisible(true);
//
//        searchBar.getDocument().addDocumentListener(new DocumentListener() {
//            public void insertUpdate1(DocumentEvent e) {
//                updateSuggestions(searchBar.getText());
//            }
//
//            public void removeUpdate1(DocumentEvent e) {
//                updateSuggestions(searchBar.getText());
//            }
//
//            public void changedUpdate1(DocumentEvent e) {
//                updateSuggestions(searchBar.getText());
//            }
//
//			@Override
//			public void insertUpdate(DocumentEvent e) {
//				// TODO Auto-generated method stub
//				
//			}
//
//			@Override
//			public void removeUpdate(DocumentEvent e) {
//				// TODO Auto-generated method stub
//				
//			}
//
//			@Override
//			public void changedUpdate(DocumentEvent e) {
//				// TODO Auto-generated method stub
//				
//			}
//        });
//        
//        save.addActionListener(new ActionListener() {
//           
//        	@Override
//			public void actionPerformed(ActionEvent e) {
//				if (isContentSaved) {
//                    JOptionPane.showMessageDialog(null, "Content has already been saved to the database.");
//                    return;
//                }
//
//                try {
//                    String result = BLO.saveImportedFileToDatabase(fileContent, fileName);
//                    JOptionPane.showMessageDialog(null, result);
//                    isContentSaved = true;
//                    text.setText("");
//                } catch (NoSuchAlgorithmException e1) {
//                    e1.printStackTrace();
//                }
//			}
//        });
//
//        suggestionsBox.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                // Only update searchBar when a suggestion is actively selected
//                if (suggestionsBox.getSelectedItem() != null && suggestionsBox.isPopupVisible()) {
//                    String selectedFile = suggestionsBox.getSelectedItem().toString();
//                    searchBar.setText(selectedFile); // Set the selected suggestion in the search bar
//                    loadFileContent(selectedFile); // Load content for selected file
//                }
//            }
//        });
//        
//        fileChooser = new JFileChooser();
//        fileChooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("Text Files", "txt"));
//
//        //view button 
//        view.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                viewFilesWindow(); 
//            }
//        });
//        
//        
//        importButton.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                int returnValue = fileChooser.showOpenDialog(null);
//                if (returnValue == JFileChooser.APPROVE_OPTION) {
//                    File selectedFile = fileChooser.getSelectedFile();
//                    try {
//                        fileContent = BLO.importTextFile(selectedFile);
//                        if (fileContent.startsWith("Invalid")) {
//                            JOptionPane.showMessageDialog(null, fileContent);
//                        } else {
//                            // Create a custom JDialog
//                            JDialog dialog = new JDialog();
//                            dialog.setTitle("Imported File Content");
//                            dialog.setSize(600, 400);
//                            dialog.setLocationRelativeTo(null); // Center the dialog
//
//                            // Create a JTextArea to display the content
//                            JTextArea textArea = new JTextArea();
//                            textArea.setText(fileContent);
//                            textArea.setEditable(false); // Make it non-editable
//                            textArea.setBackground(Color.WHITE); // Set background color to white
//                            textArea.setLineWrap(true);
//                            textArea.setWrapStyleWord(true); // Enable word wrapping
//
//                            // Add the JTextArea to a JScrollPane
//                            JScrollPane scrollPane = new JScrollPane(textArea);
//                            dialog.add(scrollPane, BorderLayout.CENTER);
//
//                            // Add a close button
//                            JButton closeButton = new JButton("Close");
//                            closeButton.setPreferredSize(new Dimension(80, 30)); // Set smaller size for close button
//                            closeButton.setBackground(Color.BLUE); // Set background color to blue
//                            closeButton.setForeground(Color.WHITE); // Set text color to white for better visibility
//                            closeButton.addActionListener(new ActionListener() {
//                                @Override
//                                public void actionPerformed(ActionEvent e) {
//                                    dialog.dispose(); // Close the dialog
//                                }
//                            });
//                            dialog.add(closeButton, BorderLayout.SOUTH);
//
//                            // Make the dialog visible
//                            dialog.setVisible(true);
//
//                            fileName = selectedFile.getName();
//                            isContentSaved = false;
//                            // Optional: Clear the JTextArea or keep it for further editing
//                            // text.setText(""); // Uncomment if you want to clear the text area
//                        }
//                    } catch (IOException e1) {
//                        JOptionPane.showMessageDialog(null, "Error reading the file: " + e1.getMessage());
//                        e1.printStackTrace();
//                    }
//                }
//            }
//        });
//
//        
//        create.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//            	 String fileName = JOptionPane.showInputDialog(null, "Enter file name:", "Save File", JOptionPane.PLAIN_MESSAGE);
//                 if (fileName != null && !fileName.trim().isEmpty()) {
//                     try {
//                         String message = BLO.createFile(fileName, inputText.getText());
//                         JOptionPane.showMessageDialog(null, message, "Information", JOptionPane.INFORMATION_MESSAGE);
//                     } catch (SQLException e1) {
//                         JOptionPane.showMessageDialog(null, "Database error: " + e1.getMessage());
//                         e1.printStackTrace();
//                     }
//                 }
//            }
//        });
//        
//        
//        delete.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//            	String fileName = JOptionPane.showInputDialog(null, "Enter file name:", "Delete File", JOptionPane.PLAIN_MESSAGE);
//                if (fileName != null && !fileName.trim().isEmpty()) {
//                    String message = BLO.deleteFile(fileName);
//                    JOptionPane.showMessageDialog(null, message, "Information", JOptionPane.INFORMATION_MESSAGE);
//                } 
//            }
//        });
//        
//        searchBar.addKeyListener((KeyListener) new KeyAdapter() {
//            @Override
//            public void keyReleased(KeyEvent e) {
//                String searchTerm = searchBar.getText();
//                if (!searchTerm.isEmpty()) {
//                    updateSuggestions(searchTerm); // Update suggestions based on input
//                }
//            }
//        });
//        
//        
//        font.addActionListener(e -> updateTextAreaFont());
//	     size.addActionListener(e -> updateTextAreaFont());
//
//
//	        setVisible(true);
//
//    }
//    
//    
//    protected void loadFileContent(String selectedFile) {
//    	String content = this.dalFacade.getFileContent(selectedFile);
//        text.setText(content != null ? content : "No content found for the selected file.");
//
//		
//	}
//
//
//	protected void updateSuggestions(String text2) {
//		List<String> suggestions = dalFacade.getFileSuggestions(text2);
//        suggestionsBox.removeAllItems(); // Clear previous suggestions
//        for (String suggestion : suggestions) {
//            suggestionsBox.addItem(suggestion); // Add new suggestions
//        }
//        System.out.println("Suggestions found: " + suggestions);
//		
//	}
//
//
//	private void updateTextAreaFont() {
//	    String selectedFont = (String) font.getSelectedItem();
//	    Integer selectedSize = (Integer) size.getSelectedItem();
//	    if (selectedFont != null && selectedSize != null) {
//	        inputText.setFont(new Font(selectedFont, Font.PLAIN, selectedSize));
//	    }
//   }
//
//    private void searchInTextArea(String searchTerm) {
//        String content = text.getText();
//        int index = content.indexOf(searchTerm);
//
//        if (index != -1) {
//            text.requestFocus();
//            text.select(index, index + searchTerm.length());
//        } else {
//            JOptionPane.showMessageDialog(this, "Search term not found");
//        }
//    }
//
//    private void updateFont(JTextArea textArea) {
//        String selectedFont = (String) font.getSelectedItem();
//        Integer selectedSize = (Integer) size.getSelectedItem();
//        if (selectedFont != null && selectedSize != null) {
//            textArea.setFont(new Font(selectedFont, Font.PLAIN, selectedSize));
//        }
//    }
//
//    private void searchInTextArea(JTextArea textArea, String searchTerm) {
//        String content = textArea.getText();
//        int index = content.indexOf(searchTerm);
//
//        if (index != -1) {
//            textArea.requestFocus();
//            textArea.select(index, index + searchTerm.length());
//            textArea.setCaretPosition(index); // Set the caret position to the start of the found term
//        } else {
//            JOptionPane.showMessageDialog(this, "Search term not found");
//        }
//    }
//
//    
//    //view
// // view presentation
//    public void viewFilesWindow(){
//        // Create a new JFrame for the view files window
//        JFrame viewFilesFrame = new JFrame("View Files");
//        viewFilesFrame.setSize(800, 600);
//        viewFilesFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        viewFilesFrame.setLocationRelativeTo(null);
//
//        // Set up colors for the UI
//        Color backgroundColor = new Color(240, 240, 240);
//        Color accentColor = new Color(33, 150, 243);
//        Color textColor = new Color(50, 50, 50);
//        Color tableHeaderColor = new Color(200, 200, 200);
//
//        // Set background color
//        viewFilesFrame.getContentPane().setBackground(backgroundColor);
//
//        // Create input field and buttons
//        JTextField fileNameField = new JTextField(20);
//        JButton searchButton = new JButton("Search File");
//        JButton showAllButton = new JButton("Show All Files");
//
//        // Set button colors and styles
//        searchButton.setBackground(accentColor);
//        searchButton.setForeground(Color.WHITE);
//        searchButton.setFocusPainted(false);
//        searchButton.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
//
//        showAllButton.setBackground(accentColor);
//        showAllButton.setForeground(Color.WHITE);
//        showAllButton.setFocusPainted(false);
//        showAllButton.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
//
//        // Set up the table model to display file details
//        DefaultTableModel tableModel = new DefaultTableModel(new Object[]{"File Name", "Last Modified"}, 0) {
//            @Override
//            public boolean isCellEditable(int row, int column) {
//                return false; // Make cells non-editable
//            }
//        };
//
//        // Create the JTable and set its model
//        JTable fileTable = new JTable(tableModel);
//        fileTable.getTableHeader().setBackground(tableHeaderColor);
//        fileTable.getTableHeader().setFont(new Font("Sans-Serif", Font.BOLD, 12));
//        fileTable.setRowHeight(25);
//        fileTable.setFont(new Font("Sans-Serif", Font.PLAIN, 12));
//        fileTable.getTableHeader().setReorderingAllowed(false);
//        JScrollPane scrollPane = new JScrollPane(fileTable);
//        scrollPane.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
//
//        // Input panel for file name and buttons
//        JPanel inputPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
//        inputPanel.setBackground(backgroundColor);
//        inputPanel.add(new JLabel("File Name:"));
//        inputPanel.add(fileNameField);
//        inputPanel.add(searchButton);
//        inputPanel.add(showAllButton);
//
//        // Label for opened file information
//        JLabel openedFileLabel = new JLabel("Opened File: None");
//        openedFileLabel.setFont(new Font("Sans-Serif", Font.PLAIN, 14));
//        openedFileLabel.setForeground(textColor);
//
//        // Header panel for the opened file label
//        JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
//        headerPanel.setBackground(backgroundColor);
//        headerPanel.add(openedFileLabel);
//
//        // Set layout and add components to the frame
//        viewFilesFrame.setLayout(new BorderLayout());
//        viewFilesFrame.add(inputPanel, BorderLayout.NORTH);
//        viewFilesFrame.add(scrollPane, BorderLayout.CENTER);
//        viewFilesFrame.add(headerPanel, BorderLayout.SOUTH);
//
//        // ActionListener for the search button
//        searchButton.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                String fileName = fileNameField.getText().trim();
//                if (fileName.isEmpty()) {
//                    JOptionPane.showMessageDialog(viewFilesFrame, "Please enter a file name to search.");
//                    return;
//                }
//                filterFiles(fileName, tableModel);
//            } 
//        });
//
//        // ActionListener for the show all button
//        showAllButton.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                refreshFileList(tableModel);
//            }
//        });
//
//        // MouseListener for selecting files in the table
//        fileTable.addMouseListener(new MouseAdapter() {
//            @Override
//            public void mouseClicked(MouseEvent e) {
//                int selectedRow = fileTable.getSelectedRow();
//                if (selectedRow != -1) {
//                    String selectedFileName = (String) tableModel.getValueAt(selectedRow, 0);
//                    openFileInNewWindow(selectedFileName, openedFileLabel);
//                }
//            }
//        });
//
//        // Populate the file list initially
//        refreshFileList(tableModel);
//
//        // Show the view files window
//        viewFilesFrame.setVisible(true);
//        
//    }
//
//    private void openFileInNewWindow(String fileName, JLabel openedFileLabel) { 
//        
//        lines = BLO.getFileDetails(fileName); 
//        if (lines != null) {
//           
//            JDialog fileDialog = new JDialog((JFrame) null, "File: " + fileName, true);
//            fileDialog.setSize(800, 600);
//            fileDialog.setLocationRelativeTo(null);
//
//            
//            JTextArea fileContentArea = new JTextArea(lines[0], 20, 40);
//            fileContentArea.setEditable(false); // Initially set as non-editable
//            JScrollPane scrollPane = new JScrollPane(fileContentArea);
//
//            
//            JButton editButton = new JButton("Edit File");
//            JButton saveButton = new JButton("Save Changes");
//            JButton closeButton = new JButton("Close");
//
//            
//            JComboBox<String> fontComboBox = new JComboBox<>(fontStyles);
//            JComboBox<Integer> fontSizeComboBox = new JComboBox<>(fontsize);
//
//            fontComboBox.setEnabled(false);
//            fontSizeComboBox.setEnabled(false);
//
//            String selectedFont = (String) fontComboBox.getSelectedItem();
//            Integer selectedSize = (Integer) fontSizeComboBox.getSelectedItem();
//            fileContentArea.setFont(new Font(selectedFont, Font.PLAIN, selectedSize));
//
//            fontComboBox.addActionListener(e -> {
//                String newFont = (String) fontComboBox.getSelectedItem();
//                int newSize = (Integer) fontSizeComboBox.getSelectedItem();
//                fileContentArea.setFont(new Font(newFont, Font.PLAIN, newSize));
//            });
//
//            fontSizeComboBox.addActionListener(e -> {
//                String newFont = (String) fontComboBox.getSelectedItem();
//                int newSize = (Integer) fontSizeComboBox.getSelectedItem();
//                fileContentArea.setFont(new Font(newFont, Font.PLAIN, newSize));
//            });
//
//            
//            saveButton.setEnabled(false);
//            searchButton.setEnabled(false);
//
//            
//            editButton.addActionListener(new ActionListener() {
//                @Override
//                public void actionPerformed(ActionEvent e) {
//                    fileContentArea.setEditable(true);
//                    saveButton.setEnabled(true);
//                    editButton.setEnabled(false);
//                    searchButton.setEnabled(true);
//                    fontComboBox.setEnabled(true);
//                    fontSizeComboBox.setEnabled(true);
//                }
//            });
//            
//     
//            searchButton.addActionListener(e -> {
//                String searchTerm = searchBar.getText().trim();
//                if (!searchTerm.isEmpty()) {
//                    searchInTextArea(fileContentArea, searchTerm); 
//                } else {
//                    JOptionPane.showMessageDialog(this, "Please enter a search term.");
//                }
//            });
//
//            saveButton.addActionListener(new ActionListener() {
//                @Override
//                public void actionPerformed(ActionEvent e) {
//                	int i = start;
//                	for(String line: fileContentArea.getText().split("\\n")){
//                		lines[i] = line;
//                	}
//                    String[] newContent;
//                    newContent = lines;
//                    String fileType = "txt"; 
//                    BLO.saveFile(fileName, newContent, fileType);
//                    fileContentArea.setEditable(false);
//                    saveButton.setEnabled(false);
//                    editButton.setEnabled(true);
//
//                    fontComboBox.setEnabled(false);
//                    fontSizeComboBox.setEnabled(false);
//
//                    JOptionPane.showMessageDialog(fileDialog, "File saved successfully.");
//                }
//            });
//
//            closeButton.addActionListener(new ActionListener() {
//                @Override
//                public void actionPerformed(ActionEvent e) {
//                    fileDialog.dispose(); 
//                    openedFileLabel.setText("Opened File: None");
//                }
//            });
//            
//            fileContentArea.setEditable(false);
//            
//           
//            int totalPages = lines.length;
//
//            updateContentArea(fileContentArea, currentPage);
//            
//           
//            JButton previousButton = new JButton("Previous");
//            JButton nextButton = new JButton("Next");
//            JLabel pageLabel = new JLabel("Page 1 of " + totalPages);
//
//
//            previousButton.addActionListener(e -> {
//                if (currentPage > 0) {
//                    currentPage--;
//                    updateContentArea(fileContentArea, currentPage);
//                    pageLabel.setText("Page " + (currentPage + 1) + " of " + totalPages);
//                }
//            });
//
//            
//            nextButton.addActionListener(e -> {
//                if (currentPage < totalPages - 1) {
//                    currentPage++;
//                    updateContentArea(fileContentArea,  currentPage);
//                    pageLabel.setText("Page " + (currentPage + 1) + " of " + totalPages);
//                }
//            });
//
//          
//            JPanel buttonPanel = new JPanel();
//            buttonPanel.add(previousButton);
//            buttonPanel.add(nextButton);
//            buttonPanel.add(pageLabel);
//            buttonPanel.add(editButton);
//            buttonPanel.add(saveButton);
//            buttonPanel.add(closeButton);
//            buttonPanel.add(searchButton);
//
//            
//            JPanel fontPanel = new JPanel();
//            fontPanel.add(new JLabel("Font:"));
//            fontPanel.add(fontComboBox);
//            fontPanel.add(new JLabel("Font Size:"));
//            fontPanel.add(fontSizeComboBox);
//            
//            fontPanel.add(new JLabel("Search:"));
//            fontPanel.add(searchBar);
//            fontPanel.add(searchButton);
//
//            
//            fileDialog.getContentPane().add(fontPanel, BorderLayout.NORTH);
//            fileDialog.getContentPane().add(scrollPane, BorderLayout.CENTER);
//            fileDialog.getContentPane().add(buttonPanel, BorderLayout.SOUTH);
//            
//           
//            fileDialog.setVisible(true);
//            openedFileLabel.setText("Opened File: " + fileName);
//        } else {
//            
//            JOptionPane.showMessageDialog(this, "File not found.");
//        }
//    }
//
//
//    private void updateContentArea(JTextArea textArea, int currentPage) {
//        textArea.setText(""); 
//        textArea.setText(lines[currentPage]);
//    }
//
//    private void filterFiles(String fileName, DefaultTableModel tableModel) {
//        tableModel.setRowCount(0);
//        List<String[]> files = BLO.listFiles();
//        for (String[] file : files) {
//            if (file[0].toLowerCase().contains(fileName.toLowerCase())) {
//                tableModel.addRow(file);
//            }
//        }
//    }
//
//    private void refreshFileList(DefaultTableModel tableModel) {
//        tableModel.setRowCount(0);
//        List<String[]> files = BLO.listFiles();
//        for (String[] file : files) {
//            tableModel.addRow(file);
//        }
//    }
//
//
//	@Override
//	public void actionPerformed(ActionEvent e) {
//		if (e.getSource() == searchButton) {
//            String searchTerm = searchBar.getText();
//            loadFileContent(searchTerm);
//        }
//		
//	}
//   
//}