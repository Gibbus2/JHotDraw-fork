package org.jhotdraw.gui.fontchooser;

import com.tngtech.jgiven.Stage;

public class GivenTextStyle extends Stage<GivenTextStyle> {
    public GivenTextStyle the_teacher_has_selected_text_in_the_canvas() {
        // Simulate teacher selecting text in the canvas
        canvas.selectText();
        return this;
    }

    public GivenTextStyle the_student_has_selected_text_with_italics_option() {
        // Simulate student selecting text with italics option
        canvas.selectTextWithItalics();
        return this;
    }
}
