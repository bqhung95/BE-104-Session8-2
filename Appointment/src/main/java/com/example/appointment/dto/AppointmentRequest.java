package com.example.appointment.dto;

import lombok.Data;

@Data
public class AppointmentRequest {
    private String patientName;
    private int doctorId; // Trường này quan trọng để lấy getDoctorId()
    private String reason;
}
