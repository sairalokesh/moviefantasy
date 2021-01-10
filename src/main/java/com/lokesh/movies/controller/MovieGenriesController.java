package com.lokesh.movies.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.lokesh.movies.domain.MovieGenres;
import com.lokesh.movies.service.MovieService;
import com.mashape.unirest.http.exceptions.UnirestException;

@Controller
public class MovieGenriesController {
	
	@Autowired private MovieService movieService;
	
	@GetMapping(value = "/genres")
	public String genres(Model model) throws UnirestException {
		List<MovieGenres> movieGenres = movieService.getMovieGenries();
		model.addAttribute("movieGenres", movieGenres);
		model.addAttribute("title", "genres");
		return "movies/genres";
	}

}
