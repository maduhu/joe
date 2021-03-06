package sos.scheduler.editor.conf.forms;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Text;

import sos.scheduler.editor.conf.listeners.CommandsListener;

import com.sos.joe.globals.interfaces.ISchedulerUpdate;
import com.sos.joe.globals.interfaces.IUpdateLanguage;
import com.sos.joe.globals.messages.SOSJOEMessageCodes;
import com.sos.joe.globals.misc.ResourceManager;
import com.sos.joe.xml.jobscheduler.SchedulerDom;


public class CommandsForm extends SOSJOEMessageCodes implements IUpdateLanguage {

    private Text              tCommands;

    private final CommandsListener  listener;

    //private SchedulerListener mainListener;

    private Group             commandsGroup = null;

    private Button            bSave         = null;



    public CommandsForm(final Composite parent, final int style, final SchedulerDom dom, final ISchedulerUpdate main) throws Exception {
        super(parent, style);
        listener = new CommandsListener(dom, main);
        initialize();
        setToolTipText();
        tCommands.setText(listener.readCommands());
    }


    private void initialize() {
        this.setLayout(new FillLayout());
        createGroup();
        setSize(new org.eclipse.swt.graphics.Point(656, 400));
    }


    /**
     * This method initializes group
     */
    private void createGroup() {
        GridLayout gridLayout = new GridLayout();
        gridLayout.numColumns = 2;
        commandsGroup = JOE_G_CommandsForm_Commands.Control(new Group(this, SWT.NONE));
//        commandsGroup.setText("Commands");
        commandsGroup.setLayout(gridLayout);
        createTable();

        tCommands = JOE_T_CommandsForm_Commands.Control(new Text(commandsGroup, SWT.V_SCROLL | SWT.MULTI | SWT.BORDER | SWT.H_SCROLL));
        tCommands.addKeyListener(new KeyAdapter() {
        	@Override
			public void keyPressed(final KeyEvent e) {
        		if(e.keyCode==97 && e.stateMask == SWT.CTRL){
        			tCommands.setSelection(0, tCommands.getText().length());
				}
        	}
        });
        tCommands.setFont(ResourceManager.getFont("Courier New", 8, SWT.NONE));
        final GridData gridData4_1 = new GridData(GridData.FILL, GridData.FILL, true, true);
        tCommands.setLayoutData(gridData4_1);
        
        GridData gridData = new org.eclipse.swt.layout.GridData(GridData.FILL, GridData.BEGINNING, false, false);
        bSave = JOE_B_CommandsForm_Apply.Control(new Button(commandsGroup, SWT.NONE));
//        bSave.setText("&Apply");
        bSave.setLayoutData(gridData);
        getShell().setDefaultButton(bSave);
        bSave.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
            @Override
			public void widgetSelected(final org.eclipse.swt.events.SelectionEvent e) {
                listener.saveCommands(tCommands.getText());
            }
        });
    }
    /**
     * This method initializes table
     */
    private void createTable() {
    }
    
    @Override
	public void setToolTipText() {
//        bSave.setToolTipText(Messages.getTooltip("commands.btn_save"));
//        tCommands.setToolTipText(Messages.getTooltip("commands.commands"));
    }
} // @jve:decl-index=0:visual-constraint="10,10"
