package Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import BL.B_LOGIC;
import DAL.DummyMySQLDAO;
import utils.Excel;

class BL_Test {
    private B_LOGIC b_logic;
    private DummyMySQLDAO dummyDAO;
    private Excel excelReader;

    @BeforeEach
    void setUp() throws IOException {
        dummyDAO = new DummyMySQLDAO();
        b_logic = new B_LOGIC(dummyDAO);
        excelReader = new Excel("path/to/your/excel/file.xlsx"); // Update the path to your Excel file
    }

    @Test
    void testCreateFile() throws SQLException {
        String fileName = excelReader.getCellDataString("BOTesting", 0, 1);
        String content = excelReader.getCellDataString("BOTesting", 0, 2);
        String result = b_logic.createFile(fileName, content);
        assertEquals("File Created", result);

        String emptyContent = excelReader.getCellDataString("BOTesting", 1, 2);
        result = b_logic.createFile("EmptyContentFile", emptyContent);
        assertEquals("File Created", result);

        String specialName = excelReader.getCellDataString("BOTesting", 2, 1);
        content = excelReader.getCellDataString("BOTesting", 2, 2);
        result = b_logic.createFile(specialName, content);
        assertEquals("File Created", result);

        StringBuilder largeContent = new StringBuilder();
        for (int i = 0; i < 10000; i++) largeContent.append("large_content ");
        result = b_logic.createFile("LargeContentFile", largeContent.toString());
        assertEquals("File Created", result);
    }

    @Test
    void testDeleteFile() throws SQLException {
        String fileName = excelReader.getCellDataString("BOTesting", 4, 1);
        b_logic.createFile(fileName, excelReader.getCellDataString("BOTesting", 4, 2));
        
        String result = b_logic.deleteFile(fileName);
        assertEquals("File Deleted", result);

        String nonExistentFile = excelReader.getCellDataString("BOTesting", 5, 1);
        result = b_logic.deleteFile(nonExistentFile);
        assertEquals("File can not Delete", result);
    }

    @Test
    void testImportTextFile() throws IOException {
        File testFile = new File(excelReader.getCellDataString("BOTesting", 6, 1));
        try (FileWriter writer = new FileWriter(testFile)) {
            writer.write(excelReader.getCellDataString("BOTesting", 6, 2));
        }

        String content = b_logic.importTextFile(testFile);
        assertEquals(excelReader.getCellDataString("BOTesting", 6, 2), content);

        File invalidFile = new File(excelReader.getCellDataString("BOTesting", 7, 1));
        String invalidResult = b_logic.importTextFile(invalidFile);
        assertEquals(excelReader.getCellDataString("BOTesting", 7, 2), invalidResult);
    }

    @Test
    void testListFiles() throws SQLException {
        b_logic.createFile(excelReader.getCellDataString("BOTesting", 8, 1), excelReader.getCellDataString("BOTesting", 8, 2));
        b_logic.createFile(excelReader.getCellDataString("BOTesting", 9, 1), excelReader.getCellDataString("BOTesting", 9, 2));

        List<String[]> files = b_logic.listFiles();
        assertEquals(2, files.size());
        assertEquals(excelReader.getCellDataString("BOTesting", 8, 1), files.get(0)[0]);
        assertEquals(excelReader.getCellDataString("BOTesting", 9, 1), files.get(1)[0]);
    }

    @Test
    void testGetFileContent() throws SQLException {
        String fileName = excelReader.getCellDataString("BOTesting", 10, 1);
        String content = excelReader.getCellDataString("BOTesting", 10, 2);
        b_logic.createFile(fileName, content);

        String retrievedContent = b_logic.getFileContent(fileName);
        assertEquals(content, retrievedContent);
    }

    @Test
    void testGetFileNames() throws SQLException {
        b_logic.createFile(excelReader.getCellDataString("BOTesting", 11, 1), excelReader.getCellDataString("BOTesting", 11, 2));
        b_logic.createFile(excelReader.getCellDataString("BOTesting", 12, 1), excelReader.getCellDataString("BOTesting", 12, 2));
        b_logic.createFile(excelReader.getCellDataString("BOTesting", 13, 1), excelReader.getCellDataString("BOTesting", 13, 2));

        List<String> result = b_logic.getFileNames("File");
        assertEquals(2, result.size());
    }
}
