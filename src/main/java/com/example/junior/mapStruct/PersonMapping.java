package com.example.junior.mapStruct;

import com.example.junior.dto.PersonDTO;
import com.example.junior.entity.Person;
import org.apache.ibatis.javassist.bytecode.stackmap.MapMaker;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * @Description: DO类和DTO类转换
 * @Author: Junior
 * @Date: 2023/3/15
 */
@Mapper
public interface PersonMapping {
    PersonMapping INSTANCE = Mappers.getMapper(PersonMapping.class);

    /**
    * PersonDTO 类映射为 Person 类
    * @param personDTO:  personDTO
    * @return: com.example.junior.entity.Person
    * @Author: Junior
    * @Date: 2023/3/15
    */
    @Mapping(source = "phone", target = "phoneNumber")
    Person personDTOToPerson(PersonDTO personDTO);

    /**
     * 多个PersonDTO 类映射为 多个Person 类
     * @param personDTOS:  personDTOS
     * @return: com.example.junior.entity.Person
     * @Author: Junior
     * @Date: 2023/3/15
     */
    List<Person> personDTOListToPersonList(List<PersonDTO> personDTOS);

    /**
    * Person 类映射为 PersonDTO 类
    * @param person:  person
    * @return: com.example.junior.dto.PersonDTO
    * @Author: Junior
    * @Date: 2023/3/15
    */
    @Mapping(source = "phoneNumber", target = "phone")
    PersonDTO personToPersonDTO(Person person);

    /**
    * 多个Person 类映射为 多个PersonDTO 类
    * @param personList:  personList
    * @return: java.util.List<com.example.junior.dto.PersonDTO>
    * @Author: Junior
    * @Date: 2023/3/15
    */
    List<PersonDTO> personListToPersonDTOList(List<Person> personList);
}
