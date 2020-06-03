package com.redis_cache.redis_sentinel.serviceImpl;

import com.redis_cache.redis_sentinel.model.Topic;
import com.redis_cache.redis_sentinel.repository.TopicRepository;
import com.redis_cache.redis_sentinel.service.TopicDataManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PersistentTopicDataManager implements TopicDataManager {

	@Autowired
	private TopicRepository topicRepository;

	public List<Topic> getAllTopics()
	{
		return topicRepository.findAll();
	}
	public Topic getTopic(String id)
	{
		return topicRepository.findById(id).orElse(null);
	}
	public Topic addTopic(Topic topic)
	{
		return topicRepository.save(topic);
	}
	public Topic updateTopic(Topic topic,String id)
	{
		topicRepository.deleteById(id);
		return topicRepository.save(topic);
	}
	public void deleteTopic(String id)
	{
		topicRepository.deleteById(id);
	}

	public Topic getById(String id)
	{
		return topicRepository.getById(id);
	}

	public Topic getByIdAndName(String id,String name)
	{
		return topicRepository.getByIdAndName(id, name);
	}

}
