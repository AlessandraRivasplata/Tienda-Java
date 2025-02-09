package dao;
import model.Employee;
import model.Product;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;


import java.time.LocalDateTime;
import java.util.ArrayList;
import model.ProductHistory;

public class DaoImplHibernate implements Dao {
	
	  private SessionFactory sessionFactory;

	  public DaoImplHibernate() {
	        connect();
	    }
	  
	  @Override
	    public void connect() {
	        try {
	           
	            Configuration configuration = new Configuration().configure("hibernate.cfg.xml");
	            sessionFactory = configuration.buildSessionFactory();
	        } catch (Exception e) {
	            e.printStackTrace();
	            throw new RuntimeException("Error al conectar con la base de datos.");
	        }
	    }


	    @Override
	    public Employee getEmployee(int employeeId, String password) {
	        try (Session session = sessionFactory.openSession()) {
	            String hql = "FROM Employee WHERE employeeId = :employeeId AND password = :password";
	            return session.createQuery(hql, Employee.class)
	                    .setParameter("employeeId", employeeId)
	                    .setParameter("password", password)
	                    .uniqueResult();
	        } catch (Exception e) {
	            e.printStackTrace();
	            return null;
	        }
	    }


	    @Override
	    public ArrayList<Product> getInventory() {
	        try (Session session = sessionFactory.openSession()) {
	            return (ArrayList<Product>) session.createQuery("FROM Product", Product.class).list();
	        } catch (Exception e) {
	            e.printStackTrace();
	            return new ArrayList<>();
	        }
	    }





	    @Override
	    public void addProduct(Product product) {
	        Transaction transaction = null;
	        try (Session session = sessionFactory.openSession()) {
	            transaction = session.beginTransaction();
	            session.save(product);
	            transaction.commit();
	        } catch (Exception e) {
	            if (transaction != null) {
	                transaction.rollback();
	            }
	            e.printStackTrace();
	        }
	    }


	    @Override
	    public void updateProduct(Product product) {
	        Transaction transaction = null;
	        try (Session session = sessionFactory.openSession()) {
	            transaction = session.beginTransaction();
	            session.update(product);
	            transaction.commit();
	        } catch (Exception e) {
	            if (transaction != null) {
	                transaction.rollback();
	            }
	            e.printStackTrace();
	        }
	    }


	    @Override
	    public void deleteProduct(int productId) {
	        Transaction transaction = null;
	        try (Session session = sessionFactory.openSession()) {
	            transaction = session.beginTransaction();
	            Product product = session.get(Product.class, productId);
	            if (product != null) {
	                session.delete(product);
	            }
	            transaction.commit();
	        } catch (Exception e) {
	            if (transaction != null) {
	                transaction.rollback();
	            }
	            e.printStackTrace();
	        }
	    }


		  @Override
		  public boolean writeInventory(ArrayList<Product> products) {
		      Session session = sessionFactory.getCurrentSession();
		      Transaction transaction = session.beginTransaction();
		      

		      try {
		          for (Product product : products) {
		              ProductHistory productHistory = new ProductHistory();
		              productHistory.setProductId(product.getId());
		              productHistory.setName(product.getName());
		              productHistory.setPrice(product.getWholesalerPrice().getValue());
		              productHistory.setAvailable(product.isAvailable());
		              productHistory.setStock(product.getStock());
		              productHistory.setCreatedAt(LocalDateTime.now());
		         
		              session.save(productHistory);
		          }

		          transaction.commit();
		          return true;
		      } catch (Exception e) {
		          e.printStackTrace();
		          if (transaction != null) {
		              transaction.rollback();
		            
		          }
		          return false;
		      }



		  }
		  @Override
		    public void disconnect() {
		       
		    }
}
