package org.basil.test.service;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.DefaultMockMvcBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

/**
 * This class contains methods to test the BankAccount class.
 *
 * @author Kutsykh Vasily (mailto:basil135@mail.ru)
 * @version $Id$
 * @since 22.05.2018
 */
@RunWith(SpringJUnit4ClassRunner.class)
@AutoConfigureWebTestClient
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class BankAccountTest {

    /**
     * parameter describes the start URI.
     */
    private static final String URI = "/bankaccount/%s";
    /**
     * parameter describes cut account id.
     */
    private final String account = "0000";
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

    @Autowired
    private WebApplicationContext wac;
    private MockMvc mockMvc;

    @Before
    public void setup () {
        DefaultMockMvcBuilder builder = MockMvcBuilders.webAppContextSetup(this.wac);
        this.mockMvc = builder.build();
    }

    /**
     * method to test positive scenario (
     *     1) create an account
     *     2) deposit the money to it
     *     3) withdraw the money from it
     *     4) check the deposit
     * ).
     */
    @Test
    public void whenAddAnAccountAndDepositItAndWithdrawItThenGetIsCorrect() throws Exception {

        MockHttpServletRequestBuilder builder =
                MockMvcRequestBuilders.post(String.format(URI, account + "4"))
                        .contentType(MediaType.APPLICATION_JSON);

        this.mockMvc.perform(builder)
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk());

        builder =
                MockMvcRequestBuilders.put(String.format(URI, account + "4/deposit"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(String.format(body, balance));

        this.mockMvc.perform(builder)
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk());

        builder =
                MockMvcRequestBuilders.put(String.format(URI, account + "4/withdraw"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(String.format(body, balance));

        this.mockMvc.perform(builder)
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk());

        builder =
                MockMvcRequestBuilders.get(String.format(URI, account + "4/balance"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(String.format(body, balance));

        this.mockMvc.perform(builder)
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.content().json(String.valueOf(balance - withdraw)));
    }

//    /**
//     * method to test negative scenario (
//     *     1) create an account
//     *     2) create equal account
//     * ).
//     */
//    @Test
//    public void whenAddTwoEqualsAccountThenError() {
//
//        ResponseEntity rs = target(String.format(URI, account + "5")).request().post(Entity.json("{}"));
//
//        assertThat(goodStatus, is(rs.getStatusCodeValue()));
//
//        rs = target(String.format(URI, account + "5")).request().post(Entity.json("{}"));
//
//        assertThat(goodStatus, not(rs.getStatusCodeValue()));
//    }
//
//    /**
//     * method to test negative scenario as withdraw the money (
//     *     1) create an account
//     *     3) withdraw the money from it
//     * ).
//     */
//    @Test
//    public void whenWithdrawMoneyGreaterThenAccountHaveThenError() {
//
//        ResponseEntity rs = target(String.format(URI, account + "6")).request().post(Entity.json("{}"));
//
//        assertThat(goodStatus, is(rs.getStatusCodeValue()));
//
//        rs = target(String.format(URI, account + "6/withdraw")).request().put(Entity.json("{money: 10}"));
//
//        assertThat(goodStatus, not(rs.getStatusCodeValue()));
//    }
//
//    /**
//     * method to test negative scenario as withdraw the money (
//     *     1) create an account
//     *     2) deposit to an account
//     *     3) withdraw the money from it
//     * ).
//     */
//    @Test
//    public void whenWithdrawOrDepositNegativeNumberThenError() {
//
//        ResponseEntity rs = target(String.format(URI, account + "7")).request().post(Entity.json("{}"));
//
//        assertThat(goodStatus, is(rs.getStatusCodeValue()));
//
//        rs = target(String.format(URI, account + "7/deposit")).request().put(Entity.json(String.format(body, balance)));
//
//        assertThat(goodStatus, is(rs.getStatusCodeValue()));
//
//        rs = target(String.format(URI, account + "7/deposit")).request().put(Entity.json(String.format(body, - balance)));
//
//        assertThat(goodStatus, not(rs.getStatusCodeValue()));
//
//        rs = target(String.format(URI, account + "7/withdraw")).request().put(Entity.json(String.format(body, - withdraw)));
//
//        assertThat(goodStatus, not(rs.getStatusCodeValue()));
//
//        rs = target(String.format(URI, account + "7/balance")).request().get();
//
//        assertThat(goodStatus, is(rs.getStatusCodeValue()));
//        assertThat(String.valueOf(balance), is(rs.readEntity(String.class)));
//    }
//
//    /**
//     * method to test negative scenario as withdraw and deposit the money (
//     *     1) deposit to an account
//     *     3) withdraw the money from it
//     * ).
//     */
//    @Test
//    public void whenDepositOrWithdrawOfNotExistingAccountThenError() {
//
//        ResponseEntity rs = target(String.format(URI, account + "9/deposit")).request().put(Entity.json(String.format(body, balance)));
//
//        assertThat(goodStatus, not(rs.getStatusCodeValue()));
//
//        rs = target(String.format(URI, account + "8/withdraw")).request().put(Entity.json(String.format(body, withdraw)));
//
//        assertThat(goodStatus, not(rs.getStatusCodeValue()));
//    }
//
//    /**
//     * method to test positive scenario (
//     *     1) create an account
//     * ).
//     */
//    @Test
//    public void whenAddAccountWithBadFormatIdThenError() {
//
//        ResponseEntity rs = target(String.format(URI, account + "77")).request().post(Entity.json("{}"));
//
//        assertThat(goodStatus, not(rs.getStatusCodeValue()));
//
//        rs = target(String.format(URI, account)).request().post(Entity.json("{}"));
//
//        assertThat(goodStatus, not(rs.getStatusCodeValue()));
//    }
//
//    /**
//     * method to test positive scenario (
//     *     1) create an account
//     *     2) deposit the money to it
//     *     3) withdraw the money from it
//     * ).
//     */
//    @Test
//    public void whenDepositOrWithdrawInBadFormatThenError() {
//
//        ResponseEntity rs = target(String.format(URI, account + "1")).request().post(Entity.json("{}"));
//
//        assertThat(goodStatus, is(rs.getStatusCodeValue()));
//
//        rs = target(String.format(URI, account + "1/deposit")).request().put(Entity.json(String.format(body, "wdas")));
//
//        assertThat(goodStatus, not(rs.getStatusCodeValue()));
//
//        rs = target(String.format(URI, account + "1/withdraw")).request().put(Entity.json(String.format(body, "qsffv")));
//
//        assertThat(goodStatus, not(rs.getStatusCodeValue()));
//    }
}
