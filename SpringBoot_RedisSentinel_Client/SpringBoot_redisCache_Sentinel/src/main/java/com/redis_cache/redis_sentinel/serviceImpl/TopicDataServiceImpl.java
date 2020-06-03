package com.redis_cache.redis_sentinel.serviceImpl;

import com.redis_cache.redis_sentinel.model.Topic;
import com.redis_cache.redis_sentinel.service.TopicDataManager;
import com.redis_cache.redis_sentinel.service.TopicDataService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TopicDataServiceImpl implements TopicDataService {

    private static final Logger logger = LoggerFactory.getLogger(TopicDataServiceImpl.class);

    @Autowired
    private TopicDataManager topicDataManager;

    // Here, we are caching the result with name : allTopicsCache.
    // Note : result will not be cached if size of the result will be 0
    @Cacheable(value = "allTopicsCache", unless = "#result.size() == 0")
    public List<Topic> getAllTopics()
    {
        logger.info("Inside Topic Data service to get all topics in db");
        return topicDataManager.getAllTopics();
    }

    // Here, we are caching the result with name : topicsCache which is
    // associated with topicId.
    // @Cacheable : method result will be cached with topicCache cache name using key as passed topicId.
    // It means for different topicId, result will be cached with different key but with same cache name.
    // Once the method result is cached for a key, then for the same key,
    // method will not execute and the cached result will be served.
    @Cacheable(value = "topicCache", key = "#topicId")
    public Topic getTopic(String topicId)
    {
        logger.info("Inside Topic Data service to get topic record based on id");
        return topicDataManager.getTopic(topicId);
    }

    // Here, method will execute for every call and method result will be added or
    // updated in cache corresponding to key for given cache name.
    // NOTE : This method has ability to change values in redis cache and
    //        and effect methods : getTopic, getById
    // So, have same value and key combinations
    // @Caching : It is the group annotation for multiple cache annotations.
    @Caching(
            put= { @CachePut(value= "topicCache", key= "#topic.id") },
            evict= { @CacheEvict(value= "allTopicsCache", allEntries= true) }
    )
    public Topic addTopic(Topic topic)
    {
        logger.info("Inside Topic Data Service to add topic record in db");
        return topicDataManager.addTopic(topic);
    }

    // @CachePut : method will execute for every call and method result will be added or
    // updated in cache corresponding to key for given cache name.
    // NOTE : This method has ability to change values in redis cache and
    //        and effect methods : getTopic, getById
    // So, have same value and key combinations
    @Caching(
            put= { @CachePut(value= "topicCache", key= "#topic.id") },
            evict= { @CacheEvict(value= "allTopicsCache", allEntries= true) }
    )
    public Topic updateTopic(Topic topic,String id)
    {

        logger.info("Inside Topic Data service to update topic record based on id in db");
        topicDataManager.deleteTopic(id);
        return topicDataManager.addTopic(topic);
    }


    // NOTE : @CacheEvict : The method will execute every call and all the entries of caches will be removed.
    @Caching(
            evict= {
                    @CacheEvict(value= "topicCache", key= "#id"),
                    @CacheEvict(value= "allTopicsCache", allEntries= true)
            }
    )
    public void deleteTopic(String id)
    {
        logger.info("Inside Topic Data service to delete topic record based on id in db");
        topicDataManager.deleteTopic(id);
    }

    // Here, we are caching the result with name : topicCache which is
    // associated with id.
    @Cacheable(value = "topicCache", key = "#id")
    public Topic getById(String id)
    {
        logger.info("Inside Topic Data service to get topic record based on id using getById");
        return topicDataManager.getById(id);
    }

    // NOTE : Caching not possible in this scenario for this
    // method as we don't have corresponding setter method.
    public Topic getByIdAndName(String id,String name)
    {
        logger.info("Inside Topic Data service to get topic record from db using id and name");
        return topicDataManager.getByIdAndName(id, name);
    }
}
