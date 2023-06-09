package dev.fullstackcode.currencyexchange;

import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.junit5.WireMockRuntimeInfo;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@WireMockTest  (httpPort = 8889)
public class DeclarativeWireMockTest  {

	@Autowired
	private MockMvc mockMvc;


	@Test
	void contextLoads() {
	}

	@Test
	public void testCurrencyConversionByCountry(WireMockRuntimeInfo wmRuntimeInfo) throws Exception {
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


		stubFor(WireMock.get(urlMatching(currencyConversionUrl)).willReturn(WireMock.aResponse().withStatus(200).withBody(response1)));
		stubFor(WireMock.get(urlMatching(countryurl)).willReturn(WireMock.aResponse().withStatus(200).withBody(response)));



		this.mockMvc.perform(get("/currencyCodeVersion/country/{countryCode}", country))
				.andExpect(status().isOk());
	}

	@Test
	public void testCurrencyConversionByCountryWhenNoCountryFoundWithGivenCode(WireMockRuntimeInfo wmRuntimeInfo) throws Exception {
		String currencyCode = "abc";
		String currencyConversionUrl = String.format("/gh/fawazahmed0/currency-api@1/latest/currencies/usd/%s.json",currencyCode.toLowerCase());

		String country = "japan";
		String countryurl = String.format("/v3.1/name/%s",country.toLowerCase());

		String countryResponse = """
				{"status":404,"message":"Not Found"}""";
		String CurrencyConversionResponse = """
				{
				    "date": "2023-05-19",
				    "jpy": 138.544529
				}""";


		stubFor(WireMock.get(urlMatching(currencyConversionUrl)).willReturn(WireMock.aResponse().withStatus(200).withBody(CurrencyConversionResponse)));
		stubFor(WireMock.get(urlMatching(countryurl)).willReturn(WireMock.aResponse().withStatus(200).withBody(countryResponse)));


		this.mockMvc.perform(get("/currencyCodeVersion/country/{countryCode}", country))
				.andDo(res -> System.out.println(res.getResponse()))
				.andExpect(status().isNotFound())
		;


	}


	@Test
	public void testConvertByCurrencyCode(WireMockRuntimeInfo wmRuntimeInfo) throws Exception {
		String currencyCode = "jpy";

		String response = """
				{
				    "date": "2023-05-19",
				    "jpy": 138.544529
				}""";

		String currencyConversionUrl = String.format("/gh/fawazahmed0/currency-api@1/latest/currencies/usd/%s.json",currencyCode.toLowerCase());

		//stubFor(WireMock.get(urlEqualTo(currencyConversionUrl)).willReturn(WireMock.aResponse().withStatus(200).withJsonBody(response)));
		stubFor(WireMock.get(urlEqualTo(currencyConversionUrl)).willReturn(WireMock.aResponse().withStatus(200).withBody(response)));

		this.mockMvc.perform(get("/currencyCodeVersion/currencyCode/{currencyCode}", currencyCode))
				.andExpect(status().isOk()).andExpect(content().string("{date=2023-05-19, jpy=138.544529}"));
	}

	@Test
	public void testConvertByCurrencyCode2(WireMockRuntimeInfo wmRuntimeInfo) throws Exception {
		String currencyCode = "jpy";

		String response = """
				{
				    "date": "2023-05-19",
				    "jpy": 138.544529
				}""";

		String currencyConversionUrl = String.format("/gh/fawazahmed0/currency-api@1/latest/currencies/usd/%s.json",currencyCode.toLowerCase());

		stubFor(WireMock.get(urlEqualTo(currencyConversionUrl)).willReturn(WireMock.aResponse().withStatus(200).withBody(response)));

		this.mockMvc.perform(get("/currencyCodeVersion/currencyCode/{currencyCode}", currencyCode))
				.andExpect(status().isOk());
	}


	@Test
	public void testConvertByCurrencyCodeWhenConversionFoundWithGivenCurrencyCode(WireMockRuntimeInfo wmRuntimeInfo) throws Exception {

		String currencyCode = "abc";
		String currencyConversionUrl = String.format("/gh/fawazahmed0/currency-api@1/latest/currencies/usd/%s.json", currencyCode.toLowerCase());

		stubFor(WireMock.get(urlEqualTo(currencyConversionUrl)).willReturn(WireMock.aResponse().withStatus(403).withBody("Package size exceeded the configured limit of 50 MB")));

		this.mockMvc.perform(get("/currencyCodeVersion/currencyCode/{currencyCode}", currencyCode))
				.andExpect(status().isForbidden());


	}


}
