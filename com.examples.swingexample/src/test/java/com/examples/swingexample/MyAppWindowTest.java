package com.examples.swingexample;

import org.assertj.swing.core.matcher.JButtonMatcher;
import org.assertj.swing.core.matcher.JLabelMatcher;
import org.assertj.swing.edt.GuiActionRunner;
import org.assertj.swing.fixture.FrameFixture;
import org.assertj.swing.fixture.JTextComponentFixture;
import org.assertj.swing.junit.testcase.AssertJSwingJUnitTestCase;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.assertj.core.api.Assertions.*;

import static org.mockito.Mockito.*;

import java.util.Arrays;

public class MyAppWindowTest extends AssertJSwingJUnitTestCase {

	private FrameFixture window;

	@Mock
	private StudentRepository studentRepository;

	private MyAppWindow myAppWindow;

	@Override
	public void onSetUp() {
		MockitoAnnotations.initMocks(this);
		MyAppWindow frame = GuiActionRunner.execute(() -> {
			myAppWindow = new MyAppWindow(studentRepository);
			return myAppWindow;
		});
		window = new FrameFixture(robot(), frame);
		window.show(); // shows the frame to test
	}

	@Test
	public void testControlsInitialStates() {
		window.label(JLabelMatcher.withText("id"));
		window.textBox("idTextBox").requireEnabled();
		window.label(JLabelMatcher.withText("name"));
		window.textBox("nameTextBox").requireEnabled();
		window.button(JButtonMatcher.withText("Add")).requireDisabled();
		window.list("studentList");
	}

	@Test
	public void testWhenEitherIdOrNameAreEmptyThenAddButtonShouldBeDisabled() {
		JTextComponentFixture idTextBox = window.textBox("idTextBox");
		JTextComponentFixture nameTextBox = window.textBox("nameTextBox");

		idTextBox.enterText("1");
		nameTextBox.enterText(" ");
		window.button(JButtonMatcher.withText("Add")).requireDisabled();

		idTextBox.setText("");
		nameTextBox.setText("");

		idTextBox.enterText(" ");
		nameTextBox.enterText("test");
		window.button(JButtonMatcher.withText("Add")).requireDisabled();
	}

	@Test
	public void testWhenIdAndNameAreNonEmptyThenAddButtonShouldBeEnabled() {
		window.textBox("idTextBox").enterText("1");
		window.textBox("nameTextBox").enterText("test");
		window.button(JButtonMatcher.withText("Add")).requireEnabled();
	}

	@Test
	public void shouldCallStudentRepository() {
		window.textBox("idTextBox").enterText("1");
		window.textBox("nameTextBox").enterText("test");
		window.button(JButtonMatcher.withText("Add")).click();
		verify(studentRepository).save(new Student("1", "test"));
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
