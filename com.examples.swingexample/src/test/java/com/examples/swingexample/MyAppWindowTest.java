package com.examples.swingexample;

import org.assertj.swing.core.matcher.JButtonMatcher;
import org.assertj.swing.core.matcher.JLabelMatcher;
import org.assertj.swing.edt.GuiActionRunner;
import org.assertj.swing.fixture.FrameFixture;
import org.assertj.swing.fixture.JButtonFixture;
import org.assertj.swing.fixture.JTextComponentFixture;
import org.assertj.swing.junit.testcase.AssertJSwingJUnitTestCase;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.examples.swingexample.MyAppWindow.StudentListModel;

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
		window.button(JButtonMatcher.withText("Delete Selected")).requireDisabled();
		window.label("errorMessageLabel").requireText(" ");
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
	public void testAddButtonShouldAddTheEnteredTheStudentInformationThroughTheRepository() {
		window.textBox("idTextBox").enterText("1");
		window.textBox("nameTextBox").enterText("test");
		window.button(JButtonMatcher.withText("Add")).click();
		verify(studentRepository).save(new Student("1", "test"));
	}

	@Test
	public void testsShowAllStudentsShouldAddStudentsToTheList() {
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

	@Test
	public void testDeleteButtonShouldBeEnabledOnlyWhenAStudentIsSelected() {
		JButtonFixture deleteButton = window.button(JButtonMatcher.withText("Delete Selected"));
		GuiActionRunner.execute(() -> myAppWindow.getListModel().addStudent(new Student("1", "test")));
		deleteButton.requireDisabled();
		window.list("studentList").selectItem(0);
		deleteButton.requireEnabled();
		window.list("studentList").clearSelection();
		deleteButton.requireDisabled();
	}

	@Test
	public void testsDeleteButtonShouldDeleteTheSelectedStudentThroughTheRepository() {
		GuiActionRunner.execute(
				() -> {
					StudentListModel listModel = myAppWindow.getListModel();
					listModel.addStudent(new Student("1", "test1"));
					listModel.addStudent(new Student("2", "test2"));
				});
		window.list("studentList").selectItem(1);
		window.button(JButtonMatcher.withText("Delete Selected")).click();
		verify(studentRepository).delete("2");
	}

	@Test
	public void testDeletedStudentShouldRemoveTheStudentFromTheList() {
		// arrange
		GuiActionRunner.execute(
			() ->
			myAppWindow.showAllStudents(
				Arrays.asList(
					new Student("1", "test1"),
					new Student("2", "test2")))
		);
		// act
		GuiActionRunner.execute(
			() ->
			myAppWindow.studentRemoved(new Student("1", "test1"))
		);
		// assert
		String[] listContents = window.list().contents();
		assertThat(listContents)
			.containsExactly("2 - test2");
	}

	@Test
	public void testStudentAddedShouldAddTheStudentToTheList() {
		GuiActionRunner.execute(
			() ->
			myAppWindow.studentAdded(new Student("1", "test1"))
		);
		String[] listContents = window.list().contents();
		assertThat(listContents)
			.containsExactly("1 - test1");
	}
}
