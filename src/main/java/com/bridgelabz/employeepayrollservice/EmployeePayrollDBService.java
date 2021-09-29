package com.bridgelabz.employeepayrollservice;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.sql.Date;  

import com.bridgelabz.employeepayrollservice.EmployeePayrollException.ExceptionType;

public class EmployeePayrollDBService {
	private PreparedStatement employeePayrollDataStatementToUpdate;
	private PreparedStatement employeePayrollDataStatement;
    private static EmployeePayrollDBService employeePayrollDBService;

    public static EmployeePayrollDBService getInstance() {
        if (employeePayrollDBService == null)
            employeePayrollDBService = new EmployeePayrollDBService();
        return employeePayrollDBService;
    }

    public EmployeePayrollDBService() {

    }

	

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
		String sql = "select * from employee , payroll where employee.id=payroll.id";
		List<EmployeePayrollData> employeePayrollList= new ArrayList<>();
		try(Connection connection =this.getConnection()) {
			Statement statement = connection.createStatement();
			ResultSet resultSet = statement.executeQuery(sql);
			employeePayrollList = this.getEmployeePayrollData(resultSet);

		}catch(SQLException e) {
			throw new EmployeePayrollException(ExceptionType.INCORRECT_QUERY,"Wrong query entered");
		}
		return employeePayrollList;
	}
	
	public EmployeePayrollData addEmployeeToPayrollUC7(String name, double salary, LocalDate startDate, char gender) {
		int employeeId = -1;
		EmployeePayrollData employeePayrollData = null;
		String sql = String.format("insert into employee_payroll(name,basic_pay, start,gender) values('%s','%s','%s','%c' )", name,salary, Date.valueOf(startDate),gender );
		try(Connection connection = this.getConnection()){
			Statement statement = connection.createStatement();
			int rowAffected = statement.executeUpdate(sql,statement.RETURN_GENERATED_KEYS);
			if(rowAffected == 1) {
				ResultSet resultSet = statement.getGeneratedKeys();
				if(resultSet.next())
					employeeId = resultSet.getInt(1);
			}
			employeePayrollData = new EmployeePayrollData(employeeId, name, salary, startDate);
		}catch(SQLException e) {
			e.printStackTrace();
		}
		return employeePayrollData;
	}
     
	public EmployeePayrollData addEmployeeToPayroll(int company_id,String name,long phoneNumber,String address,char gender,double salary,LocalDate startDate){
		int employeeId = -1;
		Connection connection = null;
		EmployeePayrollData employeePayrollData = null;
		try{
			connection = this.getConnection();
			connection.setAutoCommit(false);
		}catch (SQLException e) {
			e.printStackTrace();
		}

		try (Statement statement = connection.createStatement()){

			String sql = String.format("insert into employee (company_id,employee_name,phone_number,address,gender,basic_pay,start) VALUES ('%s', '%s', '%s','%s', '%s', '%s', '%s');",company_id,name,phoneNumber,address,gender,salary,Date.valueOf(startDate));

			int rowAffected = statement.executeUpdate(sql, statement.RETURN_GENERATED_KEYS);
			if(rowAffected == 1) {
				ResultSet resultSet = statement.getGeneratedKeys();
				if(resultSet.next()) employeeId = resultSet.getInt(1);
			}  
		}
		catch(SQLException e) {
			e.printStackTrace();
			try {
				connection.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
		}

		try(Statement statement = connection.createStatement()){

			double deductions = salary * 0.2;
			double taxablePay = salary - deductions;
			double tax = taxablePay * 0.1;
			double netPay = salary - tax;
			String sql = String.format("insert into payroll (id, basic_pay, deductions, taxable_pay, tax, net_pay)"
					     + "values ('%s', '%s', '%s', '%s', '%s','%s')",employeeId, salary, deductions, taxablePay, tax, netPay);
			int rowAffected = statement.executeUpdate(sql);
			if (rowAffected == 1) {
				employeePayrollData = new EmployeePayrollData(employeeId,company_id,name,phoneNumber,address,String.valueOf(gender),salary,startDate);
			}			
		}
		catch(SQLException e) {
			e.printStackTrace();
			try {
				connection.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
		}
		try {
			connection.commit();
		}catch (SQLException e) {
				e.printStackTrace();
		}
			finally {
			if(connection!=null)
				try {
					connection.close();
				} catch (SQLException e) { 
					e.printStackTrace();
				}
		}
		return employeePayrollData;
	}
	
	public int updateEmployeeData(String name, double salary) {
		return this.updateEmployeeDataUsingPreparedStatement(name,salary);
	}

	private int updateEmployeeDataUsingPreparedStatement(String name, double salary) {
			if(name.isBlank())
			   throw new EmployeePayrollException(ExceptionType.NAME_EMPTY,"Name entered is empty");
	        if (this.employeePayrollDataStatementToUpdate == null)
	            this.prepareStatementForUpdatingEmployeeData();
	        try {
	            employeePayrollDataStatementToUpdate.setDouble(1, salary);
	            employeePayrollDataStatementToUpdate.setString(2, name);
	            return employeePayrollDataStatementToUpdate.executeUpdate();
	        } catch (SQLException e) {
	            
	        }
	        catch (NullPointerException e) {
	            throw new EmployeePayrollException(ExceptionType.NAME_NULL,"Name entered is null");
	        }
			return 0;
	}
	
	private void prepareStatementForUpdatingEmployeeData() {
        try {
            Connection connection = this.getConnection();
            String sql ="update employee set basic_pay=? where employee_name =?";
            employeePayrollDataStatementToUpdate = connection.prepareStatement(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

	private int updateEmployeeDataUsingStatement(String name, double salary) {
		if(name.isBlank())
			   throw new EmployeePayrollException(ExceptionType.NAME_EMPTY,"Name entered is empty");
		 String sql = String.format("update employee_payroll set basic_pay= %.2f where name ='%s';", salary, name);
		try(Connection connection =this.getConnection()) {
			Statement statement = connection.createStatement();
			return statement.executeUpdate(sql);
		}catch(SQLException e) {
			throw new EmployeePayrollException(ExceptionType.INCORRECT_QUERY,"Wrong query entered");
		}catch (NullPointerException e) {
            throw new EmployeePayrollException(ExceptionType.NAME_NULL,"Name entered is null");
        }
	}
	
	public List<EmployeePayrollData> getEmployeePayrollDataFromDB(String name) {
		if(name.isBlank())
			throw new EmployeePayrollException(ExceptionType.NAME_EMPTY,"Name entered is empty");
        List<EmployeePayrollData> employeePayrollList = null;
        if (this.employeePayrollDataStatement == null)
            this.prepareStatementForEmployeeData();
        try {
            employeePayrollDataStatement.setString(1, name);
            ResultSet resultSet = employeePayrollDataStatement.executeQuery();
            employeePayrollList = this.getEmployeePayrollData(resultSet);
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (NullPointerException e) {
            throw new EmployeePayrollException(ExceptionType.NAME_NULL,"Name entered is null");
        }
        return employeePayrollList;
    }

    private List<EmployeePayrollData> getEmployeePayrollData(ResultSet resultSet) {
        List<EmployeePayrollData> employeePayrollList = new ArrayList<>();
        try {
            	while(resultSet.next()) {
    				int id=resultSet.getInt("id");
    				String name = resultSet.getString("employee_name");
    				Double salary=resultSet.getDouble("basic_pay");
    				LocalDate startDate = resultSet.getDate("start").toLocalDate();
    				String address =resultSet.getString("address");
    				String gender = resultSet.getString("gender");
    				long phoneNumber = resultSet.getLong("phone_number");
    				int companyId = resultSet.getInt("company_id");
    				employeePayrollList.add(new EmployeePayrollData(id,companyId,name,phoneNumber,address,gender,salary,startDate));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return employeePayrollList;
    }

    private void prepareStatementForEmployeeData() {
        try {
            Connection connection = this.getConnection();
            String sql = "select * from employee where employee_name=?";
            employeePayrollDataStatement = connection.prepareStatement(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public List<EmployeePayrollData> retriveEmployeeDetailForDateRange(){
		String sql = "select * from employee where start between cast('2019-01-03' as date) and cast('2020-01-03' as date) ";
		List<EmployeePayrollData> employeePayrollList= new ArrayList<>();
		try(Connection connection =this.getConnection()) {
			Statement statement = connection.createStatement();
			ResultSet result = statement.executeQuery(sql);
			while(result.next()) {
				int id=result.getInt("id");
				String name = result.getString("employee_name");
				Double salary=result.getDouble("basic_pay");
				LocalDate startDate = result.getDate("start").toLocalDate();
				employeePayrollList.add(new EmployeePayrollData(id, name, salary,startDate));
			}

		}catch(SQLException e) {
			e.printStackTrace();
		}
		return employeePayrollList;
	}
    
    public Map<String, Double> getSalarySumBasedOnGender(){
		Map<String, Double> genderSalaryMap = new HashMap<String, Double>();
		String sql="SELECT gender,SUM(basic_pay) FROM employee GROUP BY gender";

		try (Connection connection = this.getConnection()){
			Statement statement=connection.createStatement();
			ResultSet resultSet=statement.executeQuery(sql);
			while(resultSet.next()){
				String gender=resultSet.getString("gender");
				double salarySum=resultSet.getDouble("SUM(basic_pay)");
				genderSalaryMap.put(gender, salarySum);
			}
		} 
		catch (SQLException e) {
			e.printStackTrace();
		}
		return genderSalaryMap;
	}

	public Map<String, Double> getAverageSalaryBasedOnGender() {
		Map<String, Double> genderSalaryMap = new HashMap<String, Double>();
		String sql="SELECT gender,AVG(basic_pay) FROM employee GROUP BY gender";

		try (Connection connection = this.getConnection()){
			Statement statement=connection.createStatement();
			ResultSet resultSet=statement.executeQuery(sql);
			while(resultSet.next()){
				String gender=resultSet.getString("gender");
				double salarySum=resultSet.getDouble("AVG(basic_pay)");
				genderSalaryMap.put(gender, salarySum);
			}
		} 
		catch (SQLException e) {
			throw new EmployeePayrollException(ExceptionType.INCORRECT_QUERY,"Wrong query entered");
		}
		return genderSalaryMap;
	}

	public Map<String, Integer> getgetEmployeeCountBasedOnGender() {
		Map<String, Integer> genderSalaryMap = new HashMap<String, Integer>();
		String sql="SELECT gender,count(*) FROM employee GROUP BY gender";

		try (Connection connection = this.getConnection()){
			Statement statement=connection.createStatement();
			ResultSet resultSet=statement.executeQuery(sql);
			while(resultSet.next()){
				String gender=resultSet.getString("gender");
				Integer salarySum=resultSet.getInt("count(*)");
				genderSalaryMap.put(gender, salarySum);
			}
		} 
		catch (SQLException e) {
			throw new EmployeePayrollException(ExceptionType.INCORRECT_QUERY,"Wrong query entered");
		}
		return genderSalaryMap;
	}

	public Map<String, Double> getMinimumSalaryBasedOnGender() {
		Map<String, Double> genderSalaryMap = new HashMap<String, Double>();
		String sql="SELECT gender,MIN(basic_pay) FROM employee  GROUP BY gender";

		try (Connection connection = this.getConnection()){
			Statement statement=connection.createStatement();
			ResultSet resultSet=statement.executeQuery(sql);
			while(resultSet.next()){
				String gender=resultSet.getString("gender");
				double salarySum=resultSet.getDouble("MIN(basic_pay)");
				genderSalaryMap.put(gender, salarySum);
			}
		} 
		catch (SQLException e) {
			throw new EmployeePayrollException(ExceptionType.INCORRECT_QUERY,"Wrong query entered");
		}
		return genderSalaryMap;
	}

	public Map<String, Double> getMaximumSalaryBasedOnGender() {
		Map<String, Double> genderSalaryMap = new HashMap<String, Double>();
		String sql="SELECT gender,MAX(basic_pay) FROM employee GROUP BY gender";

		try (Connection connection = this.getConnection()){
			Statement statement=connection.createStatement();
			ResultSet resultSet=statement.executeQuery(sql);
			while(resultSet.next()){
				String gender=resultSet.getString("gender");
				double salarySum=resultSet.getDouble("MAX(basic_pay)");
				genderSalaryMap.put(gender, salarySum);
			}
		} 
		catch (SQLException e) {
			throw new EmployeePayrollException(ExceptionType.INCORRECT_QUERY,"Wrong query entered");
		}
		return genderSalaryMap;
	}

	public void writeData(List<EmployeePayrollData> employeePayrollList) {
		EmployeePayrollData employee=employeePayrollList.get(0);
			String sql = String.format("insert into employee_payroll (id,name,basic_pay,start) values ('%d','%s','%f','%s');",employee.id,employee.name,
					     employee.salary,employee.startDate.toString());
			try (Connection connection = this.getConnection()){
				Statement statement = connection.createStatement();
				statement.executeUpdate(sql);
			}
			catch(SQLException e) {
				throw new EmployeePayrollException(ExceptionType.INCORRECT_QUERY,"Wrong query entered");
			}
		}

		
	}
