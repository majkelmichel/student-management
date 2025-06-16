package pl.edu.wit.studentManagement.view.dialogs;

import javax.swing.*;
import java.awt.*;

public class AddStudentDialog {
    public static class StudentData {
        public final String firstName;
        public final String lastName;
        public final String album;

        public StudentData(String firstName, String lastName, String album) {
            this.firstName = firstName;
            this.lastName = lastName;
            this.album = album;
        }
    }

    public StudentData showDialog(Component parent) {
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

        gbc.gridx = 0; gbc.gridy = 0;
        panel.add(new JLabel("Imię:"), gbc);
        gbc.gridx = 1;
        panel.add(firstNameField, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        panel.add(new JLabel("Nazwisko:"), gbc);
        gbc.gridx = 1;
        panel.add(lastNameField, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        panel.add(new JLabel("Nr albumu:"), gbc);
        gbc.gridx = 1;
        panel.add(albumField, gbc);

        int result = JOptionPane.showConfirmDialog(parent, panel, "Dodaj studenta", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (result == JOptionPane.OK_OPTION) {
            String firstName = firstNameField.getText().trim();
            String lastName = lastNameField.getText().trim();
            String album = albumField.getText().trim();
            if (!firstName.isEmpty() && !lastName.isEmpty() && !album.isEmpty()) {
                return new StudentData(firstName, lastName, album);
            }
        }
        return null;
    }
}
