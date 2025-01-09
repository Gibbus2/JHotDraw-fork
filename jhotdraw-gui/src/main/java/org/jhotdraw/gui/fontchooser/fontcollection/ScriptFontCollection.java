package org.jhotdraw.gui.fontchooser.fontcollection;
import javax.swing.tree.DefaultMutableTreeNode;

import org.jhotdraw.gui.fontchooser.FontCollection;
import org.jhotdraw.gui.fontchooser.FontCollectionNode;
import org.jhotdraw.gui.fontchooser.FontFamilyNode;
import org.jhotdraw.util.ResourceBundleUtil;

import java.util.ArrayList;

public class ScriptFontCollection extends FontCollection {

    public ScriptFontCollection(ResourceBundleUtil labels, DefaultMutableTreeNode root, ArrayList<FontFamilyNode> families) {
        super(labels, root, families);
    }

    @Override
    public void addFonts() {
        root.add(new FontCollectionNode(labels.getString("FontCollection.script"), (ArrayList<FontFamilyNode>) collectFamiliesNamed(families,
                "Brush Script MT",
                "Lucida Handwriting",
                "Segoe Script")));
    }
}