package com.ak.restskillsapi.controller;

import java.security.SecureRandom;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ak.restskillsapi.domain.DetailsDTO;
import com.ak.restskillsapi.domain.DetailsFullDTO;
import com.ak.restskillsapi.domain.DetailsNewDTO;
import com.ak.restskillsapi.domain.SaveSkillsRequest;
import com.ak.restskillsapi.domain.UserCreatedDTO;
import com.ak.restskillsapi.domain.UserFullDTO;
import com.ak.restskillsapi.domain.UserNewDTO;
import com.ak.restskillsapi.service.UserService;

@Controller
public class UserController {

	static final String AB = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
	static SecureRandom rnd = new SecureRandom();
	
	@Autowired
	private UserService userService;
	
	String randomString( int len ){
		
		   StringBuilder sb = new StringBuilder( len );
		   for( int i = 0; i < len; i++ ) 
		      sb.append( AB.charAt( rnd.nextInt(AB.length()) ) );
		   return sb.toString();
		}
	
	@RequestMapping(value = "/users", method = RequestMethod.POST)
	public @ResponseBody void addNewUser(@RequestBody UserNewDTO userNewDTO, HttpServletRequest request, String pass, HttpServletResponse response) {//, HttpServletResponse response) {
		String sessionId = request.getSession(true).getId();
		pass = randomString(20);
		userService.createNewUser(userNewDTO, sessionId, pass);
		String link = "http://localhost:8080/users/"+sessionId;
		response.setHeader("Location", link);
	}
	
	@RequestMapping(value = "/users/{id}", method = RequestMethod.GET)
	public @ResponseBody UserCreatedDTO retrieveUser (@PathVariable(value = "id") String id) {
		return userService.retrieveUser(id);
	}
	
	@RequestMapping(value = "users/skills", method = RequestMethod.PUT)
	public @ResponseBody UserFullDTO addSkillsToUser (@RequestBody SaveSkillsRequest saveSkillsRequest) {
		return userService.addSkillsToUser(saveSkillsRequest);
	}
	
	@RequestMapping(value = "/users/details/{id}", method = RequestMethod.GET)
	public @ResponseBody DetailsDTO getUserDetails(@PathVariable (value = "id") String id) {
		return userService.getUserDetails(id);
	}
	
	@RequestMapping(value = "/users/details/{id}", method = RequestMethod.PUT)
	public @ResponseBody void updateUserDetails(@PathVariable (value = "id") String id, @RequestBody DetailsNewDTO detailsNewDTO) {
		userService.updateUserDetails(detailsNewDTO, id);
	}
	
	@RequestMapping(value = "/users/alldetails/{userId}", method = RequestMethod.GET)
	public @ResponseBody DetailsFullDTO getAllDetails (@PathVariable(value = "userId") String userId) {
		return userService.getAllUserDetails(userId);
		
	}
}
