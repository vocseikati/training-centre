package com.greenfoxacademy.vocseikatimasterwork.models.entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.greenfoxacademy.vocseikatimasterwork.models.CourseType;
import com.greenfoxacademy.vocseikatimasterwork.models.Status;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
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
@Table(name = "courses")
@NoArgsConstructor
@AllArgsConstructor
public class Course {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Positive(message = "ID must be positive (0: invalid).")
  private Long id;

  @NotBlank(message = "Title must have a value.")
  private String title;

  @NotNull(message = "Start date must have a value.")
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
  private LocalDate startDate;

  @NotNull(message = "End date must have a value.")
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
  private LocalDate endDate;

  @NotNull(message = "Duration must have a value.")
  @Positive(message = "Duration hours must be positive.")
  private Integer durationInHours;

  @NotNull(message = "Education format must have a value. Allowed input: true or false.")
  private Boolean individualEducation;

  @NotNull(message = "Price must be set.")
  private Double price;

  @Enumerated(EnumType.STRING)
  private CourseType type;

  @Enumerated(EnumType.STRING)
  private Status status;

  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm")
  private LocalDateTime createdAt;

  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm")
  private LocalDateTime lastUpdatedAt;

  @ManyToMany(cascade = {CascadeType.DETACH,
      CascadeType.MERGE,
      CascadeType.REFRESH,
      CascadeType.PERSIST})
  @JoinTable(
      name = "student_courses",
      joinColumns = @JoinColumn(name = "course_id"),
      inverseJoinColumns = @JoinColumn(name = "student_id"))
  private Set<Student> students = new HashSet<>();

  @ManyToOne(cascade = CascadeType.REFRESH)
  @JoinColumn(name = "instructor_id")
  private Instructor instructor;

  @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.REFRESH)
  @JoinColumn(name = "class_room_id")
  private ClassRoom classRoom;

  public Course(Long id, String title, LocalDate startDate, LocalDate endDate, int durationInHours,
                boolean individualEducation, double price, CourseType type,
                Status status, Instructor instructor, ClassRoom classRoom, LocalDateTime createdAt,
                LocalDateTime lastUpdatedAt) {
    this.id = id;
    this.title = title;
    this.startDate = startDate;
    this.endDate = endDate;
    this.durationInHours = durationInHours;
    this.individualEducation = individualEducation;
    this.price = price;
    this.type = type;
    this.status = status;
    this.instructor = instructor;
    this.classRoom = classRoom;
    this.createdAt = createdAt;
    this.lastUpdatedAt = lastUpdatedAt;
  }

  public Status getStatus() {
    if (this.endDate.isBefore(LocalDate.now())) {
      this.status = Status.FINISHED;
    }
    if (this.endDate.isAfter(LocalDate.now()) && this.startDate.isBefore(LocalDate.now())) {
      this.status = Status.IN_PROGRESS;
    }
    if (this.startDate.isAfter(LocalDate.now())) {
      this.status = Status.PLANNED;
    }
    return status;
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
    return "Course{"
        + "id=" + id
        + ", title='" + title + '\''
        + ", startDate=" + startDate
        + ", endDate=" + endDate
        + ", durationInHours=" + durationInHours
        + ", individualEducation=" + individualEducation
        + ", price=" + price
        + ", type=" + type
        + ", status=" + status
        + ", createdAt=" + createdAt
        + ", lastUpdatedAt=" + lastUpdatedAt
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
    Course course = (Course) o;
    return Objects.equals(id, course.id) && Objects.equals(title, course.title)
        && Objects.equals(startDate, course.startDate)
        && Objects.equals(endDate, course.endDate)
        && Objects.equals(durationInHours, course.durationInHours)
        && Objects.equals(individualEducation, course.individualEducation)
        && Objects.equals(price, course.price) && type == course.type
        && status == course.status;
  }

  @Override
  public int hashCode() {
    return Objects
        .hash(id, title, startDate, endDate, durationInHours, individualEducation, price, type,
            status);
  }
}
