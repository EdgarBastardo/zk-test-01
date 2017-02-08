package org.test.zk.controllers.person.editor;

import java.io.File;
import java.time.LocalDate;
import java.util.LinkedList;

import org.zkoss.zhtml.Label;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Execution;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Selectbox;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import commonlibs.commonclasses.CLanguage;
import commonlibs.commonclasses.ConstantsCommonClasses;
import commonlibs.extendedlogger.CExtendedConfigLogger;
import commonlibs.extendedlogger.CExtendedLogger;
import commonlibs.utils.Utilities;

import org.test.zk.constant.SystemConstants;
import org.test.zk.controllers.person.manager.CManagerController;
import org.test.zk.database.CDatabaseConexion;
import org.test.zk.database.dao.PersonDAO;
import org.test.zk.database.datamodel.TBLOperator;
import org.test.zk.database.datamodel.TBLPerson;
import org.test.zk.utilities.SystemUtilities;

public class CEditorController extends SelectorComposer<Component> {

	private static final long serialVersionUID = 5644171313216021048L;
	
	protected CManagerController mana = new CManagerController();
    
    protected ListModelList<String> dataModelSexo=new ListModelList<String>();

    protected Component callerComponent = null; //Variable de clase de tipo protegida
    
    protected TBLPerson personM; //Persona a modificar
    protected TBLPerson personA; //Persona a agregar
    
    
   // protected Component componentModify;
   // protected Component componentAdd;
    
    protected CDatabaseConexion DatabaseConnection=null;  
  
    protected CExtendedLogger controllerLogger = null;

    protected CLanguage controllerLanguage = null;
    
    @Wire Window windowsPerson;
	@Wire Label labeld;
	@Wire Textbox textboxId;
	@Wire Label labelFirstName;
	@Wire Textbox textboxFirstName;
	@Wire Label labelLastName;
	@Wire Textbox textboxLastName;
	@Wire Label labelBirthDate;
	@Wire Datebox dateboxBirthDate;
	@Wire Selectbox selectboxGender;
	@Wire Label labelGender;
	@Wire Listbox listboxGender;
	@Wire Label labelComment;
	@Wire Textbox textboxComment;
	
	
	
	public void initcontrollerLoggerAndcontrollerLanguage( String strRunningPath, Session currentSession ){
		
		//Leemos la configuración del logger del archivo o de la sesión
		CExtendedConfigLogger extendedConfigLogger = SystemUtilities.initLoggerConfig( strRunningPath,currentSession );
		
		//Obtenemos las credenciales del operador las cuales debieron ser guardadas por el CLoginController.java
		TBLOperator operatorCredential = (TBLOperator) currentSession.getAttribute(SystemConstants._Operator_Credential_Session_Key);
		
		//Inicializamos los valores de las variables
		//Esto es un valor por defecto no debería quedar con el pero si lo hacer el algoritmo no falla
		String strOperator = SystemConstants._Operator_Unknown; 
		
        //Recuperamos información de fecha y hora del inicio de sesión Login
		String strLoginDateTime = (String) currentSession.getAttribute( SystemConstants._Login_Date_Time_Session_Key ); 
		
        //Recuperamos el path donde se guardarn los log ya que cambia según el nombre de l operador que inicie sesion
		String strLogPath = (String) currentSession.getAttribute( SystemConstants._Log_Path_Session_Key ); 
		
        if (operatorCredential != null){
        	strOperator = operatorCredential.getName();//Obtenemos el nombre del operador que hizo login

        }

        if (strLoginDateTime == null) {//En caso de ser null no ha fecha y hora de inicio de sesión colocarle una por defecto
        	strLoginDateTime=Utilities.getDateInFormat( ConstantsCommonClasses._Global_Date_Time_Format_File_System_24, null );
        }

        final String strLoggerName = SystemConstants._Person_Editor_Controller_Logger_Name;
        final String strLoggerFileName = SystemConstants._Person_Editor_Controller_File_Log;
        
        //Aqui creamos el logger para el operador que inicio sesión login en el sistema
        controllerLogger = CExtendedLogger.getLogger( strLoggerName + " " + strOperator + " " + strLoginDateTime );
        
        //Esto se ejecuta si es la primera vez que esta creando el logger recuerden lo que pasa 
        //Cuando el usuario hace recargar en el navegador todo el .zul se vuelve a crear de cero, 
        //pero el logger persiste de manera similar a como lo hacen las variables de session
        if (controllerLogger.getSetupSet() == false) {
        	
        	//Aquí vemos si es null esa varible del logpath intentamos poner una por defecto
        	if (strLogPath == null) 
        		strLogPath = strRunningPath+File.separator+SystemConstants._Logs_Dir;
        	
        	//Si hay una configuración leida de session o del archivo la aplicamos
        	if (extendedConfigLogger != null)
        		controllerLogger.setupLogger( strOperator + " " + strLoginDateTime, false, strLogPath, strLoggerFileName, extendedConfigLogger.getClassNameMethodName(), extendedConfigLogger.getExactMatch(), extendedConfigLogger.getLevel(), extendedConfigLogger.getLogIP(), extendedConfigLogger.getLogPort(), extendedConfigLogger.getHTTPLogURL(), extendedConfigLogger.getHTTPLogUser(), extendedConfigLogger.getHTTPLogPassword(), extendedConfigLogger.getProxyIP(), extendedConfigLogger.getProxyPort(), extendedConfigLogger.getProxyUser(), extendedConfigLogger.getProxyPassword() );
        	else  //Si no usamos la por defecto
        		controllerLogger.setupLogger( strOperator + " " + strLoginDateTime, false, strLogPath, strLoggerFileName, SystemConstants._Log_Class_Method, SystemConstants._Log_Exact_Match, SystemConstants._Log_Level, "", -1, "", "", "", "", -1, "", "" );
        	
        	//Inicializamos el lenguage para ser usado por el logger
        	controllerLanguage = CLanguage.getLanguage( controllerLogger, strRunningPath + SystemConstants._Langs_Dir + strLoggerName + "." + SystemConstants._Lang_Ext ); 
        	
        	//Protección para el multi hebrado, puede que dos usuarios accedan exactamente al mismo tiempo a la página web, este código en el servidor se ejecuta en dos hebras
        	synchronized( currentSession ) { //Aquí entra un asunto de hebras y acceso multiple de varias hebras a la misma variable
                
                //Guardamos en la sesión los logger que se van creando para luego ser destruidos.
                @SuppressWarnings("unchecked")
                LinkedList<String> loggedSessionLoggers = (LinkedList<String>) currentSession.getAttribute( SystemConstants._Logged_Session_Loggers );

                if ( loggedSessionLoggers != null ) {

                    //sessionLoggers = new LinkedList<String>();

                    //El mismo problema de la otra variable
                    synchronized( loggedSessionLoggers ) {

                        //Lo agregamos a la lista
                        loggedSessionLoggers.add( strLoggerName + " " + strOperator + " " + strLoginDateTime );

                    }

                    //Lo retornamos la sesión de este operador
                    currentSession.setAttribute( SystemConstants._Logged_Session_Loggers, loggedSessionLoggers );

                }
        	}
        }
        
	}
	
	
	
	@Override	
	public void doAfterCompose(Component comp) {
		try {
			super.doAfterCompose(comp);
			dateboxBirthDate.setFormat("dd-MM-yyyy");

			dataModelSexo.add("Femenino");  //agrega los sexos
			dataModelSexo.add("Masculino");
			selectboxGender.setModel(dataModelSexo); //carga el selectbox
			selectboxGender.setSelectedIndex(0);
			dataModelSexo.addToSelection("Femenino"); //selecciona femenino

			final Execution execution = Executions.getCurrent();  //consultar que hace esto
			Session currentSession = Sessions.getCurrent();

			//Obtenemos el logger del objeto webApp y guardamoes una referencia en a variable de clase controllerLogger 
//    		controllerLogger = (CExtendedLogger) Sessions.getCurrent().getWebApp().getAttribute(ConstantsCommonClasses._Webapp_Logger_App_Attribute_Key);

			final String strRunningPath = Sessions.getCurrent().getWebApp().getRealPath(SystemConstants._WEB_INF_Dir) + File.separator ;
			//Inicializamos el Logger y el Language
			initcontrollerLoggerAndcontrollerLanguage(strRunningPath, Sessions.getCurrent());			
    		
    		
    		
    		if (currentSession.getAttribute(SystemConstants._DB_Connection_Session_Key) instanceof CDatabaseConexion) {
				DatabaseConnection= (CDatabaseConexion) currentSession.getAttribute(SystemConstants._DB_Connection_Session_Key);


				// person (persona a modificar) debe venir de la db y no de la lista pasada como argumento
				if ( execution.getArg().get( "idPerson" ) instanceof String) {
					
					//cargamos la data de la base de datos
					personM = PersonDAO.loadData(DatabaseConnection, (String) execution.getArg().get( "idPerson" ), controllerLogger, controllerLanguage);
				}
			}

			//person = ( TBLPerson ) execution.getArg().get( "persontomodify" );
			//componentModify = ( Component ) execution.getArg().get( "idModify" );
			//componentAdd = ( Component ) execution.getArg().get( "onAgregar" );

			if(personM!=null){
				textboxId.setValue( personM.getId() );
				textboxFirstName.setValue( personM.getFirstName() );
				textboxLastName.setValue( personM.getLastName() );
				if ( personM.getGender() == 0 ) {
					dataModelSexo.addToSelection( "Femenino" );
				}
				else {
					dataModelSexo.addToSelection( "Masculino" );
				}
				dateboxBirthDate.setValue( java.sql.Date.valueOf( personM.getBirthDate() ) );
				textboxComment.setValue( personM.getComment() );
			}
			 callerComponent = (Component) execution.getArg().get( "callerComponent" ); //Usamos un  typecast a Component que es el padre de todos los elementos visuales de zk
			
		} catch (Exception ex) {
			if ( controllerLogger != null )   
				controllerLogger.logException( "-1021", ex.getMessage(), ex );        
		}
	}

	@Listen ( "onClick=#buttonAccept")
	public void onClickbuttonaccept (Event event) {
		/* Messagebox.show("Id: " + textboxid.getValue() + " Nombre: "+ textboxFirstName.getValue()+
		 "Apellido: "+ textboxlastName.getValue() + " Fecha Nacimiento: " + dateboxbirthdate.getValue()
		, "Aceptar", Messagebox.OK, Messagebox.INFORMATION);
*/
		//System.out.println("hola aceptar");

/**		if (textboxId.getValue() == "") {
			Messagebox.show("debe tener un ID", "Error", Messagebox.OK, Messagebox.EXCLAMATION);
		} else 
			if (textboxFirstName.getValue() == "") {
				Messagebox.show("debe tener un Nombre", "Error", Messagebox.OK, Messagebox.EXCLAMATION);
			} else 
				if (textboxLastName.getValue() == "") {
					Messagebox.show("debe tener un Apellido", "Error", Messagebox.OK, Messagebox.EXCLAMATION);
				} else 
                  if (dateboxBirthDate.getValue()==null){
		             Messagebox.show("debe indicar una fecha válida", "Error", Messagebox.OK, Messagebox.EXCLAMATION);
		        }
		else
		{
	     
**/		
//		person = new TBLPerson(textboxId.getValue(),textboxFirstName.getValue(),textboxLastName.getValue(),selectboxGender.getSelectedIndex(),fechanac,textboxComment.getValue());
		
		//windowsPerson.detach();
		LocalDate fechanac = new java.sql.Date(dateboxBirthDate.getValue().getTime()).toLocalDate();
 
	if (personM != null) {
	
//* 		 person = new TBLPerson();
	     personM.setId(textboxId.getValue());
	     personM.setFirstName(textboxFirstName.getValue());
	     personM.setLastName(textboxLastName.getValue());
	     personM.setGender(selectboxGender.getSelectedIndex());
	     personM.setBirthDate(fechanac);
	     personM.setComment(textboxComment.getValue());
	     
	     // ******************* el en video usan dos variables persontoadd y persontomodify, yo uso una sola, este codigo es menos entendible
	     
     	PersonDAO.updateData(DatabaseConnection, personM, controllerLogger, controllerLanguage); 
        Events.echoEvent( new Event( "onCambiar", callerComponent, personM ) );    

        }
        else{
        	 personA = new TBLPerson();
    	     personA.setId(textboxId.getValue());
    	     personA.setFirstName(textboxFirstName.getValue());
    	     personA.setLastName(textboxLastName.getValue());
    	     personA.setGender(selectboxGender.getSelectedIndex());
    	     personA.setBirthDate(fechanac);
    	     personA.setComment(textboxComment.getValue());
         	PersonDAO.insertData(DatabaseConnection, personA, controllerLogger, controllerLanguage);                    //actualizamos en la db 
            Events.echoEvent( new Event( "onCambiar", callerComponent, personA ) );
        }

		windowsPerson.detach();
		}
//Events.echoEvent("onClickbuttonModify",null, _subsInfo);
		

	
	
	
@Listen ( "onClick=#buttonCancel")
public void onClickbuttoncancel (Event event) {
	 //Messagebox.show("Cancelar", "Cancel", Messagebox.OK, Messagebox.EXCLAMATION);
		//System.out.println("hola cancelar");
		windowsPerson.detach();
	}
}
