package pl.com.solution.good.messages.services;

import java.util.Date;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.ws.rs.Path;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import pl.com.solution.good.messages.constants.LoggerConst;
import pl.com.solution.good.messages.constants.MessageConst;
import pl.com.solution.good.messages.controllers.requests.SendMessageRequest;
import pl.com.solution.good.messages.entities.MessageEntity;
import pl.com.solution.good.messages.handlers.MessageHandler;
import pl.com.solution.good.messages.handlers.ProcessResult;
import pl.com.solution.good.messages.repositories.MessageRepository;

/**
 * 
 * @author Good Solution
 */
@Component
@Path("/messages")
public class MessageService {
	private static final Logger LOG = LoggerFactory.getLogger(LoggerConst.LOGGER_NAME_MESSAGES);
	
	@Autowired
	private MessageRepository messageRepository;

	@Autowired
	private JmsTemplate jmsTemplate;
	
	@Autowired
	private ObjectMapper objectMapper;

	public void saveMessage(SendMessageRequest req) {
		MessageEntity entity = new MessageEntity();
		entity.setCreateDate(new Date());
		entity.setAddress(req.getAddress());
		entity.setTitle(req.getTitle());
		entity.setBody(req.getBody());
		entity.setType(req.getType());
		entity.setStatus(MessageConst.MESSAGE_STATUS_REQUESTED);
		messageRepository.save(entity);
	}

	@Transactional
	public void sendMessageToQueue(MessageEntity entity) throws JMSException {		
		jmsTemplate.send(new MessageCreator() {
			public Message createMessage(Session session) throws JMSException {
				String msg = null;
				try {
					msg = objectMapper.writeValueAsString(entity);
				} catch (JsonProcessingException e) {
					throw new RuntimeException(e);
				}				
				TextMessage message = session.createTextMessage(msg);
				return message;
			}
		});	
		changeStatus(entity, MessageConst.MESSAGE_STATUS_QUEUED);
	}
	
	@Transactional
	public void changeStatus(MessageEntity entity, String status) {		
		entity = messageRepository.findOne(entity.getId());
		entity.setStatus(status);
		entity.setModificationDate(new Date());
		messageRepository.save(entity);
	}
	
	@Transactional
	public ProcessResult process(MessageHandler handler) {
		LOG.trace("Start processing: " + handler);		
		ProcessResult result = handler.beforeProcess();
		if(ProcessResult.ERROR.SUCCESS.equals(result.getError())) {
			LOG.trace("Finish before process: " + handler + " with result: " + result);
			return result;
		}
		result = handler.process();
		if(ProcessResult.ERROR.SUCCESS.equals(result.getError())) {
			LOG.trace("Finish after process: " + handler + " with result: " + result);
			return result;
		}		
		result = handler.afterProcess();
		LOG.trace("Finish processing: " + handler + " with result: " + result);
		return result;
	}		
}
