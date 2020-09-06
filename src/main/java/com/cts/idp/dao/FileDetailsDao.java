package com.cts.idp.dao;

public class FileDetailsDao {
	
	private String fileName;
	private String fileType;
	private boolean isInformationExtacted;
	private boolean isDocumentClassified;
	private boolean isDocumentVerified;
	private CustomerDetailsDao customerDetails;
	private String message;
	
	
	
	public FileDetailsDao(String fileName, String fileType, boolean isInformationExtacted, boolean isDocumentClassified,
			boolean isDocumentVerified, CustomerDetailsDao customerDetails) {
		super();
		this.fileName = fileName;
		this.fileType = fileType;
		this.isInformationExtacted = isInformationExtacted;
		this.isDocumentClassified = isDocumentClassified;
		this.isDocumentVerified = isDocumentVerified;
		this.customerDetails = customerDetails;
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
	public boolean isInformationExtacted() {
		return isInformationExtacted;
	}
	public void setInformationExtacted(boolean isInformationExtacted) {
		this.isInformationExtacted = isInformationExtacted;
	}
	public boolean isDocumentClassified() {
		return isDocumentClassified;
	}
	public void setDocumentClassified(boolean isDocumentClassified) {
		this.isDocumentClassified = isDocumentClassified;
	}
	public boolean isDocumentVerified() {
		return isDocumentVerified;
	}
	public void setDocumentVerified(boolean isDocumentVerified) {
		this.isDocumentVerified = isDocumentVerified;
	}
	public CustomerDetailsDao getCustomerDetails() {
		return customerDetails;
	}
	public void setCustomerDetails(CustomerDetailsDao customerDetails) {
		this.customerDetails = customerDetails;
	}
	@Override
	public String toString() {
		return "FileDetails [fileName=" + fileName + ", fileType=" + fileType + ", isInformationExtacted="
				+ isInformationExtacted + ", isDocumentClassified=" + isDocumentClassified + ", isDocumentVerified="
				+ isDocumentVerified + ", customerDetails=" + customerDetails + "]";
	}
	
	

}
