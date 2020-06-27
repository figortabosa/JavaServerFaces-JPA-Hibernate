package manageBean;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import dao.DaoTelefones;
import dao.DaoUsuario;
import model.TelefoneUser;
import model.UsuarioPessoa;

@ManagedBean(name = "telefoneManagedBean")
@ViewScoped
public class TelefoneManagedBean {

private UsuarioPessoa user = new UsuarioPessoa();	
private DaoUsuario<UsuarioPessoa> daoUser = new DaoUsuario<UsuarioPessoa>();
private DaoTelefones<TelefoneUser> daoTelefone = new DaoTelefones<TelefoneUser>();
private TelefoneUser Telefone = new TelefoneUser();
	
@PostConstruct	
public void init () {
	String coduser = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("codigouser");	
	user = daoUser.pesquisar(Long.parseLong(coduser), UsuarioPessoa.class); /* Recarrega os objetos cadastrados na tela*/
	
	}

	public String salvar() {
		Telefone.setUsuarioPessoa(user); /*Associa o Telefone a entidade pai*/
		daoTelefone.salvar(Telefone);
		Telefone = new TelefoneUser();
		user = daoUser.pesquisar(user.getId(), UsuarioPessoa.class); /* Recarrega os objetos cadastrados na tela*/
		FacesContext.getCurrentInstance().addMessage(null, 
				new FacesMessage(FacesMessage.SEVERITY_INFO, "Informação: ", "salvo com Sucesso"));
		return "";
	}
	
	public String removeTelefone() throws Exception {
		daoTelefone.deletarPorId(Telefone);
		user = daoUser.pesquisar(user.getId(), UsuarioPessoa.class); /* Recarrega os objetos cadastrados na tela*/
		FacesContext.getCurrentInstance().addMessage(null, 
				new FacesMessage(FacesMessage.SEVERITY_INFO, "Informação: ", "Telefone removido com Sucesso"));
		Telefone = new TelefoneUser();
		return "";
	}

	public UsuarioPessoa getUser() {
		return user;
	}
	
	public void setUser(UsuarioPessoa user) {
		this.user = user;
	}

	public DaoTelefones<TelefoneUser> getDaoTelefone() {
		return daoTelefone;
	}

	public void setDaoTelefone(DaoTelefones<TelefoneUser> daoTelefone) {
		this.daoTelefone = daoTelefone;
	}

	public TelefoneUser getTelefone() {
		return Telefone;
	}

	public void setTelefone(TelefoneUser telefone) {
		Telefone = telefone;
	}

	
}
