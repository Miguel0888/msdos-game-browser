package de.bund.zrb.msdosgames.frontend.swing;

import de.bund.zrb.msdosgames.domain.GameFile;

import javax.swing.DefaultListCellRenderer;
import javax.swing.JList;
import java.awt.Component;

final class GameFileComboBoxRenderer extends DefaultListCellRenderer {

    @Override
    public Component getListCellRendererComponent(
            JList<?> list,
            Object value,
            int index,
            boolean isSelected,
            boolean cellHasFocus) {
        Component component = super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
        if (value instanceof GameFile) {
            GameFile gameFile = (GameFile) value;
            setText(gameFile.getName() + "  " + formatSize(gameFile.getSize()));
        }
        return component;
    }

    private String formatSize(long bytes) {
        if (bytes <= 0L) {
            return "";
        }
        long kiloBytes = bytes / 1024L;
        if (kiloBytes < 1024L) {
            return "(" + kiloBytes + " KB)";
        }
        return "(" + (kiloBytes / 1024L) + " MB)";
    }
}
