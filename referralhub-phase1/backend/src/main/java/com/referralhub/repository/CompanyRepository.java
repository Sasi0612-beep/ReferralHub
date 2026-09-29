package com.referralhub.repository;

import com.referralhub.model.Company;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.*;
import java.util.List;

public interface CompanyRepository extends JpaRepository<Company, Long> {

    Optional<Company> findByNameIgnoreCase(String name);
}
