package org.jhotdraw.gui.fontchooser;

import java.awt.*;
import java.util.*;
import javax.swing.tree.*;

import org.jhotdraw.gui.fontchooser.fontcollection.*;
import org.jhotdraw.util.ResourceBundleUtil;

public class DefaultFontChooserModel extends AbstractFontChooserModel {

    protected DefaultMutableTreeNode root;
    private FontCollectionManager fontCollectionManager = new FontCollectionManager();
    private TreeModelSupport treeModelSupport = new TreeModelSupport(root);

    protected ArrayList<FontFamilyNode> collectFamiliesNamed(ArrayList<FontFamilyNode> families, String... names) {
        return fontCollectionManager.collectFamiliesNamed(families, names);
    }

    @Override
    public boolean isEditable(MutableTreeNode node) {
        return fontCollectionManager.isEditable(node);
    }

    @Override
    public Object getRoot() {
        return treeModelSupport.getRoot();
    }

    @Override
    public Object getChild(Object parent, int index) {
        return treeModelSupport.getChild(parent, index);
    }

    @Override
    public int getChildCount(Object parent) {
        return treeModelSupport.getChildCount(parent);
    }

    @Override
    public boolean isLeaf(Object node) {
        return treeModelSupport.isLeaf(node);
    }

    @Override
    public void valueForPathChanged(TreePath path, Object newValue) {
        treeModelSupport.valueForPathChanged(path, newValue);
    }

    @Override
    public int getIndexOfChild(Object parent, Object child) {
        return treeModelSupport.getIndexOfChild(parent, child);
    }

    public DefaultFontChooserModel() {
        root = new DefaultMutableTreeNode();
    }

    public DefaultFontChooserModel(Font[] fonts) {
        root = new DefaultMutableTreeNode();
        setFonts(fonts);
    }

    public void setFonts(Font[] fonts) {
        ResourceBundleUtil labels = ResourceBundleUtil.getBundle("org.jhotdraw.gui.Labels");
        ArrayList<FontFamilyNode> families = new ArrayList<>();
        HashMap<String, FontFamilyNode> familyMap = new HashMap<>();
        for (Font f : fonts) {
            String familyName = f.getFamily();
            FontFamilyNode family;
            if (familyMap.containsKey(familyName)) {
                family = familyMap.get(familyName);
            } else {
                family = new FontFamilyNode(familyName);
                familyMap.put(familyName, family);
            }
            family.add(new FontFaceNode(f));
        }
        families.addAll(familyMap.values());
        Collections.sort(families);
        root.removeAllChildren();
        new WebFontCollection(labels, root, families).addFonts();
        new SystemFontCollection(labels, root, families).addFonts();
        new SerifFontCollection(labels, root, families).addFonts();
        new SansSerifFontCollection(labels, root, families).addFonts();
        new ScriptFontCollection(labels, root, families).addFonts();
        new MonospacedFontCollection(labels, root, families).addFonts();
        new DecorativeFontCollection(labels, root, families).addFonts();
        new SymbolFontCollection(labels, root, families).addFonts();
        new OtherFontCollection(labels, root, families).addFonts();

        SerifFontCollection serifFontCollection = new SerifFontCollection(labels, root, families);
        FontCollectionObserver logger = new FontCollectionLogger();
        serifFontCollection.addObserver(logger);
        serifFontCollection.addFonts();

        fireTreeStructureChanged(this, new TreePath(root));
    }

    public static class UIResource extends DefaultFontChooserModel implements javax.swing.plaf.UIResource {
    }


}