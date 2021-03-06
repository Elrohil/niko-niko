package com.pyxis.nikoniko.controller;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.collection.IsIn.isIn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.Test;

import com.google.common.collect.Lists;
import com.pyxis.nikoniko.domain.Maybe;
import com.pyxis.nikoniko.domain.User;
import com.pyxis.nikoniko.domain.UserRepository;
import com.pyxis.nikoniko.domain.calendar.CalendarDate;
import com.pyxis.nikoniko.domain.calendar.CalendarRepository;
import com.pyxis.nikoniko.domain.calendar.MoodType;
import com.pyxis.nikoniko.view.CalendarViewService;

public class CalendarControllerTest {
    UserRepository userRepository = mock(UserRepository.class);
    CalendarRepository calendarRepository = mock(CalendarRepository.class);
    CalendarViewService calendarView = mock(CalendarViewService.class);
    CalendarController controller = new CalendarController(userRepository, calendarRepository, calendarView);

    @Test
    public void shouldReturnEmptyListIfNoUsersAreFound() {
	when(userRepository.list()).thenReturn(Lists.<User> newArrayList());
	List<String> userList = controller.users();
	assertThat("user should be empty", userList.isEmpty());
    }

    @Test
    public void shouldReturnListOfUsersInRepository() {
	when(userRepository.list()).thenReturn(Lists.newArrayList(new User("bob")));
	List<String> userList = controller.users();
	assertThat("should contain", "bob", isIn(userList));
    }

    @Test
    public void shouldReturnManyUsersFromRepository() {
	when(userRepository.list()).thenReturn(Lists.newArrayList(new User("bob"), new User("alice")));
	List<String> userList = controller.users();
	assertThat("should contain", "alice", isIn(userList));
	assertThat("should contain", "bob", isIn(userList));
    }

    @Test
    public void shouldBeAbleToAddUserToRepository() {
	controller.addUser("bob");
	verify(userRepository).add(new User("bob"));
    }
    
    @Test
    public void shouldBeAbleToSetMood() {
	User bob = new User("bob");
	when(userRepository.findByName("bob")).thenReturn(Maybe.some(bob));
	controller.setMood("bob", 0, MoodType.HAPPY.ordinal());
	
	verify(calendarRepository).setMood(bob, new CalendarDate(0), MoodType.HAPPY);
    }
}
