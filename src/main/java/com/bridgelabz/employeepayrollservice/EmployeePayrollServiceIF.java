package com.bridgelabz.employeepayrollservice;

import java.util.List;

import com.bridgelabz.employeepayrollservice.EmployeePayrollService.I0Service;

public interface EmployeePayrollServiceIF {
	public List<EmployeePayrollData> readEmployeePayrollData(I0Service ioService);
	void writeEmployeePayrollData(I0Service ioservice);
}
