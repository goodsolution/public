package pl.com.solution.good.messages.repositories;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

import pl.com.solution.good.messages.entities.MessageEntity;

public interface MessageRepository extends PagingAndSortingRepository<MessageEntity, Long> {
	public List<MessageEntity> findByStatus(String status, Pageable pageable);
}
