package com.greenfoxacademy.vocseikatimasterwork.services;

public interface MailSenderService {

  void sendEmailToStudentAfterRegistration(Long courseId, Long studentId);

  void sendEmailToStudentAfterDeletedFromCourse(Long courseId, Long studentId);

  void sendEmailToStudentsAfterCompletionTheCourse(Long courseId);
}
