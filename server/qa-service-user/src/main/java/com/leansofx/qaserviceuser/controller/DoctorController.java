package com.leansofx.qaserviceuser.controller;

import com.leansofx.qaserviceuser.entity.Doctor;
import com.leansofx.qaserviceuser.service.DoctorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/doctors")
public class DoctorController {

    @Autowired
    private DoctorService doctorService;

    // 获取所有医生
    @GetMapping
    public ResponseEntity<?> getAllDoctors(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        try {
            Pageable pageable = PageRequest.of(page, size);
            Page<Doctor> doctorPage = doctorService.getAllDoctors(pageable);
            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "data", doctorPage.getContent(),
                    "total", doctorPage.getTotalElements(),
                    "page", doctorPage.getNumber(),
                    "size", doctorPage.getSize(),
                    "totalPages", doctorPage.getTotalPages()
            ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                    "success", false,
                    "message", "获取医生列表失败: " + e.getMessage()
            ));
        }
    }

    // 获取活跃的医生
    @GetMapping("/active")
    public ResponseEntity<?> getActiveDoctors(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        try {
            Pageable pageable = PageRequest.of(page, size);
            Page<Doctor> doctorPage = doctorService.getActiveDoctors(pageable);
            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "data", doctorPage.getContent(),
                    "total", doctorPage.getTotalElements(),
                    "page", doctorPage.getNumber(),
                    "size", doctorPage.getSize(),
                    "totalPages", doctorPage.getTotalPages()
            ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                    "success", false,
                    "message", "获取活跃医生列表失败: " + e.getMessage()
            ));
        }
    }

    // 根据ID获取医生
    @GetMapping("/{id}")
    public ResponseEntity<?> getDoctorById(@PathVariable String id) {
        try {
            return doctorService.getDoctorById(id)
                    .map(doctor -> ResponseEntity.ok(Map.of(
                            "success", true,
                            "data", doctor
                    )))
                    .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of(
                            "success", false,
                            "message", "医生不存在"
                    )));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                    "success", false,
                    "message", "获取医生详情失败: " + e.getMessage()
            ));
        }
    }

    // 根据用户名获取医生
    @GetMapping("/username/{username}")
    public ResponseEntity<?> getDoctorByUsername(@PathVariable String username) {
        try {
            Doctor doctor = doctorService.getDoctorByUsername(username);
            if (doctor != null) {
                return ResponseEntity.ok(Map.of(
                        "success", true,
                        "data", doctor
                ));
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of(
                        "success", false,
                        "message", "医生不存在"
                ));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                    "success", false,
                    "message", "获取医生详情失败: " + e.getMessage()
            ));
        }
    }
}
