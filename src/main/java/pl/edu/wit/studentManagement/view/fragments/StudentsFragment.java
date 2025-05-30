package pl.edu.wit.studentManagement.view.fragments;

import pl.edu.wit.studentManagement.entities.Student;
import pl.edu.wit.studentManagement.view.AppWindow;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class StudentsFragment {
    private final JPanel panel;
    private JList<Student> studentsList;
    private JLabel idLabel, firstNameLabel, lastNameLabel, albumLabel;

    // Czcionki używane w panelu
    private static final Font TITLE_FONT = new JLabel().getFont().deriveFont(Font.BOLD, 18f);
    private static final Font LABEL_FONT = new JLabel().getFont().deriveFont(Font.PLAIN, 14f);
    private static final Font LIST_FONT = new JLabel().getFont().deriveFont(Font.PLAIN, 15f);

    public StudentsFragment() {
        panel = new JPanel(new BorderLayout());
        panel.add(createHeaderPanel(), BorderLayout.NORTH);

        List<Student> students = mockStudents();

        var leftPanel = createLeftPanel(students);
        var rightPanel = createDetailsPanel();

        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, leftPanel, rightPanel);
        splitPane.setDividerLocation(300);

        panel.add(splitPane, BorderLayout.CENTER);
    }

    private JPanel createHeaderPanel() {
        JPanel headerPanel = new JPanel(new BorderLayout());
        JButton backButton = new JButton("← Powrót");
        backButton.addActionListener(e -> AppWindow.navigateToDashboard());
        JLabel titleLabel = new JLabel("Studenci");
        titleLabel.setFont(TITLE_FONT);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        headerPanel.add(backButton, BorderLayout.WEST);
        headerPanel.add(titleLabel, BorderLayout.CENTER);
        headerPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        return headerPanel;
    }

    private List<Student> mockStudents() {
        List<Student> students = new ArrayList<>();
        students.add(new Student(1, "Jan", "Kowalski", "12345"));
        students.add(new Student(2, "Anna", "Nowak", "23456"));
        students.add(new Student(3, "Piotr", "Wiśniewski", "34567"));
        return students;
    }

    private JPanel createLeftPanel(List<Student> students) {
        JPanel leftPanel = new JPanel(new BorderLayout(4, 4));
        leftPanel.add(createSearchPanel(), BorderLayout.NORTH);

        DefaultListModel<Student> listModel = new DefaultListModel<>();
        for (Student s : students) {
            listModel.addElement(s);
        }

        studentsList = new JList<>(listModel);
        studentsList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        studentsList.setFont(LIST_FONT);
        studentsList.setCellRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                Student student = (Student) value;
                JLabel label = (JLabel) super.getListCellRendererComponent(list, student.getFirstName() + " " + student.getLastName(), index, isSelected, cellHasFocus);
                label.setBorder(BorderFactory.createEmptyBorder(6, 12, 6, 12));
                label.setFont(LIST_FONT);
                return label;
            }
        });

        studentsList.addListSelectionListener(e -> updateDetailsPanel());

        leftPanel.add(new JScrollPane(studentsList), BorderLayout.CENTER);
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
        // Możesz dodać obsługę filtrowania tutaj, jeśli chcesz
        return searchPanel;
    }

    private JPanel createDetailsPanel() {
        JPanel rightPanel = new JPanel();
        rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));
        idLabel = new JLabel();
        idLabel.setFont(LABEL_FONT);
        firstNameLabel = new JLabel();
        firstNameLabel.setFont(LABEL_FONT);
        lastNameLabel = new JLabel();
        lastNameLabel.setFont(LABEL_FONT);
        albumLabel = new JLabel();
        albumLabel.setFont(LABEL_FONT);
        rightPanel.add(idLabel);
        rightPanel.add(firstNameLabel);
        rightPanel.add(lastNameLabel);
        rightPanel.add(albumLabel);
        rightPanel.setBorder(BorderFactory.createTitledBorder("Szczegóły studenta"));
        return rightPanel;
    }

    private void updateDetailsPanel() {
        Student selected = studentsList.getSelectedValue();
        if (selected != null) {
            idLabel.setText("ID: " + selected.getId());
            firstNameLabel.setText("Imię: " + selected.getFirstName());
            lastNameLabel.setText("Nazwisko: " + selected.getLastName());
            albumLabel.setText("Album: " + selected.getAlbum());
        } else {
            idLabel.setText("");
            firstNameLabel.setText("");
            lastNameLabel.setText("");
            albumLabel.setText("");
        }
    }

    public JPanel getPanel() {
        return panel;
    }
}

