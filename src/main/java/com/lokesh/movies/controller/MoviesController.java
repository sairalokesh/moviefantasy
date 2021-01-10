package com.lokesh.movies.controller;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lokesh.movies.domain.Movie;
import com.lokesh.movies.domain.Person;
import com.lokesh.movies.dto.MovieTrailers;
import com.lokesh.movies.service.MovieService;
import com.lokesh.movies.util.MovieUtil;
import com.mashape.unirest.http.exceptions.UnirestException;

@Controller
public class MoviesController {
	
	@Autowired private MovieService movieService;
	
	@GetMapping(value = "movies")
	public String movies(Model model) throws UnirestException {
		model.addAttribute("type", ""); // search, person or generic
		model.addAttribute("query", ""); // search query
		model.addAttribute("queryKey", ""); // text of display Key : SearchKey, Generic Name or Person Name
		model.addAttribute("queryId", ""); // Either genericId or PersonId
		model.addAttribute("searchType", "movie"); // search movie or person (this is only for search)
		model.addAttribute("title", "movies");
		return "movies/movies";
	}
	
	@GetMapping(value = "/movies/search")
	public String movies(@RequestParam String query, @RequestParam String searchType, Model model) throws UnirestException {
		model.addAttribute("type", "search"); // search
		model.addAttribute("query", query); // what user search
		model.addAttribute("queryKey", "SearchKey");  // text of display Key : SearchKey
		model.addAttribute("queryId", ""); // nothing here
		model.addAttribute("searchType", searchType); // search movie or person (this is only for search)
		if(searchType.equalsIgnoreCase("person")) {
			model.addAttribute("title", "persons");
		} else {
			model.addAttribute("title", "movies");
		}
		
		return "movies/movies";
	}
	
	@GetMapping(value = "/movies/genre")
	public String moviesByGeneric(@RequestParam String genericId, Model model) throws UnirestException {
		List<Long> genricIds = new ArrayList<>();
		genricIds.add(Long.parseLong(genericId));
		String genericName = MovieUtil.getMovieGenries(genricIds);
		
		model.addAttribute("type", "generic"); // generic
		model.addAttribute("queryId", genericId); // genericId
		model.addAttribute("query", ""); // nothing
		model.addAttribute("queryKey", "Generic Name"); // text display Generic Name
		model.addAttribute("searchType", "movie"); // searchType is movie
		model.addAttribute("genericName", genericName); // Display Name generic name what user choose
		model.addAttribute("title", "genres");
		return "movies/movies";
	}
	
	@GetMapping(value = "/persons")
	public String persons(Model model) throws UnirestException {
		model.addAttribute("type", "person"); // person
		model.addAttribute("queryId", ""); // nothing
		model.addAttribute("query", ""); // nothing
		model.addAttribute("queryKey", ""); // nothing
		model.addAttribute("searchType", "person"); // search type is person
		model.addAttribute("title", "persons");
		return "movies/movies";
	}
	
	@GetMapping(value = "/person/movies")
	public String moviesByPersonId(@RequestParam String personId, Model model) throws UnirestException {
		Person person = movieService.getPersonByPersonId(personId);
		model.addAttribute("type", "personMovies");
		model.addAttribute("queryId", personId);
		model.addAttribute("query", "");
		model.addAttribute("queryKey", "Person Name");
		model.addAttribute("searchType", "movie");
		model.addAttribute("personName", person.getName());
		model.addAttribute("title", "persons");
		return "movies/movies";
	}
	
	@GetMapping(value = "/language/movies")
	public String moviesByLanguageId(@RequestParam String languageId, @RequestParam String languageName, Model model) throws UnirestException {
		model.addAttribute("type", "languageMovies");
		model.addAttribute("queryId", languageId);
		model.addAttribute("query", "");
		model.addAttribute("queryKey", "Language Name");
		model.addAttribute("searchType", "movie");
		model.addAttribute("languageName", languageName);
		model.addAttribute("title", "languages");
		return "movies/movies";
	}

	@GetMapping(value = "/movies/all")
	public String moviesByIndex(@RequestParam String pageIndex, @RequestParam String type, @RequestParam String query, @RequestParam String queryId, @RequestParam String searchType, Model model) throws UnirestException, UnsupportedEncodingException {
		if(searchType.equalsIgnoreCase("movie") && type.equalsIgnoreCase("personMovies") && pageIndex.equalsIgnoreCase("1")) {
			List<Movie> movies = movieService.getMoviesByPersonId(queryId);
			model.addAttribute("movies", movies);
		} else if(!type.equalsIgnoreCase("personMovies")) {
			JSONArray jsonArray = movieService.getMoviesByIndexOrSearchOrGeneric(pageIndex, type, query, queryId,  searchType);
			Gson gson = new Gson();
			if(jsonArray!=null) {
				if(searchType.equalsIgnoreCase("movie") && !type.equalsIgnoreCase("personMovies")) {
					Type responseType = new TypeToken<List<Movie>>() {
					}.getType();
					List<Movie> movies = gson.fromJson(jsonArray.toString(), responseType);
					model.addAttribute("movies", movies);
				} else {
					Type responseType = new TypeToken<List<Person>>() {
					}.getType();
					List<Person> persons = gson.fromJson(jsonArray.toString(), responseType);
					model.addAttribute("persons", persons);
				}
			}
		}
		model.addAttribute("searchType", searchType);
		return "movies/appendmovies";
	}
	
	@GetMapping(value = "/movie/details")
	public String showMovie(@RequestParam String movieId, @RequestParam String ipAddress, Model model) throws UnirestException, UnsupportedEncodingException {
		MovieTrailers movie = movieService.getMovieDetailsByMovieId(movieId, "show");
		if(movie !=null && movie.getMovie() != null && movie.getMovie().getRuntime() != null) {
			movie.getMovie().setConvertRunTime(MovieUtil.convertMovieTiming(movie.getMovie().getRuntime()));
		}
		String movieTicket = movieService.getMovieTicketByMovieIdAndTicketId(movie.getMovie().getImdb_id(), ipAddress);
		if(StringUtils.hasText(movieTicket)) {
			movie.getMovie().setMovieLink("https://videospider.stream/getvideo?key=" + MovieUtil.userKey + "&video_id=" + movie.getMovie().getImdb_id() + "&ticket=" + movieTicket);
		} else {
			movie.getMovie().setMovieLink("https://streamvideo.link/getvideo?key=" + MovieUtil.userKey + "&video_id=" + movie.getMovie().getImdb_id());
		}
		model.addAttribute("movie", movie);
		model.addAttribute("title", movie.getMovie().getTitle());
		return "movies/showmovie";
	}

}
