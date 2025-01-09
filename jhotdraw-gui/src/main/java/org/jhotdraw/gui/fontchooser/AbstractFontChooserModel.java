/**
 * @(#)AbstractFontChooserModel.java
 *
 * Copyright (c) 2008 The authors and contributors of JHotDraw.
 * You may not use, copy or modify this file, except in compliance with the
 * accompanying license terms.
 */
package org.jhotdraw.gui.fontchooser;

import java.util.*;
import javax.swing.event.*;
import javax.swing.tree.*;

/**
 * AbstractFontChooserModel.
 *
 * @author Werner Randelshofer
 * @version $Id$
 */
public abstract class AbstractFontChooserModel implements FontChooserModel {
    protected EventListenerList listenerList = new EventListenerList();
    private EventFiringSupport eventFiringSupport = new EventFiringSupport(listenerList);
    private ListenerManagementSupport listenerManagementSupport = new ListenerManagementSupport(listenerList);

    protected void fireTreeNodesChanged(Object source, Object[] path, int[] childIndices, Object[] children) {
        eventFiringSupport.fireTreeNodesChanged(source, path, childIndices, children);
    }

    protected void fireTreeNodesInserted(Object source, Object[] path, int[] childIndices, Object[] children) {
        eventFiringSupport.fireTreeNodesInserted(source, path, childIndices, children);
    }

    protected void fireTreeNodesRemoved(Object source, Object[] path, int[] childIndices, Object[] children) {
        eventFiringSupport.fireTreeNodesRemoved(source, path, childIndices, children);
    }

    protected void fireTreeStructureChanged(Object source, Object[] path, int[] childIndices, Object[] children) {
        eventFiringSupport.fireTreeStructureChanged(source, path, childIndices, children);
    }

    protected void fireTreeStructureChanged(Object source, TreePath path) {
        eventFiringSupport.fireTreeStructureChanged(source, path);
    }

    public <T extends EventListener> T[] getListeners(Class<T> listenerType) {
        return listenerManagementSupport.getListeners(listenerType);
    }


    /**
     * Listeners.
     */
    //  Events
    /**
     * Adds a listener for the TreeModelEvent posted after the tree changes.
     *
     * @see #removeTreeModelListener
     * @param l the listener to add
     */
    @Override
    public void addTreeModelListener(TreeModelListener l) {
        listenerList.add(TreeModelListener.class, l);
    }

    /**
     * Removes a listener previously added with <B>addTreeModelListener()</B>.
     *
     * @see #addTreeModelListener
     * @param l the listener to remove
     */
    @Override
    public void removeTreeModelListener(TreeModelListener l) {
        listenerList.remove(TreeModelListener.class, l);
    }

    /**
     * Returns an array of all the tree model listeners
     * registered on this model.
     *
     * @return all of this model's <code>TreeModelListener</code>s
     * or an empty
     * array if no tree model listeners are currently registered
     *
     * @see #addTreeModelListener
     * @see #removeTreeModelListener
     *
     * @since 1.4
     */
    public TreeModelListener[] getTreeModelListeners() {
        return listenerList.getListeners(
                TreeModelListener.class);
    }

    /**
     * Notifies all listeners that have registered interest for
     * notification on this event type. The event instance
     * is lazily created using the parameters passed into
     * the fire method.
     *
     * @param source the node being changed
     * @param path the path to the root node
     * @param childIndices the indices of the changed elements
     * @param children the changed elements
     * @see EventListenerList
     */


    /**
     * Notifies all listeners that have registered interest for
     * notification on this event type. The event instance
     * is lazily created using the parameters passed into
     * the fire method.
     *
     * @param source the node where new elements are being inserted
     * @param path the path to the root node
     * @param childIndices the indices of the new elements
     * @param children the new elements
     * @see EventListenerList
     */


    /**
     * Notifies all listeners that have registered interest for
     * notification on this event type. The event instance
     * is lazily created using the parameters passed into
     * the fire method.
     *
     * @param source the node where elements are being removed
     * @param path the path to the root node
     * @param childIndices the indices of the removed elements
     * @param children the removed elements
     * @see EventListenerList
     */


    /**
     * Notifies all listeners that have registered interest for
     * notification on this event type. The event instance
     * is lazily created using the parameters passed into
     * the fire method.
     *
     * @param source the node where the tree model has changed
     * @param path the path to the root node
     * @param childIndices the indices of the affected elements
     * @param children the affected elements
     * @see EventListenerList
     */


    /*
     * Notifies all listeners that have registered interest for
     * notification on this event type.  The event instance
     * is lazily created using the parameters passed into
     * the fire method.
     *
     * @param source the node where the tree model has changed
     * @param path the path to the root node
     * @see EventListenerList
     */


    /**
     * Returns an array of all the objects currently registered
     * as <code><em>Foo</em>Listener</code>s
     * upon this model.
     * <code><em>Foo</em>Listener</code>s are registered using the
     * <code>add<em>Foo</em>Listener</code> method.
     *
     * <p>
     *
     * You can specify the <code>listenerType</code> argument
     * with a class literal,
     * such as
     * <code><em>Foo</em>Listener.class</code>.
     * For example, you can query a
     * <code>DefaultTreeModel</code> <code>m</code>
     * for its tree model listeners with the following code:
     *
     * <pre>TreeModelListener[] tmls = (TreeModelListener[])(m.getListeners(TreeModelListener.class));</pre>
     *
     * If no such listeners exist, this method returns an empty array.
     *
     * @param listenerType the type of listeners requested; this parameter
     * should specify an interface that descends from
     * <code>java.util.EventListener</code>
     * @return an array of all objects registered as
     * <code><em>Foo</em>Listener</code>s on this component,
     * or an empty array if no such
     * listeners have been added
     * @exception ClassCastException if <code>listenerType</code>
     * doesn't specify a class or interface that implements
     * <code>java.util.EventListener</code>
     *
     * @see #getTreeModelListeners
     *
     * @since 1.3
     */

}
