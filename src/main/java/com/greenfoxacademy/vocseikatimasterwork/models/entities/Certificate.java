package com.greenfoxacademy.vocseikatimasterwork.models.entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDate;
import java.util.Objects;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "certificates")
@NoArgsConstructor
@AllArgsConstructor
public class Certificate {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Positive(message = "ID must be positive (0: invalid).")
  private Long id;

  @NotBlank(message = "Student first name must have a value.")
  private String studentFirstName;

  @NotBlank(message = "Student last name must have a value.")
  private String studentLastName;

  @NotNull(message = "Course start date must have a value.")
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
  private LocalDate courseStartDate;

  @NotNull(message = "Course end date must have a value.")
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
  private LocalDate courseEndDate;

  @NotNull(message = "Course duration must have a value.")
  private Integer courseDurationInHours;

  @NotBlank(message = "Course title must have a value.")
  private String courseTitle;

  private Boolean done;

  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
  private LocalDate createdAt;

  @ManyToOne(cascade = CascadeType.REFRESH)
  @JoinColumn(name = "student_id")
  Student student;

  public Certificate(String studentFirstName, String studentLastName,
                     LocalDate courseStartDate, LocalDate courseEndDate,
                     Integer courseDurationInHours, String courseTitle, Boolean done,
                     Student student) {
    this.studentFirstName = studentFirstName;
    this.studentLastName = studentLastName;
    this.courseStartDate = courseStartDate;
    this.courseEndDate = courseEndDate;
    this.courseDurationInHours = courseDurationInHours;
    this.courseTitle = courseTitle;
    this.done = done;
    this.student = student;
  }

  @PrePersist
  private void setCreationDate() {
    createdAt = LocalDate.now();
  }

  @Override
  public String toString() {
    return "Certificate{"
        + "id=" + id
        + ", studentFirstName='" + studentFirstName + '\''
        + ", studentLastName='" + studentLastName + '\''
        + ", courseStartDate=" + courseStartDate
        + ", courseEndDate=" + courseEndDate
        + ", courseDurationInHours=" + courseDurationInHours
        + ", courseTitle='" + courseTitle + '\''
        + ", done=" + done
        + ", createdAt=" + createdAt
        + '}';
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Certificate that = (Certificate) o;
    return Objects.equals(id, that.id)
        && Objects.equals(studentFirstName, that.studentFirstName)
        && Objects.equals(studentLastName, that.studentLastName)
        && Objects.equals(courseStartDate, that.courseStartDate)
        && Objects.equals(courseEndDate, that.courseEndDate)
        && Objects.equals(courseDurationInHours, that.courseDurationInHours)
        && Objects.equals(courseTitle, that.courseTitle)
        && Objects.equals(done, that.done)
        && Objects.equals(createdAt, that.createdAt);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, studentFirstName, studentLastName, courseStartDate, courseEndDate,
        courseDurationInHours, courseTitle, done, createdAt);
  }
}
