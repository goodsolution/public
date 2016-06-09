package pl.com.solution.good.messages.listeners;

import java.io.IOException;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

import pl.com.solution.good.messages.constants.LoggerConst;
import pl.com.solution.good.messages.constants.MessageConst;
import pl.com.solution.good.messages.entities.MessageEntity;
import pl.com.solution.good.messages.handlers.EmailHandler;
import pl.com.solution.good.messages.handlers.MessageHandler;
import pl.com.solution.good.messages.handlers.ProcessResult;
import pl.com.solution.good.messages.repositories.MessageRepository;
import pl.com.solution.good.messages.services.MessageService;

@Component
public class MessageReceiver implements MessageListener {
	private static final Logger LOG = LoggerFactory.getLogger(LoggerConst.LOGGER_NAME_MESSAGES);

	@Autowired
	private MessageService messageService;
	
	@Autowired
	private ObjectMapper objectMapper;	
	
	@Override
	public void onMessage(Message message) {
		if (message instanceof TextMessage) {
			TextMessage jsonTxtMessage = (TextMessage)message;
			MessageEntity entity = null;
			try {
				String jsonTxt = jsonTxtMessage.getText();
				LOG.trace("start with msg: " + jsonTxt);
				entity = objectMapper.readValue(jsonTxt, MessageEntity.class);				
								
				LOG.debug("entity id: " + entity.getId() + " type: " + entity.getType() + " status: " + entity.getStatus());
				MessageHandler handler = createHandler(entity);
				LOG.debug("handler created for entity with id: " + entity.getId());
				ProcessResult result = messageService.process(handler);
				if (ProcessResult.ERROR.SUCCESS.equals(result.getError())) {
					LOG.debug("Success id: " + entity.getId());
					messageService.changeStatus(entity, MessageConst.MESSAGE_STATUS_SENT);
				} else {
					LOG.debug("Error from process id: " + entity.getId());
					messageService.changeStatus(entity, MessageConst.MESSAGE_STATUS_ERROR);
				}
				LOG.trace("stop");
			} catch (JMSException | IllegalArgumentException | IOException e) {
				LOG.error("Message received and error occured: " + e.getMessage(), e);
				messageService.changeStatus(entity, MessageConst.MESSAGE_STATUS_ERROR);
			}
		}
	}
	
	protected MessageHandler createHandler(MessageEntity entity) {
		if (entity.getType() == null ) {
			throw new IllegalArgumentException("Unsupported message type: " + entity.getType());
		}
		MessageHandler handler = null;
		if (MessageConst.MESSAGE_TYPE_EMAIL.equals(entity.getType())) {
			handler = new EmailHandler(entity);			
		} else { 
			throw new IllegalArgumentException("Unsupported message type: " + entity.getType());
		}
		return handler;
	}
}
