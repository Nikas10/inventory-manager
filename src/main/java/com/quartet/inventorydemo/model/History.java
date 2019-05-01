package com.quartet.inventorydemo.model;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import javax.persistence.Version;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class History {

  @Column(name = "jpa_auditing_created_date", nullable = false, updatable = false)
  @CreatedDate
  private long createdDate;

  @Column(name = "jpa_auditing_record_modified_date")
  @LastModifiedDate
  private long modifiedDate;

  @Column(name = "jpa_auditing_created_by")
  @CreatedBy
  private String createdBy;

  @Column(name = "jpa_auditing_modified_by")
  @LastModifiedBy
  private String modifiedBy;

  @Version
  private Integer version;
}
