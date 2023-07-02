package org.example.redisson.test.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Student {  //older version you may need this class to implements Serializable to pass the test

    private String name;
    private int age;
    private String city;
    private List<Integer> marks;

}
