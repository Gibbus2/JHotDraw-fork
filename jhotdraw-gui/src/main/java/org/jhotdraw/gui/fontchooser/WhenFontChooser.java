package org.jhotdraw.gui.fontchooser;

import com.tngtech.jgiven.Stage;

public class WhenFontChooser extends Stage<WhenFontChooser> {
    public WhenFontChooser the_user_selects_font(String fontName) {
        // Simulate user selecting a font
        fontChooser.selectFont(fontName);
        return this;
    }
}
