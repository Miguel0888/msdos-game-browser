package de.bund.zrb.msdosgames.frontend.swing;

import de.bund.zrb.msdosgames.domain.GameSummary;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.List;

final class GameTableModel extends AbstractTableModel {

    private static final String[] COLUMNS = {"Titel", "Jahr", "Creator", "Downloads", "Identifier"};

    private final List<GameSummary> games = new ArrayList<GameSummary>();

    public void replaceGames(List<GameSummary> newGames) {
        games.clear();
        games.addAll(newGames);
        fireTableDataChanged();
    }

    public void appendGames(List<GameSummary> newGames) {
        int firstRow = games.size();
        games.addAll(newGames);
        fireTableRowsInserted(firstRow, games.size() - 1);
    }

    public GameSummary getGameAt(int rowIndex) {
        return games.get(rowIndex);
    }

    public List<GameSummary> getGames() {
        return new ArrayList<GameSummary>(games);
    }

    @Override
    public int getRowCount() {
        return games.size();
    }

    @Override
    public int getColumnCount() {
        return COLUMNS.length;
    }

    @Override
    public String getColumnName(int column) {
        return COLUMNS[column];
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        GameSummary game = games.get(rowIndex);
        switch (columnIndex) {
            case 0:
                return game.getTitle();
            case 1:
                return game.getDate();
            case 2:
                return game.getCreator();
            case 3:
                return Long.valueOf(game.getDownloads());
            case 4:
                return game.getIdentifier().getValue();
            default:
                return "";
        }
    }
}
