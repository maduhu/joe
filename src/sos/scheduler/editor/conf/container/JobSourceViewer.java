package sos.scheduler.editor.conf.container;

import static sos.scheduler.editor.app.SOSJOEMessageCodes.JOE_E_0002;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;

import sos.scheduler.editor.app.ErrorLog;
import sos.scheduler.editor.classes.FormBaseClass;
import sos.scheduler.editor.classes.TextArea;
import sos.scheduler.editor.classes.TextArea.enuSourceTypes;
import sos.scheduler.editor.classes.WindowsSaver;
import sos.scheduler.editor.conf.listeners.JOEListener;

public class JobSourceViewer extends FormBaseClass {

	// TODO f�r die reine XML-Anzeige einfach ein Browser-Control verwenden und dort links verwenden, wie im WiKi
	@SuppressWarnings("unused")
	private final String	conSVNVersion		= "$Id$";
	private final String	conClassName		= "JobSourceViewer";
	private TextArea		txtArea4XMLSource	= null;

	private WindowsSaver			objFormPosSizeHandler	= null;

	//	public JobSourceViewer(final Composite pParentComposite, final JobListener pobjDataProvider) {
	public JobSourceViewer(final Composite pParentComposite, final JOEListener pobjDataProvider) {
		super(pParentComposite, pobjDataProvider);
		objFormPosSizeHandler = new WindowsSaver(this.getClass(), getShell(), 643, 600);
		objFormPosSizeHandler.setKey(conClassName);
		createGroup();
	}

	public void apply() {
		// if (isUnsaved())
		// addParam();
	}

	public boolean isUnsaved() {
		return false;
	}

	public void refreshContent() {
		txtArea4XMLSource.refreshContent();
	}

	private void createGroup() {

		try {
			createGroup2();
		}
		catch (Exception e) {
			new ErrorLog(JOE_E_0002.params(conClassName), e);
		}
	}

	private void createGroup2() {
		try {
			showWaitCursor();

			//		Group gSourceViewer = SOSJOEMessageCodes.JOE_G_JobSourceViewer_SourceViewer.Control(new Group(objParent, SWT.NONE));
			//		final GridData gridData_5 = new GridData(GridData.FILL, GridData.FILL, true, true, 13, 1);
			//		gridData_5.heightHint = 100;
			//		gridData_5.minimumHeight = 30;
			//		gSourceViewer.setLayoutData(gridData_5);
			//		final GridLayout gridLayout_2 = new GridLayout();
			//		gridLayout_2.marginHeight = 0;
			//		gridLayout_2.numColumns = 4;
			//		gSourceViewer.setLayout(gridLayout_2);

			txtArea4XMLSource = new TextArea(objParent, SWT.V_SCROLL | SWT.MULTI | SWT.BORDER | SWT.H_SCROLL);
			txtArea4XMLSource.setEditable(false);
			txtArea4XMLSource.setDataProvider(objJobDataProvider, enuSourceTypes.xmlSource);
			txtArea4XMLSource.setFormHandler(objFormPosSizeHandler);
		}
		catch (Exception e) {
			new ErrorLog(JOE_E_0002.params(conClassName), e);
		}
		finally {
			restoreCursor();
		}
	}

}
