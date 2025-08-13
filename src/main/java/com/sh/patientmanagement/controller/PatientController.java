package com.sh.patientmanagement.controller;

import com.sh.patientmanagement.dto.PatientRequestDTO;
import com.sh.patientmanagement.dto.PatientResponseDTO;

import com.sh.patientmanagement.repository.PatientRepository;
import com.sh.patientmanagement.service.PatientService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/patients")
public class PatientController {

    private final PatientService patientService;

    public PatientController(PatientRepository patientRepository, PatientService patientService) {

        this.patientService = patientService;
    }

    @GetMapping
    public ResponseEntity<List<PatientResponseDTO>> getPatient(){
        List<PatientResponseDTO> patients = patientService.getPatient();
        return ResponseEntity.ok().body(patients);
    }

    @PostMapping
    public ResponseEntity<PatientResponseDTO> createPatient(@RequestBody @Valid PatientRequestDTO patientRequestDTO){
         PatientResponseDTO patientResponseDTO = patientService.createPatient(patientRequestDTO);
         return ResponseEntity.ok().body(patientResponseDTO);
    }
}
