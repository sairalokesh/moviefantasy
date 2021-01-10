package com.lokesh.movies.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.lokesh.movies.domain.Movie;
import com.lokesh.movies.service.MovieService;
import com.mashape.unirest.http.exceptions.UnirestException;

@Controller
public class HomeController {
	
	@Autowired private MovieService movieService;

	@GetMapping(value = "/")
	public String home() {
		return "redirect:/home";
	}

	@GetMapping(value = "/home")
	public String homeMovies(Model model)  throws UnirestException {
		List<Movie> movies = movieService.getAllMovies();
		model.addAttribute("movies", movies);
		model.addAttribute("title", "home");
		return "movies/home";

	}

}
