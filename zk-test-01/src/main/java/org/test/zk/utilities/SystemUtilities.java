package org.test.zk.utilities;

import org.test.zk.constant.SystemConstants;
import org.zkoss.zk.ui.Session;

import commonlibs.extendedlogger.CExtendedConfigLogger;
import commonlibs.extendedlogger.CExtendedLogger;

public class SystemUtilities {
	
	public static CExtendedConfigLogger initLoggerConfig( String strRunningPath, Session currentSession ) {
		
		//tratamos de cargar la configuracion de los logger de la sesi�n
		CExtendedConfigLogger result = (CExtendedConfigLogger) currentSession.getAttribute(SystemConstants._Config_Logger_Session_Key);
		
		if (result == null) {
			
			//En caso de no encontrarse en la sesi�n lo leemos del archivo de configuraci�n
			result = new CExtendedConfigLogger();
			
			String strConfigPath = strRunningPath + SystemConstants._CONFIG_Dir;
			
			//Localizamos el logger del webApp el cual para este c�digo ya debe estar presente se inicia en CZKSubsystemEvents.
			CExtendedLogger webAppLogger = CExtendedLogger.getLogger(SystemConstants._Webapp_Logger_Name);
			
			//leemos la configuracion del archivo
			if (result.loadConfig(strConfigPath + SystemConstants._Logger_Config_File_Name, webAppLogger, null)==false) {
				
				result = null; //en caso de falla no devolvemos nada
			}
			
		} 
		
		return result;
		
	}

}
