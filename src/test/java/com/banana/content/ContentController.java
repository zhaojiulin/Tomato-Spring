package com.banana.content;

import com.banana.spring.anno.Autowired;
import com.banana.spring.constant.RequestMethod;
import com.banana.spring.web.anno.WebController;
import com.banana.spring.web.anno.RequestParam;
import com.banana.spring.web.anno.WebRequestMapping;
import com.banana.UserService;

import java.util.logging.Logger;

/**
 * @Description: Controller测试
 * @author zhaojiulin
 * @Date 2025/10/18 12:22
 * @version 1.0
 */
@WebController
public class ContentController {
    static final Logger logger = Logger.getLogger(ContentController.class.getName());
    @Autowired
    private UserService userService;
    @WebRequestMapping(value = "/info", method = RequestMethod.POST)
    public void index(@RequestParam("title") String title) {
        logger.info("index：" + title);
    }

    @WebRequestMapping(value = "/hello", method = RequestMethod.POST)
    public void hello() {
        userService.test();
    }
    @WebRequestMapping(value = "/testReturn", method = RequestMethod.GET)
    public String testReturn(@RequestParam("title") String title) {
        return userService.testReturn(title);
    }
}
