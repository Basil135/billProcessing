package org.basil.test.model;

/**
 * This class uses for response from server.
 *
 * @author Kutsyih Vasily (mailto:basil135@mail.ru)
 * @version $Id$
 * @since 26.05.2018
 */
public class ResponseDto {

    /**
     * parameter contains text message from server.
     */
    private String msg;
    /**
     * parameter contains id of the account.
     */
    private String id;
    /**
     * parameter contains deposit of the account.
     */
    private Integer deposit;

    /**
     * method return message msg.
     *
     * @return msg
     */
    public String getMsg() {
        return msg;
    }

    /**
     * method set message msg
     *
     * @param msg message
     */
    public void setMsg(String msg) {
        this.msg = msg;
    }

    /**
     * method return id of the account.
     *
     * @return id of the account
     */
    public String getId() {
        return id;
    }

    /**
     * method set id of the account.
     *
     * @param id id of the account
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * method return deposit of the account.
     *
     * @return deposit of the account
     */
    public Integer getDeposit() {
        return deposit;
    }

    /**
     * method set deposit of the account.
     *
     * @param deposit of the account
     */
    public void setDeposit(Integer deposit) {
        this.deposit = deposit;
    }
}
