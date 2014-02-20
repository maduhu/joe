 package sos.scheduler.editor.app;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.stream.StreamSource;

import org.eclipse.swt.SWT;
import org.jdom.Attribute;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.Namespace;
import org.jdom.input.SAXBuilder;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;
import org.jdom.transform.JDOMResult;
import org.jdom.transform.JDOMSource;

import sos.util.SOSClassUtil;

import com.sos.i18n.I18NBase;
import com.sos.i18n.annotation.I18NResourceBundle;

@I18NResourceBundle(baseName = "JOEMessages", defaultLocale = "en")
public abstract class DomParser extends I18NBase {

	public static final String	conSchema_SCHEDULER_EDITOR_SCHEMA	= "scheduler_editor_schema";
	public static final String	conMessage_MAIN_LISTENER_OUTPUT_INVALID	= "MainListener.outputInvalid";
	private final static String	conSVNVersion			= "$Id$";

	protected static final String		DEFAULT_ENCODING	= "ISO-8859-1";
	private Document					_doc;
	private boolean						_changed			= false;
	private boolean						_init				= false;
	private IDataChanged				_changedListener;
	private final HashMap<String, String[]>	_orders				= new HashMap<String, String[]>();
	// private String[] _schemaTmpFile;
	// private String[] _schemaResource;

	private String						_xslt;
	private String						_filename			= null;

	/** wann wurde die Konfigurationsdatei zuletzt ge�ndert. Dieser parameter soll dazu dienen, mitzubekommen, ob die
	 * Konfigurationsdatei von einem anderen Process ver�ndert wurde*/
	private long						_lastModifiedFile	= 0;
	public DomParser(final String[] schemaTmp, final String[] schemaResource, final String xslt) {
		super("JOEMessages"); // , Options.getLanguage());
		_xslt = xslt;
	}

	protected void putDomOrder(final String parentElement, final String[] orderedElements) {
		_orders.put(parentElement, orderedElements);
	}

	protected HashMap<String, String[]> getDomOrders() {
		return _orders;
	}

	public void setFilename(final String filename) {
		_filename = filename;
		readFileLastModified();
		// readFileMD5encrypt();
	}

	public String getFilename() {
		return _filename;
	}

	/**
	 * Liest den letzten �nderungszeitpunkt (in long) der Konfigurationsdatei.
	 * Wurde ausserhalb vom Editor etwas ver�ndert?
	 *
	 */
	public void readFileLastModified() {

		if (_filename == null)
			_lastModifiedFile = 0;

		File f = new File(_filename);
		if (f.exists())
			_lastModifiedFile = f.lastModified();
		else
			_lastModifiedFile = 0;

		// System.out.println("domparser= " + _lastModifiedFile);

	}

	public void setXSLT(final String xslt) {
		_xslt = xslt;
	}

	public String getXSLT() {
		return _xslt;
	}

	public void setDataChangedListener(final IDataChanged listener) {
		_changedListener = listener;
	}

	public IDataChanged getDataChangedListener() {
		return _changedListener;
	}

	public Element getRoot() {
		return _doc.getRootElement();
	}

	public Document getDoc() {
		return _doc;
	}

	public void setDoc(final Document doc) {
		_doc = doc;
	}

	public Namespace getNamespace() {
		return getRoot().getNamespace();
	}

	public Namespace getNamespace(final String name) {
		return getRoot().getNamespace(name);
	}

	public List getAdditinalNamespaces() {
		return getRoot().getAdditionalNamespaces();
	}

	protected String[] writeSchemaFile() throws IOException {
		String strT = "";
		try {
			String[] s = new String[1];
			s[0] = "";
			if (this instanceof sos.scheduler.editor.actions.ActionsDom)
				s[0] = getClass().getResource(Options.getActionSchema()).toString();
			else
				if (this instanceof com.sos.jobdoc.DocumentationDom) {
					// s[0] = getClass().getResource(Options.getDocSchema()).toString();
					strT = Options.getDocSchema();
					if (strT != null) {
						s[0] = getClass().getResource(strT).toString();
					}
				}
				else {
					if (this instanceof sos.scheduler.editor.conf.SchedulerDom)
						s[0] = getClass().getResource(Options.getSchema()).toString();
					else
						s[0] = "";
				}
			return s;
		}
		catch (Exception e) {
			String strM = this.getMsg("JOE-E-1100: Schema file '%1$s' not found, but required. check editor.properties.\n Execption is %2$s", strT, e.toString());
			try {
				new ErrorLog(strM, e);
			}
			catch (Exception ee) {
			}
			throw new IOException("error in writeSchemaFile(). could get schema " + e.toString());
		}
	}

	/* protected String[] writeSchemaFile_old() throws IOException {
	     ArrayList urls = new ArrayList();

	     for (int i = 0; i < _schemaTmpFile.length; i++) {
	         if (_schemaTmpFile[i] != null && !_schemaTmpFile[i].equals("") && _schemaResource[i] != null
	                 && !_schemaResource[i].equals("")) {

	             File tmp = File.createTempFile(_schemaTmpFile[i], ".xsd");
	             tmp.deleteOnExit();

	             InputStream in = getClass().getResourceAsStream(_schemaResource[i]);
	             FileOutputStream out = new FileOutputStream(tmp, true);

	             int c;
	             while ((c = in.read()) != -1)
	                 out.write(c);

	             in.close();
	             out.close();

	             urls.add(tmp.getAbsolutePath());
	         }
	     }

	     return (String[]) urls.toArray(new String[urls.size()]);
	 }

	*/
	protected SAXBuilder getBuilder(final boolean validate) throws IOException {

		SAXBuilder builder = new SAXBuilder(validate);
		if (validate) {
			builder.setProperty("http://java.sun.com/xml/jaxp/properties/schemaLanguage", "http://www.w3.org/2001/XMLSchema");
			builder.setProperty("http://java.sun.com/xml/jaxp/properties/schemaSource", writeSchemaFile());
		}
		return builder;
	}

	public SAXBuilder getBuilder() throws IOException {
		return getBuilder(false);
	}

	public abstract boolean read(String filename) throws JDOMException, IOException;

	public abstract boolean read(String filename, boolean validate) throws JDOMException, IOException;

	public abstract boolean readString(String str, boolean validate) throws JDOMException, IOException;

	public abstract void write(String filename) throws IOException, JDOMException;

	public abstract String getXML(Element element) throws JDOMException;

	public void reorderDOM() {
		reorderDOM(getDoc().getRootElement());
	}

	protected void reorderDOM(final Element element) {
		reorderDOM(element, null);
	}

	protected void reorderDOM(final Element element, final Namespace ns) {
		// escape element Attributes
		escape(element);
		String strT = "huhu";
		// check if an order list exists for this element
		if (getDomOrders().containsKey(element.getName())) {
			// get children names in right order of this element
			String[] order = getDomOrders().get(element.getName());

			// iterate children names
			for (String element2 : order) {
				// get _new_ list of the children
				List list = new ArrayList(element.getChildren(element2, ns));
				if (list.size() > 0) {
					// remove them all
					element.removeChildren(element2, ns);

					// iterate children list
					for (Iterator it2 = list.iterator(); it2.hasNext();) {
						Element children = (Element) it2.next();
						// readd it at the end
						element.addContent(children);

						// recursion
						reorderDOM(children, ns);
					}
				}
			}
		}
		else {
			// reorder the children
			List children = element.getChildren();
			for (Iterator it = children.iterator(); it.hasNext();) {
				reorderDOM((Element) it.next(), ns);
			}
		}
	}

	public void deorderDOM() {
		deorderDOM(getDoc().getRootElement());
	}

	protected void deorderDOM(final Element element) {
		deorderDOM(element, null);
	}

	protected void deorderDOM(final Element element, final Namespace ns) {
		// escape element Attributes
		deEscape(element);

		// check if an order list exists for this element
		if (getDomOrders().containsKey(element.getName())) {
			// get children names in right order of this element
			String[] order = getDomOrders().get(element.getName());

			// iterate children names
			for (String element2 : order) {
				// get _new_ list of the children
				List list = new ArrayList(element.getChildren(element2, ns));
				if (list.size() > 0) {
					// remove them all
					element.removeChildren(element2, ns);

					// iterate children list
					for (Iterator it2 = list.iterator(); it2.hasNext();) {
						Element children = (Element) it2.next();
						// readd it at the end
						element.addContent(children);

						// recursion
						deorderDOM(children, ns);
					}
				}
			}
		}
		else {
			// reorder the children
			List children = element.getChildren();
			for (Iterator it = children.iterator(); it.hasNext();) {
				deorderDOM((Element) it.next(), ns);
			}
		}
	}

	public String transform(final Element element) throws TransformerFactoryConfigurationError, TransformerException, IOException {

		File tmp = null;
		try {
			Document doc = new Document((Element) element.clone());

			Transformer transformer = TransformerFactory.newInstance().newTransformer(new StreamSource(Options.getXSLT()));
			// Transformer transformer = TransformerFactory.newInstance().newTransformer(new StreamSource(getXSLT()));
			JDOMSource in = new JDOMSource(doc);
			JDOMResult out = new JDOMResult();
			transformer.transform(in, out);

			List result = out.getResult();

			tmp = File.createTempFile(Options.getXSLTFilePrefix(), Options.getXSLTFileSuffix());

			tmp.deleteOnExit();

			XMLOutputter outp = new XMLOutputter(Format.getPrettyFormat());
			outp.output(result, new FileWriter(tmp));

			return tmp.getAbsolutePath();
		}
		catch (Exception e) {
			// System.err.println("error in DomParser.transform: " + (tmp != null ? tmp.getCanonicalPath() : "")+ e.getMessage());
			try {
				new ErrorLog("error in " + SOSClassUtil.getMethodName(), e);
			}
			catch (Exception ee) {
				// tu nichts
			}
			MainWindow.message(MainWindow.getSShell(), "error in DomParser.transform: " + (tmp != null ? tmp.getCanonicalPath() : "") + e.getMessage(),
					SWT.ICON_WARNING | SWT.OK);
		}
		return "";
	}

	public boolean isChanged() {
		return _changed;
	}

	public void setChanged(final boolean changed) {
		if (!_init) {
			_changed = changed;
			if (_changedListener != null)
				_changedListener.dataChanged();
		}
	}

	public void setInit(final boolean init) {
		_init = init;
		_changed = false;
	}

	private void escape(final Element e) {
		List listOfAtrributes = e.getAttributes();
		for (int i = 0; i < listOfAtrributes.size(); i++) {
			// System.out.println(listOfAtrributes.get(i));
			Attribute attr = (Attribute) listOfAtrributes.get(i);
			// System.out.println("name  : " + attr.getName());
			// System.out.println("value : " + attr.getValue());
			// Utils.setAttribute(attr.getName(), Utils.escape(attr.getValue()), e);

			// ok e.setAttribute(attr.getName(), Utils.escape(attr.getValue()));
			Attribute a = new Attribute(attr.getName(), Utils.escape(attr.getValue()), attr.getAttributeType(), attr.getNamespace());
			e.setAttribute(a);
			// e.setAttribute(attr.getName(), Utils.escape(attr.getValue()));
			// System.out.println("neue value  : " + e.getAttributeValue(attr.getName()));

			// System.out.println("*************************************");

		}
	}

	private void deEscape(final Element e) {
		List listOfAtrributes = e.getAttributes();
		for (int i = 0; i < listOfAtrributes.size(); i++) {
			// System.out.println(listOfAtrributes.get(i));
			Attribute attr = (Attribute) listOfAtrributes.get(i);
			// System.out.println("name  : " + attr.getName());
			// System.out.println("value : " + attr.getValue());
			/*String newValue = attr.getValue();
			newValue = newValue.replaceAll("&quot;", "\"");
			newValue = newValue.replaceAll("&lt;", "<");
			newValue = newValue.replaceAll("&gt;", ">");
			newValue = newValue.replaceAll("&amp;", "&");
			*/
			// Utils.setAttribute(attr.getName(), Utils.deEscape(attr.getValue()), e);

			// ok e.setAttribute(attr.getName(), Utils.deEscape(attr.getValue()));

			Attribute a = new Attribute(attr.getName(), Utils.deEscape(attr.getValue()), attr.getAttributeType(), attr.getNamespace());
			e.setAttribute(a);

			// System.out.println("neue value  : " + newValue);

			// System.out.println("*************************************");

		}
	}

	/**
	 * @return the _init
	 */
	public boolean isInit() {
		return _init;
	}

	/**
	 * @return the _lastModifiedFile
	 */
	public long getLastModifiedFile() {
		return _lastModifiedFile;
	}

	/**
	 * @return the _lastModifiedFile
	 */
	public void setLastModifiedFile(final long lastModifiedFile) {
		_lastModifiedFile = lastModifiedFile;
	}

}
