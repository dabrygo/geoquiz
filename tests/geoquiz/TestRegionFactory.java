package geoquiz;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

public class TestRegionFactory {

	private AbstractRegionFactory regionFactory;

	@Before
	public void setUp() {
		regionFactory = new LightweightRegionFactory();
	}
	
	@Test
	public void test_number_of_countries_in_asia() {
		assertEquals(58, regionFactory.regionFrom(Continent.ASIA).size());
	}
	
	@Test
	public void test_number_of_countries_in_africa() {
		assertEquals(58, regionFactory.regionFrom(Continent.AFRICA).size());
	}

	@Test
	public void test_number_of_countries_in_north_america() {
		assertEquals(44, regionFactory.regionFrom(Continent.NORTH_AMERICA).size());
	}
	
	@Test
	public void test_number_of_countries_in_south_america() {
		assertEquals(14, regionFactory.regionFrom(Continent.SOUTH_AMERICA).size());
	}
	
	@Test
	public void test_number_of_countries_in_europe() {
		assertEquals(52, regionFactory.regionFrom(Continent.EUROPE).size());
	}
	
	@Test
	public void test_number_of_countries_in_australia() {
		assertEquals(34, regionFactory.regionFrom(Continent.AUSTRALIA).size());
	}
}
