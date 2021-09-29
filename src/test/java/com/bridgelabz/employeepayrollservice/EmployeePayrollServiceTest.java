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
	public void givenEmployeePayrollInDB_WhenRetrieved_ShouldMatchEmployeeCount(){
		EmployeePayrollService employeePayrollService = new EmployeePayrollService();
		List<EmployeePayrollData> employeePayrollData = employeePayrollService.readEmployeePayrollData(I0Service.DB_I0);
		Assert.assertEquals(3, employeePayrollData.size());
	}
    @Test
	public void givenNewSalaryForEmployee_WhenUpdatedUsingStatement_ShouldSyncWithDB(){
		EmployeePayrollService employeePayrollService = new EmployeePayrollService();
		List<EmployeePayrollData> employeePayrollList = employeePayrollService.readEmployeePayrollData(I0Service.DB_I0);
		employeePayrollService.updateEmployeeSalary("Terisa",30000.00);
		boolean result=employeePayrollService.checkEmployeePayrollInSyncWithDB("Terisa");
		Assert.assertTrue(result);
	}
    
    @Test
   	public void givenNewSalaryForEmployee_WhenUpdatedUsingPreparedStatement_ShouldSyncWithDB(){
   		EmployeePayrollService employeePayrollService = new EmployeePayrollService();
   		List<EmployeePayrollData> employeePayrollList = employeePayrollService.readEmployeePayrollData(I0Service.DB_I0);
   		employeePayrollService.updateEmployeeSalary("Terisa",30000.00);
   		boolean result=employeePayrollService.checkEmployeePayrollInSyncWithDB("Terisa");
   		Assert.assertTrue(result);
   	}
    
    @Test
	public void givenDateRangeForEmployee_WhenRetrieved_ShouldMatchEmployeeCount(){
    	EmployeePayrollService employeePayrollService = new EmployeePayrollService();
		List<EmployeePayrollData> employeePayrollList = employeePayrollService.retriveEmployeeListForDateRange(I0Service.DB_I0);
		List<EmployeePayrollData> expectedemployeePayrollList=new LinkedList<>();
		expectedemployeePayrollList.add(new EmployeePayrollData(111,"Bill", 50000.00,LocalDate.of(2019,01, 03)));
		expectedemployeePayrollList.add(new EmployeePayrollData(222,"Terisa", 30000.00,LocalDate.of(2020,01, 03)));
		Assert.assertEquals(expectedemployeePayrollList,employeePayrollList);
	}
    
    @Test
    public void givenEmployeePayrollInDB_ShouldRetrieveEmployeeSalarySumWithGender(){
		EmployeePayrollService employeePayrollService = new EmployeePayrollService();
		Map<String, Double> expectedGenderSalaryMap = new HashMap<String, Double>();
		expectedGenderSalaryMap.put("F", 30000.0);
		expectedGenderSalaryMap.put("M", 110000.0);
		Map<String, Double> actualgenderSalaryMap = employeePayrollService.getSalarySumBasedOnGender(I0Service.DB_I0);
		Assert.assertEquals(expectedGenderSalaryMap, actualgenderSalaryMap);

	}
    
    
    @Test
    public void givenEmployeePayrollInDB_ShouldRetrieveEmployeeAvgSalaryWithGender(){
		EmployeePayrollService employeePayrollService = new EmployeePayrollService();
		Map<String, Double> expectedGenderSalaryMap = new HashMap<String, Double>();
		expectedGenderSalaryMap.put("F", 30000.0);
		expectedGenderSalaryMap.put("M", 55000.0);
		Map<String, Double> actualgenderSalaryMap = employeePayrollService.getAverageSalaryBasedOnGender(I0Service.DB_I0);
		Assert.assertEquals(expectedGenderSalaryMap, actualgenderSalaryMap);

	}
    
    @Test
    public void givenEmployeePayrollInDB_ShouldRetrieveEmployeeCountWithGender(){
		EmployeePayrollService employeePayrollService = new EmployeePayrollService();
		Map<String, Integer> expectedGenderSalaryMap = new HashMap<String,Integer>();
		expectedGenderSalaryMap.put("F", 1);
		expectedGenderSalaryMap.put("M", 2);
		Map<String, Integer> actualgenderSalaryMap = employeePayrollService.getEmployeeCountBasedOnGender(I0Service.DB_I0);
		Assert.assertEquals(expectedGenderSalaryMap, actualgenderSalaryMap);

	}
    
    @Test
    public void givenEmployeePayrollInDB_ShouldRetrieveEmployeeMinimumSalaryWithGender(){
		EmployeePayrollService employeePayrollService = new EmployeePayrollService();
		Map<String, Double> expectedGenderSalaryMap = new HashMap<String, Double>();
		expectedGenderSalaryMap.put("F", 30000.00);
		expectedGenderSalaryMap.put("M", 50000.00);
		Map<String, Double> actualgenderSalaryMap = employeePayrollService.getMinimumSalaryBasedOnGender(I0Service.DB_I0);
		Assert.assertEquals(expectedGenderSalaryMap, actualgenderSalaryMap);

	}
    
    @Test
    public void givenEmployeePayrollInDB_ShouldRetrieveEmployeeMaximumSalaryWithGender(){
		EmployeePayrollService employeePayrollService = new EmployeePayrollService();
		Map<String, Double> expectedGenderSalaryMap = new HashMap<String, Double>();
		expectedGenderSalaryMap.put("F", 30000.00);
		expectedGenderSalaryMap.put("M", 60000.00);
		Map<String, Double> actualgenderSalaryMap = employeePayrollService.getMaximumSalaryBasedOnGender(I0Service.DB_I0);
		Assert.assertEquals(expectedGenderSalaryMap, actualgenderSalaryMap);

	} 

	@Test
	public void givenNewEmployee_WhenAdded_ShouldSynWithDB() {
		EmployeePayrollService employeePayrollService = new EmployeePayrollService();
		employeePayrollService.readEmployeePayrollData(I0Service.DB_I0);
		employeePayrollService.addEmployeeToPayroll(1,"Clark",807650078,"Madurai",'M',70000.00,LocalDate.of(2021,01, 03));
		boolean result = employeePayrollService.checkEmployeePayrollInSyncWithDB("Clark");
		Assert.assertTrue(result);
		}
   
}
