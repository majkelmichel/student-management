package pl.edu.wit.studentManagement.view.fragments;

import pl.edu.wit.studentManagement.entities.Subject;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class SubjectsFragment {
    private final JPanel panel;
    private JList<Subject> subjectsList;
    private JLabel idLabel, subjectNameLabel;

    // Czcionki używane w panelu
    private static final Font LABEL_FONT = new JLabel().getFont().deriveFont(Font.PLAIN, 14f);
    private static final Font LIST_FONT = new JLabel().getFont().deriveFont(Font.PLAIN, 15f);

    public SubjectsFragment() {
        panel = new JPanel(new BorderLayout());

        List<Subject> subjects = mockSubjects();

        var leftPanel = createLeftPanel(subjects);
        var rightPanel = createDetailsPanel();

        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, leftPanel, rightPanel);
        splitPane.setDividerLocation(300);

        panel.add(splitPane, BorderLayout.CENTER);
    }

    private List<Subject> mockSubjects() {
        List<Subject> subjects = new ArrayList<>();
        subjects.add(new Subject("Matematyka", new ArrayList<>()));
        subjects.add(new Subject("Informatyka", new ArrayList<>()));
        subjects.add(new Subject("Język angielski", new ArrayList<>()));
        subjects.add(new Subject("Matematyka dyskretna", new ArrayList<>()));

        return subjects;
    }

    private JPanel createLeftPanel(List<Subject> subjects) {
        JPanel leftPanel = new JPanel(new BorderLayout(4, 4));
        leftPanel.add(createSearchPanel(), BorderLayout.NORTH);

        DefaultListModel<Subject> listModel = new DefaultListModel<>();
        for (Subject s : subjects) {
            listModel.addElement(s);
        }

        subjectsList = new JList<>(listModel);
        subjectsList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        subjectsList.setFont(LIST_FONT);
        subjectsList.setCellRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                Subject subject = (Subject) value;
                JLabel label = (JLabel) super.getListCellRendererComponent(list, subject.getName(), index, isSelected, cellHasFocus);
                label.setBorder(BorderFactory.createEmptyBorder(6, 12, 6, 12));
                label.setFont(LIST_FONT);
                return label;
            }
        });

        subjectsList.addListSelectionListener(e -> updateDetailsPanel());

        leftPanel.add(new JScrollPane(subjectsList), BorderLayout.CENTER);
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
        subjectNameLabel = new JLabel();
        subjectNameLabel.setFont(LABEL_FONT);
        rightPanel.add(idLabel);
        rightPanel.add(subjectNameLabel);
        rightPanel.setBorder(BorderFactory.createTitledBorder("Szczegóły przedmiotu"));
        return rightPanel;
    }

    private void updateDetailsPanel() {
        Subject selected = subjectsList.getSelectedValue();
        if (selected != null) {
            idLabel.setText("ID: " + selected.getId());
            subjectNameLabel.setText("Nazwa: " + selected.getName());
        } else {
            idLabel.setText("");
            subjectNameLabel.setText("");
        }
    }

    public JPanel getPanel() {
        return panel;
    }
}

