package com.greenfoxacademy.vocseikatimasterwork.aspests;

import com.greenfoxacademy.vocseikatimasterwork.services.MailSenderService;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class MailNotifierAspect {

  @Value("${spring.mail.username}")
  private String messageFrom;

  private final MailSenderService mailSenderService;

  public MailNotifierAspect(MailSenderService mailSenderService) {
    this.mailSenderService = mailSenderService;
  }

  @AfterReturning(
      value = "@annotation(com.greenfoxacademy.vocseikatimasterwork.aspests.CourseOperation)",
      returning = "joinPoint")
  public void sendEmail(JoinPoint joinPoint) {
    MethodSignature signature = (MethodSignature) joinPoint.getSignature();
    Object[] args = joinPoint.getArgs();

    if (signature.getMethod().getName().equals("registerStudentToCourse")) {
      Long courseId = (Long) args[0];
      Long studentId = (Long) args[1];
      mailSenderService.sendEmailToStudentAfterRegistration(courseId, studentId);
    }
    if (signature.getMethod().getName().equals("deleteStudentFromCourse")) {
      Long courseId = (Long) args[0];
      Long studentId = (Long) args[1];
      mailSenderService.sendEmailToStudentAfterDeletedFromCourse(courseId, studentId);
    }
    if (signature.getMethod().getName().equals("setCompleteCourseAndMakeCertificates")) {
      Long courseId = (Long) args[0];
      mailSenderService.sendEmailToStudentsAfterCompletionTheCourse(courseId);
    }
  }

}
