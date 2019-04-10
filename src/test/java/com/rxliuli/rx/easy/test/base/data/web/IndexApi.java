package com.rxliuli.rx.easy.test.base.data.web;

import com.rxliuli.rx.easy.test.base.data.entity.Result;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author rxliuli
 */
@RestController
@RequestMapping
public class IndexApi {
    @GetMapping("/")
    public Result<String> index() {
        return new Result<String>().setData("index");
    }
}
