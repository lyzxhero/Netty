package com.lyzx.netty.netty11.event;

public class ProcessResult {
	private String status;
	private String message;
	public ProcessResult() {
		
	}
	public ProcessResult(String status, String message) {
		this.status = status;
		this.message = message;
	}
	
	public static ProcessResult OK = new ProcessResult("ok", "");
	public static ThreadLocal<ProcessResult> threadLocalParseResultError = new ThreadLocal<ProcessResult>() {
		protected ProcessResult initialValue() {
			ProcessResult processResult = new ProcessResult();
			processResult.setStatus("process_error");
			return processResult;
		};
	};
	public static ThreadLocal<ProcessResult> threadLocalParamError = new ThreadLocal<ProcessResult>() {
		protected ProcessResult initialValue() {
			ProcessResult processResult = new ProcessResult();
			processResult.setStatus("param_error");
			return processResult;
		};
	};
	
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	@Override
	public String toString() {
		return "ProcessResult [status=" + status + ", message=" + message + "]";
	}
}
