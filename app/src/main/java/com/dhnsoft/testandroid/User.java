package com.dhnsoft.testandroid;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * @Description
 * @ClassName User
 * @Author dhn
 * @date 2020.10.18 11:49
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class User {
    private String name;
    private int age;
    private String sex;

}
