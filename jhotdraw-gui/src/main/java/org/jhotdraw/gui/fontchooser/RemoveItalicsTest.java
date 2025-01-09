package org.jhotdraw.gui.fontchooser;

import com.tngtech.jgiven.junit5.ScenarioTest;
import org.junit.jupiter.api.Test;

public class RemoveItalicsTest extends ScenarioTest<GivenTextStyle, WhenTextStyle, ThenTextStyle> {

    @Test
    public void student_removes_italics_text_style() {
        given().the_student_has_selected_text_with_italics_option();
        when().the_student_chooses_the_highlighted_italics_option();
        then().the_selected_text_should_be_normal();
    }
}
