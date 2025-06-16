package pl.edu.wit.studentManagement.view.fragments;

import pl.edu.wit.studentManagement.entities.Student;
import pl.edu.wit.studentManagement.view.dialogs.AddStudentDialog;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.table.DefaultTableModel;
import java.util.UUID;

public class StudentsFragment {
    private final JPanel panel;
    // private JList<Student> studentsList;
    private JTable studentsTable;
    private JTextField idField, firstNameField, lastNameField, albumField;

    private static final Font LABEL_FONT = new JLabel().getFont().deriveFont(Font.PLAIN, 14f);
    private static final Font LIST_FONT = new JLabel().getFont().deriveFont(Font.PLAIN, 15f);

    public StudentsFragment() {
        panel = new JPanel(new BorderLayout());

        List<Student> students = mockStudents();

        var leftPanel = createLeftPanel(students);
        var rightPanel = createDetailsPanel();

        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, leftPanel, rightPanel);
        splitPane.setDividerLocation(600);

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
        JPanel leftPanel = new JPanel(new BorderLayout(4, 4));

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
        actionsPanel.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12)); // padding

        JButton addButton = new JButton("Dodaj studenta");
        JButton removeButton = new JButton("Usuń studenta");

        actionsPanel.add(Box.createHorizontalGlue());
        actionsPanel.add(addButton);
        actionsPanel.add(Box.createHorizontalStrut(8));
        actionsPanel.add(removeButton);

        // Obsługa dodawania studenta przez dialog
        addButton.addActionListener(e -> {
            AddStudentDialog dialog = new AddStudentDialog();
            AddStudentDialog.StudentData studentData = dialog.showDialog(panel);
            if (studentData != null) {
                tableModel.addRow(
                    new Object[]{studentData.firstName, studentData.lastName, studentData.album}
                );
            }
        });

        // Obsługa usuwania studenta z tabeli
        removeButton.addActionListener(e -> {
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
        });

        leftPanel.add(actionsPanel, BorderLayout.SOUTH);

        return leftPanel;
    }

    private JPanel createSearchPanel() {
        JPanel searchPanel = new JPanel(new BorderLayout());
        JLabel searchLabel = new JLabel("Szukaj:");
        searchLabel.setFont(LABEL_FONT);
        JTextField searchField = new JTextField();
        searchField.setFont(LABEL_FONT);
        Dimension searchFieldSize = new Dimension(Integer.MAX_VALUE, 32);
        searchField.setPreferredSize(searchFieldSize);
        searchField.setMinimumSize(searchFieldSize);
        searchPanel.add(searchLabel, BorderLayout.WEST);
        searchPanel.add(searchField, BorderLayout.CENTER);
        // Możesz dodać obsługę filtrowania tutaj, jeśli chcesz
        return searchPanel;
    }

    private JPanel createDetailsPanel() {
        JPanel rightPanel = new JPanel();
        rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));

        var deleteButton = new JButton("Usuń studenta");
        deleteButton.setFont(LABEL_FONT);
        deleteButton.setAlignmentX(Component.LEFT_ALIGNMENT);
        rightPanel.add(Box.createVerticalStrut(8));
        rightPanel.add(deleteButton);
        rightPanel.add(Box.createVerticalStrut(12));

        // --- ZAMIANA LABELI NA POLA TEKSTOWE ---
        idField = new JTextField();
        idField.setFont(LABEL_FONT);
        idField.setEditable(false);
        idField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 32));
        rightPanel.add(new JLabel("ID:"));
        rightPanel.add(idField);

        firstNameField = new JTextField();
        firstNameField.setFont(LABEL_FONT);
        firstNameField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 32));
        rightPanel.add(new JLabel("Imię:"));
        rightPanel.add(firstNameField);

        lastNameField = new JTextField();
        lastNameField.setFont(LABEL_FONT);
        lastNameField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 32));
        rightPanel.add(new JLabel("Nazwisko:"));
        rightPanel.add(lastNameField);

        albumField = new JTextField();
        albumField.setFont(LABEL_FONT);
        albumField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 32));
        rightPanel.add(new JLabel("Album:"));
        rightPanel.add(albumField);
        // --- KONIEC ZAMIANY ---

        // --- Dodane: lista grup i przyciski ---
        rightPanel.add(Box.createVerticalStrut(16));
        JLabel groupLabel = new JLabel("Grupy studenta:");
        groupLabel.setFont(LABEL_FONT);
        rightPanel.add(groupLabel);

        // Mockowana lista grup
        DefaultListModel<String> groupListModel = new DefaultListModel<>();
        groupListModel.addElement("Grupa A");
        groupListModel.addElement("Grupa B");
        groupListModel.addElement("Grupa C");
        JList<String> groupList = new JList<>(groupListModel);
        groupList.setFont(LABEL_FONT);
        groupList.setVisibleRowCount(3);
        JScrollPane groupScroll = new JScrollPane(groupList);
        groupScroll.setMaximumSize(new Dimension(Integer.MAX_VALUE, 80));
        rightPanel.add(groupScroll);

        JPanel groupButtonsPanel = new JPanel();
        groupButtonsPanel.setLayout(new BoxLayout(groupButtonsPanel, BoxLayout.X_AXIS));
        groupButtonsPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JButton assignButton = new JButton("Przypisz do grupy");
        assignButton.setFont(LABEL_FONT);

        JButton removeButton = new JButton("Wypisz z grupy");
        removeButton.setFont(LABEL_FONT);

        JButton saveButton = new JButton("Zapisz");
        saveButton.setFont(LABEL_FONT);

        groupButtonsPanel.add(assignButton);
        groupButtonsPanel.add(Box.createHorizontalStrut(12));
        groupButtonsPanel.add(removeButton);
        groupButtonsPanel.add(Box.createHorizontalStrut(12));
        groupButtonsPanel.add(saveButton);

        rightPanel.add(Box.createVerticalStrut(8));
        rightPanel.add(groupButtonsPanel);
        rightPanel.add(Box.createVerticalStrut(8));
        // --- Koniec dodanych elementów ---

        return rightPanel;
    }

    private void updateDetailsPanel() {
        int selectedRow = studentsTable.getSelectedRow();
        if (selectedRow != -1) {
            // Pobierz dane z tabeli
            String firstName = (String) studentsTable.getValueAt(selectedRow, 0);
            String lastName = (String) studentsTable.getValueAt(selectedRow, 1);
            String album = (String) studentsTable.getValueAt(selectedRow, 2);

            // Szukaj studenta po danych (możesz to zoptymalizować, jeśli masz referencję do listy)
            Student selected = null;
            for (Student s : mockStudents()) {
                if (s.getFirstName().equals(firstName) && s.getLastName().equals(lastName) && s.getAlbum().equals(album)) {
                    selected = s;
                    break;
                }
            }

            if (selected != null) {
                idField.setText(String.valueOf(selected.getId()));
                firstNameField.setText(selected.getFirstName());
                lastNameField.setText(selected.getLastName());
                albumField.setText(selected.getAlbum());
            }
        } else {
            idField.setText("");
            firstNameField.setText("");
            lastNameField.setText("");
            albumField.setText("");
        }
    }

    public JPanel getPanel() {
        return panel;
    }
}