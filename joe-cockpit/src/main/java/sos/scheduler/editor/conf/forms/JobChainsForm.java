package sos.scheduler.editor.conf.forms;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.events.FocusAdapter;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.jdom.Element;

import sos.scheduler.editor.app.ContextMenu;
import sos.scheduler.editor.app.MainWindow;
import sos.scheduler.editor.app.Utils;
import sos.scheduler.editor.conf.listeners.JobChainsListener;

import com.sos.joe.globals.JOEConstants;
import com.sos.joe.globals.interfaces.ISchedulerUpdate;
import com.sos.joe.globals.interfaces.IUnsaved;
import com.sos.joe.globals.messages.SOSJOEMessageCodes;
import com.sos.joe.xml.jobscheduler.SchedulerDom;

public class JobChainsForm extends SOSJOEMessageCodes implements IUnsaved {

    private ISchedulerUpdate update = null;
    private JobChainsListener listener = null;
    private Group group = null;
    private static Table tChains = null;
    private Button bRemoveChain = null;
    private Button bNewChain = null;
    private SashForm sashForm = null;
    private Button butDetails = null;
    private SchedulerDom _dom = null;
    private int lastSelectionIndex = -1;
    private boolean checkParameter = false;

    public JobChainsForm(Composite parent, int style, SchedulerDom dom, Element config, ISchedulerUpdate update_) {
        super(parent, style);
        listener = new JobChainsListener(dom, config, update_);
        _dom = dom;
        initialize();
        update = update_;
        listener.fillChains(tChains);
    }

    private void initialize() {
        this.setLayout(new FillLayout());
        createGroup();
        setSize(new org.eclipse.swt.graphics.Point(676, 464));
    }

    private void createGroup() {
        group = new Group(this, SWT.NONE);
        final GridLayout gridLayout = new GridLayout();
        group.setLayout(gridLayout);
        sashForm = new SashForm(group, SWT.VERTICAL);
        sashForm.setLayout(new GridLayout());
        sashForm.setLayoutData(new GridData(GridData.FILL, GridData.FILL, true, true));
        final Group jobchainsGroup = JOE_G_JobChainsForm_JobChains.control(new Group(sashForm, SWT.NONE));
        final GridLayout gridLayout_2 = new GridLayout();
        gridLayout_2.numColumns = 2;
        jobchainsGroup.setLayout(gridLayout_2);
        tChains = JOE_Tbl_JobChainsForm_JobChains.control(new Table(jobchainsGroup, SWT.BORDER));
        tChains.addMouseListener(new MouseAdapter() {

            public void mouseDoubleClick(final MouseEvent e) {
                if (tChains.getSelectionCount() > 0) {
                    ContextMenu.goTo(tChains.getSelection()[0].getText(0), _dom, JOEConstants.JOB_CHAIN);
                }
            }
        });
        tChains.getHorizontalBar().setMaximum(0);
        tChains.setLayoutData(new GridData(GridData.FILL, GridData.FILL, true, true, 1, 3));
        tChains.setHeaderVisible(true);
        tChains.setLinesVisible(true);
        tChains.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {

            public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {

                boolean enabled = true;
                if (tChains.getSelectionCount() > 0) {
                    listener.selectChain(tChains.getSelectionIndex());
                    Element currElem = listener.getChain();
                    if (currElem != null && !Utils.isElementEnabled("job_chain", _dom, currElem)) {
                        enabled = false;
                    }
                    butDetails.setEnabled(true);
                }
                bRemoveChain.setEnabled(tChains.getSelectionCount() > 0 && enabled);
            }
        });
        TableColumn tableColumn1 = JOE_TCl_JobChainsForm_Name.control(new TableColumn(tChains, SWT.NONE));
        tableColumn1.setWidth(150);
        TableColumn ordersRecoverableTableColumn = JOE_TCl_JobChainsForm_OrdersRecoverable.control(new TableColumn(tChains, SWT.NONE));
        ordersRecoverableTableColumn.setWidth(104);
        TableColumn tableColumn2 = JOE_TCl_JobChainsForm_Visible.control(new TableColumn(tChains, SWT.NONE));
        tableColumn2.setWidth(90);
        bNewChain = JOE_B_JobChainsForm_NewChain.control(new Button(jobchainsGroup, SWT.NONE));
        bNewChain.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, false, false));
        getShell().setDefaultButton(bNewChain);
        bNewChain.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {

            public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
                createNewJobChain();
            }
        });
        bRemoveChain = JOE_B_JobChainsForm_RemoveChain.control(new Button(jobchainsGroup, SWT.NONE));
        bRemoveChain.setLayoutData(new GridData(GridData.FILL, GridData.BEGINNING, false, false));
        bRemoveChain.setEnabled(false);
        bRemoveChain.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {

            public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {

                int c = MainWindow.message(getShell(), JOE_M_JobChainsForm_RemoveChain.label(), SWT.ICON_QUESTION | SWT.YES | SWT.NO);
                if (c != SWT.YES) {
                    return;
                }
                if ((tChains.getSelection().length > 0)
                        && (Utils.checkElement(tChains.getSelection()[0].getText(0), listener.get_dom(), JOEConstants.JOB_CHAINS, null))) {
                    deleteChain();
                }
            }
        });
        butDetails = JOE_B_JobChainsForm_Details.control(new Button(jobchainsGroup, SWT.NONE));
        butDetails.setEnabled(false);
        butDetails.addSelectionListener(new SelectionAdapter() {

            public void widgetSelected(final SelectionEvent e) {
                checkParameter = true;
                showDetails(null);
            }
        });
        butDetails.addFocusListener(new FocusAdapter() {

            public void focusGained(final FocusEvent e) {
                if (checkParameter) {
                    listener.fillChains(tChains);
                    checkParameter = false;
                }
            }
        });
        butDetails.setLayoutData(new GridData(GridData.FILL, GridData.BEGINNING, false, false));
    }

    private void deleteChain() {
        if (tChains.getSelectionCount() > 0) {
            int index = tChains.getSelectionIndex();
            listener.deleteChain(index);
            tChains.remove(index);
            if (index >= tChains.getItemCount()) {
                index--;
            }
            if (tChains.getItemCount() > 0) {
                tChains.select(index);
                listener.selectChain(index);
            }
        }
        bRemoveChain.setEnabled(tChains.getSelectionCount() > 0);
    }

    private void applyChain() {
        int i = tChains.getItemCount() + 1;
        String newName = JOE_M_JobChain.label() + i;
        while (listener.indexOf(newName) >= 0) {
            i++;
            newName = JOE_M_JobChain.label() + i;
        }
        listener.applyChain(newName, true, true);
        int index = tChains.getSelectionIndex();
        if (index == -1) {
            index = tChains.getItemCount();
        }
        listener.fillChains(tChains);
        tChains.select(tChains.getItemCount() - 1);
        bRemoveChain.setEnabled(tChains.getSelectionCount() > 0);
        getShell().setDefaultButton(bNewChain);
        if (listener.getChainName() != null && !listener.getChainName().isEmpty()) {
            butDetails.setEnabled(true);
        } else {
            butDetails.setEnabled(false);
        }
    }

    public void setISchedulerUpdate(ISchedulerUpdate update_) {
        update = update_;
    }

    private void showDetails(String state) {
        if (tChains.getSelectionIndex() < 0) {
            tChains.setSelection(lastSelectionIndex);
        }
        String name = tChains.getSelection()[0].getText(0);
        lastSelectionIndex = tChains.getSelectionIndex();
        if (name != null && !name.isEmpty()) {
            boolean isLifeElement = listener.get_dom().isLifeElement() || listener.get_dom().isDirectory();
            if (state == null) {
                DetailDialogForm detail = new DetailDialogForm(name, isLifeElement, listener.get_dom().getFilename());
                detail.showDetails();
                detail.getDialogForm().setParamsForWizzard(_dom, update);
            } else {
                DetailDialogForm detail = new DetailDialogForm(name, state, null, isLifeElement, listener.get_dom().getFilename());
                detail.showDetails();
                detail.getDialogForm().setParamsForWizzard(_dom, update);
            }
        } else {
            MainWindow.message(getShell(), JOE_M_JobAssistent_CancelWizard.label(), SWT.ICON_WARNING | SWT.OK | SWT.CANCEL);
        }
    }

    public void apply() {
        applyChain();
    }

    public boolean isUnsaved() {
        return false;
    }

    public void createNewJobChain() {
        listener.newChain();
        applyChain();
        butDetails.setEnabled(false);
    }

    public static Table getTableChains() {
        return tChains;
    }

}