package pl.edu.wit.studentManagement.view.dialogs;

import pl.edu.wit.studentManagement.service.ServiceFactory;
import pl.edu.wit.studentManagement.service.StudentService;
import pl.edu.wit.studentManagement.service.dto.student.StudentDto;
import pl.edu.wit.studentManagement.translations.Translator;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.awt.*;
import java.util.List;
import java.util.UUID;
import java.util.ArrayList;

/**
 * Dialog for assigning one or more students to a group.
 *
 * @author Wojciech Berdowski
 */
public class AssignStudentToGroupDialog {
    private final JDialog dialog;
    private final JTable studentsTable;
    private final List<StudentDto> selectedStudents = new ArrayList<>();
    private boolean confirmed = false;

    public static class Result {
        public final List<UUID> studentIds;

        public Result(List<UUID> studentIds) {
            this.studentIds = studentIds;
        }
    }

    public AssignStudentToGroupDialog() {
        dialog = new JDialog((Frame) null, Translator.translate("studentGroup.assignStudents"), true);
        dialog.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        dialog.setSize(500, 400);
        dialog.setLocationRelativeTo(null);

        StudentService studentService = ServiceFactory.getStudentService();
        var students = studentService.getStudentsNotAssignedToAnyGroup();

        studentsTable = new JTable(new StudentsTableModel(students));
        studentsTable.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        studentsTable.setRowHeight(24);
        JScrollPane studentsScroll = new JScrollPane(studentsTable);
        studentsScroll.setBorder(BorderFactory.createTitledBorder(Translator.translate("students")));

        JButton assignButton = new JButton(Translator.translate("studentGroup.assignStudentToGroup"));
        assignButton.addActionListener(e -> {
            int[] selectedRows = studentsTable.getSelectedRows();
            if (selectedRows.length > 0) {
                selectedStudents.clear();
                for (int row : selectedRows) {
                    selectedStudents.add(students.get(row));
                }
                confirmed = true;
                dialog.dispose();
            } else {
                JOptionPane.showMessageDialog(dialog, Translator.translate("student.notSelected"), Translator.translate("error"), JOptionPane.ERROR_MESSAGE);
            }
        });

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.add(studentsScroll, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(assignButton);

        dialog.getContentPane().add(mainPanel, BorderLayout.CENTER);
        dialog.getContentPane().add(buttonPanel, BorderLayout.SOUTH);
    }

    public Result showDialog(Component parent) {
        dialog.setLocationRelativeTo(parent);
        dialog.setVisible(true);
        if (confirmed && !selectedStudents.isEmpty()) {
            List<UUID> ids = new ArrayList<>();
            for (StudentDto s : selectedStudents) ids.add(s.getId());
            return new Result(ids);
        }
        return null;
    }

    private static class StudentsTableModel extends AbstractTableModel {
        private final String[] columns = {
                Translator.translate("firstName"),
                Translator.translate("lastName"),
                Translator.translate("album")
        };
        private final List<StudentDto> students;

        public StudentsTableModel(List<StudentDto> students) {
            this.students = students;
        }

        @Override
        public int getRowCount() {
            return students.size();
        }

        @Override
        public int getColumnCount() {
            return columns.length;
        }

        @Override
        public String getColumnName(int col) {
            return columns[col];
        }

        @Override
        public Object getValueAt(int row, int col) {
            StudentDto s = students.get(row);
            switch (col) {
                case 0:
                    return s.getFirstName();
                case 1:
                    return s.getLastName();
                case 2:
                    return s.getAlbum();
                default:
                    return "";
            }
        }
    }
}
