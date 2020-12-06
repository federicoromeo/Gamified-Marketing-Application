package services;

import javax.ejb.Stateless;

@Stateless(name = "TestServiceEJB")
public class TestServiceBean
{
    public TestServiceBean()
    {

    }

    public void testMethod()
    {
        System.out.println("I'm working just fine.");
    }
}
