package de.bund.zrb.msdosgames.frontend.swing;

import de.bund.zrb.msdosgames.domain.ArchiveItemNotice;
import de.bund.zrb.msdosgames.domain.ArchiveMetadataEntry;
import de.bund.zrb.msdosgames.domain.GameDetails;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.table.AbstractTableModel;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.List;

final class ArchiveItemNoticePanel extends JPanel {

    private final JLabel availabilityLabel = new JLabel("Verfügbarkeit: -");
    private final JLabel sourceLabel = new JLabel("Quelle: -");
    private final JLabel termsLabel = new JLabel("Terms: -");
    private final JTextArea accessArea = new JTextArea();
    private final MetadataTableModel metadataTableModel = new MetadataTableModel();
    private final JTable metadataTable = new JTable(metadataTableModel);

    ArchiveItemNoticePanel() {
        super(new BorderLayout(4, 4));
        setBorder(BorderFactory.createTitledBorder("Archive.org-Hinweise"));
        accessArea.setEditable(false);
        accessArea.setLineWrap(true);
        accessArea.setWrapStyleWord(true);
        accessArea.setRows(3);

        JPanel factsPanel = new JPanel(new GridLayout(3, 1, 2, 2));
        factsPanel.add(availabilityLabel);
        factsPanel.add(sourceLabel);
        factsPanel.add(termsLabel);

        add(factsPanel, BorderLayout.NORTH);
        add(new JScrollPane(accessArea), BorderLayout.CENTER);
        add(new JScrollPane(metadataTable), BorderLayout.SOUTH);
        clear();
    }

    void clear() {
        availabilityLabel.setText("Verfügbarkeit: -");
        sourceLabel.setText("Quelle: -");
        termsLabel.setText("Terms: -");
        accessArea.setText("Wähle links ein Spiel aus. Danach werden hier die auf der Archive.org-Seite erkannten Hinweise angezeigt.");
        accessArea.setCaretPosition(0);
        metadataTableModel.replaceEntries(new ArrayList<ArchiveMetadataEntry>());
    }

    void showDetails(GameDetails details) {
        ArchiveItemNotice notice = details.getArchiveItemNotice();
        availabilityLabel.setText("Verfügbarkeit: " + notice.getAvailabilityText());
        sourceLabel.setText("Quelle: " + textOrFallback(notice.getSourceUrl(), "keine Angabe"));
        termsLabel.setText("Terms: " + textOrFallback(notice.getTermsUrl(), "nicht auf der Seite gefunden"));
        accessArea.setText(notice.getAccessText());
        accessArea.setCaretPosition(0);
        metadataTableModel.replaceEntries(notice.getMetadataEntries());
    }

    private String textOrFallback(String value, String fallback) {
        if (value == null || value.trim().length() == 0) {
            return fallback;
        }
        return value.trim();
    }

    private static final class MetadataTableModel extends AbstractTableModel {

        private final List<ArchiveMetadataEntry> entries = new ArrayList<ArchiveMetadataEntry>();

        void replaceEntries(List<ArchiveMetadataEntry> newEntries) {
            entries.clear();
            if (newEntries != null) {
                entries.addAll(newEntries);
            }
            fireTableDataChanged();
        }

        @Override
        public int getRowCount() {
            return entries.size();
        }

        @Override
        public int getColumnCount() {
            return 2;
        }

        @Override
        public Object getValueAt(int rowIndex, int columnIndex) {
            ArchiveMetadataEntry entry = entries.get(rowIndex);
            if (columnIndex == 0) {
                return entry.getName();
            }
            return entry.getValue();
        }

        @Override
        public String getColumnName(int column) {
            if (column == 0) {
                return "Feld";
            }
            return "Wert";
        }
    }
}
