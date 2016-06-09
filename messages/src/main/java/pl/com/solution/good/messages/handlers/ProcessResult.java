package pl.com.solution.good.messages.handlers;

public class ProcessResult {
	public static enum ERROR {		
		SUCCESS(0, "Success"),
		UNEXPECTED_ERROR(9999, "Unexpected error");
		private int errNo;
		private String errorMessage;
		ERROR(int errNo, String errorMessage) {
			this.errNo = errNo;
			this.errorMessage = errorMessage;
		}
		public int getErrNo() {
			return errNo;
		}
		public String getErrorMessage() {
			return errorMessage;
		}		
	}
	
	private ERROR error;

	public ERROR getError() {
		return error;
	}

	public ProcessResult() {
		error = ERROR.SUCCESS;
	}
	
	public ProcessResult(ERROR error) {
		this.error = error;
	}	
	
	//TODO add additional info about error
}
