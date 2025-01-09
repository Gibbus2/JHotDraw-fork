package org.jhotdraw.gui.fontchooser;

public class FontCollectionLogger implements FontCollectionObserver {
    @Override
    public void update(FontCollection fontCollection) {
        System.out.println("FontCollection updated: " + fontCollection);
    }
}