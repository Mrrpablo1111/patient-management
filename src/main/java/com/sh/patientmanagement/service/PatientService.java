package com.sh.patientmanagement.service;

import com.sh.patientmanagement.dto.PatientRequestDTO;
import com.sh.patientmanagement.exception.EmailAlreadyException;
import com.sh.patientmanagement.exception.PatientNotFoundException;
import com.sh.patientmanagement.kafka.KafkaProducer;
import com.sh.patientmanagement.mapper.PatientMapper;
import com.sh.patientmanagement.proto.BillingServiceClient;
import com.sh.patientmanagement.repository.PatientRepository;
import com.sh.patientmanagement.dto.PatientResponseDTO;
import com.sh.patientmanagement.model.Patient;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
public class PatientService {
    private final PatientRepository patientRepository;
    private final BillingServiceClient billingServiceClient;
    private final KafkaProducer kafkaProducer;

    public PatientService(PatientRepository patientRepository, BillingServiceClient billingServiceClient, KafkaProducer kafkaProducer){

        this.patientRepository = patientRepository;
        this.billingServiceClient = billingServiceClient;
        this.kafkaProducer = kafkaProducer;
    }
    public List<PatientResponseDTO>getPatient(){
        List<Patient> patients = patientRepository.findAll();
        return patients.stream().map(PatientMapper::toDTO).toList();
    }

    public PatientResponseDTO createPatient(PatientRequestDTO patientRequestDTO){
        if(patientRepository.existsByEmail(patientRequestDTO.getEmail())){
            throw new EmailAlreadyException("Email Already Exists" + "already exists" + patientRequestDTO.getEmail());
        }
        Patient newPatient = patientRepository.save(PatientMapper.toModel(patientRequestDTO));

        billingServiceClient.createBillingAccount(newPatient.getId().toString(), newPatient.getName(), newPatient.getEmail());

        kafkaProducer.sendEvent( newPatient);

        return PatientMapper.toDTO(newPatient);


    }

    public PatientResponseDTO updatePatient(UUID id, PatientRequestDTO patientRequestDTO){
        Patient patient = patientRepository.findById(id).orElseThrow(()-> new PatientNotFoundException("Patient not found with ID: " + id));
        if(patientRepository.existsByEmailAndIdNot(patientRequestDTO.getEmail(), id)){
            throw new EmailAlreadyException("Email Already Exists" + "already exists" + patientRequestDTO.getEmail());
        }
        patient.setName(patientRequestDTO.getName());
        patient.setEmail(patientRequestDTO.getEmail  ()) ;
        patient.setAddress(patientRequestDTO.getAddress());
        patient.setDateOfBirth(LocalDate.parse(patientRequestDTO.getDateOfBirth()));

        Patient updatedPatient = patientRepository.save(patient);

        return PatientMapper.toDTO(updatedPatient);
    }

    public void deletePatient(UUID id){
        patientRepository.deleteById(id);
    }
}
