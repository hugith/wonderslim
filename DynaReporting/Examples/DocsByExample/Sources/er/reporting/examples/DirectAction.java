// Generated by the WebObjects Wizard
package er.reporting.examples;

import er.reporting.*;
import er.grouping.*;

import com.webobjects.foundation.*;
import com.webobjects.appserver.*;
import com.webobjects.eocontrol.*;

public class DirectAction extends WODirectAction {

    public DirectAction(WORequest aRequest) {
        super(aRequest);
    }

    public WOActionResults defaultAction() {
        return pageWithName("Main") /* JC_WARNING - Please check: since WO4.5, using pageWithName on the application or session instance requires two parameters (the name of the page and a WOContext). If this pageWithName is in a component or direct action instance, it is valid as is. */;
    }

}