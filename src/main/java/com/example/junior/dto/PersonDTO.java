package com.example.junior.dto;

import com.example.junior.validate.customValidation.sex.Sex;
import com.example.junior.validate.group.Insert;
import com.example.junior.validate.group.Update;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * @Description: 人实体类
 * @Author: Junior
 * @Date: 2023/3/9
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Accessors(chain = true)
public class PersonDTO{
    @NotNull(message = "id不能为空", groups = Update.class)
    private Integer id;
    @Length(min = 2, max = 6, groups = {Insert.class, Update.class})
    private String username;
    @Length(min = 6, max = 12, message = "用户密码长度必须在6-12之间", groups = {Insert.class, Update.class})
    private String password;
    @Min(value = 0, message = "年龄必须大于零", groups = {Insert.class, Update.class})
    private Integer age;
    @Sex(groups = {Insert.class, Update.class})
    private String sex;
    private Long phone;
    private String address;

}
