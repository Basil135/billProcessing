package org.basil.test.controller;

import org.basil.test.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * This class describes crud object of db.
 *
 * @author Kutsyih Vasily (mailto:basil135@mail.ru)
 * @version $Id$
 * @since 24.05.2018
 */
@Repository
public interface AccountRepository extends JpaRepository<Account, String> {
}
