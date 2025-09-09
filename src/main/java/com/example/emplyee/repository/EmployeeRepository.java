package com.example.emplyee.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.emplyee.model.Employee;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    @Query("SELECT e FROM Employee e WHERE " +
           "LOWER(e.employeeName) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(e.employeeId) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(e.employeeNumber) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(e.employeeEmail) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<Employee> findByKeyword(@Param("keyword") String keyword);
}
