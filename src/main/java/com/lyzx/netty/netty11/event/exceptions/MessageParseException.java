package com.lyzx.netty.netty11.event.exceptions;

public class MessageParseException extends RuntimeException {
	
	private static final long serialVersionUID = 1L;

	public MessageParseException() {
		super();
	}

	public MessageParseException(String message) {
		super(message);
	}

	public MessageParseException(String message, Throwable cause) {
		super(message, cause);
	}

	public MessageParseException(Throwable cause) {
		super(cause);
	}
}
