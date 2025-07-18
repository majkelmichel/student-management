package pl.edu.wit.studentManagement.view.dialogs;

import pl.edu.wit.studentManagement.service.dto.gradeCriterion.GradeCriterionDto;
import pl.edu.wit.studentManagement.translations.Translator;

import javax.swing.*;
import java.awt.*;
import java.util.List;

/**
 * Dialog for assigning a grade to a student for a selected criterion.
 *
 * @author Wojciech Berdowski
 */
public class AssignGradeDialog {
    private final String studentName;
    private final List<GradeCriterionDto> criteria;
    private byte grade;
    private GradeCriterionDto selectedCriterion;
    private boolean noGradeSelected = false;

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

    public boolean isNoGradeSelected() {
        return noGradeSelected;
    }

    public boolean showDialog(Component parent) {
        JPanel dialogPanel = new JPanel();
        dialogPanel.setLayout(new BoxLayout(dialogPanel, BoxLayout.Y_AXIS));

        JLabel studentLabel = new JLabel(Translator.translate("student") + ": " + studentName);
        studentLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        dialogPanel.add(studentLabel);

        dialogPanel.add(Box.createVerticalStrut(8));

        JLabel criterionLabel = new JLabel(Translator.translate("gradeCriterion.name") + ":");
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
            rangeLabel.setText(Translator.translate("gradeCriterion.pointsRange") + " 0 - " + initialCriterion.getMaxPoints());
        } else {
            rangeLabel.setText(Translator.translate("gradeCriterion.pointsRange") + ": -");
        }
        rangeLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        dialogPanel.add(Box.createVerticalStrut(4));
        dialogPanel.add(rangeLabel);

        criteriaCombo.addActionListener(e -> {
            GradeCriterionDto selected = (GradeCriterionDto) criteriaCombo.getSelectedItem();
            if (selected != null) {
                rangeLabel.setText(Translator.translate("gradeCriterion.pointsRange") + ": 0 - " + selected.getMaxPoints());
            } else {
                rangeLabel.setText(Translator.translate("gradeCriterion.pointsRange") + ": -");
            }
        });

        dialogPanel.add(Box.createVerticalStrut(8));

        JLabel gradeLabel = new JLabel(Translator.translate("grade.points") + ":");
        gradeLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        dialogPanel.add(gradeLabel);

        JTextField gradeField = new JTextField();
        gradeField.setAlignmentX(Component.LEFT_ALIGNMENT);

        JCheckBox noGradeCheckBox = new JCheckBox(Translator.translate("grade.noGrade"));
        noGradeCheckBox.setAlignmentX(Component.LEFT_ALIGNMENT);

        noGradeCheckBox.addActionListener(e -> {
            boolean selected = noGradeCheckBox.isSelected();
            gradeField.setEnabled(!selected);

            if (selected) {
                gradeField.setText("");
            }
        });

        dialogPanel.add(gradeField);
        dialogPanel.add(Box.createVerticalStrut(4));
        dialogPanel.add(noGradeCheckBox);

        while (true) {
            int result = JOptionPane.showConfirmDialog(parent, dialogPanel, Translator.translate("grade.set"), JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
            if (result != JOptionPane.OK_OPTION) {
                return false;
            }
            GradeCriterionDto selected = (GradeCriterionDto) criteriaCombo.getSelectedItem();
            boolean noGrade = noGradeCheckBox.isSelected();
            String gradeText = gradeField.getText().trim();
            if (selected == null) {
                JOptionPane.showMessageDialog(parent, Translator.translate("grade.gradeCriterion.empty"), Translator.translate("error"), JOptionPane.ERROR_MESSAGE);
                continue;
            }
            if (noGrade) {
                this.noGradeSelected = true;
                this.selectedCriterion = selected;
                return true;
            }
            if (gradeText.isEmpty()) {
                JOptionPane.showMessageDialog(parent, Translator.translate("grade.wrongGrade"), Translator.translate("error"), JOptionPane.ERROR_MESSAGE);
                continue;
            }
            try {
                byte parsedGrade = Byte.parseByte(gradeText);
                if (parsedGrade < 0 || parsedGrade > selected.getMaxPoints()) {
                    JOptionPane.showMessageDialog(parent, Translator.translate("grade.value.outOfRange"), Translator.translate("error"), JOptionPane.ERROR_MESSAGE);
                    continue;
                }
                this.grade = parsedGrade;
                this.selectedCriterion = selected;
                this.noGradeSelected = false;
                return true;
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(parent, Translator.translate("grade.wrongFormat"), Translator.translate("error"), JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}