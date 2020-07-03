package com.example.currency_exchange.repository;

import com.example.currency_exchange.api.stored_db.Rate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface CurrencyRepository extends JpaRepository<Rate, Long > {

    List<Rate> findAllByDateBetween(LocalDate fromDate, LocalDate toDate);
}
