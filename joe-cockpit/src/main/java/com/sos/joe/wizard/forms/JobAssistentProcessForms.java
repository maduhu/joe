package com.sos.joe.wizard.forms;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.ShellAdapter;
import org.eclipse.swt.events.ShellEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.jdom.Element;

import sos.scheduler.editor.app.Utils;
import sos.scheduler.editor.conf.forms.ScriptJobMainForm;
import sos.scheduler.editor.conf.listeners.ExecuteListener;
import sos.scheduler.editor.conf.listeners.JobsListener;

import com.sos.dialog.swtdesigner.SWTResourceManager;
import com.sos.joe.globals.JOEConstants;
import com.sos.joe.globals.interfaces.ISchedulerUpdate;
import com.sos.joe.globals.messages.ErrorLog;
import com.sos.joe.globals.messages.SOSJOEMessageCodes;
import com.sos.joe.globals.misc.ResourceManager;
import com.sos.joe.globals.options.Options;
import com.sos.joe.xml.jobscheduler.SchedulerDom;

public class JobAssistentProcessForms {

    private SchedulerDom dom = null;
    private ISchedulerUpdate update = null;
    private ExecuteListener executeListener = null;
    private Button butFinish = null;
    private Button butCancel = null;
    private Button butNext = null;
    private Button butShow = null;
    private Button butBack = null;
    private Text txtFile = null;
    private Text txtParameter = null;
    private Text txtLog = null;
    private int assistentType = -1;
    private Shell processShell = null;
    private Combo jobname = null;
    private Element jobBackUp = null;
    private ScriptJobMainForm jobForm = null;
    private boolean closeDialog = false;

    public JobAssistentProcessForms(SchedulerDom dom_, ISchedulerUpdate update_, Element job_, int assistentType_) {
        dom = dom_;
        update = update_;
        assistentType = assistentType_;
        executeListener = new ExecuteListener(dom, job_);
    }

    public void showProcessForm() {
        processShell = new Shell(ErrorLog.getSShell(), SWT.CLOSE | SWT.TITLE | SWT.APPLICATION_MODAL | SWT.BORDER);
        processShell.addShellListener(new ShellAdapter() {

            @Override
            public void shellClosed(final ShellEvent e) {
                if (!closeDialog) {
                    close();
                }
                e.doit = processShell.isDisposed();
            }
        });
        processShell.setImage(ResourceManager.getImageFromResource("/sos/scheduler/editor/editor.png"));
        final GridLayout gridLayout = new GridLayout();
        gridLayout.numColumns = 2;
        processShell.setLayout(gridLayout);
        processShell.setSize(434, 207);
        String step = " ";
        if ("yes".equalsIgnoreCase(Utils.getAttributeValue("order", executeListener.getJob()))) {
            step += SOSJOEMessageCodes.JOE_M_JobAssistent_Step5of9.label();
        } else {
            step += SOSJOEMessageCodes.JOE_M_JobAssistent_Step5of8.label();
        }
        processShell.setText(SOSJOEMessageCodes.JOE_M_JobAssistentProcessForms_Execute.params(step));
        final Group jobGroup = new Group(processShell, SWT.NONE);
        jobGroup.setText(SOSJOEMessageCodes.JOE_M_JobAssistent_JobGroup.params(Utils.getAttributeValue("name", executeListener.getJob())));
        final GridData gridData = new GridData(GridData.FILL, GridData.CENTER, true, false, 2, 1);
        jobGroup.setLayoutData(gridData);
        final GridLayout gridLayout_1 = new GridLayout();
        gridLayout_1.marginWidth = 10;
        gridLayout_1.marginTop = 10;
        gridLayout_1.marginRight = 10;
        gridLayout_1.marginLeft = 10;
        gridLayout_1.marginHeight = 10;
        gridLayout_1.marginBottom = 10;
        gridLayout_1.numColumns = 2;
        jobGroup.setLayout(gridLayout_1);
        final Label lblFile = SOSJOEMessageCodes.JOE_JobAssistent_FileLabel.Control(new Label(jobGroup, SWT.NONE));
        txtFile = SOSJOEMessageCodes.JOE_JobAssistent_FileText.Control(new Text(jobGroup, SWT.BORDER));
        txtFile.setFocus();
        txtFile.addModifyListener(new ModifyListener() {

            @Override
            public void modifyText(final ModifyEvent e) {
                if (txtFile.getText() != null || !txtFile.getText().trim().isEmpty()) {
                    executeListener.setFile(txtFile.getText());
                }
            }
        });
        txtFile.setLayoutData(new GridData(GridData.FILL, GridData.FILL, true, false));
        txtFile.setFont(SWTResourceManager.getFont("", 8, SWT.BOLD));
        txtFile.setEnabled(false);
        txtFile.setText(executeListener.getFile());
        final Label lblParameter = SOSJOEMessageCodes.JOE_JobAssistent_ParameterLabel.Control(new Label(jobGroup, SWT.NONE));
        txtParameter = SOSJOEMessageCodes.JOE_JobAssistent_ParameterText.Control(new Text(jobGroup, SWT.BORDER));
        txtParameter.addModifyListener(new ModifyListener() {

            @Override
            public void modifyText(final ModifyEvent e) {
                if (txtParameter.getText() != null || !txtParameter.getText().trim().isEmpty()) {
                    executeListener.setParam(txtParameter.getText());
                }
            }
        });
        txtParameter.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, true, false));
        txtParameter.setFont(SWTResourceManager.getFont("", 8, SWT.BOLD));
        txtParameter.setEnabled(false);
        txtParameter.setText(executeListener.getParam());
        final Label lblRessources = SOSJOEMessageCodes.JOE_JobAssistent_LogFileLabel.Control(new Label(jobGroup, SWT.NONE));
        lblRessources.setLayoutData(new GridData(SWT.DEFAULT, 17));
        txtLog = SOSJOEMessageCodes.JOE_JobAssistent_LogFileText.Control(new Text(jobGroup, SWT.BORDER));
        txtLog.addModifyListener(new ModifyListener() {

            @Override
            public void modifyText(final ModifyEvent e) {
                if (txtLog.getText() != null || !txtLog.getText().trim().isEmpty()) {
                    executeListener.setLogFile(txtLog.getText());
                }
            }
        });
        final GridData gridData_1 = new GridData(GridData.FILL, GridData.FILL, true, false);
        gridData_1.heightHint = 0;
        txtLog.setLayoutData(gridData_1);
        txtLog.setFont(SWTResourceManager.getFont("", 8, SWT.BOLD));
        txtLog.setEnabled(false);
        txtLog.setText(executeListener.getLogFile());
        if (executeListener.getFile() == null || executeListener.getFile().trim().isEmpty()) {
            txtFile.setEnabled(true);
        }
        if (executeListener.getParam() == null || executeListener.getParam().trim().isEmpty()) {
            txtParameter.setEnabled(true);
        }
        if (executeListener.getLogFile() == null || executeListener.getLogFile().trim().isEmpty()) {
            txtLog.setEnabled(true);
        }
        java.awt.Dimension screen = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
        processShell.setBounds((screen.width - processShell.getBounds().width) / 2, (screen.height - processShell.getBounds().height) / 2, processShell.getBounds().width, processShell.getBounds().height);
        processShell.open();
        final Composite composite = new Composite(processShell, SWT.NONE);
        composite.setLayoutData(new GridData());
        final GridLayout gridLayout_2 = new GridLayout();
        gridLayout_2.marginWidth = 0;
        composite.setLayout(gridLayout_2);
        butCancel = SOSJOEMessageCodes.JOE_B_JobAssistent_Cancel.Control(new Button(composite, SWT.NONE));
        butCancel.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(final SelectionEvent e) {
                close();
            }
        });
        final Composite composite_1 = new Composite(processShell, SWT.NONE);
        final GridData gridData2 = new GridData(GridData.END, GridData.CENTER, true, false);
        gridData2.widthHint = 220;
        composite_1.setLayoutData(gridData2);
        final GridLayout gridLayout_3 = new GridLayout();
        gridLayout_3.marginWidth = 0;
        gridLayout_3.numColumns = 5;
        composite_1.setLayout(gridLayout_3);
        butShow = SOSJOEMessageCodes.JOE_B_JobAssistent_Show.Control(new Button(composite_1, SWT.NONE));
        butShow.setLayoutData(new GridData());
        butShow.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(final SelectionEvent e) {
                Utils.showClipboard(Utils.getElementAsString(executeListener.getJob()), processShell, false, null, false, null, false);
                txtFile.setFocus();
            }
        });
        butFinish = SOSJOEMessageCodes.JOE_B_JobAssistent_Finish.Control(new Button(composite_1, SWT.NONE));
        butFinish.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(final SelectionEvent e) {
                doFinish();
            }
        });
        butBack = SOSJOEMessageCodes.JOE_B_JobAssistent_Back.Control(new Button(composite_1, SWT.NONE));
        butBack.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(final SelectionEvent e) {
                doBack();
            }
        });
        final GridData gridData3 = new GridData(GridData.FILL, GridData.CENTER, false, false);
        gridData3.widthHint = 47;
        butBack.setLayoutData(gridData3);
        butNext = SOSJOEMessageCodes.JOE_B_JobAssistent_Next.Control(new Button(composite_1, SWT.NONE));
        butNext.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, false, false));
        butNext.setFont(SWTResourceManager.getFont("", 8, SWT.BOLD));
        butNext.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(final SelectionEvent e) {
                Utils.startCursor(processShell);
                if (Utils.getAttributeValue("order", executeListener.getJob()).equals("yes")) {
                    JobAssistentTimeoutOrderForms timeout = new JobAssistentTimeoutOrderForms(dom, update, executeListener.getJob(), assistentType);
                    timeout.showTimeOutForm();
                    if (jobname != null) {
                        timeout.setJobname(jobname);
                    }
                    timeout.setBackUpJob(jobBackUp, jobForm);
                } else {
                    JobAssistentTimeoutForms timeout = new JobAssistentTimeoutForms(dom, update, executeListener.getJob(), assistentType);
                    timeout.showTimeOutForm();
                    timeout.setBackUpJob(jobBackUp, jobForm);
                }
                closeDialog = true;
                Utils.stopCursor(processShell);
                processShell.dispose();
            }
        });
        Utils.createHelpButton(composite_1, "JOE_M_JobAssistentProcessForms_Help.label", processShell);
        processShell.layout();
    }

    public void setToolTipText() {
        //
    }

    private void close() {
        int cont = ErrorLog.message(processShell, SOSJOEMessageCodes.JOE_M_JobAssistent_CancelWizard.label(), SWT.ICON_WARNING | SWT.OK | SWT.CANCEL);
        if (cont == SWT.OK) {
            if (jobBackUp != null) {
                executeListener.getJob().setContent(jobBackUp.cloneContent());
            }
            processShell.dispose();
        }
    }

    public void setJobname(Combo jobname) {
        this.jobname = jobname;
    }

    private void doBack() {
        if (executeListener.getJob().getChild("description") == null) {
            JobAssistentExecuteForms execute = new JobAssistentExecuteForms(dom, update, executeListener.getJob(), assistentType);
            execute.showExecuteForm();
            if (jobname != null) {
                execute.setJobname(jobname);
            }
            execute.setBackUpJob(jobBackUp, jobForm);
        } else {
            JobAssistentTasksForm tasks = new JobAssistentTasksForm(dom, update, executeListener.getJob(), assistentType);
            tasks.showTasksForm();
            if (jobname != null) {
                tasks.setJobname(jobname);
            }
            tasks.setBackUpJob(jobBackUp, jobForm);
        }
        closeDialog = true;
        processShell.dispose();
    }

    public void setBackUpJob(Element backUpJob, ScriptJobMainForm jobForm_) {
        if (backUpJob != null) {
            jobBackUp = (Element) backUpJob.clone();
        }
        jobForm = jobForm_;
    }

    private void doFinish() {
        if (assistentType == JOEConstants.JOB_WIZARD) {
            jobForm.initForm();
        } else {
            JobsListener listener = new JobsListener(dom, update);
            listener.newImportJob(executeListener.getJob(), assistentType);
        }
        if (Options.getPropertyBoolean("editor.job.show.wizard")) {
            Utils.showClipboard(SOSJOEMessageCodes.JOE_M_JobAssistent_Finish.label() + "\n\n" + Utils.getElementAsString(executeListener.getJob()), processShell, false, null, false, null, true);
        }
        if (jobname != null) {
            jobname.setText(Utils.getAttributeValue("name", executeListener.getJob()));
        }
        closeDialog = true;
        processShell.dispose();
    }

}