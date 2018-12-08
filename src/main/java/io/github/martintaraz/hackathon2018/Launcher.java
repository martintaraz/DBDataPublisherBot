package io.github.martintaraz.hackathon2018;

import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Properties;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

import io.github.martintaraz.hackathon2018.models.stations.StationQuery;


public class Launcher {

	private Properties properties;
	
	public static void main(String[] args) throws Exception {
		new Launcher().run();
	}
	
	public void run() throws Exception {
		properties = new Properties();
		try(Reader r = Files.newBufferedReader(new File("config.properties").toPath(), StandardCharsets.UTF_8)) {
			properties.load(r);
		}
		File stations = new File("files/stations.json");
		download("http://api.deutschebahn.com/stada/v2/stations", stations);
		
		new StationImporter().run(properties, new Gson().fromJson(Files.newBufferedReader(stations.toPath(), StandardCharsets.UTF_8), StationQuery.class));
	}

	private void download(String url, File file) throws UnirestException, IOException {
		if(file.exists())
			return;
		String content = Unirest
			.get(url)
			.header("Authorization", "Bearer "+properties.getProperty("db.token"))
			.asString()
			.getBody();
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		String formatted = gson.toJson(gson.fromJson(content, JsonElement.class));
		Files.write(file.toPath(), formatted.getBytes(StandardCharsets.UTF_8));
	}
}
