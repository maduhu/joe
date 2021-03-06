package sos.scheduler.editor.conf.container;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import sos.scheduler.editor.classes.FormBaseClass;
import sos.scheduler.editor.conf.listeners.JobListener;

import com.sos.joe.globals.messages.SOSJOEMessageCodes;

public class JobEmailSettings extends FormBaseClass {

    private Combo mailOnDelayAfterError = null;
    private Text mailCC = null;
    private Text mailBCC = null;
    private Combo mailOnError = null;
    private Combo mailOnWarning = null;
    private Combo mailOnSuccess = null;
    private Combo mailOnProcess = null;
    private Text mailTo = null;
    private String[] comboItems = { "yes", "no", "" };
    private JobListener objJobDataProvider = null;

    public JobEmailSettings(Composite pParentComposite, JobListener pobjJobDataProvider) {
        super(pParentComposite, pobjJobDataProvider);
        objJobDataProvider = pobjJobDataProvider;
        createGroup();
        initForm();
    }

    public void apply() {
        //
    }

    public boolean isUnsaved() {
        return false;
    }

    public void refreshContent() {
        //
    }

    private void createGroup() {
        GridData gridData = new GridData();
        gridData.horizontalAlignment = GridData.FILL;
        gridData.grabExcessHorizontalSpace = true;
        gridData.grabExcessVerticalSpace = true;
        gridData.verticalAlignment = GridData.FILL;
        GridLayout gridLayout4EMailGroup = new GridLayout();
        gridLayout4EMailGroup.marginRight = 5;
        gridLayout4EMailGroup.marginLeft = 5;
        gridLayout4EMailGroup.marginTop = 5;
        gridLayout4EMailGroup.numColumns = 2;
        Group group4EMail = SOSJOEMessageCodes.JOE_G_JobEmailSettings_Notifications.control(new Group(objParent, SWT.NONE));
        group4EMail.setLayout(gridLayout4EMailGroup);
        group4EMail.setLayoutData(gridData);
        @SuppressWarnings("unused")
        Label labelMailOnError = SOSJOEMessageCodes.JOE_L_MailForm_MailOnError.control(new Label(group4EMail, SWT.NONE));
        mailOnError = SOSJOEMessageCodes.JOE_Cbo_MailForm_MailOnError.control(new Combo(group4EMail, SWT.READ_ONLY));
        mailOnError.setItems(comboItems);
        mailOnError.addSelectionListener(new SelectionAdapter() {

            public void widgetSelected(final SelectionEvent e) {
                mailOnDelayAfterError.setEnabled("yes".equals(mailOnError.getText()) || "yes".equals(mailOnWarning.getText()));
                objJobDataProvider.setValue("mail_on_error", mailOnError.getText());
            }
        });
        GridData gd_mailOnError = new GridData(GridData.BEGINNING, GridData.CENTER, true, false);
        gd_mailOnError.minimumWidth = 150;
        mailOnError.setLayoutData(gd_mailOnError);
        Label labelMailOnWarning = SOSJOEMessageCodes.JOE_L_MailForm_MailOnWarning.control(new Label(group4EMail, SWT.NONE));
        mailOnWarning = SOSJOEMessageCodes.JOE_Cbo_MailForm_MailOnWarning.control(new Combo(group4EMail, SWT.READ_ONLY));
        mailOnWarning.setItems(comboItems);
        mailOnWarning.addSelectionListener(new SelectionAdapter() {

            public void widgetSelected(final SelectionEvent e) {
                mailOnDelayAfterError.setEnabled("yes".equals(mailOnWarning.getText()) || "yes".equals(mailOnWarning.getText()));
                objJobDataProvider.setValue("mail_on_warning", mailOnWarning.getText());
            }
        });
        GridData gd_mailOnWarning = new GridData(GridData.BEGINNING, GridData.CENTER, true, false);
        gd_mailOnWarning.minimumWidth = 150;
        mailOnWarning.setLayoutData(gd_mailOnWarning);
        Label label3 = SOSJOEMessageCodes.JOE_L_MailForm_MailOnSuccess.control(new Label(group4EMail, SWT.NONE));
        mailOnSuccess = SOSJOEMessageCodes.JOE_Cbo_MailForm_MailOnSuccess.control(new Combo(group4EMail, SWT.READ_ONLY));
        mailOnSuccess.setItems(comboItems);
        mailOnSuccess.addSelectionListener(new SelectionAdapter() {

            public void widgetSelected(final SelectionEvent e) {
                objJobDataProvider.setValue("mail_on_success", mailOnSuccess.getText());
            }
        });
        GridData gd_mailOnSuccess = new GridData(GridData.BEGINNING, GridData.CENTER, true, false);
        gd_mailOnSuccess.minimumWidth = 150;
        mailOnSuccess.setLayoutData(gd_mailOnSuccess);
        final Label mailOnProcessLabel = SOSJOEMessageCodes.JOE_L_MailForm_MailOnProcess.control(new Label(group4EMail, SWT.NONE));
        mailOnProcess = SOSJOEMessageCodes.JOE_Cbo_MailForm_MailOnProcess.control(new Combo(group4EMail, SWT.READ_ONLY));
        mailOnProcess.setItems(comboItems);
        mailOnProcess.addSelectionListener(new SelectionAdapter() {

            public void widgetSelected(final SelectionEvent e) {
                objJobDataProvider.setValue("mail_on_process", mailOnProcess.getText());
            }
        });
        GridData gd_mailOnProcess = new GridData(GridData.BEGINNING, GridData.CENTER, true, false);
        gd_mailOnProcess.minimumWidth = 150;
        mailOnProcess.setLayoutData(gd_mailOnProcess);
        final Label mailOnDelayLabel = SOSJOEMessageCodes.JOE_L_MailForm_MailOnDelayAfterError.control(new Label(group4EMail, SWT.NONE));
        mailOnDelayAfterError = SOSJOEMessageCodes.JOE_Cbo_MailForm_MailOnDelayAfterError.control(new Combo(group4EMail, SWT.READ_ONLY));
        mailOnDelayAfterError.setItems(new String[] { "all", "first_only", "last_only", "first_and_last_only", "" });
        mailOnDelayAfterError.setEnabled("yes".equals(mailOnError.getText()) || "yes".equals(mailOnWarning.getText()));
        mailOnDelayAfterError.addSelectionListener(new SelectionAdapter() {

            public void widgetSelected(final SelectionEvent e) {
                objJobDataProvider.setValue("mail_on_delay_after_error", mailOnDelayAfterError.getText());
            }
        });
        mailOnDelayAfterError.setLayoutData(new GridData(GridData.BEGINNING, GridData.CENTER, true, false));
        final Label ddddLabel = new Label(group4EMail, SWT.HORIZONTAL | SWT.SEPARATOR);
        final GridData gridData_1 = new GridData(GridData.FILL, GridData.CENTER, false, false, 2, 1);
        gridData_1.heightHint = 8;
        ddddLabel.setLayoutData(gridData_1);
        final Label mailToLabel = SOSJOEMessageCodes.JOE_L_MailForm_MailTo.control(new Label(group4EMail, SWT.NONE));
        mailTo = SOSJOEMessageCodes.JOE_T_MailForm_MailTo.control(new Text(group4EMail, SWT.BORDER));
        mailTo.addModifyListener(new ModifyListener() {

            public void modifyText(final ModifyEvent e) {
                objJobDataProvider.setValue("log_mail_to", mailTo.getText());
            }
        });
        mailTo.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, true, false));
        final Label mailCcLabel = SOSJOEMessageCodes.JOE_L_MailForm_MailCC.control(new Label(group4EMail, SWT.NONE));
        mailCC = SOSJOEMessageCodes.JOE_T_MailForm_MailCC.control(new Text(group4EMail, SWT.BORDER));
        mailCC.addModifyListener(new ModifyListener() {

            public void modifyText(final ModifyEvent e) {
                objJobDataProvider.setValue("log_mail_cc", mailCC.getText());
            }
        });
        final GridData gridData_2 = new GridData(GridData.FILL, GridData.CENTER, true, false);
        gridData_2.minimumWidth = 60;
        mailCC.setLayoutData(gridData_2);
        final Label mailBccLabel = SOSJOEMessageCodes.JOE_L_MailForm_MailBCC.control(new Label(group4EMail, SWT.NONE));
        gridData = new GridData(GridData.FILL, GridData.CENTER, false, false);
        gridData.minimumWidth = 60;
        mailBCC = SOSJOEMessageCodes.JOE_T_MailForm_MailBCC.control(new Text(group4EMail, SWT.BORDER));
        mailBCC.setLayoutData(gridData);
        mailBCC.addModifyListener(new ModifyListener() {

            @Override
            public void modifyText(ModifyEvent e) {
                objJobDataProvider.setValue("log_mail_bcc", mailBCC.getText());
            }
        });
        final Label label_1 = new Label(group4EMail, SWT.HORIZONTAL | SWT.SEPARATOR);
        final GridData gridData_3 = new GridData(GridData.FILL, GridData.CENTER, false, false, 2, 1);
        gridData_3.heightHint = 8;
        label_1.setLayoutData(gridData_3);
        objParent.layout();
    }

    private void initForm() {
        mailOnError.setText(objJobDataProvider.getValue("mail_on_error"));
        mailOnWarning.setText(objJobDataProvider.getValue("mail_on_warning"));
        mailOnSuccess.setText(objJobDataProvider.getValue("mail_on_success"));
        mailOnProcess.setText(objJobDataProvider.getValue("mail_on_process"));
        mailOnDelayAfterError.setText(objJobDataProvider.getValue("mail_on_delay_after_error"));
        mailOnDelayAfterError.setEnabled("yes".equals(mailOnError.getText()) || "yes".equals(mailOnWarning.getText()));
        mailTo.setText(objJobDataProvider.getValue("log_mail_to"));
        mailCC.setText(objJobDataProvider.getValue("log_mail_cc"));
        mailBCC.setText(objJobDataProvider.getValue("log_mail_bcc"));
    }

}