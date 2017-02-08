package org.test.zk.database.dao;

import java.sql.ResultSet;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import org.test.zk.database.CDatabaseConexion;
import org.test.zk.database.datamodel.TBLPerson;

import commonlibs.commonclasses.CLanguage;
import commonlibs.extendedlogger.CExtendedLogger;

public class PersonDAO {
	
	public static TBLPerson loadData (final CDatabaseConexion databaseConexion, final String strId, CExtendedLogger localLogger, CLanguage localLanguage) {
		
		TBLPerson result = null;

	try {
			
			if (databaseConexion != null && databaseConexion.getDBConnection() != null) {
				Statement statement = databaseConexion.getDBConnection().createStatement();
				ResultSet resultSet = statement.executeQuery("Select * From tblperson Where ID='" + strId + "'");
				if (resultSet.next()){
					
					result = new TBLPerson();
					
					
					result.setId(resultSet.getString("ID"));
					result.setFirstName(resultSet.getString("FirstName"));
					result.setLastName(resultSet.getString("LastName"));
					result.setGender(resultSet.getInt("Gender"));
					result.setBirthDate( (resultSet.getDate("BirthDate").toLocalDate()));
					result.setComment(resultSet.getString("Comment"));
					
					// interface
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
				final String strSQL = "delete from tblperson where ID = '"+strId+"'"; 
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

	public static boolean insertData (final CDatabaseConexion databaseConexion, final TBLPerson tblperson, CExtendedLogger localLogger, CLanguage localLanguage) {
		
		boolean bResult = false;
		try{
			if (databaseConexion != null && databaseConexion.getDBConnection() != null)
			{
				Statement statement = databaseConexion.getDBConnection().createStatement();
				final String strSQL = "insert into tblperson (ID,FirstName,LastName,Gender,BirthDate,Comment,CreatedBy,CreatedAtDate,CreatedAtTime,UpdatedBy,UpdatedAtDate,UpdatedAtTime) values ('"+tblperson.getId()+"','"+tblperson.getFirstName()+"','"+tblperson.getLastName()+"',"+tblperson.getGender()+ ",'"+tblperson.getBirthDate().toString()+"','"+tblperson.getComment()+"','test','"+ LocalDate.now().toString()+"','"+LocalTime.now().toString()+"',null,null,null)"; 
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

	public static boolean updateData (final CDatabaseConexion databaseConexion, final TBLPerson tblperson, CExtendedLogger localLogger, CLanguage localLanguage) {
		
		boolean bResult = false;
		try{
			if (databaseConexion != null && databaseConexion.getDBConnection() != null)
			{
				Statement statement = databaseConexion.getDBConnection().createStatement();
				final String strSQL = "Update tblperson set ID = '"+tblperson.getId()+"', FirstName = '"+tblperson.getFirstName()+"', LastName = '"+tblperson.getLastName()+"', Gender = "+tblperson.getGender()+ ", BirthDate = '"+tblperson.getBirthDate().toString()+"', Comment = '"+tblperson.getComment()+"', UpdatedBy = 'test01', UpdatedAtDate = '"+LocalDate.now().toString()+"', UpdatedAtTime ='"+LocalTime.now().toString()+"' where ID='"+ tblperson.getId()+"'"; 
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

	public static List<TBLPerson> searchData (final CDatabaseConexion databaseConexion, CExtendedLogger localLogger, CLanguage localLanguage) {
		
		List<TBLPerson> result = new ArrayList<TBLPerson>();
		
		try {
			
			if (databaseConexion != null && databaseConexion.getDBConnection() != null) {
				Statement statement = databaseConexion.getDBConnection().createStatement();
				ResultSet resultSet = statement.executeQuery("Select * From tblperson");
				while (resultSet.next()){
					
					TBLPerson tblPerson = new TBLPerson();
					
					
					tblPerson.setId(resultSet.getString("ID"));
					tblPerson.setFirstName(resultSet.getString("FirstName"));
					tblPerson.setLastName(resultSet.getString("LastName"));
					tblPerson.setGender(resultSet.getInt("Gender"));
					tblPerson.setBirthDate( (resultSet.getDate("BirthDate").toLocalDate()));
					tblPerson.setComment(resultSet.getString("Comment"));
					
					// interface
					tblPerson.setCreatedBy (resultSet.getString("CreatedBy"));
					tblPerson.setCreatedAtDate (resultSet.getDate("CreatedAtDate").toLocalDate());
					tblPerson.setCreatedAtTime (resultSet.getTime("CreatedAtTime").toLocalTime());
					tblPerson.setUpdatedBy (resultSet.getString("UpdatedBy"));
					tblPerson.setUpdatedAtDate (resultSet.getDate("UpdatedAtDate") != null ? resultSet.getDate("UpdatedAtDate").toLocalDate() : null);
					tblPerson.setUpdatedAtTime (resultSet.getTime("UpdatedAtTime") != null ? resultSet.getTime("UpdatedAtTime").toLocalTime() : null);
					
					result.add(tblPerson); // agregar a la lista resultados
				}
				// una vez termina hay que liberar recursos
				statement.close();
				resultSet.close();
			
				}	
			}
		catch (Exception ex) {
			if ( localLogger != null )   
				localLogger.logException( "-1021", ex.getMessage(), ex );        
		}
				
		return result;
	}	
	
}
