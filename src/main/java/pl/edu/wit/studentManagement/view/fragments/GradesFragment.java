package pl.edu.wit.studentManagement.view.fragments;

import pl.edu.wit.studentManagement.exceptions.ValidationException;
import pl.edu.wit.studentManagement.service.*;
import pl.edu.wit.studentManagement.service.dto.grade.AssignGradeDto;
import pl.edu.wit.studentManagement.service.dto.grade.GradeDto;
import pl.edu.wit.studentManagement.service.dto.gradeMatrix.GradeMatrixDto;
import pl.edu.wit.studentManagement.service.dto.gradeMatrix.GradeMatrixRowDto;
import pl.edu.wit.studentManagement.service.dto.studentGroup.StudentGroupDto;
import pl.edu.wit.studentManagement.service.dto.studentGroupSubjectAssignment.StudentGroupSubjectAssignmentDto;
import pl.edu.wit.studentManagement.service.dto.subject.SubjectDto;
import pl.edu.wit.studentManagement.service.dto.gradeCriterion.GradeCriterionDto;
import pl.edu.wit.studentManagement.translations.Translator;
import pl.edu.wit.studentManagement.view.AppWindow;
import pl.edu.wit.studentManagement.view.dialogs.AssignGradeDialog;
import pl.edu.wit.studentManagement.view.interfaces.Fragment;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.util.List;

/**
 * Fragment for managing grades in the application
 *
 * @author Wojciech Berdowski
 */
public class GradesFragment implements Fragment {
    private final JPanel panel;
    private final JComboBox<SubjectDto> subjectComboBox;
    private final JComboBox<StudentGroupDto> groupComboBox;
    private final JTable gradesTable;
    private final DefaultTableModel tableModel;

    private final GradeService gradeService = ServiceFactory.getGradeService();
    private final SubjectService subjectService = ServiceFactory.getSubjectService();
    private final StudentGroupService studentGroupService = ServiceFactory.getStudentGroupService();
    private final GradeQueryService gradeQueryService = ServiceFactory.getGradeQueryService();
    private final StudentGroupSubjectAssignmentService groupSubjectAssignmentService = ServiceFactory.getStudentGroupSubjectAssignmentService();

    public JPanel getPanel() {
        return panel;
    }

    public GradesFragment() {
        panel = new JPanel(new BorderLayout());

        JPanel selectionPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));

        groupComboBox = new JComboBox<>();
        groupComboBox.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof StudentGroupDto) {
                    var group = (StudentGroupDto) value;
                    setText(group.getCode() + " - " + group.getSpecialization());
                } else {
                    setText("");
                }
                return this;
            }
        });

        subjectComboBox = new JComboBox<>();
        subjectComboBox.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof SubjectDto) {
                    var subject = (SubjectDto) value;
                    setText(subject.getName());
                } else {
                    setText("");
                }
                return this;
            }
        });

        selectionPanel.add(new JLabel(Translator.translate("group") + ":"));
        selectionPanel.add(groupComboBox);
        selectionPanel.add(new JLabel(Translator.translate("subject") + ":"));
        selectionPanel.add(subjectComboBox);
        panel.add(selectionPanel, BorderLayout.NORTH);

        tableModel = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        gradesTable = new JTable(tableModel);
        gradesTable.setRowHeight(28);
        panel.add(new JScrollPane(gradesTable), BorderLayout.CENTER);

        JPanel actionsPanel = new JPanel();
        actionsPanel.setLayout(new BoxLayout(actionsPanel, BoxLayout.X_AXIS));
        actionsPanel.setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));
        JButton setGradeButton = new JButton(Translator.translate("grade.set"));
        actionsPanel.add(setGradeButton);
        panel.add(actionsPanel, BorderLayout.SOUTH);

        setGradeButton.addActionListener(e -> handleSetGrade());

        gradesTable.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2 && gradesTable.getSelectedRow() != -1) {
                    handleSetGrade();
                }
            }
        });

        subjectComboBox.addActionListener(e -> refreshTable());
        groupComboBox.addActionListener(e -> {
            updateSubjectsComboBox();
            refreshTable();
        });

        updateGroupsComboBox();

        refreshTable();
    }

    private void updateGroupsComboBox() {
        groupComboBox.removeAllItems();

        AppWindow.threadPool.submit(() -> {
            var groups = studentGroupService.getAll();

            SwingUtilities.invokeLater(() -> {
                for (StudentGroupDto group : groups) {
                    groupComboBox.addItem(group);
                }
                if (!groups.isEmpty()) {
                    groupComboBox.setSelectedIndex(0);
                }
            });
        });
    }

    private void updateSubjectsComboBox() {
        var selectedGroup = (StudentGroupDto) groupComboBox.getSelectedItem();

        if (selectedGroup == null) return;

        AppWindow.threadPool.submit(() -> {
            var assignments = groupSubjectAssignmentService.getAssignmentsByStudentGroup(
                    selectedGroup.getId());
            var allSubjects = subjectService.getAllSubjects();

            SwingUtilities.invokeLater(() -> {
                subjectComboBox.removeAllItems();

                for (StudentGroupSubjectAssignmentDto assignment : assignments) {
                    for (SubjectDto subject : allSubjects) {
                        if (subject.getId().equals(assignment.getSubjectId())) {
                            subjectComboBox.addItem(subject);
                        }
                    }
                }
            });
        });
    }

    private void refreshTable() {
        SubjectDto selectedSubject = (SubjectDto) subjectComboBox.getSelectedItem();
        StudentGroupDto selectedGroup = (StudentGroupDto) groupComboBox.getSelectedItem();
        if (selectedSubject == null || selectedGroup == null) {
            tableModel.setRowCount(0);
            tableModel.setColumnCount(0);
            return;
        }

        AppWindow.threadPool.submit(() -> {
            GradeMatrixDto matrix = gradeQueryService.getGradeMatrixForSubjectAndGroup(
                    selectedSubject.getId(), selectedGroup.getId());

            String[] columns = new String[1 + matrix.getCriteriaNames().size()];
            columns[0] = Translator.translate("student");
            for (int i = 0; i < matrix.getCriteriaNames().size(); i++) {
                columns[i + 1] = matrix.getCriteriaNames().get(i);
            }

            SwingUtilities.invokeLater(() -> {
                tableModel.setColumnIdentifiers(columns);

                tableModel.setRowCount(0);

                for (GradeMatrixRowDto row : matrix.getRows()) {
                    Object[] rowData = new Object[columns.length];
                    rowData[0] = row.getStudentName();
                    List<GradeDto> grades = row.getGrades();
                    for (int i = 0; i < grades.size(); i++) {
                        rowData[i + 1] = grades.get(i) != null ? grades.get(i).getGrade() : "";
                    }
                    tableModel.addRow(rowData);
                }
            });
        });
    }

    private void handleSetGrade() {
        int selectedRow = gradesTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(panel, Translator.translate("student.notSelected"), Translator.translate("information"), JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        SubjectDto selectedSubject = (SubjectDto) subjectComboBox.getSelectedItem();
        StudentGroupDto selectedGroup = (StudentGroupDto) groupComboBox.getSelectedItem();

        if (selectedSubject == null) {
            JOptionPane.showMessageDialog(panel, Translator.translate("subject.notSelected"), Translator.translate("information"), JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        if (selectedGroup == null) {
            JOptionPane.showMessageDialog(panel, Translator.translate("studentGroup.notSelected"), Translator.translate("information"), JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        var subjectWithCriteriaOpt = subjectService.getSubjectWithGradeCriteriaById(selectedSubject.getId());

        if (subjectWithCriteriaOpt.isEmpty()) {
            JOptionPane.showMessageDialog(panel, Translator.translate("gradeCriterion.notExists"), Translator.translate("error"), JOptionPane.ERROR_MESSAGE);
            return;
        }
        List<GradeCriterionDto> criteria = subjectWithCriteriaOpt.get().getGradeCriteria();
        if (criteria.isEmpty()) {
            JOptionPane.showMessageDialog(panel, Translator.translate("subject.hasNoGradeCriteria"), Translator.translate("error"), JOptionPane.ERROR_MESSAGE);
            return;
        }

        GradeMatrixDto matrix = gradeQueryService.getGradeMatrixForSubjectAndGroup(
                selectedSubject.getId(), selectedGroup.getId());

        GradeMatrixRowDto studentRow = matrix.getRows().get(selectedRow);
        String studentName = studentRow.getStudentName();

        AssignGradeDialog dialog = new AssignGradeDialog(studentName, criteria);

        boolean ok = dialog.showDialog(panel);
        if (!ok)
            return;

        try {
            if (dialog.isNoGradeSelected()) {
                GradeCriterionDto criterion = dialog.getSelectedCriterion();

                var gradeToDelete = studentRow.getGrades().stream()
                        .filter(g -> g != null && g.getGradeCriterionId().equals(criterion.getId()))
                        .findFirst();

                if (gradeToDelete.isPresent()) {
                    gradeService.deleteGrade(gradeToDelete.get().getId());
                    refreshTable();
                } else {
                    JOptionPane.showMessageDialog(panel, Translator.translate("grade.noGradeToDelete"), Translator.translate("information"), JOptionPane.INFORMATION_MESSAGE);
                }
            } else {
                gradeService.assignGrade(
                        new AssignGradeDto(
                                selectedSubject.getId(),
                                dialog.getSelectedCriterion().getId(),
                                studentRow.getStudentId(),
                                dialog.getGrade()
                        )
                );
                refreshTable();
            }
        } catch (ValidationException ex) {
            JOptionPane.showMessageDialog(panel, Translator.translate(ex.getMessageKey()), Translator.translate("error"), JOptionPane.ERROR_MESSAGE);
        }
    }
}