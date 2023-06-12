package com.example.junior.service.personOption;

import com.example.junior.dto.PersonDTO;
import com.example.junior.entity.Person;
import com.example.junior.mapStruct.PersonMapping;
import com.example.junior.mapper.PersonMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;

/**
* @Description: 针对 Person 类增删改查接口的实现类
* @Author: Junior
* @Date: 2023/3/13
*/
@Service
public class PersonOptionServiceImpl implements PersonOptionService{

    @Resource
    private PersonMapper personMapper;


    @Override
    public void insertPerson(PersonDTO personDTO) {
        //  DTO 转换为 DO
        Person person = PersonMapping.INSTANCE.personDTOToPerson(personDTO);
        personMapper.insertPerson(person);
    }

    @Override
    public void insertListPerson(List<PersonDTO> personDTOS) {
        List<Person> personList = PersonMapping.INSTANCE.personDTOListToPersonList(personDTOS);
        personMapper.insertListPerson(personList);
    }

    @Override
    public void updatePerson(PersonDTO personDTO) {
        Person person = PersonMapping.INSTANCE.personDTOToPerson(personDTO);
        personMapper.updatePerson(person);
    }

    @Override
    public PageInfo selectPersonByPage(int pageIndex, int pageSize) {
        PageHelper.startPage(pageIndex, pageSize);
        List<Person> personList = personMapper.selectPersonByPage();
        //  类型转换
        List<PersonDTO> personDTOS = PersonMapping.INSTANCE.personListToPersonDTOList(personList);

        //  先构建 PageInfo 实例，保存页码相关参数
        PageInfo pageInfo = new PageInfo(personList);
        //  使用 DTO 类覆盖原有内容
        pageInfo.setList(personDTOS);
        return pageInfo;
    }
}
