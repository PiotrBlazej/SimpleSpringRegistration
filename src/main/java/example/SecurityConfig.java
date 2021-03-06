package example;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter{
	
	@Autowired
	private DataSource dataSource;
	@Bean
	public BCryptPasswordEncoder passwordEncoder() {
		return  new BCryptPasswordEncoder();
	}
	
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.jdbcAuthentication().dataSource(dataSource).passwordEncoder(passwordEncoder())
		.usersByUsernameQuery("SELECT email, password, active FROM user where email=?")
		.authoritiesByUsernameQuery("SELECT email, role FROM user WHERE email=?");
	
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.
		authorizeRequests()
			.antMatchers("/").permitAll()
			.antMatchers("/signin").permitAll()
			.antMatchers("/signup").permitAll()
			.antMatchers("/confirm").permitAll()
			.antMatchers("/").hasAuthority("User").anyRequest()
			.authenticated().and().csrf().disable().formLogin()
			.loginPage("/signin").usernameParameter("email").passwordParameter("password")
		
			.defaultSuccessUrl("/")
			.and().logout().logoutUrl("/signout").logoutSuccessUrl("/")
			.and().exceptionHandling()
.accessDeniedPage("/access-denied");
	}
	@Override
	public void configure(WebSecurity web) throws Exception {
	    web
	       .ignoring()
	       .antMatchers("/resources/**", "/static/**", "/css/**", "/js/**", "/images/**");
}



}
