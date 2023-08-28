package com.greenfoxacademy.vocseikatimasterwork.models.entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.greenfoxacademy.vocseikatimasterwork.models.Gender;
import com.greenfoxacademy.vocseikatimasterwork.models.Participant;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "students")
@NoArgsConstructor
public class Student extends Participant implements Serializable {

  private String level;
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm")
  private LocalDateTime createdAt;
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm")
  private LocalDateTime lastUpdatedAt;

  @ManyToMany(mappedBy = "students")
  private Set<Course> courses = new HashSet<>();

  public Student(Long id, String firstName, String lastName, Gender gender, LocalDate dateOfBirth,
                 String mailingAddress, String billingAddress, String email,
                 String phoneNumber, String level, LocalDateTime createdAt,
                 LocalDateTime lastUpdatedAt) {
    super(id, firstName, lastName, gender, dateOfBirth, mailingAddress, billingAddress, email,
        phoneNumber);
    this.level = level;
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

  @Override
  public String toString() {
    return "Student{"
        + "level='" + level + '\''
        + super.toString();
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    if (!super.equals(o)) {
      return false;
    }
    Student student = (Student) o;
    return Objects.equals(level, student.level);
  }

  @Override
  public int hashCode() {
    return Objects.hash(super.hashCode(), level);
  }
}
