package com.hfxb.app.core.exceptions;

public class NotUniqueResultException extends Exception {
	 /**
	 * 
	 */
	private static final long serialVersionUID = -8761875941278480146L;

	/**
     * 构造函数 {@code BaseException}
     */
    public NotUniqueResultException() {
        super();
    }

    /**
     * 构造函数 {@code BaseException}
     * @param message	错误信息
     */
    public NotUniqueResultException(String message) {
        super(message);
    }

    /**
     * 构造函数 {@code BaseException}
     * @param cause  需要抛出的异常
     */
    public NotUniqueResultException(Throwable cause) {
        super(cause);
    }

    /**
     * 构造函数 {@code BaseException}
     * @param message  错误信息
     * @param cause    需要抛出的异常
     */
    public NotUniqueResultException(String message, Throwable cause) {
        super(message, cause);
    }
    
}
