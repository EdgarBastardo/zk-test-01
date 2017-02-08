package org.test.zk.controllers.login;

import java.io.File;
import java.util.LinkedList;

import org.test.zk.constant.SystemConstants;
import org.test.zk.database.CDatabaseConexion;
import org.test.zk.database.dao.OperatorDAO;
import org.test.zk.database.datamodel.TBLOperator;
import org.test.zk.utilities.SystemUtilities;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Session;
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
import commonlibs.commonclasses.ConstantsCommonClasses;
import commonlibs.extendedlogger.CExtendedConfigLogger;
import commonlibs.extendedlogger.CExtendedLogger;
import commonlibs.utils.Utilities;
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
    
    @Override	
    
  	public void doAfterCompose(Component comp) {
  		try {
  			super.doAfterCompose(comp);
  			//Obtenemos el logger del objeto webApp y guardamoes una referencia en a variable de clase controllerLogger 
      		controllerLogger = (CExtendedLogger) Sessions.getCurrent().getWebApp().getAttribute(ConstantsCommonClasses._Webapp_Logger_App_Attribute_Key);

  		} catch (Exception ex) {
  			if ( controllerLogger != null )   
  				controllerLogger.logException( "-1021", ex.getMessage(), ex );        
  		}
  	}
    
	public void initcontrollerLoggerAndcontrollerLanguage( String strRunningPath, Session currentSession ){
		
		//Leemos la configuraci�n del logger del archivo o de la sesi�n
		CExtendedConfigLogger extendedConfigLogger = SystemUtilities.initLoggerConfig( strRunningPath,currentSession );
		
		//Obtenemos las credenciales del operador las cuales debieron ser guardadas por el CLoginController.java
		TBLOperator operatorCredential = (TBLOperator) currentSession.getAttribute(SystemConstants._Operator_Credential_Session_Key);
		
		//Inicializamos los valores de las variables
		//Esto es un valor por defecto no deber�a quedar con el pero si lo hacer el algoritmo no falla
		String strOperator = SystemConstants._Operator_Unknown; 
		
        //Recuperamos informaci�n de fecha y hora del inicio de sesi�n Login
		String strLoginDateTime = (String) currentSession.getAttribute( SystemConstants._Login_Date_Time_Session_Key ); 
		
        //Recuperamos el path donde se guardarn los log ya que cambia seg�n el nombre de l operador que inicie sesion
		String strLogPath = (String) currentSession.getAttribute( SystemConstants._Log_Path_Session_Key ); 
		
        if (operatorCredential != null){
        	strOperator = operatorCredential.getName();//Obtenemos el nombre del operador que hizo login

        }

        if (strLoginDateTime == null) {//En caso de ser null no ha fecha y hora de inicio de sesi�n colocarle una por defecto
        	strLoginDateTime=Utilities.getDateInFormat( ConstantsCommonClasses._Global_Date_Time_Format_File_System_24, null );
        }

        final String strLoggerName = SystemConstants._Change_Password_Controller_Logger_Name;
        final String strLoggerFileName = SystemConstants._Change_Password_Controller_File_Log;
        
        //Aqui creamos el logger para el operador que inicio sesi�n login en el sistema
        controllerLogger = CExtendedLogger.getLogger( strLoggerName + " " + strOperator + " " + strLoginDateTime );
        
        //Esto se ejecuta si es la primera vez que esta creando el logger recuerden lo que pasa 
        //Cuando el usuario hace recargar en el navegador todo el .zul se vuelve a crear de cero, 
        //pero el logger persiste de manera similar a como lo hacen las variables de session
        if (controllerLogger.getSetupSet() == false) {
        	
        	//Aqu� vemos si es null esa varible del logpath intentamos poner una por defecto
        	if (strLogPath == null) 
        		strLogPath = strRunningPath+File.separator+SystemConstants._Logs_Dir;
        	
        	//Si hay una configuraci�n leida de session o del archivo la aplicamos
        	if (extendedConfigLogger != null)
        		controllerLogger.setupLogger( strOperator + " " + strLoginDateTime, false, strLogPath, strLoggerFileName, extendedConfigLogger.getClassNameMethodName(), extendedConfigLogger.getExactMatch(), extendedConfigLogger.getLevel(), extendedConfigLogger.getLogIP(), extendedConfigLogger.getLogPort(), extendedConfigLogger.getHTTPLogURL(), extendedConfigLogger.getHTTPLogUser(), extendedConfigLogger.getHTTPLogPassword(), extendedConfigLogger.getProxyIP(), extendedConfigLogger.getProxyPort(), extendedConfigLogger.getProxyUser(), extendedConfigLogger.getProxyPassword() );
        	else  //Si no usamos la por defecto
        		controllerLogger.setupLogger( strOperator + " " + strLoginDateTime, false, strLogPath, strLoggerFileName, SystemConstants._Log_Class_Method, SystemConstants._Log_Exact_Match, SystemConstants._Log_Level, "", -1, "", "", "", "", -1, "", "" );
        	
        	//Inicializamos el lenguage para ser usado por el logger
        	controllerLanguage = CLanguage.getLanguage( controllerLogger, strRunningPath + SystemConstants._Langs_Dir + strLoggerName + "." + SystemConstants._Lang_Ext ); 
        	
        	//Protecci�n para el multi hebrado, puede que dos usuarios accedan exactamente al mismo tiempo a la p�gina web, este c�digo en el servidor se ejecuta en dos hebras
        	synchronized( currentSession ) { //Aqu� entra un asunto de hebras y acceso multiple de varias hebras a la misma variable
                
                //Guardamos en la sesi�n los logger que se van creando para luego ser destruidos.
                @SuppressWarnings("unchecked")
                LinkedList<String> loggedSessionLoggers = (LinkedList<String>) currentSession.getAttribute( SystemConstants._Logged_Session_Loggers );

                if ( loggedSessionLoggers != null ) {

                    //sessionLoggers = new LinkedList<String>();

                    //El mismo problema de la otra variable
                    synchronized( loggedSessionLoggers ) {

                        //Lo agregamos a la lista
                        loggedSessionLoggers.add( strLoggerName + " " + strOperator + " " + strLoginDateTime );

                    }

                    //Lo retornamos la sesi�n de este operador
                    currentSession.setAttribute( SystemConstants._Logged_Session_Loggers, loggedSessionLoggers );

                }
        	}
        }
        
	}
	
	
	
	
    
    @Listen( "onClick=#buttonCancel" )
public void onClickbuttonCancel( Event evento) {
	windowPassword.detach();
 }	
	    	
@Listen ( "onClick=#buttonChangePassword; onOK=#windowPassword")
public void onClickbuttonChangePassword (Event event) {
	try {

		//Aqu� vamos a conectarnos a la bd y vamos a verificar si los valores introducidos son v�lidos	

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

