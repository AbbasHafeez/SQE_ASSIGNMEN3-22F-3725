package BL;

import java.io.File;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.List;

public interface IBLLFacade {
	public String importTextFile(File file) throws IOException;
	public String saveImportedFileToDatabase(String content, String fileName) throws NoSuchAlgorithmException;
	public List<String> getFileSuggestions(String searchTerm);
	public String getFileContent(String fileName);
	
	public boolean saveContentWithPagination(int textFileId, String content); 
	public void handlePaginationForFile(String fileName, String content); 
	public int getFileIdByName(String fileName); 
}
