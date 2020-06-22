package com.ram.ppmtool.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ram.ppmtool.entity.Backlog;

@Repository
public interface BacklogRepository extends JpaRepository<Backlog, Long>{

	Backlog  findByProjectIdentifier(String projectIdentifier);
	
}
