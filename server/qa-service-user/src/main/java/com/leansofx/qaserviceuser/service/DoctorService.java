package com.leansofx.qaserviceuser.service;

import com.leansofx.qaserviceuser.entity.Doctor;
import com.leansofx.qaserviceuser.repository.DoctorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DoctorService {

    @Autowired
    private DoctorRepository doctorRepository;

    /**
     * 获取所有医生（分页）
     */
    public Page<Doctor> getAllDoctors(Pageable pageable) {
        return doctorRepository.findAll(pageable);
    }

    /**
     * 根据部门获取医生（分页）
     */
    public Page<Doctor> getDoctorsByDepartment(String department, Pageable pageable) {
        return doctorRepository.findByDepartment(department, pageable);
    }

    /**
     * 根据是否活跃获取医生（分页）
     */
    public Page<Doctor> getActiveDoctors(boolean isActive, Pageable pageable) {
        return doctorRepository.findByIsActive(isActive, pageable);
    }

    /**
     * 根据部门和是否活跃获取医生（分页）
     */
    public Page<Doctor> getDoctorsByDepartmentAndActive(String department, boolean isActive, Pageable pageable) {
        return doctorRepository.findByDepartmentAndIsActive(department, isActive, pageable);
    }

    /**
     * 根据用户名获取医生
     */
    public Doctor getDoctorByUsername(String username) {
        return doctorRepository.findByUsername(username);
    }

    /**
     * 转换医生数据，将专业领域转换为字符串列表
     */
    public List<String> getDoctorSpecialties(Doctor doctor) {
        if (doctor.getSpecialties() == null) {
            return java.util.Collections.emptyList();
        }
        return doctor.getSpecialties().stream()
                .map(specialty -> specialty.getSpecialty())
                .collect(Collectors.toList());
    }
}
