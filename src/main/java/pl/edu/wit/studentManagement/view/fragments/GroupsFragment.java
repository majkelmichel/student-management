package pl.edu.wit.studentManagement.view.fragments;

import pl.edu.wit.studentManagement.view.dialogs.AddGroupDialog;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class GroupsFragment
{
    private final JPanel panel;
    private JTable groupsTable;
    private DefaultTableModel groupsTableModel;

    public GroupsFragment() {
        this.panel = new JPanel(new BorderLayout());

        // Mockowane dane grup
        String[] columnNames = {"Kod grupy", "Specjalizacja", "Opis"};
        Object[][] data = {
                {"GR01", "Informatyka", "Grupa projektowa 1"},
                {"GR02", "Automatyka", "Grupa laboratoryjna 2"},
                {"GR03", "Elektronika", "Grupa ćwiczeniowa 3"}
        };

        groupsTableModel = new DefaultTableModel(data, columnNames) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        groupsTable = new JTable(groupsTableModel);
        groupsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        groupsTable.setRowHeight(28);

        JScrollPane scrollPane = new JScrollPane(groupsTable);
        panel.add(scrollPane, BorderLayout.CENTER);

        // Panel dolny z przyciskami
        JPanel addPanel = new JPanel();
        addPanel.setLayout(new BoxLayout(addPanel, BoxLayout.X_AXIS));
        addPanel.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12)); // padding

        JButton addButton = new JButton("Dodaj grupę");
        JButton removeButton = new JButton("Usuń grupę");

        addPanel.add(Box.createHorizontalGlue());
        addPanel.add(addButton);
        addPanel.add(Box.createHorizontalStrut(8));
        addPanel.add(removeButton);

        // Obsługa dodawania grupy przez dialog
        addButton.addActionListener(e -> {
            AddGroupDialog dialog = new AddGroupDialog();
            AddGroupDialog.GroupData groupData = dialog.showDialog(panel);
            if (groupData != null) {
                groupsTableModel.addRow(new Object[]{groupData.code, groupData.specialization, groupData.description});
            }
        });

        // Obsługa usuwania grupy
        removeButton.addActionListener(e -> {
            int selectedRow = groupsTable.getSelectedRow();
            if (selectedRow != -1) {
                groupsTableModel.removeRow(selectedRow);
            }
        });

        panel.add(addPanel, BorderLayout.SOUTH);
    }

    public JPanel getPanel() {
        return panel;
    }
}
