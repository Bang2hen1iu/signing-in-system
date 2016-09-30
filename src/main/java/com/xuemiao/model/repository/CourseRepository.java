package com.xuemiao.model.repository;

import com.xuemiao.model.pdm.CourseEntity;
import com.xuemiao.model.pdm.StudentAndCourseNameKey;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by dzj on 9/30/2016.
 */
public interface CourseRepository extends JpaRepository<CourseEntity, StudentAndCourseNameKey> {
}
