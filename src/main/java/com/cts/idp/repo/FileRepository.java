package com.cts.idp.repo;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import com.cts.idp.model.Files;

public interface FileRepository extends JpaRepository<Files, Integer> {
	
	@Query("select f from Files f where f.userId = ?1 and f.fileName = ?2")
	Files findByFileNameUserId(String userId, String fileName);
	
	@Transactional
	@Modifying
	@Query("delete from Files f where f.userId = ?1 and f.fileName = ?2")
	Integer deleteByFileName(String userId, String fileName);
	
	List<Files> findByUserId(String userId);
	
	@Modifying
	@Transactional
	@Query("Update Files set infoExtracted = ?1, validated = ?2, processed = ?3, docClassified = ?4 , customerName = ?5 where userId = ?6 and fileName = ?7")
	void updateByUserIdFileName(Boolean infoExtracted, Boolean validated, Boolean processed, Boolean docClassfied, String customerName, String userId, String fileName);

	@Modifying
	@Transactional
	@Query("Update Files set processed = ?1 where userId = ?2 and fileName = ?3")
	void updateProcessedToFalse(boolean isProcessed, String userId, String key);
	
	@Modifying
	@Transactional
	@Query("Update Files set validated = ?1 where userId = ?2")
	void validateCustomerDetails(Boolean validated,String userId);

}
