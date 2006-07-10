package sos.scheduler.editor.forms;

 


import javax.xml.transform.TransformerException;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;

import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;

import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;

import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.jdom.Element;
import org.jdom.JDOMException;

import sos.scheduler.editor.app.DomParser;
import sos.scheduler.editor.app.IUnsaved;
import sos.scheduler.editor.app.IUpdate;
import sos.scheduler.editor.app.IUpdateLanguage;
import sos.scheduler.editor.app.Messages;
import sos.scheduler.editor.app.Utils;

 
import sos.scheduler.editor.listeners.JobCommandListener;

public class JobCommandForm extends Composite implements IUnsaved, IUpdateLanguage {
	private Table tCommands;
	private JobCommandListener listener;

	private Group group = null;

	private Group gMain = null;



	private SashForm sashForm = null;

	private Group gJobParameter = null;

	private Table tParameter = null;

	private Button bRemove = null;

	private Label label2 = null;

	private Text tParaName = null;

	private Label label6 = null;

	private Text tParaValue = null;

	private Button bApply = null;

	private Group gDescription = null;


	
	private Label label10 = null;
	private Text tTitle = null;
	private Text tState = null;
	private Text tStartAt = null;
	private Text tPriority = null;
	private Combo cJobchain = null;
	private Button bOrder = null;
	private Button bJob = null;
	private Button bReplace = null;
	private Text tJob = null;
	private boolean updateTree=false;
	private boolean event=false;
	private Combo cSource = null;
	private Combo cExitcode = null;
	private Button bRemoveExitcode = null;
	private Button bApplyExitcode = null;
	private Button bNew = null;
	
 

 

	public JobCommandForm(Composite parent, int style, DomParser dom, Element command,
			IUpdate main) throws JDOMException, TransformerException {
		super(parent, style);
		
		listener = new JobCommandListener(dom, command, main);
		initialize();
		setToolTipText();		
		sashForm.setWeights(new int[] {28, 28,44});

		dom.setInit(true);
 
	
		listener.fillCommands(tCommands);
		cJobchain.setItems(listener.getJobChains());
		updateTree = false;
		cExitcode.setText(listener.getExitCode());
		updateTree = true;
		
		dom.setInit(false);
		bApplyExitcode.setEnabled(false);
		event=true;
	}

	public void apply() {
		if(isUnsaved())
			addParam();
			addCommand();
	}

	public boolean isUnsaved() {
		return bApplyExitcode.isEnabled() || bApply.isEnabled();
	}

	private void initialize() {
		this.setLayout(new FillLayout());
		createGroup();
		setSize(new org.eclipse.swt.graphics.Point(723,566));
	}

	/**
	 * This method initializes group
	 * 
	 */
	private void createGroup() {
		GridLayout gridLayout2 = new GridLayout();
		gridLayout2.numColumns = 1;
		group = new Group(this, SWT.NONE);
		group.setText("Commands for Job: " + listener.getName() + (listener.isDisabled() ? " (Disabled)" : ""));
		group.setLayout(gridLayout2);
		createSashForm();
	}

	/**
	 * This method initializes group1
	 * 
	 */
	private void createGroup1() {
		
		
		createCombo();
		createComposite();
	}

	/**
	 * This method initializes combo
	 * 
	 */
	private void createCombo() {
	}

	/**
	 * This method initializes composite
	 * 
	 */
	private void createComposite() {
	}

	/**
	 * This method initializes sashForm
	 * 
	 */
	private void createSashForm() {
		GridData gridData18 = new org.eclipse.swt.layout.GridData(GridData.FILL, GridData.FILL, true, true);
		sashForm = new SashForm(group, SWT.NONE);
		sashForm.setOrientation(org.eclipse.swt.SWT.VERTICAL);
		sashForm.setLayoutData(gridData18);
		

		createGroup1();
		createGroup2();
		createGroup3();
	}

	/**
	 * This method initializes group2
	 * 
	 */
	private void createGroup2() {
		GridLayout gridLayout3 = new GridLayout();
		gridLayout3.numColumns = 5;
		GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 2;
		gridLayout.marginHeight = 0;
		gridLayout.verticalSpacing = 0;
		gMain = new Group(sashForm, SWT.NONE);
		gMain.setText("Commands");
		gMain.setLayout(gridLayout);

		final Label exitLabel = new Label(gMain, SWT.NONE);
		exitLabel.setText("Exit  codes");

		cExitcode = new Combo(gMain, SWT.NONE);
		cExitcode.setItems(new String[] {"success", "error"});
		cExitcode.addModifyListener(new ModifyListener() {
			public void modifyText(final ModifyEvent e) {
				listener.setExitCode(cExitcode.getText(),updateTree);
				group.setText("Job: " + listener.getName() + " " + listener.getExitCode() + " " + (listener.isDisabled() ? " (Disabled)" : ""));
        if (event) {
					listener.setExitCode(cExitcode.getText(), true);
					bNew.setEnabled(true);
				}
        //bApplyExitcode.setEnabled(!cExitcode.getText().equals(""));


			}
		});
		cExitcode.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, false, false));
		new Label(gMain, SWT.NONE);

		tCommands = new Table(gMain, SWT.FULL_SELECTION | SWT.BORDER);
		tCommands.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(final SelectionEvent e) {
				TableItem item = (TableItem) e.item;
				if (item == null)
					return;
				
				if (item.getText(0).equals("add_order")) {
					bOrder.setSelection(true);
					bJob.setSelection(false);
					setCommandsEnabled(true);
				}
				if (item.getText(0).equals("start_job")) {
					bOrder.setSelection(false);
					bJob.setSelection(true);
					setCommandsEnabled(false);
				}
				
				if (tCommands.getSelectionCount() > 0) {
					
					fillCommand();
					listener.fillParams(tCommands,tParameter);
					bApplyExitcode.setEnabled(false);
 
				}
 
				bRemoveExitcode.setEnabled(tCommands.getSelectionCount() > 0);
			}
		});
		tCommands.setLinesVisible(true);
		tCommands.setHeaderVisible(true);
		final GridData gridData9 = new GridData(GridData.BEGINNING, GridData.FILL, false, true);
		gridData9.heightHint = 71;
		gridData9.widthHint = 310;
		tCommands.setLayoutData(gridData9);

		final TableColumn tcJob = new TableColumn(tCommands, SWT.NONE);
		tcJob.setWidth(80);
		tcJob.setText("command");

	 
		final TableColumn tcCommand = new TableColumn(tCommands, SWT.NONE);
		tcCommand.setWidth(80);
		tcCommand.setText("job/id");

		final TableColumn tcJobchain = new TableColumn(tCommands, SWT.NONE);
		tcJobchain.setWidth(70);
		tcJobchain.setText("job chain");

		final TableColumn tcStartAt = new TableColumn(tCommands, SWT.NONE);
		tcStartAt.setWidth(70);
		tcStartAt.setText("start at");
		gDescription = new Group(sashForm, SWT.NONE);
		gDescription.setText("Jobs and orders");
		gDescription.setLayout(gridLayout3);
		bJob = new Button(gDescription, SWT.RADIO);
		bJob.setLayoutData(new GridData());
		bJob.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(final SelectionEvent e) {
				setCommandsEnabled(false);
				if (bJob.getSelection()){
  				 tState.setText("");
		  		 tPriority.setText("");
			  	 cJobchain.setText("");
				   tTitle.setText("");
					 bReplace.setSelection(false);
				   
		       listener.setCommandAttribute(bApplyExitcode, "replace","",tCommands);
		       listener.setCommandAttribute(bApplyExitcode, "title","",tCommands);
		       listener.setCommandAttribute(bApplyExitcode, "at","",tCommands);
		       listener.setCommandAttribute(bApplyExitcode, "job_chain","",tCommands);
		       listener.setCommandAttribute(bApplyExitcode, "priority","",tCommands);
		       listener.setCommandAttribute(bApplyExitcode, "state","",tCommands);
		       listener.setCommandAttribute(bApplyExitcode, "id","",tCommands);
				   bApplyExitcode.setEnabled(true);
				}
			}
		});
		bJob.setSelection(true);
		bJob.setText("Job");
		bOrder = new Button(gDescription, SWT.RADIO);
		final GridData gridData_7 = new GridData(GridData.FILL, GridData.CENTER, false, false);
		gridData_7.widthHint = 217;
		bOrder.setLayoutData(gridData_7);
		bOrder.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(final SelectionEvent e) {
				setCommandsEnabled(true);
				if (bOrder.getSelection()) {
					  
  					bReplace.setSelection(true);
		        listener.setCommandAttribute(bApplyExitcode, "job","",tCommands);
				    bApplyExitcode.setEnabled(true);
				}
			}
		});
		bOrder.setText("Order");

		bApplyExitcode = new Button(gDescription, SWT.NONE);
		bApplyExitcode.setEnabled(false);
		final GridData gridData = new GridData(GridData.FILL, GridData.CENTER, false, false);
		gridData.widthHint = 73;
		bApplyExitcode.setLayoutData(gridData);
		bApplyExitcode.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(final SelectionEvent e) {
				addCommand();
				bApplyExitcode.setEnabled(false);
							}
		});
		bApplyExitcode.setText("Apply");

		bNew = new Button(gDescription, SWT.NONE);
		final GridData gridData_6 = new GridData(GridData.END, GridData.CENTER, false, false);
		gridData_6.widthHint = 62;
		bNew.setLayoutData(gridData_6);
		bNew.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(final SelectionEvent e) {
				 setCommandsEnabled(bOrder.getSelection());
			   tCommands.setSelection(-1);
				 tParameter.removeAll();
				 listener.clearParams();
				 clearFields();
				 
				 bRemove.setEnabled(false);
				 tJob.setFocus();
			}
		});
		bApplyExitcode.setEnabled(false);
		bNew.setText("New");


		bRemoveExitcode = new Button(gDescription, SWT.NONE);
		bRemoveExitcode.setLayoutData(new GridData(61, SWT.DEFAULT));
		bRemoveExitcode.setEnabled(false);
		bRemoveExitcode.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(final SelectionEvent e) {
				listener.deleteCommand(tCommands);
				clearFields();
				tCommands.deselectAll();
				bRemoveExitcode.setEnabled(false);
				bApplyExitcode.setEnabled(false);
				

			}
		});
		bRemoveExitcode.setText("Remove");
		label10 = new Label(gDescription, SWT.NONE);
		label10.setText("Job/Order ID");

		tJob = new Text(gDescription, SWT.BORDER);
		tJob
		.addModifyListener(new org.eclipse.swt.events.ModifyListener() {
			public void modifyText(org.eclipse.swt.events.ModifyEvent e) {
				bApplyExitcode.setEnabled(true);
			}
		});
		final GridData gridData_3 = new GridData(150, SWT.DEFAULT);
		tJob.setLayoutData(gridData_3);
		new Label(gDescription, SWT.NONE);
		new Label(gDescription, SWT.NONE);
		new Label(gDescription, SWT.NONE);
		final Label startAtLabel = new Label(gDescription, SWT.NONE);
		startAtLabel.setLayoutData(new GridData(GridData.END, GridData.CENTER, false, false));
		startAtLabel.setText("Start at");

		tStartAt = new Text(gDescription, SWT.BORDER);
		tStartAt 
		.addModifyListener(new org.eclipse.swt.events.ModifyListener() {
			public void modifyText(org.eclipse.swt.events.ModifyEvent e) {
				bApplyExitcode.setEnabled(true);
			}
		});
		tStartAt.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(final SelectionEvent e) {
			}
		});
		final GridData gridData_4 = new GridData(150, SWT.DEFAULT);
		tStartAt.setLayoutData(gridData_4);

		final Label jobchainLabel = new Label(gDescription, SWT.NONE);
		jobchainLabel.setLayoutData(new GridData(GridData.END, GridData.CENTER, false, false));
		jobchainLabel.setText("Job chain");

		cJobchain = new Combo(gDescription, SWT.NONE);
		cJobchain.setEnabled(false);
		cJobchain
		.addModifyListener(new org.eclipse.swt.events.ModifyListener() {
			public void modifyText(org.eclipse.swt.events.ModifyEvent e) {
				bApplyExitcode.setEnabled(true);
			}
		});
		 
		cJobchain.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, false, false));
		new Label(gDescription, SWT.NONE);

		final Label titleLabel = new Label(gDescription, SWT.NONE);
		titleLabel.setLayoutData(new GridData(GridData.END, GridData.BEGINNING, false, false));
		titleLabel.setText("Title");

		tTitle = new Text(gDescription, SWT.BORDER);
		tTitle.setEnabled(false);
		tTitle
		.addModifyListener(new org.eclipse.swt.events.ModifyListener() {
			public void modifyText(org.eclipse.swt.events.ModifyEvent e) {
				bApplyExitcode.setEnabled(true);
			}
		});
		
	 
		final GridData gridData_5 = new GridData(150, SWT.DEFAULT);
		tTitle.setLayoutData(gridData_5);

		final Label priorityLabel = new Label(gDescription, SWT.NONE);
		priorityLabel.setLayoutData(new GridData(GridData.END, GridData.CENTER, false, false));
		priorityLabel.setText("Priority");

		tPriority = new Text(gDescription, SWT.BORDER);
		tPriority.setEnabled(false);
		tPriority
		.addModifyListener(new org.eclipse.swt.events.ModifyListener() {
			public void modifyText(org.eclipse.swt.events.ModifyEvent e) {
				bApplyExitcode.setEnabled(true);
			}
		});
		tPriority.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, false, false));
		new Label(gDescription, SWT.NONE);

		final Label stateLabel = new Label(gDescription, SWT.NONE);
		stateLabel.setLayoutData(new GridData(GridData.END, GridData.BEGINNING, false, false));
		stateLabel.setText("State");

		tState = new Text(gDescription, SWT.BORDER);
		tState.setEnabled(false);
		tState
		.addModifyListener(new org.eclipse.swt.events.ModifyListener() {
			public void modifyText(org.eclipse.swt.events.ModifyEvent e) {
				bApplyExitcode.setEnabled(true);
			}
		});
		tState.setLayoutData(new GridData(150, SWT.DEFAULT));

		final Label replaceLabel = new Label(gDescription, SWT.NONE);
		replaceLabel.setLayoutData(new GridData(GridData.END, GridData.CENTER, false, false));
		replaceLabel.setText("Replace");

		bReplace = new Button(gDescription, SWT.CHECK);
		bReplace.setEnabled(false);
		bReplace.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(final SelectionEvent e) {
				bApplyExitcode.setEnabled(true);
			}
		});
		bReplace.setLayoutData(new GridData());
		new Label(gDescription, SWT.NONE);

	}

	/**
	 * This method initializes table
	 * 
	 */
	private void createTable() {
		tParameter = new Table(gJobParameter, SWT.BORDER | SWT.FULL_SELECTION);
		tParameter.addPaintListener(new PaintListener() {
			public void paintControl(final PaintEvent e) {
			}
		});
		tParameter.setBounds(45, 56, 250, 156);
		tParameter.setHeaderVisible(true);
		tParameter.setLinesVisible(true);
		tParameter
				.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
					public void widgetSelected(
							org.eclipse.swt.events.SelectionEvent e) {
						TableItem item = (TableItem) e.item;
						if (item == null)
							return;
						tParaName.setText(item.getText(0));
						if (tParaName.getText().equals("<from>"))cSource.setText(item.getText(1));
						tParaValue.setText(item.getText(1));
						bRemove.setEnabled(tParameter.getSelectionCount() > 0);
						bApply.setEnabled(false);
					}
				});
		TableColumn tcName = new TableColumn(tParameter, SWT.NONE);
		tcName.setWidth(80);
		tcName.setText("Name");
		TableColumn tcValue = new TableColumn(tParameter, SWT.NONE);
		tcValue.setWidth(150);
		tcValue.setText("Value");
		bRemove = new Button(gJobParameter, SWT.NONE);
		bRemove.setBounds(340, 32, 51, 23);
		bRemove.setText("Remove");
		bRemove.setEnabled(false);
		bRemove
				.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
					public void widgetSelected(
							org.eclipse.swt.events.SelectionEvent e) {
						listener.deleteParameter(tParameter, tParameter
								.getSelectionIndex());
						tParaName.setText("");
						tParaValue.setText("");
						tParameter.deselectAll();
						bRemove.setEnabled(false);
						bApply.setEnabled(false);
					}
				});
	}

	/**
	 * This method initializes group3
	 * 
	 */
	private void createGroup3() {
		gJobParameter = new Group(sashForm, SWT.NONE);
		gJobParameter.setText("Parameter");
		label2 = new Label(gJobParameter, SWT.NONE);
		label2.setBounds(45, 20, 31, 13);
		label2.setText("Name");
		tParaName = new Text(gJobParameter, SWT.BORDER);
		tParaName.setBounds(43, 35, 76, 19);
		tParaName.addKeyListener(new org.eclipse.swt.events.KeyAdapter() {
			public void keyPressed(org.eclipse.swt.events.KeyEvent e) {
				if(e.keyCode == SWT.CR && !tParaName.equals("")  && tCommands.getSelectionIndex()>=0)
					addParam();
			}
		});
		tParaName.addModifyListener(new org.eclipse.swt.events.ModifyListener() {
			public void modifyText(org.eclipse.swt.events.ModifyEvent e) {
				bApply.setEnabled(!tParaName.getText().equals("") && tCommands.getSelectionIndex()>=0);
				if (tParaName.getText().equals("<from>")) {
					cSource.setVisible(true);
					tParaValue.setVisible(false);
				}else {
					cSource.setVisible(false);
					tParaValue.setVisible(true);
				}
			}
		});
		label6 = new Label(gJobParameter, SWT.NONE);
		label6.setBounds(129, 20, 30, 13);
		label6.setText("Value");
		tParaValue = new Text(gJobParameter, SWT.BORDER);
		tParaValue.setBounds(129, 35, 141, 19);
		tParaValue.addKeyListener(new org.eclipse.swt.events.KeyAdapter() {
			public void keyPressed(org.eclipse.swt.events.KeyEvent e) {
				if(e.keyCode == SWT.CR && !tParaName.equals("") && tCommands.getSelectionIndex()>=0)
					addParam();
			}
		});
		tParaValue.addModifyListener(new org.eclipse.swt.events.ModifyListener() {
			public void modifyText(org.eclipse.swt.events.ModifyEvent e) {
				bApply.setEnabled(!tParaName.getText().equals("")  && tCommands.getSelectionIndex()>=0);
			}
		});
		bApply = new Button(gJobParameter, SWT.NONE);
		bApply.setBounds(280, 32, 51, 23);
		bApply.setText("&Apply");
		bApply.setEnabled(false);
		bApply
				.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
					public void widgetSelected(
							org.eclipse.swt.events.SelectionEvent e) {
						addParam();
					}
				});

		final Button paramButton = new Button(gJobParameter, SWT.RADIO);
		paramButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(final SelectionEvent e) {
				tParaName.setText("");
				tParaValue.setText("");
			}
		});
		paramButton.setSelection(true);
		paramButton.setText("Parameter");
		paramButton.setBounds(323, 84, 120, 30);

		final Button fromTaskButton = new Button(gJobParameter, SWT.RADIO);
		fromTaskButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(final SelectionEvent e) {
				tParaName.setText("<from>");
				cSource.setText("task");
			}
		});
		fromTaskButton.setText("from task");
		fromTaskButton.setBounds(323, 113, 120, 19);

		final Button fromOrderButton = new Button(gJobParameter, SWT.RADIO);
		fromOrderButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(final SelectionEvent e) {
				tParaName.setText("<from>");
				cSource.setText("order");
			}
		});
		fromOrderButton.setText("from order");
		fromOrderButton.setBounds(323, 130, 120, 20);

		cSource = new Combo(gJobParameter, SWT.READ_ONLY);
		cSource.setItems(new String[] {"order", "task"});
		cSource.addModifyListener(new ModifyListener() {
			public void modifyText(final ModifyEvent e) {
				tParaValue.setText(cSource.getText());
			}
		});
		cSource.setVisible(false);
		cSource.setBounds(131, 34, 138, 21);

		createTable();
	}
	
	private void addParam() {
		listener.saveParameter(tParameter,tParaName.getText(),tParaValue.getText());
		
		tParaName.setText("");
		tParaValue.setText("");
		bRemove.setEnabled(false);
		bApply.setEnabled(false);
		tParameter.deselectAll();
		tParaName.setFocus();
	 	}
	
	private void addCommand() {
    String msg ="";
		if (cJobchain.getText().trim().equals("") && bOrder.getSelection()) {
			msg = "A jobchain must be given for an order";
			cJobchain.setFocus();
		}else {
			if (tJob.getText().trim().equals("") && bJob.getSelection()) {
				msg = "A jobname must be given for a job";
				tJob.setFocus();
			}
		}
		if (!msg.equals("")) {
			Utils.message(getShell(),msg,SWT.ICON_INFORMATION );
		}else {

  
   	Element e = null;
  	int index = tCommands.getSelectionIndex();
  	 
  	if (index == -1){
  		  if (bJob.getSelection()) {
		    	 e = new Element("start_job");
			     e.setAttribute("at", tStartAt.getText());
			     e.setAttribute("job", tJob.getText());
	  			TableItem item = new TableItem(tCommands, SWT.NONE);
	  			item.setText(new String[] { "start_job",tJob.getText(),"",tStartAt.getText()});
  		  }else {
  		     e = new Element("add_order");
  			   e.setAttribute("at", tStartAt.getText());
  			   e.setAttribute("id", tJob.getText());
  			   e.setAttribute("priority", tPriority.getText());
  			   if (bReplace.getSelection()) {
    		  	  e.setAttribute("replace", "yes");
  			   }else {
    		 	    e.setAttribute("replace", "no");
  			   }
  			   e.setAttribute("state", tState.getText());
  			   e.setAttribute("job_chain", cJobchain.getText());
  			   e.setAttribute("title", tTitle.getText());
  			   
           TableItem item = new TableItem(tCommands, SWT.NONE);
 	  			 item.setText(new String[] { "add_order",tJob.getText(),cJobchain.getText(),tStartAt.getText()});
  		   }
		 
  		  listener.addCommand(e);
  		  tCommands.setSelection(tCommands.getItemCount()-1);
  		  listener.fillParams(tCommands,tParameter);
  	}else {
  		
    		String cmd = tCommands.getItem(index).getText();
	  		if (cmd.equals("add_order") && bJob.getSelection() && tCommands.getSelectionIndex() >= 0) {
          listener.setCommandName(bApplyExitcode, "start_job", tJob.getText(), tCommands);
          tCommands.getItem(tCommands.getSelectionIndex()).setText(0, "start_job");
  		   }
			
	  		if (cmd.equals("start_job") && bOrder.getSelection() && tCommands.getSelectionIndex() >= 0) {
          listener.setCommandName(bNew, "add_order", tJob.getText(), tCommands);
          tCommands.getItem(tCommands.getSelectionIndex()).setText(0, "add_order");
          tCommands.getItem(tCommands.getSelectionIndex()).setText(2, "");
          tCommands.getItem(tCommands.getSelectionIndex()).setText(3, tStartAt.getText());
			   }
 
   			if (bJob.getSelection()) {
 	          listener.setCommandAttribute(bApplyExitcode, "job", tJob.getText(), tCommands);
		     }else {
 	          listener.setCommandAttribute(bApplyExitcode, "id", tJob.getText(), tCommands);
 				    if (bReplace.getSelection()) {
 				    	 listener.setCommandAttribute(bApplyExitcode, "replace","yes",tCommands);
 				    }else {
 				    	 listener.setCommandAttribute(bApplyExitcode, "replace","no",tCommands);
 				    }
	  	   }
			   listener.setCommandAttribute(bApplyExitcode, "state",    tState.getText(),   tCommands);
			   listener.setCommandAttribute(bApplyExitcode, "title",    tTitle.getText(),   tCommands);
			   listener.setCommandAttribute(bApplyExitcode, "priority", tPriority.getText(),tCommands);
			   listener.setCommandAttribute(bApplyExitcode, "at",       tStartAt.getText(), tCommands);
			   listener.setCommandAttribute(bApplyExitcode, "job_chain",cJobchain.getText(),tCommands);
         tCommands.getItem(index).setText(1, tJob.getText());
	       tCommands.getItem(index).setText(3, tStartAt.getText());
   	     if (bOrder.getSelection()) {
  	        tCommands.getItem(index).setText(2, cJobchain.getText());
  	     }else {
 	          tCommands.getItem(index).setText(2, "");
  	     }
  	}
    
  	bApplyExitcode.setEnabled(false);
		}
	}

	private void clearFields() {
		tJob.setText("");
		tStartAt.setText("");
		tState.setText("");
		tPriority.setText("");
		cJobchain.setText("");
		tTitle.setText("");
		bReplace.setSelection(bOrder.getSelection());
	}
	
	private void setCommandsEnabled(boolean b) {
		tState.setEnabled(b);
		tPriority.setEnabled(b);
		cJobchain.setEnabled(b);
		tTitle.setEnabled(b);
		bReplace.setEnabled(b);
	}
	
	 

	public Element getCommand() {
		return listener.getCommand();
	}

	
	public void fillCommand() {
		clearFields();
		if (listener.getCommand() != null) {
			cExitcode.setText(listener.getExitCode());
 			tStartAt.setText(listener.getCommandAttribute(tCommands,"at"));
			if (bOrder.getSelection()) {
   			tJob.setText(listener.getCommandAttribute(tCommands,"id"));
  			tTitle.setText(listener.getCommandAttribute(tCommands,"title"));
  			tState.setText(listener.getCommandAttribute(tCommands,"state"));
  			cJobchain.setText(listener.getCommandAttribute(tCommands,"job_chain"));
  			tPriority.setText(listener.getCommandAttribute(tCommands,"priority"));
  			bReplace.setSelection(listener.getCommandReplace(tCommands));   			
			}else {
   			tJob.setText(listener.getCommandAttribute(tCommands,"job"));
			}
			
		}
	}


	
	public void setToolTipText(){
		cExitcode.setToolTipText(Messages.getTooltip("jobcommand.exitcode"));
		tStartAt.setToolTipText(Messages.getTooltip("jobcommand.startat"));
		tTitle.setToolTipText(Messages.getTooltip("jobcommand.title"));
		tPriority.setToolTipText(Messages.getTooltip("jobcommand.priority"));
		tState.setToolTipText(Messages.getTooltip("jobcommand.state"));
		bJob.setToolTipText(Messages.getTooltip("jobcommand.startjob"));
		bOrder.setToolTipText(Messages.getTooltip("jobcommand.add_order"));
		bReplace.setToolTipText(Messages.getTooltip("jobcommand.replaceorder"));
		cJobchain.setToolTipText(Messages.getTooltip("jobcommand.jobchain"));
		tJob.setToolTipText(Messages.getTooltip("jobcommand.job_order_id"));
		tParaName.setToolTipText(Messages.getTooltip("job.param.name"));
		tParaValue.setToolTipText(Messages.getTooltip("job.param.value"));
		bRemove.setToolTipText(Messages.getTooltip("job.param.btn_remove"));
		bApply.setToolTipText(Messages.getTooltip("job.param.btn_add"));
		tParameter.setToolTipText(Messages.getTooltip("jobcommand.param.table"));

	 
  }	
} // @jve:decl-index=0:visual-constraint="10,10"