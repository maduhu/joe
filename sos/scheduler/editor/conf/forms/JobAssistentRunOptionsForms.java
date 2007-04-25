package sos.scheduler.editor.conf.forms;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.ShellAdapter;
import org.eclipse.swt.events.ShellEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.jdom.Element;
import com.swtdesigner.SWTResourceManager;
import sos.scheduler.editor.app.Editor;
import sos.scheduler.editor.app.MainWindow;
import sos.scheduler.editor.app.Messages;
import sos.scheduler.editor.app.ResourceManager;
import sos.scheduler.editor.app.Utils;
import sos.scheduler.editor.conf.ISchedulerUpdate;
import sos.scheduler.editor.conf.SchedulerDom;
import sos.scheduler.editor.conf.listeners.JobsListener;
import org.eclipse.swt.widgets.Combo;

public class JobAssistentRunOptionsForms {
	
	private Element           job             = null;
	
	private SchedulerDom      dom             = null;
	
	private ISchedulerUpdate  update          = null;
	
	private Button            butFinish       = null;
	
	private Button            butCancel       = null;
	
	private Button            butNext         = null;
	
	private Button            butShow         = null;				
	
	private Text              txtRunOptions   = null;
	
	private Shell             shellRunOptions = null; 
	
	/** Wer hat ihn aufgerufen, der Job assistent oder job_chain assistent*/
	private int               assistentType   = -1; 
	
	private Combo             jobname         = null;
	
	private Button            butBack         = null; 
	
	private Element           jobBackUp       = null;
	
	private JobForm           jobForm         = null;
	
	/** Hilsvariable f�r das Schliessen des Dialogs. 
	 * Das wird gebraucht wenn das Dialog �ber den "X"-Botten (oben rechts vom Dialog) geschlossen wird .*/
	private boolean           closeDialog     = false;         
	
	
	public JobAssistentRunOptionsForms(SchedulerDom dom_, ISchedulerUpdate update_, Element job_, int assistentType_) {
		dom = dom_;
		update = update_;
		assistentType = assistentType_;
		job = job_;		
	}
	
	
	public void showRunOptionsForm() {
		
		shellRunOptions = new Shell(SWT.CLOSE | SWT.TITLE | SWT.APPLICATION_MODAL | SWT.BORDER);
		shellRunOptions.addShellListener(new ShellAdapter() {
			public void shellClosed(final ShellEvent e) {
				if(!closeDialog)
					close();
				e.doit = shellRunOptions.isDisposed();
			}
		});
		
		shellRunOptions.setImage(ResourceManager.getImageFromResource("/sos/scheduler/editor/editor.png"));
		
		final GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 2;
		shellRunOptions.setLayout(gridLayout);
		shellRunOptions.setSize(542, 315);
		String step = "  ";
		if (Utils.getAttributeValue("order", job).equalsIgnoreCase("yes"))
			step = step + " [Step 7 of 9]";
		else 
			step = step + " [Step 7 of 8]";
		shellRunOptions.setText("Run Options" + step);
		
		final Group jobGroup = new Group(shellRunOptions, SWT.NONE);
		jobGroup.setText(" Job: " + Utils.getAttributeValue("name", job));
		final GridData gridData_3 = new GridData(GridData.FILL, GridData.FILL, false, false, 2, 1);
		gridData_3.widthHint = 514;
		gridData_3.heightHint = 219;
		jobGroup.setLayoutData(gridData_3);
		final GridLayout gridLayout_1 = new GridLayout();
		gridLayout_1.marginWidth = 10;
		gridLayout_1.marginTop = 10;
		gridLayout_1.marginRight = 10;
		gridLayout_1.marginLeft = 10;
		gridLayout_1.marginHeight = 10;
		gridLayout_1.marginBottom = 10;
		jobGroup.setLayout(gridLayout_1);
		
		{
			txtRunOptions = new Text(jobGroup, SWT.MULTI | SWT.WRAP);
			final GridData gridData = new GridData(GridData.BEGINNING, GridData.FILL, false, false);
			gridData.horizontalIndent = -3;
			gridData.heightHint = 143;
			gridData.widthHint = 466;
			txtRunOptions.setLayoutData(gridData);
			txtRunOptions.setEditable(false);
			txtRunOptions.setText(Messages.getString("assistent.run_options"));
		}
		
		final Composite composite = new Composite(jobGroup, SWT.NONE);
		final GridData gridData = new GridData(GridData.CENTER, GridData.CENTER, false, false);
		gridData.widthHint = 350;
		gridData.heightHint = 38;
		composite.setLayoutData(gridData);
		final GridLayout gridLayout_4 = new GridLayout();
		gridLayout_4.numColumns = 3;
		composite.setLayout(gridLayout_4);
		
		final Button butPeriod = new Button(composite, SWT.NONE);
		butPeriod.setFocus();
		butPeriod.addSelectionListener(new SelectionAdapter() {			
			public void widgetSelected(final SelectionEvent e) {
				JobAssistentPeriodForms periodF = new JobAssistentPeriodForms(dom, update, job, assistentType);
				periodF.showPeriodeForms();
			}
		});
		final GridData gridData_4 = new GridData(GridData.FILL, GridData.CENTER, false, false);
		gridData_4.horizontalIndent = 10;
		gridData_4.widthHint = 88;
		gridData_4.minimumWidth = 100;
		butPeriod.setLayoutData(gridData_4);
		butPeriod.setText("Periods");
		
		final Button butRunTime = new Button(composite, SWT.NONE);
		butRunTime.setEnabled(Utils.getAttributeValue("order", job).equals("no"));
		butRunTime.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(final SelectionEvent e) {
				JobAssistentRunTimeForms runtime = new JobAssistentRunTimeForms(dom, update, job, assistentType);
				runtime.showRunTimeForms();
				
			}
		});
		final GridData gridData_7 = new GridData(GridData.FILL, GridData.CENTER, false, false);
		gridData_7.horizontalIndent = 10;
		gridData_7.widthHint = 97;
		butRunTime.setLayoutData(gridData_7);
		butRunTime.setText("Single Starts");
		
		final Button butDirectoryMonitoring = new Button(composite, SWT.NONE);
		butDirectoryMonitoring.setEnabled(Utils.getAttributeValue("order", job).equals("no"));
		butDirectoryMonitoring.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(final SelectionEvent e) {
				JobAssistentMonitoringDirectoryForms monDir = new JobAssistentMonitoringDirectoryForms(dom, update, job, assistentType);
				monDir.showMonitoringDirectoryForm();
			}
		});
		final GridData gridData_2 = new GridData(GridData.FILL, GridData.CENTER, false, false);
		gridData_2.widthHint = 110;
		gridData_2.horizontalIndent = 10;
		gridData_2.minimumHeight = 100;
		butDirectoryMonitoring.setLayoutData(gridData_2);
		butDirectoryMonitoring.setText("Directory Monitoring");				
		
		java.awt.Dimension screen = java.awt.Toolkit.getDefaultToolkit().getScreenSize();		
		shellRunOptions.setBounds((screen.width - shellRunOptions.getBounds().width) /2, 
				(screen.height - shellRunOptions.getBounds().height) /2, 
				shellRunOptions.getBounds().width, 
				shellRunOptions.getBounds().height);
		
		shellRunOptions.open();
		
		final Composite composite_1 = new Composite(shellRunOptions, SWT.NONE);
		final GridLayout gridLayout_2 = new GridLayout();
		gridLayout_2.marginWidth = 0;
		composite_1.setLayout(gridLayout_2);
		{
			butCancel = new Button(composite_1, SWT.NONE);
			butCancel.addSelectionListener(new SelectionAdapter() {
				public void widgetSelected(final SelectionEvent e) {
					close();
				}
			});
			butCancel.setText("Cancel");
		}
		
		final Composite composite_2 = new Composite(shellRunOptions, SWT.NONE);
		composite_2.setLayoutData(new GridData(GridData.END, GridData.CENTER, false, false));
		final GridLayout gridLayout_3 = new GridLayout();
		gridLayout_3.marginWidth = 0;
		gridLayout_3.numColumns = 4;
		composite_2.setLayout(gridLayout_3);
		
		{
			butShow = new Button(composite_2, SWT.NONE);
			butShow.addSelectionListener(new SelectionAdapter() {
				public void widgetSelected(final SelectionEvent e) {										
					Utils.showClipboard(Utils.getElementAsString(job), shellRunOptions); 
				}
			});
			butShow.setText("Show");
		}
		
		{
			butFinish = new Button(composite_2, SWT.NONE);
			butFinish.addSelectionListener(new SelectionAdapter() {
				public void widgetSelected(final SelectionEvent e) {
					doFinish();
				}
			});
			butFinish.setText("Finish");
		}
		
		butBack = new Button(composite_2, SWT.NONE);
		butBack.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(final SelectionEvent e) {
				if(Utils.getAttributeValue("order", job).equals("yes")) {
					JobAssistentTimeoutOrderForms timeout = new JobAssistentTimeoutOrderForms(dom, update, job, assistentType);
					timeout.showTimeOutForm();
					if(jobname != null) 													
						timeout.setJobname(jobname);
					if(jobBackUp != null)
						timeout.setBackUpJob(jobBackUp, jobForm);
				} else {
					JobAssistentTimeoutForms timeout = new JobAssistentTimeoutForms(dom, update, job, assistentType);
					timeout.showTimeOutForm();
					if(jobBackUp != null)
						timeout.setBackUpJob(jobBackUp, jobForm);
				}
				closeDialog = true;
				shellRunOptions.dispose();
			}
		});
		butBack.setText("Back");
		butNext = new Button(composite_2, SWT.NONE);
		butNext.setFont(SWTResourceManager.getFont("", 8, SWT.BOLD));
		final GridData gridData_6 = new GridData(47, SWT.DEFAULT);
		butNext.setLayoutData(gridData_6);
		butNext.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(final SelectionEvent e) {								
				JobAssistentDelayAfterErrorForm derror = new JobAssistentDelayAfterErrorForm(dom, update, job, assistentType);
				derror.showDelayAfterErrorForm();
				if(jobname != null) 													
					derror.setJobname(jobname);
				if(jobBackUp != null)
					derror.setBackUpJob(jobBackUp, jobForm);
				closeDialog = true;
				shellRunOptions.dispose();								
			}
		});
		butNext.setText("Next");
		setToolTipText();
		shellRunOptions.layout();		
	}
	
	public void setToolTipText() {
		butCancel.setToolTipText(Messages.getTooltip("assistent.cancel"));
		butNext.setToolTipText(Messages.getTooltip("assistent.next"));
		butShow.setToolTipText(Messages.getTooltip("assistent.show"));
		butFinish.setToolTipText(Messages.getTooltip("assistent.finish"));
		butBack.setToolTipText(Messages.getTooltip("butBack"));
	}
	
	private void close() {
		int cont = MainWindow.message(shellRunOptions, sos.scheduler.editor.app.Messages.getString("assistent.cancel"), SWT.ICON_WARNING | SWT.OK |SWT.CANCEL );
		if(cont == SWT.OK){
			if(jobBackUp != null)
				job.setContent(jobBackUp.cloneContent());
			shellRunOptions.dispose();
		}
	}
	
	public void setJobname(Combo jobname) {
		this.jobname = jobname;
	}
	
	/**
	 * Der Wizzard wurde f�r ein bestehende Job gestartet. 
	 * Beim verlassen der Wizzard ohne Speichern, muss der bestehende Job ohne �nderungen wieder zur�ckgesetz werden.
	 * @param backUpJob
	 */
	public void setBackUpJob(Element backUpJob, JobForm jobForm_) {
		jobBackUp = (Element)backUpJob.clone();	
		jobForm = jobForm_;
	}
	
	private void doFinish() {
		
		
		if(jobname != null)
			jobname.setText(Utils.getAttributeValue("name",job));
		
		if(assistentType == Editor.JOB_WIZZARD) {															
			jobForm.initForm();	
		} else {
			
			JobsListener listener = new JobsListener(dom, update);
			listener.newImportJob(job, assistentType);
			
		}
		MainWindow.message(shellRunOptions,  Messages.getString("assistent.finish") + "\n\n" + Utils.getElementAsString(job), SWT.OK );
		
		closeDialog = true;
		shellRunOptions.dispose();
		
	}
}
