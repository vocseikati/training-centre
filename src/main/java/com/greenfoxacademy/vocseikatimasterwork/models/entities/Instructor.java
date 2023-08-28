package com.greenfoxacademy.vocseikatimasterwork.models.entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.greenfoxacademy.vocseikatimasterwork.models.Gender;
import com.greenfoxacademy.vocseikatimasterwork.models.Participant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.PrePersist;
import javax.persistence.PreRemove;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "instructors")
@NoArgsConstructor
public class Instructor extends Participant {

  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm:ss")
  private LocalDateTime createdAt;
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm:ss")
  private LocalDateTime lastUpdatedAt;

  @OneToMany(mappedBy = "instructor")
  private Set<Course> courses = new HashSet<>();

  public Instructor(Long id, String firstName, String lastName,
                    Gender gender, LocalDate dateOfBirth, String mailingAddress,
                    String billingAddress, String email, String phoneNumber,
                    LocalDateTime createdAt, LocalDateTime lastUpdatedAt) {
    super(id, firstName, lastName, gender, dateOfBirth, mailingAddress, billingAddress, email,
        phoneNumber);
    this.createdAt = createdAt;
    this.lastUpdatedAt = lastUpdatedAt;
  }

  @PrePersist
  private void setCreationTime() {
    createdAt = LocalDateTime.now();
  }

  @PreUpdate
  private void setModificationDate() {
    lastUpdatedAt = LocalDateTime.now();
  }

  @PreRemove
  private void preRemove() {
    for (Course course : courses) {
      course.setInstructor(null);
    }
  }
}
