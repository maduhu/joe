package sos.scheduler.editor.conf.forms;

import org.eclipse.swt.SWT;

import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.jdom.Element;
import sos.scheduler.editor.app.Editor;
import sos.scheduler.editor.app.IUnsaved;
import sos.scheduler.editor.app.IUpdateLanguage;
import sos.scheduler.editor.app.MainWindow;
import sos.scheduler.editor.app.Messages;
import sos.scheduler.editor.app.Utils;
import sos.scheduler.editor.conf.ISchedulerUpdate;
import sos.scheduler.editor.conf.SchedulerDom;
import sos.scheduler.editor.conf.listeners.JobChainListener;
import sos.scheduler.editor.conf.listeners.OrdersListener;

public class JobChainForm extends Composite implements IUnsaved, IUpdateLanguage {


	//private static final String GROUP_NODES_TITLE           = "Chain Nodes";

	//private static final String GROUP_FILEORDERSOURCE_TITLE = "File Order Sources"; 


	private JobChainListener    listener                    = null;

	private Group               jobChainGroup               = null;

	private Label               chainNameLabel              = null;

	private Text                tName                       = null;

	private Button              bRecoverable                = null;

	private Button              bVisible                    = null;

	//private Button              bApplyChain                 = null;

	//private SashForm            sashForm                    = null;

	//private Group               sashForm                    = null;

    private Button              butDetails                  = null;

	private ISchedulerUpdate    update                      = null;


	private Button              butDistributed              = null; 



	private Text                txtTitle                    = null; 


	public JobChainForm(Composite parent, int style, SchedulerDom dom, Element jobChain) {
		super(parent, style);
		listener = new JobChainListener(dom, jobChain);
		initialize();
		setToolTipText();
		fillChain(false, false);
		this.setEnabled(Utils.isElementEnabled("job_chain", dom, jobChain));

	}


	public void apply() {
		//if (bApplyChain.isEnabled())
		//	applyChain();
		//if (bApplyNode.isEnabled())
		//	applyNode();
	}


	public boolean isUnsaved() {
		//return bApplyChain.isEnabled() || bApplyNode.isEnabled();
		return false;
	}


	private void initialize() {

		this.setLayout(new FillLayout());
		createGroup();
		setSize(new org.eclipse.swt.graphics.Point(676, 464));
	}


	/**
	 * This method initializes group
	 */
	private void createGroup() {
		jobChainGroup = new Group(this, SWT.NONE);        
		jobChainGroup.setText("Job Chain:" + (listener.getChainName() != null ? listener.getChainName() : ""));

		final GridLayout gridLayout = new GridLayout();
		gridLayout.marginTop = 10;
		gridLayout.numColumns = 5;
		jobChainGroup.setLayout(gridLayout);
		chainNameLabel = new Label(jobChainGroup, SWT.NONE);
		final GridData gridData_6 = new GridData(GridData.BEGINNING, GridData.BEGINNING, false, false);
		chainNameLabel.setLayoutData(gridData_6);
		chainNameLabel.setText("Chain Name ");
		tName = new Text(jobChainGroup, SWT.BORDER);
		final GridData gridData_4 = new GridData(GridData.FILL, GridData.BEGINNING, true, false, 3, 1);
		gridData_4.widthHint = 273;
		tName.setLayoutData(gridData_4);
		tName.setText(listener.getChainName());
		tName.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_WHITE));
		tName.addModifyListener(new org.eclipse.swt.events.ModifyListener() {
			public void modifyText(org.eclipse.swt.events.ModifyEvent e) {
				boolean existname = Utils.existName(tName.getText(), listener.getChain(), "job_chain");
				if (existname)
					tName.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_YELLOW));
				else {
					//getShell().setDefaultButton(bApplyChain);
					tName.setBackground(null);
				}

				String oldJobChainname = listener.getChainName();
				boolean _continue = true;
				if(listener.getChainName().length() > 0  && !tName.getText().equals(listener.getChainName()))
					if(!Utils.checkElement(listener.getChainName(), listener.get_dom(), Editor.JOB_CHAIN, null))
						_continue = false;

				if(_continue) {
					listener.setChainName(tName.getText());
					if(update != null)
						update.updateJobChain(tName.getText(), oldJobChainname);
				}
				//bApplyChain.setEnabled(!existname && !tName.equals(""));
				//mo neu


			}
		});

		butDetails = new Button(jobChainGroup, SWT.NONE);
		butDetails.setLayoutData(new GridData());
		butDetails.setEnabled(true);
		butDetails.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(final SelectionEvent e) {
				showDetails(null);
			}
		});
		butDetails.setText("Details");
		/*bApplyChain = new Button(jobChainGroup, SWT.NONE);
		bApplyChain.setLayoutData(new GridData(GridData.FILL, GridData.BEGINNING, false, false));
		bApplyChain.setText("A&pply Job Chain");
		bApplyChain.setEnabled(false);
		bApplyChain.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
			public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
				applyChain();
			}
		});
		 */
		final Label titleLabel = new Label(jobChainGroup, SWT.NONE);
		titleLabel.setLayoutData(new GridData());
		titleLabel.setText("Title");

		txtTitle = new Text(jobChainGroup, SWT.BORDER);
		txtTitle.addModifyListener(new ModifyListener() {
			public void modifyText(final ModifyEvent e) {
				//getShell().setDefaultButton(bApplyChain);				
				//bApplyChain.setEnabled(true);
				//mo neu
				listener.setTitle(txtTitle.getText());

			}
		});
		txtTitle.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, true, false, 3, 1));
		new Label(jobChainGroup, SWT.NONE);
		new Label(jobChainGroup, SWT.NONE);

		butDistributed = new Button(jobChainGroup, SWT.CHECK);
		butDistributed.setLayoutData(new GridData(GridData.BEGINNING, GridData.BEGINNING, false, false));
		butDistributed.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(final SelectionEvent e) {
				listener.setDistributed(butDistributed.getSelection());
				//getShell().setDefaultButton(bApplyChain);
				//bApplyChain.setEnabled(true);
			}
		});
		butDistributed.setText("Distributed");
		butDistributed.setSelection(listener.isDistributed());
		bRecoverable = new Button(jobChainGroup, SWT.CHECK);
		bRecoverable.setLayoutData(new GridData(GridData.BEGINNING, GridData.BEGINNING, false, false));
		bRecoverable.setSelection(true);
		bRecoverable.setText("Orders Recoverable");
		bRecoverable.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
			public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {				
				//getShell().setDefaultButton(bApplyChain);
				//bApplyChain.setEnabled(true);
				//mo neu
				listener.setRecoverable(bRecoverable.getSelection());

			}
		});
		bVisible = new Button(jobChainGroup, SWT.CHECK);
		bVisible.setLayoutData(new GridData(GridData.BEGINNING, GridData.BEGINNING, false, false));
		bVisible.setSelection(true);
		bVisible.setText("Visible");
		bVisible.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
			public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
				listener.setVisible(bVisible.getSelection());
				//getShell().setDefaultButton(bApplyChain);
				//bApplyChain.setEnabled(true);
			}
		});
		new Label(jobChainGroup, SWT.NONE);

		if(!listener.get_dom().isLifeElement()) {
		}
		//butUp.setText("Up");
		//butDown.setText("Down");


		//gFileOrderSource = new Group(sashForm, SWT.VERTICAL);
		//group.setTabList(new Control[] {cChains, fileOrderSourceGroup, gNodes, fileOrderSinkGroup, label_2});

	}





	private void fillChain(boolean enable, boolean isNew) {
		tName.setEnabled(true);
		bRecoverable.setEnabled(true);
		bVisible.setEnabled(true);

		tName.setText(listener.getChainName());
		txtTitle.setText(listener.getTitle());

		bRecoverable.setSelection(listener.getRecoverable() );
		bVisible.setSelection(listener.getVisible());

		tName.setBackground(null);
		//bApplyChain.setEnabled(enable);

		//if (enable && !isNew) {
		/*} else {
		 bNewNode.setEnabled(false);
		 bNewFileOrderSource.setEnabled(false);
		 }*/

		enableNode(false);
		enableFileOrderSource(false);           


	}


	private void enableNode(boolean enable) {


	}

	private void enableFileOrderSource(boolean enable) {
	}



	/*private void fillNode(boolean clear) {
		boolean fullNode = listener.isFullNode();
		boolean fileSinkNode = listener.isFileSinkNode();
		boolean endNode = !fullNode && !fileSinkNode;


	}
*/




	/*private void applyChain() {
		String oldJobChainname = listener.getChainName();

		boolean _continue = true;
		if(listener.getChainName().length() > 0  && !tName.getText().equals(listener.getChainName()))
			if(!Utils.checkElement(listener.getChainName(), listener.get_dom(), Editor.JOB_CHAIN, null))
				_continue = false;

		if(_continue) {
			listener.applyChain(tName.getText(), bRecoverable.getSelection(), bVisible.getSelection(), butDistributed.getSelection(), txtTitle.getText());
			update.updateJobChain(tName.getText(), oldJobChainname);
		}
		fillChain(true, false);
		//bApplyChain.setEnabled(false);
		if(listener.getChainName() != null && listener.getChainName().length() > 0) {
			butDetails.setEnabled(true);
		}  else {
			butDetails.setEnabled(false);
		}
	}*/

	public void setISchedulerUpdate(ISchedulerUpdate update_) {
		update = update_;
	}

	private void showDetails(String state) {
		if(tName.getText() != null && tName.getText().length() > 0) {
			OrdersListener ordersListener =  new OrdersListener(listener.get_dom(), update);
			String[] listOfOrders = ordersListener.getOrderIds();
			//DetailDialogForm detail = new DetailDialogForm(tName.getText(), listOfOrders);
			//detail.showDetails();
			boolean isLifeElement = listener.get_dom().isLifeElement() || listener.get_dom().isDirectory(); 

			if(state == null) {
				DetailDialogForm detail = new  DetailDialogForm(tName.getText(), listOfOrders, isLifeElement, listener.get_dom().getFilename());
				detail.showDetails();
			} else {
				DetailDialogForm detail = new DetailDialogForm(tName.getText(), state, listOfOrders, isLifeElement, listener.get_dom().getFilename());
				detail.showDetails();
			} 

		} else {
			MainWindow.message(getShell(), sos.scheduler.editor.app.Messages.getString("assistent.cancel"), SWT.ICON_WARNING | SWT.OK |SWT.CANCEL );
		}

	}

	public void setToolTipText() {

		tName.setToolTipText(Messages.getTooltip("job_chains.chain.name"));
		bRecoverable.setToolTipText(Messages.getTooltip("job_chains.chain.orders_recoverable"));
		bVisible.setToolTipText(Messages.getTooltip("job_chains.chain.visible"));
		//bApplyChain.setToolTipText(Messages.getTooltip("job_chains.chain.btn_apply"));
		butDetails.setToolTipText(Messages.getTooltip("job_chains.chain.details"));
		butDistributed.setToolTipText(Messages.getTooltip("job_chains.distributed"));
		txtTitle.setToolTipText(Messages.getTooltip("job_chain.title"));
	}


} // @jve:decl-index=0:visual-constraint="10,10"