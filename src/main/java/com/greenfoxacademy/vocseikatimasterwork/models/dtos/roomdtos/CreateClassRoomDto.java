package com.greenfoxacademy.vocseikatimasterwork.models.dtos.roomdtos;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.greenfoxacademy.vocseikatimasterwork.models.entities.ClassRoom;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@ApiModel(description = "Details of the class room you want to create.")
public class CreateClassRoomDto {

  @ApiModelProperty(value = "ID of the class room.")
  @Positive(message = "ID must be positive (0: invalid).")
  private Long id;

  @ApiModelProperty(value = "Name of the class room you want to create.")
  private String name;

  @ApiModelProperty(value = "Address of the class room you want to create.")
  @NotEmpty(message = "Address must have a value.")
  @Column(unique = true)
  private String address;

  @ApiModelProperty(value = "Max capacity of the class room you want to create.")
  @NotNull(message = "Capacity must be set.")
  @Positive(message = "Capacity must be positive.")
  private Integer capacity;

  @JsonProperty(access = JsonProperty.Access.READ_ONLY)
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm")
  private LocalDateTime createdAt = LocalDateTime.now();

  @JsonProperty(access = JsonProperty.Access.READ_ONLY)
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm")
  private LocalDateTime lastUpdatedAt = LocalDateTime.now();

  public ClassRoom convertToRoom() {
    return new ClassRoom(this.id, this.name, this.address, this.capacity, this.createdAt,
        this.lastUpdatedAt);
  }
}
