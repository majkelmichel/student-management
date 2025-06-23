package pl.edu.wit.studentManagement.view.dialogs;

import pl.edu.wit.studentManagement.exceptions.ValidationException;
import pl.edu.wit.studentManagement.service.ServiceFactory;
import pl.edu.wit.studentManagement.service.StudentGroupService;
import pl.edu.wit.studentManagement.service.dto.studentGroup.CreateStudentGroupDto;
import pl.edu.wit.studentManagement.translations.Translator;

import javax.swing.*;
import java.awt.*;

/**
 * Dialog for creating a new student group with code, specialization, and description.
 *
 * @author Wojciech Berdowski
 */
public class AddGroupDialog {
    private final StudentGroupService groupService = ServiceFactory.getStudentGroupService();

    public boolean showDialog(Component parent) {
        JTextField codeField = new JTextField();
        JTextField specField = new JTextField();
        JTextField descField = new JTextField();

        // Ustaw szerokość pól
        Dimension fieldDim = new Dimension(220, 28);
        codeField.setPreferredSize(fieldDim);
        specField.setPreferredSize(fieldDim);
        descField.setPreferredSize(fieldDim);

        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(4, 4, 4, 4);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(new JLabel(Translator.translate("studentGroup.code") + ":"), gbc);
        gbc.gridx = 1;
        panel.add(codeField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(new JLabel(Translator.translate("studenGroup.specialization") + ":"), gbc);
        gbc.gridx = 1;
        panel.add(specField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        panel.add(new JLabel(Translator.translate("description") + ":"), gbc);
        gbc.gridx = 1;
        panel.add(descField, gbc);

        while (true) {
            int result = JOptionPane.showConfirmDialog(parent, panel, Translator.translate("studentGroup.add"), JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

            if (result == JOptionPane.OK_OPTION) {
                String code = codeField.getText().trim();
                String specialization = specField.getText().trim();
                String description = descField.getText().trim();

                CreateStudentGroupDto dto = new CreateStudentGroupDto(
                        code,
                        specialization,
                        description
                );

                try {
                    groupService.create(dto);
                    return true;
                } catch (ValidationException ex) {
                    JOptionPane.showMessageDialog(panel, Translator.translate(ex.getMessageKey()), Translator.translate("error"), JOptionPane.ERROR_MESSAGE);
                }
            } else {
                return false;
            }
        }
    }
}