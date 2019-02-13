package com.aliens.command.excel;

public class ReadExcelException extends RuntimeException{

	public ReadExcelException() {
		super();
		// TODO Auto-generated constructor stub
	}

	public ReadExcelException(String message, Throwable cause,
			boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
		// TODO Auto-generated constructor stub
	}

	public ReadExcelException(String message, Throwable cause) {
		super(message, cause);
		// TODO Auto-generated constructor stub
	}

	public ReadExcelException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

	public ReadExcelException(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}

	public ReadExcelException append(String fileName){
		return new ReadExcelException(new StringBuilder("file:[").append(fileName).append("]").append(getMessage()).append(",重新检查下数据").toString());
	}
}
