package com.sh.patientmanagement.service;

import com.sh.patientmanagement.dto.PatientRequestDTO;
import com.sh.patientmanagement.mapper.PatientMapper;
import com.sh.patientmanagement.repository.PatientRepository;
import com.sh.patientmanagement.dto.PatientResponseDTO;
import com.sh.patientmanagement.model.Patient;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PatientService {
    private final PatientRepository patientRepository;
    public PatientService(PatientRepository patientRepository){
        this.patientRepository = patientRepository;
    }
    public List<PatientResponseDTO>getPatient(){
        List<Patient> patients = patientRepository.findAll();
        return patients.stream().map(PatientMapper::toDTO).toList();
    }

    public PatientResponseDTO createPatient(PatientRequestDTO patientRequestDTO){
        Patient newPatient = patientRepository.save(PatientMapper.toModel(patientRequestDTO));
        return PatientMapper.toDTO(newPatient);
    }
}
