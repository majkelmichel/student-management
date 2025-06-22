package pl.edu.wit.studentManagement.view.fragments;

import pl.edu.wit.studentManagement.exceptions.ValidationException;
import pl.edu.wit.studentManagement.service.ServiceFactory;
import pl.edu.wit.studentManagement.service.StudentGroupService;
import pl.edu.wit.studentManagement.service.StudentService;
import pl.edu.wit.studentManagement.service.dto.student.StudentDto;
import pl.edu.wit.studentManagement.service.dto.studentGroup.StudentGroupDto;
import pl.edu.wit.studentManagement.service.dto.studentGroup.UpdateStudentGroupDto;
import pl.edu.wit.studentManagement.service.dto.studentGroup.CreateStudentGroupDto;
import pl.edu.wit.studentManagement.view.dialogs.AddGroupDialog;
import pl.edu.wit.studentManagement.view.dialogs.AssignStudentToGroupDialog;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.awt.*;
import java.util.List;

public class GroupsFragment {
    private final JPanel panel;
    private final JTable groupsTable;
    private final GroupsTableModel groupsTableModel;

    private JTextField codeField;
    private JTextField specializationField;
    private JTextArea descriptionArea;
    private JButton saveButton;

    private JTable groupStudentsTable;
    private GroupStudentsTableModel groupStudentsTableModel;
    private List<StudentDto> groupStudents;

    private final StudentGroupService studentGroupService = ServiceFactory.getStudentGroupService();
    private final StudentService studentService = ServiceFactory.getStudentService();

    private final List<StudentGroupDto> groups;

    public GroupsFragment() {
        this.panel = new JPanel(new BorderLayout());

        groups = studentGroupService.getAll();

        groupsTableModel = new GroupsTableModel(groups);
        groupsTable = new JTable(groupsTableModel);
        groupsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        groupsTable.setRowHeight(28);

        JScrollPane scrollPane = new JScrollPane(groupsTable);

        var detailsPanel = createDetailsPanel();

        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, scrollPane, detailsPanel);
        splitPane.setResizeWeight(0.8);
        panel.add(splitPane, BorderLayout.CENTER);

        var actionsPanel = new JPanel();
        actionsPanel.setLayout(new BoxLayout(actionsPanel, BoxLayout.X_AXIS));
        actionsPanel.setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));

        JButton addButton = new JButton("Dodaj grupę");
        JButton removeButton = new JButton("Usuń grupę");

        actionsPanel.add(addButton);
        actionsPanel.add(Box.createHorizontalStrut(8));
        actionsPanel.add(removeButton);

        addButton.addActionListener(e -> handleAddGroup());
        removeButton.addActionListener(e -> handleDeleteGroup());
        groupsTable.getSelectionModel().addListSelectionListener(e -> handleGroupSelected());
        saveButton.addActionListener(e -> handleSaveGroup());

        setDetailsEditable(false);
        clearDetails();

        panel.add(actionsPanel, BorderLayout.SOUTH);
    }

    private JPanel createDetailsPanel() {
        var detailsPanel = new JPanel();
        detailsPanel.setLayout(new BoxLayout(detailsPanel, BoxLayout.Y_AXIS));
        detailsPanel.setBorder(BorderFactory.createTitledBorder("Szczegóły grupy"));

        detailsPanel.add(new JLabel("Kod grupy:"));
        codeField = new JTextField();
        codeField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 28));
        codeField.setAlignmentX(Component.LEFT_ALIGNMENT);
        detailsPanel.add(codeField);

        detailsPanel.add(Box.createVerticalStrut(8));

        detailsPanel.add(new JLabel("Specjalizacja:"));
        specializationField = new JTextField();
        specializationField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 28));
        specializationField.setAlignmentX(Component.LEFT_ALIGNMENT);
        detailsPanel.add(specializationField);

        detailsPanel.add(Box.createVerticalStrut(8));

        detailsPanel.add(new JLabel("Opis:"));
        descriptionArea = new JTextArea(3, 20);
        descriptionArea.setLineWrap(true);
        descriptionArea.setWrapStyleWord(true);
        descriptionArea.setMaximumSize(new Dimension(Integer.MAX_VALUE, 60));
        descriptionArea.setAlignmentX(Component.LEFT_ALIGNMENT);

        var descScroll = new JScrollPane(descriptionArea);
        descScroll.setMaximumSize(new Dimension(Integer.MAX_VALUE, 70));
        descScroll.setAlignmentX(Component.LEFT_ALIGNMENT);
        detailsPanel.add(descScroll);

        detailsPanel.add(Box.createVerticalStrut(8));

        saveButton = new JButton("Zapisz zmiany");
        saveButton.setAlignmentX(Component.LEFT_ALIGNMENT);
        detailsPanel.add(saveButton);

        detailsPanel.add(Box.createVerticalStrut(16));
        detailsPanel.add(new JLabel("Studenci w grupie:"));
        groupStudents = new java.util.ArrayList<>();
        groupStudentsTableModel = new GroupStudentsTableModel(groupStudents);
        groupStudentsTable = new JTable(groupStudentsTableModel);
        groupStudentsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        groupStudentsTable.setRowHeight(24);
        JScrollPane studentsScroll = new JScrollPane(groupStudentsTable);
        studentsScroll.setMaximumSize(new Dimension(Integer.MAX_VALUE, 120));
        studentsScroll.setAlignmentX(Component.LEFT_ALIGNMENT);
        detailsPanel.add(studentsScroll);

        var actionsPanel = new JPanel();
        actionsPanel.setLayout(new BoxLayout(actionsPanel, BoxLayout.X_AXIS));
        actionsPanel.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
        actionsPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        var assignStudentButton = new JButton("Przypisz studentów do grupy");
        assignStudentButton.setAlignmentX(Component.LEFT_ALIGNMENT);
        assignStudentButton.addActionListener(e -> handleAssignStudentsToGroup());
        actionsPanel.add(assignStudentButton);

        actionsPanel.add(Box.createHorizontalStrut(8));

        var removeStudentButton = new JButton("Usuń z grupy");
        removeStudentButton.setAlignmentX(Component.LEFT_ALIGNMENT);
        removeStudentButton.addActionListener(e -> handleRemoveStudentFromGroup());
        actionsPanel.add(removeStudentButton);

        detailsPanel.add(actionsPanel);

        return detailsPanel;
    }

    private void clearDetails() {
        codeField.setText("");
        specializationField.setText("");
        descriptionArea.setText("");
        setDetailsEditable(false);
        groupStudents.clear();
        if (groupStudentsTableModel != null) groupStudentsTableModel.fireTableDataChanged();
    }

    private void setDetailsEditable(boolean editable) {
        codeField.setEditable(editable);
        specializationField.setEditable(editable);
        descriptionArea.setEditable(editable);
        saveButton.setEnabled(editable);
    }

    public JPanel getPanel() {
        return panel;
    }

    private void handleAddGroup() {
        AddGroupDialog dialog = new AddGroupDialog();
        var added = dialog.showDialog(panel);
        if (added)
            reloadGroups();
    }

    private void handleDeleteGroup() {
        int selectedRow = groupsTable.getSelectedRow();
        if (selectedRow == -1)
            return;

        var group = groups.get(selectedRow);

        int result = JOptionPane.showConfirmDialog(
                panel,
                "Czy na pewno usunąć grupę?",
                "Potwierdzenie usunięcia",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE
        );

        if (result != JOptionPane.YES_OPTION)
            return;

        try {
            studentGroupService.delete(group.getId());
            reloadGroups();
            clearDetails();
        } catch (ValidationException ex) {
            JOptionPane.showMessageDialog(panel, getTranslation(ex.getMessageKey()), "Błąd", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void handleGroupSelected() {
        int selectedRow = groupsTable.getSelectedRow();
        if (selectedRow != -1) {
            StudentGroupDto group = groups.get(selectedRow);
            codeField.setText(group.getCode());
            specializationField.setText(group.getSpecialization());
            descriptionArea.setText(group.getDescription());
            setDetailsEditable(true);
            reloadGroupStudents(group.getId());
        } else {
            clearDetails();
        }
    }

    private void reloadGroupStudents(java.util.UUID groupId) {
        var groupWithStudents = studentGroupService.getWithStudentsById(groupId);
        groupStudents.clear();

        if (groupWithStudents.isEmpty()) {
            groupStudentsTableModel.fireTableDataChanged();
            return;
        }

        groupStudents.addAll(groupWithStudents.get().getStudents());

        groupStudentsTableModel.fireTableDataChanged();
    }

    private void handleSaveGroup() {
        int selectedRow = groupsTable.getSelectedRow();

        if (selectedRow == -1)
            return;

        StudentGroupDto group = groups.get(selectedRow);
        UpdateStudentGroupDto dto = new UpdateStudentGroupDto();
        dto.setCode(codeField.getText());
        dto.setSpecialization(specializationField.getText());
        dto.setDescription(descriptionArea.getText());

        try {
            studentGroupService.update(group.getId(), dto);
            reloadGroups();

            groupsTable.setRowSelectionInterval(selectedRow, selectedRow);
            handleGroupSelected();

            JOptionPane.showMessageDialog(panel, "Grupa została zaktualizowana.", "Sukces", JOptionPane.INFORMATION_MESSAGE);
        } catch (ValidationException ex) {
            JOptionPane.showMessageDialog(panel, getTranslation(ex.getMessageKey()), "Błąd", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void handleAssignStudentsToGroup() {
        int selectedGroupRow = groupsTable.getSelectedRow();
        if (selectedGroupRow == -1) {
            JOptionPane.showMessageDialog(panel, "Najpierw wybierz grupę.", "Błąd", JOptionPane.ERROR_MESSAGE);
            return;
        }

        AssignStudentToGroupDialog dialog = new AssignStudentToGroupDialog();
        var result = dialog.showDialog(panel);
        if (result != null) {
            StudentGroupDto group = groups.get(selectedGroupRow);
            for (var studentId : result.studentIds) {
                try {
                    studentService.assignStudentToGroup(studentId, group.getId());
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(panel, "Błąd przypisywania: " + ex.getMessage(), "Błąd", JOptionPane.ERROR_MESSAGE);
                }
            }
            reloadGroupStudents(group.getId());
        }
    }

    private void handleRemoveStudentFromGroup() {
        int selectedStudentRow = groupStudentsTable.getSelectedRow();

        if (selectedStudentRow == -1) {
            JOptionPane.showMessageDialog(panel, "Wybierz studenta do usunięcia z grupy.", "Błąd", JOptionPane.ERROR_MESSAGE);
            return;
        }

        var student = groupStudents.get(selectedStudentRow);

        var result = JOptionPane.showConfirmDialog(
                panel,
                "Czy na pewno usunąć studenta z grupy?",
                "Potwierdzenie usunięcia",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE
        );

        if (result != JOptionPane.YES_OPTION)
            return;

        try {
            studentService.removeFromGroup(student.getId());

            int selectedGroupRow = groupsTable.getSelectedRow();
            if (selectedGroupRow != -1) {
                StudentGroupDto group = groups.get(selectedGroupRow);
                reloadGroupStudents(group.getId());
            }
        } catch (ValidationException ex) {
            JOptionPane.showMessageDialog(panel, ex.getMessageKey(), "Błąd", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void reloadGroups() {
        groups.clear();
        groups.addAll(studentGroupService.getAll());
        groupsTableModel.fireTableDataChanged();
    }

    private String getTranslation(String key) {
        return key;
    }

    private static class GroupsTableModel extends AbstractTableModel {
        private final String[] columnNames = {"Kod grupy", "Specjalizacja", "Opis"};
        private final List<StudentGroupDto> groups;

        public GroupsTableModel(List<StudentGroupDto> groups) {
            this.groups = groups;
        }

        @Override
        public int getRowCount() {
            return groups.size();
        }

        @Override
        public int getColumnCount() {
            return columnNames.length;
        }

        @Override
        public String getColumnName(int column) {
            return columnNames[column];
        }

        @Override
        public Object getValueAt(int rowIndex, int columnIndex) {
            StudentGroupDto group = groups.get(rowIndex);
            switch (columnIndex) {
                case 0:
                    return group.getCode();
                case 1:
                    return group.getSpecialization();
                case 2:
                    return group.getDescription();
                default:
                    return "";
            }
        }

    }

    private static class GroupStudentsTableModel extends AbstractTableModel {
        private final String[] columns = {"Imię", "Nazwisko", "Album"};
        private final List<pl.edu.wit.studentManagement.service.dto.student.StudentDto> students;

        public GroupStudentsTableModel(List<pl.edu.wit.studentManagement.service.dto.student.StudentDto> students) {
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
            var s = students.get(row);
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
