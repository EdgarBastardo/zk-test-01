package org.test.zk.controllers.login;

import org.test.zk.constant.SystemConstants;
import org.test.zk.database.CDatabaseConexion;
import org.test.zk.database.dao.OperatorDAO;
import org.test.zk.database.datamodel.TBLOperator;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Button;
import org.zkoss.zul.Label;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import commonlibs.commonclasses.CLanguage;
import commonlibs.extendedlogger.CExtendedLogger;
import commonlibs.utils.ZKUtilities;

public class CChangePassword extends SelectorComposer<Component> {
	
	private static final long serialVersionUID = -5430872287119950885L;
	
    protected CExtendedLogger controllerLogger = null;

    protected CLanguage controllerLanguage = null;

    @Wire Textbox textboxOldPassword;
	    
	@Wire Textbox textboxNewPassword;
	   
	@Wire Textbox textboxConfirmPassword;
	
    @Wire Button buttonCancel;

    @Wire Window windowPassword; //la ventana del changepassword

    @Wire Label labelOperator;

    @Listen( "onClick=#buttonCancel" )
public void onClickbuttonCancel( Event evento) {
	windowPassword.detach();
 }	
	    	
@Listen ( "onClick=#buttonChangePassword; onOK=#windowPassword")
public void onClickbuttonChangePassword (Event event) {
	try {

		//Aquí vamos a conectarnos a la bd y vamos a verificar si los valores introducidos son válidos	

		final String strOldPassword = ZKUtilities.getTextBoxValue(textboxOldPassword, controllerLogger);
		final String strNewPassword = ZKUtilities.getTextBoxValue(textboxNewPassword, controllerLogger);
		final String strConfirmPassword = ZKUtilities.getTextBoxValue(textboxConfirmPassword, controllerLogger);
		

		if (strOldPassword.isEmpty() == false && strNewPassword.isEmpty() == false && strConfirmPassword.isEmpty() == false ) {


			CDatabaseConexion databaseConnection = (CDatabaseConexion) Sessions.getCurrent().getAttribute(SystemConstants._DB_Connection_Session_Key);

			TBLOperator tblOperator = (TBLOperator) Sessions.getCurrent().getAttribute( SystemConstants._Operator_Credential_Session_Key ); 
			//labelOperator.setValue("Operator "+ tblOperator.getName());
			
			if (strNewPassword.equals(strConfirmPassword)) {
				OperatorDAO.changePassword(databaseConnection, tblOperator, strNewPassword, strOldPassword, controllerLogger, controllerLanguage);
				windowPassword.detach();
			}
			else
			{ Messagebox.show("Password No coinciden");

			}
		}
		else{
				Messagebox.show("Debe llenar los tres campos");
			}
    }
	catch (Exception ex) {
		ex.printStackTrace();
	}
}	
}

