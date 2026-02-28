package com.capgeminispring.basicspringboot;

import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.query.Jpa21Utils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.service.annotation.PatchExchange;


@RestController
public class DemoController {
	
	@Autowired
	EmployeeJpaRepository empJpa; //hold null
	
	@GetMapping("/hello")
	
	public String getHello() {
		return "Hello";
	}

	@GetMapping("/sendData")
//When we want to send the data in the form of key and value pair.
	//http://localhost:8080/sendData?name=Ankesh
	public String sendData(@RequestParam String name) {
		System.out.println(name);
		return "Success";
	}
	
	@GetMapping("/sendDataPathVar/{name}/{id}")
	public String sendDataPathVar(@PathVariable String name, @PathVariable int id) {
		System.out.println(name+" "+id);
		return "Success";
	}
	
	@PostMapping("/Employee")
	
	public String createEmployee(@RequestBody Employee e) {
		System.out.println(e.getId());
		System.out.println(e.getName());
		System.out.println(e.getSalary());
		return "Success";
	}
//from here database is involved.
//save into the database	
	@PostMapping("/emp")
	public String createEmployeeJpa(@RequestBody Employee e) {
		empJpa.save(e);
		return "Success";
	}
//fetch the data from database through primary key
	@GetMapping("/get-id/{id}")
	public Employee getById(@PathVariable int id) {
		Optional<Employee> employeeJpa = empJpa.findById(id);
		if(employeeJpa.isPresent()) {
			return employeeJpa.get();
		}else {
			return null;
		}
	}
	
//fetch all the employees data which is present in the database.	
	@GetMapping("/all-emp")
	public List<Employee> getAllEmployee(){
		List<Employee> listEmp  = empJpa.findAll();
		return listEmp;
	}
	
//to update the data(Employee Details) in the database.
	@PutMapping("update-empbyid/{id}")
	public String updateEmployee(@PathVariable int id,@RequestBody Employee e) {
		Employee employee = getById(id);
		employee.setId(e.getId());
		employee.setName(e.getName());
		employee.setSalary(e.getSalary());
		empJpa.save(employee);
		return "Data Updated Successfully";
	}
	
//for partial update of emp details in the database.
	
	@PatchMapping("partialupdate-emp/{id}")
	public String updateEmployeeDetails(@PathVariable int id, @RequestBody Employee e) {
		Employee emp = getById(id);
		if(emp!=null) {
			if(e.getId()!=0) {
				emp.setId(e.getId());
			}
			if(e.getName()!=null) {
				emp.setName(e.getName());
			}
			if(e.getSalary()!=0.0) {
				emp.setSalary(e.getSalary());
			}
			empJpa.save(emp);
			return "Data saved partially";
		}else {
			throw new EmployeeNotFoundException("Employee Not Found the id: "+id);
		}
	}
// for deletion of the data(employee details).
	@DeleteMapping("delete-emp/{id}")
	public String deleteEmployee(@PathVariable int id,@RequestBody Employee e) {
		Employee emp = getById(id);
		if(emp!=null) {
			empJpa.delete(emp);
			return "Data Deleted";
		}else {
			return "Data doesn't exist";
		}
	}
	
//find  by Name (made custom findByName function in the interface EmployeeRepository.
	@GetMapping("/getbyName/{name}")
	public Employee getName(@PathVariable String name) {
		return empJpa.getByName(name);
	}
	@GetMapping("/getBySalary/{salary}")
	public Employee getSalary(@PathVariable double salary) {
		return empJpa.getBySalary(salary);
	}
	
	@GetMapping("getbytwovalue/{name}/{salary}")
	public Employee getByNameAndSal(@PathVariable String name,@PathVariable double salary) {
		return empJpa.getByNameAndSalary(name, salary);
	}
	
//Updating the salary through custom updateBysalary method which is define in the EmployeeRepository.
	@PutMapping("/updateSalaryCustom/{oldSalary}/{newSalary}")
	public String updatedBySalary(@PathVariable double oldSalary,@PathVariable double newSalary) {
		int count= empJpa.updateBySalary(oldSalary, newSalary);
		if(count>0) {
			return "Updated the salary";
		}else {
			throw new EmployeeNotFoundException("Employee not found with given salary: "+oldSalary);
		}
	}
	
	//method to handle the exceptions, we can't use try catch direclty, to handle the exception for frontend we use this method.
	//this is local exception handler which handle the exception within this controller file only where the EmployeeNotFoundException throw 
	@ExceptionHandler(EmployeeNotFoundException.class)
	public String handleException(EmployeeNotFoundException ex) {
		return ex.getMessage();
	}
}
