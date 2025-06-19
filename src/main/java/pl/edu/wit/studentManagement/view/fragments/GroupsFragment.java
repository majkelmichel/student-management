package pl.edu.wit.studentManagement.view.fragments;

import pl.edu.wit.studentManagement.entities.StudentGroup;
import pl.edu.wit.studentManagement.view.dialogs.AddGroupDialog;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class GroupsFragment
{
    private final JPanel panel;
    private JTable groupsTable;
    private GroupsTableModel groupsTableModel;

    private JTextField codeField;
    private JTextField specializationField;
    private JTextArea descriptionArea;
    private JButton saveButton;

    // Lista grup jako mockowane dane
    private List<StudentGroup> groups;

    public GroupsFragment() {
        this.panel = new JPanel(new BorderLayout());

        // Mockowane dane grup jako StudentGroup
        groups = new ArrayList<>();
        groups.add(new StudentGroup("GR01", "Informatyka", "Grupa projektowa 1", new ArrayList<>(), new ArrayList<>()));
        groups.add(new StudentGroup("GR02", "Automatyka", "Grupa laboratoryjna 2", new ArrayList<>(), new ArrayList<>()));
        groups.add(new StudentGroup("GR03", "Elektronika", "Grupa ćwiczeniowa 3", new ArrayList<>(), new ArrayList<>()));

        groupsTableModel = new GroupsTableModel(groups);
        groupsTable = new JTable(groupsTableModel);
        groupsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        groupsTable.setRowHeight(28);

        JScrollPane scrollPane = new JScrollPane(groupsTable);

        var detailsPanel = createDetailsPanel();

        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, scrollPane, detailsPanel);
        splitPane.setResizeWeight(0.8);
        panel.add(splitPane, BorderLayout.CENTER);

        var actionsPanel = new JPanel();
        actionsPanel.setLayout(new BoxLayout(actionsPanel, BoxLayout.X_AXIS));
        actionsPanel.setBorder(BorderFactory.createEmptyBorder(16,16,16,16));

        JButton addButton = new JButton("Dodaj grupę");
        JButton removeButton = new JButton("Usuń grupę");

        actionsPanel.add(addButton);
        actionsPanel.add(Box.createHorizontalStrut(8));
        actionsPanel.add(removeButton);

        // Dodawanie nowej grupy
        addButton.addActionListener(e -> handleAddGroup());

        // Usuwanie grupy
        removeButton.addActionListener(e -> handleRemoveGroup());

        // Wybór grupy
        groupsTable.getSelectionModel().addListSelectionListener(e -> handleGroupSelected());

        // Zapis zmian w szczegółach
        saveButton.addActionListener(e -> handleSaveGroup());

        setDetailsEditable(false);
        clearDetails();

        panel.add(actionsPanel, BorderLayout.SOUTH);
    }

    private JPanel createDetailsPanel() {
        var detailsPanel = new JPanel();
        detailsPanel.setLayout(new BoxLayout(detailsPanel, BoxLayout.Y_AXIS));
        detailsPanel.setBorder(BorderFactory.createTitledBorder("Szczegóły grupy"));

        detailsPanel.add(new JLabel("Kod grupy:"));
        codeField = new JTextField();
        codeField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 28));
        codeField.setAlignmentX(Component.LEFT_ALIGNMENT);
        detailsPanel.add(codeField);

        detailsPanel.add(Box.createVerticalStrut(8));

        detailsPanel.add(new JLabel("Specjalizacja:"));
        specializationField = new JTextField();
        specializationField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 28));
        specializationField.setAlignmentX(Component.LEFT_ALIGNMENT);
        detailsPanel.add(specializationField);

        detailsPanel.add(Box.createVerticalStrut(8));

        detailsPanel.add(new JLabel("Opis:"));
        descriptionArea = new JTextArea(3, 20);
        descriptionArea.setLineWrap(true);
        descriptionArea.setWrapStyleWord(true);
        descriptionArea.setMaximumSize(new Dimension(Integer.MAX_VALUE, 60));
        descriptionArea.setAlignmentX(Component.LEFT_ALIGNMENT);

        var descScroll = new JScrollPane(descriptionArea);
        descScroll.setMaximumSize(new Dimension(Integer.MAX_VALUE, 70));
        descScroll.setAlignmentX(Component.LEFT_ALIGNMENT);
        detailsPanel.add(descScroll);

        detailsPanel.add(Box.createVerticalStrut(8));

        saveButton = new JButton("Zapisz");
        saveButton.setAlignmentX(Component.LEFT_ALIGNMENT);

        detailsPanel.add(saveButton);

        return detailsPanel;
    }

    private void clearDetails() {
        codeField.setText("");
        specializationField.setText("");
        descriptionArea.setText("");
        setDetailsEditable(false);
    }

    private void setDetailsEditable(boolean editable) {
        codeField.setEditable(editable);
        specializationField.setEditable(editable);
        descriptionArea.setEditable(editable);
        saveButton.setEnabled(editable);
    }

    public JPanel getPanel() {
        return panel;
    }

    // --- Handlery przycisków i zdarzeń ---
    private void handleAddGroup() {
        AddGroupDialog dialog = new AddGroupDialog();
        AddGroupDialog.GroupData groupData = dialog.showDialog(panel);
        if (groupData != null) {
            StudentGroup newGroup = new StudentGroup(groupData.code, groupData.specialization, groupData.description, new ArrayList<>(), new ArrayList<>());
            groups.add(newGroup);
            groupsTableModel.fireTableDataChanged();
        }
    }

    private void handleRemoveGroup() {
        int selectedRow = groupsTable.getSelectedRow();
        if (selectedRow != -1) {
            groups.remove(selectedRow);
            groupsTableModel.fireTableDataChanged();
            clearDetails();
        }
    }

    private void handleGroupSelected() {
        int selectedRow = groupsTable.getSelectedRow();
        if (selectedRow != -1) {
            StudentGroup group = groups.get(selectedRow);
            codeField.setText(group.getCode());
            specializationField.setText(group.getSpecialization());
            descriptionArea.setText(group.getDescription());
            setDetailsEditable(true);
        } else {
            clearDetails();
        }
    }

    private void handleSaveGroup() {
        int selectedRow = groupsTable.getSelectedRow();
        if (selectedRow != -1) {
            StudentGroup group = groups.get(selectedRow);
            group.setCode(codeField.getText());
            group.setSpecialization(specializationField.getText());
            group.setDescription(descriptionArea.getText());
            groupsTableModel.fireTableRowsUpdated(selectedRow, selectedRow);
        }
    }

    // Model tabeli dla StudentGroup
    private static class GroupsTableModel extends AbstractTableModel {
        private final String[] columnNames = {"Kod grupy", "Specjalizacja", "Opis"};
        private final List<StudentGroup> groups;

        public GroupsTableModel(List<StudentGroup> groups) {
            this.groups = groups;
        }

        @Override
        public int getRowCount() {
            return groups.size();
        }

        @Override
        public int getColumnCount() {
            return columnNames.length;
        }

        @Override
        public String getColumnName(int column) {
            return columnNames[column];
        }

        @Override
        public Object getValueAt(int rowIndex, int columnIndex) {
            StudentGroup group = groups.get(rowIndex);
            switch (columnIndex) {
                case 0: return group.getCode();
                case 1: return group.getSpecialization();
                case 2: return group.getDescription();
                default: return "";
            }
        }

        @Override
        public boolean isCellEditable(int rowIndex, int columnIndex) {
            return false;
        }
    }
}
