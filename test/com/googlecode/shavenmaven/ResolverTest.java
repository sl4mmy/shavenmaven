package com.googlecode.shavenmaven;

import com.googlecode.shavenmaven.mvn.Handler;
import org.junit.Test;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

import static com.googlecode.shavenmaven.Resolver.url;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class ResolverTest {
    @Test
    public void supportsHttp() throws Exception{
        File directory = new TemporaryDirectory();
        Resolver resolver = new Resolver(directory);

        assertThat(directory.listFiles().length, is(0));
        resolver.resolve(url("http://yatspec.googlecode.com/files/yatspec-87.jar"));
        File[] files = directory.listFiles();
        assertThat(files.length, is(1));
        assertThat(files[0].getName(), is("yatspec-87.jar"));
    }

    @Test
    public void supportsSpecificMavenRepository() throws Exception{
        File directory = new TemporaryDirectory();
        Resolver resolver = new Resolver(directory);

        assertThat(directory.listFiles().length, is(0));
        resolver.resolve(url("mvn://repo.bodar.com/com.googlecode.yadic:yadic:jar:116"));
        File[] files = directory.listFiles();
        assertThat(files.length, is(1));
        assertThat(files[0].getName(), is("yadic-116.jar"));
    }

    @Test
    public void supportsUnspecifiedMavenRepository() throws Exception{
        File directory = new TemporaryDirectory();
        Resolver resolver = new Resolver(directory);

        assertThat(directory.listFiles().length, is(0));
        resolver.resolve(url("mvn:org.objenesis:objenesis:jar:1.2"));
        File[] files = directory.listFiles();
        assertThat(files.length, is(1));
        assertThat(files[0].getName(), is("objenesis-1.2.jar"));
    }
}
