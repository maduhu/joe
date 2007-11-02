/**
 * 
 */
package sos.scheduler.editor.conf.forms;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Spinner;
import org.eclipse.swt.widgets.Text;

import sos.scheduler.editor.app.Editor;
import sos.scheduler.editor.app.IOUtils;
import sos.scheduler.editor.app.IUpdateLanguage;
import sos.scheduler.editor.app.MergeAllXMLinDirectory;
import sos.scheduler.editor.app.Messages;
import sos.scheduler.editor.app.Options;
import sos.scheduler.editor.app.ResourceManager;
import sos.scheduler.editor.conf.ISchedulerUpdate;
import sos.scheduler.editor.conf.SchedulerDom;
import sos.scheduler.editor.conf.listeners.ConfigListener;


/**
 * @author sky2000
 */
public class ConfigForm extends Composite implements IUpdateLanguage {

    private Button         butBrowse_2                  = null;
    
    private Combo          cConfigurationDeleteEvent    = null;
    
    private Label          label12_3                    = null;
    
    private Button         butBrowse_1                  = null;
    
    private Combo          cConfigurationModifyEvent    = null;
    
    private Label          label12_2                    = null;
    
    private Button         butBrowse                    = null;
    
    private Combo          cConfigurationAddEvent       = null;
    
    private Label          label12_1                    = null;
    
    private ConfigListener listener                     = null;

    private Group          gConfig                      = null;

    private Label          label                        = null;

    private Text           tSpoolerID                   = null;

    private Label          label7                       = null;

    private Text           tParameter                   = null;

    private Label          label10                      = null;

    private Text           tIncludePath                 = null;

    private Label          label11                      = null;

    private Text           tLogDir                      = null;

    private Label          label12                      = null;

    private Text           tMailXSLTStylesheet          = null;

    private Group          gPorts                       = null;

    private Button         cSamePorts                   = null;

    private Label          label14                      = null;

    private Text           sPort                        = null;

    private Text           sTcpPort                     = null;

    private Label          label4                       = null;

    private Text           sUdpPort                     = null;

    private Group          gMainScheduler               = null;

    private Button         cUseMainScheduler            = null;

    private Label          label1                       = null;

    private Text           tMainSchedulerHost           = null;

    private Label          label2                       = null;

    private Spinner        sMainSchedulerPort           = null;

    private Group          gJavaOptions                 = null;

    private Label          label8                       = null;

    private Text           tJavaClassPath               = null;

    private Label          label9                       = null;

    private Text           tJavaOptions                 = null;

    private Group          group                        = null;

    private Text           tComment                     = null;
    
    private Text           tIpAddress                   = null;
    
    private SchedulerDom   _dom                         = null;

    private ISchedulerUpdate update                     = null;

    /**
     * @param parent
     * @param style
     */
    public ConfigForm(Composite parent, int style, SchedulerDom dom) {
        super(parent, style);
        listener = new ConfigListener(dom);
        _dom = dom;
        initialize();
        setToolTipText();
        tSpoolerID.setFocus();
    }


    private void initialize() {
        this.setLayout(new FillLayout());
        createGConfig();
        setSize(new org.eclipse.swt.graphics.Point(714, 501));

        // set all values
        listener.getDom().setInit(true);
        tSpoolerID.setText(listener.getSpoolerID());
        tParameter.setText(listener.getParam());
        tIncludePath.setText(listener.getIncludePath());
        tIpAddress.setText(listener.getIpAddress());
        tLogDir.setText(listener.getLogDir());
        tMailXSLTStylesheet.setText(listener.getMailXSLTStylesheet());

        cSamePorts.setSelection(listener.isPort());
        if (listener.isPort())
            sPort.setText(listener.getPort());
        sTcpPort.setText(listener.getTcpPort());
        sUdpPort.setText(listener.getUdpPort());
        cUseMainScheduler.setSelection(listener.isMainScheduler());

        int port = listener.getMainSchedulerPort();
        tMainSchedulerHost.setText(listener.getMainSchedulerHost());
        sMainSchedulerPort.setSelection(port);

        tJavaClassPath.setText(listener.getJavaClasspath());
        tJavaOptions.setText(listener.getJavaOptions());

        tComment.setText(listener.getComment());

        setEditable();
        listener.getDom().setInit(false);
    }


    private void setEditable() {
        tMainSchedulerHost.setEnabled(cUseMainScheduler.getSelection());
        sMainSchedulerPort.setEnabled(cUseMainScheduler.getSelection());

        sPort.setEnabled(cSamePorts.getSelection());
        sTcpPort.setEnabled(!cSamePorts.getSelection());
        sUdpPort.setEnabled(!cSamePorts.getSelection());
    }


    /**
     * This method initializes gConfig
     */
    private void createGConfig() {
        GridLayout gridLayout1 = new GridLayout();
        GridLayout gridLayout = new GridLayout();
        gridLayout.numColumns = 2;
        gConfig = new Group(this, SWT.NONE);
        gConfig.setText("Config");
        gConfig.setLayout(gridLayout1);
        gConfig.setSize(new Point(798, 516));

        final Group group_1 = new Group(gConfig, SWT.NONE);
        group_1.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, true, false));
        final GridLayout gridLayout_1 = new GridLayout();
        gridLayout_1.numColumns = 2;
        group_1.setLayout(gridLayout_1);
        label = new Label(group_1, SWT.NONE);
        label.setText("Scheduler ID:");
        tSpoolerID = new Text(group_1, SWT.BORDER);
        tSpoolerID.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, true, false));

        tSpoolerID.addModifyListener(new org.eclipse.swt.events.ModifyListener() {
            public void modifyText(org.eclipse.swt.events.ModifyEvent e) {
                listener.setSpoolerID(tSpoolerID.getText());
            }
        });
        label7 = new Label(group_1, SWT.NONE);
        label7.setText("Parameter:");
        tParameter = new Text(group_1, SWT.BORDER);
        tParameter.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, true, false));

        tParameter.addModifyListener(new org.eclipse.swt.events.ModifyListener() {
            public void modifyText(org.eclipse.swt.events.ModifyEvent e) {
                listener.setParam(tParameter.getText());
            }
        });
        label10 = new Label(group_1, SWT.NONE);
        label10.setText("Include Path:");
        tIncludePath = new Text(group_1, SWT.BORDER);
        tIncludePath.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, true, false));

        tIncludePath.addModifyListener(new org.eclipse.swt.events.ModifyListener() {
            public void modifyText(org.eclipse.swt.events.ModifyEvent e) {
                listener.setIncludePath(tIncludePath.getText());
            }
        });

        final Label ipaddressLabel = new Label(group_1, SWT.NONE);
        ipaddressLabel.setText("IP-Address");

        tIpAddress = new Text(group_1, SWT.BORDER);
        tIpAddress.addModifyListener(new ModifyListener() {
        	public void modifyText(final ModifyEvent e) {
            listener.setIpAddress(tIpAddress.getText());
        		
        	}
        });
        tIpAddress.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, true, false));
        label11 = new Label(group_1, SWT.NONE);
        label11.setText("Log Dir:");
        tLogDir = new Text(group_1, SWT.BORDER);
        tLogDir.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, true, false));

        tLogDir.addModifyListener(new org.eclipse.swt.events.ModifyListener() {
            public void modifyText(org.eclipse.swt.events.ModifyEvent e) {
                listener.setLogDir(tLogDir.getText());
            }
        });
        label12 = new Label(group_1, SWT.NONE);
        label12.setText("Mail XSLT");
        tMailXSLTStylesheet = new Text(group_1, SWT.BORDER);
        tMailXSLTStylesheet.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, true, false));
        tMailXSLTStylesheet.addModifyListener(new org.eclipse.swt.events.ModifyListener() {
            public void modifyText(org.eclipse.swt.events.ModifyEvent e) {
                listener.setMailXSLTStylesheet(tMailXSLTStylesheet.getText());
            }
        });
        createGPorts();
        createGMainScheduler();
        createGJavaOptions();
        createGroup();
    }


    /**
     * This method initializes gPorts
     */
    private void createGPorts() {
        GridData gridData42 = new GridData();
        gridData42.horizontalSpan = 2;
        gridData42.verticalAlignment = GridData.CENTER;
        gridData42.grabExcessHorizontalSpace = true;
        gridData42.horizontalAlignment = GridData.FILL;
        GridLayout gridLayout2 = new GridLayout();
        gridLayout2.numColumns = 6;
        GridData gridData7 = new GridData(60, SWT.DEFAULT);
        gridData7.horizontalIndent = 41;
        GridLayout gridLayout11 = new GridLayout();
        gridLayout11.numColumns = 4;
        GridData gridData12 = new GridData(GridData.FILL, GridData.CENTER, false, false);

        final Group eventGroup = new Group(gConfig, SWT.NONE);
        eventGroup.setText("Event");
        eventGroup.setLayoutData(new GridData(GridData.FILL, GridData.FILL, false, false));
        final GridLayout gridLayout = new GridLayout();
        gridLayout.numColumns = 3;
        eventGroup.setLayout(gridLayout);

        label12_1 = new Label(eventGroup, SWT.NONE);
        label12_1.setText("Configuration Add Event");

        cConfigurationAddEvent = new Combo(eventGroup, SWT.NONE);
        final GridData gridData13 = new GridData(GridData.FILL, GridData.CENTER, true, false);
        cConfigurationAddEvent.setLayoutData(gridData13);
        cConfigurationAddEvent.setItems(listener.getJobs());
        cConfigurationAddEvent.addModifyListener(new ModifyListener() {
        	public void modifyText(final ModifyEvent e) {
        		listener.setConfigurationAddEvent(cConfigurationAddEvent.getText());
        	}
        });
        cConfigurationAddEvent.setText(listener.getConfigurationAddEvent());
        

        butBrowse = new Button(eventGroup, SWT.NONE);
        butBrowse.addSelectionListener(new SelectionAdapter() {
        	public void widgetSelected(final SelectionEvent e) {        		
        		String jobname = IOUtils.openDirectoryFile(MergeAllXMLinDirectory.MASK_JOB);
				if(jobname != null && jobname.length() > 0) {
					cConfigurationAddEvent.setText(jobname);
				}
        	}
        });
        butBrowse.setText("Browse");

        label12_2 = new Label(eventGroup, SWT.NONE);
        label12_2.setText("Configuration Modify Event");

        cConfigurationModifyEvent = new Combo(eventGroup, SWT.NONE);
        final GridData gridData13_1 = new GridData(GridData.FILL, GridData.CENTER, true, false);
        cConfigurationModifyEvent.setLayoutData(gridData13_1);
        cConfigurationModifyEvent.setItems(listener.getJobs());
        cConfigurationModifyEvent.addModifyListener(new ModifyListener() {
        	public void modifyText(final ModifyEvent e) {
        		listener.setConfigurationModifyEvent(cConfigurationModifyEvent.getText());
        	}
        });
        cConfigurationModifyEvent.setText(listener.getConfigurationModifyEvent());
        butBrowse_1 = new Button(eventGroup, SWT.NONE);
        butBrowse_1.addSelectionListener(new SelectionAdapter() {
        	public void widgetSelected(final SelectionEvent e) {
        		String jobname = IOUtils.openDirectoryFile(MergeAllXMLinDirectory.MASK_JOB);
				if(jobname != null && jobname.length() > 0)
					cConfigurationModifyEvent.setText(jobname);
        	}
        });
        butBrowse_1.setText("Browse");

        label12_3 = new Label(eventGroup, SWT.NONE);
        label12_3.setText("Configuration Delete Event");

        cConfigurationDeleteEvent = new Combo(eventGroup, SWT.NONE);
        final GridData gridData13_1_1 = new GridData(GridData.FILL, GridData.CENTER, true, false);
        cConfigurationDeleteEvent.setLayoutData(gridData13_1_1);
        cConfigurationDeleteEvent.setItems(listener.getJobs());
        cConfigurationDeleteEvent.addModifyListener(new ModifyListener() {
        	public void modifyText(final ModifyEvent e) {
        		listener.setConfigurationDeleteEvent(cConfigurationDeleteEvent.getText());
        	}
        });
        cConfigurationDeleteEvent.setText(listener.getConfigurationDeleteEvent());

        butBrowse_2 = new Button(eventGroup, SWT.NONE);
        butBrowse_2.addSelectionListener(new SelectionAdapter() {
        	public void widgetSelected(final SelectionEvent e) {
        		String jobname = IOUtils.openDirectoryFile(MergeAllXMLinDirectory.MASK_JOB);
				if(jobname != null && jobname.length() > 0)
					cConfigurationDeleteEvent.setText(jobname);
        	}
        });
        butBrowse_2.setText("Browse");
        gPorts = new Group(gConfig, SWT.NONE);
        gPorts.setText("Job Scheduler Port");
        gPorts.setLayoutData(gridData12);
        gPorts.setLayout(gridLayout11);
        GridData gridData3 = new GridData(GridData.BEGINNING, GridData.CENTER, false, false, 2, 1);
        cSamePorts = new Button(gPorts, SWT.CHECK);
        cSamePorts.setText("Use the same port for udp and tcp");

        cSamePorts.setLayoutData(gridData3);
        cSamePorts.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
            public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
                setEditable();
            }
        });

        final Label tcpLabel = new Label(gPorts, SWT.NONE);
        tcpLabel.setText("TCP:");
        label4 = new Label(gPorts, SWT.NONE);
        label4.setLayoutData(new GridData());
        label4.setText("UDP:");
        label14 = new Label(gPorts, SWT.NONE);
        label14.setText("Port:");
        sPort = new Text(gPorts, SWT.BORDER);

        sPort.setLayoutData(gridData7);
        sPort.addModifyListener(new ModifyListener() {
            public void modifyText(org.eclipse.swt.events.ModifyEvent e) {
                listener.setPort(sPort.getText());
            }
        });
        GridData gridData5 = new GridData(60, SWT.DEFAULT);
        sTcpPort = new Text(gPorts, SWT.BORDER);

        sTcpPort.setLayoutData(gridData5);
        sTcpPort.addModifyListener(new ModifyListener() {
            public void modifyText(org.eclipse.swt.events.ModifyEvent e) {
                listener.setTcpPort(sTcpPort.getText());
            }
        });
        GridData gridData8 = new GridData(60, SWT.DEFAULT);
        sUdpPort = new Text(gPorts, SWT.BORDER);

        sUdpPort.setLayoutData(gridData8);
        sUdpPort.addModifyListener(new ModifyListener() {
            public void modifyText(org.eclipse.swt.events.ModifyEvent e) {
                listener.setUdpPort(sUdpPort.getText());
            }
        });
    }


    /**
     * This method initializes gMainScheduler
     */
    private void createGMainScheduler() {
        GridData gridData6 = new GridData(200, SWT.DEFAULT);
        gridData6.horizontalIndent = 37;
        GridLayout gridLayout7 = new GridLayout();
        gridLayout7.numColumns = 4;
        GridData gridData18 = new GridData();
        gridData18.horizontalSpan = 2;
        gridData18.verticalAlignment = GridData.CENTER;
        gridData18.grabExcessHorizontalSpace = true;
        gridData18.horizontalAlignment = GridData.FILL;
        GridLayout gridLayout3 = new GridLayout();
        gridLayout3.numColumns = 4;
        GridData gridData2 = new GridData();
        gridData2.horizontalSpan = 4;
        GridData gridData11 = new GridData();
        gridData11.widthHint = 60;
        GridData gridData9 = new GridData();
        gridData9.horizontalSpan = 4;
        GridLayout gridLayout4 = new GridLayout();
        gridLayout4.numColumns = 4;
        GridData gridData13 = new GridData();
        gridData13.horizontalAlignment = GridData.FILL;
        gridData13.horizontalSpan = 2;
        gridData13.verticalAlignment = GridData.CENTER;
        gMainScheduler = new Group(gConfig, SWT.NONE);
        gMainScheduler.setText("Main Job Scheduler");
        gMainScheduler.setLayout(gridLayout7);
        gMainScheduler.setLayoutData(gridData13);
        gMainScheduler.setLayout(gridLayout4);
        cUseMainScheduler = new Button(gMainScheduler, SWT.CHECK);
        cUseMainScheduler.setText("use a main scheduler");
        cUseMainScheduler.setLayoutData(gridData2);
        cUseMainScheduler.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
            public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
                setEditable();
            }
        });
        cUseMainScheduler.setLayoutData(gridData9);
        label1 = new Label(gMainScheduler, SWT.NONE);
        label1.setText("Host:");
        tMainSchedulerHost = new Text(gMainScheduler, SWT.BORDER);
        tMainSchedulerHost.setLayoutData(gridData6);
        tMainSchedulerHost.addModifyListener(new org.eclipse.swt.events.ModifyListener() {
            public void modifyText(org.eclipse.swt.events.ModifyEvent e) {
                listener.setMainScheduler(tMainSchedulerHost.getText() + ":" + sMainSchedulerPort.getSelection());
            }
        });
        label2 = new Label(gMainScheduler, SWT.NONE);
        label2.setText("Port:");
        sMainSchedulerPort = new Spinner(gMainScheduler, SWT.NONE);
        sMainSchedulerPort.setMaximum(99000);

        sMainSchedulerPort.setLayoutData(gridData11);
        sMainSchedulerPort.addModifyListener(new ModifyListener() {
            public void modifyText(org.eclipse.swt.events.ModifyEvent e) {
                listener.setMainScheduler(tMainSchedulerHost.getText() + ":" + sMainSchedulerPort.getSelection());
            }
        });
    }


    /**
     * This method initializes gJavaOptions
     */
    private void createGJavaOptions() {
        GridData gridData24 = new GridData();
        gridData24.horizontalIndent = 9;
        gridData24.horizontalAlignment = GridData.FILL;
        gridData24.grabExcessHorizontalSpace = true;
        gridData24.verticalAlignment = GridData.CENTER;
        GridData gridData22 = new GridData();
        gridData22.horizontalIndent = 9;
        gridData22.horizontalAlignment = GridData.FILL;
        gridData22.grabExcessHorizontalSpace = true;
        gridData22.verticalAlignment = GridData.CENTER;
        GridLayout gridLayout8 = new GridLayout();
        gridLayout8.numColumns = 2;
        GridData gridData20 = new GridData(GridData.FILL, GridData.CENTER, true, false);
        GridData gridData19 = new GridData();
        gridData19.horizontalAlignment = GridData.FILL;
        gridData19.verticalAlignment = GridData.CENTER;
        gridData19.horizontalSpan = 2;
        gridData19.grabExcessHorizontalSpace = true;
        GridLayout gridLayout6 = new GridLayout();
        gridLayout6.numColumns = 2;
        gJavaOptions = new Group(gConfig, SWT.NONE);
        gJavaOptions.setText("Main Java Options");
        gJavaOptions.setLayout(gridLayout8);
        gJavaOptions.setLayoutData(gridData20);
        label8 = new Label(gJavaOptions, SWT.NONE);
        label8.setText("Class Path:");
        tJavaClassPath = new Text(gJavaOptions, SWT.BORDER);
        tJavaClassPath.setLayoutData(gridData22);
        tJavaClassPath.addModifyListener(new org.eclipse.swt.events.ModifyListener() {
            public void modifyText(org.eclipse.swt.events.ModifyEvent e) {
                listener.setJavaClasspath(tJavaClassPath.getText());
            }
        });
        label9 = new Label(gJavaOptions, SWT.NONE);
        label9.setText("Options:");
        tJavaOptions = new Text(gJavaOptions, SWT.BORDER);

        tJavaOptions.setLayoutData(gridData24);
        tJavaOptions.addModifyListener(new org.eclipse.swt.events.ModifyListener() {
            public void modifyText(org.eclipse.swt.events.ModifyEvent e) {
                listener.setJavaOptions(tJavaOptions.getText());
            }
        });
    }


    /**
     * This method initializes group
     */
    private void createGroup() {
        GridData gridData4 = new org.eclipse.swt.layout.GridData(GridData.FILL, GridData.FILL, true, true);
        GridData gridData = new org.eclipse.swt.layout.GridData(GridData.FILL, GridData.FILL, true, true);
        group = new Group(gConfig, SWT.NONE);
        group.setText("Comment");
        group.setLayoutData(gridData);
        group.setLayout(new GridLayout());
        tComment = new Text(group, SWT.MULTI | SWT.V_SCROLL | SWT.BORDER | SWT.H_SCROLL);
        tComment.setLayoutData(gridData4);
        tComment.setFont(ResourceManager.getFont("Courier New", 8, SWT.NONE));
        tComment.addModifyListener(new org.eclipse.swt.events.ModifyListener() {
            public void modifyText(org.eclipse.swt.events.ModifyEvent e) {
                listener.setComment(tComment.getText());
            }
        });
    }


    public void setToolTipText() {
        tSpoolerID.setToolTipText(Messages.getTooltip("config.spooler_id"));
        tParameter.setToolTipText(Messages.getTooltip("config.param"));
        tIncludePath.setToolTipText(Messages.getTooltip("config.include_path"));
        tLogDir.setToolTipText(Messages.getTooltip("config.log_dir"));
        tMailXSLTStylesheet.setToolTipText(Messages.getTooltip("config.mail_xslt_stylesheet"));

        cSamePorts.setToolTipText(Messages.getTooltip("config.use_same_port"));
        sPort.setToolTipText(Messages.getTooltip("config.port"));
        sTcpPort.setToolTipText(Messages.getTooltip("config.tcp_port"));
        sUdpPort.setToolTipText(Messages.getTooltip("config.udp_port"));

        cUseMainScheduler.setToolTipText(Messages.getTooltip("config.use_main_scheduler"));
        tMainSchedulerHost.setToolTipText(Messages.getTooltip("config.main_scheduler_host"));
        tIpAddress.setToolTipText(Messages.getTooltip("config.main_scheduler_ip_address"));

        sMainSchedulerPort.setToolTipText(Messages.getTooltip("config.main_scheduler_port"));
        tJavaClassPath.setToolTipText(Messages.getTooltip("config.java_class_path"));
        tComment.setToolTipText(Messages.getTooltip("config.comment"));
        tJavaOptions.setToolTipText(Messages.getTooltip("config.java_options"));
        
        cConfigurationAddEvent.setToolTipText(Messages.getTooltip("config.configuration_add_event"));
        cConfigurationModifyEvent.setToolTipText(Messages.getTooltip("config.configuration_modify_event"));
        cConfigurationDeleteEvent.setToolTipText(Messages.getTooltip("config.configuration_delete_event"));

    }
    
    public void setISchedulerUpdate(ISchedulerUpdate update_) {
		update = update_;
	}
} // @jve:decl-index=0:visual-constraint="10,10"
