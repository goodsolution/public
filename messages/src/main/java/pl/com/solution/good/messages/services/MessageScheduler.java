package pl.com.solution.good.messages.services;

import java.util.List;
import javax.jms.JMSException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import pl.com.solution.good.messages.constants.LoggerConst;
import pl.com.solution.good.messages.constants.MessageConst;
import pl.com.solution.good.messages.entities.MessageEntity;
import pl.com.solution.good.messages.repositories.MessageRepository;

/** 
 * @author Good Solution
 */
@Component
public class MessageScheduler {
	private static final Logger LOG = LoggerFactory.getLogger(LoggerConst.LOGGER_NAME_MESSAGES);
	
	@Autowired
	private MessageRepository messageRepository;
	
	@Autowired
	private MessageService messageService;

	@Scheduled(fixedRate=10000)
    public void pullMessages() {
		LOG.trace("pull");
		List<MessageEntity> list = messageRepository.findByStatus(MessageConst.MESSAGE_STATUS_REQUESTED, new PageRequest(0, 20));

		for (MessageEntity entity: list) {
			LOG.trace("entity: " + entity);
			try {
				messageService.sendMessageToQueue(entity);
			} catch (JMSException e) {
				messageService.changeStatus(entity, MessageConst.MESSAGE_STATUS_ERROR);
			}
		}
    }	
}
