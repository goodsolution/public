package pl.com.solution.good.messages.handlers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pl.com.solution.good.messages.constants.LoggerConst;
import pl.com.solution.good.messages.entities.MessageEntity;

public class EmailHandler implements MessageHandler {
	private static final Logger LOG = LoggerFactory.getLogger(LoggerConst.LOGGER_NAME_MESSAGES);
	
	private MessageEntity entity;
	
	public EmailHandler(MessageEntity entity) {
		this.entity = entity;
	}
	
	@Override
	public ProcessResult beforeProcess() {
		return new ProcessResult();
	}

	@Override
	public ProcessResult process() {
		//TODO to implement sending email
		LOG.debug("email sent.... " + entity);
		
		return new ProcessResult();
	}

	@Override
	public ProcessResult afterProcess() {
		return new ProcessResult();
	}
}
