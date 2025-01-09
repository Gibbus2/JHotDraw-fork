package org.jhotdraw.gui.fontchooser;

import com.tngtech.jgiven.Stage;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ThenTextStyle extends Stage<ThenTextStyle> {
    public ThenTextStyle the_selected_text_should_be_bold() {
        assertTrue(canvas.isTextBold());
        return this;
    }

    public ThenTextStyle the_selected_text_should_be_normal() {
        assertTrue(canvas.isTextNormal());
        return this;
    }
}
