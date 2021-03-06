package sos.scheduler.editor.conf.container;
import org.apache.log4j.Logger;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import sos.scheduler.editor.classes.FormBaseClass;

import com.sos.joe.globals.messages.SOSJOEMessageCodes;
import com.sos.joe.objects.job.JobListener;
//import org.eclipse.swt.events.VerifyEvent;
//import org.eclipse.swt.events.VerifyListener;

public class JobJavaAPI extends FormBaseClass<JobListener>  {

	private final String conClassName = this.getClass().getSimpleName();
	private final Logger logger = Logger.getLogger(this.getClass());
	private final String	conSVNVersion			= "$Id$";

	private boolean init = true;
	private Text tClasspath = null;
	private Text txtJavaOptions = null;
	private Text tbxClassName = null;

	public JobJavaAPI(final Composite pParentComposite, final JobListener pobjJobDataProvider,final JobJavaAPI that) {
		super(pParentComposite, pobjJobDataProvider);
		objJobDataProvider = pobjJobDataProvider;
		init = true;
		createGroup();
		init = false;
		getValues(that);
		logger.debug(conClassName + "\n" + conSVNVersion);
	}

	private void getValues(final JobJavaAPI that){
	    if (that == null){
	        return;
	    }

        tClasspath.setText(that.tClasspath.getText());
        txtJavaOptions.setText(that.txtJavaOptions.getText());
        tbxClassName.setText(that.tbxClassName.getText());

	}

	@Override
	public void createGroup() {
		showWaitCursor();

		Group gScript_2 = new Group(objParent, SWT.NONE);
		GridLayout lgridLayout = new GridLayout();
		lgridLayout.numColumns = 13;
		gScript_2.setLayout(lgridLayout);
		setResizableV(gScript_2);

		Label lblClassNameLabel = SOSJOEMessageCodes.JOE_L_JobJavaAPI_Classname.Control(new Label(gScript_2, SWT.NONE));
		GridData labelGridData = new GridData(SWT.BEGINNING, SWT.CENTER, false, false, 1, 1);
		lblClassNameLabel.setLayoutData(labelGridData);

		tbxClassName = SOSJOEMessageCodes.JOE_T_JobJavaAPI_Classname.Control(new Text(gScript_2, SWT.BORDER));
		tbxClassName.setEnabled(true);
		tbxClassName.setText(objJobDataProvider.getJavaClass());
//		tbxClassName.addVerifyListener(new VerifyListener() {
//			public void verifyText(final VerifyEvent e) {
				/*if (e.text.length() > 0 && objJobDataProvider.isJava() && objJobDataProvider.getSource().length() > 0) {
					MsgWarning("Please remove Script-Code first.");
					e.doit = false;
					return;
				}*/
//			}
//		});
		GridData gd_tClass = new GridData(GridData.FILL, GridData.CENTER, true, false);
		gd_tClass.horizontalSpan = 8;
		tbxClassName.setLayoutData(gd_tClass);
		tbxClassName.addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(final ModifyEvent e) {
				if (!init) {
					if (objJobDataProvider.isJava()) {
						objJobDataProvider.setJavaClass(tbxClassName.getText());
					}
				}
			}
		});

		// TODO Jobdoc f�r die JAva-Klasse anzeigen ...
		// TODO die verf�gbaren Java-Klassen in einer combobox ausw�hlbar machen ...
		Group gScript_1 = gScript_2;
		new Label(gScript_1, SWT.NONE);
		new Label(gScript_1, SWT.NONE);
		new Label(gScript_1, SWT.NONE);
		new Label(gScript_1, SWT.NONE);

		Label lblNewLabel_1 = SOSJOEMessageCodes.JOE_L_JobJavaAPI_Classpath.Control(new Label(gScript_2, SWT.NONE));
		lblNewLabel_1.setLayoutData(labelGridData);

		tClasspath = SOSJOEMessageCodes.JOE_T_JobJavaAPI_Classpath.Control(new Text(gScript_2, SWT.BORDER));
		tClasspath.setEnabled(true);
		tClasspath.setText(objJobDataProvider.getClasspath());
        GridData gd_tClasspath = new GridData(GridData.FILL, GridData.CENTER, true, false);
        gd_tClasspath.horizontalSpan = 8;
        tClasspath.setLayoutData(gd_tClasspath);
        tClasspath.addModifyListener(new ModifyListener() {
            @Override
			public void modifyText(final ModifyEvent e) {
                if (!init) {
                    if (objJobDataProvider.isJava()) {
                        objJobDataProvider.setClasspath(tClasspath.getText());
                    }
                }
            }
        });

		new Label(gScript_2, SWT.NONE);
		new Label(gScript_2, SWT.NONE);
		new Label(gScript_2, SWT.NONE);
		new Label(gScript_2, SWT.NONE);

		final Label java_optionsLabel = SOSJOEMessageCodes.JOE_L_JobJavaAPI_Options.Control(new Label(gScript_2, SWT.NONE));
		java_optionsLabel.setLayoutData(labelGridData);

		txtJavaOptions = SOSJOEMessageCodes.JOE_T_JobJavaAPI_Options.Control(new Text(gScript_2, SWT.BORDER));
		txtJavaOptions.setText(objJobDataProvider.getJavaOptions());
		txtJavaOptions.addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(final ModifyEvent e) {
				if (!init) {
				   objJobDataProvider.setJavaOptions(txtJavaOptions.getText());
				}
			}
		});
		txtJavaOptions.setLayoutData(gd_tClass);

		gScript_1.setVisible(true);
		gScript_1.redraw();
		restoreCursor();
	}

    public Text getTClasspath() {
        return tClasspath;
    }

    public Text getTbxClassName() {
        return tbxClassName;
    }
}
