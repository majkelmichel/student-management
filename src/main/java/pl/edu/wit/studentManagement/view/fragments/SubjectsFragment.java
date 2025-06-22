package pl.edu.wit.studentManagement.view.fragments;

import pl.edu.wit.studentManagement.exceptions.ValidationException;
import pl.edu.wit.studentManagement.service.ServiceFactory;
import pl.edu.wit.studentManagement.service.SubjectService;
import pl.edu.wit.studentManagement.service.dto.gradeCriterion.CreateGradeCriterionDto;
import pl.edu.wit.studentManagement.service.dto.gradeCriterion.GradeCriterionDto;
import pl.edu.wit.studentManagement.service.dto.subject.CreateSubjectDto;
import pl.edu.wit.studentManagement.service.dto.subject.SubjectDto;
import pl.edu.wit.studentManagement.service.dto.subject.SubjectWithGradeCriteriaDto;
import pl.edu.wit.studentManagement.service.dto.subject.UpdateSubjectDto;
import pl.edu.wit.studentManagement.view.dialogs.AddGradeCriterionDialog;

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

    private static final SubjectService subjectService = ServiceFactory.getSubjectService();

    public SubjectsFragment() {
        panel = new JPanel(new BorderLayout());

        List<SubjectDto> subjects = subjectService.getAllSubjects();

        var leftPanel = createLeftPanel(subjects);
        var rightPanel = createDetailsPanel();

        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, leftPanel, rightPanel);
        splitPane.setResizeWeight(0.8);

        panel.add(splitPane, BorderLayout.CENTER);

        setDetailsEditable(false); // domyślnie zablokowane pole
    }

    private JPanel createLeftPanel(List<SubjectDto> subjects) {
        JPanel leftPanel = new JPanel(new BorderLayout(4, 4));

        listModel = new DefaultListModel<>();
        for (SubjectDto s : subjects) {
            listModel.addElement(s);
        }

        subjectsList = new JList<>(listModel);
        subjectsList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        subjectsList.setCellRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                SubjectDto subject = (SubjectDto) value;
                JLabel label = (JLabel) super.getListCellRendererComponent(list, subject.getName(), index, isSelected, cellHasFocus);
                label.setBorder(BorderFactory.createEmptyBorder(6, 12, 6, 12));
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
        var actionsPanel = new JPanel();
        actionsPanel.setLayout(new BoxLayout(actionsPanel, BoxLayout.X_AXIS));
        actionsPanel.setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));

        JButton addButton = new JButton("Dodaj przedmiot");
        JButton removeButton = new JButton("Usuń przedmiot");

        actionsPanel.add(addButton);
        actionsPanel.add(Box.createHorizontalStrut(8));
        actionsPanel.add(removeButton);

        addButton.addActionListener(e -> handleAddSubject());
        removeButton.addActionListener(e -> handleDeleteSubject());

        return actionsPanel;
    }

    private JPanel createDetailsPanel() {
        JPanel detailsPanel = new JPanel();
        detailsPanel.setLayout(new BoxLayout(detailsPanel, BoxLayout.Y_AXIS));
        detailsPanel.setBorder(BorderFactory.createTitledBorder("Szczegóły przedmiotu"));

        renderSubjectNameField(detailsPanel);
        renderCriteriaSection(detailsPanel);

        return detailsPanel;
    }

    private void renderSubjectNameField(JPanel panel) {
        panel.add(new JLabel("Nazwa przedmiotu:"));

        subjectNameField = new JTextField();
        subjectNameField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 32));
        subjectNameField.setAlignmentX(Component.LEFT_ALIGNMENT);

        panel.add(subjectNameField);

        JPanel actionsPanel = new JPanel();
        actionsPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        actionsPanel.setLayout(new BoxLayout(actionsPanel, BoxLayout.X_AXIS));
        actionsPanel.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
        panel.add(actionsPanel);

        JButton saveButton = new JButton("Zapisz zmiany");
        saveButton.addActionListener(e -> handleSaveSubject());
        actionsPanel.add(saveButton);
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
        criteriaTable.setAlignmentX(Component.LEFT_ALIGNMENT);

        JScrollPane criteriaScroll = new JScrollPane(criteriaTable);
        criteriaScroll.setPreferredSize(new Dimension(350, 120));
        criteriaScroll.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(criteriaScroll);

        JPanel criteriaActions = new JPanel();
        criteriaActions.setLayout(new BoxLayout(criteriaActions, BoxLayout.X_AXIS));
        criteriaActions.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
        criteriaActions.setAlignmentX(Component.LEFT_ALIGNMENT);

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

    private void updateDetailsPanel() {
        SubjectDto selected = subjectsList.getSelectedValue();

        if (selected != null) {
            Optional<SubjectWithGradeCriteriaDto> subjectWithGradeCriteria = subjectService.getSubjectWithGradeCriteriaById(selected.getId());

            subjectNameField.setText(selected.getName());
            setDetailsEditable(true);
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
            setDetailsEditable(false);
        }
    }

    private void setDetailsEditable(boolean editable) {
        subjectNameField.setEditable(editable);
    }

    public JPanel getPanel() {
        return panel;
    }

    private void handleAddSubject() {
        var name = JOptionPane.showInputDialog(panel, "Nazwa przedmiotu:", "Dodaj przedmiot", JOptionPane.PLAIN_MESSAGE);

        try {
            SubjectDto newSubject = subjectService.createSubject(new CreateSubjectDto(name.trim()));
            listModel.addElement(newSubject);
            subjectsList.setSelectedValue(newSubject, true);
        } catch (ValidationException ex) {
            JOptionPane.showMessageDialog(panel, ex.getMessageKey(), "Błąd", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void handleDeleteSubject() {
        int idx = subjectsList.getSelectedIndex();
        if (idx == -1)
            return;

        var result = JOptionPane.showConfirmDialog(panel, "Czy na pewno chcesz usunąć ten przedmiot?", "Usuń przedmiot", JOptionPane.YES_NO_OPTION);

        if (result != JOptionPane.YES_OPTION) {
            return;
        }

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
            JOptionPane.showMessageDialog(panel, ex.getMessageKey(), "Błąd", JOptionPane.ERROR_MESSAGE);
        }

    }

    private void handleAddCriterion(JPanel panel) {
        SubjectDto selected = subjectsList.getSelectedValue();
        if (selected == null) return;

        var res = AddGradeCriterionDialog.showDialog(panel);

        if (res == null)
            return;

        String name = res[0].trim();
        String ptsStr = res[1].trim();

        if (!name.isEmpty() && !ptsStr.isEmpty()) {
            try {
                int pts = Integer.parseInt(ptsStr);
                var newCriterion = subjectService.addCriterionToSubject(
                        selected.getId(),
                        new CreateGradeCriterionDto(name, (byte) pts)
                );
                criteriaTableModel.addRow(new Object[]{newCriterion.getName(), (int) newCriterion.getMaxPoints()});
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(panel, "Nieprawidłowa liczba punktów!", "Błąd", JOptionPane.ERROR_MESSAGE);
            } catch (ValidationException ex) {
                JOptionPane.showMessageDialog(panel, ex.getMessageKey(), "Błąd", JOptionPane.ERROR_MESSAGE);
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
                        JOptionPane.showMessageDialog(panel, ex.getMessageKey(), "Błąd", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        }
    }

    private void handleEditSubject() {
        setDetailsEditable(true);
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

                    JOptionPane.showMessageDialog(panel, "Przedmiot został zaktualizowany.", "Sukces", JOptionPane.INFORMATION_MESSAGE);

                    return;
                } catch (ValidationException ex) {
                    JOptionPane.showMessageDialog(panel, "Błąd: " + ex.getMessage(), "Błąd", JOptionPane.ERROR_MESSAGE);
                }
            }

            setDetailsEditable(false);
        }
    }
}
