package org.basil.test.service;

import org.basil.test.controller.AccountRepository;
import org.basil.test.model.Account;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.DefaultMockMvcBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import java.net.URI;

import static java.lang.String.format;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

/**
 * This class describes test of the rest api.
 *
 * @author Kutsyih Vasily (mailto:basil135@mail.ru)
 * @version $Id$
 * @since 26.05.2018
 */
@RunWith(SpringJUnit4ClassRunner.class)
@AutoConfigureWebTestClient
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class BankAccountPositiveTest {

    private final String accountId = "00001";
    private final String URI = "/bankaccount/%s";
    private final String param = "money";
    private final String content = "{'msg':'ok','id':'%s','deposit':%s}";

    private final String messageErrorId = "{'msg':'The account number should consist of five digits!','id':null,'deposit':null}";
    private final String messageErrorNegativeNumber = "{'msg':'The count of money should be greater then zero!','id':null,'deposit':null}";
    private final String messageErrorFormatNumber = "{'msg':'The count of money should be integer!','id':null,'deposit':null}";

    private final int startDeposit = 10;
    private final int deposit = 5;
    private final int withdraw = 3;

    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private WebApplicationContext wac;
    private MockMvc mockMvc;

    @Before
    public void init() {
        Account testAccount = new Account();
        testAccount.setId(accountId);
        testAccount.setDeposit(startDeposit);

        DefaultMockMvcBuilder builder = MockMvcBuilders.webAppContextSetup(this.wac);
        this.mockMvc = builder.build();

        accountRepository.save(testAccount);
    }

    @After
    public void end() {
        accountRepository.deleteById(accountId);
    }

    @Test
    @Transactional
    @Rollback
    public void whenAddTheAccountThenCorrect() throws Exception {

        String accountIdToAdd = "00002";

        this.mockMvc.perform(post(new URI(format(URI, accountIdToAdd)))
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(format(content, accountIdToAdd, "0")));
    }

    @Test
    @Transactional
    @Rollback
    public void whenAddAlreadyExistAccountThenError() throws Exception {

        final String messageErrorIdAlreadyExist = "{'msg':'The account %s is already exist!','id':null,'deposit':null}";

        this.mockMvc.perform(post(format(URI, accountId))
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().is4xxClientError())
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.content().json(format(messageErrorIdAlreadyExist, accountId)));
    }

    @Test
    @Transactional
    @Rollback
    public void whenAddGreaterFiveDigitsIdThenError() throws Exception {

        final String errorAccountId = "123456";

        this.mockMvc.perform(post(format(URI, errorAccountId))
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().is4xxClientError())
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.content().json(messageErrorId));
    }

    @Test
    @Transactional
    @Rollback
    public void whenAddLessFiveDigitsIdThenError() throws Exception {

        final String errorAccountId = "1234";

        this.mockMvc.perform(post(format(URI, errorAccountId))
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().is4xxClientError())
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.content().json(messageErrorId));
    }

    @Test
    @Transactional
    @Rollback
    public void whenAddNonOnlyFiveDigitsIdThenError() throws Exception {

        final String errorAccountId = "12g34";

        this.mockMvc.perform(post(format(URI, errorAccountId))
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().is4xxClientError())
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.content().json(messageErrorId));
    }

    @Test
    @Transactional
    @Rollback
    public void whenPutTheAccountDepositThenCorrect() throws Exception {

        this.mockMvc.perform(put(new URI(format(URI, accountId + "/deposit")))
                .contentType(MediaType.APPLICATION_JSON)
                .param(param, String.valueOf(deposit)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(format(content, accountId, String.valueOf(startDeposit + deposit))));
    }

    @Test
    @Transactional
    @Rollback
    public void whenPutNegativeDepositThenError() throws Exception {

        this.mockMvc.perform(put(format(URI, accountId + "/deposit"))
                .contentType(MediaType.APPLICATION_JSON)
                .param(param, String.valueOf(-deposit)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().is4xxClientError())
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.content().json(messageErrorNegativeNumber));
    }

    @Test
    @Transactional
    @Rollback
    public void whenPutDepositWithNonDigitsThenError() throws Exception {

        this.mockMvc.perform(put(format(URI, accountId + "/deposit"))
                .contentType(MediaType.APPLICATION_JSON)
                .param(param, "1k2"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().is4xxClientError())
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.content().json(messageErrorFormatNumber));
    }

    @Test
    @Transactional
    @Rollback
    public void whenPutDepositGreaterFiveDigitsIdThenError() throws Exception {

        final String errorAccountId = "123456";

        this.mockMvc.perform(put(format(URI, errorAccountId + "/deposit"))
                .contentType(MediaType.APPLICATION_JSON)
                .param(param, String.valueOf(deposit)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().is4xxClientError())
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.content().json(messageErrorId));
    }

    @Test
    @Transactional
    @Rollback
    public void whenPutDepositLessFiveDigitsIdThenError() throws Exception {

        final String errorAccountId = "1234";

        this.mockMvc.perform(put(format(URI, errorAccountId + "/deposit"))
                .contentType(MediaType.APPLICATION_JSON)
                .param(param, String.valueOf(deposit)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().is4xxClientError())
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.content().json(messageErrorId));
    }

    @Test
    @Transactional
    @Rollback
    public void whenPutDepositNonOnlyFiveDigitsIdThenError() throws Exception {

        final String errorAccountId = "12g34";

        this.mockMvc.perform(put(format(URI, errorAccountId + "/deposit"))
                .contentType(MediaType.APPLICATION_JSON)
                .param(param, String.valueOf(deposit)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().is4xxClientError())
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.content().json(messageErrorId));
    }

    @Test
    @Transactional
    @Rollback
    public void whenPutTheAccountWithdrawThenCorrect() throws Exception {

        this.mockMvc.perform(put(new URI(format(URI, accountId + "/withdraw")))
                .contentType(MediaType.APPLICATION_JSON)
                .param(param, String.valueOf(withdraw)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(format(content, accountId, String.valueOf(startDeposit - withdraw))));
    }

    @Test
    @Transactional
    @Rollback
    public void whenPutNegativeWithdrawThenError() throws Exception {

        this.mockMvc.perform(put(format(URI, accountId + "/withdraw"))
                .contentType(MediaType.APPLICATION_JSON)
                .param(param, String.valueOf(-withdraw)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().is4xxClientError())
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.content().json(messageErrorNegativeNumber));
    }

    @Test
    @Transactional
    @Rollback
    public void whenPutWithdrawWithNonDigitsThenError() throws Exception {

        this.mockMvc.perform(put(format(URI, accountId + "/withdraw"))
                .contentType(MediaType.APPLICATION_JSON)
                .param(param, "1k2"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().is4xxClientError())
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.content().json(messageErrorFormatNumber));
    }

    @Test
    @Transactional
    @Rollback
    public void whenPutWithdrawGreaterFiveDigitsIdThenError() throws Exception {

        final String errorAccountId = "123456";

        this.mockMvc.perform(put(format(URI, errorAccountId + "/withdraw"))
                .contentType(MediaType.APPLICATION_JSON)
                .param(param, String.valueOf(withdraw)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().is4xxClientError())
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.content().json(messageErrorId));
    }

    @Test
    @Transactional
    @Rollback
    public void whenPutWithdrawLessFiveDigitsIdThenError() throws Exception {

        final String errorAccountId = "1234";

        this.mockMvc.perform(put(format(URI, errorAccountId + "/withdraw"))
                .contentType(MediaType.APPLICATION_JSON)
                .param(param, String.valueOf(withdraw)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().is4xxClientError())
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.content().json(messageErrorId));
    }

    @Test
    @Transactional
    @Rollback
    public void whenPutWithdrawNonOnlyFiveDigitsIdThenError() throws Exception {

        final String errorAccountId = "12g34";

        this.mockMvc.perform(put(format(URI, errorAccountId + "/withdraw"))
                .contentType(MediaType.APPLICATION_JSON)
                .param(param, String.valueOf(withdraw)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().is4xxClientError())
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.content().json(messageErrorId));
    }

    @Test
    @Transactional
    @Rollback
    public void whenGetTheAccountThenSuccess() throws Exception {
        this.mockMvc.perform(get(format(URI, accountId + "/balance"))
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.content().json(format(content, accountId, String.valueOf(startDeposit))));
    }

    @Test
    @Transactional
    @Rollback
    public void whenGetNonexistentAccountThenError() throws Exception {

        final String nonexistentAccountId = "12345";
        final String messageErrorNonexistentId = "{'msg':'There is no account with - %s id','id':null,'deposit':null}";

        this.mockMvc.perform(get(format(URI, nonexistentAccountId + "/balance"))
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().is4xxClientError())
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.content().json(format(messageErrorNonexistentId, nonexistentAccountId)));
    }

    @Test
    @Transactional
    @Rollback
    public void whenGetGreaterFiveDigitsIdThenError() throws Exception {

        final String errorAccountId = "123456";

        this.mockMvc.perform(get(format(URI, errorAccountId + "/balance"))
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().is4xxClientError())
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.content().json(messageErrorId));
    }

    @Test
    @Transactional
    @Rollback
    public void whenGetLessFiveDigitsIdThenError() throws Exception {

        final String errorAccountId = "1234";

        this.mockMvc.perform(get(format(URI, errorAccountId + "/balance"))
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().is4xxClientError())
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.content().json(messageErrorId));
    }

    @Test
    @Transactional
    @Rollback
    public void whenGetNonOnlyFiveDigitsIdThenError() throws Exception {

        final String errorAccountId = "12g34";

        this.mockMvc.perform(get(format(URI, errorAccountId + "/balance"))
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().is4xxClientError())
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.content().json(messageErrorId));
    }
}