package com.examples.swingexample;

import org.assertj.swing.edt.FailOnThreadViolationRepaintManager;
import org.assertj.swing.edt.GuiActionRunner;
import org.assertj.swing.fixture.FrameFixture;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.assertj.swing.core.matcher.JButtonMatcher.*;
import static org.assertj.core.api.Assertions.*;

import static org.mockito.Mockito.*;

import java.util.Arrays;

public class MyAppWindowTest {

	private FrameFixture window;

	@Mock
	private StudentRepository studentRepository;

	private MyAppWindow myAppWindow;

	@BeforeClass
	public static void setUpOnce() {
		FailOnThreadViolationRepaintManager.install();
	}

	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);
		MyAppWindow frame = GuiActionRunner.execute(() -> {
			myAppWindow = new MyAppWindow(studentRepository);
			return myAppWindow;
		});
		window = new FrameFixture(frame);
		window.show(); // shows the frame to test
	}

	@After
	public void tearDown() {
		window.cleanUp();
	}

	@Test
	public void shouldCallStudentRepository() {
		window.button(withText("Add")).click();
		verify(studentRepository).save(new Student());
	}

	@Test
	public void shouldAddStudentToList() {
		GuiActionRunner.execute(
			() ->
			myAppWindow.showAllStudents(
				Arrays.asList(
					new Student("1", "test1"),
					new Student("2", "test2")))
		);
		String[] listContents = window.list().contents();
		assertThat(listContents)
			.containsExactly("1 - test1", "2 - test2");
	}
}
