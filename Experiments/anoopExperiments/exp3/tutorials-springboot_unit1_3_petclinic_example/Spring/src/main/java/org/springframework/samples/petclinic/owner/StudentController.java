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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;


@RestController
class StudentController {

    @Autowired
    StudentRepository studentRepository;

    private final Logger logger = LoggerFactory.getLogger(StudentController.class);

    @RequestMapping(method = RequestMethod.POST, path = "/students/new")
    public String saveStudent(Student student) {
        studentRepository.save(student);
        return "New Student " + student.getFirstName() + " Saved";
    }

    // Function to create dummy student data
    @RequestMapping(method = RequestMethod.GET, path = "/student/create")
    public String createDummyData() {
        Student s1 = new Student(1, "Anoop", "b", 101, 2026, "junior", "A");
        Student s2 = new Student(2, "justin", "m", 102, 2027, "sophmore", "B");
        Student s3 = new Student(3, "pj", "p", 103, 2026, "junior", "C");
        Student s4 = new Student(4, "nick", "l", 104, 2026, "senior", "A");
        studentRepository.save(s1);
        studentRepository.save(s2);
        studentRepository.save(s3);
        studentRepository.save(s4);
        return "Successfully created student data";
    }

    @RequestMapping(method = RequestMethod.GET, path = "/students")
    public List<Student> getAllStudents() {
        logger.info("Entered into Controller Layer");
        List<Student> results = studentRepository.findAll();
        logger.info("Number of Records Fetched: " + results.size());
        return results;
    }


}
