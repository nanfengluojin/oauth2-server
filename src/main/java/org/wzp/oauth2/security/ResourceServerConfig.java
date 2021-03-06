package org.wzp.oauth2.security;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.wzp.oauth2.config.CustomConfig;

import javax.annotation.Resource;

/**
 * @Author: zp.wei
 * @DATE: 2020/8/28 16:57
 */
@Configuration
@EnableResourceServer
public class ResourceServerConfig extends ResourceServerConfigurerAdapter {

    @Resource
    private CustomAccessDeniedHandler customAccessDeniedHandler;

    @Override
    public void configure(ResourceServerSecurityConfigurer resources) {
        //访问失败返回信息
        resources.authenticationEntryPoint(new AuthExceptionEntryPoint()).accessDeniedHandler(customAccessDeniedHandler);
        resources.resourceId(CustomConfig.resourceId).stateless(true);
    }


    /**
     * 配置拦截规则
     *
     * @param http
     * @throws Exception
     */
    @Override
    public void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .exceptionHandling().authenticationEntryPoint(new AuthExceptionEntryPoint()).and()// 解决匿名用户访问无权限资源时的异常
                .authorizeRequests()
                .antMatchers(HttpMethod.OPTIONS).permitAll()
                .antMatchers("/back/user/login", "/common/getTime", "/common/getCode").permitAll()
                .antMatchers("/swagger-ui.html", "/swagger-ui/**", "/swagger-resources/**", "/v3/api-docs/**", "/webjars/**", "/mqtt.html").permitAll()
                .antMatchers("/", "/doc.html", "/v2/api-docs", "/swagger-resources/**", "/swagger-ui.html**", "/webjars/**").permitAll()
                .anyRequest().authenticated().and()
                .httpBasic().and();
    }

}
