package com.example.appointment;

import com.example.appointment.dto.ApiResponseError;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class AppointmentService {

    private final RestTemplate restTemplate;

    public AppointmentService() {
        this.restTemplate = new RestTemplate();
    }

    // 1. Gắn Annotation CircuitBreaker với tên instance và tên hàm fallback
    @CircuitBreaker(name = "doctorServiceCB", fallbackMethod = "getDoctorFallback")
    public ResponseEntity<?> checkDoctorSchedule(int doctorId) {
        // Logic gọi sang Doctor-Service (port 8082)
        String url = "http://localhost:8082/api/doctors/check-schedule?doctorId=" + doctorId;

        // Nếu Doctor-Service sập, dòng này sẽ ném lỗi (ResourceAccessException / Timeout)
        // và Circuit Breaker sẽ bắt lấy để kích hoạt fallback
        return restTemplate.getForEntity(url, String.class);
    }

    // 2. Viết hàm Fallback
    // Chữ ký hàm phải giống hệt hàm gốc, nhưng thêm tham số Exception ở cuối
    public ResponseEntity<?> getDoctorFallback(int doctorId, Exception e) {
        // Log lỗi ra console để dev biết (tùy chọn)
        System.err.println("Doctor Service Error: " + e.getMessage());

        // Tạo đối tượng lỗi theo yêu cầu đề bài
        ApiResponseError errorResponse = new ApiResponseError(
                "Hiện tại không thể kiểm tra thông tin bác sĩ, vui lòng thử lại sau vài giây",
                HttpStatus.SERVICE_UNAVAILABLE.value(), // 503
                "Doctor Service Error"
        );

        // Trả về HTTP Status 503 kèm JSON
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(errorResponse);
    }
}
