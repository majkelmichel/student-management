package pl.edu.wit.studentManagement.view.fragments;

import pl.edu.wit.studentManagement.service.ServiceFactory;
import pl.edu.wit.studentManagement.service.SubjectService;
import pl.edu.wit.studentManagement.service.dto.gradeCriterion.CreateCriterionDto;
import pl.edu.wit.studentManagement.service.dto.gradeCriterion.GradeCriterionDto;
import pl.edu.wit.studentManagement.service.dto.subject.CreateSubjectDto;
import pl.edu.wit.studentManagement.service.dto.subject.SubjectDto;
import pl.edu.wit.studentManagement.service.dto.subject.SubjectWithGradeCriteriaDto;
import pl.edu.wit.studentManagement.service.dto.subject.UpdateSubjectDto;
import pl.edu.wit.studentManagement.validation.ValidationException;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
import java.util.Optional;

public class SubjectsFragment {
    private final JPanel panel;
    private JList<SubjectDto> subjectsList;
    private DefaultListModel<SubjectDto> listModel;
    private JTextField subjectNameField;
    private JTable criteriaTable;
    private DefaultTableModel criteriaTableModel;

    private static final Font LABEL_FONT = new JLabel().getFont().deriveFont(Font.PLAIN, 14f);
    private static final Font LIST_FONT = new JLabel().getFont().deriveFont(Font.PLAIN, 15f);

    private static final SubjectService subjectService = ServiceFactory.getSubjectService();

    public SubjectsFragment() {
        panel = new JPanel(new BorderLayout());

        List<SubjectDto> subjects = subjectService.getAllSubjects();

        var leftPanel = createLeftPanel(subjects);
        var rightPanel = createDetailsPanel();

        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, leftPanel, rightPanel);
        splitPane.setResizeWeight(0.8);

        panel.add(splitPane, BorderLayout.CENTER);
    }

    private JPanel createLeftPanel(List<SubjectDto> subjects) {
        JPanel leftPanel = new JPanel(new BorderLayout(4, 4));

        listModel = new DefaultListModel<>();
        for (SubjectDto s : subjects) {
            listModel.addElement(s);
        }

        subjectsList = new JList<>(listModel);
        subjectsList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        subjectsList.setFont(LIST_FONT);
        subjectsList.setCellRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                SubjectDto subject = (SubjectDto) value;
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
        SubjectDto selected = subjectsList.getSelectedValue();

        if (selected != null) {
            Optional<SubjectWithGradeCriteriaDto> subjectWithGradeCriteria = subjectService.getSubjectWithGradeCriteriaById(selected.getId());

            subjectNameField.setText(selected.getName());
            subjectNameField.setEditable(false);
            criteriaTableModel.setRowCount(0);

            if (subjectWithGradeCriteria.isPresent()) {
                var criteria = subjectWithGradeCriteria.get().getGradeCriteria();

                for (GradeCriterionDto c : criteria) {
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
            try {
                SubjectDto newSubject = subjectService.createSubject(new CreateSubjectDto(name.trim()));
                listModel.addElement(newSubject);
                subjectsList.setSelectedValue(newSubject, true);
            } catch (ValidationException ex) {
                JOptionPane.showMessageDialog(panel, "Błąd: " + ex.getMessage(), "Błąd", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void handleRemoveSubject() {
        int idx = subjectsList.getSelectedIndex();
        if (idx != -1) {
            SubjectDto selected = subjectsList.getSelectedValue();
            try {
                boolean deleted = subjectService.deleteSubject(selected.getId());
                if (deleted) {
                    listModel.remove(idx);
                    if (!listModel.isEmpty()) {
                        subjectsList.setSelectedIndex(Math.min(idx, listModel.size() - 1));
                    }
                }
            } catch (ValidationException ex) {
                JOptionPane.showMessageDialog(panel, "Błąd: " + ex.getMessage(), "Błąd", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void handleAddCriterion(JPanel panel) {
        SubjectDto selected = subjectsList.getSelectedValue();
        if (selected == null) return;

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
                    GradeCriterionDto newCriterion = subjectService.addCriterionToSubject(
                            selected.getId(),
                            new CreateCriterionDto(name, (byte) pts)
                    );
                    criteriaTableModel.addRow(new Object[]{newCriterion.getName(), (int) newCriterion.getMaxPoints()});
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(panel, "Nieprawidłowa liczba punktów!", "Błąd", JOptionPane.ERROR_MESSAGE);
                } catch (ValidationException ex) {
                    JOptionPane.showMessageDialog(panel, "Błąd: " + ex.getMessage(), "Błąd", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }

    private void handleRemoveCriterion() {
        int row = criteriaTable.getSelectedRow();
        SubjectDto selected = subjectsList.getSelectedValue();
        if (row != -1 && selected != null) {
            Optional<SubjectWithGradeCriteriaDto> subjectWithGradeCriteria = subjectService.getSubjectWithGradeCriteriaById(selected.getId());
            if (subjectWithGradeCriteria.isPresent()) {
                List<GradeCriterionDto> criteria = subjectWithGradeCriteria.get().getGradeCriteria();
                if (row < criteria.size()) {
                    GradeCriterionDto criterion = criteria.get(row);
                    try {
                        boolean deleted = subjectService.deleteGradeCriterion(criterion.getId());
                        if (deleted) {
                            criteriaTableModel.removeRow(row);
                        }
                    } catch (ValidationException ex) {
                        JOptionPane.showMessageDialog(panel, "Błąd: " + ex.getMessage(), "Błąd", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        }
    }

    private void handleEditSubject() {
        subjectNameField.setEditable(true);
        subjectNameField.requestFocus();
    }

    private void handleSaveSubject() {
        SubjectDto selected = subjectsList.getSelectedValue();
        if (selected != null) {
            String newName = subjectNameField.getText().trim();
            if (!newName.isEmpty() && !newName.equals(selected.getName())) {
                UpdateSubjectDto updateDto = new UpdateSubjectDto();
                updateDto.setName(newName);
                try {
                    SubjectDto updated = subjectService.updateSubject(selected.getId(), updateDto);
                    int idx = subjectsList.getSelectedIndex();
                    listModel.set(idx, updated);
                    subjectsList.setSelectedIndex(idx);
                } catch (ValidationException ex) {
                    JOptionPane.showMessageDialog(panel, "Błąd: " + ex.getMessage(), "Błąd", JOptionPane.ERROR_MESSAGE);
                }
            }
            subjectNameField.setEditable(false);
        }
    }
}
