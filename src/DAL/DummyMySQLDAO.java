package DAL;

import java.sql.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DummyMySQLDAO implements IDAO {

    private List<Map<String, Object>> textFiles;
    private List<Map<String, Object>> pages;

    public DummyMySQLDAO() {
        textFiles = new ArrayList<>();
        pages = new ArrayList<>();
    }

    @Override
    public boolean importFile(String fileName, String content) {
        Map<String, Object> newFile = new HashMap<>();
        int newFileId = textFiles.size() + 1; // Generate a new ID
        newFile.put("id", newFileId);
        newFile.put("fileName", fileName);
        textFiles.add(newFile);

        return saveContentWithPagination(newFileId, content);
    }

    @Override
    public boolean createFileWithCheck(String fileName, String newContent, Date createdDate) {
        if (isContentExisting(fileName, newContent)) {
            return true;
        } else {
            return creatingFile(fileName, newContent, createdDate);
        }
    }

    private boolean creatingFile(String fileName, String newContent, Date createdDate) {
        Map<String, Object> newFile = new HashMap<>();
        int newFileId = textFiles.size() + 1; // Generate a new ID
        newFile.put("id", newFileId);
        newFile.put("fileName", fileName);
        newFile.put("createdDate", createdDate);
        textFiles.add(newFile);

        return saveContentWithPagination(newFileId, newContent);
    }

    @Override
    public boolean isContentExisting(String fileName, String newContent) {
        for (Map<String, Object> file : textFiles) {
            if (file.get("fileName").equals(fileName)) {
                String existingContent = (String) file.get("content");
                return existingContent != null && existingContent.equals(newContent);
            }
        }
        return false;
    }

    @Override
    public boolean deleteFileFromDatabase(String fileName) {
        return textFiles.removeIf(file -> file.get("fileName").equals(fileName));
    }

    @Override
    public boolean saveFile(String fileName, String[] content, String fileType) {
        for (Map<String, Object> file : textFiles) {
            if (file.get("fileName").equals(fileName)) {
                file.put("fileType", fileType);
                for (int i = 0; i < content.length; i++) {
                    Map<String, Object> page = new HashMap<>();
                    page.put("textFileId", file.get("id"));
                    page.put("pageNumber", i + 1);
                    page.put("pageContent", content[i]);
                    pages.add(page);
                }
                return true;
            }
        }
        return false;
    }

    @Override
    public String[] getFileDetails(String fileName) {
        List<String> contentList = new ArrayList<>();
        for (Map<String, Object> page : pages) {
            if (page.get("textFileId").equals(getFileIdByName(fileName))) {
                contentList.add((String) page.get("pageContent"));
            }
        }
        return contentList.toArray(new String[0]);
    }

    @Override
    public List<String[]> listAllFiles() {
        List<String[]> files = new ArrayList<>();
        for (Map<String, Object> file : textFiles) {
            String fileName = (String) file.get("fileName");
            String lastModified = file.get("lastModified") != null ? file.get("lastModified").toString() : "N/A";
            files.add(new String[]{fileName, lastModified});
        }
        return files;
    }

    @Override
    public List<String> getFileNames(String searchTerm) {
        List<String> fileNames = new ArrayList<>();
        for (Map<String, Object> file : textFiles) {
            String fileName = (String) file.get("fileName");
            if (fileName.contains(searchTerm)) {
                fileNames.add(fileName);
            }
        }
        return fileNames;
    }

    @Override
    public String getFileContent(String fileName) {
        for (Map<String, Object> file : textFiles) {
            if (file.get("fileName").equals(fileName)) {
                return (String) file.get("content");
            }
        }
        return null;
    }

    @Override
    public List<String> searchFileNames(String searchTerm) {
        return getFileNames(searchTerm);
    }

    @Override
    public boolean saveContentWithPagination(int textFileId, String content) {
        String[] words = content.split("\\s+");
        int maxWordsPerLine = 15;
        int maxCharsPerLine = 90;
        int linesPerPage = 25;

        List<String> pagesContent = new ArrayList<>();
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
                    pagesContent.add(pageBuilder.toString().trim());
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
            pagesContent.add(pageBuilder.toString().trim());
        }

        for (int i = 0; i < pagesContent.size(); i++) {
            Map<String, Object> page = new HashMap<>();
            page.put("textFileId", textFileId);
            page.put("pageNumber", i + 1);
            page.put("pageContent", pagesContent.get(i));
            pages.add(page);
        }
        return true;
    }

    @Override
    public void handlePaginationForFile(String fileName, String content) {
        int textFileId = getFileIdByName(fileName);
        if (textFileId > 0) {
            saveContentWithPagination(textFileId, content);
        } else {
            System.out.println("Error: File ID could not be retrieved for " + fileName);
        }
    }

    @Override
    public int getFileIdByName(String fileName) {
        for (Map<String, Object> file : textFiles) {
            if (file.get("fileName").equals(fileName)) {
                return (int) file.get("id");
            }
        }
        return -1;
    }

    @Override
    public List<String> getFileSuggestions(String searchTerm) {
        List<String> suggestions = new ArrayList<>();
        for (Map<String, Object> file : textFiles) {
            String fileName = (String) file.get("fileName");
            if (fileName.contains(searchTerm)) {
                suggestions.add(fileName);
            }
        }
        return suggestions;
    }
}
