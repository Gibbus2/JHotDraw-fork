package org.jhotdraw.gui.fontchooser.fontcollection;
import javax.swing.tree.DefaultMutableTreeNode;

import org.jhotdraw.gui.fontchooser.FontCollection;
import org.jhotdraw.gui.fontchooser.FontCollectionNode;
import org.jhotdraw.gui.fontchooser.FontFamilyNode;
import org.jhotdraw.util.ResourceBundleUtil;

import java.util.ArrayList;

public class SymbolFontCollection extends FontCollection {

    public SymbolFontCollection(ResourceBundleUtil labels, DefaultMutableTreeNode root, ArrayList<FontFamilyNode> families) {
        super(labels, root, families);
    }

    @Override
    public void addFonts() {
        root.add(new FontCollectionNode(labels.getString("FontCollection.symbol"), (ArrayList<FontFamilyNode>) collectFamiliesNamed(families,
                "Symbol",
                "Wingdings")));
    }
}