// File: jhotdraw-gui/src/main/java/org/jhotdraw/gui/fontchooser/FontCollection.java
package org.jhotdraw.gui.fontchooser;

import javax.swing.tree.DefaultMutableTreeNode;
import org.jhotdraw.util.ResourceBundleUtil;
import java.util.ArrayList;
import java.util.List;

public abstract class FontCollection implements FontManagement{
    protected ResourceBundleUtil labels;
    protected DefaultMutableTreeNode root;
    protected ArrayList<FontFamilyNode> families;
    private List<FontCollectionObserver> observers = new ArrayList<>();

    public FontCollection(ResourceBundleUtil labels, DefaultMutableTreeNode root, ArrayList<FontFamilyNode> families) {
        this.labels = labels;
        this.root = root;
        this.families = families;
    }

    public abstract void addFonts();

    public void addObserver(FontCollectionObserver observer) {
        observers.add(observer);
    }

    public void removeObserver(FontCollectionObserver observer) {
        observers.remove(observer);
    }

    protected void notifyObservers() {
        for (FontCollectionObserver observer : observers) {
            observer.update(this);
        }
    }
    protected List<FontFamilyNode> collectFamiliesNamed(ArrayList<FontFamilyNode> families, String... names) {
        ArrayList<FontFamilyNode> result = new ArrayList<>();
        for (FontFamilyNode family : families) {
            for (String name : names) {
                if (family.getName().equals(name)) {
                    result.add(family);
                }
            }
        }
        return result;
    }
}