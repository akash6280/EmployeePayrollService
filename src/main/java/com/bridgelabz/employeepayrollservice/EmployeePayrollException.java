package com.bridgelabz.employeepayrollservice;
public class EmployeePayrollException extends RuntimeException {
	enum ExceptionType {
        NAME_EMPTY,
        NAME_NULL,
        INCORRECT_QUERY
    }

    ExceptionType exceptionType;

    public EmployeePayrollException(ExceptionType exceptionType, String message) {
        super(message);
        this.exceptionType = exceptionType;
    }
}