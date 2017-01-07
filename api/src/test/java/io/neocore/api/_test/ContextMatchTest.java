package io.neocore.api._test;

import org.junit.Assert;
import org.junit.Test;

import io.neocore.api.host.Context;

public class ContextMatchTest {

	@Test
	public void testContextCreation() {
		
		Context g = Context.GLOBAL;
		Context a = Context.create("a");
		
		Assert.assertEquals(a, Context.create("a"));
		Assert.assertNotEquals(a, Context.create("b"));
		Assert.assertEquals(g, Context.create(null));
		Assert.assertEquals(g, Context.create("GLOBAL")); // Don't create like this, usually.
		
	}
	
	@Test
	public void testContextMatches() {
		
		Context a = Context.create("a");
		Context b = Context.create("b");
		
		Assert.assertTrue(Context.checkCompatility(Context.GLOBAL, a));
		Assert.assertTrue(Context.checkCompatility(Context.GLOBAL, b));
		Assert.assertTrue(Context.checkCompatility(a, a));
		Assert.assertTrue(Context.checkCompatility(Context.GLOBAL, Context.GLOBAL));
		Assert.assertFalse(Context.checkCompatility(a, b));
		Assert.assertFalse(Context.checkCompatility(b, a));
		
	}
	
}
