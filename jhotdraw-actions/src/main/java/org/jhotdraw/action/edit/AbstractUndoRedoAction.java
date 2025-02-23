package org.jhotdraw.action.edit;

import java.awt.event.*;
import java.beans.*;
import javax.swing.*;
import org.jhotdraw.action.AbstractViewAction;
import org.jhotdraw.api.app.Application;
import org.jhotdraw.api.app.View;
import org.jhotdraw.util.*;

public abstract class AbstractUndoRedoAction extends AbstractViewAction {

    public String ID;

    private static final long serialVersionUID = 1L;
    private final PropertyChangeListener undoRedoActionPropertyListener = evt -> {
        String name = evt.getPropertyName();
        if (name == null || name.equals(AbstractAction.NAME)) {
            putValue(AbstractAction.NAME, evt.getNewValue());
        } else if ("enabled".equals(name)) {
            updateEnabledState();
        }
    };

    /**
     * Creates a new instance.
     */
    public AbstractUndoRedoAction(Application app, View view, String id) {
        super(app, view);
        this.ID = id;
        ResourceBundleUtil labels = ResourceBundleUtil.getBundle("org.jhotdraw.action.Labels");
        labels.configureAction(this, this.ID);
    }

    protected void updateEnabledState() {
        boolean isEnabled = false;
        Action realAction = getRealAction();
        if (realAction != null && realAction != this) {
            isEnabled = realAction.isEnabled();
        }
        setEnabled(isEnabled);
    }

    @Override
    protected void updateView(View oldValue, View newValue) {
        super.updateView(oldValue, newValue);
        if (newValue != null
                && newValue.getActionMap().get(this.ID) != null
                && newValue.getActionMap().get(this.ID) != this) {
            putValue(AbstractAction.NAME, newValue.getActionMap().get(this.ID).
                    getValue(AbstractAction.NAME));
            updateEnabledState();
        }
    }

    /**
     * Installs listeners on the view object.
     */
    @Override
    protected void installViewListeners(View p) {
        super.installViewListeners(p);
        Action actionInView = p.getActionMap().get(this.ID);
        if (actionInView != null && actionInView != this) {
            actionInView.addPropertyChangeListener(undoRedoActionPropertyListener);
        }
    }

    /**
     * Installs listeners on the view object.
     */
    @Override
    protected void uninstallViewListeners(View p) {
        super.uninstallViewListeners(p);
        Action actionInView = p.getActionMap().get(this.ID);
        if (actionInView != null && actionInView != this) {
            actionInView.removePropertyChangeListener(undoRedoActionPropertyListener);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Action realAction = getRealAction();
        if (realAction != null && realAction != this) {
            realAction.actionPerformed(e);
        }
    }

    private Action getRealAction() {
        return (getActiveView() == null) ? null : getActiveView().getActionMap().get(ID);
    }

}
