package org.jhotdraw.gui.fontchooser;

import com.tngtech.jgiven.junit5.ScenarioTest;
import org.junit.jupiter.api.Test;

public class FontChooserTest extends ScenarioTest<GivenFontChooser, WhenFontChooser, ThenFontChooser> {

    @Test
    public void user_changes_font_to_comic_sans() {
        given().the_user_has_opened_the_font_chooser();
        when().the_user_selects_font("Comic Sans");
        then().the_selected_font_should_be("Comic Sans");
    }
}