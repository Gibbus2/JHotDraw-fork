package org.jhotdraw.gui.fontchooser;

import com.tngtech.jgiven.Stage;
import com.tngtech.jgiven.annotation.ProvidedScenarioState;

public class GivenFontChooser extends Stage<GivenFontChooser> {
    @ProvidedScenarioState
    private FontChooser fontChooser;

    public GivenFontChooser the_user_has_opened_the_font_chooser() {
        fontChooser = new FontChooser();
        fontChooser.open();
        return this;
    }
}
