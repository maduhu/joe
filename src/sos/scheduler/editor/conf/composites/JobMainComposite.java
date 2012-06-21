package sos.scheduler.editor.conf.composites;

import org.apache.log4j.Logger;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.FocusAdapter;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.VerifyEvent;
import org.eclipse.swt.events.VerifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
 import org.eclipse.swt.widgets.Text;

 
import sos.scheduler.editor.app.ContextMenu;
import sos.scheduler.editor.app.Editor;
import sos.scheduler.editor.app.IOUtils;
import sos.scheduler.editor.app.MainWindow;
import sos.scheduler.editor.app.MergeAllXMLinDirectory;
import sos.scheduler.editor.app.Messages;
import sos.scheduler.editor.app.Utils;
import sos.scheduler.editor.conf.listeners.JobListener;

public class JobMainComposite extends Composite {
    @SuppressWarnings("unused")
    private final String conSVNVersion = "$Id$";

    private final int intNoOfLabelColumns = 2;
    private final int _style = 0;

    @SuppressWarnings("unused")
    private static Logger logger = Logger.getLogger(JobMainComposite.class);
    @SuppressWarnings("unused")
    private final String conClassName = "JobMainForm";

    private JobListener objDataProvider = null;
    private Group gMain = null;

    private Text tbxJobName = null;
    private Label lblJobTitlelabel1 = null;
    private Label lblProcessClass = null;
    private Text tbxJobTitle = null;
    private Combo cProcessClass = null;
    private Button butBrowse = null;
    private boolean init = true;
    private Button butShowProcessClass = null;
    @SuppressWarnings("unused")
    private Label label = null;
    private int intComboBoxStyle = SWT.NONE;
    private GridLayout gridLayout = null;

    
    

    /**
     * Create the composite.
     * @param parent
     * @param style
     */
    public JobMainComposite(Group parent, int style,JobListener objDataProvider_) {
        super(parent, style);
        objDataProvider = objDataProvider_;
        createGroup(parent);

    }
    
    private void createGroup(final Group parent) {
 
        gridLayout = new GridLayout();
        gridLayout.marginHeight = 1;
        gridLayout.numColumns = 6;

        gMain = new Group(parent, SWT.NONE);
        gMain.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false));
        gMain.setText(Messages.getLabel("MainOptions"));
        gMain.setToolTipText(Messages.getTooltip("MainOptions"));
        gMain.setLayout(gridLayout);

        label = new Label(gMain, SWT.NONE);
        label.setLayoutData(new GridData(GridData.BEGINNING, GridData.END, false, false, intNoOfLabelColumns, 1));
        label.setText(Messages.getLabel("JobName"));
        tbxJobName = new Text(gMain, SWT.BORDER);
        tbxJobName.setToolTipText(Messages.getTooltip("jobname"));

        tbxJobName.addFocusListener(new FocusAdapter() {
            public void focusGained(final FocusEvent e) {
                // tbxJobName.selectAll();
            }

            @Override
            public void focusLost(FocusEvent e) {
            }
        });

        tbxJobName.addKeyListener(new KeyListener() {
            @Override
            public void keyPressed(KeyEvent event) {
                if (event.keyCode == SWT.F1) {
                    MainWindow.message("F1 gedr�ckt", SWT.ICON_INFORMATION);
                    objDataProvider.openHelp(Messages.getF1("jobname")); // "http:www.sos-berlin.com/doc/en/scheduler.doc/xml/job.xml");
                }
            }

            @Override
            public void keyReleased(KeyEvent arg0) {
            }
        });

        tbxJobName.addVerifyListener(new VerifyListener() {
            public void verifyText(final VerifyEvent e) {
                if (!init) {
                    e.doit = Utils.checkElement(objDataProvider.getJobName(), objDataProvider.get_dom(), Editor.JOB, null);
                }
            }
        });
        tbxJobName.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, true, false, 4, 1));
        tbxJobName.addModifyListener(new ModifyListener() {
            public void modifyText(ModifyEvent e) {
                if (init) {
                    return;
                }
                checkName();
                objDataProvider.setJobName(tbxJobName.getText(), true);
                parent.setText(objDataProvider.getJobNameAndTitle());
            }
        });

        lblJobTitlelabel1 = new Label(gMain, SWT.NONE);
        lblJobTitlelabel1.setLayoutData(new GridData(GridData.BEGINNING, GridData.CENTER, false, false, intNoOfLabelColumns, 1));
        lblJobTitlelabel1.setText(Messages.getLabel("jobtitle"));
        lblJobTitlelabel1.setToolTipText(Messages.getTooltip("jobtitle"));

        // tbxJobTitle = new Combo(gMain, SWT.BORDER);
        tbxJobTitle = new Text(gMain, SWT.BORDER);
        tbxJobTitle.setToolTipText(Messages.getTooltip("jobtitle"));
        tbxJobTitle.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, false, false, 4, 1));
        tbxJobTitle.addModifyListener(new ModifyListener() {
            public void modifyText(ModifyEvent e) {
                if (init)
                    return;
                objDataProvider.setTitle(tbxJobTitle.getText());
            }
        });

        // tbxJobTitle.setItems(Options.getJobTitleList());

        lblProcessClass = new Label(gMain, SWT.NONE);
        lblProcessClass.setText(Messages.getLabel("ProcessClass"));
        lblProcessClass.setToolTipText(Messages.getTooltip("ProcessClass"));

        butShowProcessClass = new Button(gMain, SWT.ARROW | SWT.DOWN);
        butShowProcessClass.setVisible(objDataProvider.get_dom() != null && !objDataProvider.get_dom().isLifeElement());
        butShowProcessClass.setToolTipText(Messages.getTooltip("goto"));
        butShowProcessClass.addSelectionListener(new SelectionAdapter() {
            public void widgetSelected(final SelectionEvent e) {
                String strT = cProcessClass.getText();
                if (strT.length() > 0) {
                    ContextMenu.goTo(strT, objDataProvider.get_dom(), Editor.PROCESS_CLASSES);
                }
            }
        });
        butShowProcessClass.setAlignment(SWT.RIGHT);
        butShowProcessClass.setVisible(true);

        cProcessClass = new Combo(gMain, intComboBoxStyle);
        cProcessClass.setToolTipText(Messages.getTooltip("ProcessClass"));
        cProcessClass.setMenu(new ContextMenu(cProcessClass, objDataProvider.get_dom(), Editor.PROCESS_CLASSES).getMenu());

        cProcessClass.addModifyListener(new ModifyListener() {
            public void modifyText(final ModifyEvent e) {

                if (init) {
                    return;
                }
                objDataProvider.setProcessClass(cProcessClass.getText());
                butShowProcessClass.setVisible(true);
            }
        });
        cProcessClass.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, false, false, 3, 1));
        cProcessClass.addSelectionListener(new SelectionAdapter() {
            public void widgetSelected(SelectionEvent e) {

                if (init) {
                    return;
                }
                objDataProvider.setProcessClass(cProcessClass.getText());
            }
        });

        cProcessClass.addKeyListener(new KeyListener() {
            @Override
            public void keyPressed(KeyEvent event) {
                if (event.keyCode == SWT.F1) {
                    objDataProvider.openXMLAttributeDoc("job", "process_class");
                }
                if (event.keyCode == SWT.F10) {
                    objDataProvider.openXMLDoc("job");
                }
            }

            @Override
            public void keyReleased(KeyEvent arg0) {
            }
        });

        cProcessClass.addMouseListener(new MouseListener() {

            @Override
            public void mouseUp(MouseEvent arg0) {
                // TODO Auto-generated method stub

            }

            @Override
            public void mouseDown(MouseEvent arg0) {
                // TODO Auto-generated method stub

            }

            @Override
            public void mouseDoubleClick(MouseEvent arg0) {
                String strT = cProcessClass.getText();
                if (strT.length() > 0) {
                    ContextMenu.goTo(strT, objDataProvider.get_dom(), Editor.PROCESS_CLASSES);
                }
            }
        });

        // -----

        butBrowse = new Button(gMain, SWT.NONE);
        butBrowse.addSelectionListener(new SelectionAdapter() {
            public void widgetSelected(final SelectionEvent e) {
                String name = IOUtils.openDirectoryFile(MergeAllXMLinDirectory.MASK_PROCESS_CLASS);
                if (name != null && name.length() > 0)
                    cProcessClass.setText(name);
            }
        });
        butBrowse.setText(Messages.getLabel("Browse.ProcessClass"));
        butBrowse.setToolTipText(Messages.getTooltip("Browse.ProcessClass"));

    }

    public void init(){
        init = true;
        tbxJobName.setText(objDataProvider.getJobName());
        tbxJobTitle.setText(objDataProvider.getTitle());
        tbxJobName.setFocus();
        String process_class = objDataProvider.getProcessClass();
        cProcessClass.setItems(objDataProvider.getProcessClasses());
        cProcessClass.setText(process_class);
        init = false;


    }
    
    private void checkName() {
        if (Utils.existName(tbxJobName.getText(), objDataProvider.getJob(), "job")) {
            tbxJobName.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_YELLOW));
        } else {
            tbxJobName.setBackground(null);
        }
    }
    
    

    @Override protected void checkSubclass() {
        // Disable the check that prevents subclassing of SWT components
    }

    public Group getgMain() {
        return gMain;
    }
 

}
