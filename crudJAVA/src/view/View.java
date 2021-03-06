package view;

import java.awt.EventQueue;
import java.sql.*;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.Font;

import javax.swing.JPanel;
import javax.swing.border.TitledBorder;

import net.proteanit.sql.DbUtils;

import javax.swing.border.EtchedBorder;
import java.awt.Color;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JTable;
import javax.swing.JScrollPane;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class View {

	private JFrame frame;
	private JTextField txtNome;
	private JTextField txtQtd;
	private JTextField txtPreco;
	private JTable table;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					View window = new View();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public View() {
		initialize();
		connectBD();
		table_load();
	}

	Connection con;
	PreparedStatement pst;
	ResultSet rs;
	private JTextField txtID;

	public void connectBD() {
		Statement stmt;

		try {
			// Conex?o BD
			Class.forName("org.postgresql.Driver");
			con = DriverManager.getConnection("jdbc:postgresql://localhost:5432/produto", "postgres", "123");

			if (con != null) {
				System.out.println("Conectado com Sucesso!");

				// Cria Tabela produto
				stmt = con.createStatement();
				String sql = "CREATE TABLE IF NOT EXISTS produto"
				+ "(id SERIAL PRIMARY KEY,"
				+ "nome varchar(40) UNIQUE NOT NULL," 
				+ "quantidade int," + "preco double precision)";
				stmt.execute(sql);
				System.out.println("Criado a Tabela necessaria no BD");
			}

			else {
				System.out.println("Conex?o Falhou!");
			}
		}

		catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void table_load() {
		try {

			pst = con.prepareStatement("select * from produto");
			rs = pst.executeQuery();
			table.setModel(DbUtils.resultSetToTableModel(rs));

		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setResizable(false);
		frame.setBounds(100, 100, 867, 600);
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.getContentPane().setLayout(null);

		JLabel lblNewLabel = new JLabel("Cadastro Produto");
		lblNewLabel.setFont(new Font("Tahoma", Font.BOLD, 30));
		lblNewLabel.setBounds(277, 31, 270, 62);
		frame.getContentPane().add(lblNewLabel);

		JPanel panel = new JPanel();
		panel.setBorder(new TitledBorder(
				new EtchedBorder(EtchedBorder.LOWERED, new Color(255, 255, 255), new Color(160, 160, 160)), "Cadastrar",
				TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
		panel.setBounds(10, 148, 402, 256);
		frame.getContentPane().add(panel);
		panel.setLayout(null);

		JLabel lblNewLabel_1 = new JLabel("Nome do Produto");
		lblNewLabel_1.setFont(new Font("Tahoma", Font.BOLD, 14));
		lblNewLabel_1.setBounds(10, 40, 137, 37);
		panel.add(lblNewLabel_1);

		JLabel lblNewLabel_1_1 = new JLabel("Quantidade");
		lblNewLabel_1_1.setFont(new Font("Tahoma", Font.BOLD, 14));
		lblNewLabel_1_1.setBounds(10, 118, 137, 37);
		panel.add(lblNewLabel_1_1);

		JLabel lblNewLabel_1_1_1 = new JLabel("Pre\u00E7o");
		lblNewLabel_1_1_1.setFont(new Font("Tahoma", Font.BOLD, 14));
		lblNewLabel_1_1_1.setBounds(10, 187, 137, 37);
		panel.add(lblNewLabel_1_1_1);

		txtNome = new JTextField();
		txtNome.setBounds(157, 50, 202, 20);
		panel.add(txtNome);
		txtNome.setColumns(10);

		txtQtd = new JTextField();
		txtQtd.setBounds(157, 128, 202, 20);
		panel.add(txtQtd);
		txtQtd.setColumns(10);

		txtPreco = new JTextField();
		txtPreco.setBounds(157, 197, 202, 20);
		panel.add(txtPreco);
		txtPreco.setColumns(10);

		JButton btnNewButton = new JButton("SALVAR");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				String nome;
				Integer quantidade;
				Double preco;

				nome = txtNome.getText();
				quantidade = Integer.parseInt(txtQtd.getText());
				preco = Double.parseDouble(txtPreco.getText());

				try {
					pst = con.prepareStatement("insert into produto(nome,quantidade,preco)values(?,?,?)");
					pst.setString(1, nome);
					pst.setInt(2, quantidade);
					pst.setDouble(3, preco);
					pst.executeUpdate();
					JOptionPane.showMessageDialog(null, "Salvo com Sucesso!");
					table_load();
					txtNome.setText("");
					txtQtd.setText("");
					txtPreco.setText("");
					txtNome.requestFocus();

				} catch (SQLException e1) {
					e1.printStackTrace();
				}

			}
		});
		btnNewButton.setFont(new Font("Tahoma", Font.BOLD, 14));
		btnNewButton.setBounds(10, 415, 108, 56);
		frame.getContentPane().add(btnNewButton);

		JButton btnSair = new JButton("SAIR");
		btnSair.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.exit(0);				
			}
		});
		btnSair.setFont(new Font("Tahoma", Font.BOLD, 14));
		btnSair.setBounds(304, 415, 108, 56);
		frame.getContentPane().add(btnSair);

		JButton btnNewButton_1_1 = new JButton("LIMPAR");
		btnNewButton_1_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				txtNome.setText("");
				txtQtd.setText("");
				txtPreco.setText("");
				txtNome.requestFocus();
			}
		});
		btnNewButton_1_1.setFont(new Font("Tahoma", Font.BOLD, 14));
		btnNewButton_1_1.setBounds(153, 415, 108, 56);
		frame.getContentPane().add(btnNewButton_1_1);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(422, 148, 429, 256);
		frame.getContentPane().add(scrollPane);

		table = new JTable();
		scrollPane.setViewportView(table);

		JButton btnDeletar = new JButton("DELETAR");
		btnDeletar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
							
				Integer id;
				id = Integer.parseInt(txtID.getText());

				try {
					pst = con.prepareStatement("delete from produto where id=?");
					pst.setInt(1, id);
					pst.executeUpdate();
					JOptionPane.showMessageDialog(null, "Produto Deletado!");
					table_load();
					txtNome.setText("");
					txtQtd.setText("");
					txtPreco.setText("");
					txtNome.requestFocus();

				} catch (SQLException e1) {
					e1.printStackTrace();
				}
			}
		});
		btnDeletar.setFont(new Font("Tahoma", Font.BOLD, 14));
		btnDeletar.setBounds(655, 415, 128, 56);
		frame.getContentPane().add(btnDeletar);
		
		JButton btnAtualizar = new JButton("ATUALIZAR");
		btnAtualizar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {				
				
				String nome;
				Integer quantidade;
				Double preco;
				Integer id;

				nome = txtNome.getText();
				quantidade = Integer.parseInt(txtQtd.getText());
				preco = Double.parseDouble(txtPreco.getText());
				id = Integer.parseInt(txtID.getText());

				try {
					pst = con.prepareStatement("update produto set nome=?,quantidade=?,preco=? where id=?");
					pst.setString(1, nome);
					pst.setInt(2, quantidade);
					pst.setDouble(3, preco);
					pst.setInt(4, id);
					pst.executeUpdate();
					JOptionPane.showMessageDialog(null, "Atualizado com Sucesso!");
					table_load();
					txtNome.setText("");
					txtQtd.setText("");
					txtPreco.setText("");
					txtNome.requestFocus();

				} catch (SQLException e1) {
					e1.printStackTrace();
				}
				
				
			}
		});
		btnAtualizar.setFont(new Font("Tahoma", Font.BOLD, 14));
		btnAtualizar.setBounds(490, 415, 128, 56);
		frame.getContentPane().add(btnAtualizar);
		
		JPanel panel_1 = new JPanel();
		panel_1.setBorder(new TitledBorder(null, "Pesquisar", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panel_1.setBounds(10, 482, 402, 78);
		frame.getContentPane().add(panel_1);
		panel_1.setLayout(null);
		
		JLabel lblNewLabel_1_2 = new JLabel("Produto ID");
		lblNewLabel_1_2.setBounds(10, 21, 86, 17);
		lblNewLabel_1_2.setFont(new Font("Tahoma", Font.BOLD, 14));
		panel_1.add(lblNewLabel_1_2);
		
		txtID = new JTextField();
		txtID.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				
				Integer id = Integer.parseInt(txtID.getText());
				
				try {
					
					pst = con.prepareStatement("select nome,quantidade,preco from produto where id = ?");
					pst.setInt(1, id);
				    rs = pst.executeQuery();				    
				    
				    if (rs.next()==true) {
				    	
		                String nome = rs.getString(1);
		                Integer quantidade =Integer.parseInt(rs.getString(2));
		                Double preco =Double.parseDouble(rs.getString(3));
		                
		                txtNome.setText(nome);
		                txtQtd.setText(quantidade.toString());
		                txtPreco.setText(preco.toString());		                
		            }   
				    else
		            {
		            	txtNome.setText("");
		            	txtQtd.setText("");
		                txtPreco.setText("");
		                 
		            }
							
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
		                              
		             
		}					
				
		});
		txtID.setBounds(106, 21, 227, 20);
		txtID.setColumns(10);
		panel_1.add(txtID);
	}
}
