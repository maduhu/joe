package sos.scheduler.editor.conf.listeners;

import java.io.File;

import org.apache.log4j.Logger;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.program.Program;
import org.eclipse.swt.widgets.Display;
import org.jdom.Element;
import org.jdom.JDOMException;

import sos.scheduler.editor.app.ErrorLog;
import sos.scheduler.editor.app.MainWindow;
import sos.scheduler.editor.app.Options;
import sos.scheduler.editor.app.ResourceManager;
import sos.scheduler.editor.app.TreeData;
import sos.scheduler.editor.app.Utils;
import sos.scheduler.editor.conf.SchedulerDom;
import sos.util.SOSClassUtil;

import com.sos.JSHelper.Basics.JSToolBox;
import com.sos.VirtualFileSystem.FTP.SOSFTPOptions;
import com.sos.VirtualFileSystem.Factory.VFSFactory;
import com.sos.VirtualFileSystem.Interfaces.ISOSVFSHandler;
import com.sos.VirtualFileSystem.Interfaces.ISOSVfsFileTransfer;
import com.sos.joe.interfaces.ISchedulerUpdate;
import com.sos.scheduler.model.SchedulerObjectFactory;
import com.sos.scheduler.model.objects.JSObjJob;
import com.sos.scheduler.model.objects.JSObjJobChain;
import com.sos.scheduler.model.objects.Spooler;

/**
* \class JOEListener
*
* \brief JOEListener -
*
* \details
*
*
* \code
*   .... code goes here ...
* \endcode
*
* <p style="text-align:center">
* <br />---------------------------------------------------------------------------
* <br /> APL/Software GmbH - Berlin
* <br />##### generated by ClaviusXPress (http://www.sos-berlin.com) #########
* <br />---------------------------------------------------------------------------
* </p>
* \author KB
* \version $Id$
* \see reference
*
* Created on 16.11.2011 15:25:39
 */

/**
 * @author KB
 *
 */
public class JOEListener extends JSToolBox {
	@SuppressWarnings("unused")
	private final String		conClassName			= this.getClass().getSimpleName();
	@SuppressWarnings("unused")
	private static final String	conSVNVersion			= "$Id$";
	@SuppressWarnings("unused")
	private final Logger		logger					= Logger.getLogger(this.getClass());
	@Deprecated
	protected SchedulerDom		_dom					= null;

	protected ISchedulerUpdate	_main					= null;
	@Deprecated
	protected Element			_job					= null;
	@Deprecated
	protected Element			_parent					= null;
	@Deprecated
	protected Element			objElement				= null;

	protected ISchedulerUpdate	update					= null;

	public final static int		NONE					= -1;
	protected String			strUpdateElementName	= "";
	protected String			strUpdateObjectType		= "";

	public JOEListener() {
		//
	}

	public void setISchedulerUpdate(final ISchedulerUpdate update_) {
		update = update_;
	}

	public int getLanguage() {
		return NONE;
	}

	public Color getColor4InvalidValues() {
		Color objC = Display.getCurrent().getSystemColor(SWT.COLOR_YELLOW);
		return objC;
	}

	public Color getColor4MandatoryValue() {
		Color objC = Options.getRequiredColor();
		return objC;
	}

	public Color getColor4Background() {
		Color objC = Display.getCurrent().getSystemColor(SWT.COLOR_WHITE);
		return objC;
	}

	public Color getColor4DisabledElements() {
		Color objC = Display.getCurrent().getSystemColor(SWT.COLOR_GRAY);
		return objC;
	}

	public Color getColor4NodeParameter() {
		Color objC = Options.getLightBlueColor();
		return objC;
	}

	public Color getColor4hasParameter() {
		Color objC = Display.getCurrent().getSystemColor(SWT.COLOR_GREEN);
		return objC;
	}

	public String getLanguageAsString() {
		return "";
	}

	/**
		public String getLanguage(final int language) {
			return "";
		}
	**/
	public String getComment() {
		return "";
	}

	public String getDescription() {
		return "";
	}

	public String getSource() {
		return "";
	}

	public String getPrePostProcessingScriptSource() {
		String strT = "";
		return strT;
	}

	public void setSource(final String pstrS) {

	}

	public void setComment(final String pstrS) {

	}

	public void setDescription(final String pstrD) {

	}

	public void setLanguage(final String pstrLanguage) {
	}

	public String getJobName() {
		return "???";
	}

	public boolean isDisabled() {
		return false;
	}

	public SchedulerDom get_dom() {
		return _dom;
	}

	public String getFileName () {
		return get_dom().getFilename();
	}
	public boolean isLiveFolderElement () {
		return get_dom().isLifeElement() || get_dom().isDirectory();
	}

	public Image getImage(final String pstrImageFileName) {
		return ResourceManager.getImageFromResource("/sos/scheduler/editor/" + pstrImageFileName);
	}

	// "http://www.sos-berlin.com/doc/en/scheduler.doc/xml/job.xml"

	public void openXMLDoc(final String pstrTagName) {

		String lang = Options.getLanguage();
		String strHelpUrl = "http://www.sos-berlin.com/doc/" + lang + "/scheduler.doc/xml/" + pstrTagName + ".xml";
		openHelp(strHelpUrl);
	}

	// http://www.sos-berlin.com/doc/en/scheduler.doc/xml/job.xml#attribute_stop_on_error

	public void openXMLAttributeDoc(final String pstrTagName, final String pstrAttributeName) {

		String lang = Options.getLanguage();
		String strHelpUrl = "http://www.sos-berlin.com/doc/" + lang + "/scheduler.doc/xml/" + pstrTagName + ".xml#attribute_" + pstrAttributeName;
		openHelp(strHelpUrl);

	}

	public boolean Check4HelpKey(final int pintKeyCode, final String pstrTagName, final String pstrAttribute) {
		if (isHelpKey(pintKeyCode)) {
			openXMLAttributeDoc(pstrTagName, pstrAttribute);
			return true;
		}
		if (isGlobalHelpKey(pintKeyCode)) {
			openXMLDoc(pstrTagName);
			return true;
		}

		return false;

	}

	public boolean isHelpKey(final int pintKeyCode) {
		boolean flgRet = pintKeyCode == SWT.F1;
		return flgRet;
	}

	public boolean isGlobalHelpKey(final int pintKeyCode) {
		boolean flgRet = pintKeyCode == SWT.F10;
		return flgRet;
	}

	@SuppressWarnings("deprecation")
	public void openHelp(final String helpKey) {
		String lang = Options.getLanguage();
		String url = helpKey;
		try {
			// TODO: �berpr�fen, ob Datei wirklich existiert
			if (url.contains("http:")) {
			}
			else {
				url = new File(url).toURL().toString();
			}
			Program prog = Program.findProgram("html");
			if (prog != null)
				prog.execute(url);
			else {
				Runtime.getRuntime().exec(Options.getBrowserExec(url, lang));
			}
		}
		catch (Exception e) {
			try {
				new ErrorLog("error in " + SOSClassUtil.getMethodName() + "; "
						+ sos.scheduler.editor.app.Messages.getString("MainListener.cannot_open_help", new String[] { url, lang, e.getMessage() }), e);
			}
			catch (Exception ee) {
				// tu nichts
			}
			e.printStackTrace();
			MainWindow.message(sos.scheduler.editor.app.Messages.getString("MainListener.cannot_open_help", new String[] { url, lang, e.getMessage() }),
					SWT.ICON_ERROR | SWT.OK);
		}
	}

	public String getXML() {


		String strXmlText = "no xml source found. TreeData = null";

		Object objO = objTreeData.getObject();
		if (objO instanceof JSObjJob) {
			JSObjJob objJ = (JSObjJob) objO;
			strXmlText = objJ.marshal();
		}
		else {
			if (objO instanceof JSObjJobChain) {
				JSObjJobChain objJ = (JSObjJobChain) objO;
				strXmlText = objJ.marshal();
			}
		}

//		if (objElement != null) {
//			strXmlText = getXML(objElement);
//
//			if (strXmlText != null) {
//
//				// newXML ist null, wenn �nderungen nicht �bernommen werden sollen
//				// if (newXML != null)
//				// applyXMLChange(newXML);
//
//			}
//		}
		return strXmlText;
	}

	private String getXML(final Element element) {

		String xml = "";
		if (element != null) {
			try {
				if (_dom instanceof SchedulerDom && _dom.isDirectory()) {

					xml = _dom.getXML(Utils.getHotFolderParentElement(element));
				}
				else {
					xml = _dom.getXML(element);
				}

			}
			catch (JDOMException ex) {
				try {
					new ErrorLog("error in " + SOSClassUtil.getMethodName(), ex);
				}
				catch (Exception ee) {
					// tu nichts
				}
				// message("Error: " + ex.getMessage(), SWT.ICON_ERROR | SWT.OK);
				return null;
			}

		}
		return xml;
	}

	protected void setDirty() {
		_dom.setChanged(true);
		if (_dom.isDirectory() || _dom.isLifeElement()) {
			_dom.setChangedForDirectory(strUpdateObjectType, strUpdateElementName, SchedulerDom.MODIFY);
		}
	}

	protected String getBoolYesNo(final boolean pflgValue) {
		String strR = "no";
		if (pflgValue == true) {
			strR = "yes";
		}
		return strR;
	}
	public boolean getYesOrNo(final String yesOrNo) {
		if (yesOrNo == null) {
			return false;
		}
		String work = yesOrNo.toLowerCase();
		return work.equals("1") || work.equals("yes") || work.equals("true") || work.equals("ja");
	}

	public String setYesOrNo(final boolean pflgV) {
		if (pflgV == true) {
			return "yes";
		}
		else {
			return "no";
		}
	}

	public void setTreeData (final TreeData pobjTreeData) {
		objTreeData = pobjTreeData;
	}
	public static SchedulerObjectFactory	JobSchedulerObjectFactory	= null;
	public JSObjJobChain					objJSJobChain				= null;
	protected  TreeData			objTreeData							= null;

	public static ISOSVFSHandler			objVFS						= null;
	public static ISOSVfsFileTransfer		objFileSystemHandler		= null;
	public final SOSFTPOptions				objOptions					= null;

	protected SchedulerObjectFactory getJobSchedulerObjectFactory() {
		if (JobSchedulerObjectFactory == null) {
			//			JobSchedulerObjectFactory = new SchedulerObjectFactory("localhost", 4444);
			JobSchedulerObjectFactory = new SchedulerObjectFactory();
			JobSchedulerObjectFactory.initMarshaller(Spooler.class);
			try {
				// TODO das kommt von aussen aus dem Workspace
				objVFS = VFSFactory.getHandler("local");
				objFileSystemHandler = (ISOSVfsFileTransfer) objVFS;
			}
			catch (Exception e) {
				new ErrorLog(e.getLocalizedMessage(), e);
			}
		}
		return JobSchedulerObjectFactory;
	}

}
