package com.DBProject.gui.PanelManager;

import javax.swing.*;
import java.awt.*;

public class PanelManager {
    private final JPanel content_panel;
    private final CardLayout card_layout;

    public JPanel get_content_panel() {
        return content_panel;
    }

    public CardLayout get_card_layout() {
        return card_layout;
    }

    public PanelManager() {
        card_layout = new CardLayout();
        content_panel = new JPanel(card_layout);
    }
}
