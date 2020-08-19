package com.cts.idp.repo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import com.cts.idp.model.Files;

public interface FileRepository extends JpaRepository<Files, Integer> {
	Files findByFileName(String fileName);
	
	@Transactional
	Integer deleteByFileName(String fileName);
}
