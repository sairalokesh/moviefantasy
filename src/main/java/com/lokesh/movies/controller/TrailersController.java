package com.lokesh.movies.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.lokesh.movies.dto.MovieTrailers;
import com.lokesh.movies.service.MovieService;
import com.mashape.unirest.http.exceptions.UnirestException;

@Controller
public class TrailersController {
	
	@Autowired private MovieService movieService;
	
	@GetMapping(value = "trailers")
	public String trailers(@RequestParam String movieId, Model model) throws UnirestException {	
		MovieTrailers movieTrailers = movieService.getMovieDetailsByMovieId(movieId, "trailers");
		model.addAttribute("movieTrailers", movieTrailers);
		return "movies/trailers";
	}

}
