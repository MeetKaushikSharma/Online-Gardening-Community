package com.gardening;

import com.gardening.dao.CommentDao;
import com.gardening.dao.PostDao;
import com.gardening.dao.ProjectDao;
import com.gardening.dao.UserDao;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SpringBootApplication
public class GardeningCommunityApplication {

    public static void main(String[] args) {
        SpringApplication.run(GardeningCommunityApplication.class, args);
    }

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                String allowedOrigins = System.getenv("CORS_ALLOWED_ORIGINS");
                if (allowedOrigins == null || allowedOrigins.isEmpty()) {
                    // Default to localhost for development
                    allowedOrigins = "http://localhost:5173,http://localhost:3000";
                }

                registry.addMapping("/api/**")
                        .allowedOrigins(allowedOrigins.split(","))
                        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                        .allowedHeaders("*")
                        .allowCredentials(true);
            }
        };
    }

    @Bean
    public UserDao userDao() {
        return new UserDao();
    }

    @Bean
    public PostDao postDao() {
        return new PostDao();
    }

    @Bean
    public CommentDao commentDao() {
        return new CommentDao();
    }

    @Bean
    public ProjectDao projectDao() {
        return new ProjectDao();
    }
}
