/*
 * Copyright 2002-2013 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.springframework.samples.petclinic.owner;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;
import org.springframework.core.style.ToStringCreator;

/**
 * Simple JavaBean domain object representing a student.
 */
@Entity
@Table(name = "student")
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    @NotFound(action = NotFoundAction.IGNORE)
    private int id;

    @Column(name = "first_name")
    @NotFound(action = NotFoundAction.IGNORE)
    private String firstName;

    @Column(name = "last_name")
    @NotFound(action = NotFoundAction.IGNORE)
    private String lastName;

    @Column(name = "student_id_number")
    @NotFound(action = NotFoundAction.IGNORE)
    private int studentIdNumber;

    @Column(name = "year")
    @NotFound(action = NotFoundAction.IGNORE)
    private int year;

    @Column(name = "experience_level")
    @NotFound(action = NotFoundAction.IGNORE)
    private String experienceLevel;

    @Column(name = "grade")
    @NotFound(action = NotFoundAction.IGNORE)
    private String grade;

    public Student() {
    }

    public Student(int id, String firstName, String lastName, int studentIdNumber, int year, String experienceLevel, String grade) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.studentIdNumber = studentIdNumber;
        this.year = year;
        this.experienceLevel = experienceLevel;
        this.grade = grade;
    }

    public int getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public int getStudentIdNumber() {
        return studentIdNumber;
    }

    public void setStudentIdNumber(int studentIdNumber) {
        this.studentIdNumber = studentIdNumber;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public String getExperienceLevel() {
        return experienceLevel;
    }

    public void setExperienceLevel(String experienceLevel) {
        this.experienceLevel = experienceLevel;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }



    @Override
    public String toString() {
        return new ToStringCreator(this)
                .append("id", this.getId())
                .append("firstName", this.getFirstName())
                .append("lastName", this.getLastName())
                .append("studentIdNumber", this.studentIdNumber)
                .append("year", this.year)
                .append("experienceLevel", this.experienceLevel)
                .append("grade", this.grade)
                .toString();
    }
}
