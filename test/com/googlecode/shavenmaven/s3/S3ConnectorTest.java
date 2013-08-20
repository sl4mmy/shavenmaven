package com.googlecode.shavenmaven.s3;

import com.googlecode.totallylazy.time.StoppedClock;
import com.googlecode.utterlyidle.Request;
import org.junit.Test;

import java.net.HttpURLConnection;
import java.util.Date;

import static com.googlecode.shavenmaven.Artifacts.artifact;
import static com.googlecode.shavenmaven.s3.AwsCredentials.awsCredentials;
import static com.googlecode.totallylazy.Sequences.sequence;
import static com.googlecode.totallylazy.time.Dates.date;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class S3ConnectorTest {

    @Test
    public void createsAwsAuthorisedHttpConnection() throws Exception {
        Date now = date(2001, 1, 1);
        S3Connector connector = new S3Connector(awsCredentials("*", "access-key", "secret-key"), new StoppedClock(now));
        Request request = connector.call(sequence(artifact("s3://repo.bodar.com/com.googlecode.yadic:yadic:jar:116")).head());
        assertThat(request.uri().toString(), is("http://s3.amazonaws.com/repo.bodar.com/com/googlecode/yadic/yadic/116/yadic-116.jar"));
    }
}