package sos.scheduler.editor.conf.listeners;
import java.util.Iterator;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.TreeItem;
import org.jdom.Element;

import com.sos.joe.globals.JOEConstants;
import com.sos.joe.globals.interfaces.ISchedulerUpdate;
import com.sos.joe.globals.misc.TreeData;
import com.sos.joe.globals.options.Options;
import com.sos.joe.xml.Utils;
import com.sos.joe.xml.jobscheduler.SchedulerDom;

public class JobCommandExitCodesListener {
	private ISchedulerUpdate	_main		= null;
	private SchedulerDom		_dom		= null;
	private Element				_command	= null;
	private Element				_job		= null;

	public JobCommandExitCodesListener(final SchedulerDom dom, final Element command, final ISchedulerUpdate update) {
		_dom = dom;
		_command = command;
		_main = update;
		if (_command != null)
			_job = _command.getParentElement();
	}

	public void fillCommands(final Element job, final TreeItem parent, final boolean expand) {
		List commands = job.getChildren("commands");
		java.util.ArrayList listOfReadOnly = _dom.getListOfReadOnlyFiles();
		if (commands != null) {
			Iterator it = commands.iterator();
			parent.removeAll();
			while (it.hasNext()) {
				Element e = (Element) it.next();
				if (e.getAttributeValue("on_exit_code") != null) {
					TreeItem item = new TreeItem(parent, SWT.NONE);
					item.setText(e.getAttributeValue("on_exit_code"));
					item.setData(new TreeData(JOEConstants.JOB_COMMAND, e, Options.getHelpURL("job.commands")));
					if (listOfReadOnly != null && listOfReadOnly.contains(Utils.getAttributeValue("name", job))) {
						item.setForeground(Display.getCurrent().getSystemColor(SWT.COLOR_GRAY));
					}
					else {
						item.setForeground(Display.getCurrent().getSystemColor(SWT.COLOR_BLACK));
					}
				}
			}
		}
		parent.setExpanded(expand);
	}

	public boolean isDisabled() {
		boolean disabled = Utils.getAttributeValue("enabled", _job).equalsIgnoreCase("no");
		return disabled;
	}

	public String getName() {
		return Utils.getAttributeValue("name", _job);
	}

	public void addCommand(final Element e) {
		_dom.setChanged(true);
		_dom.setChangedForDirectory("job", Utils.getAttributeValue("name", _job), SchedulerDom.MODIFY);
		_command.addContent(e);
		_main.updateExitCodesCommand();
	}

	private int getActCommand(final Table table) {
		int index = table.getSelectionIndex();
		int j = index;
		int ignore = 0;
		List c = _command.getChildren();
		Iterator it2 = c.iterator();
		while (it2.hasNext() && j >= 0) {
			Element e2 = (Element) it2.next();
			if (!e2.getName().equals("start_job") && !e2.getName().equals("add_order") && !e2.getName().equals("order")) {
				ignore++;
			}
			else {
				j--;
			}
		}
		return index + ignore;
	}

	public void deleteCommand(final Table table) {
		int j = 0;
		int index = table.getSelectionIndex();
		j = getActCommand(table);
		table.remove(index);
		List c = _command.getChildren();
		if (_command != null) {
			c.remove(j);
		}
		_main.updateExitCodesCommand();
		_dom.setChanged(true);
		_dom.setChangedForDirectory("job", Utils.getAttributeValue("name", _job), SchedulerDom.DELETE);
	}

	public String getCommandAttribute(final Table table, final String attribute) {
		int i = getActCommand(table);
		List l = _command.getChildren();
		Element e = (Element) l.get(i);
		return Utils.getAttributeValue(attribute, e);
	}

	public String getExitCode() {
		return Utils.getAttributeValue("on_exit_code", _command);
	}

	public void setExitCode(final String value, final boolean updateTree) {
		Utils.setAttribute("on_exit_code", value, _command, _dom);
		if (updateTree)
			_main.updateTreeItem(value);
	}

	public Element getCommand() {
		return _command;
	}

	public void fillCommands(final Table table) {
		boolean created;
		TableItem item = null;
		table.removeAll();
		List c = _command.getChildren();
		Iterator it2 = c.iterator();
		while (it2.hasNext()) {
			Element e2 = (Element) it2.next();
			created = false;
			if (e2.getName().equals("start_job") || e2.getName().equals("add_order") || e2.getName().equals("order")) {
				if (!created) { // Nur die commands add_order und start_job
					// anzeigen
					item = new TableItem(table, SWT.NONE);
					item.setText(1, "");
					created = true;
				}
				item.setText(0, e2.getName());
				item.setText(3, Utils.getAttributeValue("at", e2));
				if (e2.getName().equals("start_job"))
					item.setText(1, Utils.getAttributeValue("job", e2));
				if (e2.getName().equals("add_order") || e2.getName().equals("order")) {
					item.setText(1, Utils.getAttributeValue("id", e2));
					item.setText(2, Utils.getAttributeValue("job_chain", e2));
				}
			}
		}
	}
}
