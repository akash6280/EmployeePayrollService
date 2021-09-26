package com.bridgelabz.employeepayrollservice;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class EmployeePayrollService {

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

	public void readEmployeePayrollData(Scanner consoleInputReader) {
        System.out.println("Enter Employee ID: ");
        int id = consoleInputReader.nextInt();
        System.out.println("Enter Employee Name: ");
        String name = consoleInputReader.next();
        System.out.println("Enter Employee Salary: ");
        double salary = consoleInputReader.nextDouble();
        employeePayrollList.add(new EmployeePayrollData(id, name, salary));
    }
    
    void writeEmployeePayrollData(I0Service ioservice) {
        if (ioservice.equals(I0Service.CONSOLE_IO))
            System.out.println("\nWriting Employee Payroll to Console\n" + employeePayrollList);
        else if (ioservice.equals(I0Service.FILE_I0)) {
            new EmployeePayrollFileIOService().writeData(employeePayrollList);
        }
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
	
	public long  readEmployeePayrollData(I0Service ioservice) {
		if(ioservice.equals(I0Service.FILE_I0))
			this.employeePayrollList=new EmployeePayrollFileIOService().readData();
		return employeePayrollList.size();
	}
	public List<EmployeePayrollData> readEmployeePayrollDBData(I0Service ioService) {
		if(ioService.equals(I0Service.DB_I0))
			this.employeePayrollList = new EmployeePayrollDBService().readData();
		return this.employeePayrollList;
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
        Scanner consoleInputReader = new Scanner(System.in);
        employeePayrollService.readEmployeePayrollData(consoleInputReader);
        employeePayrollService.writeEmployeePayrollData(I0Service.FILE_I0);
    }



} 

