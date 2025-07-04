package pl.edu.wit.studentManagement.view.fragments;

import pl.edu.wit.studentManagement.exceptions.ValidationException;
import pl.edu.wit.studentManagement.service.ServiceFactory;
import pl.edu.wit.studentManagement.service.SubjectService;
import pl.edu.wit.studentManagement.service.dto.gradeCriterion.CreateGradeCriterionDto;
import pl.edu.wit.studentManagement.service.dto.gradeCriterion.GradeCriterionDto;
import pl.edu.wit.studentManagement.service.dto.gradeCriterion.UpdateGradeCriterionDto;
import pl.edu.wit.studentManagement.service.dto.subject.CreateSubjectDto;
import pl.edu.wit.studentManagement.service.dto.subject.SubjectDto;
import pl.edu.wit.studentManagement.service.dto.subject.UpdateSubjectDto;
import pl.edu.wit.studentManagement.translations.Translator;
import pl.edu.wit.studentManagement.view.AppWindow;
import pl.edu.wit.studentManagement.view.dialogs.AddGradeCriterionDialog;
import pl.edu.wit.studentManagement.view.dialogs.EditGradeCriterionDialog;
import pl.edu.wit.studentManagement.view.interfaces.Fragment;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

/**
 * Fragment for managing subjects in the application
 *
 * @author Wojciech Berdowski
 */
public class SubjectsFragment implements Fragment {
    private final JPanel panel;
    private JList<SubjectDto> subjectsList;
    private DefaultListModel<SubjectDto> listModel;
    private JTextField subjectNameField;
    private JTable criteriaTable;
    private DefaultTableModel criteriaTableModel;

    private static final SubjectService subjectService = ServiceFactory.getSubjectService();

    public SubjectsFragment() {
        panel = new JPanel(new BorderLayout());

        var leftPanel = createLeftPanel();
        var rightPanel = createDetailsPanel();

        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, leftPanel, rightPanel);
        splitPane.setResizeWeight(0.8);

        panel.add(splitPane, BorderLayout.CENTER);

        reloadSubjects();

        setDetailsEditable(false);
    }

    private void reloadSubjects() {
        AppWindow.threadPool.submit(() -> {
            List<SubjectDto> subjects = subjectService.getAllSubjects();

            SwingUtilities.invokeLater(() -> {
                listModel.clear();

                if (subjects == null || subjects.isEmpty()) {
                    subjectsList.setSelectedIndex(-1);
                    return;
                }

                for (SubjectDto s : subjects) {
                    listModel.addElement(s);
                }

                if (!listModel.isEmpty()) {
                    subjectsList.setSelectedIndex(0);
                }
            });
        });
    }

    private JPanel createLeftPanel() {
        JPanel leftPanel = new JPanel(new BorderLayout(4, 4));

        listModel = new DefaultListModel<>();

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

        JButton addButton = new JButton(Translator.translate("subject.add"));
        JButton removeButton = new JButton(Translator.translate("subject.delete"));

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
        detailsPanel.setBorder(BorderFactory.createTitledBorder(Translator.translate("subject.details")));

        renderSubjectNameField(detailsPanel);
        renderCriteriaSection(detailsPanel);

        return detailsPanel;
    }

    private void renderSubjectNameField(JPanel panel) {
        panel.add(new JLabel(Translator.translate("subject.name") + ":"));

        subjectNameField = new JTextField();
        subjectNameField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 32));
        subjectNameField.setAlignmentX(Component.LEFT_ALIGNMENT);

        panel.add(subjectNameField);

        JPanel actionsPanel = new JPanel();
        actionsPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        actionsPanel.setLayout(new BoxLayout(actionsPanel, BoxLayout.X_AXIS));
        actionsPanel.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
        panel.add(actionsPanel);

        JButton saveButton = new JButton(Translator.translate("save.changes"));
        saveButton.addActionListener(e -> handleSaveSubject());
        actionsPanel.add(saveButton);
    }

    private void renderCriteriaSection(JPanel panel) {
        panel.add(new JLabel(Translator.translate("grading.criteria") + ":"));

        String[] colNames = {Translator.translate("gradeCriterion.name"), Translator.translate("max.points")};
        criteriaTableModel = new DefaultTableModel(colNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }

            @Override
            public Class<?> getColumnClass(int columnIndex) {
                return columnIndex == 1 ? Integer.class : String.class;
            }
        };
        criteriaTable = new JTable(criteriaTableModel);
        criteriaTable.setRowHeight(28);
        criteriaTable.setAlignmentX(Component.LEFT_ALIGNMENT);

        JScrollPane criteriaScroll = new JScrollPane(criteriaTable);
        criteriaScroll.setAlignmentX(Component.LEFT_ALIGNMENT);
        criteriaScroll.setMaximumSize(new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE));
        criteriaScroll.setPreferredSize(null);
        panel.add(criteriaScroll);
        panel.add(Box.createVerticalGlue());

        JPanel criteriaActions = new JPanel();
        criteriaActions.setLayout(new BoxLayout(criteriaActions, BoxLayout.X_AXIS));
        criteriaActions.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
        criteriaActions.setAlignmentX(Component.LEFT_ALIGNMENT);

        JButton addCriterionBtn = new JButton(Translator.translate("gradeCriterion.add"));
        JButton editCriterionBtn = new JButton(Translator.translate("gradeCriterion.edit"));
        JButton removeCriterionBtn = new JButton(Translator.translate("gradeCriterion.delete"));

        criteriaActions.add(addCriterionBtn);
        criteriaActions.add(Box.createHorizontalStrut(8));
        criteriaActions.add(editCriterionBtn);
        criteriaActions.add(Box.createHorizontalStrut(8));
        criteriaActions.add(removeCriterionBtn);

        panel.add(Box.createVerticalStrut(8));
        panel.add(criteriaActions);

        addCriterionBtn.addActionListener(e -> handleAddCriterion(panel));
        editCriterionBtn.addActionListener(e -> handleEditCriterion(panel));
        removeCriterionBtn.addActionListener(e -> handleRemoveCriterion());

        criteriaTable.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2 && criteriaTable.getSelectedRow() != -1) {
                    handleEditCriterion(panel);
                }
            }
        });

    }

    private void updateDetailsPanel() {
        SubjectDto selected = subjectsList.getSelectedValue();
        if (selected == null) {
            subjectNameField.setText("");
            criteriaTableModel.setRowCount(0);
            setDetailsEditable(false);
            return;
        }
        var subjectWithGradeCriteria = subjectService.getSubjectWithGradeCriteriaById(selected.getId());
        subjectNameField.setText(selected.getName());
        setDetailsEditable(true);
        criteriaTableModel.setRowCount(0);
        if (subjectWithGradeCriteria.isPresent()) {
            var criteria = subjectWithGradeCriteria.get().getGradeCriteria();
            for (GradeCriterionDto c : criteria) {
                criteriaTableModel.addRow(new Object[]{c.getName(), (int) c.getMaxPoints()});
            }
        }
    }

    private void setDetailsEditable(boolean editable) {
        subjectNameField.setEditable(editable);
    }

    public JPanel getPanel() {
        return panel;
    }

    private void handleAddSubject() {
        var name = JOptionPane.showInputDialog(panel, Translator.translate("subject.name"), Translator.translate("subject.add"), JOptionPane.PLAIN_MESSAGE);

        if (name == null) {
            return;
        }
        try {
            SubjectDto newSubject = subjectService.createSubject(new CreateSubjectDto(name.trim()));
            listModel.addElement(newSubject);
            subjectsList.setSelectedValue(newSubject, true);
        } catch (ValidationException ex) {
            JOptionPane.showMessageDialog(panel, Translator.translate(ex.getMessageKey()), Translator.translate("error"), JOptionPane.ERROR_MESSAGE);
        }
    }

    private void handleDeleteSubject() {
        int idx = subjectsList.getSelectedIndex();
        if (idx == -1)
            return;

        var result = JOptionPane.showConfirmDialog(panel, Translator.translate("confirm.subject.deletion"), Translator.translate("subject.delete"), JOptionPane.YES_NO_OPTION);
        if (result != JOptionPane.YES_OPTION)
            return;

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
            JOptionPane.showMessageDialog(panel, Translator.translate(ex.getMessageKey()), Translator.translate("error"), JOptionPane.ERROR_MESSAGE);
        }

    }

    private void handleAddCriterion(JPanel panel) {
        SubjectDto selected = subjectsList.getSelectedValue();
        if (selected == null)
            return;

        var res = AddGradeCriterionDialog.showDialog(panel);
        if (res == null)
            return;

        String name = res[0].trim();
        String ptsStr = res[1].trim();
        if (name.isEmpty() || ptsStr.isEmpty())
            return;

        try {
            int pts = Integer.parseInt(ptsStr);
            var newCriterion = subjectService.addCriterionToSubject(
                    selected.getId(),
                    new CreateGradeCriterionDto(name, (byte) pts)
            );
            criteriaTableModel.addRow(new Object[]{newCriterion.getName(), (int) newCriterion.getMaxPoints()});
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(panel, Translator.translate("gradeCriterion.wrongFormat"), Translator.translate("error"), JOptionPane.ERROR_MESSAGE);
        } catch (ValidationException ex) {
            JOptionPane.showMessageDialog(panel, Translator.translate(ex.getMessageKey()), Translator.translate("error"), JOptionPane.ERROR_MESSAGE);
        }
    }

    private void handleEditCriterion(JPanel panel) {
        int row = criteriaTable.getSelectedRow();
        SubjectDto selected = subjectsList.getSelectedValue();
        if (row == -1 || selected == null) {
            JOptionPane.showMessageDialog(panel, Translator.translate("gradeCriterion.notSelected"), Translator.translate("error"), JOptionPane.WARNING_MESSAGE);
            return;
        }
        var subjectWithGradeCriteria = subjectService.getSubjectWithGradeCriteriaById(selected.getId());
        if (subjectWithGradeCriteria.isEmpty())
            return;

        var criteria = subjectWithGradeCriteria.get().getGradeCriteria();
        if (row >= criteria.size())
            return;

        var criterion = criteria.get(row);
        var res = EditGradeCriterionDialog.showDialog(panel, criterion.getName(), String.valueOf(criterion.getMaxPoints()));
        if (res == null)
            return;

        String name = res[0].trim();
        String ptsStr = res[1].trim();
        if (name.isEmpty() || ptsStr.isEmpty())
            return;

        try {
            int pts = Integer.parseInt(ptsStr);
            GradeCriterionDto updated = subjectService.updateGradeCriterion(
                    criterion.getId(),
                    new UpdateGradeCriterionDto(name, (byte) pts)
            );
            criteriaTableModel.setValueAt(updated.getName(), row, 0);
            criteriaTableModel.setValueAt((int) updated.getMaxPoints(), row, 1);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(panel, Translator.translate("gradeCriterion.wrongFormat"), Translator.translate("error"), JOptionPane.ERROR_MESSAGE);
        } catch (ValidationException ex) {
            JOptionPane.showMessageDialog(panel, Translator.translate(ex.getMessageKey()), Translator.translate("error"), JOptionPane.ERROR_MESSAGE);
        }
    }

    private void handleRemoveCriterion() {
        int row = criteriaTable.getSelectedRow();
        SubjectDto selected = subjectsList.getSelectedValue();
        if (row == -1 || selected == null)
            return;

        var subjectWithGradeCriteria = subjectService.getSubjectWithGradeCriteriaById(selected.getId());
        if (subjectWithGradeCriteria.isEmpty())
            return;

        var criteria = subjectWithGradeCriteria.get().getGradeCriteria();

        if (row >= criteria.size())
            return;

        var criterion = criteria.get(row);

        try {
            boolean deleted = subjectService.deleteGradeCriterion(criterion.getId());
            if (deleted) {
                criteriaTableModel.removeRow(row);
            }
        } catch (ValidationException ex) {
            JOptionPane.showMessageDialog(panel, Translator.translate(ex.getMessageKey()), Translator.translate("error"), JOptionPane.ERROR_MESSAGE);
        }
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

                    return;
                } catch (ValidationException ex) {
                    JOptionPane.showMessageDialog(panel, Translator.translate(ex.getMessageKey()), Translator.translate("error"), JOptionPane.ERROR_MESSAGE);
                }
            }

            setDetailsEditable(false);
        }
    }
}
