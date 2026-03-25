package com.leansofx.qaserviceuser.service;

import com.leansofx.qaserviceuser.entity.Doctor;
import com.leansofx.qaserviceuser.repository.DoctorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DoctorService {

    @Autowired
    private DoctorRepository doctorRepository;

    // 获取所有医生
    public List<Doctor> getAllDoctors() {
        return doctorRepository.findAll();
    }

    // 获取所有医生（分页）
    public Page<Doctor> getAllDoctors(Pageable pageable) {
        return doctorRepository.findAll(pageable);
    }

    // 获取活跃的医生
    public List<Doctor> getActiveDoctors() {
        return doctorRepository.findByIsActive(true);
    }

    // 获取活跃的医生（分页）
    public Page<Doctor> getActiveDoctors(Pageable pageable) {
        return doctorRepository.findByIsActive(true, pageable);
    }

    // 根据ID获取医生
    public Optional<Doctor> getDoctorById(String id) {
        return doctorRepository.findById(id);
    }

    // 根据用户名获取医生
    public Doctor getDoctorByUsername(String username) {
        return doctorRepository.findByUsername(username);
    }

    // 保存医生
    public Doctor saveDoctor(Doctor doctor) {
        return doctorRepository.save(doctor);
    }

    // 删除医生
    public void deleteDoctor(String id) {
        doctorRepository.deleteById(id);
    }
}
