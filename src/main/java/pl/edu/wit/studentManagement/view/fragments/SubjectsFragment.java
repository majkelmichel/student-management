package pl.edu.wit.studentManagement.view.fragments;

import pl.edu.wit.studentManagement.entities.Subject;
import pl.edu.wit.studentManagement.entities.GradeCriterion;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class SubjectsFragment {
    private final JPanel panel;
    private JList<Subject> subjectsList;
    private DefaultListModel<Subject> listModel;
    private JTextField subjectNameField;
    private JTable criteriaTable;
    private DefaultTableModel criteriaTableModel;

    private static final Font LABEL_FONT = new JLabel().getFont().deriveFont(Font.PLAIN, 14f);
    private static final Font LIST_FONT = new JLabel().getFont().deriveFont(Font.PLAIN, 15f);

    public SubjectsFragment() {
        panel = new JPanel(new BorderLayout());

        List<Subject> subjects = mockSubjects();

        var leftPanel = createLeftPanel(subjects);
        var rightPanel = createDetailsPanel();

        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, leftPanel, rightPanel);
        splitPane.setResizeWeight(0.8);

        panel.add(splitPane, BorderLayout.CENTER);
    }

    private List<Subject> mockSubjects() {
        List<Subject> subjects = new ArrayList<>();
        List<GradeCriterion> javaCriteria = new ArrayList<>();
        javaCriteria.add(new GradeCriterion("Kolokwium 1", (byte) 20));
        javaCriteria.add(new GradeCriterion("Kolokwium 2", (byte) 40));
        javaCriteria.add(new GradeCriterion("Projekt", (byte) 40));
        subjects.add(new Subject("Język Java", javaCriteria));
        subjects.add(new Subject("Matematyka", new ArrayList<>()));
        subjects.add(new Subject("Informatyka", new ArrayList<>()));
        return subjects;
    }

    private JPanel createLeftPanel(List<Subject> subjects) {
        JPanel leftPanel = new JPanel(new BorderLayout(4, 4));
        leftPanel.add(createSearchPanel(), BorderLayout.NORTH);

        listModel = new DefaultListModel<>();
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

        JPanel actionsPanel = createActionsPanel();

        leftPanel.add(actionsPanel, BorderLayout.SOUTH);

        return leftPanel;
    }

    private JPanel createActionsPanel() {
        var actionsPanel  = new JPanel();
        actionsPanel.setLayout(new BoxLayout(actionsPanel, BoxLayout.X_AXIS));
        actionsPanel.setBorder(BorderFactory.createEmptyBorder(16,16,16,16));

        JButton addButton = new JButton("Dodaj przedmiot");
        JButton removeButton = new JButton("Usuń przedmiot");

        actionsPanel.add(addButton);
        actionsPanel.add(Box.createHorizontalStrut(8));
        actionsPanel.add(removeButton);

        addButton.addActionListener(e -> handleAddSubject());
        removeButton.addActionListener(e -> handleRemoveSubject());

        return actionsPanel;
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
        detailsPanel.setBorder(BorderFactory.createTitledBorder("Szczegóły przedmiotu"));

        renderSubjectNameField(detailsPanel);
        renderCriteriaSection(detailsPanel);
        renderActionsPanel(detailsPanel);

        return detailsPanel;
    }

    private void renderSubjectNameField(JPanel panel) {
        panel.add(new JLabel("Nazwa przedmiotu:"));
        subjectNameField = new JTextField();
        subjectNameField.setFont(LABEL_FONT);
        subjectNameField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 32));
        panel.add(subjectNameField);
        panel.add(Box.createVerticalStrut(12));
    }

    private void renderCriteriaSection(JPanel panel) {
        panel.add(new JLabel("Kryteria oceniania:"));

        String[] colNames = {"Nazwa kryterium", "Maks. liczba punktów"};
        criteriaTableModel = new DefaultTableModel(colNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return true;
            }
            @Override
            public Class<?> getColumnClass(int columnIndex) {
                return columnIndex == 1 ? Integer.class : String.class;
            }
        };
        criteriaTable = new JTable(criteriaTableModel);
        criteriaTable.setRowHeight(28);
        criteriaTable.setFont(LABEL_FONT);
        criteriaTable.getTableHeader().setFont(LABEL_FONT);

        JScrollPane criteriaScroll = new JScrollPane(criteriaTable);
        criteriaScroll.setPreferredSize(new Dimension(350, 120));
        panel.add(criteriaScroll);

        JPanel criteriaActions = new JPanel();
        criteriaActions.setLayout(new BoxLayout(criteriaActions, BoxLayout.X_AXIS));
        JButton addCriterionBtn = new JButton("Dodaj kryterium");
        JButton removeCriterionBtn = new JButton("Usuń kryterium");
        criteriaActions.add(addCriterionBtn);
        criteriaActions.add(Box.createHorizontalStrut(8));
        criteriaActions.add(removeCriterionBtn);
        panel.add(Box.createVerticalStrut(8));
        panel.add(criteriaActions);

        addCriterionBtn.addActionListener(e -> handleAddCriterion(panel));
        removeCriterionBtn.addActionListener(e -> handleRemoveCriterion());
    }

    private void renderActionsPanel(JPanel panel) {
        JPanel actionsPanel = new JPanel();
        actionsPanel.setLayout(new BoxLayout(actionsPanel, BoxLayout.X_AXIS));
        actionsPanel.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));

        JButton saveButton = new JButton("Zapisz zmiany");
        JButton editButton = new JButton("Edytuj przedmiot");

        actionsPanel.add(Box.createHorizontalGlue());
        actionsPanel.add(editButton);
        actionsPanel.add(Box.createHorizontalStrut(8));
        actionsPanel.add(saveButton);

        editButton.addActionListener(e -> handleEditSubject());
        saveButton.addActionListener(e -> handleSaveSubject());

        panel.add(Box.createVerticalStrut(16));
        panel.add(actionsPanel);
    }

    private void updateDetailsPanel() {
        Subject selected = subjectsList.getSelectedValue();
        if (selected != null) {
            subjectNameField.setText(selected.getName());
            subjectNameField.setEditable(false);
            criteriaTableModel.setRowCount(0);
            if (selected.getGradeCriteria() != null) {
                for (GradeCriterion c : selected.getGradeCriteria()) {
                    criteriaTableModel.addRow(new Object[]{c.getName(), (int) c.getMaxPoints()});
                }
            }
        } else {
            subjectNameField.setText("");
            criteriaTableModel.setRowCount(0);
        }
    }

    public JPanel getPanel() {
        return panel;
    }

    // --- Handlery przycisków ---
    private void handleAddSubject() {
        String name = JOptionPane.showInputDialog(panel, "Nazwa przedmiotu:", "Dodaj przedmiot", JOptionPane.PLAIN_MESSAGE);
        if (name != null && !name.trim().isEmpty()) {
            Subject newSubject = new Subject(name.trim(), new ArrayList<GradeCriterion>());
            listModel.addElement(newSubject);
            subjectsList.setSelectedValue(newSubject, true);
        }
    }

    private void handleRemoveSubject() {
        int idx = subjectsList.getSelectedIndex();
        if (idx != -1) {
            listModel.remove(idx);
            if (!listModel.isEmpty()) {
                subjectsList.setSelectedIndex(Math.min(idx, listModel.size() - 1));
            }
        }
    }

    private void handleAddCriterion(JPanel panel) {
        JTextField nameField = new JTextField();
        JTextField maxPointsField = new JTextField();
        JPanel dialogPanel = new JPanel(new GridLayout(2, 2, 4, 4));
        dialogPanel.add(new JLabel("Nazwa kryterium:"));
        dialogPanel.add(nameField);
        dialogPanel.add(new JLabel("Maks. liczba punktów:"));
        dialogPanel.add(maxPointsField);
        int res = JOptionPane.showConfirmDialog(panel, dialogPanel, "Dodaj kryterium", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (res == JOptionPane.OK_OPTION) {
            String name = nameField.getText().trim();
            String ptsStr = maxPointsField.getText().trim();
            if (!name.isEmpty() && !ptsStr.isEmpty()) {
                try {
                    int pts = Integer.parseInt(ptsStr);
                    criteriaTableModel.addRow(new Object[]{name, pts});
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(panel, "Nieprawidłowa liczba punktów!", "Błąd", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }

    private void handleRemoveCriterion() {
        int row = criteriaTable.getSelectedRow();
        if (row != -1) {
            criteriaTableModel.removeRow(row);
        }
    }

    private void handleEditSubject() {
        subjectNameField.setEditable(true);
        subjectNameField.requestFocus();
    }

    private void handleSaveSubject() {
        Subject selected = subjectsList.getSelectedValue();
        if (selected != null) {
            selected.setName(subjectNameField.getText().trim());
            List<GradeCriterion> newCriteria = new ArrayList<>();
            for (int i = 0; i < criteriaTableModel.getRowCount(); i++) {
                String critName = (String) criteriaTableModel.getValueAt(i, 0);
                int maxPts = (Integer) criteriaTableModel.getValueAt(i, 1);
                newCriteria.add(new GradeCriterion(critName, (byte) maxPts));
            }
            selected.setGradeCriteria(newCriteria);
            subjectsList.repaint();
            subjectNameField.setEditable(false);
        }
    }
}
