package io.neocore.bukkit._test;
import org.junit.Assert;
import org.junit.Test;

import io.neocore.bukkit.permissions.PermHelper;

public class PermHelperTest {
	
	@Test
	public void testPerms() {
		
		Assert.assertTrue(PermHelper.matches("*", "foo.bar"));
		Assert.assertTrue(PermHelper.matches("foo.bar.*", "foo.bar.baz"));
		Assert.assertTrue(PermHelper.matches("bar.baz.quux", "bar.baz.quux"));
		
		Assert.assertFalse(PermHelper.matches("a.b.c", "a.b.d"));
		Assert.assertFalse(PermHelper.matches("a.b.*", "a.c.d"));
		Assert.assertFalse(PermHelper.matches("asdf.qwerty.*", "haha"));
		
	}
	
}
