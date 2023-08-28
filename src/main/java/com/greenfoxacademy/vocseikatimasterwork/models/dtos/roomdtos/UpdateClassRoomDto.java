package com.greenfoxacademy.vocseikatimasterwork.models.dtos.roomdtos;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import javax.persistence.Column;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@ApiModel(description = "Details of the class room you want to update.")
public class UpdateClassRoomDto {

  @ApiModelProperty(value = "ID of the class room.")
  private Long id;

  @ApiModelProperty(value = "Name of the class room you want to update.")
  private String name;

  @ApiModelProperty(value = "Address of the class room you want to update.")
  @Column(unique = true)
  private String address;

  @ApiModelProperty(value = "Max capacity of the class room you want to update.")
  @NotNull(message = "Capacity must be set.")
  @Positive(message = "Capacity must be positive.")
  private Integer capacity;
}
