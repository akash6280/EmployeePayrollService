package com.bridgelabz.employeepayrollservice;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class EmployeePayrollDBService {

	private Connection getConnection() throws SQLException {
		String jdbcURL="jdbc:mysql://localhost:3306/payroll_service?useSSL=false";
		String userName="root";
		String password="Welcome@123";
		Connection connection;
		System.out.println("Connecting to database"+ jdbcURL);
		connection=DriverManager.getConnection(jdbcURL,userName,password);
		System.out.println("Connection is successfull"+ connection);
		return connection;
	}

	public List<EmployeePayrollData> readData(){
		String sql = "select p.emp_id, e.employee_name, p.basic_pay from employee e, payroll p  where e.id=p.emp_id";
		List<EmployeePayrollData> employeePayrollList= new ArrayList<>();
		try {
			Connection connection =this.getConnection();
			Statement statement = connection.createStatement();
			ResultSet result = statement.executeQuery(sql);
			while(result.next()) {
				int id=result.getInt("emp_id");
				String name = result.getString("employee_name");
				Double salary=result.getDouble("basic_pay");
				employeePayrollList.add(new EmployeePayrollData(id, name, salary));
			}

		}catch(SQLException e) {
			e.printStackTrace();
		}
		return employeePayrollList;
	}


}
