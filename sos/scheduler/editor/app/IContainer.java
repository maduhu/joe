package sos.scheduler.editor.app;

import sos.scheduler.editor.conf.forms.SchedulerForm;
import sos.scheduler.editor.doc.forms.DocumentationForm;

public interface IContainer {
    public SchedulerForm newScheduler();


    public DocumentationForm newDocumentation();


    public SchedulerForm openScheduler();


    public DocumentationForm openDocumentation();


    public IEditor getCurrentEditor();


    public void setStatusInTitle();


    public void setNewFilename(String oldFilename);


    public boolean closeAll();


    public void updateLanguages();
}