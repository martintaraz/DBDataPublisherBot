package io.github.martintaraz.dbdatapublisherbot;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Properties;
import java.util.Set;
import java.util.stream.Collectors;

import org.wikidata.wdtk.datamodel.implementation.GlobeCoordinatesValueImpl;
import org.wikidata.wdtk.datamodel.implementation.ItemIdValueImpl;
import org.wikidata.wdtk.datamodel.implementation.PropertyIdValueImpl;
import org.wikidata.wdtk.datamodel.implementation.ReferenceImpl;
import org.wikidata.wdtk.datamodel.implementation.SnakGroupImpl;
import org.wikidata.wdtk.datamodel.implementation.StatementImpl;
import org.wikidata.wdtk.datamodel.implementation.StringValueImpl;
import org.wikidata.wdtk.datamodel.implementation.TermImpl;
import org.wikidata.wdtk.datamodel.implementation.ValueSnakImpl;
import org.wikidata.wdtk.datamodel.interfaces.EntityIdValue;
import org.wikidata.wdtk.datamodel.interfaces.GlobeCoordinatesValue;
import org.wikidata.wdtk.datamodel.interfaces.ItemDocument;
import org.wikidata.wdtk.datamodel.interfaces.ItemIdValue;
import org.wikidata.wdtk.datamodel.interfaces.MonolingualTextValue;
import org.wikidata.wdtk.datamodel.interfaces.Reference;
import org.wikidata.wdtk.datamodel.interfaces.StatementGroup;
import org.wikidata.wdtk.datamodel.interfaces.StatementRank;
import org.wikidata.wdtk.datamodel.interfaces.StringValue;
import org.wikidata.wdtk.util.WebResourceFetcherImpl;
import org.wikidata.wdtk.wikibaseapi.ApiConnection;
import org.wikidata.wdtk.wikibaseapi.WbGetEntitiesSearchData;
import org.wikidata.wdtk.wikibaseapi.WikibaseDataEditor;
import org.wikidata.wdtk.wikibaseapi.WikibaseDataFetcher;
import org.wikidata.wdtk.wikibaseapi.apierrors.MediaWikiApiErrorException;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.MoreCollectors;

import io.github.martintaraz.dbdatapublisherbot.models.stations.EVANumber;
import io.github.martintaraz.dbdatapublisherbot.models.stations.RiL100Identifier;
import io.github.martintaraz.dbdatapublisherbot.models.stations.Station;
import io.github.martintaraz.dbdatapublisherbot.models.stations.StationQuery;

public class StationImporter {
	
	private static final String siteIri = "http://www.wikidata.org/entity/";
	private static final PropertyIdValueImpl INSTANCE_OF = new PropertyIdValueImpl("P31", siteIri);
	private static final PropertyIdValueImpl LOCATION = new PropertyIdValueImpl("P131", siteIri);
	private static final PropertyIdValueImpl ADDRESS = new PropertyIdValueImpl("P969", siteIri);
	private static final PropertyIdValueImpl CATEGORY = new PropertyIdValueImpl("P5105", siteIri);
	private static final PropertyIdValueImpl COORDINATES = new PropertyIdValueImpl("P625", siteIri);
	private static final PropertyIdValueImpl STATION_CODE = new PropertyIdValueImpl("P296", siteIri);
	private static final List<Reference> REFERENCE = Collections.singletonList(new ReferenceImpl(
		Collections.singletonList(new SnakGroupImpl(
			Collections.singletonList(new ValueSnakImpl(
				new PropertyIdValueImpl("P854", siteIri),
				new StringValueImpl("http://data.deutschebahn.com/dataset/stada-stationsdaten")
			))
		))
	)); 
	private static final Map<String, ItemIdValue> FEDERAL_STATES = ImmutableMap
		.<String, ItemIdValue>builder()
		.put("Nordrhein-Westfalen", 	new ItemIdValueImpl("Q1198", siteIri))
		.put("Baden-Württemberg", 		new ItemIdValueImpl("Q985", siteIri))
		.put("Bayern", 					new ItemIdValueImpl("Q980", siteIri))
		.put("Niedersachsen", 			new ItemIdValueImpl("Q1197", siteIri))
		.put("Sachsen", 				new ItemIdValueImpl("Q1202", siteIri))
		.put("Schleswig-Holstein", 		new ItemIdValueImpl("Q1194", siteIri))
		.put("Berlin", 					new ItemIdValueImpl("Q64", siteIri))
		.put("Brandenburg", 			new ItemIdValueImpl("Q1208", siteIri))
		.put("Rheinland-Pfalz", 		new ItemIdValueImpl("Q1200", siteIri))
		.put("Hessen", 					new ItemIdValueImpl("Q1199", siteIri))
		.put("Hamburg", 				new ItemIdValueImpl("Q1055", siteIri))
		.put("Mecklenburg-Vorpommern", 	new ItemIdValueImpl("Q1196", siteIri))
		.put("Thüringen", 				new ItemIdValueImpl("Q1205", siteIri))
		.put("Sachsen-Anhalt", 			new ItemIdValueImpl("Q1206", siteIri))
		.put("Saarland", 				new ItemIdValueImpl("Q1201", siteIri))
		.put("Bremen", 					new ItemIdValueImpl("Q24879", siteIri))
		.build();

	private static final Set<String> RAILWAYS_STATION_TYPES = new HashSet<>(Arrays.asList("Q55488","Q55484","Q55485","Q55490","Q55491","Q55493","Q55494","Q55677","Q55678","Q178887","Q569333","Q693145","Q801748","Q801751","Q801754","Q801756","Q801762","Q928830","Q1147171","Q1283745","Q1335652","Q1406904","Q1434842","Q1478755","Q1483049","Q1545726","Q1567913","Q1793804","Q1924069","Q2105978","Q2138274","Q2142091","Q2657895","Q2778255","Q2794145","Q3098283","Q3201814","Q4540061","Q4663385","Q5283604","Q5283609","Q5283610","Q5760716","Q7240482","Q7886778","Q7888186","Q8015548","Q11424045","Q11519484","Q12013402","Q12339507","Q15855903","Q15921844","Q17158079","Q18209808","Q18543139","Q18681690","Q18681691","Q18681692","Q20819285","Q20819288","Q20819294","Q20819297","Q20819299","Q25109570","Q27996466","Q28109487","Q28837381","Q43401036","Q50060886","Q54168722","Q56822863","Q58484445","Q1562299","Q55492","Q2451577","Q261068","Q597668","Q2630536","Q2630690","Q190352","Q831844","Q27996460","Q117939","Q168565","Q3510579","Q4312270","Q12055881","Q14562709","Q14641099","Q14646407","Q15898661","Q18516630","Q23213972","Q23305149","Q45315673","Q56156507","Q5135349","Q54883424","Q54883641","Q54883692","Q54883881","Q54883938","Q54884015","Q54884061","Q7250393","Q332496","Q519608","Q41601293","Q54883781","Q54883829"));
	
	private static final ItemIdValue[] CATEGORY_ENTITIES = {
		null,
		new ItemIdValueImpl("Q18681579", siteIri),
		new ItemIdValueImpl("Q18681660", siteIri),
		new ItemIdValueImpl("Q18681688", siteIri),
		new ItemIdValueImpl("Q18681690", siteIri),
		new ItemIdValueImpl("Q18681691", siteIri),
		new ItemIdValueImpl("Q18681692", siteIri),
		new ItemIdValueImpl("Q18681693", siteIri)
	};
	
	
	private static final String DE = "de";

	public void run(Properties properties, StationQuery query) throws Exception {
		WebResourceFetcherImpl.setUserAgent("DB Date Exporter");
		ApiConnection connection = ApiConnection.getWikidataApiConnection();
		connection.login(properties.getProperty("wikidata.username"), properties.getProperty("wikidata.password"));
		WikibaseDataEditor editor = new WikibaseDataEditor(connection, siteIri);
		editor.setEditAsBot(true);
		editor.disableEditing();
		WikibaseDataFetcher wbdf = new WikibaseDataFetcher(connection, siteIri);
		
		System.out.println("Stations: "+query.getResult().size());
		int counter=0;
		for(Station station : query.getResult()) {
			System.out.println("\n"+(counter++)+" "+station.getName());
			
			ItemDocument entity = findEntity(wbdf, station);
			if(entity != null) {
				StringBuilder message = new StringBuilder();
				System.out.println("\t"+entity.getEntityId());

				//----NAME
				TermImpl name = new TermImpl(DE, station.getName());
				if((
						entity.getLabels().get(DE) == null
						|| !entity.getLabels().get(DE).equals(name)
					) && (
						!entity.getAliases().containsKey(DE)
						|| !entity.getAliases().get(DE).contains(name)
					)
				) {
					message(message, "added name "+name);
					
					
					List<MonolingualTextValue> l = entity.getAliases().getOrDefault(DE, Collections.emptyList());
					l = new ArrayList<>(l);
					l.add(name);
					entity = entity.withAliases(DE, l);
				}
				
				
				//----STATE
				if(!entity.hasStatement(LOCATION) && station.getFederalState() != null) {
					message(message, "added location "+station.getFederalState());
					entity = entity.withStatement(new StatementImpl(
						null,
						StatementRank.NORMAL,
						new ValueSnakImpl(LOCATION, FEDERAL_STATES.get(station.getFederalState())),
						Collections.emptyList(),
						REFERENCE,
						entity.getEntityId()
					));
				}
				
				
				//----ADDRESS
				if(!entity.hasStatement(ADDRESS) && station.getMailingAddress() != null && station.getMailingAddress().getStreet() != null) {
					StringBuilder address = new StringBuilder(station.getMailingAddress().getStreet());
					if(station.getMailingAddress().getHouseNumber() != null)
						address.append(' ').append(station.getMailingAddress().getHouseNumber());
					address.append(", ").append(station.getMailingAddress().getZipcode()).append(' ').append(station.getMailingAddress().getCity());
					
					message(message, "added address "+address);
					entity = entity.withStatement(new StatementImpl(
						null,
						StatementRank.NORMAL,
						new ValueSnakImpl(ADDRESS, new StringValueImpl(address.toString())),
						Collections.emptyList(),
						REFERENCE,
						entity.getEntityId()
					));
				}
				
				
				//----category
				if(!entity.hasStatement(CATEGORY) && station.getCategory() != null && station.getCategory() > 0) {
					message(message, "added category "+CATEGORY_ENTITIES[station.getCategory()]);
					entity = entity.withStatement(new StatementImpl(
						null,
						StatementRank.NORMAL,
						new ValueSnakImpl(CATEGORY, CATEGORY_ENTITIES[station.getCategory()]),
						Collections.emptyList(),
						REFERENCE,
						entity.getEntityId()
					));
				}
				
				
				//----COORDINATES
				if(station.getEvaNumbers() != null && !station.getEvaNumbers().isEmpty()) {
					Optional<EVANumber> number = station.getEvaNumbers().stream().filter(evan->evan.isIsMain() == Boolean.TRUE).collect(MoreCollectors.toOptional());
					if(number.isPresent() && number.get().getGeographicCoordinates() != null) {
						GlobeCoordinatesValueImpl coords = new GlobeCoordinatesValueImpl(
							number.get().getGeographicCoordinates().getCoordinates().get(1),
							number.get().getGeographicCoordinates().getCoordinates().get(0),
							0,
							"Earth"
							);

						if(!entity.hasStatement(COORDINATES)) {
							message(message, "added coordinates "+coords);
							entity = entity.withStatement(new StatementImpl(
								null,
								StatementRank.NORMAL,
								new ValueSnakImpl(COORDINATES, coords),
								Collections.emptyList(),
								REFERENCE,
								entity.getEntityId()
							));
						}
						else if(coords.toString().length() > entity.findStatementGroup(COORDINATES).getStatements().stream().mapToInt(st -> ((GlobeCoordinatesValue)st.getValue()).toString().length()).max().getAsInt()) {
							message(message, "improved coordinates "+entity.findStatementGlobeCoordinatesValue(COORDINATES)+" -> "+coords);
							entity = entity.withStatement(new StatementImpl(
								null,
								StatementRank.NORMAL,
								new ValueSnakImpl(COORDINATES, coords),
								Collections.emptyList(),
								REFERENCE,
								entity.getEntityId()
							));
						}
					}
				}
				
				
				
				//----station code
				if(station.getRil100Identifiers() != null) {
					StatementGroup stationCodeGroup = entity.findStatementGroup(STATION_CODE);
					for(RiL100Identifier id : station.getRil100Identifiers()) {
						if(stationCodeGroup == null || stationCodeGroup.getStatements().stream().noneMatch(st->((StringValue)st.getValue()).getString().equals(id.getRilIdentifier()))) {
							message(message, "added station code "+id.getRilIdentifier());
							entity = entity.withStatement(new StatementImpl(
								null,
								StatementRank.NORMAL,
								new ValueSnakImpl(STATION_CODE, new StringValueImpl(id.getRilIdentifier())),
								Collections.emptyList(),
								REFERENCE,
								entity.getEntityId()
							));
						}
					}
				}
				
				
				
				//----commit changes
				if(message.length() > 0) {
					editor.editItemDocument(entity, false, message.toString());
				}
			}
			
			
		};


	}

	private void message(StringBuilder messages, String msg) {
		if(messages.length() != 0)
			messages.append("\n");
		messages.append(msg);
		System.out.println("\t\t"+msg);
	}

	private ItemDocument findEntity(WikibaseDataFetcher wbdf, Station station) throws Exception {
		List<ItemDocument> res = findEntity(wbdf, station.getName());
		if(res.size()==1)
			return res.get(0);
		if(res.size() > 1)
			return null;
		
		res = findEntity(wbdf, station.getName()+" station");
		if(res.size()==1)
			return res.get(0);
		if(res.size() > 1)
			return null;
		
		res = findEntity(wbdf, "Bahnhof "+station.getName());
		if(res.size()==1)
			return res.get(0);
		if(res.size() > 1)
			return null;
		
		System.out.println("\tno match found");
		return null;
	}
	
	private List<ItemDocument> findEntity(WikibaseDataFetcher wbdf, String name) throws Exception {
		wbdf.getFilter().setPropertyFilter(
			Collections.singleton(
				INSTANCE_OF
			)
		);
		WbGetEntitiesSearchData search = new WbGetEntitiesSearchData();
		{
			search.search = name;
			search.language = "de";
			search.type = "item";
			search.limit = Long.valueOf(5);
		}
		return wbdf
			.searchEntities(search)
			.stream()
			.map(res -> {
				try {
					return wbdf.getEntityDocument(res.getEntityId());
				} catch (MediaWikiApiErrorException e) {
					throw new RuntimeException(e);
				}
			})
			.map(ItemDocument.class::cast)
			.filter(res -> res.hasStatement(INSTANCE_OF))
			.filter(res -> res.findStatementGroup(INSTANCE_OF).getStatements().stream().anyMatch(st -> 
				st.getValue() instanceof EntityIdValue
				&& RAILWAYS_STATION_TYPES.contains(((EntityIdValue)st.getValue()).getId())
			))
			.collect(Collectors.toList());
	}

}
