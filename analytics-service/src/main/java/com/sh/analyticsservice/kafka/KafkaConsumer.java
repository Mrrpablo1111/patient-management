package com.sh.analyticsservice.kafka;

import com.google.protobuf.InvalidProtocolBufferException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import patient.events.PatientEvents;


@Service
public class KafkaConsumer {
    private static final Logger log = LoggerFactory.getLogger(KafkaConsumer.class);

    @KafkaListener(topics = "patient", groupId = "analytics-service")
    public void consumeEvent(byte[] event){
      try{
          //perform any business related to analytics here
          PatientEvents patientEvent = PatientEvents.parseFrom(event);
          log.info("Received patient event: [PatientId:{}, PatientName:{}, PatientEmail:{}]", patientEvent.getPatientId(), patientEvent.getName(), patientEvent.getEmail());
      } catch (InvalidProtocolBufferException e) {
          log.error("Error deserializing event {}", e.getMessage());
      }

    }
}
