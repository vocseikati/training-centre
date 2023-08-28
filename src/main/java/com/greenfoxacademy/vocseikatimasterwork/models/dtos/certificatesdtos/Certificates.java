package com.greenfoxacademy.vocseikatimasterwork.models.dtos.certificatesdtos;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@ApiModel(description = "List of certificates.")
public class Certificates {

  @ApiModelProperty(value = "List of certificates.")
  private Set<CertificateDto> certificates;
}
