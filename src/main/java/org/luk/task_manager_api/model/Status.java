package org.luk.task_manager_api.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum Status {
  @JsonProperty("todo")
  TODO, 

  @JsonProperty("in_progress")
  IN_PROGRESS, 

  @JsonProperty("done")
  DONE
}
