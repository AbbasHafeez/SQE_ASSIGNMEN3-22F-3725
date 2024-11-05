package DAL;

import java.sql.Date;
import java.sql.SQLException;
import java.util.List;

public interface IDAO {
    List<String> getFileNames(String searchTerm);
    boolean importFile(String fileName, String content);
    String getFileContent(String fileName);
	boolean createFileWithCheck(String fileName, String content, Date date) throws SQLException;
	boolean deleteFileFromDatabase(String fileName);
	boolean saveFile(String fileName, String[] newContent, String fileType);
	String[] getFileDetails(String fileName);
	List<String[]> listAllFiles();
	List<String> searchFileNames(String searchTerm);
	boolean saveContentWithPagination(int textFileId, String content);
	void handlePaginationForFile(String fileName, String content);
	public int getFileIdByName(String fileName);
	List<String> getFileSuggestions(String text2);
	boolean isContentExisting(String fileName, String newContent);
}
