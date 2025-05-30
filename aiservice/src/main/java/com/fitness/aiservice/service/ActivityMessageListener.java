package com.fitness.aiservice.service;

import com.fitness.aiservice.model.Activity;
import com.fitness.aiservice.model.Recommendation;
import com.fitness.aiservice.repository.RecommendationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.listener.exception.FatalListenerExecutionException;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClientResponseException;

@Service
@Slf4j
@RequiredArgsConstructor
public class ActivityMessageListener {

    private final ActivityAIService aiService;
    private final RecommendationRepository recommendationRepository;

    @RabbitListener(queues = "activity.queue")
    public void processActivity(Activity activity) {
        log.info("Received activity for processing: {}", activity.getId());
//        log.info("Generated Recommendation: {}", aiService.generateRecommendation(activity));
//        Recommendation recommendation = aiService.generateRecommendation(activity);
//        recommendationRepository.save(recommendation);
        try {
            Recommendation recommendation = aiService.generateRecommendation(activity);
            recommendationRepository.save(recommendation);
        } catch (WebClientResponseException e) {
            if (e.getStatusCode().is4xxClientError()) {
                log.warn("Client error while generating recommendation for activity {}: {}", activity.getId(), e.getMessage());

                // STOP requeueing for bad client request
                throw new FatalListenerExecutionException("Rejecting message due to client error", e);
            }
            throw e; // Let other exceptions bubble up (e.g. 5xx)
        } catch (Exception e) {
            log.error("Unexpected error for activity {}: {}", activity.getId(), e.getMessage(), e);
            throw e;
        }

    }
}