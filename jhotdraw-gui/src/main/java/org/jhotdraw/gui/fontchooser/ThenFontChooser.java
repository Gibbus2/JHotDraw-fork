package org.jhotdraw.gui.fontchooser;

import com.tngtech.jgiven.Stage;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class ThenFontChooser extends Stage<ThenFontChooser> {
    public ThenFontChooser the_selected_font_should_be(String expectedFont) {
        assertEquals(expectedFont, fontChooser.getSelectedFont());
        return this;
    }
}
