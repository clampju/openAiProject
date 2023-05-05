package com.openai;

import io.github.asleepyfish.annotation.EnableChatGPT;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableChatGPT

public class OpenAiProjectApplication {

  public static void main(String[] args) {
    SpringApplication.run(OpenAiProjectApplication.class, args);
  }
}
