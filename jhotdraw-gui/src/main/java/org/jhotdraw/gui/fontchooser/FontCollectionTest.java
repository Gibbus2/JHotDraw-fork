package org.jhotdraw.gui.fontchooser;

import org.jhotdraw.util.ResourceBundleUtil;
import org.junit.Before;
import org.junit.Test;

import javax.swing.tree.DefaultMutableTreeNode;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class FontCollectionTest {

    private FontCollection fontCollection;
    private ResourceBundleUtil labels;
    private DefaultMutableTreeNode root;
    private ArrayList<FontFamilyNode> families;

    @Before
    public void setUp() {
        labels = new ResourceBundleUtil("org.jhotdraw.gui.Labels", java.util.Locale.getDefault());        root = new DefaultMutableTreeNode();
        families = new ArrayList<>();
        fontCollection = new FontCollection(labels, root, families) {
            @Override
            public void addFonts() {
            }
        };
    }

    @Test
    public void testAddObserver() {
        TestObserver observer = new TestObserver();
        fontCollection.addObserver(observer);
        fontCollection.notifyObservers();
        assertTrue(observer.isUpdated());
    }

    @Test
    public void testRemoveObserver() {
        TestObserver observer = new TestObserver();
        fontCollection.addObserver(observer);
        fontCollection.removeObserver(observer);
        fontCollection.notifyObservers();
        assertFalse(observer.isUpdated());
    }

    @Test
    public void testNotifyObservers() {
        TestObserver observer1 = new TestObserver();
        TestObserver observer2 = new TestObserver();
        fontCollection.addObserver(observer1);
        fontCollection.addObserver(observer2);
        fontCollection.notifyObservers();
        assertTrue(observer1.isUpdated());
        assertTrue(observer2.isUpdated());
    }

    @Test
    public void testCollectFamiliesNamed() {
        FontFamilyNode family1 = new FontFamilyNode("SansSerif");
        FontFamilyNode family2 = new FontFamilyNode("Serif");
        families.add(family1);
        families.add(family2);

        List<FontFamilyNode> result = fontCollection.collectFamiliesNamed(families, "SansSerif");
        assertEquals(1, result.size());
        assertEquals("SansSerif", result.get(0).getName());
    }

    private static class TestObserver implements FontCollectionObserver {
        private boolean updated = false;

        @Override
        public void update(FontCollection fontCollection) {
            updated = true;
        }

        public boolean isUpdated() {
            return updated;
        }
    }
    @Test
    public void testBestCaseScenario() {
        FontFamilyNode family1 = new FontFamilyNode("SansSerif");
        families.add(family1);

        List<FontFamilyNode> result = fontCollection.collectFamiliesNamed(families, "SansSerif");
        assertEquals(1, result.size());
        assertEquals("SansSerif", result.get(0).getName());
    }
    @Test
    public void testBoundaryCaseEmptyFamilies() {
        List<FontFamilyNode> result = fontCollection.collectFamiliesNamed(families, "SansSerif");
        assertTrue(result.isEmpty());
    }

    @Test
    public void testBoundaryCaseNoMatchingFamily() {
        FontFamilyNode family1 = new FontFamilyNode("Serif");
        families.add(family1);

        List<FontFamilyNode> result = fontCollection.collectFamiliesNamed(families, "SansSerif");
        assertTrue(result.isEmpty());
    }
}