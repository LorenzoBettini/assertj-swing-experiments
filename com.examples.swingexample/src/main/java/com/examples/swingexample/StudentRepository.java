package com.examples.swingexample;

import java.util.List;

public interface StudentRepository {

	public List<Student> findAll();

	public Student findById(String id);

	public void save(Student student);

	public void delete(String id);
}
