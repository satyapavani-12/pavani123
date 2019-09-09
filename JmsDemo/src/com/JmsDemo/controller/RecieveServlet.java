package com.JmsDemo.controller;

import java.io.IOException;
import java.io.PrintWriter;

import javax.jms.Connection;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.Queue;
import javax.jms.QueueConnectionFactory;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class RecieveServlet
 */
public class RecieveServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
   
	protected void service(HttpServletRequest rq, HttpServletResponse rsp) throws ServletException, IOException
	{
		Connection connection=null;
		try
		{
			String ss=rq.getParameter("send");
			Context ctx=new InitialContext();
			Queue qu=(Queue)ctx.lookup("java:/zensarqueue");
			Destination dest=(Destination)qu;
			QueueConnectionFactory qcf=(QueueConnectionFactory)ctx.lookup("java:/ConnectionFactory");//step1
			connection=qcf.createConnection();//step2
			Session session=connection.createSession(false,Session.AUTO_ACKNOWLEDGE);//step3
			MessageConsumer consumer=session.createConsumer(dest);
			connection.start();
			PrintWriter out=rsp.getWriter();
			rsp.setContentType("text/html");
			out.println("<html><body>");
			while(true)
			{
				Message m= consumer.receive(1);
				if(m!=null)
				{
					if(m instanceof TextMessage)
					{
						TextMessage message=(TextMessage)m;
						out.println("Reading message:"+message.getText());
						
					}else {
						break;
					}
				}
			}
                  out.println("to send message please <a href=reg.html>click here</a>");
			
			         out.println("</body></html>");
			
				
			
			
			
		}
		catch(Exception e) {
			System.err.println("Exception occured" + e.toString());
		}
		finally {
			try {
				connection.close();
			}catch(JMSException e) {
				e.printStackTrace();
			}
		}
	}
}
	



	


