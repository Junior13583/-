package com.example.junior.mapper;

import com.example.junior.entity.Person;

import java.util.List;

/**
* @Description: 数据库查询接口
* @Author: Junior
* @Date: 2023/3/13
*/
public interface PersonMapper {
    /**
    * 添加一个用户
    * @param person:  person
    * @return: void
    * @Author: Junior
    * @Date: 2023/3/13
    */
    void insertPerson(Person person);

    /**
    * 添加多个用户
    * @param personList:  personList
    * @return: void
    * @Author: Junior
    * @Date: 2023/3/13
    */
    void insertListPerson(List<Person> personList);

    /**
    * 更新用户
    * @param person:  person
    * @return: void
    * @Author: Junior
    * @Date: 2023/3/13
    */
    void updatePerson(Person person);

    /**
    * 查询所有人
    * @return: java.util.List<com.example.junior.entity.Person>
    * @Author: Junior
    * @Date: 2023/3/15
    */
    List<Person> selectPersonByPage();
}
