package com.bridgelabz.employeepayrollservice;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import com.bridgelabz.employeepayrollservice.EmployeePayrollService.I0Service;

public class EmployeePayrollService implements EmployeePayrollServiceIF {

    public enum I0Service {
        CONSOLE_IO, FILE_I0, DB_I0, REST_I0
    }

    public List<EmployeePayrollData> employeePayrollList;
    EmployeePayrollDBService employeePayrollDBService = new EmployeePayrollDBService();

    public EmployeePayrollService(List<EmployeePayrollData> list) {
    	this();
        this.employeePayrollList = list;
    }

	public EmployeePayrollService() {
		employeePayrollDBService= EmployeePayrollDBService.getInstance();
	}

	public List<EmployeePayrollData> readEmployeePayrollData(I0Service ioService) {
        if(ioService.equals(I0Service.CONSOLE_IO))
			this.employeePayrollList=new EmployeePayrollService().readData();
        if(ioService.equals(I0Service.FILE_I0))
    		this.employeePayrollList=new EmployeePayrollFileIOService().readData();
        if(ioService.equals(I0Service.DB_I0))
    		this.employeePayrollList = new EmployeePayrollDBService().readData();
    		return this.employeePayrollList;
    	}
    	
    private List<EmployeePayrollData> readData() {
    	Scanner consoleInputReader = new Scanner(System.in);
    	System.out.println("Enter Employee ID: ");
        int id = consoleInputReader.nextInt();
        System.out.println("Enter Employee Name: ");
        String name = consoleInputReader.next();
        System.out.println("Enter Employee Salary: ");
        double salary = consoleInputReader.nextDouble();
        employeePayrollList.add(new EmployeePayrollData(id, name, salary));
        return this.employeePayrollList;
	}
    
    public void writeEmployeePayrollData(I0Service ioservice) {
        if (ioservice.equals(I0Service.CONSOLE_IO))
            System.out.println("\nWriting Employee Payroll to Console\n" + employeePayrollList);
        if (ioservice.equals(I0Service.FILE_I0)) 
            new EmployeePayrollFileIOService().writeData(this.employeePayrollList);
        if (ioservice.equals(I0Service.DB_I0)) 
            new EmployeePayrollDBService().writeData(this.employeePayrollList);
    }
    
    public void printData(I0Service ioService) {
		if(ioService.equals(I0Service.FILE_I0)) {
			new EmployeePayrollFileIOService().printData();
		}
	}
	public long countEntries(I0Service ioService) {
		if(ioService.equals(I0Service.FILE_I0)) {
			return new EmployeePayrollFileIOService().countEntries();
		}
		return 0;
	}
	
	public void updateEmployeeSalary(String name, double salary) {
		int result =employeePayrollDBService.updateEmployeeData(name, salary);
		if(result==0) return;
		EmployeePayrollData employeePayrollData=this.getEmployeePayrollData(name);
		if(employeePayrollData!=null) employeePayrollData.salary=salary;

	}

	private EmployeePayrollData getEmployeePayrollData(String name) {
		EmployeePayrollData employeePayrollData;
		 employeePayrollData=this.employeePayrollList.stream()
							.filter(employeeDataItem->employeeDataItem.name.equals(name))
							.findFirst()
							.orElse(null);

		return employeePayrollData;
	}
	
	public boolean checkEmployeePayrollInSyncWithDB(String name){
		List<EmployeePayrollData> employeePayrollDataList=employeePayrollDBService.getEmployeePayrollDataFromDB(name);
		return employeePayrollDataList.get(0).equals(getEmployeePayrollData(name));
	}
    public static void main(String[] args) {
        ArrayList<EmployeePayrollData> employeePayrollList = new ArrayList<>();
        EmployeePayrollService employeePayrollService = new EmployeePayrollService(employeePayrollList);
        employeePayrollService.readEmployeePayrollData(I0Service.CONSOLE_IO);
        employeePayrollService.writeEmployeePayrollData(I0Service.FILE_I0);
    }

	public List<EmployeePayrollData> retriveEmployeeListForDateRange(I0Service ioService) {
		return this.employeePayrollList=employeePayrollDBService.retriveEmployeeDetailForDateRange();
	}

	public Map<String, Double> getSalarySumBasedOnGender(I0Service ioService) {
		if(ioService==I0Service.DB_I0)
		return new EmployeePayrollDBService().getSalarySumBasedOnGender();
		return null;
	}

	public Map<String, Double> getAverageSalaryBasedOnGender(I0Service ioService) {
		if(ioService==I0Service.DB_I0)
		return new EmployeePayrollDBService().getAverageSalaryBasedOnGender();
		return null;
	}

	public Map<String, Integer> getEmployeeCountBasedOnGender(I0Service ioService) {
		if(ioService==I0Service.DB_I0)
		return new EmployeePayrollDBService().getgetEmployeeCountBasedOnGender();
		return null;
	}

	public Map<String, Double> getMinimumSalaryBasedOnGender(I0Service ioService) {
		if(ioService==I0Service.DB_I0)
		return new EmployeePayrollDBService().getMinimumSalaryBasedOnGender();
		return null;
	}

	public Map<String, Double> getMaximumSalaryBasedOnGender(I0Service ioService) {
		if(ioService==I0Service.DB_I0)
		return new EmployeePayrollDBService().getMaximumSalaryBasedOnGender();
		return null;
	}



} 

