package manageBean;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.AjaxBehaviorEvent;

import com.google.gson.Gson;

import dao.DaoUsuario;
import model.UsuarioPessoa;

@ManagedBean(name = "usuarioPessoaManageBean")
@ViewScoped
public class UsuarioPessoaManageBean {

	
	private UsuarioPessoa usuarioPessoa = new UsuarioPessoa();
	
	private List<UsuarioPessoa> list = new ArrayList<UsuarioPessoa>();
	private DaoUsuario<UsuarioPessoa> daoGeneric = new DaoUsuario<UsuarioPessoa>();
	
	@PostConstruct
	public void init() {
		list = daoGeneric.listar(UsuarioPessoa.class);
	}
	
	public UsuarioPessoa getUsuarioPessoa() {
		return usuarioPessoa;
	}
	public void setUsuarioPessoa(UsuarioPessoa usuarioPessoa) {
		this.usuarioPessoa = usuarioPessoa;
	}
	
	public void pesquisarCep(AjaxBehaviorEvent event) {
		try {
			URL url = new URL("https://viacep.com.br/ws/"+usuarioPessoa.getCep()+"/json/");
			URLConnection urlconnection = url.openConnection();
			InputStream is = urlconnection.getInputStream();
			BufferedReader br = new BufferedReader(new InputStreamReader(is,"UTF-8"));
			
			String cep = "";
			StringBuilder jsonCep = new StringBuilder();
			
			while((cep = br.readLine()) != null) {
				jsonCep.append(cep);
			}
			UsuarioPessoa userCepPessoa = new Gson().fromJson(jsonCep.toString(), UsuarioPessoa.class);
				usuarioPessoa.setCep(userCepPessoa.getCep());
				usuarioPessoa.setLogradouro(userCepPessoa.getLogradouro());
				usuarioPessoa.setComplemento(userCepPessoa.getComplemento());
				usuarioPessoa.setBairro(userCepPessoa.getBairro());
				usuarioPessoa.setLocalidade(userCepPessoa.getLocalidade());
				usuarioPessoa.setUf(userCepPessoa.getUf());
				usuarioPessoa.setUnidade(userCepPessoa.getUnidade());
				usuarioPessoa.setIbge(userCepPessoa.getIbge());
				usuarioPessoa.setGia(userCepPessoa.getGia());
			
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public String salvar() {
		daoGeneric.salvar(usuarioPessoa);
		list.add(usuarioPessoa);
		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Informaçao: ","Salvo com Sucesso!"));
		return "";
	}
	
	public String Novo() {
		usuarioPessoa = new UsuarioPessoa();
		return "";
	}
	
	public List<UsuarioPessoa> getList() {
		
		return list;
	}
	
	public String remover() {
		
		try {
		daoGeneric.removerUsuario(usuarioPessoa);
		list.remove(usuarioPessoa);
		usuarioPessoa = new UsuarioPessoa();
		FacesContext.getCurrentInstance().addMessage(null, 
				new FacesMessage(FacesMessage.SEVERITY_INFO, "Informação: ", "Removel com Sucesso"));
		usuarioPessoa = new UsuarioPessoa();
		
		}catch (Exception e) {
			if(e.getCause() instanceof org.hibernate.exception.ConstraintViolationException) {
				FacesContext.getCurrentInstance().addMessage(null, 
						new FacesMessage(FacesMessage.SEVERITY_INFO,
								"Informação: ", "Existem telefones para usuário"));
			}else {
				e.printStackTrace();
			}
		}
			
		return "";
	}
	
}
