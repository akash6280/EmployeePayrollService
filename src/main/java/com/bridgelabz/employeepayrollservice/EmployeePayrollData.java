/*
 * This Java source file was generated by the Gradle 'init' task.
 */
package com.bridgelabz.employeepayrollservice;

import java.time.LocalDate;
import java.util.Objects;

public class EmployeePayrollData {

		public int id;
		public int companyId;
		public String name;
		public long phoneNumber;
		public String address;
		public String gender;
		public double salary;
		public LocalDate startDate;

		public EmployeePayrollData(int id, String name, double salary) {
			this.id = id;
			this.name = name;
			this.salary = salary;
		}

		public EmployeePayrollData(int id, String name, Double salary, LocalDate startDate) {
			this.id = id;
	        this.name = name;
	        this.salary = salary;
	        this.startDate = startDate;
		}
		
		

		public EmployeePayrollData(int id, int companyId, String name, long phoneNumber, String address,
				String gender, double salary, LocalDate startDate) {
			super();
			this.id = id;
			this.companyId = companyId;
			this.name = name;
			this.phoneNumber = phoneNumber;
			this.address = address;
			this.gender = gender;
			this.salary = salary;
			this.startDate = startDate;
		}

		@Override
		public String toString() {
			  return "id=" + id + ", name=" + name + ", salary=" + salary;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			EmployeePayrollData other = (EmployeePayrollData) obj;
			return Objects.equals(address, other.address) && companyId == other.companyId
					&& Objects.equals(gender, other.gender) && id == other.id && Objects.equals(name, other.name)
					&& Objects.equals(phoneNumber, other.phoneNumber)
					&& Double.doubleToLongBits(salary) == Double.doubleToLongBits(other.salary)
					&& Objects.equals(startDate, other.startDate);
		}

		public int getId() {
			return id;
		}

		public void setId(int id) {
			this.id = id;
		}

		public int getCompanyId() {
			return companyId;
		}

		public void setCompanyId(int companyId) {
			this.companyId = companyId;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public long getPhoneNumber() {
			return phoneNumber;
		}

		public void setPhoneNumber(long phoneNumber) {
			this.phoneNumber = phoneNumber;
		}

		public String getAddress() {
			return address;
		}

		public void setAddress(String address) {
			this.address = address;
		}

		public String getGender() {
			return gender;
		}

		public void setGender(String gender) {
			this.gender = gender;
		}

		public double getSalary() {
			return salary;
		}

		public void setSalary(double salary) {
			this.salary = salary;
		}

		public LocalDate getStartDate() {
			return startDate;
		}

		public void setStartDate(LocalDate startDate) {
			this.startDate = startDate;
		}
		
		
}

