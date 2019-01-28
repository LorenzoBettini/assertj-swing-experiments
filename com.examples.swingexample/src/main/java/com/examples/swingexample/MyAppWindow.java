package com.examples.swingexample;

import java.awt.EventQueue;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

public class MyAppWindow extends JFrame {

	static final class StudentViewModel {
		private Student student;

		public StudentViewModel(Student student) {
			this.student = student;
		}

		@Override
		public String toString() {
			return student.getId() + " - " + student.getName();
		}
	}

	static final class StudentListModel extends DefaultListModel<StudentViewModel> {
		private static final long serialVersionUID = 1L;

		public void addStudent(Student student) {
			addElement(new StudentViewModel(student));
		}
	}

	private static final long serialVersionUID = 1L;

	private StudentRepository studentRepository;
	private JTextField idTextField;
	private JTextField nameTextField;
	private JButton btnAdd;
	private JList<StudentViewModel> list;
	private JScrollPane scrollPane;

	private StudentListModel listModel = new StudentListModel();

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
	public MyAppWindow(StudentRepository repository) {
		this.studentRepository = repository;
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{0, 267};
		gridBagLayout.rowHeights = new int[]{0, 0, 0, 0, 0};
		gridBagLayout.columnWeights = new double[]{0.0, 1.0};
		gridBagLayout.rowWeights = new double[]{0.0, 0.0, 0.0, 1.0, Double.MIN_VALUE};
		getContentPane().setLayout(gridBagLayout);
		
		JLabel lblId = new JLabel("id");
		GridBagConstraints gbc_lblId = new GridBagConstraints();
		gbc_lblId.insets = new Insets(0, 0, 5, 5);
		gbc_lblId.anchor = GridBagConstraints.EAST;
		gbc_lblId.gridx = 0;
		gbc_lblId.gridy = 0;
		getContentPane().add(lblId, gbc_lblId);
		
		idTextField = new JTextField();
		GridBagConstraints gbc_textField = new GridBagConstraints();
		gbc_textField.insets = new Insets(0, 0, 5, 0);
		gbc_textField.fill = GridBagConstraints.HORIZONTAL;
		gbc_textField.gridx = 1;
		gbc_textField.gridy = 0;
		getContentPane().add(idTextField, gbc_textField);
		idTextField.setColumns(10);
		
		JLabel lblName = new JLabel("name");
		GridBagConstraints gbc_lblName = new GridBagConstraints();
		gbc_lblName.insets = new Insets(0, 0, 5, 5);
		gbc_lblName.gridx = 0;
		gbc_lblName.gridy = 1;
		getContentPane().add(lblName, gbc_lblName);
		
		nameTextField = new JTextField();
		GridBagConstraints gbc_textField_1 = new GridBagConstraints();
		gbc_textField_1.insets = new Insets(0, 0, 5, 0);
		gbc_textField_1.fill = GridBagConstraints.HORIZONTAL;
		gbc_textField_1.gridx = 1;
		gbc_textField_1.gridy = 1;
		getContentPane().add(nameTextField, gbc_textField_1);
		nameTextField.setColumns(10);
		
		btnAdd = new JButton("Add");
		btnAdd.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				studentRepository.save(new Student());
				listModel.addStudent(new Student("1", "test"));
			}
		});
		GridBagConstraints gbc_btnAdd = new GridBagConstraints();
		gbc_btnAdd.gridwidth = 2;
		gbc_btnAdd.insets = new Insets(0, 0, 5, 0);
		gbc_btnAdd.gridx = 0;
		gbc_btnAdd.gridy = 2;
		getContentPane().add(btnAdd, gbc_btnAdd);
		
		list = new JList<>(listModel);
		scrollPane = new JScrollPane(list);
		GridBagConstraints gbc_scrollPane = new GridBagConstraints();
		gbc_scrollPane.fill = GridBagConstraints.BOTH;
		gbc_scrollPane.gridwidth = 2;
		gbc_scrollPane.gridx = 0;
		gbc_scrollPane.gridy = 3;
		getContentPane().add(scrollPane, gbc_scrollPane);
		
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		setTitle(getClass().getSimpleName());
		setBounds(100, 100, 450, 300);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

	}

	public void showAllStudents(List<Student> asList) {
		asList.stream()
			.forEach(listModel::addStudent);
	}
}
