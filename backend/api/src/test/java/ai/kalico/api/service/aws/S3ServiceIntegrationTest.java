//package com.kalico.api.service.aws;
//
//
//import com.amazonaws.services.s3.model.PutObjectResult;
//import com.fasterxml.jackson.core.JsonProcessingException;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.kalico.api.props.AWSProps;
//import com.kalico.api.service.ServiceTestConfiguration;
//import com.kalico.model.MRUserPrincipal;
//import lombok.SneakyThrows;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
//import org.testng.annotations.*;
//
//import java.lang.reflect.Method;
//import java.util.UUID;
//
//import static org.hamcrest.MatcherAssert.assertThat;
//import static org.hamcrest.Matchers.*;
//
///**
// * @author Bizuwork Melesse
// * created on 2/13/22
// */
//@Slf4j
//@SpringBootTest(classes = ServiceTestConfiguration.class)
//public class S3ServiceIntegrationTest extends AbstractTestNGSpringContextTests {
//
//    @Autowired
//    private S3Service s3Service;
//
//    @Autowired
//    private AWSProps awsProps;
//
//    @Autowired
//    private ObjectMapper objectMapper;
//
//    private MRUserPrincipal expectedUser;
//
//    private String bucketKey;
//
//    @BeforeClass
//    public void setup() {
//        bucketKey = awsProps.getCvPrefix() + "/sherlock";
//        expectedUser = new MRUserPrincipal();
//        expectedUser.setFirebaseId(UUID.randomUUID().toString());
//        expectedUser.setEmailVerified(false);
//        expectedUser.setName("Sherlock Holmes");
//        expectedUser.setPicture("http://lorempicsum.com/200");
//        expectedUser.setEmail("holmes@downingstreet.gov");
//    }
//
//    @AfterClass
//    public void tearDown() {
//        s3Service.deleteObject(awsProps.getBucket(), bucketKey);
//    }
//
//    @BeforeMethod
//    public void beforeEachTest(Method method) {
//        log.info("  Testcase: " + method.getName() + " has started");
//    }
//
//    @AfterMethod
//    public void afterEachTest(Method method) {
//        log.info("  Testcase: " + method.getName() + " has ended");
//    }
//
//    @SneakyThrows
//    @Test
//    public void putObjectTest() {
//        PutObjectResult result = s3Service.putObject(awsProps.getBucket(), bucketKey,
//                objectMapper.writeValueAsString(expectedUser));
//        assertThat(result, is(notNullValue()));
//        assertThat(result.getMetadata(), is(notNullValue()));
//    }
//
//    @Test(dependsOnMethods = "putObjectTest")
//    public void getObjectTest() throws JsonProcessingException {
//        String response = s3Service.getObject(awsProps.getBucket(), bucketKey);
//        assertThat(response, is(notNullValue()));
//      MRUserPrincipal actualUser = objectMapper.readValue(response, MRUserPrincipal.class);
//        assertThat(actualUser, is(notNullValue()));
//        assertThat(actualUser.getEmailVerified(), equalTo(expectedUser.getEmailVerified()));
//        assertThat(actualUser.getEmail(), startsWith(expectedUser.getEmail()));
//        assertThat(actualUser.getName(), startsWith(expectedUser.getName()));
//        assertThat(actualUser.getFirebaseId(), startsWith(expectedUser.getFirebaseId()));
//        assertThat(actualUser.getPicture(), startsWith(expectedUser.getPicture()));
//    }
//}

