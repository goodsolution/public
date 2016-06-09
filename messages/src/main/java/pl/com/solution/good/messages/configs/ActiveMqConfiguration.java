package pl.com.solution.good.messages.configs;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.command.ActiveMQQueue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.task.TaskExecutor;
import org.springframework.jms.connection.CachingConnectionFactory;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.listener.DefaultMessageListenerContainer;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import pl.com.solution.good.messages.listeners.MessageReceiver;

@Configuration()
public class ActiveMqConfiguration {
	private static final String THREAD_NAME_PREFIX_JMS = "jms-messages-";
	private static final String THREAD_GROUP_NAME_JMS = "JmsThreads";
	
	@Value("${amq.brokerUrl}")
	private String brokerUrl;
	@Value("${amq.queueName}")
	private String queueName;
	
	@Autowired
	private MessageReceiver messageReceiver;		
	
	public String getBrokerUrl() {
		return brokerUrl;
	}

	public void setBrokerUrl(String brokerUrl) {
		this.brokerUrl = brokerUrl;
	}

	public String getQueueName() {
		return queueName;
	}

	public void setQueueName(String queueName) {
		this.queueName = queueName;
	}

	@Bean
	public TaskExecutor taskExecutor() {
        ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
        taskExecutor.setCorePoolSize(10);
        taskExecutor.setMaxPoolSize(100);
        taskExecutor.setQueueCapacity(1000);
        taskExecutor.setThreadGroupName(THREAD_GROUP_NAME_JMS);
        taskExecutor.setThreadNamePrefix(THREAD_NAME_PREFIX_JMS);
        return taskExecutor;
    }
	
	@Bean
	public DefaultMessageListenerContainer defaultMessageListenerContainer() {
		DefaultMessageListenerContainer defaultMessageListenerContainer = new DefaultMessageListenerContainer();
		defaultMessageListenerContainer.setConnectionFactory(cachingConnectionFactory());
		defaultMessageListenerContainer.setDestination(activeMQQueue());
		defaultMessageListenerContainer.setMessageListener(messageReceiver);
		defaultMessageListenerContainer.setSessionTransacted(true);
		defaultMessageListenerContainer.setTaskExecutor(taskExecutor());
		return defaultMessageListenerContainer;
	}
	
	@Bean
	public CachingConnectionFactory cachingConnectionFactory() {
		CachingConnectionFactory cachingConnectionFactory = new CachingConnectionFactory();
		cachingConnectionFactory.setTargetConnectionFactory(activeMQConnectionFactory());		
		return cachingConnectionFactory;
	}
	
	@Bean
	@Primary
	public ActiveMQConnectionFactory activeMQConnectionFactory() {
		ActiveMQConnectionFactory activeMQConnectionFactory = new ActiveMQConnectionFactory();
		activeMQConnectionFactory.setBrokerURL(getBrokerUrl());
		return activeMQConnectionFactory;
	}
	
	@Bean
	public JmsTemplate jmsTemplate() {
		JmsTemplate jmsTemplate = new JmsTemplate();
		jmsTemplate.setConnectionFactory(cachingConnectionFactory());
		jmsTemplate.setDefaultDestination(activeMQQueue());
		jmsTemplate.setSessionTransacted(true);
		jmsTemplate.setDeliveryPersistent(true);
		return jmsTemplate;
	}
	
	@Bean
	public ActiveMQQueue activeMQQueue() {
		ActiveMQQueue activeMQQueue = new ActiveMQQueue();
		activeMQQueue.setPhysicalName(getQueueName());
		return activeMQQueue;
	}
}
