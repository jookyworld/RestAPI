package com.back.global.jpa.entity;


import jakarta.persistence.*;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@EntityListeners(AuditingEntityListener.class)  // JPA Auditing 기능 포함
@MappedSuperclass   // JPA Entity 클래스들이 BaseEntity를 상속할 경우 필드들도 컬럼으로 인식하도록 함
@Getter
public class BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 기본 키 생성을 데이터베이스에 위임
    private long id;

    @CreatedDate
    private LocalDateTime createDate;

    @LastModifiedBy
    private LocalDateTime modifyDate;
}
