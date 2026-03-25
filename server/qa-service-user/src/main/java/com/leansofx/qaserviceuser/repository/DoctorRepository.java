package com.leansofx.qaserviceuser.repository;

import com.leansofx.qaserviceuser.entity.Doctor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DoctorRepository extends JpaRepository<Doctor, String> {
    
    // 根据活跃状态查询医生
    List<Doctor> findByIsActive(boolean isActive);
    
    // 根据活跃状态查询医生（分页）
    Page<Doctor> findByIsActive(boolean isActive, Pageable pageable);
    
    // 根据用户名查询医生
    Doctor findByUsername(String username);
}
