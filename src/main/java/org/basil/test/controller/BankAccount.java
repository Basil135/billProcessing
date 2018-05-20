package org.basil.test.controller;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.sun.org.glassfish.external.probe.provider.annotations.ProbeParam;
import org.basil.test.model.Account;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Path("bankaccount")
public class BankAccount {

    private static final Map<String, Account> accounts = new ConcurrentHashMap<>();

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("{id}/balance")
    public Response getBill(
            final @PathParam("id") String id
    ) {
        if (id == null || !id.matches("^[0-9]{5}$")) {
            return Response.notModified("The account number should consist of five digits!").build();
        }
        Account account = accounts.get(id);
        if (account == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(String.valueOf(account.getDeposit())).build();
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Path("{id}")
    public Response addBill(
            final @PathParam("id") String id
    ) {
        if (!id.matches("^[0-9]{5}$")) {
            return Response.notModified("The bill number should consist of five digits!").build();
        }
        if (accounts.containsKey(id)) {
            return Response.notModified("The bill is already exist!").build();
        }
        accounts.put(id, new Account(id));
        return Response.ok().build();
    }

    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("{id}/withdraw")
    public Response withdraw(
            final @PathParam("id") String id,
            final @ProbeParam("withdraw") String strCountMoney
    ) {
        if (!id.matches("^[0-9]{5}$")) {
            return Response.notModified("The account number should consist of five digits!").build();
        }
        JsonObject json = new Gson().fromJson(strCountMoney, JsonObject.class);
        String money = json.get("withdraw").getAsString();
        Integer countMoney;
        try {
            countMoney = Integer.parseInt(money);
        } catch (ClassCastException e) {
            return Response.notModified("The count of money should be double!").build();
        }
        if (countMoney < 0) {
            return Response.notModified("The count of money should be greater then zero!").build();
        }
        Account account = accounts.get(id);
        if (account == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        Integer balance = account.getDeposit() - countMoney;
        if (balance < 0) {
            return Response.notModified("Not enough money at the account!").build();
        }
        account.setDeposit(balance);
        accounts.replace(id, account);
        return Response.ok().build();
    }

    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("{id}/deposit")
    public Response deposit(
            final @PathParam("id") String id,
            final @ProbeParam("deposit") String strCountMoney
    ) {
        if (!id.matches("^[0-9]{5}$")) {
            return Response.notModified("The account number should consist of five digits!").build();
        }
        JsonObject json = new Gson().fromJson(strCountMoney, JsonObject.class);
        String money = json
                .get("deposit")
                .getAsString();
        Integer countMoney;
        try {
            countMoney = Integer.parseInt(money);
        } catch (Exception e) {
            return Response.notModified("The count of money should be double!").build();
        }
        if (countMoney < 0) {
            return Response.notModified("The count of money should be greater then zero!").build();
        }
        Account account = accounts.get(id);
        if (account == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        Integer balance = account.getDeposit() + countMoney;
        account.setDeposit(balance);
        accounts.replace(id, account);
        return Response.ok().build();
    }
}
