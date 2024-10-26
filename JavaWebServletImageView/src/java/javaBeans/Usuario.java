package javaBeans;
import com.mysql.cj.jdbc.Blob;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.Base64;

public class Usuario extends Conectar {
    public int pkuser;
    public String nome;
    public String email;
    public String celular;
    public String senha;
    public String nivel;

    public InputStream foto;// Campo de armazenamento da imagem
    public long tamanho ; 
    public String imagemBase64; // Armazenará o caminho da imagem puxada do banco

    public boolean getLogin() {
        this.statusSQL = null;
        try {
            sql = "select * from usuarios where ucase(trim(email)) = ucase(trim(?)) and ucase(trim(senha)) = ucase(trim(?))";
            ps = con.prepareStatement(sql); // prepara SQL
            ps.setString(1, email); // Configura Parametros
            ps.setString(2, senha); // Configura Parametros
            tab = ps.executeQuery(); // Executa comando SQL
            if (tab.next()) {
                nome = tab.getString("nome");
                email = tab.getString("email");
                pkuser = tab.getInt("pkuser");
                nivel = tab.getString("nivel");

                Blob blob = (Blob) tab.getBlob("foto");
                if (blob == null) {
                    imagemBase64 = null;
                } else {
                    byte[] imageBytes = blob.getBytes(1, (int) blob.length());
                    imagemBase64 = Base64.getEncoder().encodeToString(imageBytes);
                }               
             
                return true;
            }
            if (email.equals(userMaster) && senha.equals(senhaMaster)) {
                nome = "";
                email = "";
                pkuser = 0;
                nivel = nivelMaster;

                return true;
            }
        } catch (SQLException ex) {
            this.statusSQL = "Erro ao tentar buscar Usuário! " + ex.getMessage();
        }
        return false;
    }

    public void incluir() {
        try {
            sql = "insert into usuarios ( nome, email, celular, senha, nivel, foto ) "
                    + "values (?,?,?,?,?,?) ";

            ps = con.prepareStatement(sql); // prepara SQL
            ps.setString(1, nome); // Configura Parametros
            ps.setString(2, email); // Configura Parametros
            ps.setString(3, celular); // Configura Parametros
            ps.setString(4, senha); // Configura Parametros
            ps.setString(5, nivel); // Configura Parametros
            ps.setBlob(6, foto, tamanho);

            ps.executeUpdate(); // executa comando SQL
            this.statusSQL = null; // armazena null se deu tudo certo
        } catch (SQLException ex) {
            this.statusSQL = "Erro ao incluir usuario ! <br> " + ex.getMessage();
        }
    }

    public void alterar() {
        try {
            sql = "update usuarios set nome=?, email=?, celular=?,"
                    + " senha=?, nivel=?, foto=? where ucase(trim(email))=ucase(trim(?)) ";

            ps = con.prepareStatement(sql); // prepara SQL
            ps.setString(1, nome); // Configura Parametros
            ps.setString(2, email); // Configura Parametros
            ps.setString(3, celular); // Configura Parametros
            ps.setString(4, senha); // Configura Parametros
            ps.setString(5, nivel); // Configura Parametros

            ps.setBlob(6, foto, tamanho); // Adiciiona a foto

            ps.setString(7, email); // Configura Parametros

            ps.executeUpdate(); // executa comando SQL
            this.statusSQL = null; // armazena null se deu tudo certo
        } catch (SQLException ex) {
            this.statusSQL = "Erro ao Alterar usuario ! <br> " + ex.getMessage();
        }
    }

    public void deletar() {
        try {
            sql = "delete from usuarios where email = ? ";
            ps = con.prepareStatement(sql); // prepara SQL
            ps.setString(1, email); // Configura Parametros
            ps.executeUpdate();
            this.statusSQL = null; // armazena null se deu tudo certo
        } catch (SQLException ex) {
            this.statusSQL = sql + " <br> Erro ao deletar usuario ! <br> " + ex.getMessage();
        }
    }

    public void gravar() {
        try {
            sql = "select * from usuarios where ucase(trim(email)) = ucase(trim(?))";
            ps = con.prepareStatement(sql); // prepara SQL
            ps.setString(1, email); // Configura Parametros
            tab = ps.executeQuery();
            if (tab.next()) {
                alterar();
            } else {
                incluir();
            }
            this.statusSQL = null;
        } catch (SQLException ex) {
            this.statusSQL = sql + " <br> Erro ao gravar o registro ! <br> " + ex.getMessage();
        }
    }

    public boolean buscarEmail() {
        try {
            sql = "select * from usuarios where ucase(trim(email)) = ucase(trim(?))";
            ps = con.prepareStatement(sql); // prepara SQL
            ps.setString(1, email); // Configura Parametros
            tab = ps.executeQuery();
            if (tab.next()) {
                pkuser = tab.getInt("pkuser");
                nome = tab.getString("nome");
                celular = tab.getString("celular");
                senha = tab.getString("senha");
                nivel = tab.getString("nivel");

               Blob blob = (Blob) tab.getBlob("foto");
                if (blob == null) {
                    imagemBase64 = null;
                } else {
                    byte[] imageBytes = blob.getBytes(1, (int) blob.length());
                    imagemBase64 = Base64.getEncoder().encodeToString(imageBytes);
                }

                return true;
            }
            this.statusSQL = null; // armazena null se deu tudo certo
        } catch (SQLException ex) {
            this.statusSQL = sql + " <br> Erro ao deletar usuario ! <br> " + ex.getMessage();
        }
        return false;
    }

}
