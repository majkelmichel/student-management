package pl.edu.wit.studentManagement.view.dialogs;

import pl.edu.wit.studentManagement.service.dto.subject.SubjectDto;

import javax.swing.*;
import java.awt.*;
import java.util.List;

/**
 * Dialog for assigning a subject to a student group.
 * @author Wojciech Berdowski
 */
public class AssignSubjectToGroupDialog {
    public static SubjectDto showDialog(Component parent, List<SubjectDto> subjects) {
        JComboBox<SubjectDto> subjectComboBox = new JComboBox<>(subjects.toArray(new SubjectDto[0]));
        subjectComboBox.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof SubjectDto) {
                    setText(((SubjectDto) value).getName());
                }
                return this;
            }
        });

        JPanel panel = new JPanel(new GridLayout(0, 1));
        panel.add(new JLabel("Wybierz przedmiot:"));
        panel.add(subjectComboBox);

        int result = JOptionPane.showConfirmDialog(
                parent,
                panel,
                "Przypisz przedmiot do grupy",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE
        );

        if (result == JOptionPane.OK_OPTION && subjectComboBox.getSelectedItem() instanceof SubjectDto) {
            return (SubjectDto) subjectComboBox.getSelectedItem();
        }
        return null;
    }
}
