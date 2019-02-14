package eu.albertomorales.calciteIntro;

import org.junit.Assert;
import org.junit.Test;

import eu.albertomorales.calciteIntro.DatasetComparator;

public class DatasetComparatorTest {

	@Test 
	public void test6() throws Exception {	
		DatasetComparator comparator = new DatasetComparator("calciteIntro");
		boolean resultado = 
				comparator.compare("STOCKALMACEN ",
						   		   "STOCKALMACENDIFERENTE ",
						   		   "CODIGO_DE_BARRAS");
		Assert.assertFalse(resultado);
	}
	
	@Test 
	public void test5() throws Exception {	
		DatasetComparator comparator = new DatasetComparator("calciteIntro");
		boolean resultado = 
				comparator.compare("STOCKALMACEN ",
						   		   "STOCKALMACENDIFERENTE ",
						   		   "CODIGO_DE_BARRAS",
						   		   new String[] {"CATEGORIA", "SUBCATEGORIA"});
		Assert.assertTrue(resultado);
	}
	
	@Test 
	public void test4() throws Exception {	
		DatasetComparator comparator = new DatasetComparator("calciteIntro");
		boolean resultado = 
				comparator.compare("STOCKALMACEN ",
						   		   "STOCKALMACENCOLUMNAS ",
						   		   "CODIGO_DE_BARRAS",
						   		   new String[] {"CATEGORIA", "SUBCATEGORIA"});
		Assert.assertTrue(resultado);
	}
	
	@Test 
	public void test3() throws Exception {	
		DatasetComparator comparator = new DatasetComparator("calciteIntro");
		boolean resultado = 
				comparator.compare("STOCKALMACEN ",
						   		   "STOCKALMACENCOLUMNAS ",
						   		   "CODIGO_DE_BARRAS");
		Assert.assertFalse(resultado);
	}
	
	@Test 
	public void test2() throws Exception {	
		DatasetComparator comparator = new DatasetComparator("calciteIntro");
		boolean resultado = 
				comparator.compare("STOCKALMACEN ",
						   		   "STOCKALMACENDESORDENADO ",
						   		   "CODIGO_DE_BARRAS");
		Assert.assertTrue(resultado);
	}
	
	@Test 
	public void test1() throws Exception {	
		DatasetComparator comparator = new DatasetComparator("calciteIntro");
		boolean resultado = 
				comparator.compare("STOCKALMACEN",
						   		   "STOCKALMACENDESORDENADO");
		Assert.assertFalse(resultado);
	}
	
	@Test 
	public void test0() throws Exception {	
		DatasetComparator comparator = new DatasetComparator("calciteIntro");
		boolean resultado = 
				comparator.compare("STOCKALMACEN",
						   		   "STOCKALMACEN");
		Assert.assertTrue(resultado);
	}
		
}
