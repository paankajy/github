package com.ram.ppmtool.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ram.ppmtool.entity.Project;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long>{

	@Override
	 List<Project> findAll();
	
	Iterable<Project> findAllByProjectLeader(String username);

	Project findByProjectIdentifier(String upperCase);
	
}
