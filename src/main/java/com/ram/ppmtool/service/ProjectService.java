package com.ram.ppmtool.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ram.ppmtool.entity.Backlog;
import com.ram.ppmtool.entity.Project;
import com.ram.ppmtool.entity.User;
import com.ram.ppmtool.exception.ProjectIdException;
import com.ram.ppmtool.exception.ProjectNotFoundException;
import com.ram.ppmtool.repository.BacklogRepository;
import com.ram.ppmtool.repository.ProjectRepository;
import com.ram.ppmtool.repository.UserRepository;

@Service
public class ProjectService {

	@Autowired
	private ProjectRepository projectRepository;
	
	@Autowired
	private BacklogRepository backlogRepository;
	
	@Autowired
	private UserRepository userRepository;

	public Project saveorUpdateProject(Project project, String username) {

		try {
			
			if(project.getId() != null) {
				Project existingProject = projectRepository.findByProjectIdentifier(project.getProjectIdentifier());
				
				if(existingProject != null && (!existingProject.getProjectLeader().equals(username))) {
					throw new ProjectNotFoundException("Project not found in your account");
				} else if(existingProject == null) {
					throw new ProjectNotFoundException("Project with ID: "+project.getProjectIdentifier()+" cann't be updated because it does not exist");
				}
			} 
			
			
			
			User user = userRepository.findByUsername(username);
			project.setUser(user);
			project.setProjectLeader(user.getUsername());
			project.setProjectIdentifier(project.getProjectIdentifier().toUpperCase());
			
			if(project.getId() == null) {
			Backlog backlog = new Backlog();
			project.setBacklog(backlog);
			backlog.setProject(project);
			backlog.setProjectIdentifier(project.getProjectIdentifier().toUpperCase());
			}
			
			if(project.getId() != null) {
				project.setBacklog(backlogRepository.findByProjectIdentifier(project.getProjectIdentifier().toUpperCase()));
			}
			return projectRepository.save(project);
		} catch (Exception e) {
			throw new ProjectIdException(
					"Project ID: " + project.getProjectIdentifier().toUpperCase() + " already exits");
		}
	}

	public Project findProjectByIdentifier(String projectId, String username) {
		Project project = projectRepository.findByProjectIdentifier(projectId.toUpperCase());
		
		if(project == null) {
			throw new ProjectIdException("Project ID "+projectId+" does not exits");
		}
		
		if(!project.getProjectLeader().equals(username)) {
			throw new ProjectNotFoundException("Project not found in your account");
		}
		return project;
	}
	
	public Iterable<Project> findAllProjects(String username){
		return projectRepository.findAllByProjectLeader(username); 
	}
	
	public void deleteProjectByIdentifier(String projectid, String username) {
		
		
		projectRepository.delete(findProjectByIdentifier(projectid, username));
	}
}
