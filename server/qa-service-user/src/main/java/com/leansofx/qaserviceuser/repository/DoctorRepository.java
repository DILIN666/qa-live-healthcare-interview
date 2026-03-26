package com.leansofx.qaserviceuser.repository;

import com.leansofx.qaserviceuser.entity.Doctor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DoctorRepository extends JpaRepository<Doctor, String> {

    // 分页查询所有医生
    Page<Doctor> findAll(Pageable pageable);

    // 根据部门查询医生
    Page<Doctor> findByDepartment(String department, Pageable pageable);

    // 根据是否活跃查询医生
    Page<Doctor> findByIsActive(boolean isActive, Pageable pageable);

    // 根据部门和是否活跃查询医生
    Page<Doctor> findByDepartmentAndIsActive(String department, boolean isActive, Pageable pageable);

    // 根据用户名查询医生
    Doctor findByUsername(String username);
}
