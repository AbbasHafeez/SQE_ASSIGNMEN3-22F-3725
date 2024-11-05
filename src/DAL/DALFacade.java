package DAL;

import java.sql.Date;
import java.sql.SQLException;
import java.util.List;

public class DALFacade implements IDALFacade {

	private DummyMySQLDAO dao = null;
	
	public DALFacade(DummyMySQLDAO dao) {
		this.dao = dao;
	}
	
	@Override
	public List<String> getFileNames(String searchTerm) {
		return this.dao.searchFileNames(searchTerm);
	}

	@Override
	public boolean importFile(String fileName, String content) {
		return this.dao.importFile(fileName, content);
	}

	@Override
	public String getFileContent(String fileName) {
		return this.dao.getFileContent(fileName);
	}

	@Override
	public boolean createFileWithCheck(String fileName, String content, Date date) throws SQLException {
		return this.dao.createFileWithCheck( fileName, content, date);
	}

	@Override
	public boolean deleteFileFromDatabase(String fileName) {
		return this.dao.deleteFileFromDatabase(fileName);
	}

	@Override
	public String[] getFileDetails(String fileName) {
		return this.dao.getFileDetails(fileName);
	}

	@Override
	public List<String[]> listAllFiles() {
		return this.dao.listAllFiles();
	}

	@Override
	public List<String> searchFileNames(String searchTerm) {
		return this.dao.searchFileNames(searchTerm);
	}
	
	@Override
	public boolean saveContentWithPagination(int textFileId, String content) {
		return this.dao.saveContentWithPagination( textFileId, content);
	}
	
	@Override
	public void handlePaginationForFile(String fileName, String content) {
		return;
	}
	
	@Override
	public int getFileIdByName(String fileName) {
		return this.dao.getFileIdByName(fileName);
	}

	@Override
	public boolean saveFile(String fileName, String[] newContent, String fileType) {
		return this.dao.saveFile(fileName, newContent, fileType);
	}

	@Override
	public List<String> getFileSuggestions(String text2) {
		return this.dao.getFileSuggestions(text2);
	}

	@Override
	public boolean isContentExisting(String fileName, String newContent) {
		// TODO Auto-generated method stub
		return false;
	}
}