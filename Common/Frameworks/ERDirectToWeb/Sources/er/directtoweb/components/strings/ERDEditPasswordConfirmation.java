package er.directtoweb.components.strings;

import org.apache.log4j.Logger;

import com.webobjects.appserver.WOContext;
import com.webobjects.appserver.WORequest;
import com.webobjects.eocontrol.EOEnterpriseObject;
import com.webobjects.foundation.NSDictionary;
import er.directtoweb.components.ERDCustomEditComponent;
import er.extensions.foundation.ERXMutableUserInfoHolderInterface;
import er.extensions.validation.ERXValidationFactory;

/**
 * Provides a "confirm password" service. Can
 * only be used in conjunction with {$link ERDEditPassword}, because
 * the original password value is grabbed from the <code>context.mutableUserInfo</code>.
 * As all the names in a displayPropertyRepetition must be distinct, you must provide a
 * dummy name for this component to show. The corresponding rules for a key named <code>passwordConfirmation</code> have already been set up.
 * @binding object the object to edit
 * @binding length the length of the text field
 * @d2wKey length the length of the text field
 *
 * @author ak on Sun Aug 17 2003
 * @project ERDirectToWeb
 */

public class ERDEditPasswordConfirmation extends ERDCustomEditComponent {

    /** logging support */
    private static final Logger log = Logger.getLogger(ERDEditPasswordConfirmation.class);

    public int length;
    /**
     * Public constructor
     * @param context the context
     */
    public ERDEditPasswordConfirmation(WOContext context) {
        super(context);
    }

    public void fail(String errorCode) {
        if(log.isDebugEnabled())
            log.debug("fail:<object:" + object() + "; key:" + key() + ";  password: " + password() + "; code:" + errorCode + ";>");
        validationFailedWithException(ERXValidationFactory.defaultFactory().createException(object(), key(), password(), errorCode), password(), key() + "," + passwordPropertyKey());
    }

    public String passwordPropertyKey() {
        String passwordPropertyKey = null;
        if(context() instanceof ERXMutableUserInfoHolderInterface) {
            NSDictionary userInfo = ((ERXMutableUserInfoHolderInterface)context()).mutableUserInfo();
            passwordPropertyKey = (String) userInfo.valueForKey(ERDEditPassword.passwordPropertyKey);
        }
        if(passwordPropertyKey == null) {
            throw new IllegalStateException("Can't find the passwordPropertyKey. There needs to be a ERDEditPassword component on this page and its 'passwordConfirmationValidates' needs to be true for this component to work.");
        }
        return passwordPropertyKey;
    }

    public Object objectKeyPathValue() {
        return passwordPropertyKey() == null || object() == null ? null : object().valueForKeyPath(passwordPropertyKey()); 
    }

    public String password() {
        String password = null;
        if(context() instanceof ERXMutableUserInfoHolderInterface) {
            NSDictionary userInfo = ((ERXMutableUserInfoHolderInterface)context()).mutableUserInfo();
            password = (String) userInfo.valueForKey("ERDEditPassword." + passwordPropertyKey() + ".value");
        }
        return password;
    }

    protected String _passwordConfirm;
    public String passwordConfirm() {
        /*if(_passwordConfirm == null) {
            _passwordConfirm = (String)valueForBinding("passwordConfirm");
        }*/
        return _passwordConfirm;
    }
    public void setPasswordConfirm(String value) {
        _passwordConfirm = value;
    }

    public void setObject(EOEnterpriseObject newObject) {
        if (newObject!=object()) {
            _passwordConfirm = null;
        }
        super.setObject(newObject);
    }

    protected void checkPasswords() {
        String password = password();
        String passwordPropertyKey = passwordPropertyKey();
        String passwordConfirm = passwordConfirm();

        if (password==null || password.equals("") || passwordConfirm==null || passwordConfirm.equals("")) {
            // one or both is null or empty
            if (objectKeyPathValue() == null) {
                fail("PasswordsFillBothFieldsException");
            } else {
                // if we already have a value, then they need to both be null||empty
                if (!(password==null || password.equals("")) && (passwordConfirm==null || passwordConfirm.equals(""))) {
                    fail("PasswordsFillBothFieldsException");
                }
            }
        } else {
            // they are both non-null
            if(!password.equals(passwordConfirm)) {
                fail("PasswordsDontMatchException");
            } else {
                object().validateTakeValueForKeyPath(password, passwordPropertyKey);
            }
        }
    }
    
    public void takeValuesFromRequest(WORequest r, WOContext c) {
        super.takeValuesFromRequest(r,c);
        checkPasswords();
    }
}