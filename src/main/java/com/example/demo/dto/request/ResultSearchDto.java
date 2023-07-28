package com.example.demo.dto.request;

import com.example.demo.model.Doctor;
import com.example.demo.model.Specialty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class ResultSearchDto {
   private List<Specialty> specialtyList;
   private List<Doctor> doctorList;
}
