package com.cts.idp.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;

@Entity
public class Files {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	private String userId;
	
	private String fileName;
	
	private String fileType;
	
	private String customerName;
	
	private boolean validated;
	
	private boolean processed;
	
	private boolean infoExtracted;
	
	private boolean docClassified;
	
	
	
	@Lob
	private byte[] data;

	

	public Files(String userId, String fileName, String fileType, String customerName, boolean validated,
			boolean processed, boolean infoExtracted, boolean docClassified, byte[] data) {
		super();
		this.userId = userId;
		this.fileName = fileName;
		this.fileType = fileType;
		this.customerName = customerName;
		this.validated = validated;
		this.processed = processed;
		this.infoExtracted = infoExtracted;
		this.docClassified = docClassified;
		this.data = data;
	}

	public Files() {
		super();
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getFileType() {
		return fileType;
	}

	public void setFileType(String fileType) {
		this.fileType = fileType;
	}

	public byte[] getData() {
		return data;
	}

	public void setData(byte[] data) {
		this.data = data;
	}

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public boolean isValidated() {
		return validated;
	}

	public void setValidated(boolean validated) {
		this.validated = validated;
	}

	public boolean isProcessed() {
		return processed;
	}

	public void setProcessed(boolean processed) {
		this.processed = processed;
	}

	public boolean isInfoExtracted() {
		return infoExtracted;
	}

	public void setInfoExtracted(boolean infoExtracted) {
		this.infoExtracted = infoExtracted;
	}

	public boolean isDocClassified() {
		return docClassified;
	}

	public void setDocClassified(boolean docClassified) {
		this.docClassified = docClassified;
	}
	
	
	
	

}
