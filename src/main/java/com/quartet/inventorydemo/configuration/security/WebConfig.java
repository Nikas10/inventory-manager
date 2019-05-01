package com.quartet.inventorydemo.configuration.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebMvc
@ComponentScan("com.quartet.inventorydemo")
public class WebConfig implements WebMvcConfigurer {

  private final Environment env;

  @Autowired
  public WebConfig(Environment env) {
    this.env = env;
  }

  public void addViewControllers(ViewControllerRegistry registry) {
    registry.addRedirectViewController("/", "/home.html");
    registry.addRedirectViewController("/camunda/app", "/app/admin/default");
  }

  @Override
  public void addResourceHandlers(ResourceHandlerRegistry registry) {
    registry.addResourceHandler("/**").addResourceLocations("classpath:/static/");
    registry
        .addResourceHandler("swagger-ui.html")
        .addResourceLocations("classpath:/META-INF/resources/");
    registry
        .addResourceHandler("/webjars/**")
        .addResourceLocations("classpath:/META-INF/resources/webjars/");
  }

  @Override
  public void addCorsMappings(CorsRegistry registry) {
    // TODO: Remove after development
    registry.addMapping("/api/**").allowedOrigins("*");
  }
}
