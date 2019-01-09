package com.spring.microservices.controllers;

import java.net.URI;
import java.util.List;
import java.util.Locale;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.mvc.ControllerLinkBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.*;

import com.spring.microservices.exceptions.UserNotFoundException;
import com.spring.microservices.services.UsersBSI;
import com.spring.microservices.vo.UserVO;

@RestController
public class UserController {

	@Autowired
	private UsersBSI userBS;
	
	//The name of the varaible should match with the method name
	//which we have defined as a @Bean.
	@Autowired
	private MessageSource messageSource;

	@GetMapping("/users")
	public List<UserVO> getAllUsers() {
		return userBS.getUsers();
	}

	// The Path variable name should be common in parameter and in request also.
	@GetMapping("/users/{userId}")
	public Resource<UserVO> getUserById(@PathVariable int userId) throws UserNotFoundException {
		UserVO userVO = userBS.getUser(userId);
		if (userVO == null) {
			throw new UserNotFoundException("User Not Found with " + (new Integer(userId).toString()));
		}
		
		//Adding HATEOAS
		Resource<UserVO>resource = new Resource<UserVO>(userVO);
		//We have imported all static methods of ControllerLinkBuilder and linkTo is also part of it.
		ControllerLinkBuilder linkTo=linkTo(methodOn(this.getClass()).getAllUsers());
		resource.add(linkTo.withRel("all-users"));
		return resource;
	}

	@PostMapping("/users")
	public ResponseEntity<Object> saveUser(@Valid @RequestBody UserVO user) {
		UserVO userVO = userBS.saveUser(user);
		//Using ResponseEntity to give proper response.
		URI loc = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(userVO.getId()).toUri();
		return ResponseEntity.created(loc).build();
	}
	
	/**
	 * Here we are using Request header and Internationalization or i18n.
	 * 
	 * If I have to add the request header in every method that is internationalized 
	 * then it is headache, so spring gives this feature, instead of writing this
	 * @RequestHeader(name = "Accept-Language", required = false) Locale locale
	 * in method parameter, we can directly use the below way.
	 * 
	 * @param locale
	 * @return
	 */
	@GetMapping("/hello-world-internalizalized")
	public String helloWorld() {
		//LocaleContextHolder this will automatically take the header which we are sending from Request.
		return messageSource.getMessage("good.morning.message", null, LocaleContextHolder.getLocale());
	}
}
