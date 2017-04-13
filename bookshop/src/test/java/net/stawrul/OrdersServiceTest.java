package net.stawrul;

import net.stawrul.model.Book;
import net.stawrul.model.Order;
import net.stawrul.services.OrdersService;
import net.stawrul.services.exceptions.OutOfStockException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import javax.persistence.EntityManager;
import junit.framework.Assert;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;

@RunWith(MockitoJUnitRunner.class)
public class OrdersServiceTest {

    @Mock
    EntityManager em;

   @Test(expected = OutOfStockException.class)
   public void whenOrderedBookNotAvailable_placeOrderThrowsOutOfStockEx() {
        //Arrange
        Order order = new Order();
        Book book = new Book();
        book.setAmount(0);
        order.getBooks().add(book);

        Mockito.when(em.find(Book.class, book.getId())).thenReturn(book);

        OrdersService ordersService = new OrdersService(em);

        //Act
       
        ordersService.isAmountOfBookCorrect(order);
        //Assert - exception expected
        
    }
   
   @Test(expected = OutOfStockException.class)
   public void whenOrderedBookHasToLowAmount_placeOrderThrowsOutOfStockEx() {
        //Arrange
        Order order = new Order();
        Book book = new Book();
        book.setAmount(1);
        book.setPrize(30.0);
        order.getBooks().add(book);

        Mockito.when(em.find(Book.class, book.getId())).thenReturn(book);

        OrdersService ordersService = new OrdersService(em);

        //Act
        
        ordersService.isPrizeCorrect(order);
        //Assert - exception expected
       
    }
   
   @Test(expected = OutOfStockException.class)
   public void whenOrderedBookIsEmpty_placeOrderThrowsOutOfStockEx() {
        //Arrange
        Order order = new Order();

        OrdersService ordersService = new OrdersService(em);
        //Act
       
       ordersService.isNotEmpty(order);
        //Assert - exception expected
       
    }
   
    @Test
    public void whenOrderedBookAvailable_placeOrderDecreasesAmountByOne() {
        //Arrange
        Order order = new Order();
        Book book = new Book();
        book.setAmount(1);
        book.setPrize(60.0);
        order.getBooks().add(book);

        Mockito.when(em.find(Book.class, book.getId())).thenReturn(book);

        
        OrdersService ordersService = new OrdersService(em);
        ordersService = Mockito.spy(ordersService);
        
        Mockito.doReturn(true).when(ordersService).isAmountOfBookCorrect(order);
        
        //Act
        ordersService.placeOrder(order);

        //Assert
        //dostępna liczba książek zmniejszyła się:
        assertEquals(0, (int)book.getAmount());
        //nastąpiło dokładnie jedno wywołanie em.persist(order) w celu zapisania zamówienia:
        Mockito.verify(em, times(1)).persist(order);
    }
     @Test
    public void whenOrderedBooksAvailable_placeCorectAmountOfBook() {
        //Arrange
        Order order = new Order();
        Book book1 = new Book();
        book1.setAmount(1);
        book1.setPrize(60.0);
        Book book2 = new Book();
        book2.setAmount(5);
        book2.setPrize(12.0);
        Book book3 = new Book();
        book3.setAmount(6);
        book3.setPrize(11.0);
        Book book4 = new Book();
        book4.setAmount(2);
        book4.setPrize(13.0);
        
        
        order.getBooks().add(book1);
        order.getBooks().add(book2);
        order.getBooks().add(book2);
        order.getBooks().add(book3);
        order.getBooks().add(book4);
        order.getBooks().add(book4);

        Mockito.when(em.find(Book.class, book1.getId())).thenReturn(book1);
        Mockito.when(em.find(Book.class, book2.getId())).thenReturn(book2);
        Mockito.when(em.find(Book.class, book3.getId())).thenReturn(book3);
        Mockito.when(em.find(Book.class, book4.getId())).thenReturn(book4);

        OrdersService ordersService = new OrdersService(em);

        //Act
        ordersService.placeOrder(order);

        //Assert
        //dostępna liczba książek zmniejszyła się:
        assertEquals(0, (int)book1.getAmount());
        assertEquals(3, (int)book2.getAmount());
        assertEquals(5, (int)book3.getAmount());
        assertEquals(0, (int)book4.getAmount());
        //nastąpiło dokładnie jedno wywołanie em.persist(order) w celu zapisania zamówienia:
        Mockito.verify(em, times(1)).persist(order);
    }
    @Test
    public void whenGivenLowercaseString_toUpperReturnsUppercase() {

        //Arrange
        String lower = "abcdef";

        //Act
        String result = lower.toUpperCase();

        //Assert
        assertEquals("ABCDEF", result);
    }
}
