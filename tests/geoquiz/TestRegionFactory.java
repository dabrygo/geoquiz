package geoquiz;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class TestRegionFactory {

	@Test
	public void test_number_of_countries_in_asia() {
		assertEquals(58, RegionFactory.regionFrom(Continent.ASIA, true).size());
	}
	
	@Test
	public void test_number_of_countries_in_africa() {
		assertEquals(58, RegionFactory.regionFrom(Continent.AFRICA, true).size());
	}

	@Test
	public void test_number_of_countries_in_north_america() {
		assertEquals(44, RegionFactory.regionFrom(Continent.NORTH_AMERICA, true).size());
	}
	
	@Test
	public void test_number_of_countries_in_south_america() {
		assertEquals(14, RegionFactory.regionFrom(Continent.SOUTH_AMERICA, true).size());
	}
	
	@Test
	public void test_number_of_countries_in_europe() {
		assertEquals(52, RegionFactory.regionFrom(Continent.EUROPE, true).size());
	}
	
	@Test
	public void test_number_of_countries_in_australia() {
		assertEquals(34, RegionFactory.regionFrom(Continent.AUSTRALIA, true).size());
	}
}
