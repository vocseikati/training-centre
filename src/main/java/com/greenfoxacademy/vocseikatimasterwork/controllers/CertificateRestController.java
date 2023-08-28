package com.greenfoxacademy.vocseikatimasterwork.controllers;

import static com.greenfoxacademy.vocseikatimasterwork.services.ValidationService.isNumeric;
import static com.greenfoxacademy.vocseikatimasterwork.services.ValidationService.requireNonNull;
import static com.greenfoxacademy.vocseikatimasterwork.services.ValidationService.validateBindingResult;

import com.greenfoxacademy.vocseikatimasterwork.models.dtos.certificatesdtos.CertificateDto;
import com.greenfoxacademy.vocseikatimasterwork.services.CertificateService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import java.util.List;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/ohaj")
public class CertificateRestController {

  private final CertificateService certificateService;

  @Autowired
  public CertificateRestController(CertificateService certificateService) {
    this.certificateService = certificateService;
  }

  @GetMapping("/certificates")
  @ApiOperation(value = "Lists all certificates.")
  public ResponseEntity<List<CertificateDto>> listAllCertificates(
      @ApiParam(value = "Id of the student whose certificates you want to list.", required = true,
          type = "Integer as string")
      @RequestParam(required = false) String studentId
  ) {

    if (studentId == null || studentId.isEmpty()) {
      return ResponseEntity.ok().body(certificateService.getAllCertificates(null));
    }

    return ResponseEntity.ok().body(certificateService.getAllCertificates(isNumeric(studentId)));
  }

  @GetMapping("/certificates/{id}")
  @ApiOperation(value = "Fetches and returns a certificate by id.")
  public ResponseEntity<CertificateDto> getCertificateById(
      @ApiParam(value = "Id of the required certificate.", required = true,
          type = "Integer as string")
      @PathVariable String id
  ) {
    requireNonNull(id, "Id must not be null.");
    return ResponseEntity.ok()
        .body(certificateService.getCertificateById(isNumeric(id)));
  }

  @PutMapping("/certificates/{id}")
  @ApiOperation(value = "Updates an existing certificate in the database.")
  public ResponseEntity<CertificateDto> modifyCertificateById(
      @ApiParam(value = "Id of the certificate you want to update.", required = true,
          type = "Integer as string")
      @PathVariable String id,

      @ApiParam(value = "The certificate object that you want to update the original with",
          required = true)
      @RequestBody(required = false) @Valid CertificateDto certificate,
      BindingResult bindingResult
  ) {
    requireNonNull(id, "Id must not be null.");
    requireNonNull(certificate, "Certificate must not be null.");
    validateBindingResult(bindingResult);

    certificateService.modifyCertificate(certificate, isNumeric(id));
    return ResponseEntity.noContent().build();
  }

  @DeleteMapping("/certificates/{id}")
  @ApiOperation(value = "Deletes a certificate from the database.")
  public ResponseEntity<CertificateDto> deleteCertificate(
      @ApiParam(value = "Id of the certificate that you want to delete.", required = true,
          type = "Integer as string")
      @PathVariable String id
  ) {
    requireNonNull(id, "Id must not be null.");
    return ResponseEntity.ok().body(certificateService.deleteCertificate(isNumeric(id)));
  }
}
