package com.example.junior.controller;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.example.junior.dto.PersonDTO;
import com.example.junior.handle.exceptHandler.customException.BusinessException;
import com.example.junior.handle.sentinelHandler.MyBlockException;
import com.example.junior.handle.sentinelHandler.MyFallback;
import com.example.junior.service.multithreading.MultithreadingServiceImpl;
import com.example.junior.service.personOption.PersonOptionServiceImpl;
import com.example.junior.service.sentinel.SentinelServiceImpl;
import com.example.junior.validate.ValidationList;
import com.example.junior.validate.group.Insert;
import com.example.junior.validate.group.Update;
import com.example.junior.vo.ResponseDataVO;
import com.github.pagehelper.PageInfo;
import org.hibernate.validator.constraints.Length;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.constraints.Pattern;

/**
 * @Description: 测试控制器
 * @Author: Junior
 * @Date: 2023/3/2
 */
@RestController
@RequestMapping("/test")
@Validated
public class TestController {

    @Resource
    private MultithreadingServiceImpl multithreadingService;

    @Resource
    private PersonOptionServiceImpl personOptionService;

    @Resource
    private SentinelServiceImpl sentinelService;

    @GetMapping("/testApi")
    public ResponseDataVO test(@RequestParam @Length(min = 2, max = 5) String name, @RequestParam @Pattern(regexp = "^[0-9]*$") String phone) {

        return ResponseDataVO.success("测试接口访问成功");
    }

    @GetMapping("/exceptApi")
    public ResponseDataVO testExcept() {
        throw new BusinessException("自定义异常");
    }

    @GetMapping("/taskExecutor")
    public ResponseDataVO taskExecutor(@RequestParam long times) {
        multithreadingService.testMulti(times);
        return ResponseDataVO.success("测试线程池接口");
    }

    @GetMapping("/taskExecutorExcept")
    public ResponseDataVO taskExecutorExcept(@RequestParam long times, @RequestParam long exceptNum) {
        multithreadingService.testMultiExcept(times, exceptNum);
        return ResponseDataVO.success("测试线程池接口");
    }

    @PostMapping("/insertPerson")
    public ResponseDataVO insertPerson(@RequestBody @Validated(Insert.class) PersonDTO personDTO) {
        personOptionService.insertPerson(personDTO);
        return ResponseDataVO.success("插入成功");
    }

    @PostMapping("/insertListPerson")
    public ResponseDataVO insertListPerson(@RequestBody @Validated(Insert.class) ValidationList<PersonDTO> personDTOS) {
        personOptionService.insertListPerson(personDTOS);
        return ResponseDataVO.success("集合插入成功");
    }

    @PostMapping("/updatePerson")
    public ResponseDataVO updatePerson(@RequestBody @Validated(Update.class) PersonDTO personDTO) {
        personOptionService.updatePerson(personDTO);
        return ResponseDataVO.success("更新成功");
    }

    @GetMapping("/selectPerson")
    public ResponseDataVO selectPerson(@RequestParam int pageIndex, @RequestParam int pageSize) {
        PageInfo pageInfo = personOptionService.selectPersonByPage(pageIndex, pageSize);
        return ResponseDataVO.success(pageInfo);
    }

    @GetMapping("/limit")
    @SentinelResource(value = "limit", defaultFallback = "allFallback", fallbackClass = {MyFallback.class},
            blockHandler = "allHandlerException", blockHandlerClass = {MyBlockException.class})
    public ResponseDataVO limit() throws InterruptedException {
        return ResponseDataVO.success(sentinelService.hello("测试QPS限流"));
    }
}
