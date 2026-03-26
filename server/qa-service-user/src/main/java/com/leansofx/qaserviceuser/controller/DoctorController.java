package com.leansofx.qaserviceuser.controller;

import com.leansofx.qaserviceuser.entity.Doctor;
import com.leansofx.qaserviceuser.service.DoctorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/doctors")
public class DoctorController {

    @Autowired
    private DoctorService doctorService;

    /**
     * 获取医生列表（支持分页、排序和过滤）
     */
    @GetMapping
    public ResponseEntity<?> getDoctors(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDirection,
            @RequestParam(required = false) String department,
            @RequestParam(required = false) Boolean isActive) {

        try {
            // 构建排序对象
            Sort sort = Sort.by(Sort.Direction.fromString(sortDirection), sortBy);
            // 构建分页对象
            Pageable pageable = PageRequest.of(page, size, sort);

            Page<Doctor> doctorPage;

            // 根据参数查询
            if (department != null && isActive != null) {
                doctorPage = doctorService.getDoctorsByDepartmentAndActive(department, isActive, pageable);
            } else if (department != null) {
                doctorPage = doctorService.getDoctorsByDepartment(department, pageable);
            } else if (isActive != null) {
                doctorPage = doctorService.getActiveDoctors(isActive, pageable);
            } else {
                doctorPage = doctorService.getAllDoctors(pageable);
            }

            // 转换响应数据，处理专业领域
            var doctors = doctorPage.getContent().stream().map(doctor -> {
                Map<String, Object> doctorMap = new HashMap<>();
                doctorMap.put("id", doctor.getId());
                doctorMap.put("username", doctor.getUsername());
                doctorMap.put("name", doctor.getName());
                doctorMap.put("title", doctor.getTitle());
                doctorMap.put("department", doctor.getDepartment());
                doctorMap.put("avatar", doctor.getAvatar());
                doctorMap.put("experience", doctor.getExperience());
                doctorMap.put("isActive", doctor.isActive());
                doctorMap.put("specialties", doctorService.getDoctorSpecialties(doctor));
                return doctorMap;
            }).collect(Collectors.toList());

            // 构建响应
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("data", doctors);
            response.put("total", doctorPage.getTotalElements());
            response.put("page", doctorPage.getNumber());
            response.put("size", doctorPage.getSize());
            response.put("totalPages", doctorPage.getTotalPages());

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", "获取医生列表失败: " + e.getMessage());
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }

    /**
     * 根据用户名获取医生详情
     */
    @GetMapping("/{username}")
    public ResponseEntity<?> getDoctorByUsername(@PathVariable String username) {
        try {
            Doctor doctor = doctorService.getDoctorByUsername(username);
            if (doctor == null) {
                Map<String, Object> errorResponse = new HashMap<>();
                errorResponse.put("success", false);
                errorResponse.put("message", "医生不存在");
                return ResponseEntity.status(404).body(errorResponse);
            }

            Map<String, Object> doctorMap = new HashMap<>();
            doctorMap.put("id", doctor.getId());
            doctorMap.put("username", doctor.getUsername());
            doctorMap.put("name", doctor.getName());
            doctorMap.put("title", doctor.getTitle());
            doctorMap.put("department", doctor.getDepartment());
            doctorMap.put("avatar", doctor.getAvatar());
            doctorMap.put("experience", doctor.getExperience());
            doctorMap.put("isActive", doctor.isActive());
            doctorMap.put("specialties", doctorService.getDoctorSpecialties(doctor));

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("data", doctorMap);

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", "获取医生详情失败: " + e.getMessage());
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }
}
