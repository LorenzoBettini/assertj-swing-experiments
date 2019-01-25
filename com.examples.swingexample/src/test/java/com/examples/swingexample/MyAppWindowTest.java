package com.examples.swingexample;

import org.assertj.swing.edt.FailOnThreadViolationRepaintManager;
import org.assertj.swing.edt.GuiActionRunner;
import org.assertj.swing.fixture.FrameFixture;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.*;

public class MyAppWindowTest {

	private FrameFixture window;

	@Mock
	private StudentRepository studentRepository;

	@BeforeClass
	public static void setUpOnce() {
		FailOnThreadViolationRepaintManager.install();
	}

	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);
		MyAppWindow frame = GuiActionRunner.execute(() -> new MyAppWindow(studentRepository));
		window = new FrameFixture(frame);
		window.show(); // shows the frame to test
	}

	@Test
	public void shouldCallStudentRepository() {
		window.button("clickMe").click();
		verify(studentRepository).save(new Student());
	}
}
