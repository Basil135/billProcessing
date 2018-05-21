package org.basil.test.controller;

import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTest;
import org.junit.Test;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.Response;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNot.not;
import static org.junit.Assert.*;

/**
 * This class contains methods to test the BankAccount class.
 *
 * @author Kutsykh Vasily (mailto:basil135@mail.ru)
 * @version $Id$
 * @since 22.05.2018
 */
public class BankAccountTest extends JerseyTest {

    /**
     * parameter describes the start URI.
     */
    private static final String URI = "bankaccount/%s";
    /**
     * parameter describes cut account id.
     */
    private final String account = "0000";
    /**
     * parameter describes success status of http response.
     */
    private final int goodStatus = 200;
    /**
     * parameter describes body of the request.
     */
    private final String body = "{money: %s}";
    /**
     * parameter balance to the account,
     */
    private final int balance = 3000;
    /**
     * parameter withdraw from the account;
     */
    private final int withdraw = 1280;

    /**
     * method to test positive scenario (
     *     1) create an account
     *     2) deposit the money to it
     *     3) withdraw the money from it
     *     4) check the deposit
     * ).
     */
    @Test
    public void whenAddAnAccountAndDepositItAndWithdrawItThenGetIsCorrect() {

        Response rs = target(String.format(URI, account + "4")).request().post(Entity.json("{}"));

        assertThat(goodStatus, is(rs.getStatus()));

        rs = target(String.format(URI, account + "4/deposit")).request().put(Entity.json(String.format(body, balance)));

        assertThat(goodStatus, is(rs.getStatus()));

        rs = target(String.format(URI, account + "4/withdraw")).request().put(Entity.json(String.format(body, withdraw)));

        assertThat(goodStatus, is(rs.getStatus()));

        rs = target(String.format(URI, account + "4/balance")).request().get();

        assertThat(goodStatus, is(rs.getStatus()));
        assertThat(String.valueOf(balance - withdraw), is(rs.readEntity(String.class)));
    }

    /**
     * method to test negative scenario (
     *     1) create an account
     *     2) create equal account
     * ).
     */
    @Test
    public void whenAddTwoEqualsAccountThenError() {

        Response rs = target(String.format(URI, account + "5")).request().post(Entity.json("{}"));

        assertThat(goodStatus, is(rs.getStatus()));

        rs = target(String.format(URI, account + "5")).request().post(Entity.json("{}"));

        assertThat(goodStatus, not(rs.getStatus()));
    }

    /**
     * method to test negative scenario as withdraw the money (
     *     1) create an account
     *     3) withdraw the money from it
     * ).
     */
    @Test
    public void whenWithdrawMoneyGreaterThenAccountHaveThenError() {

        Response rs = target(String.format(URI, account + "6")).request().post(Entity.json("{}"));

        assertThat(goodStatus, is(rs.getStatus()));

        rs = target(String.format(URI, account + "6/withdraw")).request().put(Entity.json("{money: 10}"));

        assertThat(goodStatus, not(rs.getStatus()));
    }

    /**
     * method to test negative scenario as withdraw the money (
     *     1) create an account
     *     2) deposit to an account
     *     3) withdraw the money from it
     * ).
     */
    @Test
    public void whenWithdrawOrDepositNegativeNumberThenError() {

        Response rs = target(String.format(URI, account + "7")).request().post(Entity.json("{}"));

        assertThat(goodStatus, is(rs.getStatus()));

        rs = target(String.format(URI, account + "7/deposit")).request().put(Entity.json(String.format(body, balance)));

        assertThat(goodStatus, is(rs.getStatus()));

        rs = target(String.format(URI, account + "7/deposit")).request().put(Entity.json(String.format(body, - balance)));

        assertThat(goodStatus, not(rs.getStatus()));

        rs = target(String.format(URI, account + "7/withdraw")).request().put(Entity.json(String.format(body, - withdraw)));

        assertThat(goodStatus, not(rs.getStatus()));

        rs = target(String.format(URI, account + "7/balance")).request().get();

        assertThat(goodStatus, is(rs.getStatus()));
        assertThat(String.valueOf(balance), is(rs.readEntity(String.class)));
    }

    /**
     * method to test negative scenario as withdraw and deposit the money (
     *     1) deposit to an account
     *     3) withdraw the money from it
     * ).
     */
    @Test
    public void whenDepositOrWithdrawOfNotExistingAccountThenError() {

        Response rs = target(String.format(URI, account + "9/deposit")).request().put(Entity.json(String.format(body, balance)));

        assertThat(goodStatus, not(rs.getStatus()));

        rs = target(String.format(URI, account + "8/withdraw")).request().put(Entity.json(String.format(body, withdraw)));

        assertThat(goodStatus, not(rs.getStatus()));
    }

    /**
     * method to test positive scenario (
     *     1) create an account
     * ).
     */
    @Test
    public void whenAddAccountWithBadFormatIdThenError() {

        Response rs = target(String.format(URI, account + "77")).request().post(Entity.json("{}"));

        assertThat(goodStatus, not(rs.getStatus()));

        rs = target(String.format(URI, account)).request().post(Entity.json("{}"));

        assertThat(goodStatus, not(rs.getStatus()));
    }

    /**
     * method to test positive scenario (
     *     1) create an account
     *     2) deposit the money to it
     *     3) withdraw the money from it
     * ).
     */
    @Test
    public void whenDepositOrWithdrawInBadFormatThenError() {

        Response rs = target(String.format(URI, account + "1")).request().post(Entity.json("{}"));

        assertThat(goodStatus, is(rs.getStatus()));

        rs = target(String.format(URI, account + "1/deposit")).request().put(Entity.json(String.format(body, "wdas")));

        assertThat(goodStatus, not(rs.getStatus()));

        rs = target(String.format(URI, account + "1/withdraw")).request().put(Entity.json(String.format(body, "qsffv")));

        assertThat(goodStatus, not(rs.getStatus()));
    }

    /**
     * method return the Application.
     *
     * @return application
     */
    @Override
    protected Application configure() {
        return new ResourceConfig(BankAccount.class);
    }
}
