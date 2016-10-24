package com.esoft.ischool.dao;

import java.util.List;

import com.esoft.ischool.model.Course;
import com.esoft.ischool.model.Teacher;

public interface ExamDao {

	public  List<Teacher> getTeacher(String className, String subjectName);
	public List<Course> getCourses(String className, String subjectName);
}
