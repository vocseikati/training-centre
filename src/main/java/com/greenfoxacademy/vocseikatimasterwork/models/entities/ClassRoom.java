package com.greenfoxacademy.vocseikatimasterwork.models.entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Entity
@Table(name = "rooms")
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class ClassRoom {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Positive(message = "ID must be positive (0: invalid).")
  private Long id;

  private String name;

  @NotEmpty(message = "Address must have a value.")
  @Column(unique = true)
  private String address;

  @NotNull(message = "Capacity must be set.")
  @Positive(message = "Capacity must be positive.")
  private Integer capacity;

  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm")
  private LocalDateTime createdAt;

  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm")
  private LocalDateTime lastUpdatedAt;

  @PrePersist
  private void setCreationTime() {
    createdAt = LocalDateTime.now();
  }

  @PreUpdate
  private void setModificationDate() {
    lastUpdatedAt = LocalDateTime.now();
  }

}
