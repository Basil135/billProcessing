package org.basil.test.model;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Entity
@Table
public class Account {

    @Id
    private final String id;
    @Column
    private Integer  deposit = 0;
}
