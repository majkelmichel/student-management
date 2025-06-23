package pl.edu.wit.studentManagement.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("ServiceFactory Test Suite")
class ServiceFactoryTest {

    @Test
    @DisplayName("getStudentService returns same instance on multiple calls")
    void getStudentService_ReturnsSameInstance() {
        // Act
        StudentService first = ServiceFactory.getStudentService();
        StudentService second = ServiceFactory.getStudentService();

        // Assert
        assertSame(first, second, "Multiple calls should return the same instance");
        assertNotNull(first, "Service instance should not be null");
    }

    @Test
    @DisplayName("getStudentGroupService returns same instance on multiple calls")
    void getStudentGroupService_ReturnsSameInstance() {
        // Act
        StudentGroupService first = ServiceFactory.getStudentGroupService();
        StudentGroupService second = ServiceFactory.getStudentGroupService();

        // Assert
        assertSame(first, second, "Multiple calls should return the same instance");
        assertNotNull(first, "Service instance should not be null");
    }

    @Test
    @DisplayName("getSubjectService returns same instance on multiple calls")
    void getSubjectService_ReturnsSameInstance() {
        // Act
        SubjectService first = ServiceFactory.getSubjectService();
        SubjectService second = ServiceFactory.getSubjectService();

        // Assert
        assertSame(first, second, "Multiple calls should return the same instance");
        assertNotNull(first, "Service instance should not be null");
    }

    @Test
    @DisplayName("getGradeService returns same instance on multiple calls")
    void getGradeService_ReturnsSameInstance() {
        // Act
        GradeService first = ServiceFactory.getGradeService();
        GradeService second = ServiceFactory.getGradeService();

        // Assert
        assertSame(first, second, "Multiple calls should return the same instance");
        assertNotNull(first, "Service instance should not be null");
    }

    @Test
    @DisplayName("getGradeQueryService returns same instance on multiple calls")
    void getGradeQueryService_ReturnsSameInstance() {
        // Act
        GradeQueryService first = ServiceFactory.getGradeQueryService();
        GradeQueryService second = ServiceFactory.getGradeQueryService();

        // Assert
        assertSame(first, second, "Multiple calls should return the same instance");
        assertNotNull(first, "Service instance should not be null");
    }

    @Test
    @DisplayName("getStudentGroupSubjectAssignmentService returns same instance on multiple calls")
    void getStudentGroupSubjectAssignmentService_ReturnsSameInstance() {
        // Act
        StudentGroupSubjectAssignmentService first = ServiceFactory.getStudentGroupSubjectAssignmentService();
        StudentGroupSubjectAssignmentService second = ServiceFactory.getStudentGroupSubjectAssignmentService();

        // Assert
        assertSame(first, second, "Multiple calls should return the same instance");
        assertNotNull(first, "Service instance should not be null");
    }
}