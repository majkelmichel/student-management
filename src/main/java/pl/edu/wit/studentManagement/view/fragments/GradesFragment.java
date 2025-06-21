package pl.edu.wit.studentManagement.view.fragments;

import pl.edu.wit.studentManagement.exceptions.ValidationException;
import pl.edu.wit.studentManagement.service.*;
import pl.edu.wit.studentManagement.service.dto.grade.AssignGradeDto;
import pl.edu.wit.studentManagement.service.dto.gradeMatrix.GradeMatrixDto;
import pl.edu.wit.studentManagement.service.dto.gradeMatrix.GradeMatrixRowDto;
import pl.edu.wit.studentManagement.service.dto.studentGroup.StudentGroupDto;
import pl.edu.wit.studentManagement.service.dto.subject.SubjectDto;
import pl.edu.wit.studentManagement.service.dto.gradeCriterion.GradeCriterionDto;
import pl.edu.wit.studentManagement.view.dialogs.AssignGradeDialog;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import javax.swing.event.*;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class GradesFragment {
    private final JPanel panel;
    private final JComboBox<SubjectDto> subjectComboBox;
    private final JComboBox<StudentGroupDto> groupComboBox;
    private final JTable gradesTable;
    private final DefaultTableModel tableModel;
    private JButton setGradeButton;

    private final GradeService gradeService = ServiceFactory.getGradeService();
    private final SubjectService subjectService = ServiceFactory.getSubjectService();
    private final StudentGroupService studentGroupService = ServiceFactory.getStudentGroupService();
    private final GradeQueryService gradeQueryService = ServiceFactory.getGradeQueryService();

    public JPanel getPanel() {
        return panel;
    }

    public GradesFragment() {
        panel = new JPanel(new BorderLayout());

        JPanel selectionPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        subjectComboBox = new JComboBox<>(subjectService.getAllSubjects().toArray(new SubjectDto[0]));
        groupComboBox = new JComboBox<>(studentGroupService.getAll().toArray(new StudentGroupDto[0]));

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

        selectionPanel.add(new JLabel("Przedmiot:"));
        selectionPanel.add(subjectComboBox);
        selectionPanel.add(new JLabel("Grupa:"));
        selectionPanel.add(groupComboBox);
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

        // Dodaj pasek z przyciskiem na dole
        JPanel actionsPanel = new JPanel();
        actionsPanel.setLayout(new BoxLayout(actionsPanel, BoxLayout.X_AXIS));
        actionsPanel.setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));
        setGradeButton = new JButton("Ustaw ocenę");
        actionsPanel.add(setGradeButton);
        panel.add(actionsPanel, BorderLayout.SOUTH);

        setGradeButton.addActionListener(e -> handleSetGrade());

        // Dwuklik w tabeli
        gradesTable.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2 && gradesTable.getSelectedRow() != -1) {
                    handleSetGrade();
                }
            }
        });

        subjectComboBox.addActionListener(e -> refreshTable());
        groupComboBox.addActionListener(e -> refreshTable());

        refreshTable();
    }

    private void refreshTable() {
        SubjectDto selectedSubject = (SubjectDto) subjectComboBox.getSelectedItem();
        StudentGroupDto selectedGroup = (StudentGroupDto) groupComboBox.getSelectedItem();
        if (selectedSubject == null || selectedGroup == null) {
            tableModel.setRowCount(0);
            tableModel.setColumnCount(0);
            return;
        }

        GradeMatrixDto matrix = gradeQueryService.getGradeMatrixForSubjectAndGroup(
                selectedSubject.getId(), selectedGroup.getId());

        String[] columns = new String[1 + matrix.getCriteriaNames().size()];
        columns[0] = "Nazwa studenta";
        for (int i = 0; i < matrix.getCriteriaNames().size(); i++) {
            columns[i + 1] = matrix.getCriteriaNames().get(i);
        }
        tableModel.setColumnIdentifiers(columns);

        tableModel.setRowCount(0);

        for (GradeMatrixRowDto row : matrix.getRows()) {
            Object[] rowData = new Object[columns.length];
            rowData[0] = row.getStudentName();
            List<Byte> grades = row.getGrades();
            for (int i = 0; i < grades.size(); i++) {
                rowData[i + 1] = grades.get(i) != null ? grades.get(i) : "";
            }
            tableModel.addRow(rowData);
        }
    }

    private void handleSetGrade() {
        int selectedRow = gradesTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(panel, "Wybierz studenta z listy.", "Informacja", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        SubjectDto selectedSubject = (SubjectDto) subjectComboBox.getSelectedItem();
        StudentGroupDto selectedGroup = (StudentGroupDto) groupComboBox.getSelectedItem();
        if (selectedSubject == null || selectedGroup == null) {
            JOptionPane.showMessageDialog(panel, "Wybierz przedmiot i grupę.", "Informacja", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        // Pobierz kryteria ocen z SubjectService
        var subjectWithCriteriaOpt = subjectService.getSubjectWithGradeCriteriaById(selectedSubject.getId());
        if (subjectWithCriteriaOpt.isEmpty()) {
            JOptionPane.showMessageDialog(panel, "Nie znaleziono kryteriów ocen dla przedmiotu.", "Błąd", JOptionPane.ERROR_MESSAGE);
            return;
        }
        List<GradeCriterionDto> criteria = subjectWithCriteriaOpt.get().getGradeCriteria();
        if (criteria.isEmpty()) {
            JOptionPane.showMessageDialog(panel, "Brak kryteriów ocen dla tego przedmiotu.", "Błąd", JOptionPane.ERROR_MESSAGE);
            return;
        }

        GradeMatrixDto matrix = gradeQueryService.getGradeMatrixForSubjectAndGroup(
                selectedSubject.getId(), selectedGroup.getId());

        GradeMatrixRowDto studentRow = matrix.getRows().get(selectedRow);
        String studentName = studentRow.getStudentName();

        // Użycie nowego dialogu
        AssignGradeDialog dialog = new AssignGradeDialog(studentName, criteria);

        boolean ok = dialog.showDialog(panel);
        if (ok) {
            try {
                gradeService.assignGrade(
                    new AssignGradeDto(
                        selectedSubject.getId(),
                        dialog.getSelectedCriterion().getId(),
                        studentRow.getStudentId(),
                        dialog.getGrade()
                    )
                );
                refreshTable();
            } catch (ValidationException ex) {
                JOptionPane.showMessageDialog(panel, ex.getMessageKey(), "Błąd", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
