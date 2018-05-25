package org.basil.test.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * This class describes an account.
 *
 * @author Kutsykh Vasily (mailto:basil135@mail.ru)
 * @version $Id$
 * @since 22.05.2018
 */
@Entity
@Table
public class Account {

    /**
     * parameter id number of the account.
     */
    @Id
    private String id;
    /**
     * parameter deposit as how much money at the account.
     */
    private Integer  deposit = 0;

    /**
     * method set id to the account.
     *
     * @param id is id of the account
     */
    public void setId(String id) {
        this.id = id;
    }
    /**
     * method return id number of the account.
     *
     * @return id number
     */
    public String getId() {
        return id;
    }

    /**
     * method return how much money at the account.
     *
     * @return integer number
     */
    public Integer getDeposit() {
        return deposit;
    }

    /**
     * method set money to the account.
     *
     * @param deposit is how much money
     */
    public void setDeposit(Integer deposit) {
        this.deposit = deposit;
    }
}
