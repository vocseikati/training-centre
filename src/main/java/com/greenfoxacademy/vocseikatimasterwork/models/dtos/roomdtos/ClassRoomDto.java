package com.greenfoxacademy.vocseikatimasterwork.models.dtos.roomdtos;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.greenfoxacademy.vocseikatimasterwork.models.entities.ClassRoom;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@ApiModel(description = "Details of a class room.")
public class ClassRoomDto {

  @ApiModelProperty(value = "Unique ID of the classroom, autogenerated by the database.")
  private Long id;

  @ApiModelProperty(value = "Name of the class room.")
  private String name;

  @ApiModelProperty(value = "Address of the class room.")
  private String address;

  @ApiModelProperty(value = "Max capacity of the class room.")
  private Integer capacity;

  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm")
  private LocalDateTime createdAt;
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm")
  private LocalDateTime lastUpdatedAt;

  public static ClassRoomDto convertToRoomDto(ClassRoom room) {
    return new ClassRoomDto(room.getId(), room.getName(), room.getAddress(), room.getCapacity(),
        room.getCreatedAt(), room.getLastUpdatedAt());
  }
}
