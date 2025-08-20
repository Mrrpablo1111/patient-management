package com.sh.patientmanagement.kafka;

import com.sh.patientmanagement.model.Patient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import patient.events.PatientEvents;

@Service
public class KafkaProducer {
    private static final Logger log = LoggerFactory.getLogger(KafkaProducer.class);
    private final KafkaTemplate<String, byte[]> kafkaTemplate;
    //Constructor
    public KafkaProducer(KafkaTemplate<String, byte[]> kafkaTemplate){
        this.kafkaTemplate = kafkaTemplate;
    }
    //Producer

    public void sendEvent (Patient patient){
        PatientEvents events = PatientEvents.newBuilder()
                .setPatientId(patient.getId().toString())
                .setName(patient.getName())
                .setEmail(patient.getEmail())
                .setEventType("PATIENT_CREATED")
                .build();
        try{
        kafkaTemplate.send("patient", events.toByteArray());
        }catch (Exception e){
            log.error("Error while sending event",  e);
        }

    }
}
