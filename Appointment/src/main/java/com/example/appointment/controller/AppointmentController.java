package com.example.appointment.controller;


import com.example.appointment.AppointmentService;
import com.example.appointment.dto.AppointmentRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AppointmentController {

    private final AppointmentService appointmentService;

    public AppointmentController(AppointmentService appointmentService) {
        this.appointmentService = appointmentService;
    }
    @PostMapping("/api/v1/appointments")
    public ResponseEntity<?> addAppointment(@RequestBody AppointmentRequest request) {
        // Gọi service
        return appointmentService.checkDoctorSchedule(request.getDoctorId());
    }
}
