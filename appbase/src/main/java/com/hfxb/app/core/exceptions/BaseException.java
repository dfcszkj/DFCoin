package com.hfxb.app.core.exceptions;

public class BaseException extends RuntimeException {

	private static final long serialVersionUID = 6405346532397172434L;

	 /**
     * 构造函数 {@code BaseException}
     */
    public BaseException() {
        super();
    }

    /**
     * 构造函数 {@code BaseException}
     *
     * @param message	错误信息
     */
    public BaseException(String message) {
        super(message);
    }

    /**
     * 构造函数 {@code BaseException}
     *
     * @param cause  需要抛出的异常
     */
    public BaseException(Throwable cause) {
        super(cause);
    }

    /**
     * 构造函数 {@code BaseException}
     *
     * @param message  错误信息
     * @param cause    需要抛出的异常
     */
    public BaseException(String message, Throwable cause) {
        super(message, cause);
    }
	
}
