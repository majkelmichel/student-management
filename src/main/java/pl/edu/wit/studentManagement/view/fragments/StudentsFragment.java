package pl.edu.wit.studentManagement.view.fragments;

import pl.edu.wit.studentManagement.exceptions.ValidationException;
import pl.edu.wit.studentManagement.service.ServiceFactory;
import pl.edu.wit.studentManagement.service.StudentService;
import pl.edu.wit.studentManagement.service.dto.student.StudentDto;
import pl.edu.wit.studentManagement.service.dto.student.UpdateStudentDto;
import pl.edu.wit.studentManagement.translations.Translator;
import pl.edu.wit.studentManagement.view.AppWindow;
import pl.edu.wit.studentManagement.view.dialogs.AddStudentDialog;
import pl.edu.wit.studentManagement.view.interfaces.Fragment;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;
import javax.swing.table.DefaultTableModel;
import java.util.UUID;

/**
 * Fragment for managing students in the application
 *
 * @author Wojciech Berdowski
 */
public class StudentsFragment implements Fragment {
    private final JPanel panel;
    private JTable studentsTable;
    private JTextField firstNameField, lastNameField, albumField;
    private DefaultTableModel tableModel;
    private final StudentService studentService = ServiceFactory.getStudentService();
    private List<StudentDto> currentStudents;
    private JTextField searchField;

    public StudentsFragment() {
        panel = new JPanel(new BorderLayout());

        currentStudents = new ArrayList<>();

        var leftPanel = createLeftPanel();
        var rightPanel = createDetailsPanel();

        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, leftPanel, rightPanel);
        splitPane.setResizeWeight(0.8);

        panel.add(splitPane, BorderLayout.CENTER);

        reloadStudents();

        setDetailsEditable(false);
    }

    private JPanel createLeftPanel() {
        JPanel leftPanel = new JPanel(new BorderLayout(0, 0));

        JPanel topPanel = new JPanel();
        topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.Y_AXIS));

        JPanel searchPanel = createSearchPanel();
        searchPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        topPanel.add(Box.createVerticalStrut(8));
        topPanel.add(searchPanel);
        topPanel.add(Box.createVerticalStrut(8));

        leftPanel.add(topPanel, BorderLayout.NORTH);

        String[] columnNames = {Translator.translate("first.name"), Translator.translate("last.name"), Translator.translate("student.id")};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        refreshTableData(currentStudents);

        studentsTable = new JTable(tableModel);
        studentsTable.setRowHeight(28);
        studentsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        studentsTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                updateDetailsPanel();
            }
        });

        leftPanel.add(new JScrollPane(studentsTable), BorderLayout.CENTER);

        JPanel actionsPanel = new JPanel();
        actionsPanel.setLayout(new BoxLayout(actionsPanel, BoxLayout.X_AXIS));
        actionsPanel.setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));

        JButton addButton = new JButton(Translator.translate("student.add"));
        JButton removeButton = new JButton(Translator.translate("student.delete"));

        actionsPanel.add(addButton);
        actionsPanel.add(Box.createHorizontalStrut(8));
        actionsPanel.add(removeButton);

        addButton.addActionListener(this::handleAddStudentButton);
        removeButton.addActionListener(this::handleRemoveStudentButton);

        leftPanel.add(actionsPanel, BorderLayout.SOUTH);

        return leftPanel;
    }

    private JPanel createSearchPanel() {
        JPanel searchPanel = new JPanel(new BorderLayout());
        JLabel searchLabel = new JLabel(Translator.translate("search") + ":");
        searchLabel.setBorder(BorderFactory.createEmptyBorder(2, 8, 2, 8));
        searchField = new JTextField();
        Dimension searchFieldSize = new Dimension(Integer.MAX_VALUE, 32);
        searchField.setPreferredSize(searchFieldSize);
        searchField.setMinimumSize(searchFieldSize);
        searchPanel.add(searchLabel, BorderLayout.WEST);
        searchPanel.add(searchField, BorderLayout.CENTER);

        searchField.addActionListener(e -> reloadStudents());
        searchField.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            public void insertUpdate(javax.swing.event.DocumentEvent e) {
                reloadStudents();
            }

            public void removeUpdate(javax.swing.event.DocumentEvent e) {
                reloadStudents();
            }

            public void changedUpdate(javax.swing.event.DocumentEvent e) {
                reloadStudents();
            }
        });

        return searchPanel;
    }

    private JPanel createDetailsPanel() {
        JPanel detailsPanel = new JPanel();
        detailsPanel.setLayout(new BoxLayout(detailsPanel, BoxLayout.Y_AXIS));
        detailsPanel.setBorder(BorderFactory.createTitledBorder(Translator.translate("student.details")));

        detailsPanel.add(new JLabel(Translator.translate("first.name") + ":"));

        firstNameField = new JTextField();
        firstNameField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 32));
        firstNameField.setAlignmentX(Component.LEFT_ALIGNMENT);
        detailsPanel.add(firstNameField);

        detailsPanel.add(new JLabel(Translator.translate("last.name") + ":"));

        lastNameField = new JTextField();
        lastNameField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 32));
        lastNameField.setAlignmentX(Component.LEFT_ALIGNMENT);
        detailsPanel.add(lastNameField);

        detailsPanel.add(new JLabel(Translator.translate("student.id") + ":"));
        albumField = new JTextField();
        albumField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 32));
        albumField.setAlignmentX(Component.LEFT_ALIGNMENT);
        detailsPanel.add(albumField);

        detailsPanel.add(Box.createVerticalStrut(16));

        JPanel groupButtonsPanel = new JPanel();
        groupButtonsPanel.setLayout(new BoxLayout(groupButtonsPanel, BoxLayout.X_AXIS));
        groupButtonsPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JButton saveButton = new JButton(Translator.translate("save.changes"));
        saveButton.addActionListener(e -> handleSaveStudentButton());
        groupButtonsPanel.add(saveButton);

        detailsPanel.add(Box.createVerticalStrut(8));
        detailsPanel.add(groupButtonsPanel);
        detailsPanel.add(Box.createVerticalStrut(8));

        return detailsPanel;
    }

    private void setDetailsEditable(boolean editable) {
        firstNameField.setEditable(editable);
        lastNameField.setEditable(editable);
        albumField.setEditable(editable);
    }

    private void updateDetailsPanel() {
        if (studentsTable == null)
            return;

        int selectedRow = studentsTable.getSelectedRow();

        if (selectedRow != -1 && selectedRow < currentStudents.size()) {
            StudentDto selected = currentStudents.get(selectedRow);
            firstNameField.setText(selected.getFirstName());
            lastNameField.setText(selected.getLastName());
            albumField.setText(selected.getAlbum());
            setDetailsEditable(true);
            return;
        }

        firstNameField.setText("");
        lastNameField.setText("");
        albumField.setText("");
        setDetailsEditable(false);
    }

    public JPanel getPanel() {
        return panel;
    }

    private void handleAddStudentButton(ActionEvent e) {
        AddStudentDialog dialog = new AddStudentDialog();
        var added = dialog.showDialog(panel);

        if (!added)
            return;

        reloadStudents();
    }

    private void handleRemoveStudentButton(ActionEvent e) {
        int selectedRow = studentsTable.getSelectedRow();
        if (selectedRow == -1 || selectedRow >= currentStudents.size())
            return;

        int result = JOptionPane.showConfirmDialog(
                panel,
                Translator.translate("confirm.student.deletion"),
                Translator.translate("confirm.deletion.title"),
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE
        );
        if (result != JOptionPane.YES_OPTION)
            return;

        UUID id = currentStudents.get(selectedRow).getId();
        try {
            studentService.deleteStudent(id);
            reloadStudents();
        } catch (ValidationException ex) {
            JOptionPane.showMessageDialog(panel, Translator.translate(ex.getMessageKey()), Translator.translate("error"), JOptionPane.ERROR_MESSAGE);
        }
    }

    private void handleSaveStudentButton() {
        int selectedRow = studentsTable.getSelectedRow();
        if (selectedRow == -1 || selectedRow >= currentStudents.size())
            return;

        StudentDto selected = currentStudents.get(selectedRow);
        UpdateStudentDto updateDto = new UpdateStudentDto();
        updateDto.setFirstName(firstNameField.getText());
        updateDto.setLastName(lastNameField.getText());
        updateDto.setAlbum(albumField.getText());
        try {
            studentService.updateStudent(selected.getId(), updateDto);

            UUID selectedId = selected.getId();
            reloadStudents(selectedId);

        } catch (ValidationException ex) {
            JOptionPane.showMessageDialog(panel, Translator.translate(ex.getMessageKey()), Translator.translate("error"), JOptionPane.ERROR_MESSAGE);
        }
    }

    private void reloadStudents() {
        reloadStudents(null);
    }

    private void reloadStudents(UUID studentIdToSelect) {
        String query = searchField.getText();

        AppWindow.threadPool.submit(() -> {
            if (query == null || query.isBlank()) {
                currentStudents = studentService.getAllStudents();
            } else {
                currentStudents = studentService.search(query);
            }
            SwingUtilities.invokeLater(() -> {
                refreshTableData(currentStudents);

                if (studentIdToSelect != null) {
                    int idx = -1;
                    for (int i = 0; i < currentStudents.size(); i++) {
                        if (currentStudents.get(i).getId().equals(studentIdToSelect)) {
                            idx = i;
                            break;
                        }
                    }
                    if (idx != -1) {
                        studentsTable.setRowSelectionInterval(idx, idx);
                    }
                }
            });
        });
    }

    private void refreshTableData(List<StudentDto> students) {
        tableModel.setRowCount(0);
        for (StudentDto s : students) {
            tableModel.addRow(new Object[]{s.getFirstName(), s.getLastName(), s.getAlbum()});
        }
        updateDetailsPanel();
    }
}
