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
	@Query("Update Files set infoExtracted = ?1, validated = ?2, processed = ?3, docClassified = ?4 where userId = ?5 and fileName = ?6")
	void updateByUserIdFileName(Boolean infoExtracted, Boolean validated, Boolean processed, Boolean docClassfied, String userId, String fileName);
	
	
}
