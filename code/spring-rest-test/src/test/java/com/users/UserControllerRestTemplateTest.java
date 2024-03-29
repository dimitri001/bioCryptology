package com.users;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.json.JSONException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.users.domain.User;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT) // for restTemplate
@ActiveProfiles("test")
public class UserControllerRestTemplateTest {

    private static final ObjectMapper om = new ObjectMapper();

    @Autowired
    private TestRestTemplate restTemplate;

    @MockBean
    private UserRepository mockRepository;

    @Before
    public void init() {
        User user = new User("Nom5", "five@email.com",25, "m", true);
        user.setId(1L);
        when(mockRepository.findById(1L)).thenReturn(Optional.of(user));
    }

    @Test
    public void find_userId_OK() throws JSONException {

        String expected = "{id:1,name:\"Nom5\",email:\"five@email.com\",age:25,sex:\"m\",activated:true}";

        ResponseEntity<String> response = restTemplate.getForEntity("/users/1", String.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(MediaType.APPLICATION_JSON_UTF8, response.getHeaders().getContentType());

        JSONAssert.assertEquals(expected, response.getBody(), false);

        verify(mockRepository, times(1)).findById(1L);

    }

    @Test
    public void find_allUser_OK() throws Exception {

    	User us1 = new User("Nom1", "one@email.com",21, "m", true);
    	us1.setId(1L);
    	
    	User us2 = new User("Nom2", "two@email.com",22, "w", true);
    	us2.setId(2L);
    	
        List<User> users = Arrays.asList(us1, us2);
        
        when(mockRepository.findAll()).thenReturn(users);

        String expected = om.writeValueAsString(users);

        ResponseEntity<String> response = restTemplate.getForEntity("/users", String.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        JSONAssert.assertEquals(expected, response.getBody(), false);

        verify(mockRepository, times(1)).findAll();
    }


    @Test
    public void save_user_OK() throws Exception {

        User newUser = new User("Nom3", "one@email.com",23, "w", true);
        when(mockRepository.save(any(User.class))).thenReturn(newUser);

        String expected = om.writeValueAsString(newUser);

        ResponseEntity<String> response = restTemplate.postForEntity("/users", newUser, String.class);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        JSONAssert.assertEquals(expected, response.getBody(), false);

        verify(mockRepository, times(1)).save(any(User.class));

    }

    @Test
    public void update_user_OK() throws Exception {

        User updateUser = new User("Nom3", "one@email.com",23, "w", true);
        updateUser.setId(1L);
        when(mockRepository.save(any(User.class))).thenReturn(updateUser);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity<>(om.writeValueAsString(updateUser), headers);

        ResponseEntity<String> response = restTemplate.exchange("/users/1", HttpMethod.PUT, entity, String.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        JSONAssert.assertEquals(om.writeValueAsString(updateUser), response.getBody(), false);

        verify(mockRepository, times(1)).findById(1L);
        verify(mockRepository, times(1)).save(any(User.class));

    }

   
    @Test
    public void delete_user_OK() {

        doNothing().when(mockRepository).deleteById(1L);

        HttpEntity<String> entity = new HttpEntity<>(null, new HttpHeaders());
        ResponseEntity<String> response = restTemplate.exchange("/users/1", HttpMethod.DELETE, entity, String.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());

        verify(mockRepository, times(1)).deleteById(1L);
    }

    private static void printJSON(Object object) {
        String result;
        try {
            result = om.writerWithDefaultPrettyPrinter().writeValueAsString(object);
            System.out.println(result);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

}
