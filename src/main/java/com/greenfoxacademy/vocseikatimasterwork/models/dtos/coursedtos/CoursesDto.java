package com.greenfoxacademy.vocseikatimasterwork.models.dtos.coursedtos;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.HashSet;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(description = "List of basic information for courses is required for education.")
public class CoursesDto {

  @ApiModelProperty(value = "List of basic information for courses is required for education.")
  private Set<CourseForParticipantDto> courses = new HashSet<>();

}
