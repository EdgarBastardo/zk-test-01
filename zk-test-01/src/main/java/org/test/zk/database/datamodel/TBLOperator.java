package org.test.zk.database.datamodel;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;

import commonlibs.utils.BCrypt;

public class TBLOperator extends CAuditableDataModel implements Serializable {

	private static final long serialVersionUID = -460145854236713435L;

	protected String strId;
	protected String strName;
	protected String strRole;
	protected String strPassword;
	protected String strComment;
	
	protected String strDisabledBy;
	protected LocalDate disabledAtDate;
	protected LocalTime disabledAtTime;

	protected LocalDate LastLoginAtDate;
	protected LocalTime LastLoginAtTime;

	public String getId() {
		
		return strId;
		
	}
	
	public void setId(String strId) {
		
		this.strId = strId;
		
	}
	
	public String getName() {
		
		return strName;
	}
	
	public void setName(String strName) {
		
		this.strName = strName;
		
	}
	
	public String getRole() {
		
		return strRole;
	}
	
	public void setRole(String strRole) {
		
		this.strRole = strRole;
		
	}
	
	public String getPassword() {
		
		return strPassword;
		
	}
	
	public void setPassword(String strPassword) {
		//verificamos si el strPassword ya viene encryptado
		if  ( strPassword.startsWith("$2y$10$") == false) {
		 String strPasswordKey = BCrypt.gensalt(10); //establecemos el parametro de inicio para encriptar
		 strPassword = BCrypt.hashpw(strPassword, strPasswordKey); //aqui se realiza la encriptación
		 strPassword = strPassword.replaceAll("$2a$10$", "$2y$10$"); //
		}
		this.strPassword = strPassword;
		
	}
	
	public String getComment() {
		
		return strComment;
		
	}
	
	public void setComment(String strComment) {
		
		this.strComment = strComment;
		
	}
	
	public String getDisabledBy() {
		
		return strDisabledBy;
		
	}
	
	public void setDisabledBy(String strDisabledBy) {
		
		this.strDisabledBy = strDisabledBy;
		
	}
	
	public LocalDate getDisabledAtDate() {
		
		return disabledAtDate;
		
	}
	
	public void setDisabledAtDate(LocalDate disabledAtDate) {
		
		this.disabledAtDate = disabledAtDate;
		
	}
	
	public LocalTime getDisabledAtTime() {
		
		return disabledAtTime;
		
	}
	
	public void setDisabledAtTime(LocalTime disabledAtTime) {
		
		this.disabledAtTime = disabledAtTime;
		
	}
	
	public LocalDate getLastLoginAtDate() {
		
		return LastLoginAtDate;
		
	}
	
	public void setLastLoginAtDate(LocalDate lastLoginAtDate) {
		
		LastLoginAtDate = lastLoginAtDate;
		
	}
	
	public LocalTime getLastLoginAtTime() {
		
		return LastLoginAtTime;
		
	}
	
	public void setLastLoginAtTime(LocalTime lastLoginAtTime) {
		
		LastLoginAtTime = lastLoginAtTime;
		
	}
	



}
