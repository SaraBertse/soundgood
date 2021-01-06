/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package se.kth.iv1351.soundgoodjdbc.model;

/**
 *
 * @author sarab
 */
public class Student implements StudentDTO {
    private int studentId;
    private int personId;
    private int age;
    private String skillLevel;
    
 public Student (int studentId, int personId){
     this.studentId = studentId;
     this.personId = personId;
     this.age = 0;
     this.skillLevel = null;
 }
 
 public int getStudentId(){
     return studentId;
 }
 
  public int getPersonId(){
     return personId;
 }
}
    
 