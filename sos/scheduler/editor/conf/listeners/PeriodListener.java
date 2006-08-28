package sos.scheduler.editor.conf.listeners;

import org.eclipse.swt.widgets.Button;
import org.jdom.Element;

import sos.scheduler.editor.app.Utils;
import sos.scheduler.editor.conf.SchedulerDom;

public class PeriodListener {

    private SchedulerDom _dom;

    private Element      _period;


    public PeriodListener(SchedulerDom dom) {
        _dom = dom;
    }


    public void setPeriod(Element period) {
        _period = period;
    }


    public Element getPeriod() {
        return _period;
    }


    public String getBegin() {
        return Utils.getTime(getBeginHours(), getBeginMinutes(), getBeginSeconds(), false);
    }


    public String getEnd() {
        return Utils.getTime(getEndHours(), getEndMinutes(), getEndSeconds(), false);
    }


    public String getRepeat() {
        return Utils.getTime(getRepeatHours(), getRepeatMinutes(), getRepeatSeconds(), true);
    }


    public String getSingle() {
        return Utils.getTime(getSingleHours(), getSingleMinutes(), getSingleSeconds(), false);
    }


    public String getBeginHours() {
        return Utils.getIntegerAsString(Utils.getHours(_period.getAttributeValue("begin"), -999));
    }


    public void setPeriodTime(int maxHour, Button bApply, String node, String hours, String minutes, String seconds) {
        if (_period != null) {

            Utils.setAttribute(node, Utils.getTime(maxHour, hours, minutes, seconds, false), _period, _dom);
            if (bApply != null) {
                bApply.setEnabled(true);
            }
        }
    }


    public void setRepeatSeconds(Button bApply, String seconds) {
        Utils.setAttribute("repeat", seconds, _period, _dom);
        if (bApply != null) {
            bApply.setEnabled(true);
        }
    }


    public String getBeginMinutes() {
        return Utils.getIntegerAsString(Utils.getMinutes(_period.getAttributeValue("begin"), -999));
    }


    public String getBeginSeconds() {
        return Utils.getIntegerAsString(Utils.getSeconds(_period.getAttributeValue("begin"), -999));
    }


    public String getEndHours() {
        return Utils.getIntegerAsString(Utils.getHours(_period.getAttributeValue("end"), -999));
    }


    public String getEndMinutes() {
        return Utils.getIntegerAsString(Utils.getMinutes(_period.getAttributeValue("end"), -999));
    }


    public String getEndSeconds() {
        return Utils.getIntegerAsString(Utils.getSeconds(_period.getAttributeValue("end"), -999));
    }


    public String getRepeatHours() {
        return Utils.getIntegerAsString(Utils.getHours(_period.getAttributeValue("repeat"), -999));
    }


    public String getRepeatMinutes() {
        return Utils.getIntegerAsString(Utils.getMinutes(_period.getAttributeValue("repeat"), -999));
    }


    public String getRepeatSeconds() {
        return Utils.getIntegerAsString(Utils.getSeconds(_period.getAttributeValue("repeat"), -999));
    }


    public String getSingleHours() {
        return Utils.getIntegerAsString(Utils.getHours(_period.getAttributeValue("single_start"), -999));
    }


    public String getSingleMinutes() {
        return Utils.getIntegerAsString(Utils.getMinutes(_period.getAttributeValue("single_start"), -999));
    }


    public String getSingleSeconds() {
        return Utils.getIntegerAsString(Utils.getSeconds(_period.getAttributeValue("single_start"), -999));
    }


    public boolean getLetRun() {
        String letrun = _period.getAttributeValue("let_run");
        return letrun == null ? false : letrun.equalsIgnoreCase("yes");
    }


    public void setLetRun(boolean letrun) {
        _period.setAttribute("let_run", letrun ? "yes" : "no");
        _dom.setChanged(true);
    }


    public boolean getRunOnce() {
        return Utils.isAttributeValue("once", _period);
    }


    public void setRunOnce(boolean once) {
        Utils.setAttribute("once", once, false, _period);
        _dom.setChanged(true);
    }

}