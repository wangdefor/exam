package com.github.tangyi.common.core.exceptions;

import java.util.function.Supplier;

/**
 *
 * 服务异常
 *
 * @author tangyi
 * @date 2019-10-08 12:56
 */
public class ServiceException extends CommonException {

	private static final long serialVersionUID = -7285211528095468156L;

	public ServiceException() {
	}

	public ServiceException(String msg) {
		super(msg);
	}

	public static Exception getException(){
		return new ServiceException();
	}

	 public static Supplier<ServiceException> exception(String message) {
		return () -> new ServiceException(message);
	}

}
