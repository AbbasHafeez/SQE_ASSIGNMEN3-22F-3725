package DAL;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

public class MySQLDAO implements IDAO {
	private DB_Connection dbConnection;

	public MySQLDAO() {
		dbConnection = new DB_Connection();
	}

	public boolean importFile(String fileName, String content) {
        String insertFileSql = "INSERT INTO textFiles (fileName) VALUES (?)";
        try (Connection conn = dbConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(insertFileSql, PreparedStatement.RETURN_GENERATED_KEYS)) {

            // Insert into textFiles
            pstmt.setString(1, fileName);
            pstmt.executeUpdate();

            // Get generated ID for the file
            int generatedFileId;
            try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    generatedFileId = generatedKeys.getInt(1);
                } else {
                    return false; // Insert failed
                }
            }

            // Handle pagination
            return saveContentWithPagination(generatedFileId, content);
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
	
	public boolean createFileWithCheck(String fileName, String newContent, Date createdDate) throws SQLException {
        boolean res = isContentExisting(fileName, newContent);
		if(res == true) {
			return true;
		} else {
		    creatingFile(fileName, newContent, createdDate);
		    return true;
		}
    }

	private boolean creatingFile(String fileName, String newContent, Date createdDate) {
	    // First, insert the file into the textFiles table
	    String queryInsert = "INSERT INTO textFiles (fileName, createdDate) VALUES (?, ?)";

	    try (Connection conn = dbConnection.connect(); 
	         PreparedStatement pstmt = conn.prepareStatement(queryInsert, PreparedStatement.RETURN_GENERATED_KEYS)) {
	        
	        // Set parameters and execute the insert
	        pstmt.setString(1, fileName);
	        pstmt.setDate(2, createdDate);
	        pstmt.executeUpdate();

	        // Retrieve the generated ID for the new text file
	        int generatedFileId;
	        try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
	            if (generatedKeys.next()) {
	                generatedFileId = generatedKeys.getInt(1);
	            } else {
	                return false; // Insert failed, no ID obtained
	            }
	        }

	        // Save content with pagination
	        return saveContentWithPagination(generatedFileId, newContent);
	    } catch (SQLException e) {
	        e.printStackTrace();
	        return false;
	    }
	}
	public boolean isContentExisting(String fileName, String newContent) {
	    String query = "SELECT pageContent FROM pages WHERE pageNumber = 1 AND textFileId = (Select id FROM textfiles where fileName = ?)";

	    try (Connection conn = dbConnection.connect(); PreparedStatement pstmt = conn.prepareStatement(query)) {
	        pstmt.setString(1, fileName);
	        ResultSet rs = pstmt.executeQuery();

	        if (rs.next()) {
	            String oldContent = rs.getString("fileContent");
	            return oldContent.equals(newContent);
	        }
	        return false;
	    } catch (SQLException e) {
	        e.printStackTrace();
	        return false;
	    }
	}

	public boolean deleteFileFromDatabase(String fileName) {
	    String deleteQuery = "DELETE FROM textfiles WHERE fileName = ?";

	    try (Connection conn = dbConnection.connect(); PreparedStatement stmt = conn.prepareStatement(deleteQuery)) {
	        stmt.setString(1, fileName);
	        int rowsAffected = stmt.executeUpdate();
	        return rowsAffected > 0;
	    } catch (SQLException e) {
	        e.printStackTrace();
	        return false;
	    }
	}
	
	
	// view data layer
	
	//save file function to update changes
	public boolean saveFile(String fileName, String [] content, String fileType) { // yahan kion same code hai aony jis din facade factory kia sab functions idhr add kiye hyeee
		String queryCheck = "SELECT COUNT(*) FROM textFiles WHERE fileName = ?";
	    String updateQuery = "UPDATE textFiles SET lastModified = CURRENT_TIMESTAMP WHERE fileName = ?";
	    String updatePages = "UPDATE pages SET pageContent = ? WHERE textFileId = (SELECT id FROM textfiles WHERE fileName = ?) AND pageNumber = ?";
	    
	    try (Connection conn = dbConnection.connect();
	        PreparedStatement checkStmt = conn.prepareStatement(queryCheck)) {
	        checkStmt.setString(1, fileName);
	        ResultSet rs = checkStmt.executeQuery();
	        rs.next();
	        int count = rs.getInt(1);

	        if (count > 0) {
	            // Update existing file
	        	try (PreparedStatement updateStmt = conn.prepareStatement(updateQuery)) {
	                updateStmt.setString(1, fileName);
	                updateStmt.executeUpdate();
	            }

                for(int i = 0; i < content.length; ++i)
                {
                	try (PreparedStatement updateStmt = conn.prepareStatement(updatePages)) {
    	            	updateStmt.setString(1, content[i]);
    	            	updateStmt.setString(2, fileName); // kro run
    	            	updateStmt.setInt(3, i+1);
    	                updateStmt.executeUpdate();
    	            }
                }
	        	return true;
	        } else {
	            // Insert new file
	            try (PreparedStatement insertStmt = conn.prepareStatement(updateQuery)) {
	                insertStmt.executeUpdate();
	            }
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	    return false;
	}

	public String[] getFileDetails(String fileName) {
    	String[] data;
    	List <String> temp = new ArrayList<>();
		String query = "SELECT id, pageContent FROM pages WHERE textFileId = (SELECT id FROM textfiles WHERE fileName = ?)";

		try (Connection conn = dbConnection.connect(); PreparedStatement stmt = conn.prepareStatement(query)) {
			stmt.setString(1, fileName);
			ResultSet rs = stmt.executeQuery();

			int ik = 0;
			while (rs.next()) {
				temp.add(rs.getString("pageContent"));
	            System.out.println(rs.getString("id"));
	            ++ik;
			}

			

            data = new String[temp.size()];
            System.out.println(temp.size());
            for(int i = 0; i < temp.size(); ++i)
            {
            	data[i] = temp.get(i);
            }
            System.out.println(data.length);
            return data; 
		} catch (SQLException e) {
			e.printStackTrace();
			
			return new String[0]; 
		}
	}

	    public List<String[]> listAllFiles() {
	        List<String[]> files = new ArrayList<>();
	        String query = "SELECT fileName, lastModified FROM textfiles";
	        try (Connection conn = dbConnection.connect();
	             PreparedStatement stmt = conn.prepareStatement(query);
	             ResultSet rs = stmt.executeQuery()) {
	            while (rs.next()) {
	                String fileName = rs.getString("fileName");
	                String lastModified = rs.getTimestamp("lastModified").toString();
	                files.add(new String[]{fileName, lastModified});
	            }
	        } catch (SQLException e) {
	            e.printStackTrace();
	        }
	        return files;
	    }

		@Override
		public List<String> getFileNames(String searchTerm) {
			 List<String> fileNames = new ArrayList<>();
		        String sql = "SELECT fileName FROM textfiles WHERE fileName LIKE ?";

		        try (Connection conn = dbConnection.connect(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
		            pstmt.setString(1, "%" + searchTerm + "%");
		            ResultSet rs = pstmt.executeQuery();

		            while (rs.next()) {
		                fileNames.add(rs.getString("fileName"));
		            }
		        } catch (SQLException e) {
		            e.printStackTrace();
		        }
		        return fileNames;
		}

		@Override
		public String getFileContent(String fileName) {
			String content = null;
	        String sql = "SELECT fileContent FROM textfiles WHERE fileName = ?";

	        try (Connection conn = dbConnection.connect(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
	            pstmt.setString(1, fileName);
	            ResultSet rs = pstmt.executeQuery();

	            if (rs.next()) {
	                content = rs.getString("fileCcontent");
	            }
	        } catch (SQLException e) {
	            e.printStackTrace();
	        }
	        return content;
		}

		 @Override
		    public List<String> searchFileNames(String searchTerm) {
		        List<String> fileNames = new ArrayList<>();
		        String sql = "SELECT fileName FROM textfiles WHERE fileName LIKE ?";

		        try (Connection conn = dbConnection.connect(); PreparedStatement stmt = conn.prepareStatement(sql)) {
		            
		            stmt.setString(1, "%" + searchTerm + "%");

		            try (ResultSet rs = stmt.executeQuery()) {
		                while (rs.next()) {
		                    
		                    fileNames.add(rs.getString("fileName"));
		                }
		            }
		        } catch (SQLException e) {
		            e.printStackTrace();
		            
		        }

		        return fileNames;
		    }

		@Override
		public boolean saveContentWithPagination(int textFileId, String content) {
		    String[] words = content.split("\\s+");
		    int maxWordsPerLine = 15;
		    int maxCharsPerLine = 90;
		    int linesPerPage = 25;

		    List<String> pages = new ArrayList<>();
		    StringBuilder pageBuilder = new StringBuilder();
		    StringBuilder lineBuilder = new StringBuilder();
		    int lineWordCount = 0, lineCharCount = 0, lineCount = 0;

		    for (String word : words) {
		       
		        if (lineWordCount < maxWordsPerLine && (lineCharCount + word.length() + 1 <= maxCharsPerLine)) {
		            
		            lineBuilder.append(word).append(" ");
		            lineWordCount++;
		            lineCharCount += word.length() + 1; 
		        } else {
		            
		            pageBuilder.append(lineBuilder.toString().trim()).append("\n");
		            lineCount++;

		            lineBuilder.setLength(0);
		            lineBuilder.append(word).append(" ");
		            lineWordCount = 1;
		            lineCharCount = word.length() + 1;

		            if (lineCount == linesPerPage) {
		                pages.add(pageBuilder.toString().trim()); 
		                pageBuilder.setLength(0); 
		                lineCount = 0; 
		            }
		        }
		    }

		 
		    if (lineBuilder.length() > 0) {
		        pageBuilder.append(lineBuilder.toString().trim()).append("\n");
		        lineCount++;
		    }
		    if (pageBuilder.length() > 0) {
		        pages.add(pageBuilder.toString().trim());
		    }

		    String insertPageSql = "INSERT INTO pages (textFileId, pageNumber, pageContent) VALUES (?, ?, ?)";
		    try (Connection conn = dbConnection.connect();
		         PreparedStatement pstmt = conn.prepareStatement(insertPageSql)) {

		        for (int i = 0; i < pages.size(); i++) {
		            pstmt.setInt(1, textFileId);
		            pstmt.setInt(2, i + 1); 
		            pstmt.setString(3, pages.get(i));
		            pstmt.executeUpdate();
		        }
		        return true;
		    } catch (SQLException e) {
		        e.printStackTrace();
		        return false;
		    }
		}

		
		
		public void handlePaginationForFile(String fileName, String content) {
		   
		    int textFileId = getFileIdByName(fileName);
		    if (textFileId > 0) {
		        saveContentWithPagination(textFileId, content);
		    } else {
		        System.out.println("Error: File ID could not be retrieved for " + fileName);
		    }
		}
		
	
		public int getFileIdByName(String fileName) {
		    String query = "SELECT id FROM textFiles WHERE fileName = ?";
		    try (Connection conn = dbConnection.connect();
		         PreparedStatement pstmt = conn.prepareStatement(query)) {
		        pstmt.setString(1, fileName);
		        ResultSet rs = pstmt.executeQuery();
		        if (rs.next()) {
		            return rs.getInt("id");
		        }
		    } catch (SQLException e) {
		        e.printStackTrace();
		    }
		    return -1; 
		}

		@Override
		public List<String> getFileSuggestions(String text2) {
			// TODO Auto-generated method stub
			return null;
		}

	
}