package com.srpinfotec.cvslog.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.NaturalId;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "CVS_USER")
public class User extends BaseTime {
    @Id @GeneratedValue
    @Column(name = "USER_ID")
    private Long id;

    @NaturalId
    @Column(name = "USER_NAME", unique = true)
    private String name;

    public User(String name) {
        this.name = name;
    }
}
