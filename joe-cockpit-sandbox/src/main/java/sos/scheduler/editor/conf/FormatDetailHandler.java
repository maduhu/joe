package sos.scheduler.editor.conf;

import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.sos.joe.globals.options.Options;
import com.sos.joe.xml.jobscheduler.DetailDom;


public class FormatDetailHandler extends DefaultHandler implements ContentHandler {
	
	
    private final    StringBuffer    _sb          = new StringBuffer();
    private    String          _encoding    = "ISO-8859-1";
    private final    String          _indentStr   = "    ";
    private    String          _indent      = "    ";
    private    StringBuffer    _text        = new StringBuffer();
    private    int             _level       = 0;
    private    boolean         _isOpen      = false;

    public FormatDetailHandler(DetailDom dom) {
        //_dom = dom;
    }


    public void setEnconding(String encoding) {
        _encoding = encoding;
    }

    public String getXML() {
        return _sb.toString();
    }


    @Override public void startDocument() {
        _sb.append("<?xml version=\"1.0\" encoding=\"" + _encoding + "\"?>\n\n");
        if(Options.getDetailXSLT() != null && Options.getDetailXSLT().length() > 0) {
        	_sb.append("<?xml-stylesheet type=\"text/xsl\" href=\""+ Options.getDetailXSLT() + "\"?> ");
		}
    }


    @Override public void characters(char[] ch, int start, int length) throws SAXException {
    	//System.out.println("ch:" + new String(ch, start, length));
        _text.append(new String(ch, start, length));
    }


    @Override public void endElement(String namespaceURI, String localName, String qName) throws SAXException {
    	//System.out.println("endelement:" + qName);
        _level--;
        _indent = strRepeat(_indentStr, _level);

        String text = _text.toString().trim();
        _text = new StringBuffer();
                
    
        boolean hasText = text.length() > 0;

        if (_isOpen && !hasText)
            _sb.append("/>\n");
        else if (_isOpen)
            _sb.append(">\n");

        /*if (hasText) {
            _sb.append(text + "\n");
        }*/
        if (hasText) {
            _sb.append(_indent + _indentStr + "<![CDATA[\n");
            _sb.append(text + "\n");
            _sb.append(_indent + _indentStr + "]]>\n");
        }

        if (!_isOpen || hasText)
            _sb.append(_indent + "</" + qName + ">\n");

        _isOpen = false;
    }


    @Override public void startElement(String namespaceURI, String localName, String qName, Attributes atts) throws SAXException {
    	//System.out.println("startelement:" + qName);
        if (_isOpen)
            _sb.append(">\n");        
        else
            _sb.append("\n");

        _indent = strRepeat(_indentStr, _level);

        StringBuffer attributes = new StringBuffer();
        String sep = " ";
        String sepStr = "\n" + _indent + strRepeat(" ", new String("<" + qName).length() + 1);

        //String attrName = "";

        // iterate atributes
        for (int i = 0; i < atts.getLength(); i++) {
            String name = atts.getQName(i);
            String value = atts.getValue(i);
            String uri = atts.getURI(i);

            if (name.equals("__comment__")) { // build element comment
                _sb.append(_indent + "<!-- " + value + " -->\n");
            } else { // add attribute
                if (!uri.equals("")) {
                    attributes.append(sep);
                    attributes.append("xmlns:xsi" + "=\"" + uri + "\"");
                }
                attributes.append(sep);
                attributes.append(name + "=\"" + value + "\"");
                sep = sepStr;

                //if (name.equals("name"))
                 //   attrName = value;
            }
        }
       
        String text = _text.toString().trim();
        if(text != null && text.length() > 0) {        	
        	_sb.append(text + " \n");
        	_text = new StringBuffer();
        }
        String ns = "";
        if( namespaceURI != null && namespaceURI.length() > 0) {
        	ns = " xmlns=\"" + namespaceURI + "\"";        	
        }
        
        _sb.append(_indent + "<" + qName +  ns + attributes.toString());
        _isOpen = true;
        _level++;

    }


    private String strRepeat(String str, int cnt) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < cnt; i++)
            sb.append(str);
        return sb.toString();
    }
}
