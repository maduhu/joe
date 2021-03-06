package com.sos.event.service.forms;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;
import org.jdom.Element;

import com.sos.event.service.listeners.ActionsListListener;
import com.sos.joe.globals.messages.SOSJOEMessageCodes;
import com.sos.joe.xml.Events.ActionsDom;

public class ActionsListForm extends SOSJOEMessageCodes {

    private ActionsListListener listener = null;
    private Group actionsGroup = null;
    private Table list = null;
    private Button butRemove = null;
    private Button butNew = null;
    private ActionsForm gui = null;
    private ActionsDom _dom = null;

    public ActionsListForm(Composite parent, int style, ActionsDom dom, Element actions, ActionsForm _gui) {
        super(parent, style);
        gui = _gui;
        _dom = dom;
        listener = new ActionsListListener(dom, actions);
        initialize();
    }

    private void initialize() {
        createGroup();
        setSize(new Point(696, 462));
        setLayout(new FillLayout());
        listener.fillActions(list);
    }

    private void createGroup() {
        GridLayout gridLayout = new GridLayout();
        gridLayout.numColumns = 2;
        actionsGroup = JOE_G_ActionsListForm_Actions.control(new Group(this, SWT.NONE));
        actionsGroup.setLayout(gridLayout);
        list = JOE_Tbl_ActionsListForm_ActionsList.control(new Table(actionsGroup, SWT.BORDER));
        list.addMouseListener(new MouseAdapter() {

            public void mouseDoubleClick(final MouseEvent e) {
                //
            }
        });
        list.addSelectionListener(new SelectionAdapter() {

            public void widgetSelected(final SelectionEvent e) {
                butRemove.setEnabled(list.getSelectionCount() > 0);
            }
        });
        list.setLayoutData(new GridData(GridData.FILL, GridData.FILL, true, true, 1, 3));
        butNew = JOE_B_ActionsListForm_New.control(new Button(actionsGroup, SWT.NONE));
        butNew.addSelectionListener(new SelectionAdapter() {

            public void widgetSelected(final SelectionEvent e) {
                TableItem item = new TableItem(list, SWT.NONE);
                item.setText(JOE_M_ActionsListForm_NewAction.params(list.getItemCount()));
                listener.newAction(JOE_M_ActionsListForm_NewAction.params(list.getItemCount()));
                listener.fillActions(list);
                butRemove.setEnabled(false);
                gui.updateActions();
            }
        });
        butNew.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, false, false));
        butRemove = JOE_B_ActionsListForm_Remove.control(new Button(actionsGroup, SWT.NONE));
        butRemove.addSelectionListener(new SelectionAdapter() {

            public void widgetSelected(final SelectionEvent e) {
                listener.removeAction(list);
                butRemove.setEnabled(false);
                gui.updateActions();
            }
        });
        new Label(actionsGroup, SWT.NONE);
    }

}