package com.stackroute.ToDo.service.repository;

import com.stackroute.ToDo.service.model.Feedback;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface FeedbackRepository extends MongoRepository<Feedback,String> {
}