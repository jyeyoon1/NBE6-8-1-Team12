package com.caffe.global.jpa.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.Objects;

@MappedSuperclass //상속하는 클래스가 아래 필드를 컬럼으로 인식
@EntityListeners(AuditingEntityListener.class) //auditing 기능을 엔티티에 적용
@Getter
public abstract class BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter(AccessLevel.PROTECTED)
    private int id;

    @CreatedDate
    private LocalDateTime createDate;

    @LastModifiedDate
    private LocalDateTime modifyDate;

    /*
        객체의 메모리 주소값을 비교하여
        논리적으로 같다면 같은 데이터로 취급하도록 정의
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BaseEntity that = (BaseEntity) o;
        return id == that.id;
    }

    /*
        equals()가 true라면 동일한 해시코드를 반환하도록 정의
     */
    @Override
    public int hashCode() { return Objects.hashCode(id); }

}
