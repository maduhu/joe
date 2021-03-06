package com.sos.joe.jobdoc.editor.listeners;

import org.jdom.Element;

import com.sos.joe.xml.Utils;

import com.sos.joe.xml.jobdoc.DocumentationDom;

public class ReleaseListener extends JobDocBaseListener<DocumentationDom> {

    private Element _release;

    public ReleaseListener(DocumentationDom dom, Element release) {
        _dom = dom;
        _release = release;
    }

    public String getTitle() {
        return Utils.getElement("title", _release, _dom.getNamespace());
    }

    public void setTitle(String title) {
        Utils.setElement("title", title, false, _release, _dom.getNamespace(), _dom);
    }

    public String getID() {
        return Utils.getAttributeValue("id", _release);
    }

    public void setId(String id) {
        Utils.setAttribute("id", id, _release, _dom);
    }

    public String getCreated() {
        return Utils.getAttributeValue("created", _release);
    }

    public void setCreated(String created) {
        Utils.setAttribute("created", created, _release, _dom);
    }

    public String getModified() {
        return Utils.getAttributeValue("modified", _release);
    }

    public void setModified(String modified) {
        Utils.setAttribute("modified", modified, _release);
    }

    public Element getRelease() {
        return _release;
    }

    public String getNote(String which) {
        if (_release != null && !_release.getChildren(which, _release.getNamespace()).isEmpty()) {
            return _dom.noteAsStr(_release.getChild(which, _release.getNamespace()));
        } else {
            return "";
        }
    }

    public void applyRelease(String title, String id, String created, String modified) {
        Utils.setAttribute("id", id, _release);
        Utils.setAttribute("created", created, _release);
        Utils.setAttribute("modified", modified, _release);
        Utils.setElement("title", title, false, _release, _dom.getNamespace(), _dom);
        _dom.setChanged(true);
    }

}