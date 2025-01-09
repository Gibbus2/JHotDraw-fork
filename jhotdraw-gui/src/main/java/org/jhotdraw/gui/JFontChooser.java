// File: jhotdraw-gui/src/main/java/org/jhotdraw/gui/JFontChooser.java
package org.jhotdraw.gui;

import java.awt.*;
import java.awt.event.*;
import java.beans.*;
import java.util.ArrayList;
import java.util.concurrent.*;
import javax.swing.*;
import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;
import javax.swing.tree.*;
import org.jhotdraw.gui.fontchooser.DefaultFontChooserModel;
import org.jhotdraw.gui.fontchooser.FontChooserModel;
import org.jhotdraw.gui.fontchooser.FontCollectionNode;
import org.jhotdraw.gui.fontchooser.FontFaceNode;
import org.jhotdraw.gui.fontchooser.FontFamilyNode;
import org.jhotdraw.gui.plaf.FontChooserUI;
import org.jhotdraw.gui.plaf.palette.PaletteFontChooserUI;

/**
 * Font chooser dialog.
 *
 * @author Werner Randelshofer
 * @version $Id$
 */
public class JFontChooser extends JComponent {

    private static final long serialVersionUID = 1L;
    /**
     * @see #getUIClassID
     * @see #readObject
     */
    private static final String UI_CLASS_ID = "FontChooserUI";
    /**
     * Identifies the "selectedFont" property.
     */
    public static final String SELECTED_FONT_PROPERTY = "selectedFont";
    /**
     * Identifies the "selectionPath" property.
     */
    public static final String SELECTION_PATH_PROPERTY = "selectionPath";
    /**
     * Instruction to cancel the current selection.
     */
    public static final String CANCEL_SELECTION = "CancelSelection";
    /**
     * Instruction to approve the current selection
     * (same as pressing yes or ok).
     */
    public static final String APPROVE_SELECTION = "ApproveSelection";
    /**
     * Identifies the "model" property.
     */
    public static final String MODEL_PROPERTY = "model";
    /**
     * Holds the selected path of the JFontChooser.
     */
    private TreePath selectionPath;
    /**
     * Holds the selected font of the JFontChooser.
     */
    private Font selectedFont;
    /**
     * Holds the model of the JFontChooser.
     */
    private FontChooserModel model;
    // ********************************
    // ***** Dialog Return Values *****
    // ********************************
    /**
     * Return value if cancel is chosen.
     */
    public static final int CANCEL_OPTION = 1;
    /**
     * Return value if approve (yes, ok) is chosen.
     */
    public static final int APPROVE_OPTION = 0;
    /**
     * Return value if an error occured.
     */
    public static final int ERROR_OPTION = -1;
    private int returnValue = ERROR_OPTION;
    // DIALOG
    private JDialog dialog = null;
    /**
     * This future is used to load fonts lazily
     */
    private static FutureTask<Font[]> future;

    /**
     * Creates new form JFontChooser
     */
    public JFontChooser() {
        loadAllFonts();
        model = new DefaultFontChooserModel.UIResource();
        model.addTreeModelListener(new ModelHandler());
        updateUI();
        addPropertyChangeListener(new AncestorPropertyChangeListener());
    }

    /**
     * Resets the UI property with a value from the current look and feel.
     *
     * @see JComponent#updateUI
     */
    @Override
    public void updateUI() {
        // Try to get a browser UI from the UIManager.
        // Fall back to BasicBrowserUI, if none is available.
        if (UIManager.get(getUIClassID()) != null) {
            setUI((FontChooserUI) UIManager.getUI(this));
        } else {
            setUI(PaletteFontChooserUI.createUI(this));
        }
    }

    /**
     * Returns the look and feel (L&amp;F) object that renders this component.
     *
     * @return the PanelUI object that renders this component
     * @since 1.4
     */
    public FontChooserUI getUI() {
        return (FontChooserUI) ui;
    }

    /**
     * Sets the look and feel (L&amp;F) object that renders this component.
     *
     * @param ui the PanelUI L&amp;F object
     * @see UIDefaults#getUI
     */
    public void setUI(FontChooserUI ui) {
        super.setUI(ui);
    }

    /**
     * Returns a string that specifies the name of the L&amp;F class
     * that renders this component.
     *
     * @return "FontChooserUI"
     * @see JComponent#getUIClassID
     * @see UIDefaults#getUI
     */
    @Override
    public String getUIClassID() {
        return UI_CLASS_ID;
    }

    /**
     * Called by the UI when the user hits the Approve button
     * (labeled "Open" or "Save", by default). This can also be
     * called by the programmer.
     * This method causes an action event to fire
     * with the command string equal to
     * <code>APPROVE_SELECTION</code>.
     *
     * @see #APPROVE_SELECTION
     */
    public void approveSelection() {
        returnValue = APPROVE_OPTION;
        if (dialog != null) {
            dialog.setVisible(false);
        }
        fireActionPerformed(APPROVE_SELECTION);
    }

    /**
     * Called by the UI when the user chooses the Cancel button.
     * This can also be called by the programmer.
     * This method causes an action event to fire
     * with the command string equal to
     * <code>CANCEL_SELECTION</code>.
     *
     * @see #CANCEL_SELECTION
     */
    public void cancelSelection() {
        returnValue = CANCEL_OPTION;
        if (dialog != null) {
            dialog.setVisible(false);
        }
        fireActionPerformed(CANCEL_SELECTION);
    }

    /**
     * Adds an <code>ActionListener</code> to the font chooser.
     *
     * @param l the listener to be added
     *
     * @see #approveSelection
     * @see #cancelSelection
     */
    public void addActionListener(ActionListener l) {
        listenerList.add(ActionListener.class, l);
    }

    /**
     * Removes an <code>ActionListener</code> from the font chooser.
     *
     * @param l the listener to be removed
     *
     * @see #addActionListener
     */
    public void removeActionListener(ActionListener l) {
        listenerList.remove(ActionListener.class, l);
    }

    /**
     * Notifies all listeners that have registered interest for
     * notification on this event type. The event instance
     * is lazily created using the <code>command</code> parameter.
     */
    protected void fireActionPerformed(String command) {
        // Guaranteed to return a non-null array
        Object[] listeners = listenerList.getListenerList();
        long mostRecentEventTime = EventQueue.getMostRecentEventTime();
        int modifiers = 0;
        AWTEvent currentEvent = EventQueue.getCurrentEvent();
        if (currentEvent instanceof InputEvent) {
            modifiers = ((InputEvent) currentEvent).getModifiers();
        } else if (currentEvent instanceof ActionEvent) {
            modifiers = ((ActionEvent) currentEvent).getModifiers();
        }
        ActionEvent e = null;
        // Process the listeners last to first, notifying
        // those that are interested in this event
        for (int i = listeners.length - 2; i >= 0; i -= 2) {
            if (listeners[i] == ActionListener.class) {
                // Lazily create the event:
                if (e == null) {
                    e = new ActionEvent(this, ActionEvent.ACTION_PERFORMED, command, mostRecentEventTime, modifiers);
                }
                ((ActionListener) listeners[i + 1]).actionPerformed(e);
            }
        }
    }

    /**
     * Gets the selected Font.
     * This is a bound property.
     *
     * @return The selected font, or null, if no font is selected.
     */
    public TreePath getSelectionPath() {
        return selectionPath;
    }

    /**
     * Sets the selected Font.
     * This is a bound property.
     * <p>
     * Changing the selection path, causes a change of the
     * selected font, if the selected font is not the last
     * path segment of the selection path.
     *
     * @param newValue The new selected font, or null if no font is to be
     * selected..
     */
    public void setSelectionPath(TreePath newValue) {
        TreePath oldValue = selectionPath;
        this.selectionPath = newValue;
        firePropertyChange(SELECTION_PATH_PROPERTY, oldValue, newValue);
        if (selectionPath != null && selectionPath.getPathCount() == 4) {
            setSelectedFont(((FontFaceNode) selectionPath.getLastPathComponent()).getFont());
        }
    }

    /**
     * Starts loading all fonts from the local graphics environment
     * using a worker thread.
     */
    public synchronized static void loadAllFonts() {
        if (future == null) {
            future = new FutureTask<>(new Callable<Font[]>() {
                @Override
                public Font[] call() throws Exception {
                    Font[] fonts = GraphicsEnvironment.getLocalGraphicsEnvironment().getAllFonts();
                    // get rid of bogus fonts
                    ArrayList<Font> goodFonts = new ArrayList<>(fonts.length);
                    for (Font f : fonts) {
                        //System.out.println("JFontChooser "+f.getFontName());
                        Font decoded = Font.decode(f.getFontName());
                        if (decoded.getFontName().equals(f.getFontName()) || decoded.getFontName().endsWith("-Derived")) {
                            goodFonts.add(f);
                        } else {
                            //System.out.println("JFontChooser ***bogus*** "+decoded.getFontName());
                        }
                    }
                    return goodFonts.toArray(new Font[goodFonts.size()]);
                    // return fonts;
                }
            });
            new Thread(future).start();
        }
    }

    /**
     * Returns all fonts that have been loaded.
     *
     * @return An array of all fonts.
     */
    public static synchronized Font[] getAllFonts() {
        loadAllFonts();
        try {
            return future.get().clone();
        } catch (InterruptedException | ExecutionException ex) {
            return new Font[0];
        }
    }

    /**
     * Gets the selected Font.
     * This is a bound property.
     *
     * @return The selected font, or null, if no font is selected.
     */
    public Font getSelectedFont() {
        return selectedFont;
    }

    /**
     * Sets the selected Font.
     * This is a bound property.
     *
     * @param newValue The new selected font, or null if no font is to be
     * selected.
     */
    public void setSelectedFont(Font newValue) {
        Font oldValue = selectedFont;
        this.selectedFont = newValue;
        firePropertyChange(SELECTED_FONT_PROPERTY, oldValue, newValue);
        updateSelectionPath(newValue);
    }

    /**
     * Updates the selection path based on the selected font.
     *
     * @param newValue The new selected font.
     */
    protected void updateSelectionPath(Font newValue) {
        if (newValue == null || selectionPath == null || selectionPath.getPathCount() != 4
                || !((FontFaceNode) selectionPath.getLastPathComponent()).getFont().getFontName().equals(newValue.getFontName())) {
            if (newValue == null) {
                setSelectionPath(null);
            } else {
                TreePath path = selectionPath;
                FontCollectionNode oldCollection = (path != null && path.getPathCount() > 1) ? (FontCollectionNode) path.getPathComponent(1) : null;
                FontFamilyNode oldFamily = (path != null && path.getPathCount() > 2) ? (FontFamilyNode) path.getPathComponent(2) : null;
                FontFaceNode oldFace = (path != null && path.getPathCount() > 3) ? (FontFaceNode) path.getPathComponent(3) : null;
                FontCollectionNode newCollection = oldCollection;
                FontFamilyNode newFamily = oldFamily;
                FontFaceNode newFace = findFontFaceNode(newValue, newCollection, newFamily);
                if (newFace != null) {
                    setSelectionPath(new TreePath(new Object[]{
                            getModel().getRoot(),
                            newCollection,
                            newFamily,
                            newFace
                    }));
                } else {
                    setSelectionPath(null);
                }
            }
        }
    }

    private FontFaceNode findFontFaceNode(Font newValue, FontCollectionNode newCollection, FontFamilyNode newFamily) {
        FontFaceNode newFace = null;
        if (newFace == null && newFamily != null) {
            newFace = findFontFaceInFamily(newValue, newFamily);
        }
        if (newFace == null && newCollection != null) {
            newFace = findFontFaceInCollection(newValue, newCollection);
        }
        if (newFace == null) {
            newFace = findFontFaceInAllCollections(newValue);
        }
        return newFace;
    }

    private FontFaceNode findFontFaceInFamily(Font newValue, FontFamilyNode newFamily) {
        for (FontFaceNode face : newFamily.faces()) {
            if (face.getFont().getFontName().equals(newValue.getFontName())) {
                return face;
            }
        }
        return null;
    }

    private FontFaceNode findFontFaceInCollection(Font newValue, FontCollectionNode newCollection) {
        for (FontFamilyNode family : newCollection.families()) {
            for (FontFaceNode face : family.faces()) {
                if (face.getFont().getFontName().equals(newValue.getFontName())) {
                    return face;
                }
            }
        }
        return null;
    }

    private FontFaceNode findFontFaceInAllCollections(Font newValue) {
        TreeNode root = (TreeNode) getModel().getRoot();
        for (int i = 0, n = root.getChildCount(); i < n; i++) {
            FontCollectionNode collection = (FontCollectionNode) root.getChildAt(i);
            for (FontFamilyNode family : collection.families()) {
                for (FontFaceNode face : family.faces()) {
                    if (face.getFont().getFontName().equals(newValue.getFontName())) {
                        return face;
                    }
                }
            }
        }
        return null;
    }

    /**
     * Gets the model of the JFontChooser.
     *
     * @return The model.
     */
    public FontChooserModel getModel() {
        return model;
    }

    /**
     * Sets the model of the JFontChooser.
     *
     * @param newValue The new model.
     */
    public void setModel(FontChooserModel newValue) {
        FontChooserModel oldValue = model;
        if (oldValue != null) {
            oldValue.removeTreeModelListener(new ModelHandler());
        }
        this.model = newValue;
        if (newValue != null) {
            newValue.addTreeModelListener(new ModelHandler());
        }
        firePropertyChange(MODEL_PROPERTY, oldValue, newValue);
        updateSelectionPath(selectedFont);
    }

    private class ModelHandler implements TreeModelListener {
        @Override
        public void treeNodesChanged(TreeModelEvent e) {
            updateSelectionPath(getSelectedFont());
        }

        @Override
        public void treeNodesInserted(TreeModelEvent e) {
            updateSelectionPath(getSelectedFont());
        }

        @Override
        public void treeNodesRemoved(TreeModelEvent e) {
            updateSelectionPath(getSelectedFont());
        }

        @Override
        public void treeStructureChanged(TreeModelEvent e) {
            updateSelectionPath(getSelectedFont());
        }
    }

    private class AncestorPropertyChangeListener implements PropertyChangeListener {
        @Override
        public void propertyChange(PropertyChangeEvent evt) {
            if ("ancestor".equals(evt.getPropertyName()) && evt.getNewValue() != null) {
                try {
                    ((DefaultFontChooserModel) model).setFonts(getAllFonts());
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                JFontChooser.this.removePropertyChangeListener(this);
            }
        }
    }
}