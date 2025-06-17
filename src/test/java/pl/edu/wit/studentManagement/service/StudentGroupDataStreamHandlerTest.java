package pl.edu.wit.studentManagement.service;

import org.junit.jupiter.api.*;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class StudentGroupDataStreamHandlerTest {

    private File tempFile;
    private StudentGroupDataStreamHandler handler;

    @BeforeEach
    void setUp() throws IOException {
        tempFile = File.createTempFile("test-groups", ".dat");
        // Delete the file to simulate first-time creation by the handler
        tempFile.delete();
        handler = new StudentGroupDataStreamHandler(tempFile.getAbsolutePath());
    }

    @AfterEach
    void tearDown() {
        if (tempFile.exists()) {
            tempFile.delete();
        }
    }

    @Test
    void testWriteAndReadAll() throws IOException {
        StudentGroup group1 = new StudentGroup("CS101", "Computer Science", "First group");
        StudentGroup group2 = new StudentGroup("CS102", "Computer Science", "Second group");

        handler.write(group1);
        handler.write(group2);

        List<StudentGroup> groups = handler.readAll();

        assertEquals(2, groups.size());
        assertTrue(groups.stream().anyMatch(g -> g.getId().equals(group1.getId())));
        assertTrue(groups.stream().anyMatch(g -> g.getId().equals(group2.getId())));
    }

    @Test
    void testUpdate() throws IOException {
        StudentGroup group = new StudentGroup("CS101", "CS", "Desc");
        handler.write(group);

        group.setCode("CS101-UPDATED");
        group.setDescription("Updated description");
        handler.update(group);

        List<StudentGroup> groups = handler.readAll();
        assertEquals(1, groups.size());
        assertEquals("CS101-UPDATED", groups.get(0).getCode());
        assertEquals("Updated description", groups.get(0).getDescription());
    }

    @Test
    void testUpdateNonExistentGroupThrows() {
        StudentGroup group = new StudentGroup("NonExistent", "X", "Y");

        IOException ex = assertThrows(IOException.class, () -> handler.update(group));
        assertTrue(ex.getMessage().contains("StudentGroup not found"));
    }

    @Test
    void testDeleteById() throws IOException {
        StudentGroup group = new StudentGroup("CS101", "CS", "Desc");
        handler.write(group);

        handler.deleteById(group.getId());
        List<StudentGroup> groups = handler.readAll();
        assertTrue(groups.isEmpty());
    }

    @Test
    void testDeleteNonExistentGroupThrows() {
        UUID randomId = UUID.randomUUID();
        IOException ex = assertThrows(IOException.class, () -> handler.deleteById(randomId));
        assertTrue(ex.getMessage().contains("StudentGroup not found"));
    }

    @Test
    void testReadEmptyFileReturnsEmptyList() throws IOException {
        List<StudentGroup> groups = handler.readAll();
        assertNotNull(groups);
        assertTrue(groups.isEmpty());
    }
}
