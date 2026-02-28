package com.capgeminispring.basicspringboot;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import jakarta.transaction.Transactional;

public interface EmployeeJpaRepository extends JpaRepository<Employee,Integer> {
		public Employee getByName(String name);
		//getBy, findBy, readBy
		
		public Employee getBySalary(double salary);
		public Employee getByNameAndSalary(String name,double salary);
		
		@Modifying
		@Transactional
		@Query(value="update employee set salary=:newsalary where salary=:oldsalary",nativeQuery=true)
		public int updateBySalary(@Param("oldsalary")double oldsalary, @Param("newsalary")double newsalary);
}
