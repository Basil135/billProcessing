package org.basil.test.model;

import lombok.Data;

@Data
public class Account {

    private final String id;
    private Integer  deposit = 0;
}
