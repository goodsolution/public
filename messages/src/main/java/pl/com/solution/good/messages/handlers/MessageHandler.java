package pl.com.solution.good.messages.handlers;

public interface MessageHandler {
	public ProcessResult beforeProcess();
	
	public ProcessResult process();
	
	public ProcessResult afterProcess();
}
