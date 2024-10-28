package com.desafiowicket;

import com.desafiowicket.pages.ClientList;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.protocol.http.WebApplication;

import java.sql.Connection;

/**
 * Application object for your web application.
 * If you want to run this application without deploying, run the Start class.
 * 
 * @see com.desafiowicket.Start#main(String[])
 */
public class WicketApplication extends WebApplication
{
	private Connection connection;
	/**
	 * @see org.apache.wicket.Application#getHomePage()
	 */
	@Override
	public Class<? extends WebPage> getHomePage()
	{
		return ClientList.class;
	}

	/**
	 * @see org.apache.wicket.Application#init()
	 */
	@Override
	public void init()
	{
		super.init();

		// add your configuration here
//		connection = createConnection();
	}

//	private Connection createConnection() {
//		Class.forName()
//	}

	public Connection getConnection() {
		return connection;
	}
}
