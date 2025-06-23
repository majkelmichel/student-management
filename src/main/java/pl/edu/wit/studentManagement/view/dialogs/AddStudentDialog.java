package pl.edu.wit.studentManagement.view.dialogs;

import pl.edu.wit.studentManagement.exceptions.ValidationException;
import pl.edu.wit.studentManagement.service.ServiceFactory;
import pl.edu.wit.studentManagement.service.StudentService;
import pl.edu.wit.studentManagement.service.dto.student.CreateStudentDto;
import pl.edu.wit.studentManagement.translations.Translator;

import javax.swing.*;
import java.awt.*;

/**
 * Dialog for adding a new student with first name, last name, and album number.
 *
 * @author Wojciech Berdowski
 */
public class AddStudentDialog {
    private final StudentService studentService = ServiceFactory.getStudentService();

    public boolean showDialog(Component parent) {
        JTextField firstNameField = new JTextField();
        JTextField lastNameField = new JTextField();
        JTextField albumField = new JTextField();

        Dimension fieldDim = new Dimension(220, 28);
        firstNameField.setPreferredSize(fieldDim);
        lastNameField.setPreferredSize(fieldDim);
        albumField.setPreferredSize(fieldDim);

        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(4, 4, 4, 4);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(new JLabel(Translator.translate("first.name") + ":"), gbc);
        gbc.gridx = 1;
        panel.add(firstNameField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(new JLabel(Translator.translate("last.name") + ":"), gbc);
        gbc.gridx = 1;
        panel.add(lastNameField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        panel.add(new JLabel(Translator.translate("student.id") + ":"), gbc);
        gbc.gridx = 1;
        panel.add(albumField, gbc);

        while (true) {
            int result = JOptionPane.showConfirmDialog(parent, panel, Translator.translate("student.add"), JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

            if (result == JOptionPane.OK_OPTION) {
                String firstName = firstNameField.getText().trim();
                String lastName = lastNameField.getText().trim();
                String album = albumField.getText().trim();

                try {
                    studentService.createStudent(new CreateStudentDto(
                            firstName,
                            lastName,
                            album
                    ));
                    return true;
                } catch (ValidationException e) {
                    JOptionPane.showMessageDialog(parent, Translator.translate(e.getMessageKey()), Translator.translate("error"), JOptionPane.ERROR_MESSAGE);
                }
            } else {
                return false;
            }
        }
    }
}
