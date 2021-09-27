package com.bridgelabz.employeepayrollservice;

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.time.LocalDate;
import org.junit.Assert;
import org.junit.Test;

import com.bridgelabz.employeepayrollservice.EmployeePayrollService.I0Service;

public class EmployeePayrollServiceTest {

    @Test
    public void given3EmployeesWhenWrittenToFileShouldMatchEmployeeEntries() {
        EmployeePayrollData[] arrayOfEmps = {
                new EmployeePayrollData(1, "Jeff Bezos", 100000.0),
                new EmployeePayrollData(2, "Bill Gates", 200000.0),
                new EmployeePayrollData(3, "Mark Zuckerberg", 300000.0)
        };
        EmployeePayrollService employeePayrollService;
        employeePayrollService = new EmployeePayrollService(Arrays.asList(arrayOfEmps));
        employeePayrollService.writeEmployeePayrollData(I0Service.FILE_I0);
		employeePayrollService.printData(I0Service.FILE_I0);
		long entries = employeePayrollService.countEntries(I0Service.FILE_I0);
		Assert.assertEquals(3, entries);
    }
    
    @Test
	public void givenEmployeePayrollInDB_WhenRetrieved_ShouldMatchEmployeeCount(){
		EmployeePayrollService employeePayrollService = new EmployeePayrollService();
		List<EmployeePayrollData> employeePayrollData = employeePayrollService.readEmployeePayrollDBData(I0Service.DB_I0);
		Assert.assertEquals(4, employeePayrollData.size());
	}
    @Test
	public void givenNewSalaryForEmployee_WhenUpdated_ShouldSyncWithDB(){
		EmployeePayrollService employeePayrollService = new EmployeePayrollService();
		List<EmployeePayrollData> employeePayrollList = employeePayrollService.readEmployeePayrollDBData(I0Service.DB_I0);
		employeePayrollService.updateEmployeeSalary("Terisa",3000000.00);
		boolean result=employeePayrollService.checkEmployeePayrollInSyncWithDB("Terisa");
		Assert.assertTrue(result);
	}
    
    @Test
	public void givenDateRangeForEmployee_WhenRetrieved_ShouldMatchEmployeeCount(){
    	EmployeePayrollService employeePayrollService = new EmployeePayrollService();
		List<EmployeePayrollData> employeePayrollList = employeePayrollService.retriveEmployeeListForDateRange();
		List<EmployeePayrollData> expectedemployeePayrollList=new LinkedList<>();
		expectedemployeePayrollList.add(new EmployeePayrollData(1, "mark", 2000000.00,LocalDate.of(2019,01, 03)));
		expectedemployeePayrollList.add(new EmployeePayrollData(4,"Terisa", 3000000.00,LocalDate.of(2019,11, 13)));
		Assert.assertEquals(expectedemployeePayrollList,employeePayrollList);
	}
    
    public void givenEmployeePayrollInDB_ShouldRetrieveEmployeeSalarySumWithGender(){
		EmployeePayrollService employeePayrollService = new EmployeePayrollService();

		Map<String, Double> expectedGenderSalaryMap = new HashMap<String, Double>();
		expectedGenderSalaryMap.put("F", 6000000.0);
		expectedGenderSalaryMap.put("M", 3000000.0);

		Map<String, Double> actualgenderSalaryMap = employeePayrollService.getSalarySumBasedOnGender();
		Assert.assertEquals(expectedGenderSalaryMap, actualgenderSalaryMap);

	}

    
}
