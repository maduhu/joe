package sos.scheduler.editor.app;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;

public class MainWindow {
    private static Shell sShell        = null; // @jve:decl-index=0:visual-constraint="3,1"

    private MainListener listener      = null;

    private IContainer   container     = null;

    private Menu         menuBar       = null;

    private Menu         mFile         = null;

    private Menu         submenu       = null;

    private Menu         menuLanguages = null;

    private Menu         submenu1      = null;


    public MainWindow() {
        super();

    }


    /**
     * This method initializes composite
     */
    private void createContainer() {
        container = new TabbedContainer(this, sShell);
    }


    /**
     * This method initializes sShell
     */
    public void createSShell() {
        sShell = new Shell();
        sShell.setText("Job Scheduler Editor");
        sShell.setData(sShell.getText());

        sShell.setImage(ResourceManager.getImageFromResource("/sos/scheduler/editor/editor.png"));
        sShell.setLayout(new FillLayout());
        createContainer();

        listener = new MainListener(this, container);

        sShell.setSize(new org.eclipse.swt.graphics.Point(895, 625));
        sShell.setMinimumSize(890, 620);

        // load resources
        listener.loadOptions();
        listener.loadMessages();
        // Options.loadSash("main", sashForm);
        Options.loadWindow(sShell, "editor");

        menuBar = new Menu(sShell, SWT.BAR);
        MenuItem submenuItem2 = new MenuItem(menuBar, SWT.CASCADE);
        submenuItem2.setText("&File");
        mFile = new Menu(submenuItem2);
        MenuItem pNew = new MenuItem(mFile, SWT.PUSH);
        pNew.setText("New Configuration\tCtrl+N");
        pNew.setAccelerator(SWT.CTRL | 'N');
        pNew.addSelectionListener(new org.eclipse.swt.events.SelectionListener() {
            public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
                if (container.newScheduler() != null)
                    setSaveStatus();
            }


            public void widgetDefaultSelected(org.eclipse.swt.events.SelectionEvent e) {
            }
        });
        MenuItem pOpenFile = new MenuItem(mFile, SWT.PUSH);
        pOpenFile.setText("Open Configuration...\tCtrl+O");
        pOpenFile.setAccelerator(SWT.CTRL | 'O');
        MenuItem separator2 = new MenuItem(mFile, SWT.SEPARATOR);
        MenuItem push = new MenuItem(mFile, SWT.PUSH);
        push.setText("New Documentation\tCtrl+M"); // Generated
        push.addSelectionListener(new org.eclipse.swt.events.SelectionListener() {
            public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
                if (container.newDocumentation() != null)
                    setSaveStatus();
            }


            public void widgetDefaultSelected(org.eclipse.swt.events.SelectionEvent e) {
            }
        });
        MenuItem push1 = new MenuItem(mFile, SWT.PUSH);
        push1.setText("Open Documentation...\tCtrl+P"); // Generated
        push1.addSelectionListener(new org.eclipse.swt.events.SelectionListener() {
            public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
                if (container.openDocumentation() != null)
                    setSaveStatus();
            }


            public void widgetDefaultSelected(org.eclipse.swt.events.SelectionEvent e) {
            }
        });
        MenuItem separator = new MenuItem(mFile, SWT.SEPARATOR);
        pOpenFile.addSelectionListener(new org.eclipse.swt.events.SelectionListener() {
            public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
                if (container.openScheduler() != null)
                    setSaveStatus();
            }


            public void widgetDefaultSelected(org.eclipse.swt.events.SelectionEvent e) {
            }
        });
        MenuItem pSaveFile = new MenuItem(mFile, SWT.PUSH);
        pSaveFile.setText("Save\tCtrl+S");
        pSaveFile.setAccelerator(SWT.CTRL | 'S');
        pSaveFile.setEnabled(false);
        pSaveFile.addSelectionListener(new org.eclipse.swt.events.SelectionListener() {
            public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
                if (container.getCurrentEditor().applyChanges()) {
                    container.getCurrentEditor().save();
                    setSaveStatus();
                }
            }


            public void widgetDefaultSelected(org.eclipse.swt.events.SelectionEvent e) {
            }
        });
        MenuItem pSaveAs = new MenuItem(mFile, SWT.PUSH);
        pSaveAs.setText("Save As...\tCtrl+A");
        pSaveAs.setAccelerator(SWT.CTRL | 'A');
        pSaveAs.setEnabled(false);
        pSaveAs.addSelectionListener(new org.eclipse.swt.events.SelectionListener() {
            public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
                if (container.getCurrentEditor() != null && container.getCurrentEditor().applyChanges()) {
                    container.getCurrentEditor().saveAs();
                    setSaveStatus();
                }
            }


            public void widgetDefaultSelected(org.eclipse.swt.events.SelectionEvent e) {
            }
        });
        MenuItem separator1 = new MenuItem(mFile, SWT.SEPARATOR);
        submenuItem2.setMenu(mFile);
        MenuItem pExit = new MenuItem(mFile, SWT.PUSH);
        pExit.setText("Exit\tCtrl+E");
        pExit.setAccelerator(SWT.CTRL | 'E');
        MenuItem submenuItem = new MenuItem(menuBar, SWT.CASCADE);
        submenuItem.setText("Options");
        MenuItem submenuItem3 = new MenuItem(menuBar, SWT.CASCADE);
        submenuItem3.setText("&Help");
        submenu1 = new Menu(submenuItem3);
        MenuItem pHelp = new MenuItem(submenu1, SWT.PUSH);
        pHelp.setText("Help\tF1");
        pHelp.setAccelerator(SWT.F1);
        pHelp.addSelectionListener(new org.eclipse.swt.events.SelectionListener() {
            public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
            	  if (container.getCurrentEditor() != null) {
                  listener.openHelp(container.getCurrentEditor().getHelpKey());
            	  }else {
            	  	String msg = "Help is available after documentation or configuration is opened";
            	  	MainWindow.message(msg, SWT.ICON_INFORMATION);
            	  }
            }


            public void widgetDefaultSelected(org.eclipse.swt.events.SelectionEvent e) {
            }
        });
        MenuItem pAbout = new MenuItem(submenu1, SWT.PUSH);
        pAbout.setText("About");
        pAbout.addSelectionListener(new org.eclipse.swt.events.SelectionListener() {
            public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
                listener.showAbout();
            }


            public void widgetDefaultSelected(org.eclipse.swt.events.SelectionEvent e) {
            }
        });
        submenuItem3.setMenu(submenu1);
        submenu = new Menu(submenuItem);
        MenuItem submenuItem1 = new MenuItem(submenu, SWT.CASCADE);
        submenuItem1.setText("Help Language");
        menuLanguages = new Menu(submenuItem1);

        // create languages menu
        listener.setLanguages(menuLanguages);

        submenuItem1.setMenu(menuLanguages);
        submenuItem.setMenu(submenu);
        pExit.addSelectionListener(new org.eclipse.swt.events.SelectionListener() {
            public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
                sShell.close();
            }


            public void widgetDefaultSelected(org.eclipse.swt.events.SelectionEvent e) {
            }
        });
        sShell.setMenuBar(menuBar);
        sShell.addShellListener(new org.eclipse.swt.events.ShellAdapter() {
            public void shellClosed(org.eclipse.swt.events.ShellEvent e) {
                e.doit = container.closeAll();
                setSaveStatus();
                Options.saveWindow(sShell, "editor");
                listener.saveOptions();
                ResourceManager.dispose();
            }


            public void shellActivated(org.eclipse.swt.events.ShellEvent e) {
                setSaveStatus();
            }
        });
    }


    public static Shell getSShell() {
        return sShell;
    }


    private void setSaveStatus() {
        setMenuStatus();
        container.setStatusInTitle();
    }


    public boolean setMenuStatus() {
        boolean saved = true;
        if (container.getCurrentEditor() != null)
            saved = !container.getCurrentEditor().hasChanges();

        MenuItem[] items = mFile.getItems();
        items[6].setEnabled(!saved);
        items[7].setEnabled(true);

        return saved;
    }


    public static int message(String message, int style) {
        MessageBox mb = new MessageBox(getSShell(), style);
        mb.setMessage(message);

        String title = "Message";
        if ((style & SWT.ICON_ERROR) != 0)
            title = "Error";
        else if ((style & SWT.ICON_INFORMATION) != 0)
            title = "Information";
        else if ((style & SWT.ICON_QUESTION) != 0)
            title = "Question";
        else if ((style & SWT.ICON_WARNING) != 0)
            title = "Warning";
        mb.setText(title);

        return mb.open();
    }
}