package org.jhotdraw.gui.fontchooser;

import com.tngtech.jgiven.junit5.ScenarioTest;
import org.junit.jupiter.api.Test;

public class TextStyleTest extends ScenarioTest<GivenTextStyle, WhenTextStyle, ThenTextStyle> {

    @Test
    public void teacher_changes_text_style_to_bold() {
        given().the_teacher_has_selected_text_in_the_canvas();
        when().the_teacher_chooses_the_bold_option();
        then().the_selected_text_should_be_bold();
    }
}