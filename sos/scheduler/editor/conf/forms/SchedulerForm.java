package sos.scheduler.editor.conf.forms;

import java.util.Collection;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;

import sos.scheduler.editor.app.IContainer;
import sos.scheduler.editor.app.IEditor;
import sos.scheduler.editor.app.IOUtils;
import sos.scheduler.editor.app.IUpdateLanguage;
import sos.scheduler.editor.app.Options;
import sos.scheduler.editor.app.TreeData;
import sos.scheduler.editor.app.TreeMenu;
import sos.scheduler.editor.app.Utils;
import sos.scheduler.editor.conf.ISchedulerUpdate;
import sos.scheduler.editor.conf.SchedulerDom;
import sos.scheduler.editor.conf.listeners.SchedulerListener;

public class SchedulerForm extends Composite implements ISchedulerUpdate, IEditor {
    private SchedulerDom      dom;

    private SchedulerListener listener;

    private IContainer        container;

    private TreeItem          selection;

    private SashForm          sashForm  = null;

    private Group             gTree     = null;

    private Tree              tree      = null;

    private Composite         cMainForm = null;


    public SchedulerForm(IContainer container, Composite parent, int style) {
        super(parent, style);
        this.container = container;

        // initialize();
        this.dom = new SchedulerDom();
        this.dom.setDataChangedListener(this);

        listener = new SchedulerListener(this, this.dom);

    }


    private void initialize() {
        FillLayout fillLayout = new FillLayout();
        fillLayout.spacing = 0;
        fillLayout.marginWidth = 5;
        fillLayout.marginHeight = 5;
        setSize(new Point(783, 450));
        setLayout(fillLayout);
        createSashForm();
    }


    /**
     * This method initializes sashForm
     */
    private void createSashForm() {
        sashForm = new SashForm(this, SWT.NONE);
        createGTree();
        createCMainForm();
        sashForm.setWeights(new int[] { 176, 698 });
        Options.loadSash("main", sashForm);
    }


    /**
     * This method initializes gTree
     */
    private void createGTree() {
        gTree = new Group(sashForm, SWT.NONE);
        gTree.setLayout(new FillLayout());
        gTree.setText("Scheduler Elements");
        tree = new Tree(gTree, SWT.BORDER);
        tree.setMenu(new TreeMenu(tree, dom, this).getMenu());
        tree.addListener(SWT.MenuDetect, new Listener() {
            public void handleEvent(Event e) {
                e.doit = tree.getSelectionCount() > 0;
            }
        });
        tree.addListener(SWT.Selection, new Listener() {
            public void handleEvent(Event e) {
                if (tree.getSelectionCount() > 0) {
                    if (selection == null)
                        selection = tree.getItem(0);

                    e.doit = listener.treeSelection(tree, cMainForm);

                    if (!e.doit) {
                        tree.setSelection(new TreeItem[] { selection });
                    } else {
                        selection = tree.getSelection()[0];
                    }
                }
            }
        });
    }


    /**
     * This method initializes cMainForm
     */
    private void createCMainForm() {
        cMainForm = new Composite(sashForm, SWT.NONE);
        cMainForm.setLayout(new FillLayout());
    }


    public Shell getSShell() {
        return this.getShell();
    }


    public void updateLanguage() {
        if (cMainForm.getChildren().length > 0) {
            if (cMainForm.getChildren()[0] instanceof IUpdateLanguage) {
                ((IUpdateLanguage) cMainForm.getChildren()[0]).setToolTipText();
            }
        }
    }


    public void dataChanged() {
        container.setStatusInTitle();
    }


    public void updateCommand(String s) {
        TreeItem item = tree.getSelection()[0];
        item.setText(s);
    }


    public void updateCommands() {
        if (tree.getSelectionCount() > 0) {
            TreeItem item = tree.getSelection()[0];
            TreeData data = (TreeData) item.getData();
            listener.treeFillCommands(tree.getSelection()[0], data.getElement(), true);
        }
    }


    public void updateDays(int type) {
        if (tree.getSelectionCount() > 0) {
            TreeItem item = tree.getSelection()[0];
            TreeData data = (TreeData) item.getData();
            listener.treeFillDays(item, data.getElement(), type, true);
        }
    }


    public void updateJob() {
        if (tree.getSelectionCount() > 0) {
            TreeItem item = tree.getSelection()[0];
            TreeData data = (TreeData) item.getData();
            listener.treeFillJob(item, data.getElement(), true);
        }
    }


    public void updateJob(String s) {
        TreeItem item = tree.getSelection()[0];
        String job = "Job: " + s;
        item.setText(job);
    }


    public void updateJobs() {
        if (tree.getSelectionCount() > 0)
            listener.treeFillJobs(tree.getSelection()[0]);
    }


    public void updateOrder(String s) {
        TreeItem item = tree.getSelection()[0];
        String order = "Order: " + s;
        item.setText(order);
    }


    public void updateOrders() {
        if (tree.getSelectionCount() > 0) {
            listener.treeFillOrders(tree.getSelection()[0], true);
        }
    }


    public boolean applyChanges() {
        Control[] c = cMainForm.getChildren();
        return c.length == 0 || Utils.applyFormChanges(c[0]);
    }


    public void openBlank() {
        initialize();
        // dom.initScheduler();
        listener.treeFillMain(tree, cMainForm);
    }


    public boolean open(Collection files) {
        boolean res = IOUtils.openFile(files, dom);
        if (res) {
            initialize();
            listener.treeFillMain(tree, cMainForm);
        }

        return res;
    }


    public boolean save() {
        boolean res = IOUtils.saveFile(dom, false);
        if (res)
            container.setNewFilename(null);
        return res;
    }


    public boolean saveAs() {
        String old = dom.getFilename();
        boolean res = IOUtils.saveFile(dom, true);
        if (res)
            container.setNewFilename(old);
        return res;
    }


    public boolean close() {
        return applyChanges() && IOUtils.continueAnyway(dom);
    }


    public boolean hasChanges() {
        Options.saveSash("main", sashForm.getWeights());

        return dom.isChanged();
    }


    public String getHelpKey() {
        if (tree.getSelectionCount() > 0) {
            TreeItem item = tree.getSelection()[0];
            TreeData data = (TreeData) item.getData();
            if (data != null && data.getHelpKey() != null)
                return data.getHelpKey();
        }
        return null;
    }


    public String getFilename() {
        return dom.getFilename();
    }
}