package pl.edu.wit.studentManagement.view.dialogs;

import pl.edu.wit.studentManagement.service.dto.gradeCriterion.GradeCriterionDto;

import javax.swing.*;
import java.awt.*;
import java.util.List;
/**
 * Dialog for assigning a grade to a student for a selected criterion.
 * Author: Wojciech Berdowski
 */
public class AssignGradeDialog {
    private final String studentName;
    private final List<GradeCriterionDto> criteria;
    private byte grade;
    private GradeCriterionDto selectedCriterion;

    public AssignGradeDialog(String studentName, List<GradeCriterionDto> criteria) {
        this.studentName = studentName;
        this.criteria = criteria;
    }

    public byte getGrade() {
        return grade;
    }

    public GradeCriterionDto getSelectedCriterion() {
        return selectedCriterion;
    }

    public boolean showDialog(Component parent) {
        JPanel dialogPanel = new JPanel();
        dialogPanel.setLayout(new BoxLayout(dialogPanel, BoxLayout.Y_AXIS));

        JLabel studentLabel = new JLabel("Student: " + studentName);
        studentLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        dialogPanel.add(studentLabel);

        dialogPanel.add(Box.createVerticalStrut(8));

        JLabel criterionLabel = new JLabel("Kryterium:");
        criterionLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        dialogPanel.add(criterionLabel);

        JComboBox<GradeCriterionDto> criteriaCombo = new JComboBox<>(criteria.toArray(new GradeCriterionDto[0]));
        criteriaCombo.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof GradeCriterionDto) {
                    setText(((GradeCriterionDto) value).getName());
                } else {
                    setText("");
                }
                return this;
            }
        });
        criteriaCombo.setAlignmentX(Component.LEFT_ALIGNMENT);
        dialogPanel.add(criteriaCombo);

        JLabel rangeLabel = new JLabel();
        GradeCriterionDto initialCriterion = (GradeCriterionDto) criteriaCombo.getSelectedItem();
        if (initialCriterion != null) {
            rangeLabel.setText("Zakres punktów: 0 - " + initialCriterion.getMaxPoints());
        } else {
            rangeLabel.setText("Zakres punktów: -");
        }
        rangeLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        dialogPanel.add(Box.createVerticalStrut(4));
        dialogPanel.add(rangeLabel);

        criteriaCombo.addActionListener(e -> {
            GradeCriterionDto selected = (GradeCriterionDto) criteriaCombo.getSelectedItem();
            if (selected != null) {
                rangeLabel.setText("Zakres punktów: 0 - " + selected.getMaxPoints());
            } else {
                rangeLabel.setText("Zakres punktów: -");
            }
        });

        dialogPanel.add(Box.createVerticalStrut(8));

        JLabel gradeLabel = new JLabel("Punkty:");
        gradeLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        dialogPanel.add(gradeLabel);

        JTextField gradeField = new JTextField();
        gradeField.setAlignmentX(Component.LEFT_ALIGNMENT);
        dialogPanel.add(gradeField);

        while (true) {
            int result = JOptionPane.showConfirmDialog(parent, dialogPanel, "Ustaw ocenę", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
            if (result != JOptionPane.OK_OPTION) {
                return false;
            }
            GradeCriterionDto selected = (GradeCriterionDto) criteriaCombo.getSelectedItem();
            String gradeText = gradeField.getText().trim();
            if (selected == null) {
                JOptionPane.showMessageDialog(parent, "Wybierz kryterium.", "Błąd", JOptionPane.ERROR_MESSAGE);
                continue;
            }
            if (gradeText.isEmpty()) {
                JOptionPane.showMessageDialog(parent, "Podaj ocenę.", "Błąd", JOptionPane.ERROR_MESSAGE);
                continue;
            }
            try {
                byte parsedGrade = Byte.parseByte(gradeText);
                if (parsedGrade < 0 || parsedGrade > selected.getMaxPoints()) {
                    JOptionPane.showMessageDialog(parent, "Ocena musi być w zakresie 0-" + selected.getMaxPoints(), "Błąd", JOptionPane.ERROR_MESSAGE);
                    continue;
                }
                this.grade = parsedGrade;
                this.selectedCriterion = selected;
                return true;
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(parent, "Ocena musi być liczbą.", "Błąd", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
