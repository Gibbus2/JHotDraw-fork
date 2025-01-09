package org.jhotdraw.gui.fontchooser;

import java.util.*;
import javax.swing.tree.*;

public class FontCollectionManager {
    public ArrayList<FontFamilyNode> collectFamiliesNamed(ArrayList<FontFamilyNode> families, String... names) {
        ArrayList<FontFamilyNode> coll = new ArrayList<>();
        HashSet<String> nameMap = new HashSet<>();
        nameMap.addAll(Arrays.asList(names));
        for (FontFamilyNode family : families) {
            if (nameMap.contains(family.getName())) {
                coll.add(family.clone());
            }
        }
        return coll;
    }

    public boolean isEditable(MutableTreeNode node) {
        boolean result = true;
        if (node instanceof FontFaceNode) {
            result &= ((FontFaceNode) node).isEditable();
            node = (MutableTreeNode) node.getParent();
        }
        if (result && (node instanceof FontFamilyNode)) {
            result &= ((FontFamilyNode) node).isEditable();
            node = (MutableTreeNode) node.getParent();
        }
        if (result && (node instanceof FontCollectionNode)) {
            result &= ((FontCollectionNode) node).isEditable();
        }
        return result;
    }
}