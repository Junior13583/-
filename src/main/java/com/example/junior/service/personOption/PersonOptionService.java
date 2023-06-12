package com.example.junior.service.personOption;

import com.example.junior.dto.PersonDTO;
import com.github.pagehelper.PageInfo;

import java.util.List;

/**
* @Description: 针对 Person 类增删改查接口
* @Author: Junior
* @Date: 2023/3/13
*/
public interface PersonOptionService {
    /**
     * 添加一个用户
     * @param personDTO:  personDTO
     * @return: void
     * @Author: Junior
     * @Date: 2023/3/13
     */
    void insertPerson(PersonDTO personDTO);

    /**
     * 添加多个用户
     * @param personDTOS:  personDTOS
     * @return: void
     * @Author: Junior
     * @Date: 2023/3/13
     */
    void insertListPerson(List<PersonDTO> personDTOS);

    /**
     * 更新用户
     * @param personDTO:  personDTO
     * @return: void
     * @Author: Junior
     * @Date: 2023/3/13
     */
    void updatePerson(PersonDTO personDTO);

    /**
    * 分页查询
    * @param pageIndex:  当前页
	* @param pageSize:  页大小
    * @return: com.github.pagehelper.PageInfo
    * @Author: Junior
    * @Date: 2023/3/15
    */
    PageInfo selectPersonByPage(int pageIndex, int pageSize);
}
