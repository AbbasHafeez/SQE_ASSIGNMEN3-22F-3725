package BL;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.security.NoSuchAlgorithmException;
import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

import DAL.DummyMySQLDAO;
import DAL.IDALFacade;
import DAL.IDAO;

public class B_LOGIC {

	private DummyMySQLDAO dalFacade;

	public B_LOGIC(DummyMySQLDAO dalFacade) {
		this.dalFacade = dalFacade;
	}

	public String importTextFile(File file) throws IOException {
	    if (!file.getName().endsWith(".txt")) {
	        return "Invalid file type. Only text files (.txt) are allowed.";
	    }

	    return new String(Files.readAllBytes(file.toPath()), java.nio.charset.StandardCharsets.UTF_8);
	}

    public String saveImportedFileToDatabase(String content, String fileName) throws NoSuchAlgorithmException {
    	return dalFacade.importFile(fileName, content) ? "File content saved to database successfully." : "Failed to save file content to database.";
    }
	
	public String createFile(String fileName, String content) throws SQLException {
	    Date date = Date.valueOf(LocalDate.now());
	    boolean res = dalFacade.createFileWithCheck(fileName, content, date);
		if(res ==true) {
			return "File Created";
		}
		return "File can not Create";
	}
	
	public String deleteFile(String fileName) {
	    boolean res= dalFacade.deleteFileFromDatabase(fileName);
	    if (res==true) {
	    	return "File Deleted";
	    }
	    return "File can not Delete";
	}
	 
	//view
	    public boolean saveFile(String fileName, String[] newContent, String fileType) {
	        return dalFacade.saveFile(fileName, newContent, fileType);
	    } 

	    public String[] getFileDetails(String fileName) {
	        return dalFacade.getFileDetails(fileName);
	    }

	    public List<String[]> listFiles() {
	        return dalFacade.listAllFiles();
	    }

	    public List<String> getFileNames(String searchTerm) {
	        
	        return dalFacade.searchFileNames(searchTerm);
	    }

	    public String getFileContent(String fileName) {
	        
	        return dalFacade.getFileContent(fileName);
	    }
	    
	    
		public boolean saveContentWithPagination(int textFileId, String content) {
			return this.dalFacade.saveContentWithPagination( textFileId, content);
		}
		
		
		public void handlePaginationForFile(String fileName, String content) {
			return;
		}
		
		
		public int getFileIdByName(String fileName) {
			return this.dalFacade.getFileIdByName(fileName);
		}
}