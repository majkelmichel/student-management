package pl.edu.wit.studentManagement.view.fragments;

import pl.edu.wit.studentManagement.exceptions.ValidationException;
import pl.edu.wit.studentManagement.service.ServiceFactory;
import pl.edu.wit.studentManagement.service.StudentGroupService;
import pl.edu.wit.studentManagement.service.StudentService;
import pl.edu.wit.studentManagement.service.StudentGroupSubjectAssignmentService;
import pl.edu.wit.studentManagement.service.SubjectService;
import pl.edu.wit.studentManagement.service.dto.student.StudentDto;
import pl.edu.wit.studentManagement.service.dto.studentGroup.StudentGroupDto;
import pl.edu.wit.studentManagement.service.dto.studentGroup.UpdateStudentGroupDto;
import pl.edu.wit.studentManagement.service.dto.studentGroupSubjectAssignment.CreateStudentGroupSubjectAssignmentDto;
import pl.edu.wit.studentManagement.service.dto.studentGroupSubjectAssignment.StudentGroupSubjectAssignmentDto;
import pl.edu.wit.studentManagement.service.dto.subject.SubjectDto;
import pl.edu.wit.studentManagement.translations.Translator;
import pl.edu.wit.studentManagement.view.dialogs.AddGroupDialog;
import pl.edu.wit.studentManagement.view.dialogs.AssignStudentToGroupDialog;
import pl.edu.wit.studentManagement.view.dialogs.AssignSubjectToGroupDialog;

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

    private JList<String> groupSubjectsList;
    private DefaultListModel<String> groupSubjectsListModel;
    private List<SubjectDto> groupSubjects;

    private final StudentGroupService studentGroupService = ServiceFactory.getStudentGroupService();
    private final StudentService studentService = ServiceFactory.getStudentService();
    private final StudentGroupSubjectAssignmentService groupSubjectAssignmentService = ServiceFactory.getStudentGroupSubjectAssignmentService();
    private final SubjectService subjectService = ServiceFactory.getSubjectService();

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

        JButton addButton = new JButton(Translator.translate("studentGroup.add"));
        JButton removeButton = new JButton(Translator.translate("studentGroup.delete"));

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
        detailsPanel.setBorder(BorderFactory.createTitledBorder(Translator.translate("studentGroup.details")));

        detailsPanel.add(new JLabel(Translator.translate("studentGroup.code") + ":"));
        codeField = new JTextField();
        codeField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 28));
        codeField.setAlignmentX(Component.LEFT_ALIGNMENT);
        detailsPanel.add(codeField);

        detailsPanel.add(Box.createVerticalStrut(8));

        detailsPanel.add(new JLabel(Translator.translate("studentGroup.specialization") + ":"));
        specializationField = new JTextField();
        specializationField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 28));
        specializationField.setAlignmentX(Component.LEFT_ALIGNMENT);
        detailsPanel.add(specializationField);

        detailsPanel.add(Box.createVerticalStrut(8));

        detailsPanel.add(new JLabel(Translator.translate("studentGroup.details") + ":"));
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

        saveButton = new JButton(Translator.translate("save.changes"));
        saveButton.setAlignmentX(Component.LEFT_ALIGNMENT);
        detailsPanel.add(saveButton);

        detailsPanel.add(Box.createVerticalStrut(16));

        JPanel splitVerticalPanel = new JPanel(new GridLayout(2, 1, 0, 8));
        splitVerticalPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JPanel studentsPanel = new JPanel();
        studentsPanel.setLayout(new BoxLayout(studentsPanel, BoxLayout.Y_AXIS));
        studentsPanel.add(new JLabel(Translator.translate("studentGroup.assignedStudents") + ":"));
        groupStudents = new java.util.ArrayList<>();
        groupStudentsTableModel = new GroupStudentsTableModel(groupStudents);
        groupStudentsTable = new JTable(groupStudentsTableModel);
        groupStudentsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        groupStudentsTable.setRowHeight(24);
        JScrollPane studentsScroll = new JScrollPane(groupStudentsTable);
        studentsScroll.setAlignmentX(Component.LEFT_ALIGNMENT);
        studentsPanel.add(studentsScroll);

        var studentsActionsPanel = new JPanel();
        studentsActionsPanel.setLayout(new BoxLayout(studentsActionsPanel, BoxLayout.X_AXIS));
        studentsActionsPanel.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
        studentsActionsPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        var assignStudentButton = new JButton(Translator.translate("studentGroup.assignStudentToGroup"));
        assignStudentButton.setAlignmentX(Component.LEFT_ALIGNMENT);
        assignStudentButton.addActionListener(e -> handleAssignStudentsToGroup());
        studentsActionsPanel.add(assignStudentButton);

        studentsActionsPanel.add(Box.createHorizontalStrut(8));

        var removeStudentButton = new JButton(Translator.translate("studentGroup.removeStudentFromGroup"));
        removeStudentButton.setAlignmentX(Component.LEFT_ALIGNMENT);
        removeStudentButton.addActionListener(e -> handleRemoveStudentFromGroup());
        studentsActionsPanel.add(removeStudentButton);

        studentsPanel.add(studentsActionsPanel);

        JPanel subjectsPanel = new JPanel();
        subjectsPanel.setLayout(new BoxLayout(subjectsPanel, BoxLayout.Y_AXIS));
        subjectsPanel.add(new JLabel(Translator.translate("studentGroup.assignedSubjects") + ":"));
        groupSubjects = new java.util.ArrayList<>();
        groupSubjectsListModel = new DefaultListModel<>();
        groupSubjectsList = new JList<>(groupSubjectsListModel);
        groupSubjectsList.setVisibleRowCount(5);
        JScrollPane subjectsScroll = new JScrollPane(groupSubjectsList);
        subjectsScroll.setAlignmentX(Component.LEFT_ALIGNMENT);
        subjectsPanel.add(subjectsScroll);

        var subjectActionsPanel = new JPanel();
        subjectActionsPanel.setLayout(new BoxLayout(subjectActionsPanel, BoxLayout.X_AXIS));
        subjectActionsPanel.setBorder(BorderFactory.createEmptyBorder(0, 8, 0, 8));
        subjectActionsPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        var assignSubjectButton = new JButton(Translator.translate("studentGroup.assignSubjectToGroup"));
        assignSubjectButton.setAlignmentX(Component.LEFT_ALIGNMENT);
        assignSubjectButton.addActionListener(e -> handleAssignSubjectToGroup());
        subjectActionsPanel.add(assignSubjectButton);

        subjectActionsPanel.add(Box.createHorizontalStrut(8));

        var removeSubjectButton = new JButton(Translator.translate("studentGroup.removeSubjectFromGroup"));
        removeSubjectButton.setAlignmentX(Component.LEFT_ALIGNMENT);
        removeSubjectButton.addActionListener(e -> handleRemoveSubjectFromGroup());
        subjectActionsPanel.add(removeSubjectButton);

        subjectsPanel.add(subjectActionsPanel);

        splitVerticalPanel.add(studentsPanel);
        splitVerticalPanel.add(subjectsPanel);

        detailsPanel.add(splitVerticalPanel);

        return detailsPanel;
    }

    private void clearDetails() {
        codeField.setText("");
        specializationField.setText("");
        descriptionArea.setText("");
        setDetailsEditable(false);
        groupStudents.clear();
        groupSubjects.clear();
        if (groupStudentsTableModel != null) groupStudentsTableModel.fireTableDataChanged();
        if (groupSubjectsListModel != null) groupSubjectsListModel.clear();
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
        if (!added) return;
        reloadGroups();
    }

    private void handleDeleteGroup() {
        int selectedRow = groupsTable.getSelectedRow();
        if (selectedRow == -1) return;
        var group = groups.get(selectedRow);
        int result = JOptionPane.showConfirmDialog(
                panel,
                Translator.translate("confirm.group.deletion"),
                Translator.translate("confirm.deletion.title"),
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE
        );
        if (result != JOptionPane.YES_OPTION) return;
        try {
            studentGroupService.delete(group.getId());
            reloadGroups();
            clearDetails();
        } catch (ValidationException ex) {
            JOptionPane.showMessageDialog(panel, Translator.translate(ex.getMessageKey()), Translator.translate("error"), JOptionPane.ERROR_MESSAGE);
        }
    }

    private void handleGroupSelected() {
        int selectedRow = groupsTable.getSelectedRow();
        if (selectedRow == -1) {
            clearDetails();
            return;
        }
        StudentGroupDto group = groups.get(selectedRow);
        codeField.setText(group.getCode());
        specializationField.setText(group.getSpecialization());
        descriptionArea.setText(group.getDescription());
        setDetailsEditable(true);
        reloadGroupStudents(group.getId());
        reloadGroupSubjects(group.getId());
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

    private void reloadGroupSubjects(java.util.UUID groupId) {
        groupSubjects.clear();
        groupSubjectsListModel.clear();
        List<StudentGroupSubjectAssignmentDto> assignments = groupSubjectAssignmentService.getAssignmentsByStudentGroup(groupId);
        List<SubjectDto> allSubjects = subjectService.getAllSubjects();
        for (StudentGroupSubjectAssignmentDto assignment : assignments) {
            for (SubjectDto subject : allSubjects) {
                if (subject.getId().equals(assignment.getSubjectId())) {
                    groupSubjects.add(subject);
                    groupSubjectsListModel.addElement(subject.getName());
                    break;
                }
            }
        }
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
        } catch (ValidationException ex) {
            JOptionPane.showMessageDialog(panel, Translator.translate(ex.getMessageKey()), Translator.translate("error"), JOptionPane.ERROR_MESSAGE);
        }
    }

    private void handleAssignStudentsToGroup() {
        int selectedGroupRow = groupsTable.getSelectedRow();
        if (selectedGroupRow == -1) {
            JOptionPane.showMessageDialog(panel, Translator.translate("studentGroup.notSelected"), Translator.translate("error"), JOptionPane.ERROR_MESSAGE);
            return;
        }
        AssignStudentToGroupDialog dialog = new AssignStudentToGroupDialog();
        var result = dialog.showDialog(panel);
        if (result == null)
            return;

        StudentGroupDto group = groups.get(selectedGroupRow);
        for (var studentId : result.studentIds) {
            try {
                studentService.assignStudentToGroup(studentId, group.getId());
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(panel, Translator.translate(ex.getMessage()), Translator.translate("error"), JOptionPane.ERROR_MESSAGE);
            }
        }
        reloadGroupStudents(group.getId());
    }

    private void handleRemoveStudentFromGroup() {
        int selectedStudentRow = groupStudentsTable.getSelectedRow();

        if (selectedStudentRow == -1) {
            JOptionPane.showMessageDialog(panel, Translator.translate("student.notSelected"), Translator.translate("error"), JOptionPane.ERROR_MESSAGE);
            return;
        }

        var student = groupStudents.get(selectedStudentRow);

        var result = JOptionPane.showConfirmDialog(
                panel,
                Translator.translate("confirm.removeStudentFromGroup"),
                Translator.translate("confirm.deletion.title"),
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
            JOptionPane.showMessageDialog(panel, Translator.translate(ex.getMessageKey()), Translator.translate("error"), JOptionPane.ERROR_MESSAGE);
        }
    }

    private void handleAssignSubjectToGroup() {
        int selectedGroupRow = groupsTable.getSelectedRow();
        if (selectedGroupRow == -1) {
            JOptionPane.showMessageDialog(panel, Translator.translate("studentGroup.notSelected"), Translator.translate("error"), JOptionPane.ERROR_MESSAGE);
            return;
        }
        var group = groups.get(selectedGroupRow);

        var allSubjects = subjectService.getAllSubjects();
        allSubjects.removeIf(subject -> groupSubjects.stream().anyMatch(s -> s.getId().equals(subject.getId())));

        if (allSubjects.isEmpty()) {
            JOptionPane.showMessageDialog(panel, Translator.translate("studentGroup.noSubjectsToAssign"), Translator.translate("information"), JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        SubjectDto selectedSubject = AssignSubjectToGroupDialog.showDialog(panel, allSubjects);
        if (selectedSubject != null) {
            try {
                var dto = new CreateStudentGroupSubjectAssignmentDto(
                        group.getId(), selectedSubject.getId()
                );
                groupSubjectAssignmentService.createAssignment(dto);
                reloadGroupSubjects(group.getId());
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(panel, Translator.translate(ex.getMessage()), Translator.translate("error"), JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void handleRemoveSubjectFromGroup() {
        int selectedGroupRow = groupsTable.getSelectedRow();
        int selectedSubjectIndex = groupSubjectsList.getSelectedIndex();

        if (selectedGroupRow == -1) {
            JOptionPane.showMessageDialog(panel, Translator.translate("studentGroup.notSelected"), Translator.translate("error"), JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (selectedSubjectIndex == -1) {
            JOptionPane.showMessageDialog(panel, Translator.translate("subject.notSelected"), Translator.translate("error"), JOptionPane.ERROR_MESSAGE);
            return;
        }

        var group = groups.get(selectedGroupRow);
        var subject = groupSubjects.get(selectedSubjectIndex);

        var assignments = groupSubjectAssignmentService.getAssignmentsByStudentGroup(group.getId());
        var assignment = assignments.stream()
                .filter(a -> a.getSubjectId().equals(subject.getId()))
                .findFirst();

        if (assignment.isPresent()) {
            int result = JOptionPane.showConfirmDialog(
                    panel,
                    Translator.translate("studentGroup.confirmRemoveSubjectFromGroup"),
                    Translator.translate("confirm.deletion.title"),
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE
            );
            if (result != JOptionPane.YES_OPTION)
                return;

            groupSubjectAssignmentService.deleteAssignment(assignment.get().getId());
            reloadGroupSubjects(group.getId());
        }
    }

    private void reloadGroups() {
        groups.clear();
        groups.addAll(studentGroupService.getAll());
        groupsTableModel.fireTableDataChanged();
    }

    private static class GroupsTableModel extends AbstractTableModel {
        private final String[] columnNames = {
                Translator.translate("studentGroup.code"),
                Translator.translate("studentGroup.specialization"),
                Translator.translate("studentGroup.description")
        };
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
        private final String[] columns = {Translator.translate("first.name"), Translator.translate("last.name"), Translator.translate("student.id")};
        private final List<StudentDto> students;

        public GroupStudentsTableModel(List<StudentDto> students) {
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