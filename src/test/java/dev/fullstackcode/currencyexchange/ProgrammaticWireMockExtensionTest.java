package dev.fullstackcode.currencyexchange;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import dev.fullstackcode.currencyexchange.junit.extension.TimingExtension;
import dev.fullstackcode.currencyexchange.junit.extension.WiremockServerExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import static com.github.tomakehurst.wiremock.client.WireMock.urlMatching;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(TimingExtension.class)
@ExtendWith(WiremockServerExtension.class)
public class ProgrammaticWireMockExtensionTest {

	@Autowired
	private MockMvc mockMvc;


	@Test
	void contextLoads() {
	}


	@Test
	@ExtendWith(TimingExtension.class)
	public void testCurrencyConversionByCountry(WireMockServer currencyServer,WireMockServer countryServer) throws Exception {

		String url = String.format("%s/v3.1/name/%s", currencyServer.baseUrl(),"%s");
	    System.out.println("url :" + url);

		String currencyCode = "jpy";
		String currencyConversionUrl = String.format("/gh/fawazahmed0/currency-api@1/latest/currencies/usd/%s.json",currencyCode.toLowerCase());

		String country = "japan";
		String countryurl = String.format("/v3.1/name/%s",country.toLowerCase());

		String response = """
				[{"name":{"common":"Japan","official":"Japan","nativeName":{"jpn":{"official":"日本","common":"日本"}}},"tld":[".jp",".みんな"],"cca2":"JP","ccn3":"392","cca3":"JPN","cioc":"JPN","independent":true,"status":"officially-assigned","unMember":true,"currencies":{"JPY":{"name":"Japanese yen","symbol":"¥"}},"idd":{"root":"+8","suffixes":["1"]},"capital":["Tokyo"],"altSpellings":["JP","Nippon","Nihon"],"region":"Asia","subregion":"Eastern Asia","languages":{"jpn":"Japanese"},"translations":{"ara":{"official":"اليابان","common":"اليابان"},"bre":{"official":"Japan","common":"Japan"},"ces":{"official":"Japonsko","common":"Japonsko"},"cym":{"official":"Japan","common":"Japan"},"deu":{"official":"Japan","common":"Japan"},"est":{"official":"Jaapan","common":"Jaapan"},"fin":{"official":"Japani","common":"Japani"},"fra":{"official":"Japon","common":"Japon"},"hrv":{"official":"Japan","common":"Japan"},"hun":{"official":"Japán","common":"Japán"},"ita":{"official":"Giappone","common":"Giappone"},"jpn":{"official":"日本","common":"日本"},"kor":{"official":"일본국","common":"일본"},"nld":{"official":"Japan","common":"Japan"},"per":{"official":"ژاپن","common":"ژاپن"},"pol":{"official":"Japonia","common":"Japonia"},"por":{"official":"Japão","common":"Japão"},"rus":{"official":"Япония","common":"Япония"},"slk":{"official":"Japonsko","common":"Japonsko"},"spa":{"official":"Japón","common":"Japón"},"srp":{"official":"Јапан","common":"Јапан"},"swe":{"official":"Japan","common":"Japan"},"tur":{"official":"Japonya","common":"Japonya"},"urd":{"official":"جاپان","common":"جاپان"},"zho":{"official":"日本国","common":"日本"}},"latlng":[36.0,138.0],"landlocked":false,"area":377930.0,"demonyms":{"eng":{"f":"Japanese","m":"Japanese"},"fra":{"f":"Japonaise","m":"Japonais"}},"flag":"\\uD83C\\uDDEF\\uD83C\\uDDF5","maps":{"googleMaps":"https://goo.gl/maps/NGTLSCSrA8bMrvnX9","openStreetMaps":"https://www.openstreetmap.org/relation/382313"},"population":125836021,"gini":{"2013":32.9},"fifa":"JPN","car":{"signs":["J"],"side":"left"},"timezones":["UTC+09:00"],"continents":["Asia"],"flags":{"png":"https://flagcdn.com/w320/jp.png","svg":"https://flagcdn.com/jp.svg","alt":"The flag of Japan features a crimson-red circle at the center of a white field."},"coatOfArms":{"png":"https://mainfacts.com/media/images/coats_of_arms/jp.png","svg":"https://mainfacts.com/media/images/coats_of_arms/jp.svg"},"startOfWeek":"monday","capitalInfo":{"latlng":[35.68,139.75]},"postalCode":{"format":"###-####","regex":"^(\\\\d{7})$"}}]""";
		String response1 = """
				{
				    "date": "2023-05-19",
				    "jpy": 138.544529
				}""";


		currencyServer.stubFor(WireMock.get(urlMatching(currencyConversionUrl)).willReturn(WireMock.aResponse().withStatus(200).withBody(response1)));
		countryServer.stubFor(WireMock.get(urlMatching(countryurl)).willReturn(WireMock.aResponse().withStatus(200).withBody(response)));



		this.mockMvc.perform(get("/currencyCodeVersion/country/{countryCode}", country))
				.andExpect(status().isOk());
	}



	@DynamicPropertySource
	public static void properties(DynamicPropertyRegistry registry) {
		registry.add("country.url",()-> String.format("%s/v3.1/name/%s",
				WiremockServerExtension.countryWireMock().baseUrl(),"%s"));
		registry.add("currencyconverter.url",()->String.format("%s/gh/fawazahmed0/currency-api@1" +
				"/latest/currencies/usd/%s.json", WiremockServerExtension.currencyWireMock().baseUrl(),"%s"));
	}
}
