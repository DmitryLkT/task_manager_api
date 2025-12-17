package org.luk.task_manager_api.controllerTest;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.hasSize;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.luk.task_manager_api.controller.ProjectController;
import org.luk.task_manager_api.dto.ProjectResponse;
import org.luk.task_manager_api.dto.TaskResponse;
import org.luk.task_manager_api.model.Status;
import org.luk.task_manager_api.service.ProjectService;
import org.luk.task_manager_api.service.TaskService;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.security.test.context.support.WithMockUser;

import com.fasterxml.jackson.databind.ObjectMapper;

@ExtendWith(MockitoExtension.class)
public class ProjectControllerTest {
  @Mock
  private ProjectService projectService;
  @Mock
  private TaskService taskService;
  @InjectMocks
  private ProjectController projectController;
  private MockMvc mockMvc;
  private ObjectMapper objectMapper;

  private final LocalDateTime TEST_DATE = LocalDateTime.of(2025, 1, 15, 10, 30, 0);

  @BeforeEach
  void setUp() {
    LocalValidatorFactoryBean validator = new LocalValidatorFactoryBean();
    validator.afterPropertiesSet();

    mockMvc = MockMvcBuilders.standaloneSetup(projectController).setValidator(validator).build();
    objectMapper = new ObjectMapper();
  }

  @Test
  @WithMockUser(roles = "USER")
  void getAllProjectsFromUser_Test() throws Exception {
    List<ProjectResponse> list = List.of(
      new ProjectResponse(1L, "New project 1", "mini app", TEST_DATE, "Dmitry"),
      new ProjectResponse(2L, "New project 2", "mini app", TEST_DATE, "Dmitry")
    );

    when(projectService.getAllProjectsFromUser()).thenReturn(list);

    mockMvc.perform(get("/projects/allProjects"))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$", hasSize(2)))
        .andExpect(jsonPath("$[0].id").value(1))
        .andExpect(jsonPath("$[0].title").value("New project 1"))
        .andExpect(jsonPath("$[0].description").value("mini app"))
        .andExpect(jsonPath("$[0].createdAt").exists())
        .andExpect(jsonPath("$[0].ownerName").value("Dmitry"))
        .andExpect(jsonPath("$[1].id").value(2))
        .andExpect(jsonPath("$[1].title").value("New project 2"));
  }

  @Test
  @WithMockUser(roles = "USER")
  void getAllProjectsFromUser_EmptyListWhenNoProjects() throws Exception {
    when(projectService.getAllProjectsFromUser()).thenReturn(List.of());

    mockMvc.perform(get("/projects/allProjects"))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$", hasSize(0)));
  }

  @Test
  @WithMockUser(roles = "USER")
  void getAllTasksThisProject_Test() throws Exception {
    String titleProject = "Project1";

    List<TaskResponse> list = List.of(new TaskResponse(1L, "New task 1", "mini app", Status.TODO, titleProject, "Dmitri", TEST_DATE, TEST_DATE));  

    when(taskService.getAllTasksThisProject(titleProject)).thenReturn(list);
    
    mockMvc.perform(get("/projects/{title}/tasks", titleProject))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$", hasSize(1)))
        .andExpect(jsonPath("$[0].id").value(1))
        .andExpect(jsonPath("$[0].title").value("New task 1"))
        .andExpect(jsonPath("$[0].description").value("mini app"))
        .andExpect(jsonPath("$[0].status").value("todo"))
        .andExpect(jsonPath("$[0].projectTitle").value(titleProject))
        .andExpect(jsonPath("$[0].userName").value("Dmitri"))
        .andExpect(jsonPath("$[0].createdAt").exists())
        .andExpect(jsonPath("$[0].updatedAt").exists());
  }

  @Test
  @WithMockUser(roles = "USER")
  void getAllTasksThisProject_EmptyListWhenNoTasks() throws Exception {
    when(taskService.getAllTasksThisProject("Project1")).thenReturn(List.of());

    mockMvc.perform(get("/projects/{title}/tasks", "Project1"))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$", hasSize(0)));
  }

  @Test
  @WithMockUser(roles = "USER")
  void getProject_Test() throws Exception {

    when(projectService.getProject("New project 1")).thenReturn(
      new ProjectResponse(1L, "New project 1", "mini app", TEST_DATE, "Dmitry")
    );

    mockMvc.perform(get("/projects/{id}", "New project 1"))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.id").value(1))
        .andExpect(jsonPath("$.title").value("New project 1"))
        .andExpect(jsonPath("$.description").value("mini app"));
  }

}