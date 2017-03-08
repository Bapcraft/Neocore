package io.neocore.test;

import org.junit.Assert;
import org.junit.Test;

import io.neocore.api.player.permission.PermHelper;

public class PermHelperTest {

	@Test
	public void testPerms() {

		Assert.assertEquals(0, PermHelper.matchDepth("*", "foo.bar"));
		Assert.assertEquals(1, PermHelper.matchDepth("a.*", "a.b.c.d.e"));
		Assert.assertEquals(2, PermHelper.matchDepth("foo.bar.*", "foo.bar.baz"));
		Assert.assertEquals(3, PermHelper.matchDepth("bar.baz.quux", "bar.baz.quux"));
		Assert.assertEquals(5, PermHelper.matchDepth("1.2.3.4.5.*", "1.2.3.4.5.6.7"));

		Assert.assertFalse(PermHelper.matches("a.b.c", "a.b.d"));
		Assert.assertFalse(PermHelper.matches("a.b.*", "a.c.d"));
		Assert.assertFalse(PermHelper.matches("asdf.qwerty.*", "haha"));

	}

}
