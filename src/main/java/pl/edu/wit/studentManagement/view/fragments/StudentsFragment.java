package pl.edu.wit.studentManagement.view.fragments;

import pl.edu.wit.studentManagement.exceptions.ValidationException;
import pl.edu.wit.studentManagement.service.ServiceFactory;
import pl.edu.wit.studentManagement.service.StudentService;
import pl.edu.wit.studentManagement.service.dto.student.StudentDto;
import pl.edu.wit.studentManagement.service.dto.student.CreateStudentDto;
import pl.edu.wit.studentManagement.service.dto.student.UpdateStudentDto;
import pl.edu.wit.studentManagement.view.dialogs.AddStudentDialog;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.List;
import javax.swing.table.DefaultTableModel;
import java.util.UUID;

public class StudentsFragment {
    private final JPanel panel;
    private JTable studentsTable;
    private JTextField firstNameField, lastNameField, albumField;
    private DefaultTableModel tableModel;
    private final StudentService studentService = ServiceFactory.getStudentService();
    private List<StudentDto> currentStudents;
    private JTextField searchField;

    public StudentsFragment() {
        panel = new JPanel(new BorderLayout());

        currentStudents = studentService.getAllStudents();

        var leftPanel = createLeftPanel();
        var rightPanel = createDetailsPanel();

        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, leftPanel, rightPanel);
        splitPane.setResizeWeight(0.8);

        panel.add(splitPane, BorderLayout.CENTER);

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

        String[] columnNames = {"Imię", "Nazwisko", "Nr albumu"};
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

        JButton addButton = new JButton("Dodaj studenta");
        JButton removeButton = new JButton("Usuń studenta");

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
        JLabel searchLabel = new JLabel("Szukaj:");
        searchLabel.setBorder(BorderFactory.createEmptyBorder(2, 8, 2, 8));
        searchField = new JTextField();
        Dimension searchFieldSize = new Dimension(Integer.MAX_VALUE, 32);
        searchField.setPreferredSize(searchFieldSize);
        searchField.setMinimumSize(searchFieldSize);
        searchPanel.add(searchLabel, BorderLayout.WEST);
        searchPanel.add(searchField, BorderLayout.CENTER);

        searchField.addActionListener(e -> handleSearch());
        searchField.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            public void insertUpdate(javax.swing.event.DocumentEvent e) { handleSearch(); }
            public void removeUpdate(javax.swing.event.DocumentEvent e) { handleSearch(); }
            public void changedUpdate(javax.swing.event.DocumentEvent e) { handleSearch(); }
        });

        return searchPanel;
    }

    private JPanel createDetailsPanel() {
        JPanel detailsPanel = new JPanel();
        detailsPanel.setLayout(new BoxLayout(detailsPanel, BoxLayout.Y_AXIS));
        detailsPanel.setBorder(BorderFactory.createTitledBorder("Szczegóły studenta"));

        detailsPanel.add(new JLabel("Imię:"));

        firstNameField = new JTextField();
        firstNameField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 32));
        firstNameField.setAlignmentX(Component.LEFT_ALIGNMENT);
        detailsPanel.add(firstNameField);

        detailsPanel.add(new JLabel("Nazwisko:"));

        lastNameField = new JTextField();
        lastNameField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 32));
        lastNameField.setAlignmentX(Component.LEFT_ALIGNMENT);
        detailsPanel.add(lastNameField);

        detailsPanel.add(new JLabel("Album:"));
        albumField = new JTextField();
        albumField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 32));
        albumField.setAlignmentX(Component.LEFT_ALIGNMENT);
        detailsPanel.add(albumField);

        detailsPanel.add(Box.createVerticalStrut(16));

        JPanel groupButtonsPanel = new JPanel();
        groupButtonsPanel.setLayout(new BoxLayout(groupButtonsPanel, BoxLayout.X_AXIS));
        groupButtonsPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JButton saveButton = new JButton("Zapisz zmiany");
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
        if (studentsTable == null) {
            return;
        }

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

    // --- Handlery przycisków ---

    private void handleAddStudentButton(ActionEvent e) {
        AddStudentDialog dialog = new AddStudentDialog();
        AddStudentDialog.StudentData studentData = dialog.showDialog(panel);
        if (studentData != null) {
            try {
                studentService.createStudent(new CreateStudentDto(
                        studentData.firstName,
                        studentData.lastName,
                        studentData.album
                ));
                fetchStudents();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(panel, "Błąd dodawania studenta: " + ex.getMessage(), "Błąd", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void handleRemoveStudentButton(ActionEvent e) {
        int selectedRow = studentsTable.getSelectedRow();
        if (selectedRow != -1 && selectedRow < currentStudents.size()) {
            int result = JOptionPane.showConfirmDialog(
                    panel,
                    "Czy na pewno usunąć studenta?",
                    "Potwierdzenie usunięcia",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE
            );
            if (result == JOptionPane.YES_OPTION) {
                UUID id = currentStudents.get(selectedRow).getId();
                try {
                    studentService.deleteStudent(id);
                    fetchStudents();
                } catch (ValidationException ex) {
                    JOptionPane.showMessageDialog(panel, ex.getMessageKey(), "Błąd", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }

    private void handleSaveStudentButton() {
        int selectedRow = studentsTable.getSelectedRow();

        if (selectedRow != -1 && selectedRow < currentStudents.size()) {
            StudentDto selected = currentStudents.get(selectedRow);
            UpdateStudentDto updateDto = new UpdateStudentDto();
            updateDto.setFirstName(firstNameField.getText());
            updateDto.setLastName(lastNameField.getText());
            updateDto.setAlbum(albumField.getText());
            try {
                studentService.updateStudent(selected.getId(), updateDto);

                fetchStudents();

                studentsTable.setRowSelectionInterval(selectedRow, selectedRow);
                updateDetailsPanel();

                JOptionPane.showMessageDialog(panel, "Zmiany zostały zapisane.", "Informacja", JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(panel, "Błąd zapisu: " + ex.getMessage(), "Błąd", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void handleSearch() {
        String query = searchField.getText();
        if (query == null || query.isBlank()) {
            currentStudents = studentService.getAllStudents();
        } else {
            currentStudents = studentService.search(query);
        }
        refreshTableData(currentStudents);
    }

    private void fetchStudents() {
        String query = searchField.getText();
        if (query == null || query.isBlank()) {
            currentStudents = studentService.getAllStudents();
        } else {
            currentStudents = studentService.search(query);
        }
        refreshTableData(currentStudents);
    }

    private void refreshTableData(List<StudentDto> students) {
        tableModel.setRowCount(0);
        for (StudentDto s : students) {
            tableModel.addRow(new Object[]{s.getFirstName(), s.getLastName(), s.getAlbum()});
        }
        updateDetailsPanel();
    }
}
