package BL;

import java.io.File;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.List;

import DAL.DummyMySQLDAO;
import DAL.IDAO;

public class Facade implements IBLLFacade{
	private B_LOGIC businessLogic;

	public Facade(DummyMySQLDAO dao) {
		DummyMySQLDAO dalFacade = new DummyMySQLDAO();
		this.businessLogic = new B_LOGIC(dalFacade);
	}

	public String importTextFile(File file) throws IOException {
		return businessLogic.importTextFile(file);
	}

	public String saveImportedFileToDatabase(String content, String fileName) throws NoSuchAlgorithmException {
		return businessLogic.saveImportedFileToDatabase(content, fileName);
	}

	public List<String> getFileSuggestions(String searchTerm) {
		return businessLogic.getFileNames(searchTerm);
	}

	public String getFileContent(String fileName) {
		return businessLogic.getFileContent(fileName);
	}
	
	
	public boolean saveContentWithPagination(int textFileId, String content) {
		return this.businessLogic.saveContentWithPagination( textFileId, content);
	}
	
	
	public void handlePaginationForFile(String fileName, String content) {
		return;
	}
	
	
	public int getFileIdByName(String fileName) {
		return this.businessLogic.getFileIdByName(fileName);
	}
}
