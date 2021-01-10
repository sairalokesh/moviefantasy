package com.lokesh.movies.service.impl;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.json.JSONArray;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lokesh.movies.domain.Languages;
import com.lokesh.movies.domain.Movie;
import com.lokesh.movies.domain.MovieCastCrew;
import com.lokesh.movies.domain.MovieGenres;
import com.lokesh.movies.domain.Person;
import com.lokesh.movies.domain.Trailer;
import com.lokesh.movies.dto.MovieTrailers;
import com.lokesh.movies.service.MovieService;
import com.lokesh.movies.util.MovieUtil;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

@Service
public class MovieServiceImpl implements MovieService {

	@Override
	public List<Movie> getAllMovies() throws UnirestException {
		String url = "https://api.themoviedb.org/3/movie/now_playing?api_key=" + MovieUtil.apiKey + "&page=1";
		HttpResponse<JsonNode> jsonResponse = Unirest.get(url).asJson();
		JSONArray jsonArray = jsonResponse.getBody().getObject().getJSONArray("results");
		Gson gson = new Gson();
		Type type = new TypeToken<List<Movie>>() {
		}.getType();
		List<Movie> movies = gson.fromJson(jsonArray.toString(), type);
		if (movies != null && movies.size() > 0) {
			for (Movie movie : movies) {
				movie.setGenrics(MovieUtil.getMovieGenries(movie.getGenre_ids()));
			}
		}
		return movies;
	}
	
	@Override
	public List<MovieGenres> getMovieGenries() throws UnirestException {
		String url = "https://api.themoviedb.org/3/genre/movie/list?api_key=" + MovieUtil.apiKey;
		HttpResponse<JsonNode> jsonResponse = Unirest.get(url).asJson();
		JSONArray jsonArray = jsonResponse.getBody().getObject().getJSONArray("genres");
		Gson gson = new Gson();
		Type type = new TypeToken<List<MovieGenres>>() {}.getType();
		List<MovieGenres> movieGenres = gson.fromJson(jsonArray.toString(), type);
		return movieGenres;
	}
	
	@Override
	public Person getPersonByPersonId(String personId) throws UnirestException {
		String url = "https://api.themoviedb.org/3/person/" + personId + "?api_key=" + MovieUtil.apiKey;
		HttpResponse<String> response = Unirest.get(url).asString();
		Gson gson = new Gson();
		Type movieType = new TypeToken<Person>() {}.getType();
		Person person = gson.fromJson(response.getBody(), movieType);
		return person;
	}
	
	
	
	
	
	
	
	
	
	
	

	@Override
	public MovieTrailers getMovieDetailsByMovieId(String movieId, String pageType) throws UnirestException {
		HttpResponse<String> response = Unirest.get("https://api.themoviedb.org/3/movie/" + movieId + "?api_key=" + MovieUtil.apiKey).asString();
		HttpResponse<JsonNode> jsonResponse = Unirest.get("https://api.themoviedb.org/3/movie/" + movieId + "/videos?api_key=" + MovieUtil.apiKey).asJson();
		HttpResponse<JsonNode> castCrewResponse = Unirest.get("https://api.themoviedb.org/3/movie/"+movieId+"/credits?api_key=" + MovieUtil.apiKey).asJson();
		JSONArray casts = castCrewResponse.getBody().getObject().getJSONArray("cast");
		JSONArray crews = castCrewResponse.getBody().getObject().getJSONArray("crew");
		Gson gson = new Gson();
		
		JSONArray trailers = jsonResponse.getBody().getObject().getJSONArray("results");
		Type type = new TypeToken<Movie>() {}.getType();
		Type trailer = new TypeToken<List<Trailer>>() {}.getType();
		
		Type cast = new TypeToken<List<MovieCastCrew>>() {}.getType();
		List<MovieCastCrew> movieCasts = gson.fromJson(casts.toString(), cast);
		
		Type crew = new TypeToken<List<MovieCastCrew>>() {}.getType();
		List<MovieCastCrew> movieCrews = gson.fromJson(crews.toString(), crew);
		Movie movie = gson.fromJson(response.getBody(), type);
		if(movie != null && movie.getGenres() != null && movie.getGenres().size() > 0) {
			String genres =  movie.getGenres().stream().map(MovieGenres::getName).collect(Collectors.joining(", "));
			movie.setGenrics(genres);
		}

		List<Trailer> movieTrailers = gson.fromJson(trailers.toString(), trailer);
		if (movieTrailers != null && movieTrailers.size() > 0 && movieTrailers.size() == 1 && !pageType.equalsIgnoreCase("show")) {
			movieTrailers.addAll(movieTrailers);
		}
		MovieTrailers movieTrailer = new MovieTrailers();
		movieTrailer.setMovie(movie);
		movieTrailer.setTrailers(MovieUtil.getMovieTrailers(movieTrailers, movie));
		movieTrailer.setCasts(movieCasts);
		movieTrailer.setCrews(movieCrews);
		return movieTrailer;
	}

	@Override
	public JSONArray getMoviesByIndexOrSearchOrGeneric(String pageIndex, String type, String query, String queryId, String SearchType) throws UnirestException, UnsupportedEncodingException {
		String url = "";
		if(StringUtils.hasText(type) && !type.equalsIgnoreCase("null")) {
			if(StringUtils.hasText(type) && type.equalsIgnoreCase("search")) {
				if(StringUtils.hasText(query)) {
					String search = URLEncoder.encode(query, "UTF-8");
					url = "https://api.themoviedb.org/3/search/" + SearchType + "?query=" + search +  "&api_key=" + MovieUtil.apiKey + "&page=" + pageIndex;
				} else {
					url = "https://api.themoviedb.org/3/"+SearchType+"/popular?api_key=" + MovieUtil.apiKey + "&page=" + pageIndex;
				}
			} else if(StringUtils.hasText(type) && type.equalsIgnoreCase("generic")) {
				url = "https://api.themoviedb.org/3/discover/movie?api_key=" + MovieUtil.apiKey + "&page=" + pageIndex + "&with_genres=" + queryId +"&sort_by=popularity.desc";
			} else if(StringUtils.hasText(type) && type.equalsIgnoreCase("person")) {
				url = "https://api.themoviedb.org/3/" + SearchType + "/popular?api_key=" + MovieUtil.apiKey + "&page=" + pageIndex;
			} else if(StringUtils.hasText(type) && type.equalsIgnoreCase("personMovies")) {
				url = "https://api.themoviedb.org/3/person/" + queryId + "/movie_credits?api_key=" + MovieUtil.apiKey;
			}  else if(StringUtils.hasText(type) && type.equalsIgnoreCase("languageMovies")) {
				url = "https://api.themoviedb.org/3/discover/movie?api_key=" + MovieUtil.apiKey + "&page=" + pageIndex + "&with_original_language=" + queryId;
			}
		} else {
			url = "https://api.themoviedb.org/3/"+SearchType+"/popular?api_key=" + MovieUtil.apiKey + "&page=" + pageIndex;
		}	
		HttpResponse<JsonNode> jsonResponse = Unirest.get(url).asJson();
		JSONArray jsonArray = jsonResponse.getBody().getObject().getJSONArray("results");
		return jsonArray;
	}
	
	@Override
	public List<Movie> getMoviesByPersonId(String personId) throws UnirestException {
		String url = "https://api.themoviedb.org/3/person/" + personId + "/movie_credits?api_key=" + MovieUtil.apiKey;
		HttpResponse<JsonNode> jsonResponse = Unirest.get(url).asJson();
		JSONArray cast = jsonResponse.getBody().getObject().getJSONArray("cast");
		JSONArray crew = jsonResponse.getBody().getObject().getJSONArray("crew");
		
		Gson gson = new Gson();
		Type type = new TypeToken<List<Movie>>() {}.getType();
		List<Movie> castmovies = gson.fromJson(cast.toString(), type);
		List<Movie> crewmovies = gson.fromJson(crew.toString(), type);
		castmovies.addAll(crewmovies);
		
		List<Movie> movies = new ArrayList<Movie>();
		List<Long> duplicates = new ArrayList<Long>();
		if(castmovies!=null && castmovies.size()>0) {
			for(Movie movie : castmovies) {
				if(!duplicates.contains(movie.getId())) {
					duplicates.add(movie.getId());
					movies.add(movie);
				}
			}
		}
		return movies;
	}

	@Override
	public List<Languages> getMovieLanguages() throws UnirestException {
		String url = "https://api.themoviedb.org/3/configuration/languages?api_key=" + MovieUtil.apiKey;
		HttpResponse<JsonNode> jsonResponse  = Unirest.get(url).asJson();
		JSONArray response = jsonResponse.getBody().getArray();
		Gson gson = new Gson();
		Type type = new TypeToken<List<Languages>>() {
		}.getType();
		List<Languages> languages = gson.fromJson(response.toString(), type);
		return languages;
	}

	@Override
	public String getMovieTicketByMovieIdAndTicketId(String movieId, String ipAddress) throws UnirestException {
		try {
			HttpResponse<String> response = Unirest.get("https://videospider.in/getticket.php?key=" + MovieUtil.userKey + "&secret_key=" + MovieUtil.secretKey + "&video_id=" + movieId + "&ip=" + ipAddress).asString();
			return response.getBody();
		}catch (Exception e) {
			return null;
		}
		
	}
}
