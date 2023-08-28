package com.greenfoxacademy.vocseikatimasterwork.services;

import com.greenfoxacademy.vocseikatimasterwork.exceptions.types.EntityNotFoundException;
import com.greenfoxacademy.vocseikatimasterwork.models.entities.Course;
import com.greenfoxacademy.vocseikatimasterwork.models.entities.Student;
import com.greenfoxacademy.vocseikatimasterwork.repositories.CourseRepository;
import com.greenfoxacademy.vocseikatimasterwork.repositories.StudentRepository;
import java.util.Set;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class DefaultMailSenderService implements MailSenderService {

  @Value("${spring.mail.username}")
  private String messageFrom;

  private final JavaMailSender javaMailSender;
  private final SimpleMailMessage mail;

  private final CourseRepository courseRepository;
  private final StudentRepository studentRepository;

  public DefaultMailSenderService(JavaMailSender javaMailSender,
                                  CourseRepository courseRepository,
                                  StudentRepository studentRepository) {
    this.javaMailSender = javaMailSender;
    this.courseRepository = courseRepository;
    this.studentRepository = studentRepository;
    this.mail = new SimpleMailMessage();
  }

  @Override
  public void sendEmailToStudentAfterRegistration(Long courseId, Long studentId) {
    Course course = getCourse(courseId);
    Student student = getStudent(studentId);

    mail.setFrom(messageFrom);
    mail.setTo(student.getEmail());
    mail.setSubject("Enrolling to course");
    mail.setText("Dear " + student.getFirstName() + " " + student.getLastName() + "!\n\n"
        + "You have successfully applied for the following course:\n"
        + "Course title: " + course.getTitle()
        + "\n"
        + "Course start date: " + course.getStartDate()
        + "\n"
        + "Course end date: " + course.getEndDate()
        + "\n"
        + "Course locate: " + course.getClassRoom().getAddress()
        + "\n");

    javaMailSender.send(mail);
  }

  @Override
  public void sendEmailToStudentAfterDeletedFromCourse(Long courseId, Long studentId) {
    Course course = getCourse(courseId);
    Student student = getStudent(studentId);
    mail.setFrom(messageFrom);
    mail.setTo(student.getEmail());
    mail.setSubject("Deleting from the course");
    mail.setText("Dear " + student.getFirstName() + " " + student.getLastName() + "!\n\n"
        + "You have been deleted from the following course:\n"
        + "Course title: " + course.getTitle()
        + "\n");

    javaMailSender.send(mail);
  }

  @Override
  public void sendEmailToStudentsAfterCompletionTheCourse(Long courseId) {
    Course course = getCourse(courseId);
    Set<Student> students = checkStudents(course);
    for (Student student : students) {
      mail.setFrom(messageFrom);
      mail.setTo(student.getEmail());
      mail.setSubject("Certificate");
      mail.setText("Dear " + student.getFirstName() + " " + student.getLastName() + "!\n\n"
          + "Congratulations on successfully completing the course!\n"
          + "Course title: " + course.getTitle()
          + "\n"
          + "Course start date: " + course.getStartDate()
          + "\n"
          + "Course end date: " + course.getEndDate()
          + "\n"
          + "Course duration in hours: " + course.getDurationInHours()
          + "\n");

      javaMailSender.send(mail);
    }

  }

  private Set<Student> checkStudents(Course course) {
    Set<Student> students = course.getStudents();
    if (students.isEmpty()) {
      throw new EntityNotFoundException(
          "Could not find any of students associated with the course. Course id: "
              + course.getId());
    }
    return students;
  }

  private Student getStudent(Long studentId) {
    return studentRepository.findById(studentId)
        .orElseThrow(() -> new EntityNotFoundException("Student with id: " + studentId
            + " cannot be found."));
  }

  private Course getCourse(Long courseId) {
    return courseRepository.findById(courseId).orElseThrow(
        () -> new EntityNotFoundException("Course with id: " + courseId + " cannot be found."));
  }
}
