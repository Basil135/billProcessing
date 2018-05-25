package org.basil.test.controller;

import org.basil.test.model.Account;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * This class describes simple rest API.
 *
 * @author Kutsykh Vasily (mailto:basil135@mail.ru)
 * @version $Id$
 * @since 22.05.2018
 */
@RestController
@RequestMapping("/bankaccount")
public class BankAccount {

    /**
     * parameter is crud object of db.
     */
    @Autowired
    private AccountRepository accountRepository;
    /**
     * parameter regex inspect the input id that contains only five digits.
     */
    private static String regex = "^[0-9]{5}$";

    /**
     * method describes http get request of rest API to get balance of the account.
     *
     * @param id of the account
     * @return http response and balance of the account if it exist
     */
    @GetMapping("{id}/balance")
    public ResponseEntity getBill(
            final @PathVariable("id") String id
    ) {
        if (id == null || !id.matches(regex)) {
            return ResponseEntity.badRequest().body("The account number should consist of five digits!");
        }

        if (accountRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        Account account = accountRepository.getOne(id);

        return ResponseEntity.ok(account);
    }

    /**
     * method describes post http request to create an account.
     *
     * @param id of the account
     * @return http response
     */
    @PostMapping("{id}")
    public ResponseEntity addBill(
            final @PathVariable("id") String id
    ) {
        if (!id.matches(regex)) {
            return ResponseEntity.badRequest().body("The bill number should consist of five digits!");
        }

        if (accountRepository.existsById(id)) {
            return ResponseEntity.badRequest().body("The bill is already exist!");
        }

        Account account = new Account();
        account.setId(id);

        accountRepository.save(account);

        return ResponseEntity.ok(account);
    }

    /**
     * method describes http put request to withdraw the money from the account.
     *
     * @param id of the account
     * @param strCountMoney count of money as String
     * @return http response
     */
    @PutMapping("{id}/withdraw")
    public ResponseEntity withdraw(
            final @PathVariable("id") String id,
            final @RequestParam("money") String strCountMoney
    ) {
        if (!id.matches(regex)) {
            return ResponseEntity.badRequest().body("The account number should consist of five digits!");
        }

        String money = strCountMoney;

        Integer countMoney;
        try {
            countMoney = Integer.parseInt(money);
        } catch (ClassCastException e) {
            return ResponseEntity.badRequest().body("The count of money should be integer!");
        }

        if (countMoney < 0) {
            return ResponseEntity.badRequest().body("The count of money should be greater then zero!");
        }


        if (accountRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }

        Account account = accountRepository.getOne(id);
        Integer balance = account.getDeposit() - countMoney;

        if (balance < 0) {
            return ResponseEntity.badRequest().body("Not enough money at the account!");
        }

        account.setDeposit(balance);

        accountRepository.save(account);

        return ResponseEntity.ok(account);
    }

    /**
     * method describes http put request to deposit the money to the account.
     *
     * @param id of the account
     * @param strCountMoney count of money as String
     * @return http response
     */
    @PutMapping("{id}/deposit")
    public ResponseEntity deposit(
            final @PathVariable("id") String id,
            final @RequestParam("money") String strCountMoney
    ) {
        if (!id.matches(regex)) {
            return ResponseEntity.badRequest().body("The account number should consist of five digits!");
        }

        String money = strCountMoney;

        Integer countMoney;
        try {
            countMoney = Integer.parseInt(money);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("The count of money should be integer!");
        }

        if (countMoney < 0) {
            return ResponseEntity.badRequest().body("The count of money should be greater then zero!");
        }


        if (accountRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }

        Account account = accountRepository.getOne(id);
        Integer balance = account.getDeposit() + countMoney;
        account.setDeposit(balance);

        accountRepository.save(account);

        return ResponseEntity.ok(account);
    }
}
