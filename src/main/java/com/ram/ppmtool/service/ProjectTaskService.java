package com.ram.ppmtool.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ram.ppmtool.entity.Backlog;
import com.ram.ppmtool.entity.Project;
import com.ram.ppmtool.entity.ProjectTask;
import com.ram.ppmtool.exception.ProjectNotFoundException;
import com.ram.ppmtool.repository.BacklogRepository;
import com.ram.ppmtool.repository.ProjectRepository;
import com.ram.ppmtool.repository.ProjectTaskRepository;

@Service
public class ProjectTaskService {

	@Autowired
	private ProjectService projectService;
	@Autowired
	private BacklogRepository backlogRepository;

	@Autowired
	private ProjectTaskRepository projectTaskRepository;

	@Autowired
	private ProjectRepository projectRepository;
	
	

	public ProjectTask addProjectTask(String projectIdentifier, ProjectTask projectTask,String username) {

		try {
			
			
			Backlog backlog = projectService.findProjectByIdentifier(projectIdentifier, username).getBacklog();

			projectTask.setBacklog(backlog);

			Integer BacklogSequence = backlog.getPTSequence();
			BacklogSequence++;

			backlog.setPTSequence(BacklogSequence);

			projectTask.setProjectSequence(projectIdentifier + "-" + BacklogSequence);
			projectTask.setProjectIdentifier(projectIdentifier);

			if (projectTask.getPriority() ==null || projectTask.getPriority() == 0) {
				projectTask.setPriority(3);
			}
			if (projectTask.getStatus() == "" || projectTask.getStatus() == null) {
				projectTask.setStatus("TO_DO");
			}

			return projectTaskRepository.save(projectTask);
		} catch (Exception e) {
			throw new ProjectNotFoundException("Project not found");
		}

	}

	public Iterable<ProjectTask> findBacklogById(String id, String username) {

		projectService.findProjectByIdentifier(id, username);
		return projectTaskRepository.findByProjectIdentifierOrderByPriority(id);

	}

	
	public ProjectTask findPTByProjectSequence(String backlog_id, String pt_id, String username) {
		
       projectService.findProjectByIdentifier(backlog_id, username);		
		
		ProjectTask projectTask = projectTaskRepository.findByProjectSequence(pt_id);

		if (projectTask == null) {
			throw new ProjectNotFoundException("Project Task: " + pt_id + " does not exits");
		}

		if (!projectTask.getProjectIdentifier().equals(backlog_id)) {
			throw new ProjectNotFoundException("Project Task: " + pt_id + " does not exits in project: " + backlog_id);
		}

		return projectTask;
	}

	public ProjectTask updateByProjectSequence(ProjectTask updatedTask, String backlog_id, String pt_id, String username) {

		ProjectTask projectTask = findPTByProjectSequence(backlog_id, pt_id, username);

		projectTask = updatedTask;

		return projectTaskRepository.save(projectTask);
	}

	public void deletePTByProjectSequence(String backlog_id, String pt_id, String username) {
		
		ProjectTask projectTask = findPTByProjectSequence(backlog_id, pt_id, username);
		
		
		projectTaskRepository.delete(projectTask);
	}
}
