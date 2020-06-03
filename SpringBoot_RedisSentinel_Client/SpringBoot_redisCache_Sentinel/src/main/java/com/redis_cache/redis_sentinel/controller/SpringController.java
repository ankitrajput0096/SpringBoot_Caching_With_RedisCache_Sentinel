package com.redis_cache.redis_sentinel.controller;

import com.redis_cache.redis_sentinel.model.Topic;
import com.redis_cache.redis_sentinel.service.TopicDataManager;
import com.redis_cache.redis_sentinel.service.TopicDataService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(value="/springBootJpa")
public class SpringController {

	private static final Logger logger = LoggerFactory.getLogger(SpringController.class);

	@Autowired
	private TopicDataService topicDataService;

	@RequestMapping(value="/",method= RequestMethod.GET, produces = "text/plain")
	public ResponseEntity<String> helloMethod()
	{
		return new ResponseEntity<>("Hello World", HttpStatus.OK);
	}

	@RequestMapping(value="/topics", method = RequestMethod.GET, produces = "application/json")
	public ResponseEntity<List<Topic>> listOfTopcs()
	{
		logger.info("In Spring Controller, getting all topics");
		return new ResponseEntity<List<Topic>>(topicDataService.getAllTopics(), HttpStatus.OK);
	}

	@RequestMapping(value="/topics/{id}", method = RequestMethod.GET, produces = "application/json")
	public ResponseEntity<Topic> getRequiredTopic(@PathVariable String id)
	{
		logger.info("In Spring controller, getting topic using id");
		Topic resultTopic = topicDataService.getTopic(id);
		if(resultTopic != null) {
			return new ResponseEntity<Topic>(resultTopic, HttpStatus.FOUND);
		} else {
			return new ResponseEntity<Topic>(HttpStatus.NOT_FOUND);
		}
	}
	
	
	//In this json object is sent 
	/*
	 * {
	 * 		"id":"java"
	 * 		"name":"java programming"
	 * 		"description":"java is easy"
	 * }
	 */
	@RequestMapping(value="/topics/add",method= RequestMethod.POST, consumes = "application/json", produces = "application/json")
	public ResponseEntity<?> addTopic(@RequestBody Topic topic)
	{
		logger.info("In Spring controller, creating new topic record");
		topicDataService.addTopic(topic);
		return new ResponseEntity<>(HttpStatus.CREATED);
	}

	@RequestMapping(value="/topics/update/{id}",method= RequestMethod.PUT, consumes = "application/json", produces = "application/json")
	public ResponseEntity<?> updateTopic(@RequestBody Topic topic, @PathVariable String id)
	{
		logger.info("In Spring controller, updating topic record using id");
		topicDataService.updateTopic(topic,id);
		return new ResponseEntity<>(HttpStatus.OK);
	}
	
	
	//Url "localhost:8080/springBootJpa/topics/delete/java
	@RequestMapping(value="/topics/delete/{id}",method= RequestMethod.DELETE, produces = "application/json")
	public ResponseEntity<?> deleteTopic(@PathVariable String id)
	{
		logger.info("In Spring controller, deleting topic record using id");
		topicDataService.deleteTopic(id);
		return new ResponseEntity<>(HttpStatus.OK);
	}
	
	
	//Url "localhost:8080/springBootJpa/topics/getById?id=java
	@RequestMapping(value="/topics/getById",method= RequestMethod.GET, produces = "application/json")
	public ResponseEntity<Topic> getById(@RequestParam(value="id")String id)
	{
		logger.info("In Spring controller, getting topic using id");
		Topic resultTopic = topicDataService.getById(id);
		if(resultTopic != null) {
			return new ResponseEntity<Topic>(resultTopic, HttpStatus.FOUND);
		} else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}

	@RequestMapping(value="/topics/getByIdAndName",method= RequestMethod.GET, produces = "application/json")
	public ResponseEntity<Topic> getByIdAndName(@RequestParam(value="id")String id, @RequestParam(value="name")String name)
	{
		logger.info("In Spring controller, getting topic using id and name");
		Topic resultTopic = topicDataService.getByIdAndName(id, name);
		if(resultTopic != null) {
			return new ResponseEntity<Topic>(resultTopic, HttpStatus.FOUND);
		} else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}
	
}
