package org.test.zk.database.dao;

import java.sql.ResultSet;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import org.test.zk.database.CDatabaseConexion;
import org.test.zk.database.datamodel.TBLOperator;
import org.zkoss.zul.Messagebox;

import commonlibs.commonclasses.CLanguage;
import commonlibs.extendedlogger.CExtendedLogger;
import commonlibs.utils.BCrypt;


public class OperatorDAO {
	
	public static TBLOperator loadData (final CDatabaseConexion databaseConexion, final String strId, CExtendedLogger localLogger, CLanguage localLanguage) {
		
		TBLOperator result = null;
	try {
			
			if (databaseConexion != null && databaseConexion.getDBConnection() != null) {
				Statement statement = databaseConexion.getDBConnection().createStatement();
				ResultSet resultSet = statement.executeQuery("Select * From tbloperator Where Id='" + strId + "'");
				if (resultSet.next()){
					
					result = new TBLOperator();
					
					
					result.setId(resultSet.getString("ID"));
					result.setName(resultSet.getString("Name"));
					result.setRole(resultSet.getString("NameRole"));
					result.setPassword(resultSet.getString("Password"));
					result.setComment(resultSet.getString("Comment"));
					
					// interface
					result.setDisabledBy (resultSet.getString("DisabledBy"));
					result.setDisabledAtDate (resultSet.getDate("DisabledAtDate").toLocalDate());
					result.setDisabledAtTime (resultSet.getTime("DisabledAtTime").toLocalTime());
					result.setLastLoginAtDate (resultSet.getDate("LastLoginAtDate").toLocalDate() != null ? resultSet.getDate("LastLoginAtDate").toLocalDate() : null);
					result.setLastLoginAtTime (resultSet.getTime("LastLoginAtTime").toLocalTime() != null ? resultSet.getTime("LastLoginAtTime").toLocalTime() : null);

					result.setCreatedBy (resultSet.getString("CreatedBy"));
					result.setCreatedAtDate (resultSet.getDate("CreatedAtDate").toLocalDate());
					result.setCreatedAtTime (resultSet.getTime("CreatedAtTime").toLocalTime());
					result.setUpdatedBy (resultSet.getString("UpdatedBy"));
					result.setUpdatedAtDate (resultSet.getDate("UpdatedAtDate").toLocalDate() != null ? resultSet.getDate("UpdatedAtDate").toLocalDate() : null);
					result.setUpdatedAtTime (resultSet.getTime("UpdatedAtTime").toLocalTime() != null ? resultSet.getTime("UpdatedAtTime").toLocalTime() : null);
					
				}
				// una vez termina hay que liberar recursos
				statement.close();
				resultSet.close();
			
				}	
			}
		catch (Exception ex) {
			if (databaseConexion != null && databaseConexion.getDBConnection() != null)
			{
				try {
					// i hay error hacer rollback a las operaciones anteriores
					databaseConexion.getDBConnection().rollback(); 
				} catch (Exception e) {
					
					if ( localLogger != null )   
						localLogger.logException( "-1021", e.getMessage(), e );        
				}
			}						
			if ( localLogger != null )   
				localLogger.logException( "-1022", ex.getMessage(), ex );        
		}
			
		
		
		return result;
	}
	

	public static boolean deleteData (final CDatabaseConexion databaseConexion, final String strId, CExtendedLogger localLogger, CLanguage localLanguage) {
		boolean bResult = false;
		try{
			if (databaseConexion != null && databaseConexion.getDBConnection() != null)
			{
				Statement statement = databaseConexion.getDBConnection().createStatement();
				final String strSQL = "delete from tbloperator where ID = '"+strId+"'"; 
				statement.executeUpdate(strSQL);
				databaseConexion.getDBConnection().commit(); // importante hacer commit sino no guarda la información en la base de datos
				statement.close(); //liberar recursos
				bResult = true;
		}
		}
		catch (Exception ex) {
			if (databaseConexion != null && databaseConexion.getDBConnection() != null)
			{
				try {
					// i hay error hacer rollback a las operaciones anteriores
					databaseConexion.getDBConnection().rollback(); 
				} catch (Exception e) {
					
					if ( localLogger != null )   
						localLogger.logException( "-1021", e.getMessage(), e );        
				}
			}						
			if ( localLogger != null )   
				localLogger.logException( "-1022", ex.getMessage(), ex );        
		} 
				
		return bResult;
	}
	
	
	public static boolean insertData (final CDatabaseConexion databaseConexion, final TBLOperator tbloperator, CExtendedLogger localLogger, CLanguage localLanguage) {
		
		boolean bResult = false;
		try{
			if (databaseConexion != null && databaseConexion.getDBConnection() != null)
			{
				Statement statement = databaseConexion.getDBConnection().createStatement();
				final String strSQL = "insert into tbloperator (Id, Name, Role, Password, Comment, CreatedBy, CreatedAtDate, CreatedAtTime, UpdatedBy, UpdatedAtDate, UpdatedAtTime, DisabledBy, DisabledAtDate, DisabledAtTime, LastLoginAtDate, LastLoginAtTime) values ('"+tbloperator.getId()+"','"+tbloperator.getName()+"','"+tbloperator.getRole()+"','"+tbloperator.getPassword()+"','"+tbloperator.getComment()+ "','test','"+ LocalDate.now().toString()+"','"+LocalTime.now().toString()+"',null,null,null,null,null)"; 
				statement.executeUpdate(strSQL);
				databaseConexion.getDBConnection().commit(); // importante hacer commit sino no guarda la información en la base de datos
				statement.close(); //liberar recursos
				bResult = true;
				
		}
			}
		
		catch (Exception ex) {
			if (databaseConexion != null && databaseConexion.getDBConnection() != null)
			{
				try {
					// i hay error hacer rollback a las operaciones anteriores
					databaseConexion.getDBConnection().rollback(); 
				} catch (Exception e) {
					
					if ( localLogger != null )   
						localLogger.logException( "-1021", e.getMessage(), e );        
				}
			}			
			if ( localLogger != null )   
				localLogger.logException( "-1022", ex.getMessage(), ex );        
		} 
		return bResult;	}
	
	public static boolean updateData (final CDatabaseConexion databaseConexion, final TBLOperator tbloperator, CExtendedLogger localLogger, CLanguage localLanguage) {
		
		boolean bResult = false;
		try{
			if (databaseConexion != null && databaseConexion.getDBConnection() != null)
			{
				Statement statement = databaseConexion.getDBConnection().createStatement();
				
				final String strDisabledAtDate = tbloperator.getDisabledBy() != null ? "'"+LocalDate.now().toString() +"'":"null";
				final String strDisabledAtTime = tbloperator.getDisabledBy() != null ? "'"+LocalTime.now().toString() +"'":"null";
				final String strSQL;
				if (strDisabledAtTime==("null")){
				    strSQL = "Update tbloperator set ID = '"+tbloperator.getId()+"', Name = '"+tbloperator.getName()+"', Role = '"+tbloperator.getRole()+"', Password = '"+tbloperator.getPassword()+"',  Comment = '"+tbloperator.getComment()+"', UpdatedBy = 'test01', UpdatedAtDate = '"+LocalDate.now().toString()+"', UpdatedAtTime ='"+LocalTime.now().toString()+"' where ID='"+ tbloperator.getId()+"'";}
				else
				{   strSQL = "Update tbloperator set ID = '"+tbloperator.getId()+"', Name = '"+tbloperator.getName()+"', Role = '"+tbloperator.getRole()+"', Password = '"+tbloperator.getPassword()+"',  Comment = '"+tbloperator.getComment()+"', UpdatedBy = 'test01', UpdatedAtDate = '"+LocalDate.now().toString()+"', UpdatedAtTime ='"+LocalTime.now().toString()+"', DisabledBy ='"+tbloperator.getDisabledBy() +"' , DisabledAtDate ='"+strDisabledAtDate+"', DisabledAtTime ='"+strDisabledAtTime+"' where ID='"+ tbloperator.getId()+"'";}
					
				statement.executeUpdate(strSQL);
				databaseConexion.getDBConnection().commit(); // importante hacer commit sino no guarda la información en la base de datos
				statement.close(); //liberar recursos
				bResult = true;
				
		}
			}
		
		catch (Exception ex) {
			if (databaseConexion != null && databaseConexion.getDBConnection() != null)
			{
				try {
					// i hay error hacer rollback a las operaciones anteriores
					databaseConexion.getDBConnection().rollback(); 
				} catch (Exception e) {
					
					if ( localLogger != null )   
						localLogger.logException( "-1021", e.getMessage(), e);        
				}
			}			
			if ( localLogger != null )   
				localLogger.logException( "-1022", ex.getMessage(), ex );        
		} 
		return bResult;
	
	}
	
	public static boolean updateLastLogin (final CDatabaseConexion databaseConexion, final String strId, CExtendedLogger localLogger, CLanguage localLanguage) {
		
		boolean bResult = false;
		try{
			if (databaseConexion != null && databaseConexion.getDBConnection() != null)
			{
				Statement statement = databaseConexion.getDBConnection().createStatement();
				
				
				final String strSQL = "Update tbloperator set LastLoginAtDate='"+LocalDate.now().toString()+"', LastLoginAtTime='"+LocalTime.now().toString()+"' where ID='"+ strId+"'"; 
				statement.executeUpdate(strSQL);
				databaseConexion.getDBConnection().commit(); // importante hacer commit sino no guarda la información en la base de datos
				statement.close(); //liberar recursos
				bResult = true;
				
		}
			}
		
		catch (Exception ex) {
			if (databaseConexion != null && databaseConexion.getDBConnection() != null)
			{
				try {
					// i hay error hacer rollback a las operaciones anteriores
					databaseConexion.getDBConnection().rollback(); 
				} catch (Exception e) {
					
					if ( localLogger != null )   
						localLogger.logException( "-1021", e.getMessage(), e);        
				}
			}			
			if ( localLogger != null )   
				localLogger.logException( "-1022", ex.getMessage(), ex );        
		} 
		return bResult;
	
	}
	
	public static List<TBLOperator> searchData (final CDatabaseConexion databaseConexion, CExtendedLogger localLogger, CLanguage localLanguage) {
		
		List<TBLOperator> result = new ArrayList<TBLOperator>();
		
		return result;
	}
	
	
	public static TBLOperator checkvalid (final CDatabaseConexion databaseConexion, final String strName, final String strPassword, CExtendedLogger localLogger, CLanguage localLanguage) {
		
	    TBLOperator result = null;
	try {
			
			if (databaseConexion != null && databaseConexion.getDBConnection() != null) {
				
				Statement statement = databaseConexion.getDBConnection().createStatement();
				
				//aqui se va a necesitar encriptar
				//final String strTest="Select * From tbloperator Where Name='" + strName + "' and Password='"+ strPassword +"' and DisabledBy Is Null and DisabledAtTime Is Null and DisabledAtDate Is Null";
				//ahora no se puede usar el password para buscar en la db, como el name no se repite lo buscamos por alli
				final String strTest="Select * From tbloperator Where Name='" + strName + "' and DisabledBy Is Null and DisabledAtTime Is Null and DisabledAtDate Is Null";

				ResultSet resultSet = statement.executeQuery(strTest);
				if (resultSet.next()){
					
					//Obtenemos el password escriptado del operador de la db
					String strDBPassword = resultSet.getString("Password");
					
					//lo guardamos
					String strDBPasswordKey = strDBPassword; 
					
					//esxtraemos los 30 primeros caracteres
					strDBPasswordKey = strDBPasswordKey.substring(0,29);
					
					//PHP     ->    Java(BCrypt)
					//$2y$10$ ->    $2a$10$       
					
					//cambiamos el $2y$10$ a $2a$10$, un bug de la libreria 
					strDBPasswordKey = strDBPasswordKey.replace("$2y$10$", "$2a$10$");
					
					//Luego usamos el password key para encriptar el password enviado por el operador
					// en la pantalla de login que viene sin encriptar
					String strPasswordHashed = BCrypt.hashpw(strPassword, strDBPasswordKey);
					
					
					//Java(BCrypt)->    PHP     
					//devolvemos el $2a$10$ a $2y$10$
					strPasswordHashed = strPasswordHashed.replace("$2a$10$", "$2y$10$");
					
					if (strPasswordHashed.equals(strDBPassword)) {
					result = new TBLOperator();
					
					
					result.setId(resultSet.getString("ID"));
					result.setName(resultSet.getString("Name"));
					result.setRole(resultSet.getString("Role"));
					result.setPassword(resultSet.getString("Password"));
					result.setComment(resultSet.getString("Comment"));
					
					// interface
					result.setDisabledBy (resultSet.getString("DisabledBy"));
					result.setDisabledAtDate (resultSet.getDate("DisabledAtDate") != null ?  resultSet.getDate("DisabledAtDate").toLocalDate(): null); //puede ser null de la db
					result.setDisabledAtTime (resultSet.getTime("DisabledAtTime") != null ?  resultSet.getTime("DisabledAtTime").toLocalTime(): null);//puede ser null de la db
					result.setLastLoginAtDate (resultSet.getDate("LastLoginAtDate") != null ? resultSet.getDate("LastLoginAtDate").toLocalDate() : null);//puede ser null de la db
					result.setLastLoginAtTime (resultSet.getTime("LastLoginAtTime") != null ? resultSet.getTime("LastLoginAtTime").toLocalTime() : null);//puede ser null de la db

					result.setCreatedBy (resultSet.getString("CreatedBy"));
					result.setCreatedAtDate (resultSet.getDate("CreatedAtDate").toLocalDate());
					result.setCreatedAtTime (resultSet.getTime("CreatedAtTime").toLocalTime());
					result.setUpdatedBy (resultSet.getString("UpdatedBy"));
					result.setUpdatedAtDate (resultSet.getDate("UpdatedAtDate") != null ? resultSet.getDate("UpdatedAtDate").toLocalDate() : null);
					result.setUpdatedAtTime (resultSet.getTime("UpdatedAtTime") != null ? resultSet.getTime("UpdatedAtTime").toLocalTime() : null);
					
				}
					}
				// una vez termina hay que liberar recursos
				statement.close();
				resultSet.close();
			
				}	
			}
		catch (Exception ex) {
			if (databaseConexion != null && databaseConexion.getDBConnection() != null)
			{
				try {
					//Si hay error hacer rollback a las operaciones anteriores
					databaseConexion.getDBConnection().rollback(); 
				} catch (Exception e) {
					
					if ( localLogger != null )   
						localLogger.logException( "-1021", e.getMessage(), e );        
				}
			}						
			if ( localLogger != null )   
				localLogger.logException( "-1022", ex.getMessage(), ex );        
		}
				
		return result;
		
	}
		
	
	

public static TBLOperator changePassword(final CDatabaseConexion databaseConexion, final TBLOperator tbloperator, String strNewPassword, String strOldPassword, CExtendedLogger localLogger, CLanguage localLanguage) {
		
	    TBLOperator result = null;
	try {
			
			if (databaseConexion != null && databaseConexion.getDBConnection() != null) {
				
				Statement statement = databaseConexion.getDBConnection().createStatement();
				
				//aqui se va a necesitar encriptar
				//final String strTest="Select * From tbloperator Where Name='" + strName + "' and Password='"+ strPassword +"' and DisabledBy Is Null and DisabledAtTime Is Null and DisabledAtDate Is Null";
				//ahora no se puede usar el password para buscar en la db, como el name no se repite lo buscamos por alli
				final String strTest="Select * From tbloperator Where Name='" + tbloperator.getName() + "' and DisabledBy Is Null and DisabledAtTime Is Null and DisabledAtDate Is Null";

				ResultSet resultSet = statement.executeQuery(strTest);
				if (resultSet.next()){
					
					//Obtenemos el password escriptado del operador de la db
					String strDBPassword = resultSet.getString("Password");
					
					//lo guardamos
					String strDBPasswordKey = strDBPassword; 
					
					//esxtraemos los 30 primeros caracteres
					strDBPasswordKey = strDBPasswordKey.substring(0,29);
					
					//PHP     ->    Java(BCrypt)
					//$2y$10$ ->    $2a$10$       
					
					//cambiamos el $2y$10$ a $2a$10$, un bug de la libreria 
					strDBPasswordKey = strDBPasswordKey.replace("$2y$10$", "$2a$10$");
					
					//Luego usamos el password key para encriptar el password enviado por el operador
					// en la pantalla de login que viene sin encriptar
					String strPasswordHashed = BCrypt.hashpw(strOldPassword, strDBPasswordKey);
					
					
					//Java(BCrypt)->    PHP     
					//devolvemos el $2a$10$ a $2y$10$
					strPasswordHashed = strPasswordHashed.replace("$2a$10$", "$2y$10$");
					
					if (strPasswordHashed.equals(strDBPassword)) {
						
						String strPasswordKey = BCrypt.gensalt(10); //establecemos el parametro de inicio para encriptar
						strNewPassword= BCrypt.hashpw(strNewPassword, strPasswordKey); //aqui se realiza la encriptación
						strNewPassword = strNewPassword.replace("$2a$10$", "$2y$10$"); //
						
						tbloperator.setPassword(strNewPassword);

						updateData(databaseConexion, tbloperator, localLogger, localLanguage);
						Messagebox.show("Password Cambiado exitosamente");
                    }else
				{
					 Messagebox.show("Password Incorrecto");
				}
					}
				// una vez termina hay que liberar recursos
				statement.close();
				resultSet.close();
			
				}	
			}
		catch (Exception ex) {
			if (databaseConexion != null && databaseConexion.getDBConnection() != null)
			{
				try {
					//Si hay error hacer rollback a las operaciones anteriores
					databaseConexion.getDBConnection().rollback(); 
				} catch (Exception e) {
					
					if ( localLogger != null )   
						localLogger.logException( "-1021", e.getMessage(), e );        
				}
			}						
			if ( localLogger != null )   
				localLogger.logException( "-1022", ex.getMessage(), ex );        
		}
				
		return result;
		
	}
		
	
	
}
