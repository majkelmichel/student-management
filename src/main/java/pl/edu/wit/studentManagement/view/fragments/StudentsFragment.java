package pl.edu.wit.studentManagement.view.fragments;

import pl.edu.wit.studentManagement.entities.Student;
import pl.edu.wit.studentManagement.view.dialogs.AddStudentDialog;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;
import javax.swing.table.DefaultTableModel;
import java.util.UUID;

public class StudentsFragment {
    private final JPanel panel;
    private JTable studentsTable;
    private JTextField firstNameField, lastNameField, albumField;

    private static final Font LABEL_FONT = new JLabel().getFont().deriveFont(Font.PLAIN, 14f);
    private static final Font LIST_FONT = new JLabel().getFont().deriveFont(Font.PLAIN, 15f);

    public StudentsFragment() {
        panel = new JPanel(new BorderLayout());

        List<Student> students = mockStudents();

        var leftPanel = createLeftPanel(students);
        var rightPanel = createDetailsPanel();

        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, leftPanel, rightPanel);
        splitPane.setResizeWeight(0.8);

        panel.add(splitPane, BorderLayout.CENTER);
    }

    private List<Student> mockStudents() {
        List<Student> students = new ArrayList<>();
        students.add(new Student(UUID.randomUUID(), "Jan", "Kowalski", "12345"));
        students.add(new Student(UUID.randomUUID(), "Anna", "Nowak", "23456"));
        students.add(new Student(UUID.randomUUID(), "Piotr", "Wiśniewski", "34567"));
        return students;
    }

    private JPanel createLeftPanel(List<Student> students) {
        JPanel leftPanel = new JPanel(new BorderLayout(0, 0));

        // Panel górny tylko z wyszukiwaniem
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.Y_AXIS));

        JPanel searchPanel = createSearchPanel();
        searchPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        topPanel.add(Box.createVerticalStrut(8));
        topPanel.add(searchPanel);
        topPanel.add(Box.createVerticalStrut(8));

        leftPanel.add(topPanel, BorderLayout.NORTH);

        // Model tabeli
        String[] columnNames = {"Imię", "Nazwisko", "Nr albumu"};
        Object[][] data = new Object[students.size()][3];
        for (int i = 0; i < students.size(); i++) {
            Student s = students.get(i);
            data[i][0] = s.getFirstName();
            data[i][1] = s.getLastName();
            data[i][2] = s.getAlbum();
        }

        DefaultTableModel tableModel = new DefaultTableModel(data, columnNames) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        studentsTable = new JTable(tableModel);
        studentsTable.setFont(LIST_FONT);
        studentsTable.setRowHeight(28);
        studentsTable.getTableHeader().setFont(LABEL_FONT);
        studentsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        studentsTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                updateDetailsPanel();
            }
        });

        leftPanel.add(new JScrollPane(studentsTable), BorderLayout.CENTER);

        // Panel dolny z przyciskami (jak w grupach)
        JPanel actionsPanel = new JPanel();
        actionsPanel.setLayout(new BoxLayout(actionsPanel, BoxLayout.X_AXIS));
        actionsPanel.setBorder(BorderFactory.createEmptyBorder(16,16,16,16)); // padding

        JButton addButton = new JButton("Dodaj studenta");
        JButton removeButton = new JButton("Usuń studenta");

        actionsPanel.add(addButton);
        actionsPanel.add(Box.createHorizontalStrut(8));
        actionsPanel.add(removeButton);

        // Obsługa dodawania studenta przez dialog
        addButton.addActionListener(e -> handleAddStudentButton(e, tableModel));

        // Obsługa usuwania studenta z tabeli
        removeButton.addActionListener(e -> handleRemoveStudentButton(e, tableModel));

        leftPanel.add(actionsPanel, BorderLayout.SOUTH);

        return leftPanel;
    }

    private JPanel createSearchPanel() {
        JPanel searchPanel = new JPanel(new BorderLayout());
        JLabel searchLabel = new JLabel("Szukaj:");
        searchLabel.setBorder(BorderFactory.createEmptyBorder(2, 8, 2, 8));
        searchLabel.setFont(LABEL_FONT);
        JTextField searchField = new JTextField();
        searchField.setFont(LABEL_FONT);
        Dimension searchFieldSize = new Dimension(Integer.MAX_VALUE, 32);
        searchField.setPreferredSize(searchFieldSize);
        searchField.setMinimumSize(searchFieldSize);
        searchPanel.add(searchLabel, BorderLayout.WEST);
        searchPanel.add(searchField, BorderLayout.CENTER);
        return searchPanel;
    }

    private JPanel createDetailsPanel() {
        JPanel detailsPanel = new JPanel();
        detailsPanel.setLayout(new BoxLayout(detailsPanel, BoxLayout.Y_AXIS));
        detailsPanel.setBorder(BorderFactory.createTitledBorder("Szczegóły studenta"));

        detailsPanel.add( new JLabel("Imię:"));

        firstNameField = new JTextField();
        firstNameField.setFont(LABEL_FONT);
        firstNameField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 32));
        firstNameField.setAlignmentX(Component.LEFT_ALIGNMENT);
        detailsPanel.add(firstNameField);

        detailsPanel.add(new JLabel("Nazwisko:"));

        lastNameField = new JTextField();
        lastNameField.setFont(LABEL_FONT);
        lastNameField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 32));
        lastNameField.setAlignmentX(Component.LEFT_ALIGNMENT);
        detailsPanel.add(lastNameField);

        detailsPanel.add(new JLabel("Album:"));
        albumField = new JTextField();
        albumField.setFont(LABEL_FONT);
        albumField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 32));
        albumField.setAlignmentX(Component.LEFT_ALIGNMENT);
        detailsPanel.add(albumField);

        detailsPanel.add(Box.createVerticalStrut(16));

        var groupLabel = new JLabel("Grupy studenta:", JLabel.LEFT);
        groupLabel.setFont(LABEL_FONT);
        detailsPanel.add(groupLabel);

        // Mockowana lista grup
        DefaultListModel<String> groupListModel = new DefaultListModel<>();
        groupListModel.addElement("Grupa A");
        groupListModel.addElement("Grupa B");
        groupListModel.addElement("Grupa C");

        var groupList = new JList<>(groupListModel);
        groupList.setAlignmentX(Component.LEFT_ALIGNMENT);
        groupList.setFont(LABEL_FONT);
        groupList.setVisibleRowCount(3);
        JScrollPane groupScroll = new JScrollPane(groupList);
        groupScroll.setMaximumSize(new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE));
        detailsPanel.add(groupScroll);

        JPanel groupButtonsPanel = new JPanel();
        groupButtonsPanel.setLayout(new BoxLayout(groupButtonsPanel, BoxLayout.X_AXIS));
        groupButtonsPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JButton assignButton = new JButton("Przypisz do grupy");
        groupButtonsPanel.add(assignButton);

        groupButtonsPanel.add(Box.createHorizontalStrut(12));

        JButton removeButton = new JButton("Wypisz z grupy");
        groupButtonsPanel.add(removeButton);

        groupButtonsPanel.add(Box.createHorizontalStrut(12));

        JButton saveButton = new JButton("Zapisz zmiany");
        groupButtonsPanel.add(saveButton);


        detailsPanel.add(Box.createVerticalStrut(8));
        detailsPanel.add(groupButtonsPanel);
        detailsPanel.add(Box.createVerticalStrut(8));

        return detailsPanel;
    }

    private void updateDetailsPanel() {
        int selectedRow = studentsTable.getSelectedRow();

        if (selectedRow != -1) {

            String firstName = (String) studentsTable.getValueAt(selectedRow, 0);
            String lastName = (String) studentsTable.getValueAt(selectedRow, 1);
            String album = (String) studentsTable.getValueAt(selectedRow, 2);

            Student selected = null;
            for (Student s : mockStudents()) {
                if (s.getFirstName().equals(firstName) && s.getLastName().equals(lastName) && s.getAlbum().equals(album)) {
                    selected = s;
                    break;
                }
            }

            if (selected != null) {
                firstNameField.setText(selected.getFirstName());
                lastNameField.setText(selected.getLastName());
                albumField.setText(selected.getAlbum());
            }
            return;
        }

        firstNameField.setText("");
        lastNameField.setText("");
        albumField.setText("");
    }

    public JPanel getPanel() {
        return panel;
    }

    // --- Handlery przycisków ---

    private void handleAddStudentButton(ActionEvent e, DefaultTableModel tableModel) {
        AddStudentDialog dialog = new AddStudentDialog();
        AddStudentDialog.StudentData studentData = dialog.showDialog(panel);
        if (studentData != null) {
            tableModel.addRow(
                    new Object[]{studentData.firstName, studentData.lastName, studentData.album}
            );
        }
    }

    private void handleRemoveStudentButton(ActionEvent e, DefaultTableModel tableModel) {
        int selectedRow = studentsTable.getSelectedRow();
        if (selectedRow != -1) {
            int result = JOptionPane.showConfirmDialog(
                    panel,
                    "Czy na pewno usunąć studenta?",
                    "Potwierdzenie usunięcia",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE
            );
            if (result == JOptionPane.YES_OPTION) {
                tableModel.removeRow(selectedRow);
            }
        }
    }
}
