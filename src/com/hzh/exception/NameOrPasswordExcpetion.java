package com.hzh.exception;

public class NameOrPasswordExcpetion extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public void printStackTrace() {
		System.err.println("ÓÃ»§ÃûÃÜÂë´íÎó£¡");
	}
}
