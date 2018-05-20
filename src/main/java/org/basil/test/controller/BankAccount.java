package org.basil.test.controller;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.sun.org.glassfish.external.probe.provider.annotations.ProbeParam;
import org.basil.test.model.Account;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("bankaccount")
public class BankAccount {

    private static String regex = "^[0-9]{5}$";

    private static SessionFactory sessionFactory;
    private static Session session;

    private Session getSession() {
        if (session == null) {
            sessionFactory = getSessionFactory();
            session = sessionFactory.openSession();
        }
        return session;
    }

    private SessionFactory getSessionFactory() {
        if (sessionFactory == null) {
            sessionFactory = new Configuration()
                    .configure()
                    .addAnnotatedClass(Account.class)
                    .buildSessionFactory();
        }
        return sessionFactory;
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("{id}/balance")
    public Response getBill(
            final @PathParam("id") String id
    ) {
        if (id == null || !id.matches(regex)) {
            return Response.notModified("The account number should consist of five digits!").build();
        }

        Session session = getSession();

        Account account = session.get(Account.class, id);

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
        if (!id.matches(regex)) {
            return Response.notModified("The bill number should consist of five digits!").build();
        }

        Session session = getSession();

        Account account = session.get(Account.class, id);

        if (account != null) {
            return Response.notModified("The bill is already exist!").build();
        }

        session.save(new Account(id));

        return Response.ok().build();
    }

    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("{id}/withdraw")
    public Response withdraw(
            final @PathParam("id") String id,
            final @ProbeParam("money") String strCountMoney
    ) {
        if (!id.matches(regex)) {
            return Response.notModified("The account number should consist of five digits!").build();
        }

        JsonObject json = new Gson().fromJson(strCountMoney, JsonObject.class);
        String money = json.get("money").getAsString();

        Integer countMoney;
        try {
            countMoney = Integer.parseInt(money);
        } catch (ClassCastException e) {
            return Response.notModified("The count of money should be double!").build();
        }

        if (countMoney < 0) {
            return Response.notModified("The count of money should be greater then zero!").build();
        }

        Session session = getSession();

        Account account = session.get(Account.class, id);

        if (account == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        Integer balance = account.getDeposit() - countMoney;

        if (balance < 0) {
            return Response.notModified("Not enough money at the account!").build();
        }

        account.setDeposit(balance);

        session.saveOrUpdate(account);

        return Response.ok().build();
    }

    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("{id}/deposit")
    public Response deposit(
            final @PathParam("id") String id,
            final @ProbeParam("money") String strCountMoney
    ) {
        if (!id.matches(regex)) {
            return Response.notModified("The account number should consist of five digits!").build();
        }

        JsonObject json = new Gson().fromJson(strCountMoney, JsonObject.class);
        String money = json.get("money").getAsString();

        Integer countMoney;
        try {
            countMoney = Integer.parseInt(money);
        } catch (Exception e) {
            return Response.notModified("The count of money should be double!").build();
        }

        if (countMoney < 0) {
            return Response.notModified("The count of money should be greater then zero!").build();
        }

        Session session = getSession();

        Account account = session.get(Account.class, id);

        if (account == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        Integer balance = account.getDeposit() + countMoney;
        account.setDeposit(balance);

        session.saveOrUpdate(account);

        return Response.ok().build();
    }
}
