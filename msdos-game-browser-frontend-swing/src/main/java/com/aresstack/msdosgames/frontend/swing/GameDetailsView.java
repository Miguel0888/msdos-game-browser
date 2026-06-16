package com.aresstack.msdosgames.frontend.swing;

import com.aresstack.msdosgames.domain.GameDetails;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.io.File;

final class GameDetailsView extends JPanel {

    private final GameMetadataPanel metadataPanel = new GameMetadataPanel();
    private final GameImagePreviewPanel imagePreviewPanel;
    private final GameDescriptionPanel descriptionPanel = new GameDescriptionPanel();
    private final ArchiveItemNoticePanel archiveItemNoticePanel = new ArchiveItemNoticePanel();

    GameDetailsView(GameImagePreviewPanel.PreviewImageLoader imageLoader) {
        super(new BorderLayout(6, 6));
        this.imagePreviewPanel = new GameImagePreviewPanel(imageLoader);

        JPanel topPanel = new JPanel(new GridLayout(1, 2, 6, 6));
        topPanel.add(metadataPanel);
        topPanel.add(imagePreviewPanel);

        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, descriptionPanel, archiveItemNoticePanel);
        splitPane.setResizeWeight(0.55d);

        add(topPanel, BorderLayout.NORTH);
        add(new JScrollPane(splitPane), BorderLayout.CENTER);
    }

    void clear(File downloadDirectory) {
        metadataPanel.clear();
        imagePreviewPanel.clear();
        descriptionPanel.clear();
        archiveItemNoticePanel.clear();
    }

    void showDetails(GameDetails details, File downloadDirectory) {
        metadataPanel.showDetails(details);
        imagePreviewPanel.showDetails(details);
        descriptionPanel.showDetails(details);
        archiveItemNoticePanel.showDetails(details);
    }
}
