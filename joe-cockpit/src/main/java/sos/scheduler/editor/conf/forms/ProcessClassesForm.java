/**
 * 
 */
package sos.scheduler.editor.conf.forms;
import static com.sos.joe.globals.messages.SOSJOEMessageCodes.JOE_Cbo_JCNodesForm_OnError;
import static com.sos.joe.globals.messages.SOSJOEMessageCodes.JOE_M_JCNodesForm_Setback;
import static com.sos.joe.globals.messages.SOSJOEMessageCodes.JOE_M_JCNodesForm_Suspend;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.TraverseEvent;
import org.eclipse.swt.events.TraverseListener;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.jdom.Element;
import org.jdom.JDOMException;

import sos.scheduler.editor.app.MainWindow;
import sos.scheduler.editor.app.Utils;
import sos.scheduler.editor.conf.listeners.ProcessClassesListener;

import com.sos.dialog.components.IntegerField;
import com.sos.joe.globals.JOEConstants;
import com.sos.joe.globals.interfaces.IUnsaved;
import com.sos.joe.globals.messages.ErrorLog;
import com.sos.joe.globals.messages.Messages;
import com.sos.joe.globals.messages.SOSJOEMessageCodes;
import com.sos.joe.xml.jobscheduler.SchedulerDom;


public class ProcessClassesForm extends SOSJOEMessageCodes implements IUnsaved {
	
    private ProcessClassesListener	listener		        = null;
	private Group					group                   = null; 
    private Table                   tableRemoteScheduler    = null;
    private static Table            tableProcessClasses     = null;
	private Button					btRemove			    = null;
    private Button                  btNew                   = null;
    private Button                  btNewRemoteScheduler    = null;
    private Button                  btApply                 = null;
    private Button                  btOkRemoteScheduler     = null;
    private Button                  btRemoveRemoteScheduler = null;
	private Text					tProcessClass	        = null;
	private IntegerField    		tMaxProcesses	        = null;
	private Text                    tRemoteUrl              = null;
	private IntegerField            tHttpHeartBeatPeriod    = null;
	private IntegerField            tHttpHeartBeatTimeout   = null;
    private Text                    tRemoteSchedulerUrl     = null;
	private SchedulerDom			dom				        = null;
    private Combo                   cSelect                 = null;

 
	public ProcessClassesForm(Composite parent, int style, SchedulerDom dom_, Element config) throws JDOMException {
		super(parent, style);
		dom = dom_;
		listener = new ProcessClassesListener(dom, config);
		initialize();
	}

	public void apply() {
		if (isUnsaved())
			applyClass();
	}

	public boolean isUnsaved() {
		return btApply.isEnabled();
	}

	private void initialize() {
		this.setLayout(new FillLayout());
		createGroup();
	    setSize(new org.eclipse.swt.graphics.Point(694, 294));
	    if (dom.isLifeElement()) {
	       if (tableProcessClasses.getItemCount() > 0)
	           tableProcessClasses.setSelection(0);
	           listener.selectProcessClass(0);
	           setInput(true);
	           tProcessClass.setBackground(null);
	           setEnabled(true);
	           tableProcessClasses.setVisible(false);
	           btNew.setVisible(false);
	           btRemove.setVisible(false);
	        }
	     listener.fillProcessClassesTable(tableProcessClasses);
	     new Label(group, SWT.NONE);
	
	}

 
	private void createGroup() {
 
	    group = JOE_G_ProcessClassesForm_ProcessClasses.Control(new Group(this, SWT.NONE));
        group.setLayout(new GridLayout(5, false));

         
        Label lbProcessClass = JOE_L_ProcessClassesForm_ProcessClass.Control(new Label(group, SWT.NONE));
        lbProcessClass.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1));
        new Label(group, SWT.NONE);
        new Label(group, SWT.NONE);
        new Label(group, SWT.NONE);
        new Label(group, SWT.NONE);

        
        tProcessClass = JOE_T_ProcessClassesForm_ProcessClass.Control(new Text(group, SWT.BORDER));
        tProcessClass.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 4, 1));
        tProcessClass.setEnabled(false);
        
        tProcessClass.addTraverseListener(new TraverseListener() {
            public void keyTraversed(final TraverseEvent e) {
                if (!listener.isValidClass(tProcessClass.getText()) || dom.isLifeElement()) {
                    e.doit = false;
                    return;
                }
                traversed(e);
            }
        });
        tProcessClass.addModifyListener(new org.eclipse.swt.events.ModifyListener() {
            public void modifyText(org.eclipse.swt.events.ModifyEvent e) {
                boolean valid = listener.isValidClass(tProcessClass.getText()) || dom.isLifeElement();
                if (valid){
                    tProcessClass.setBackground(null);
                }else{
                    tProcessClass.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_YELLOW));
                }
                btApply.setEnabled(valid);
            }
        });
         
        btApply = JOE_B_ProcessClassesForm_Apply.Control(new Button(group, SWT.NONE));
        btApply.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
        btApply.setEnabled(false);
        btApply.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
            public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
                applyClass();
            }
        });

        
        Label lbMaxProcesses = JOE_L_ProcessClassesForm_MaxProcesses.Control(new Label(group, SWT.NONE));
        lbMaxProcesses.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false, false, 1, 1));
       
        final Label lbUrl = new Label(group, SWT.NONE);
        lbUrl.setText("Url");
        lbUrl.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false, false, 1, 1));
        new Label(group, SWT.NONE);
      
        new Label(group, SWT.NONE);
        new Label(group, SWT.NONE);
        
        tMaxProcesses = JOE_T_ProcessClassesForm_MaxProcesses.integerField(new IntegerField(group, SWT.BORDER));
        tMaxProcesses.setLayoutData(new GridData(GridData.FILL, SWT.CENTER, true, false,1,1));
        tMaxProcesses.addModifyListener(new ModifyListener() {
            public void modifyText(ModifyEvent arg0) {
                btApply.setEnabled(true);
            }
        });
        tMaxProcesses.addTraverseListener(new TraverseListener() {
            public void keyTraversed(final TraverseEvent e) {
                traversed(e);
            }
        });
        tMaxProcesses.setEnabled(false);
        tMaxProcesses.addKeyListener(new org.eclipse.swt.events.KeyAdapter() {
            public void keyPressed(org.eclipse.swt.events.KeyEvent e) {
                if (e.keyCode == SWT.CR) {
                    applyClass();
                    btNew.setEnabled(!btApply.getEnabled());
                }
            }
        });
        
        
        tRemoteUrl = JOE_T_ProcessClassesForm_remoteExecution.Control(new Text(group, SWT.BORDER));
        tRemoteUrl.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false,1,1));
        tRemoteUrl.setEnabled(false);

        tRemoteUrl.addTraverseListener(new TraverseListener() {
            public void keyTraversed(final TraverseEvent e) {
                traversed(e);
            }
        });
        tRemoteUrl.addModifyListener(new ModifyListener() {
            public void modifyText(final ModifyEvent e) {
                btApply.setEnabled(true);
            }
        });
        new Label(group, SWT.NONE);
        
        new Label(group, SWT.NONE);
        new Label(group, SWT.NONE);
        
        
        
            

        
        createTableRemoteScheduler();
        
        GridData gridData3 = new GridData(SWT.FILL, SWT.TOP, false, false,1,1);
        gridData3.widthHint = 100;
        btNewRemoteScheduler = JOE_B_ProcessClassesForm_NewRemoteScheduler.Control(new Button(group, SWT.NONE));
        btNewRemoteScheduler.setLayoutData(gridData3);
        btNewRemoteScheduler.setEnabled(false);
        btNewRemoteScheduler.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
            public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
                tableRemoteScheduler.setSelection(-1);
                tRemoteSchedulerUrl.setText("");
                tHttpHeartBeatPeriod.setText("");
                tHttpHeartBeatTimeout.setText("");
                tRemoteSchedulerUrl.setFocus();
                btNewRemoteScheduler.setEnabled(false);
            }
        });
        
        Label lbSeperator = new Label(group, SWT.SEPARATOR | SWT.HORIZONTAL);
        lbSeperator.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, false, false));
        
        GridData gridData4 = new GridData(SWT.FILL, SWT.TOP, false, false,1,1);
        gridData4.widthHint = 100;
        btRemoveRemoteScheduler = JOE_B_ProcessClassesForm_RemoveRemoteScheduler.Control(new Button(group, SWT.NONE));
        btRemoveRemoteScheduler.setLayoutData(gridData4);
        btRemoveRemoteScheduler.setEnabled(false);

        btRemoveRemoteScheduler.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
            public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
                if (tableRemoteScheduler.getSelectionCount() > 0) {
                    int index = tableRemoteScheduler.getSelectionIndex();
                    tableRemoteScheduler.remove(index);
                    tableRemoteScheduler.setSelection(-1);
                    tRemoteSchedulerUrl.setText("");
                    tHttpHeartBeatPeriod.setText("");
                    tHttpHeartBeatTimeout.setText("");
                    tRemoteSchedulerUrl.setFocus();
                    btRemoveRemoteScheduler.setEnabled(false);
                    btApply.setEnabled(true);
                }
                btRemoveRemoteScheduler.setEnabled(tableRemoteScheduler.getSelectionCount() > 0);
            }
        });

        
        new Label(group, SWT.NONE);
        new Label(group, SWT.NONE);
               
        Label lbSchedulerRemoteUrl = new Label(group, SWT.NONE);
        lbSchedulerRemoteUrl.setLayoutData(new GridData(SWT.CENTER, SWT.BOTTOM, false, false, 1, 1));
        lbSchedulerRemoteUrl.setText("Url");
        new Label(group, SWT.NONE);
        new Label(group, SWT.NONE);
        new Label(group, SWT.NONE);

        
      
        GridData layoutDataOkButton = new GridData(SWT.LEFT, SWT.BOTTOM, false, false, 1, 1);
        layoutDataOkButton.widthHint = 100;
        btOkRemoteScheduler = JOE_B_ProcessClassesForm_ApplyRemoteScheduler.Control(new Button(group, SWT.NONE));
        btOkRemoteScheduler.setLayoutData(layoutDataOkButton);
        btOkRemoteScheduler.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
            public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
            	applyRemoteSchedulerEntry();
 
            }
        });

        tRemoteSchedulerUrl = JOE_T_ProcessClassesForm_remoteExecution.Control(new Text(group, SWT.BORDER));
        tRemoteSchedulerUrl.setLayoutData(new GridData(SWT.FILL, SWT.BOTTOM, true, false, 3, 1));
        
        new Label(group, SWT.NONE);

        final Label lbSelect = new Label(group, SWT.NONE);
        lbSelect.setText("Select");
        lbSelect.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false, false, 1, 1));
       
        final Label lbHttpHeartBeatTimeout = new Label(group, SWT.NONE);
        lbHttpHeartBeatTimeout.setText("Heartbeat Timeout");
        lbHttpHeartBeatTimeout.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false, false, 1, 1));

        Label lbHttpHeartBeatPeriod = new Label(group, SWT.NONE);
        lbHttpHeartBeatPeriod.setText("Heartbeat Period");
        lbHttpHeartBeatPeriod.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false, false, 1, 1));
        
        new Label(group, SWT.NONE);
        new Label(group, SWT.NONE);
    
        
        cSelect = JOE_Cbo_JCNodesForm_OnError.Control(new Combo(group, SWT.READ_ONLY));
        cSelect.setItems(new String[] { "first", "next" });
        cSelect.addModifyListener(new ModifyListener() {
            @Override public void modifyText(final ModifyEvent e) {
            	btApply.setEnabled(true);
            }
        });
         
        final GridData gridData_12 = new GridData(SWT.FILL, GridData.CENTER, true, false);
        gridData_12.widthHint = 50;
        gridData_12.minimumWidth = 20;
        cSelect.setLayoutData(gridData_12);        
        
        
        
        tHttpHeartBeatTimeout = JOE_T_ProcessClassesForm_httpHeartBeatTimeout.integerField(new IntegerField(group, SWT.BORDER));
        tHttpHeartBeatTimeout.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false,1,1));
        tHttpHeartBeatTimeout.setEnabled(true);

        tHttpHeartBeatTimeout.addTraverseListener(new TraverseListener() {
            public void keyTraversed(final TraverseEvent e) {
                traversed(e);
            }
        });
        tHttpHeartBeatTimeout.addModifyListener(new ModifyListener() {
            public void modifyText(final ModifyEvent e) {
                btApply.setEnabled(true);
            }
        });  
        
        tHttpHeartBeatPeriod = JOE_T_ProcessClassesForm_httpHeartBeatTimeout.integerField(new IntegerField(group, SWT.BORDER));
        tHttpHeartBeatPeriod.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false,1,1));
        tHttpHeartBeatPeriod.setEnabled(true);

        tHttpHeartBeatPeriod.addTraverseListener(new TraverseListener() {
            public void keyTraversed(final TraverseEvent e) {
                traversed(e);
            }
        });

        tHttpHeartBeatPeriod.addModifyListener(new ModifyListener() {
            public void modifyText(final ModifyEvent e) {
                btApply.setEnabled(true);
            }
        }); 
        
        new Label(group, SWT.NONE);
          
        
        //newline
        new Label(group, SWT.NONE);
        new Label(group, SWT.NONE);
        new Label(group, SWT.NONE);
        new Label(group, SWT.NONE);
        new Label(group, SWT.NONE);
        new Label(group, SWT.NONE);

        
        createTableProcessClasses();
        
        
        btNew = JOE_B_ProcessClassesForm_NewProcessClass.Control(new Button(group, SWT.NONE));
        btNew.setLayoutData(new GridData(SWT.FILL, SWT.BOTTOM, false, false,1,1));
        getShell().setDefaultButton(btNew);
        btNew.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
            public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
                listener.newProcessClass();
                setInput(true);
                btApply.setEnabled(listener.isValidClass(tProcessClass.getText()));
            }
        });
        Label lbSeperator2 = new Label(group, SWT.SEPARATOR | SWT.HORIZONTAL);
        lbSeperator2.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, false, false));
        btRemove = JOE_B_ProcessClassesForm_RemoveProcessClass.Control(new Button(group, SWT.NONE));
        btRemove.setEnabled(false);
        btRemove.setLayoutData(new GridData(SWT.FILL, SWT.BOTTOM, false, false,1,1));
        btRemove.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
            public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
                if (tableProcessClasses.getSelectionCount() > 0) {
                    if (Utils.checkElement(tableProcessClasses.getSelection()[0].getText(0), dom, JOEConstants.PROCESS_CLASSES, null)) {
                        int index = tableProcessClasses.getSelectionIndex();
                        listener.removeProcessClass(index);
                        tableProcessClasses.remove(index);
                        if (index >= tableProcessClasses.getItemCount())
                            index--;
                        if (tableProcessClasses.getItemCount() > 0) {
                            tableProcessClasses.select(index);
                            listener.selectProcessClass(index);
                            setInput(true);
                        }
                        else
                            setInput(false);
                    }
                }
                btRemove.setEnabled(tableProcessClasses.getSelectionCount() > 0);
                tProcessClass.setBackground(null);
                // bNew.setEnabled(true);
            }
        });

        
        
	}
	
	private void applyRemoteSchedulerEntry(){
       	boolean checkHearbeat =  (tHttpHeartBeatPeriod.getText().length() > 0 || tHttpHeartBeatTimeout.getText().length() > 0);
		
    	
    		if (checkHearbeat && tHttpHeartBeatPeriod.getIntegerValue(0) >= tHttpHeartBeatTimeout.getIntegerValue(-1)){
    			MainWindow.message("HTTP Hearbeat Timeout must be greater than HTTP Heartbeat Period ", SWT.ICON_WARNING);
    		}else{
    			if (checkHearbeat && tHttpHeartBeatPeriod.getIntegerValue(0) < 0){
    				MainWindow.message("HTTP Hearbeat Period must be greater than 0 ", SWT.ICON_WARNING);

    			}else{
        	
	            	if (tRemoteSchedulerUrl.getText().length() > 0) {
	                    if (tableRemoteScheduler.getSelectionIndex() >= 0) {
	                        TableItem item = tableRemoteScheduler.getItems()[tableRemoteScheduler.getSelectionIndex()];
	                        item.setText(0, tRemoteSchedulerUrl.getText());
	                        item.setText(1, tHttpHeartBeatTimeout.getText());
	                        item.setText(2, tHttpHeartBeatPeriod.getText());
	                        item.setText(3, cSelect.getText());
	                        
	                        cSelect.select(0);
	                        tRemoteSchedulerUrl.setText("");
	                        tHttpHeartBeatTimeout.setText("");
	                        tHttpHeartBeatPeriod.setText("");
	                        tRemoteSchedulerUrl.setFocus();
	                        btApply.setEnabled(true);
	                    }else {
	                        for (int i = 0; i < tableRemoteScheduler.getItemCount(); i++) {
	                            TableItem item = tableRemoteScheduler.getItems()[i];
	                            if ((item.getText(0).equals(tRemoteSchedulerUrl.getText())  )) {
	                                item.setText(0, tRemoteSchedulerUrl.getText());
	                                item.setText(1, tHttpHeartBeatTimeout.getText());
	                                item.setText(2, tHttpHeartBeatPeriod.getText());
	                                item.setText(3, cSelect.getText());
	    	                        cSelect.select(0);
	    	                        tRemoteSchedulerUrl.setText("");
	                                tHttpHeartBeatTimeout.setText("");
	                                tHttpHeartBeatPeriod.setText("");
	                                tRemoteSchedulerUrl.setFocus();
	
	                                btApply.setEnabled(true);
	                                }
	                        }
	                        if (tRemoteSchedulerUrl.getText().length() > 0) {
	    
	                            TableItem item = new TableItem(tableRemoteScheduler, SWT.NONE);
	                            item.setText(0, tRemoteSchedulerUrl.getText());
	                            item.setText(1, tHttpHeartBeatTimeout.getText());
	                            item.setText(2, tHttpHeartBeatPeriod.getText());
	                            item.setText(3, cSelect.getText());
    	                        cSelect.select(0);
	                            tRemoteSchedulerUrl.setText("");
	                            tHttpHeartBeatTimeout.setText("");
	                            tHttpHeartBeatPeriod.setText("");
	                            tRemoteSchedulerUrl.setFocus();
	
	                            btApply.setEnabled(true);
	                        }
	                    }
	            	}
    			}
            }
	}
 
 
    private void createTableRemoteScheduler()   {    
        
        tableRemoteScheduler = new Table(group, SWT.FULL_SELECTION | SWT.BORDER);
        tableRemoteScheduler.setHeaderVisible(true);
        tableRemoteScheduler.setLinesVisible(true);
        GridData gd_table = new GridData(SWT.FILL, SWT.TOP, true, false,4, 4);
        gd_table.heightHint = 112;
        tableRemoteScheduler.setLayoutData(gd_table);       
        tableRemoteScheduler.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
            public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
                if (tableRemoteScheduler.getSelectionIndex() >= 0) {
                    TableItem item = tableRemoteScheduler.getItems()[tableRemoteScheduler.getSelectionIndex()];
                    tRemoteSchedulerUrl.setText(item.getText(0));
                    tHttpHeartBeatTimeout.setText(item.getText(1));
                    tHttpHeartBeatPeriod.setText(item.getText(2));
                    cSelect.setText(item.getText(3));

                    btRemoveRemoteScheduler.setEnabled(true);
                    btNewRemoteScheduler.setEnabled(true);
                }

            }
        });
        TableColumn tableColumnHost = new TableColumn(tableRemoteScheduler, SWT.NONE);
        tableColumnHost.setWidth(300);
        tableColumnHost.setText("Url"); 
              
        TableColumn tableColumnHeatbeatTimeout = new TableColumn(tableRemoteScheduler, SWT.NONE);
        tableColumnHeatbeatTimeout.setWidth(150);
        tableColumnHeatbeatTimeout.setText("Heartbeat Timeout"); 

        TableColumn tableColumnHeatbeatPeriod = new TableColumn(tableRemoteScheduler, SWT.NONE);
        tableColumnHeatbeatPeriod.setWidth(150);
        tableColumnHeatbeatPeriod.setText("Heartbeat Period");
        
        TableColumn tableColumnSelect = new TableColumn(tableRemoteScheduler, SWT.NONE);
        tableColumnSelect.setWidth(50);
        tableColumnSelect.setText("Select");           
    }
    
    private boolean checkChange() {
            int ok = ErrorLog.message(Messages.getString("MainListener.apply_changes"), SWT.ICON_QUESTION | SWT.YES | SWT.NO | SWT.CANCEL);
            return ok == SWT.YES;
    }
   
	/**
	 * This method initializes table
	 */
	private void createTableProcessClasses() {
		tableProcessClasses = JOE_Tbl_ProcessClassesForm_ProcessClasses.Control(new Table(group, SWT.FULL_SELECTION | SWT.BORDER));
		tableProcessClasses.setHeaderVisible(true);
		tableProcessClasses.setLayoutData(new GridData(GridData.FILL, GridData.FILL, true, true, 4, 4));
		tableProcessClasses.setLinesVisible(true);
		tableProcessClasses.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
			public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
			    if (btApply.isEnabled() && checkChange()){
			        applyClass();
		         }
				Element currElem = listener.selectProcessClass(tableProcessClasses.getSelectionIndex());
				if (currElem != null && !Utils.isElementEnabled("process_class", dom, currElem)) {
					setInput(false);
					btRemove.setEnabled(false);
					btApply.setEnabled(false);
				}
				else {
					boolean selection = tableProcessClasses.getSelectionCount() > 0;
					btRemove.setEnabled(selection);
					if (selection) {
	                    btNewRemoteScheduler.setEnabled(true);
						listener.selectProcessClass(tableProcessClasses.getSelectionIndex());
						setInput(true);
						tProcessClass.setBackground(null);
					}
				}
			}
		});
		TableColumn tableColumn = JOE_TCl_ProcessClassesForm_ProcessClass.Control(new TableColumn(tableProcessClasses, SWT.NONE));
		tableColumn.setWidth(104);
		TableColumn tableColumn1 = JOE_TCl_ProcessClassesForm_MaxProcesses.Control(new TableColumn(tableProcessClasses, SWT.NONE));
		tableColumn1.setWidth(91);
		TableColumn tableColumn2 = JOE_TCl_ProcessClassesForm_RemoteExecution.Control(new TableColumn(tableProcessClasses, SWT.NONE));
		tableColumn2.setWidth(355);
	}

	private void applyClass() {
		 applyRemoteSchedulerEntry();
		applyRemoteSchedulerEntry();
	     listener.applyRemoteSchedulerTable(tableRemoteScheduler);
	     listener.applyProcessClass(tProcessClass.getText(), tRemoteUrl.getText(), tMaxProcesses.getIntegerValue(1));
	     listener.fillProcessClassesTable(tableProcessClasses);
			
		 getShell().setDefaultButton(btNew);
		 tProcessClass.setBackground(null);
		 btApply.setEnabled(false);
		 if (dom.isLifeElement()) {
		 	setInput(true);
		 }
	}

	private void setInput(boolean enabled) {
		tProcessClass.setEnabled(enabled);
		tMaxProcesses.setEnabled(enabled);
		tRemoteUrl.setEnabled(enabled);

		if (enabled) {
			tProcessClass.setText(listener.getProcessClass());
			tRemoteUrl.setText(listener.getRemoteUrl());
			tMaxProcesses.setText(listener.getMaxProcesses());
			tProcessClass.setFocus();
		    listener.fillRemoteSchedulerTable(tableRemoteScheduler);

		}
		else {
			tProcessClass.setText("");
			tRemoteUrl.setText("");
			tMaxProcesses.setText("");
		}
		btApply.setEnabled(false);
		btRemove.setEnabled(tableProcessClasses.getSelectionCount() > 0);
	}


	 

	public static  Table getTable() {
		return tableProcessClasses;
	}

	private void traversed(final TraverseEvent e) {
		if (e.keyCode == SWT.CR) {
			e.doit = false;
			applyClass();

		}
	}
} // @jve:decl-index=0:visual-constraint="10,10"