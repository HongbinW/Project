package com.mmall.common;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.json.MappingJacksonJsonView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
@Component
public class ExceptionResolver implements HandlerExceptionResolver{
    @Override
    public ModelAndView resolveException(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) {
        //object o 是具体的一个handler
        log.error("{} Exception",httpServletRequest.getRequestURI(),e);
        ModelAndView modelAndView = new ModelAndView(new MappingJacksonJsonView());  //返回给前段应该是个json,且因为我们的jackson版本为1.9，因此要使用MappingJacksonJsonView,若使用的是jackson2.0,则使用MappingJackson2JsonView
        //模仿ServerResponse来给客户端返回信息
        modelAndView.addObject("status",ResponseCode.ERROR.getCode());
        modelAndView.addObject("msg","接口异常，详情请查看服务端日志的异常信息");
        modelAndView.addObject("data",e.toString());    //exception tostring just get simple exception information

        return modelAndView;
    }
}
