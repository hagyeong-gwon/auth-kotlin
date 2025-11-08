package demo.kotlin.auth.infrastructure.config

import org.springframework.context.annotation.Configuration
import org.springframework.data.jpa.repository.config.EnableJpaAuditing

// TODO: master, slave connection 구분하기
@Configuration
@EnableJpaAuditing
class JpaConfig
