package sos.scheduler.editor.conf.listeners;


import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;
import org.jdom.Element;
import org.jdom.xpath.XPath;

import sos.util.SOSClassUtil;

import com.sos.joe.globals.interfaces.ISchedulerUpdate;
import com.sos.joe.globals.messages.ErrorLog;
import com.sos.joe.xml.Utils;
import com.sos.joe.xml.jobscheduler.MergeAllXMLinDirectory;
import com.sos.joe.xml.jobscheduler.SchedulerDom;


public class OrderListener {


    private final ISchedulerUpdate _main;

    private final SchedulerDom     _dom;

    private String[]         _chains = new String[0];

    private List             _params;

    private final Element          _order;

    private final Element          _config;


    public OrderListener(final SchedulerDom dom, final Element order, final ISchedulerUpdate update) {

        _dom = dom;
        _config = _dom.getRoot().getChild("config");
        _order = order;
        _main = update;

    }


    private void initParams() {

        if (_order.getChild("params") != null) {
            _params = _order.getChild("params").getChildren();
        }

        if (_params == null) {
            _order.addContent(0, new Element("params"));
            _params = _order.getChild("params").getChildren();
        }
    }


    public void clearParams() {
        _params = null;
    }


    public void fillParams(final Table tableParameters) {
        clearParams();
        tableParameters.removeAll();
        initParams();


        if (_params != null) {
            Iterator it = _params.iterator();
            while (it.hasNext()) {
                Object o = it.next();
                if (o instanceof Element) {
                    Element e = (Element) o;
                    if (e.getName().equals("param")) {
                        TableItem item = new TableItem(tableParameters, SWT.NONE);
                        item.setText(0, ((Element) o).getAttributeValue("name"));
                        item.setText(1, ((Element) o).getAttributeValue("value"));
                    }
                }
            }
        }
    }


    public void deleteParameter(final Table table, final int index) {
        if (_params != null) {
            _params.remove(index);
            _dom.setChangedForDirectory("order", Utils.getAttributeValue("job_chain",_order)+","+Utils.getAttributeValue("id",_order), SchedulerDom.MODIFY);
            _dom.setChanged(true);
        }
        table.remove(index);
    }


    public void saveParameter(final Table table, final String name, final String value) {
        boolean found = false;
        //String value2 = value.replaceAll("\"", "&quot;");

        if (_params != null) {

            int index = 0;
            Iterator it = _params.iterator();
            while (it.hasNext()) {
                Object o = it.next();
                if (o instanceof Element) {
                    Element e = (Element) o;
                    if (e.getName().equals("param")) {
                        if (name.equals(e.getAttributeValue("name"))) {
                            found = true;
                            //e.setAttribute("value", value2);
                            e.setAttribute("value", value);
                            _dom.setChanged(true);
                            _dom.setChangedForDirectory("order", Utils.getAttributeValue("job_chain",_order)+","+Utils.getAttributeValue("id",_order), SchedulerDom.MODIFY);
                            table.getItem(index).setText(1, value);
                        }
                    }
                    index++;
                }
            }
        }

        if (!found) {
            Element e = new Element("param");

            e.setAttribute("name", name);
            //e.setAttribute("value", value2);
            e.setAttribute("value", value);
            _dom.setChanged(true);
            _dom.setChangedForDirectory("order", Utils.getAttributeValue("job_chain",_order)+","+Utils.getAttributeValue("id",_order), SchedulerDom.MODIFY);

            if (_params == null)
                initParams();
            if (_params != null)
                _params.add(e);

            TableItem item = new TableItem(table, SWT.NONE);
            item.setText(new String[] { name, value });
        }
    }


    public String getCommandAttribute(final String attribute) {
        return Utils.getAttributeValue(attribute, _order);
    }


    public String[] getStates() {
    	String[] retVal = new String[]{""};
    	ArrayList<String> stateList = new ArrayList<String>();

    	try {
    		String jobChainname = getCommandAttribute("job_chain");
    		XPath x3 = XPath.newInstance("//job_chain[@name='"+ jobChainname + "']");
    		Element jobChain = (Element)x3.selectSingleNode(_dom.getDoc());

    		if(jobChain == null) {
    			jobChainname = getCommandAttribute("job_chain_node.job_chain");
    			x3 = XPath.newInstance("//job_chain_node.job_chain[@name='"+ jobChainname + "']");
        		jobChain = (Element)x3.selectSingleNode(_dom.getDoc());

    		}
    		if(jobChain == null)
    			return retVal;


    		List<Element> l = jobChain.getChildren();
    		for(int i = 0; i < l.size(); i++) {
    			Element e = l.get(i);
    			String state = Utils.getAttributeValue("state", e);
    			if(state.length() > 0)
					stateList.add(state);
    		}

    		if(stateList.size() > 0) {
    			retVal = new String[stateList.size()];
    			for(int i = 0; i < stateList.size(); i++)
    				retVal[i] = stateList.get(i).toString();
    		}
    		//if(!listOfElement_3.isEmpty())
    	} catch (Exception e){
    		try {
				new ErrorLog("error in " + SOSClassUtil.getMethodName() , e);
			} catch(Exception ee) {
			}
    	}
    	return retVal;

    }

    public void setCommandAttribute(final String name, final String value) {
    	_dom.setChanged(true);
    	_dom.setChangedForDirectory("order", Utils.getAttributeValue("job_chain",_order)+","+Utils.getAttributeValue("id",_order), SchedulerDom.DELETE);
        Utils.setAttribute(name, value, _order, _dom);
        if(name.equals("id"))
        	_main.updateOrder(value);
        _dom.setChangedForDirectory("order", Utils.getAttributeValue("job_chain",_order)+","+Utils.getAttributeValue("id",_order), SchedulerDom.MODIFY);
    }


    public void setOrderId(final String id, final boolean updateTree, final boolean rem) {

    	String removename = Utils.getAttributeValue("job_chain",_order)+","+Utils.getAttributeValue("id",_order);
    	Utils.setAttribute("id", id, _order, _dom);
    	if(rem)
    		_dom.setChangedForDirectory("order", removename, SchedulerDom.DELETE);

        if (updateTree)
            _main.updateOrder(id);

        _dom.setChangedForDirectory("order", Utils.getAttributeValue("job_chain",_order)+","+Utils.getAttributeValue("id",_order), SchedulerDom.MODIFY);
    }


    public boolean getCommandReplace() {
        return Utils.getAttributeValue("replace", _order).equalsIgnoreCase("yes") || Utils.getAttributeValue("replace", _order).length() == 0;
    }


    public String[] getJobChains() {
    	if(_dom.isLifeElement()) {
    		if(_dom.getFilename()!= null) {
    			try {
    			java.io.File f = new java.io.File(_dom.getFilename());
    			java.util.Vector filelist = sos.util.SOSFile.getFilelist(f.getParent(), MergeAllXMLinDirectory.MASK_JOB_CHAIN,java.util.regex.Pattern.CASE_INSENSITIVE,true);

    			Object[] o = filelist.toArray();
    			_chains= new String[o.length];
    			for(int i= 0; i < o.length; i++) {
    				String n = ((java.io.File)o[i]).getName();
    				n = n.substring(0, n.indexOf(".job_chain.xml"));
    				_chains[i] = n;
    			}
    			return _chains;
    			//_chains = new java.io.File(_dom.getFilename());
    			} catch(Exception e) {
    				try {
						new ErrorLog("error in " + SOSClassUtil.getMethodName() , e);
					} catch(Exception ee) {

					}
    				System.out.println(e.getMessage());

    			}
    		}

    		_chains = new String[0];
    		return _chains;
    	}

        Element element = _config.getChild("job_chains");
        if (element != null) {
            List chains = element.getChildren("job_chain");
            _chains = new String[chains.size()];
            int index = 0;
            Iterator it = chains.iterator();
            while (it.hasNext()) {
                String name = ((Element) it.next()).getAttributeValue("name");
                _chains[index++] = name != null ? name : "";
            }
        } else
            _chains = new String[0];

        return _chains;
    }


	public Element getOrder() {
		return _order;
	}

	 public boolean existName(final String removename){

	    	if(_dom.isDirectory()) {
	    		java.util.List l = getOrder().getParentElement().getChildren("order");
	    		for(int i = 0; i < l.size(); i++) {
	    			Element e = (Element)l.get(i);
	    			String name = Utils.getAttributeValue("id", e) + "," + Utils.getAttributeValue("job_chain", e);
	    			if(!e.equals(_order) && removename != null && name.equals(removename))
	    				return true;
	    			/*if(!h.containsKey(name)) {
	    				h.put(name, "");
	    			} else {
	    				return true;
	    			}*/
	    		}
	    	}

	    	return false;
	    }
}
