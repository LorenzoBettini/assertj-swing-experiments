package com.examples.swingexample;

import java.awt.EventQueue;
import java.util.List;

import javax.swing.JFrame;
import java.awt.GridLayout;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JTable;

public class MyAppWindow extends JFrame {
	private static final long serialVersionUID = 1L;

	private StudentRepository studentRepository;
	private JTextField idTextField;
	private JTextField nameTextField;
	private JTable studentTable;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MyAppWindow window = new MyAppWindow(new StudentRepository() {
						
						@Override
						public void save(Student student) {
							
						}
						
						@Override
						public Student findById(String id) {
							return null;
						}
						
						@Override
						public List<Student> findAll() {
							return null;
						}
						
						@Override
						public void delete(String id) {
							
						}
					});
					window.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public MyAppWindow(StudentRepository studentRepository) {
		this.studentRepository = studentRepository;
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		setTitle(getClass().getSimpleName());
		setBounds(100, 100, 450, 300);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		getContentPane().setLayout(new GridLayout(4, 0, 0, 0));
		
		JButton btnClickMe = new JButton("Click me");
		btnClickMe.setName("clickMe");
		btnClickMe.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				studentRepository.save(new Student());
			}
		});
		
		JLabel lblId = new JLabel("id");
		getContentPane().add(lblId);
		
		idTextField = new JTextField();
		getContentPane().add(idTextField);
		idTextField.setColumns(10);
		
		JLabel lblName = new JLabel("name");
		getContentPane().add(lblName);
		
		nameTextField = new JTextField();
		getContentPane().add(nameTextField);
		nameTextField.setColumns(10);
		getContentPane().add(btnClickMe);
		
		studentTable = new JTable();
		getContentPane().add(studentTable);
	}
}
