package org.basil.test.controller;

import org.basil.test.model.Account;
import org.basil.test.model.ResponseDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    private final static String regex = "^[0-9]{5}$";

    /**
     * method describes http get request of rest API to get balance of the account.
     *
     * @param id of the account
     * @return http response and balance of the account if it exist
     */
    @GetMapping("{id}/balance")
    public ResponseEntity<ResponseDto> getBill(
            final @PathVariable("id") String id
    ) {
        ResponseDto dto = new ResponseDto();
        if (id == null || !id.matches(regex)) {
            dto.setMsg("The account number should consist of five digits!");
            return ResponseEntity.badRequest().body(dto);
        }

        if (!accountRepository.existsById(id)) {
            dto.setMsg(String.format("There is no account with - %s id", id));
            return ResponseEntity.badRequest().body(dto);
        }
        Account account = accountRepository.getOne(id);
        dto.setId(account.getId());
        dto.setDeposit(account.getDeposit());
        dto.setMsg("ok");

        return ResponseEntity.ok(dto);
    }

    /**
     * method describes post http request to create an account.
     *
     * @param id of the account
     * @return http response
     */
    @PostMapping("{id}")
    public ResponseEntity<ResponseDto> addBill(
            final @PathVariable("id") String id
    ) {
        ResponseDto dto = new ResponseDto();
        if (id == null || !id.matches(regex)) {
            dto.setMsg("The account number should consist of five digits!");
            return ResponseEntity.badRequest().body(dto);
        }

        if (accountRepository.existsById(id)) {
            dto.setMsg(String.format("The account %s is already exist!", id));
            return ResponseEntity.badRequest().body(dto);
        }

        Account account = new Account();
        account.setId(id);

        accountRepository.save(account);
        dto.setId(account.getId());
        dto.setDeposit(account.getDeposit());
        dto.setMsg("ok");

        return ResponseEntity.ok(dto);
    }

    /**
     * method describes http put request to withdraw the money from the account.
     *
     * @param id of the account
     * @param strCountMoney count of money as String
     * @return http response
     */
    @PutMapping("{id}/withdraw")
    public ResponseEntity<ResponseDto> withdraw(
            final @PathVariable("id") String id,
            final @RequestParam("money") List<String> strCountMoney
    ) {
        ResponseDto dto = new ResponseDto();
        if (id == null || !id.matches(regex)) {
            dto.setMsg("The account number should consist of five digits!");
            return ResponseEntity.badRequest().body(dto);
        }

        String money = strCountMoney.get(0);

        Integer countMoney;
        try {
            countMoney = Integer.parseInt(money);
        } catch (NumberFormatException e) {
            dto.setMsg("The count of money should be integer!");
            return ResponseEntity.badRequest().body(dto);
        }

        if (countMoney < 0) {
            dto.setMsg("The count of money should be greater then zero!");
            return ResponseEntity.badRequest().body(dto);
        }


        if (!accountRepository.existsById(id)) {
            dto.setMsg(String.format("There is no account with - %s id", id));
            return ResponseEntity.badRequest().body(dto);
        }

        Account account = accountRepository.getOne(id);
        Integer balance = account.getDeposit() - countMoney;

        if (balance < 0) {
            dto.setMsg("Not enough money at the account!");
            return ResponseEntity.badRequest().body(dto);
        }

        account.setDeposit(balance);

        accountRepository.save(account);

        dto.setId(account.getId());
        dto.setDeposit(account.getDeposit());
        dto.setMsg("ok");
        return ResponseEntity.ok(dto);
    }

    /**
     * method describes http put request to deposit the money to the account.
     *
     * @param id of the account
     * @param strCountMoney count of money as String
     * @return http response
     */
    @PutMapping("{id}/deposit")
    public ResponseEntity<ResponseDto> deposit(
            final @PathVariable("id") String id,
            final @RequestParam("money") List<String> strCountMoney
    ) {
        ResponseDto dto = new ResponseDto();
        if (id == null || !id.matches(regex)) {
            dto.setMsg("The account number should consist of five digits!");
            return ResponseEntity.badRequest().body(dto);
        }

        String money = strCountMoney.get(0);

        Integer countMoney;
        try {
            countMoney = Integer.parseInt(money);
        } catch (NumberFormatException e) {
            dto.setMsg("The count of money should be integer!");
            return ResponseEntity.badRequest().body(dto);
        }

        if (countMoney < 0) {
            dto.setMsg("The count of money should be greater then zero!");
            return ResponseEntity.badRequest().body(dto);
        }


        if (!accountRepository.existsById(id)) {
            dto.setMsg(String.format("There is no account with - %s id", id));
            return ResponseEntity.badRequest().body(dto);
        }

        Account account = accountRepository.getOne(id);
        Integer balance = account.getDeposit() + countMoney;
        account.setDeposit(balance);

        accountRepository.save(account);

        dto.setId(account.getId());
        dto.setDeposit(account.getDeposit());
        dto.setMsg("ok");
        return ResponseEntity.ok(dto);
    }
}