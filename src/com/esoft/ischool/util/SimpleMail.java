package com.esoft.ischool.util;

import java.security.Security;
import java.util.Properties; 
import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
 
 
public class SimpleMail 
{
	
	public static void sendMail(String subject, String body, String sender, String recipients,
			String mailHost, final String mailUser, final String mailPassword
			) 
																				   throws Exception 
	{	
		
		Security.addProvider(new com.sun.net.ssl.internal.ssl.Provider());
		 
		Properties props = new Properties();
		props.setProperty("mail.transport.protocol", "smtp");
		props.setProperty("mail.host", mailHost);
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.port", "465");
		props.put("mail.smtp.socketFactory.port", "465");
		props.put("mail.smtp.socketFactory.class",
		"javax.net.ssl.SSLSocketFactory");
        props.put("mail.smtp.timeout", "10000");
        props.put("mail.smtp.connectiontimeout", "10000");
		props.put("mail.smtp.socketFactory.fallback", "false");
		props.setProperty("mail.smtp.quitwait", "false");
 
		Session session = Session.getDefaultInstance(props,
				new javax.mail.Authenticator() 
		{
			@Override
			protected PasswordAuthentication getPasswordAuthentication()
			{ return new PasswordAuthentication(mailUser,mailPassword);	}
		});		
 
		MimeMessage message = new MimeMessage(session);
		message.setFrom(new InternetAddress(sender));
		message.setSubject(subject);
		message.setContent(body, "text/html");
		if (recipients.indexOf(',') > 0) 
					message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipients));
		else
					message.setRecipient(Message.RecipientType.TO, new InternetAddress(recipients));
 
		
		Transport.send(message);
		
	}
	
	
	public static void main(String args[]) throws Exception
	{
		SimpleMail mailutils = new SimpleMail();
		SimpleMail.sendMail("test", "test", "panawe@gmail.com", "panawe@gmail.com,panawe@yahoo.fr","smtp.gmail.com","panawe","pass");
		
	}
	
}