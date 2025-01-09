package org.jhotdraw.gui.fontchooser;

import com.tngtech.jgiven.Stage;

public class WhenTextStyle extends Stage<WhenTextStyle> {
    public WhenTextStyle the_teacher_chooses_the_bold_option() {
        // Simulate teacher choosing the bold option
        canvas.applyBold();
        return this;
    }

    public WhenTextStyle the_student_chooses_the_highlighted_italics_option() {
        // Simulate student choosing the highlighted italics option
        canvas.removeItalics();
        return this;
    }
}
