package de.bund.zrb.msdosgames.frontend.swing;

import de.bund.zrb.msdosgames.domain.GameDetails;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.Cursor;
import java.awt.GridLayout;

final class GameMetadataPanel extends JPanel {

    private final ExternalUrlOpener externalUrlOpener = new ExternalUrlOpener();
    private final JButton titleButton = new JButton("Kein Spiel ausgewählt");
    private final JLabel identifierLabel = new JLabel(" ");
    private final JLabel sizeLabel = new JLabel(" ");

    private GameDetails currentDetails;

    GameMetadataPanel() {
        super(new BorderLayout(4, 4));
        setBorder(BorderFactory.createTitledBorder("Spiel"));
        configureTitleButton();

        JPanel factsPanel = new JPanel(new GridLayout(2, 1, 2, 2));
        factsPanel.add(identifierLabel);
        factsPanel.add(sizeLabel);

        add(titleButton, BorderLayout.NORTH);
        add(factsPanel, BorderLayout.CENTER);
    }

    void clear() {
        currentDetails = null;
        titleButton.setText("Kein Spiel ausgewählt");
        titleButton.setEnabled(false);
        identifierLabel.setText(" ");
        sizeLabel.setText(" ");
    }

    void showDetails(GameDetails details) {
        currentDetails = details;
        titleButton.setText("<html><u>" + escape(details.getTitle()) + "</u></html>");
        titleButton.setEnabled(details.getLicenseNotice().getSourceUrl().length() > 0);
        identifierLabel.setText("Identifier: " + details.getIdentifier().getValue());
        sizeLabel.setText("Item-Größe: " + formatSize(details.getItemSize()));
    }

    private void configureTitleButton() {
        titleButton.setBorderPainted(false);
        titleButton.setContentAreaFilled(false);
        titleButton.setFocusPainted(false);
        titleButton.setHorizontalAlignment(JButton.LEFT);
        titleButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        titleButton.setFont(titleButton.getFont().deriveFont(15.0f));
        titleButton.addActionListener(event -> openCurrentDetailsPage());
        titleButton.setEnabled(false);
    }

    private void openCurrentDetailsPage() {
        if (currentDetails == null) {
            return;
        }
        String url = currentDetails.getLicenseNotice().getSourceUrl();
        try {
            externalUrlOpener.openUrl(url);
        } catch (Exception exception) {
            JOptionPane.showMessageDialog(this, "Die Detailseite konnte nicht geöffnet werden.\n" + url + "\n" + exception.getMessage(), "Fehler", JOptionPane.ERROR_MESSAGE);
        }
    }

    private String formatSize(long bytes) {
        if (bytes <= 0L) {
            return "unbekannt";
        }
        long kiloBytes = bytes / 1024L;
        if (kiloBytes < 1024L) {
            return kiloBytes + " KB";
        }
        return kiloBytes / 1024L + " MB";
    }

    private String escape(String value) {
        if (value == null) {
            return "";
        }
        return value.replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\"", "&quot;");
    }
}
