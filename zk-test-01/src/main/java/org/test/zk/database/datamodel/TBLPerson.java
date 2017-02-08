package org.test.zk.database.datamodel;

import java.io.Serializable;
import java.time.LocalDate;

public class TBLPerson extends CAuditableDataModel implements Serializable {

	private static final long serialVersionUID = -8697546381830704030L;

	protected String strId;
	protected String strFirstName;
	protected String strLastName;
	protected Integer IntGender; //0 Female - 1 Male
	protected LocalDate birthDate;
	protected String strComment;
	
	

	//Constructor
	
	public TBLPerson (String strId, String strFirstName, String strLastName, int IntGender, LocalDate birthDate, String strComment){
		
		this.strId=strId;
		this.strFirstName=strFirstName;
		this.strLastName=strLastName;
		this.IntGender=IntGender;
		this.birthDate=birthDate;
		this.strComment=strComment;


	}

	public TBLPerson()
	{
		
	}
	
	
	public String getId() {
		return strId;
	}

	public void setId(String strId) {
		this.strId = strId;
	}

	public String getFirstName() {
		return strFirstName;
	}

	public void setFirstName(String strFirstName) {
		this.strFirstName = strFirstName;
	}

	public String getLastName() {
		return strLastName;
	}

	public void setLastName(String strLastName) {
		this.strLastName = strLastName;
	}
	public Integer getGender() {
		return IntGender;
	}


	public void setGender(Integer intGender) {
		IntGender = intGender;
	}


	public LocalDate getBirthDate() {
		return birthDate;
	}


	public void setBirthDate(LocalDate birthDate) {
		this.birthDate = birthDate;
	}


	public String getComment() {
		return strComment;
	}


	public void setComment(String strComment) {
		this.strComment = strComment;
	}

	
}
