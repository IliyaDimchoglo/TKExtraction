package com.tkextraction.domain;

import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "user_entity")
@EqualsAndHashCode(callSuper = false)
public class UserEntity extends BaseUpdatedEntity {

    @Column(nullable = false, unique = true)
    private String userName;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "user")
    private CVEntity cv = new CVEntity();

    public UserEntity(String userName) {
        this.userName = userName;
        this.cv = new CVEntity(this);
    }
}
