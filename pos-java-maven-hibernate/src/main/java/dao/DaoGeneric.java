package dao;

import java.io.Serializable;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

import posjavamavenhibernate.HibernateUtil;

public class DaoGeneric<E>{

	
	private EntityManager entityManager = HibernateUtil.getEntityManager();

	public void salvar(E entidade) {
		EntityTransaction transaction = entityManager.getTransaction();
		transaction.begin();
		entityManager.persist(entidade);
		transaction.commit();
	}

	public E updateMerger(E entidade) {
		EntityTransaction transaction = entityManager.getTransaction();
		transaction.begin();
		E entidadesalva = entityManager.merge(entidade);
		transaction.commit();

		return entidadesalva;
	}

	public E pesquisar(E entidade) { /* PRIMEIRA FORMA DE PESQUISA */
		Object id = HibernateUtil.getPrimaryKey(entidade);

		E e = (E) entityManager.find(entidade.getClass(), id);
		return e;
	}

	public E pesquisar(Long id, Class<E> entidade) { /* SEGUNDA FORMA DE PESQUISA DIREETAMENTE POR PARAMETRO */
		entityManager.clear();
		E e = (E) entityManager.createQuery("from " + entidade.getSimpleName() + " where id = " + id).getSingleResult();
		return e;
	}

	public void deletarPorId(E entidade) throws Exception{

		Object id = HibernateUtil.getPrimaryKey(entidade); /*obtem o ID do objeto PK*/
		EntityTransaction transaction = entityManager.getTransaction(); /* Objeto de transação*/
		transaction.begin(); /* Começa uma transação*/
		entityManager.createNativeQuery(
				"delete from"+ " " + entidade.getClass().getSimpleName().toLowerCase()+ " " + "where id =" + id).executeUpdate();
		transaction.commit();
	}
	
	public List<E> listar(Class<E> entidade) {
		EntityTransaction transaction = entityManager.getTransaction();
		transaction.begin();
		
		List<E> lista = entityManager.createQuery("from" + " " + entidade.getName()).getResultList();
		
		transaction.commit();
		
		return lista;
	}
	
	public EntityManager getEntityManager() {
		return entityManager;
	}
}
